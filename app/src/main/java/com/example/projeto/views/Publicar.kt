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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.projeto.R
import com.example.projeto.datasource.UserData
import com.example.projeto.layoutsprontos.arrowVoltar
import com.example.projeto.layoutsprontos.loadImage
import com.example.projeto.turmasItens.turmasItem
import com.example.projeto.ui.theme.Dongle
import com.example.projeto.ui.theme.Jomhuria
import com.example.projeto.ui.theme.LARANJA
import com.example.projeto.viewmodel.PublicacaoViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.auth.User
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
    val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Expanded)
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

    //Máximo caracteres titulo
    val maxCaracteresTitulo = 35

    //Iniciar o processo de marcar turmas
    var adicionarTurmaState by remember { mutableStateOf(false) }
    var selecaoTurmas: List<String> by remember { mutableStateOf(emptyList()) } //a lista das turmas em si
    var operacaoConcluida by remember { mutableStateOf(false) } //gambiarra pra nao bugar

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = { //essa parte do sheetContent é a parte de baixo (kkkkkk vai entender)
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(190.dp))
            {
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
                            colorFilter = ColorFilter.tint(Color(0xFFBDBBBB))
                        )
                    }


                    //Midias do bottomsheet
                    //Imagens
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                onClick = {
                                    galeriaState = true
                                }),
                        backgroundColor = Color(243, 243, 243, 255),
                        shape = RoundedCornerShape(8.dp),
                        elevation = 8.dp
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
                                contentDescription = "Adicionar foto ou video",
                                modifier = Modifier
                                    .size(38.dp)
                                /*colorFilter = ColorFilter.tint(Color(0xFFC5C4C4)
                                )*/
                            )

                            //Só pra espaçar um pouco a imagem e o texto
                            Spacer(modifier = Modifier
                                .width(20.dp))

                            //Texto da Imagem
                            Text(text = "Adicionar mídia",
                                fontSize = 35.sp,
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
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    onClick = {
                                        galeriaState = true
                                    }),
                            backgroundColor = Color(233, 233, 233, 255),
                            shape = RoundedCornerShape(8.dp),
                            elevation = 8.dp
                        ){
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background((Color(243, 242, 242, 255)))
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
                                        .size(38.dp)
                                    /*colorFilter = ColorFilter.tint(Color(0xFFC5C4C4)
                                    )*/
                                )

                                //Só pra espaçar um pouco a imagem e o texto
                                Spacer(modifier = Modifier
                                    .width(20.dp))

                                //Texto da Imagem
                                Text(text = "Marcar turmas",
                                    fontSize = 35.sp,
                                    color = Color(0xFF303030),
                                    fontFamily = Dongle,
                                )

                            }
                        }
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
                .background(Color(241, 241, 241, 255))
                .padding(horizontal = 4.dp)
                .verticalScroll(scroll)
        ) {

            //Começo constraint layout.
            //Eu vou começar por ele pra que as coisas que eu posicionar aqui tenham um menor hierarquia nas camadas em geral.
            //Tô optando por ele porque a forma padrão tava bugando dms
            val (arrow, areaPublicar, areaTexto,areaTitulo, tagTurmas, boxImagem) = createRefs()

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
                color = Color(0xFF3C3C3C)
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
                    if (!UserData.imagemUrl.isNullOrEmpty()){
                        Text(text = "Criar Publicação",
                            fontSize = 30.sp,
                            fontFamily = Dongle,
                            modifier = Modifier
                                .padding(top = 5.dp)
                                .padding(end = 10.dp)
                        )

                        Surface(
                            modifier = Modifier
                                .size(40.dp)
                                .padding(start = 0.dp),
                            shape = RoundedCornerShape(30.dp)
                        ) {
                            loadImage(
                                path = UserData.imagemUrl,
                                contentDescription = "Mini imagem do perfil",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                            )
                        }
                    }
                    else{
                        Text(text = "Criar Publicação",
                            fontSize = 26.sp,
                            //fontWeight = FontWeight.Bold,
                            fontFamily = Dongle,
                            modifier = Modifier
                                .padding(top = 5.dp)
                                .padding(end = 10.dp)
                        )

                    }

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
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        //color = Color(158, 158, 158, 255),
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedLabelColor = Color(39, 39, 39, 255),
                    focusedBorderColor = Color(241, 241, 241, 255),
                    unfocusedBorderColor = Color(241, 241, 241, 255),
                    cursorColor = Color(85, 85, 85, 255),
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
                        Text(text = "Diga algo interessante abaixo!",
                            fontSize = 18.sp
                        )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = Color(241, 241, 241, 255),
                    focusedLabelColor = Color(49, 49, 49, 255),
                    focusedBorderColor = Color(184, 184, 184, 255),
                    unfocusedBorderColor = Color(241, 241, 241, 255),
                    cursorColor = Color(184, 184, 184, 255),
                ),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                ),
                modifier = Modifier
                    .constrainAs(areaTexto) {
                        top.linkTo(areaTitulo.bottom, margin = 5.dp)
                    }
                    .fillMaxWidth()
                    .height(150.dp)
            )

            //Área para mostrar as turmas que serão marcadas
            if (!selecaoTurmas.isNullOrEmpty()){
                Text(text = "$selecaoTurmas",
                fontSize = 30.sp,
                    fontFamily = Jomhuria,
                    color = LARANJA,
                    lineHeight = (15).sp,
                    modifier = Modifier.constrainAs(tagTurmas){
                        top.linkTo(areaTexto.bottom, margin = 3.dp)
                    }
                    )
            }

            //Área que mostra as imagens
            Card(
                modifier = Modifier
                    .constrainAs(boxImagem) {
                        top.linkTo(areaTexto.bottom, margin = 70.dp)
                    }
                    .fillMaxWidth()
                    .height(if (imagensColuna.isEmpty()) 200.dp else 220.dp * imagensColuna.size + 90.dp),
                elevation = 8.dp,
                backgroundColor = Color(241, 241, 241, 255)
            )
            {
                if (imagensColuna.isEmpty()){
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        loadImage(
                            path = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/imagempadrao_publicar.jpg",
                            contentDescription = "Corujinha adc.Imagem",
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
                    navController = navController
                )
            }


        }
    }


    if (adicionarTurmaState){
        adicionarTurma(onDismiss = { selecao ->
            selecaoTurmas = selecao.map { it.title }
            adicionarTurmaState = false
            operacaoConcluida = true
            println("Valor retornado ao clicar: $selecao")
        })
    }

    println("Fora: $selecaoTurmas")
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
fun CriarPublicacao(foto: String, nome:String, titulo:String, texto:String, imagensPublicacao: List<Bitmap>, turmasSelecionadas: List<String>, navController: NavController){


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

            if (!imagensPublicacao.isNullOrEmpty()) { // " ! " para negação, ou seja, não está vazio
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
                                        println("Imagens enviadas: $imagensUrls, total de imagens $totalImagens")
                                        val usuarioPostagem = hashMapOf(
                                            "fotoPerfil" to UserData.imagemUrl,
                                            "nome" to UserData.nomeEncontrado,
                                            "apelido" to UserData.apelidoUsuario,
                                            "cpsID" to UserData.cpsIDEncontrado,
                                            "titulo" to titulo,
                                            "turmasMarcadas" to turmasSelecionadas,
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
                        //mudanças aqui
                        .addOnFailureListener {exception ->
                            println("Não encontrou a pasta $exception")
                        }
                }
                else{ //Não há imagens no post

                    val usuarioPostagem = hashMapOf(
                        "fotoPerfil" to UserData.imagemUrl,
                        "nome" to UserData.nomeEncontrado,
                        "apelido" to UserData.apelidoUsuario,
                        "cpsID" to UserData.cpsIDEncontrado,
                        "titulo" to titulo,
                        "turmasMarcadas" to turmasSelecionadas,
                        "texto" to texto,
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

            //Marcar turmas
            coroutineScope {
                //Agora a lógica para sinalizar todas as turmas que foram marcadas na publicação:
                val turmas = turmasSelecionadas
                println("Aqui é o teste para saber se a lista está sendo obtida corretamente $turmas")
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
                                println("saindo do laço")
                            }
                            println("saindo do if")
                        }
                    }
                    .addOnFailureListener {exception ->
                        println("Erro ao acessar o documento: $exception")
                    }
                println("saindo da coroutine")
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

            val referenciaHora = formatoFinal //nao sei se vou usar mais
            if (!imagensPublicacao.isNullOrEmpty()) { // " ! " para negação, ou seja, não está vazio
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
                                        println("Imagens enviadas: $imagensUrls, total de imagens $totalImagens")
                                        val usuarioPostagem = hashMapOf(
                                            "fotoPerfil" to UserData.imagemUrl,
                                            "nome" to UserData.nomeEncontrado,
                                            "apelido" to UserData.apelidoUsuario,
                                            "RM" to UserData.rmEncontrado,
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
                        //mudanças aqui
                        .addOnFailureListener {exception ->
                            println("Não encontrou a pasta $exception")
                        }
                }
                else{ //Não há imagens no post

                    val usuarioPostagem = hashMapOf(
                        "fotoPerfil" to UserData.imagemUrl,
                        "nome" to UserData.nomeEncontrado,
                        "apelido" to UserData.apelidoUsuario,
                        "RM" to UserData.rmEncontrado,
                        "titulo" to titulo,
                        "texto" to texto,
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

        }
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
                    .padding(top = 26.dp)
            ){
                items(listaTurmas){ turmaSelecionada ->

                    val selectedItem = items.find { it.title == turmaSelecionada.title }
                    Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
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
