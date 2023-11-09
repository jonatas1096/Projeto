package com.connectstudent.projeto.views

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.connectstudent.projeto.R
import com.connectstudent.projeto.datasource.UserData
import com.connectstudent.projeto.layoutsprontos.arrowVoltar
import com.connectstudent.projeto.layoutsprontos.loadImage
import com.connectstudent.projeto.turmasItens.turmasItem
import com.connectstudent.projeto.ui.theme.Dongle
import com.connectstudent.projeto.ui.theme.Jomhuria
import com.connectstudent.projeto.ui.theme.LARANJA
import com.connectstudent.projeto.viewmodel.PublicacaoViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.TimeZone
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
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
    //
    val scroll = rememberScrollState()

    //unica forma que eu consegui pra abrir a galeria sendo uma função composable
    var galeriaState by remember { mutableStateOf(false) }
    //a lista das imagens p ser exibidas
    val imagensColuna = remember { mutableStateListOf<Bitmap>() }

    //Iniciar o processo de publicação
    var publicacaoState by remember { mutableStateOf(false) }


    //Iniciar o processo de marcar turmas
    var adicionarTurmaState by remember { mutableStateOf(false) }
    var selecaoTurmas: List<String> by remember { mutableStateOf(emptyList()) } //a lista das turmas em si
    var operacaoConcluida by remember { mutableStateOf(false) } //gambiarra pra nao bugar

    //Lógica da box de loading
    var loadingState by remember{ mutableStateOf(false) }

    //Imagem do perfil em tempo real
    val storage = Firebase.storage
    var imagemPerfil = remember{ mutableStateOf("") }
    val imagemPadrao = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/imagemdefault.jpg"
    var check by remember{ mutableStateOf(false) }

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                try {
                    if (!UserData.rmEncontrado.isNullOrEmpty()) { //nao está vazio.

                    val storageRefAluno = storage.reference.child("Alunos/Fotos de Perfil/${UserData.rmEncontrado}")
                    val uri = storageRefAluno.downloadUrl.await()
                    imagemPerfil.value = uri.toString()
                    check = true
                    }else{
                        val storageRefAluno = storage.reference.child("CPS/Fotos de Perfil/${UserData.cpsIDEncontrado}")
                        val uri = storageRefAluno.downloadUrl.await()
                        imagemPerfil.value = uri.toString()
                        check = true
                    }

                } catch (downloadException: Exception) {
                    println("Erro ao obter uma URL de foto de perfil.")
                    println("Vamos exibir a imagem padrão.")
                    imagemPerfil.value = imagemPadrao
                }
            } catch (e: Exception) {
                println("Erro ao carregar a foto de perfil no Publicar: $e")
            }
        }
    }


    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = { //essa parte do sheetContent é a parte de baixo (kkkkkk vai entender)
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(166.dp))
            {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp),
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
                                .size(70.dp),
                            colorFilter = ColorFilter.tint(Color(0xFFBDBBBB))
                        )
                    }


                    //Midias do bottomsheet
                    //Imagens
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                onClick = {
                                    galeriaState = true
                                }),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            //Espaçar
                            Spacer(modifier = Modifier
                                .width(10.dp))

                            //Imagem da imagem
                            Image(ImageVector.vectorResource(id = R.drawable.ic_imagem),
                                contentDescription = "Adicionar foto",
                                modifier = Modifier
                                    .size(29.dp),
                                colorFilter = ColorFilter.tint(Color(0xFFBB26D5)),
                            )


                            //Só pra espaçar um pouco a imagem e o texto
                            Spacer(modifier = Modifier
                                .width(20.dp))

                            //Texto da Imagem
                            Text(text = "Adicionar mídia",
                                fontSize = 34.sp,
                                color = Color(0xFF303030),
                                fontFamily = Dongle,
                            )

                        }
                    }


                    //Espaçar um pouco as duas opções

                    Spacer(modifier = Modifier
                        .height(15.dp))

                    //Marcar turmas (apenas para CPS)
                    if (!UserData.cpsIDEncontrado.isNullOrEmpty()){ //" ! " de negação, ou seja, o cpsID não está vazio e vai habilitar o marcar turmas.
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    onClick = {
                                        galeriaState = true
                                    }),
                        ){
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(
                                        onClick = {
                                            adicionarTurmaState = true
                                        })
                            ) {
                                //Espaçar
                                Spacer(modifier = Modifier
                                    .width(10.dp))

                                //Imagem da imagem
                                Image(ImageVector.vectorResource(id = R.drawable.ic_marcarturma),
                                    contentDescription = "Marcar turma",
                                    modifier = Modifier
                                        .size(29.dp),
                                    colorFilter = ColorFilter.tint(Color(0xFFE97014)
                                    )
                                )

                                //Só pra espaçar um pouco a imagem e o texto
                                Spacer(modifier = Modifier
                                    .width(20.dp))

                                //Texto da Imagem
                                Text(text = "Marcar turmas",
                                    fontSize = 34.sp,
                                    color = Color(0xFF303030),
                                    fontFamily = Dongle,
                                )

                                //Só pra espaçar do final
                                Spacer(modifier = Modifier
                                    .height(20.dp)
                                    .border(2.dp, Color.Black))

                            }
                        }
                    }


                }


            }
        },
        sheetBackgroundColor = Color(247, 246, 246, 255),
        sheetShape = RoundedCornerShape(25.dp, 25.dp,0.dp, 0.dp),
        sheetElevation = 2.dp,
    )
    {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFFFFF))
                .verticalScroll(scroll)
        ) {

            //Começo constraint layout.
            //Eu vou começar por ele pra que as coisas que eu posicionar aqui tenham um menor hierarquia nas camadas em geral.
            //Tô optando por ele porque a forma padrão tava bugando dms
            val (areaPublicar, arrow, fotoPerfil, nomeUsuario, areaTexto, areaTitulo, maxTituloText, tagTurmas, boxImagem,removerImagem) = createRefs()

            var titulo by remember { mutableStateOf("") }
            var texto by remember { mutableStateOf("") }

            //Máximo caracteres titulo
            val maxCaracteresTitulo = 35
            var maxTitulo = maxCaracteresTitulo - titulo.length

            Card(
                modifier = Modifier
                    .constrainAs(areaPublicar) {
                        top.linkTo(parent.top)
                    }
                    .fillMaxWidth()
                    .size(50.dp),
                elevation = 8.dp,
                backgroundColor = Color(240, 239, 239, 255),
            ) {
                //Parte do criar publicação e enviar com o Publicar
                Row(
                    Modifier
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(text = "Criar Publicação",
                        fontSize = 30.sp,
                        //fontWeight = FontWeight.Bold,
                        fontFamily = Dongle,
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .padding(end = 10.dp)
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
                                .padding(16.dp, 5.dp, 20.dp, 5.dp)
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
                                .padding(16.dp, 5.dp, 20.dp, 5.dp)
                        ) {
                            Text(text = "Publicar",
                                color = Color(0xFF005CFA),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                }
            }

            // Arrow voltar (seta que volta)
            arrowVoltar(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier
                    .constrainAs(arrow) {
                        start.linkTo(parent.start, margin = 8.dp)
                        top.linkTo(parent.top, margin = 10.dp)
                    }
                    .size(35.dp),
                color = Color(0xFF3C3C3C)
            )

            //Mini foto de perfil
            if (!check){
                Surface(
                    modifier = Modifier
                        .constrainAs(fotoPerfil) {
                            start.linkTo(parent.start, margin = 8.dp)
                            top.linkTo(areaPublicar.bottom, margin = 10.dp)
                        }
                        //.size(55.dp)
                        .padding(start = 0.dp),
                    shape = RoundedCornerShape(30.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(36.dp),
                        color = Color(43, 41, 41, 233),
                        strokeWidth = 10.dp
                    )
                }

            }
            else{
                Surface(
                    modifier = Modifier
                        .constrainAs(fotoPerfil) {
                            start.linkTo(parent.start, margin = 8.dp)
                            top.linkTo(areaPublicar.bottom, margin = 10.dp)
                        }
                        .size(55.dp)
                        .padding(start = 0.dp),
                    shape = RoundedCornerShape(30.dp)
                ) {
                    loadImage(
                        path = imagemPerfil.value,
                        contentDescription = "Mini imagem do perfil",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                    )
                }
            }

            //Área do nome do usuario
            Text(
                text = if
                               (!UserData.apelidoUsuario.isNullOrEmpty()) "${UserData.nomeEncontrado} (${UserData.apelidoUsuario})"
                else
                    UserData.nomeEncontrado,
                fontSize = 30.sp,
                fontFamily = Dongle,
                modifier = Modifier
                    .constrainAs(nomeUsuario) {
                        start.linkTo(fotoPerfil.end, margin = 5.dp)
                        top.linkTo(areaPublicar.bottom, margin = 10.dp)
                    }
            )
            //Area do título
            OutlinedTextField(
                value = titulo,
                onValueChange = {
                    if (titulo.length < maxCaracteresTitulo){
                        titulo = it
                    }

                },
                label = {
                    Text(text = "Título da publicação",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(148, 148, 148, 255),
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(85, 85, 85, 255),
                    unfocusedBorderColor = Color(170, 169, 169, 255),
                    cursorColor = Color(85, 85, 85, 255),
                    backgroundColor = Color(250, 250, 250, 255),
                ),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .constrainAs(areaTitulo) {
                        top.linkTo(fotoPerfil.bottom, margin = 0.dp)
                    }
                    .height(60.dp)
            )

            Text(
                text ="Max.Caracteres: $maxTitulo",
                color = Color(148, 148, 148, 255),
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                modifier = Modifier.constrainAs(maxTituloText){
                    end.linkTo(areaTitulo.end, margin = 8.dp)
                    bottom.linkTo(areaTitulo.top, margin = (-8.dp))
                }
            )

            //Area para escrever o texto da publicação
            OutlinedTextField(
                value = texto,
                onValueChange = {
                    texto = it
                },
                label = {
                    Text(text =
                    if (imagensColuna.isNullOrEmpty())"O que você deseja contar?" else "Diga algo sobre essa foto!",
                        fontSize = 18.sp,
                        color = Color(148, 148, 148, 255),
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = Color(250, 250, 250, 255),
                    focusedBorderColor = Color(85, 85, 85, 255),
                    unfocusedBorderColor = Color(170, 169, 169, 255),
                    cursorColor = Color(184, 184, 184, 255),

                    ),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                ),
                modifier = Modifier
                    .constrainAs(areaTexto) {
                        top.linkTo(areaTitulo.bottom, margin = 0.dp)
                    }
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(horizontal = 8.dp)
            )

            //Área para mostrar as turmas que serão marcadas
            if (!selecaoTurmas.isNullOrEmpty()){
                Text(text = "$selecaoTurmas",
                    fontSize = 30.sp,
                    fontFamily = Jomhuria,
                    color = LARANJA,
                    lineHeight = (15).sp,
                    modifier = Modifier
                        .constrainAs(tagTurmas) {
                            top.linkTo(areaTexto.bottom, margin = 3.dp)
                        }
                        .padding(horizontal = 8.dp)
                )
            }

            //Área que mostra as imagens
            Card(
                modifier = Modifier
                    .constrainAs(boxImagem) {
                        if (selecaoTurmas.isEmpty()) {
                            top.linkTo(areaTexto.bottom, margin = 25.dp)
                        } else {
                            top.linkTo(tagTurmas.bottom, margin = (-8).dp)
                        }
                    }
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .height(if (imagensColuna.isEmpty()) 200.dp else 220.dp * imagensColuna.size + 100.dp),
                backgroundColor = Color(255, 255, 255, 255)

            )
            {
                if (imagensColuna.isEmpty()){
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                galeriaState = true
                            }
                    ) {
                        loadImage(
                            path = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/adc_novaimagem.png",
                            contentDescription = "adc. Imagem",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier)
                    }
                }
                else{
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                    ) {
                        items(imagensColuna) { imagem ->
                            IconButton(
                                onClick = {
                                    imagensColuna.remove(imagem)
                                },
                                modifier = Modifier
                                    .constrainAs(removerImagem) {
                                        start.linkTo(parent.start, margin = 10.dp)
                                    }
                                    .size(20.dp)
                            ){Image(
                                ImageVector.vectorResource(id = R.drawable.ic_fechar2),
                                contentDescription = "Ícone para remover a imagem já selecionada",
                                colorFilter = ColorFilter.tint(Color.Red),
                                //contentScale = ContentScale.Fit
                            )}

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
                    turmasSelecionadas = selecaoTurmas,
                    navController = navController,
                    loading = {state ->
                        loadingState = true
                    }
                )
            }


        }
    }


    if (adicionarTurmaState){
        adicionarTurma(onDismiss = { selecao ->
            selecaoTurmas = selecao.map { it.title }
            adicionarTurmaState = false
            operacaoConcluida = true
        })
    }

    if (loadingState){
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(255, 255, 255, 163))
        ){
            val (circularProgress, logo) = createRefs()
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {},
                backgroundColor = Color(0, 0, 0, 136) //Desfoque
            ) {}

            CircularProgressIndicator(
                modifier = Modifier
                    .constrainAs(circularProgress) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .size(150.dp),
                color = Color(43, 41, 41, 233),
                strokeWidth = 10.dp
            )
            Box(
                modifier = Modifier
                    .constrainAs(logo) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .size(80.dp)
            ){
                loadImage(
                    path = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/logo_padrao.png",
                    contentDescription = "logo do App",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
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
fun CriarPublicacao(foto: String, nome:String, titulo:String, texto:String, imagensPublicacao: List<Bitmap>, turmasSelecionadas: List<String>, navController: NavController, loading:(Boolean) -> Unit){


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
    //Tela de carregamento
    var loadingState by remember{ mutableStateOf(false) }


    println("loading = $loadingState")
    //Tela de carregamento para sinalizar que a publicação está sendo enviada

    //Começo da lógica do post:
    LaunchedEffect(Unit) {
        loadingState = true
        if (loadingState){
            loading(loadingState)
        }
        if (alunoRM.isEmpty()) { //se o alunoRM estiver vazio, entendemos que é um professor que está tentando fazer uma postagem:

            /////////////
            //1 - Parte para mandar as imagens para o storage
            val cpsPastaRef = storageRef.child("$cpsID/$formatoFinal")
            println(formatoFinal)

            if (!imagensPublicacao.isNullOrEmpty()) { // " ! " para negação, ou seja, não está vazio
                Toast.makeText(context,"Estamos trabalhando nisso! Aguarde...", Toast.LENGTH_LONG).show()
                coroutineScope {
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
            println("proximo passo: subir dados normais")
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

                if (!imagensPublicacao.isNullOrEmpty()) { // " ! " para negação, ou seja, não está vazio
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
                                        val uniqueID = UUID.randomUUID().toString() //Esse aqui é o ID único que está sendo gerado para cada post

                                        val usuarioPostagem = hashMapOf(
                                            "nome" to UserData.nomeEncontrado,
                                            "apelido" to UserData.apelidoUsuario,
                                            "cpsID" to UserData.cpsIDEncontrado,
                                            "titulo" to titulo,
                                            "turmasMarcadas" to turmasSelecionadas,
                                            "texto" to texto,
                                            "imagensPostagem" to imagensUrls, //(lista de urls)
                                            "ultimaAtualizacao" to FieldValue.serverTimestamp(), // Adiciona a data/hora da atualização
                                            "idPost" to uniqueID,
                                        )


                                        postagensColecao.document("$titulo  (${UserData.nomeEncontrado}) - $uniqueID")
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
                        //mudanças aqui
                        .addOnFailureListener {exception ->
                            println("Não encontrou a pasta $exception")
                        }
                }
                else{ //Não há imagens no post
                    val uniqueID = UUID.randomUUID().toString() //Esse aqui é o ID único que está sendo gerado para cada post

                    val usuarioPostagem = hashMapOf(
                        "nome" to UserData.nomeEncontrado,
                        "apelido" to UserData.apelidoUsuario,
                        "cpsID" to UserData.cpsIDEncontrado,
                        "titulo" to titulo,
                        "turmasMarcadas" to turmasSelecionadas,
                        "texto" to texto,
                        "ultimaAtualizacao" to FieldValue.serverTimestamp(), // Adiciona a data/hora da atualização
                        "idPost" to uniqueID,
                    )

                    postagensColecao.document("$titulo  (${UserData.nomeEncontrado}) - $uniqueID")
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

            //Marcar turmas
            coroutineScope {
                //Agora a lógica para sinalizar todas as turmas que foram marcadas na publicação:
                val turmas = turmasSelecionadas
                var rmProvisorio:String? //isso vai ser util para sinalizar todos os alunos (não é o rm do aluno logado agora, é só uma box p/ guardar)

                val consultarTurmas = firestore.collection("Data") //Instância da coleção
                val rmDocumento =  consultarTurmas.document("RM")

                rmDocumento.get()//Entrando no documento
                    .addOnSuccessListener {documento ->
                        val data = documento.data

                        if (data != null) {
                            for (arrayCont in data.keys) {
                                val array = data[arrayCont] as? List<String>
                                if (array != null && array.isNotEmpty()) {

                                    val turmaAluno = array.getOrNull(2) // Pega o índice [2] do array, que no caso é onde eu guardei a turma do aluno
                                    rmProvisorio = array.getOrNull(0)  // Pega o rm
                                    println("Turma do aluno: $turmaAluno, rm dele para sinalizar $rmProvisorio")

                                    //Agora vamos passar a lista inteira que o usuário selecionou para comparar se o aluno está na turma.
                                    for(turma in turmas){
                                        if (turma == turmaAluno){ //a turma do aluno atual é igual a que o usuário marcou, então vamos notificar ele (usando a box Provisória).
                                            val sinalizarAluno = firestore.collection("Alunos") //Instância da coleção, agora vamos para outro lugar.
                                            val alunoDocumento =  sinalizarAluno.document("$rmProvisorio")
                                            alunoDocumento.get()
                                                .addOnSuccessListener {documento ->
                                                    println("Encontramos com sucesso o aluno que vai ser notificado")
                                                    //vamos atualizar (caso ja tenha sido notificado antes, no caso) o campo de notificação desse aluno em questão.
                                                    if (documento.contains("notificacoes")){
                                                        val numeroNotificacoesConversao = documento.getLong("notificacoes") //Obtendo a quantidade de notificação que ele já possui
                                                        val notificacoes = numeroNotificacoesConversao?.toInt() ?.plus(1) //Convertendo para int (o número nao vem como inteiro totalmente)
                                                        //e depois adicionando (.plus(1), só consegui fazer assim) +1 para a conta. Ou seja, mais uma notificação.
                                                        println("Número de notificações atuais é: $notificacoes")
                                                        //Agora de fato subindo uma notificação a mais do que já existia:
                                                        val notificar = hashMapOf(
                                                            "notificacoes" to notificacoes
                                                        )
                                                        //Mesclando os dados para nao excluir o que já está lá
                                                        alunoDocumento.set(notificar, SetOptions.merge())
                                                            .addOnSuccessListener {
                                                                println("o Aluno do rm $rmProvisorio foi notificado!")
                                                            }
                                                            .addOnFailureListener {exception ->
                                                                Toast.makeText(context,"Erro ao notificar um aluno/turma em especifico.", Toast.LENGTH_SHORT).show()
                                                                println(exception)
                                                            }
                                                    }
                                                    else{
                                                        val notificar = hashMapOf( //aqui nao precisamos fazer nenhuma conta, já que nao existe notificação previamente.
                                                            "notificacoes" to 1
                                                        )
                                                        //Mesclando os dados para nao excluir o que já está lá
                                                        alunoDocumento.set(notificar, SetOptions.merge())
                                                            .addOnSuccessListener {
                                                                println("o Aluno do rm $rmProvisorio foi notificado!")
                                                            }
                                                            .addOnFailureListener {exception ->
                                                                Toast.makeText(context,"Erro ao notificar um aluno em especifico.", Toast.LENGTH_SHORT).show()
                                                                println(exception)
                                                            }
                                                    }

                                                }
                                                .addOnFailureListener {
                                                    println("Erro ao encontrar o documento do aluno")
                                                }
                                        }
                                    }

                                }
                            }
                        }
                    }
                    .addOnFailureListener {exception ->
                        println("Erro ao acessar o documento: $exception")
                    }
            }

            println("Encerrando o processo de publicação")
        }

        //////////////////////////
        //Parte do Aluno
        else{ //Aluno tentando fazer uma postagem
            /////////////
            //1 - Parte para mandar as imagens para o storage
            val alunoPastaRef = storageRef.child("$alunoRM/$formatoFinal")
            println(formatoFinal)

            if (!imagensPublicacao.isNullOrEmpty()) { // " ! " para negação, ou seja, não está vazio
                Toast.makeText(context,"Estamos trabalhando nisso! Aguarde...", Toast.LENGTH_LONG).show()
                coroutineScope {
                    for ((index, imagem) in imagensPublicacao.withIndex()) {
                        val caminhoImagem =
                            "$alunoPastaRef/imagem$index.jpg" // Caminho exclusivo para cada imagem

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
            println("proximo passo: subir dados normais")
            /////////////

            /////////////
            //Agora os demais dados do post
            val postagensColecao = firestore.collection("Postagens")

            //Primeiro buscando as imagens que acabei de subir anteriormente para o storage
            //vamos armazenar o link de todas elas dentro do imagensUrls:
            coroutineScope {
                val imagensUrls = mutableListOf<String>()
                println("Valor da lista: $imagensUrls")

                val pastaImagens = storageRef.child("/gs:/tcc-projeto-f3873.appspot.com/$alunoRM/$formatoFinal")
                val totalImagens = pastaImagens.listAll().await().items.size //pegando o total de imagens que subiu para o storage
                var imagesEnviadasComSucesso = 0 //essa variavel vai servir para "barrar" o código de continuar até que de fato as urls sejam obtidas e subam corretamente

                if (!imagensPublicacao.isNullOrEmpty()) { // " ! " para negação, ou seja, não está vazio
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
                                        val uniqueID = UUID.randomUUID().toString()

                                        val usuarioPostagem = hashMapOf(
                                            "nome" to UserData.nomeEncontrado,
                                            "apelido" to UserData.apelidoUsuario,
                                            "RM" to UserData.rmEncontrado,
                                            "titulo" to titulo,
                                            "texto" to texto,
                                            "imagensPostagem" to imagensUrls, //(lista de urls)
                                            "ultimaAtualizacao" to FieldValue.serverTimestamp(), // Adiciona a data/hora da atualização
                                            "idPost" to uniqueID,
                                        )


                                        postagensColecao.document("$titulo  (${UserData.nomeEncontrado}) - $uniqueID")
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
                        //mudanças aqui
                        .addOnFailureListener {exception ->
                            println("Não encontrou a pasta $exception")
                        }
                }
                else{ //Não há imagens no post
                    val uniqueID = UUID.randomUUID().toString()

                    val usuarioPostagem = hashMapOf(
                        "nome" to UserData.nomeEncontrado,
                        "apelido" to UserData.apelidoUsuario,
                        "RM" to UserData.rmEncontrado,
                        "titulo" to titulo,
                        "texto" to texto,
                        "ultimaAtualizacao" to FieldValue.serverTimestamp(), // Adiciona a data/hora da atualização
                        "idPost" to uniqueID,
                    )

                    postagensColecao.document("$titulo  (${UserData.nomeEncontrado}) - $uniqueID")
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

        }
        loadingState = false
    }
}

@Composable
fun adicionarTurma(onDismiss:(List<turmasItem>) -> Unit){

    val turmas = listOf(
        "1ADM", "2ADM", "3ADM",
        "1CONT", "2CONT", "3CONT",
        "1DS", "2DS", "3DS",
        "1LOG", "2LOG", "3LOG",
        "1JURI", "2JURI", "3JURI",
    )

    var selecao: List<turmasItem> = emptyList()

    var items by remember {
        mutableStateOf(
            turmas.map { turma ->
                turmasItem(
                    title = turma,
                    isSelected = false
                )
            }
        )
    }


    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = LARANJA)
            .padding(15.dp)
    ) {
        val (turmasColumn, checkBox, turmasSelecionadasPreview, finalizar) = createRefs()

        Card(
            modifier = Modifier
                .constrainAs(turmasColumn) {
                    top.linkTo(parent.top)
                }
                .fillMaxSize()
                .padding(bottom = 330.dp),
            backgroundColor = Color.White,
            elevation = 8.dp,
            shape = RoundedCornerShape(15.dp)
        ){
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp)
            ) {
                items(items.size) { i ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                            .clickable {
                                items = items.mapIndexed { j, item ->
                                    if (i == j) {
                                        item.copy(isSelected = !item.isSelected)
                                    } else item
                                }
                            },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (items[i].isSelected){
                            Text(text = items[i].title,
                                fontSize = 30.sp,
                                color = LARANJA,
                                fontFamily = Jomhuria,
                            )
                            Text(text = "Selecionado!",
                                color = LARANJA,
                                fontSize = 28.sp,
                                fontFamily = Jomhuria,
                            )
                        }
                        else{
                            Text(text = items[i].title,
                                fontSize = 28.sp,
                                fontFamily = Jomhuria,
                                color = Color(92, 92, 92, 255)
                            )
                        }
                        if (items[i].isSelected){
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selecionado",
                                tint = LARANJA,
                                modifier = Modifier.size(28.dp)
                            )
                        }


                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .border(1.dp, Color(124, 124, 124, 255))
                    ) {}
                }

            }
        }

        //Marcar todas as turmas
        var isChecked by remember { mutableStateOf(false) }
        Box(modifier = Modifier
            .constrainAs(checkBox) {
                top.linkTo(turmasColumn.bottom, margin = (-328).dp)
            }
        ){
            Checkbox(
                checked = isChecked,
                onCheckedChange = { marcarTudo ->
                    isChecked = marcarTudo
                    if(marcarTudo){
                        items = items.map { item ->
                            item.copy(isSelected = true)
                        }
                    }
                    else{
                        items = items.map { item ->
                            item.copy(isSelected = false)
                        }
                    }
                },
                colors = CheckboxDefaults.colors(
                    uncheckedColor = Color.Black,
                    checkedColor = Color.White,
                    checkmarkColor = Color.Black
                ),
            )
            Text(text = "Marcar todas as turmas",
                fontSize = 20.sp,
                //color = LARANJA,
                color = Color(255, 255, 255, 255),
                modifier = Modifier
                    .padding(46.dp,8.dp))
        }



        //Área e lógica das turmas selecionadas (visualizar e salvar)
        var listaTurmas = items.filter { it.isSelected }
        Card(
            elevation = 5.dp,
            shape = RoundedCornerShape(10.dp),
            backgroundColor = Color.White,
            modifier = Modifier
                .constrainAs(turmasSelecionadasPreview) {
                    top.linkTo(checkBox.bottom, margin = (-0).dp)
                }
                .fillMaxWidth()
                .size(220.dp)
        ){
            Text(
                text = "Turmas selecionadas: ${listaTurmas.size}",
                fontSize = 22.sp,
                //color = LARANJA,
                color = Color(112, 112, 112, 255),
                modifier = Modifier
                    .padding(8.dp)
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .padding(top = 26.dp),

                ){
                items(listaTurmas){ turmaSelecionada ->

                    val selectedItem = items.find { it.title == turmaSelecionada.title }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = turmaSelecionada.title,
                            fontSize = 34.sp,
                            color = LARANJA,
                            fontFamily = Jomhuria,
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .clickable {
                                    items = items.map { item ->
                                        if (item == selectedItem) {
                                            item.copy(isSelected = false)
                                        } else {
                                            item
                                        }
                                    }
                                }
                        )
                        IconButton(
                            onClick = {
                                items = items.map { item ->
                                    if (item == selectedItem) {
                                        item.copy(isSelected = false)
                                    } else {
                                        item }
                                }
                            },
                            modifier = Modifier.size(22.dp)
                        ){Image(
                            ImageVector.vectorResource(id = R.drawable.ic_fechar2),
                            contentDescription = "Ícone para remover a turma selecionada",
                            colorFilter = ColorFilter.tint(Color.Red))
                        }
                    }

                }
            }
        }


        Row(
            modifier = Modifier
                .constrainAs(finalizar) {
                    top.linkTo(turmasSelecionadasPreview.bottom, margin = 15.dp)
                }
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ){
            Button(
                onClick = {
                    onDismiss(selecao)
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White
                ),
                modifier = Modifier
            ) {
                Text(text = "Cancelar",
                    color = LARANJA,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Button(
                onClick = {
                    selecao = items.filter { it.isSelected }

                    println("aqui os selecionados no button: $selecao")
                    onDismiss(selecao)
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White
                ),
                modifier = Modifier
            ) {
                Text(text = "Salvar",
                    color = LARANJA,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

}
