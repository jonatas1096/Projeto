package com.example.projeto.views

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.projeto.R
import com.example.projeto.datasource.UserData
import com.example.projeto.layoutsprontos.arrowVoltar
import com.example.projeto.layoutsprontos.loadImage
import com.example.projeto.ui.theme.Dongle
import com.example.projeto.viewmodel.PublicacaoViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.TimeZone
import kotlinx.coroutines.coroutineScope
import java.util.*


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Publicar(navController: NavController, viewModel: PublicacaoViewModel = hiltViewModel()) {
    ////////////////////////////////

    //toda a palhaçada do jetpack só pra abrir o bottomshet
    val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)
    val scope = rememberCoroutineScope()
    //
    //
    val context = LocalContext.current


    //unica forma que eu consegui pra abrir a galeria sendo uma função composable
    var galeriaState by remember { mutableStateOf(false) }
    //a lista das imagens p ser exibidas
    val imagensColuna = remember { mutableStateListOf<Bitmap>() }

    //Iniciar o processo de publicação
    var publicacaoState by remember { mutableStateOf(false) }

    //Máximo caracteres titulo
    val maxCaracteresTitulo = 35


    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = { //essa parte do sheetContent é a parte de baixo (kkkkkk vai entender)
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(240.dp))
            {
                loadImage(path = "",
                    contentDescription = "Plano de fundo do bottomsheet",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 10.dp)
                        .padding(end = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    //botão para subir o bottomsheet
                    IconButton(onClick = {
                        scope.launch {
                            if (sheetState.isCollapsed){
                                sheetState.expand()
                            }
                            else{
                                sheetState.collapse()
                            }

                        }
                    }) {
                        Image(ImageVector.vectorResource(id = R.drawable.ic_minus),
                            contentDescription = "Subir o BottomSheet",
                            modifier = Modifier
                                .size(80.dp),
                            colorFilter = ColorFilter.tint(Color(0xFFC5C4C4))
                        )
                    }


                    //Midias
                    Row(

                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.Red)
                            .clickable(
                                onClick = {
                                    galeriaState = true
                                })
                    ) {
                        //Imagem da imagem
                        Image(ImageVector.vectorResource(id = R.drawable.ic_imagem),
                            contentDescription = "Adicionar foto ou video",
                            modifier = Modifier
                                .size(38.dp)
                            /*colorFilter = ColorFilter.tint(Color(0xFFC5C4C4)
                            )*/
                        )

                        //Só pra espaçar um pouco a imagem e o texto
                        Spacer(modifier = Modifier
                            .width(20.dp)
                            .border(2.dp, Color.Green))

                        //Texto da Imagem
                        Text(text = "Foto/vídeo",
                            fontSize = 25.sp,
                            color = Color(0xFF303030),
                        )

                    }

                }


            }
        },
        sheetBackgroundColor = Color(0xFFFFFFFF),
        sheetShape = RoundedCornerShape(25.dp, 25.dp,0.dp, 0.dp),
        sheetElevation = 8.dp,
    )
    {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(243, 243, 243, 255))
                .padding(horizontal = 4.dp)
        ) {

            //Começo constraint layout.
            //Eu vou começar por ele pra que as coisas que eu posicionar aqui tenham um menor hierarquia nas camadas em geral.
            //Tô optando por ele porque a forma padrão tava bugando dms
            val (arrow, areaPublicar, areaTexto,areaTitulo, boxImagem) = createRefs()

            var titulo by remember { mutableStateOf("") }
            var texto by remember { mutableStateOf("") }


            // Arrow voltar (seta que volta)
            arrowVoltar(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier
                    .constrainAs(arrow) {
                        start.linkTo(parent.start, margin = 5.dp)
                        top.linkTo(parent.top, margin = 10.dp)
                    }
                    .size(35.dp),
                color = Color(0xFF000000)
            )
            Box(
                modifier = Modifier
                    .constrainAs(areaPublicar) {
                        top.linkTo(parent.top)
                    }
                    .fillMaxWidth()
                    .size(50.dp)

            ) {
                //Parte do criar publicação e enviar com o Publicar
                Row(
                    Modifier
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Criar Publicação",
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Dongle,
                        modifier = Modifier.padding(top = 5.dp)
                    )
                    if (titulo.isEmpty() || texto.isEmpty()){
                        Button(
                            onClick = {
                                Toast.makeText(context,"O título e o texto da publicação são obrigatórios!",Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFFE7E6E6)
                            ),
                            modifier = Modifier
                                .padding(26.dp, 5.dp, 20.dp, 5.dp)
                        ) {
                            Text(text = "Publicar",
                                color = Color(0xFFBDBBBB))
                        }
                    }
                    else if(titulo.length > maxCaracteresTitulo){
                        Button(
                            onClick = {
                                Toast.makeText(context,"Título excedeu o tamanho limite de caracteres.",Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFFE7E6E6)
                            ),
                            modifier = Modifier
                                .padding(26.dp, 5.dp, 20.dp, 5.dp)
                        ) {
                            Text(text = "Publicar",
                                color = Color(0xFFBDBBBB))
                        }
                    }
                    else{
                        Button(
                            onClick = {
                                publicacaoState = true
                            },
                            colors = ButtonDefaults.buttonColors(
                                Color(0xFFB8D2FF)
                            ),
                            modifier = Modifier
                                .padding(26.dp, 5.dp, 20.dp, 5.dp)
                        ) {
                            Text(text = "Publicar",
                                color = Color(0xFF005CFA),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                }
            }
            //Area do título
            OutlinedTextField(
                value = titulo,
                onValueChange = {
                    titulo = it
                },
                label = {
                    Text(text = "Título da publicação",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = Color(0xFFB8D2FF),
                    focusedLabelColor = Color(0xFF226EF0),
                    unfocusedLabelColor = Color(0xFF6492E0),
                    focusedBorderColor = Color(0xFF226EF0),
                    unfocusedBorderColor = Color(0xFF6492E0)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(areaTitulo) {
                        top.linkTo(parent.top, margin = 50.dp)
                    }
                    .height(60.dp)
            )


            //Area para escrever o texto da publicação
            OutlinedTextField(
                value = texto,
                onValueChange = {
                    texto = it
                },
                label = {
                        Text(text = "Digite algo sobre a sua publicação aqui")
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = Color(0xFFFAFAFA),
                    focusedLabelColor = Color(0xFF226EF0),
                    focusedBorderColor = Color(0xFF226EF0)

                ),
                modifier = Modifier
                    .constrainAs(areaTexto) {
                        top.linkTo(areaTitulo.bottom)
                    }
                    .fillMaxWidth()
                    .height(150.dp)
            )


            //Área que mostra as imagens
            Card(
                modifier = Modifier
                    .constrainAs(boxImagem) {
                        top.linkTo(areaTexto.bottom, margin = 15.dp)
                    }
                    .fillMaxWidth()
                    .height(220.dp),
                elevation = 8.dp
            )
            {
                if (imagensColuna.isEmpty()){
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        loadImage(
                            path = "https://i.imgur.com/avyQZS0.jpg",
                            contentDescription = "Corujinha",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier)
                    }
                }
                else{
                    LazyColumn(
                        modifier = Modifier
                            .constrainAs(boxImagem) {
                                top.linkTo(areaTexto.bottom, margin = 15.dp)
                            }
                            .fillMaxWidth()
                            .height(220.dp)
                    ) {
                        items(imagensColuna) { imagem ->
                            println("executou $galeriaState")
                            Image(
                                bitmap = imagem.asImageBitmap(),
                                contentDescription = "Imagem Selecionada",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .fillMaxSize()
                                    //Esse modificador abaixo serviu para adequar a imagem totalmente à coluna.
                                    .aspectRatio(
                                        ratio = imagem.width.toFloat() / imagem.height.toFloat(),
                                        matchHeightConstraintsFirst = false
                                    )

                            )
                        }


                    }
                }
            }




            //}

            //Fim Constraint.
            //

            if (galeriaState) {
                println("chamou a função $galeriaState")
                SelecionarImagem{ imagem ->
                    if (imagem != null) {
                        imagensColuna.add(imagem)
                        println("Adicinou a imagem.")
                        galeriaState = false
                    }
                }
            }

            if (publicacaoState){
                println("Chamou a função de publicacao = $publicacaoState")

                CriarPublicacao(
                    foto =   UserData.imagemUrl,
                    nome =  UserData.nomeEncontrado,
                    titulo =  titulo,
                    texto =  texto,
                    imagensPublicacao = imagensColuna,
                    navController = navController
                )
            }
        }


    }



}

//Função para abrir a galeria e selecionar imagens
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SelecionarImagem(onImageSelected: (Bitmap?) -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        imageUri = it
    }

    LaunchedEffect(Unit) {
        scope.launch {
            launcher.launch("image/*")
        }
    }

    DisposableEffect(imageUri) {
        if (imageUri != null) {
            val bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, imageUri!!)
                ImageDecoder.decodeBitmap(source)
            }
            onImageSelected(bitmap)
        }
        onDispose {
            // Serve para executar alguma coisa como limpeza quando a execução acaba, nao sei como usar
        }
    }
}




//Nesta parte fica a função que vai coletar os dados daqui e mandar para o firebase com os dados da nova publicação.
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun CriarPublicacao(foto: String, nome:String, titulo:String, texto:String, imagensPublicacao: List<Bitmap>, navController: NavController){


    // A instância do firebase firestore (vou usar para os dados normais, nome, titulo e texto):
    val firestore = Firebase.firestore

    // A instância do firebase storage e as variáveis que vou precisar (vou subir as fotos da publicação):
    val storage = Firebase.storage
    val storageRef = storage.reference
    val alunoRM = UserData.rmEncontrado
    val cpsID = UserData.cpsIDEncontrado

    //abaixo uma formatação que vai ser a chave para que os usuários não sobreescrevam as próprias publicações.
    val timeZone = TimeZone.getTimeZone("America/Sao_Paulo")
    val sdf = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
    sdf.timeZone = timeZone

    val dataHoraAtual = Calendar.getInstance(timeZone).time
    val formatoFinal = sdf.format(dataHoraAtual)
    //////////////////
    val context = LocalContext.current
    //



    //Começo da lógica do post:
    LaunchedEffect(Unit) {
        if (alunoRM.isEmpty()) { //se o alunoRM estiver vazio, entendemos que é um professor que está tentando fazer uma postagem:

            /////////////
            //1 - Parte para mandar as imagens para o storage
            val cpsPastaRef = storageRef.child("$cpsID/$formatoFinal")
            println(formatoFinal)

            val referenciaHora = formatoFinal //nao sei se vou usar mais
            coroutineScope {
                if (imagensPublicacao != null) {
                    for ((index, imagem) in imagensPublicacao.withIndex()) {
                        val caminhoImagem =
                            "$cpsPastaRef/imagem$index.jpg" // Caminho exclusivo para cada imagem

                        val byteArrayOutputStream = ByteArrayOutputStream()
                        imagem.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                        val bytes = byteArrayOutputStream.toByteArray()

                        val imagemRef = storageRef.child(caminhoImagem)

                        try {
                            // Upload da imagem
                            imagemRef.putBytes(bytes).await()
                            println("Upload da imagem $index concluído.")
                        } catch (e: Exception) {
                            println("Erro ao fazer upload da imagem $index: $e")
                            // Lidar com o erro, se necessário
                        }
                    }

                }
            }
            // As imagens foram enviadas para o storage
            println("proximo passo")
            /////////////

            /////////////
            //Agora os demais dados do post
            val postagensColecao = firestore.collection("Postagens")

            //Primeiro buscando as imagens que acabei de subir anteriormente para o storage
            //vamos armazenar o link de todas elas dentro do imagensUrls:
            coroutineScope {
                val imagensUrls = mutableListOf<String>()
                println("Valor da lista: $imagensUrls")

                val pastaImagens = storageRef.child("/gs:/tcc-projeto-f3873.appspot.com/$cpsID/$formatoFinal")
                val totalImagens = pastaImagens.listAll().await().items.size //pegando o total de imagens que subiu para o storage
                var imagesEnviadasComSucesso = 0 //essa variavel vai servir para "barrar" o código de continuar até que de fato as urls sejam obtidas e subam corretamente

                pastaImagens.listAll()
                    .addOnSuccessListener { ImagensEncontradas ->
                        println("Encontrou a pasta")
                        for (item in ImagensEncontradas.items) {
                            item.downloadUrl.addOnSuccessListener { uri ->
                                val url = uri.toString()
                                println("Printando a url $url")
                                imagensUrls.add(url)
                                imagesEnviadasComSucesso++ //faz parte da lógica de barrar o código


                                //Esse bloco só vai ser executado caso as  duas variaveis tenham o mesmo valor
                                //Ou seja, se o total de imagens for igual as imagens que foram enviadas, tudo está perfeitamente bem.
                                if (imagesEnviadasComSucesso == totalImagens) {
                                    println("Imagens enviadas: $imagensUrls, total de imagens $totalImagens")
                                    val usuarioPostagem = hashMapOf(
                                        "fotoPerfil" to UserData.imagemUrl,
                                        "nome" to nome,
                                        "cpsID" to UserData.cpsIDEncontrado,
                                        "titulo" to titulo,
                                        "texto" to texto,
                                        "imagensPostagem" to imagensUrls, //(lista de urls)
                                        "ultimaAtualizacao" to FieldValue.serverTimestamp() // Adiciona a data/hora da atualização
                                    )


                                    postagensColecao.document(titulo)
                                        .set(usuarioPostagem)
                                        .addOnSuccessListener {
                                            println("Dentro do on listener: $imagensUrls")
                                            println("Subiu para o Firestore com caminho de documento personalizado: Postagens/$titulo")
                                            Toast.makeText(context,"Publicação enviada com sucesso!",Toast.LENGTH_SHORT).show()
                                            navController.navigate("Index")
                                        }
                                        .addOnFailureListener { erro ->
                                            println("Erro ao adicionar documento: $erro")
                                        }
                                }
                            }
                                .addOnFailureListener { erro ->
                                    println("Erro ao entrar na pasta $erro")
                                }
                        }
                    }
            }


         }
        }
}

