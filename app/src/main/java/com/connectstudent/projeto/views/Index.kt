package com.connectstudent.projeto.views

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.connectstudent.projeto.bottomNavigation.BottomNavItem
import com.connectstudent.projeto.bottomNavigation.withIconModifier
import com.connectstudent.projeto.datasource.PostagemData
import com.connectstudent.projeto.datasource.UserData
import com.connectstudent.projeto.layoutsprontos.*
import com.connectstudent.projeto.listener.ListenerPublicacao
import com.connectstudent.projeto.ui.theme.Dongle
import com.connectstudent.projeto.ui.theme.LARANJA
import com.connectstudent.projeto.viewmodel.PublicacaoViewModel
import com.connectstudent.projeto.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch



@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Index(navController: NavController, viewModel: PublicacaoViewModel = hiltViewModel()) {

    val postagensOrdenadas = remember { mutableStateListOf<PostagemData>() }


    //Variaveis para o funcionamento das publicações na index
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()

    //////////////////////////////////////
    // A instância do firebase firestore:
    val firestore = Firebase.firestore  // Também funcionaria assim: val firestore = FirebaseFirestore.getInstance()
    //Instância do authentication:
    val auth = FirebaseAuth.getInstance()
    // A instância do firebase authenticaiton e as variáveis que vou precisar:
    val UIDref = FirebaseAuth.getInstance().currentUser?.uid.toString()
    val email = auth.currentUser?.email
    // A instância do firebase storage
    val storage = Firebase.storage
    val storageRef = storage.reference
    //////////////////////////////////////


    //Para guardar temporariamente a imagem do perfil do usuário (na página de perfil também é carregado).
    var imagemUrl by remember { mutableStateOf<String?>("") }

    //Lógica do carregamento
    var indexState by remember{ mutableStateOf(true) }

    //Notificações do usuário
    var notificacoes by remember { mutableStateOf<Int?>(0) }

    // Para aguardar a foto do perfil do usuário no drawerContent
    var urlBaixada by remember{ mutableStateOf(false) }

    //Aqui é primordial, é dessa forma que os dados bases (tipo RM) chegam na index.
    LaunchedEffect(Unit){

        scope.launch {
            //A viewmodel traz os dados básicos assim que o usuário loga (pegamos pelo UID)
            viewModel.usuarioEncontrado(object : ListenerPublicacao{
                override fun onSucess(rm:String, cpsID:String, apelido:String, nome:String, turma:String) {
                    println("o usuario que vem do listener tem o rm: $rm, ou o cpsID $cpsID , o apelido $apelido, o nome: $nome e a turma: $turma")
                    if (email != null) { // <- precisei colocar por conta do "?" do authentication do firebase
                        UserData.setUserData(rm, cpsID, nome, turma, UIDref, email)
                    }
                    if (!apelido.isNullOrEmpty()){//na negativa "!", nao está vazio ou nullo.
                        UserData.setApelido(apelido)
                    }
                }
                override fun onFailure(erro: String) {
                    println("Nenhum usuario encontrado.")
                }

            })
            delay(1500)


            //Parte para trazer as postagens
            val postagensRef = firestore.collection("Postagens")

            val filaOrdenar = postagensRef
                .orderBy("ultimaAtualizacao", Query.Direction.DESCENDING)

            filaOrdenar.get()
                .addOnSuccessListener {postagens ->
                    println("Entrou no onSucess (ordenando os dados)")
                    println("Tamanho de postagens: ${postagens.size()}")
                    val postagensData = mutableListOf<PostagemData>()
                    for (posts in postagens){
                        val fotoPerfil = posts.getString("fotoPerfil") ?: ""
                        val imagensPostagem = posts.get("imagensPostagem") as? List<String> ?: emptyList()
                        val nome = posts.getString("nome") ?: ""
                        val rm = posts.getString("RM") ?: ""
                        val cpsID = posts.getString("cpsID") ?: ""
                        val apelido = posts.getString("apelido") ?: ""
                        val texto = posts.getString("texto") ?: ""
                        val titulo = posts.getString("titulo") ?: ""
                        val turmas = posts.get("turmasMarcadas") as? List<String> ?: emptyList()
                        val idPost = posts.getString("idPost") ?: ""
                        //o numero de curtidas eu vou converter de long para int.
                        var Curtidas = 0
                        //Além disso, a validação do campo foi meio que necessário para nao quebrar o código:
                        if (posts.contains("curtidas")){
                            val numeroCurtidas = posts.getLong("curtidas")?.toInt()
                            if (numeroCurtidas != null) {
                                Curtidas = numeroCurtidas
                            }
                        }




                        println("Agora vai armazenar os dados na val postagemData.")
                        val postagemData = PostagemData(
                            fotoPerfil = fotoPerfil,
                            nomeAutor = nome,
                            rm = rm,
                            cpsID = cpsID,
                            apelidoAutor = apelido,
                            textoPostagem = texto,
                            imagensPost = imagensPostagem,
                            tituloPost = titulo,
                            turmasMarcadas = turmas,
                            idPostagem = idPost,
                            curtidas = Curtidas,
                        )

                        postagensData.add(postagemData)
                        println("Printando o conteúdo da postagemData: $postagemData")

                    }
                    postagensOrdenadas.clear()
                    postagensOrdenadas.addAll(postagensData)
                }
                .addOnFailureListener{erro ->
                    println("Não foi possivel coletar os dados $erro")
                }


            delay(1000) //esse delay serve para dar tempo do rm ser guardado na classe UserData e conserguirmos fazer as lógicas abaixo.

            //Parte para trazer as notificações. Começando pelo aluno:
            if (!UserData.rmEncontrado.isNullOrEmpty()){// " ! " de negação, ou seja, não está vazio.
                val usuarioCollection = firestore.collection("Alunos")
                val usuarioRef = usuarioCollection.document(UserData.rmEncontrado) //Usando o RM que guardamos
                usuarioRef.get()
                    .addOnSuccessListener {documento ->
                        if (documento.contains("notificacoes")){
                            val numeroNotificacoesConversao = documento.getLong("notificacoes") //Obtendo a quantidade de notificação que o usuário já possui
                            notificacoes = numeroNotificacoesConversao?.toInt()
                            println("O usuário tem $notificacoes notificações.")
                        }else{
                            notificacoes = 0
                        }
                    }
            }
            else if(!UserData.cpsIDEncontrado.isNullOrEmpty()){
                val usuarioCollection = firestore.collection("Cps")
                val usuarioRef = usuarioCollection.document(UserData.cpsIDEncontrado)
                usuarioRef.get()
                    .addOnSuccessListener {documento ->
                        if (documento.contains("notificacoes")){
                            val numeroNotificacoesConversao = documento.getLong("notificacoes")
                            notificacoes = numeroNotificacoesConversao?.toInt()
                        }
                        else{
                            notificacoes = 0
                        }
                    }
            }

            delay(500)

            //Parte para recuperar a foto de perfil do usuário (meio que provisória, fazemos o mesmo no profile).
            if (!UserData.rmEncontrado.isNullOrEmpty()) {
                val alunoRef = storageRef.child("Alunos/Fotos de Perfil").child(UserData.rmEncontrado)
                alunoRef.downloadUrl
                    .addOnSuccessListener { uri ->
                        val url = uri.toString()
                        println("URL obtida: $url")
                        imagemUrl = url
                        UserData.updateUrl(url)
                    }
                    .addOnFailureListener { exception ->
                        println("A URL não pôde ser obtida. Erro: $exception")
                    }
            } else if (!UserData.cpsIDEncontrado.isNullOrEmpty()) {
                val cpsRef = storageRef.child("CPS/Fotos de Perfil").child(UserData.cpsIDEncontrado)
                cpsRef.downloadUrl
                    .addOnSuccessListener { uri ->
                        val url = uri.toString()
                        println("URL obtida: $url")
                        imagemUrl = url
                        UserData.updateUrl(url)
                    }
                    .addOnFailureListener { exception ->
                        println("A URL não pôde ser obtida. Erro: $exception")
                    }
            }
            delay(1000)
            urlBaixada = true // a lógica pro progressIndicator
            println(UserData.imagemUrl)
        }

    }



    var expandirCard by remember { mutableStateOf(false) }
    var abrirFoto by remember { mutableStateOf(false) }
    var caminhoImagem by remember { mutableStateOf("") }
    var postagemReferencia by remember { mutableStateOf("") }

    //Começo do layout
    Scaffold(
        scaffoldState = scaffoldState,

        topBar = {
            TopAppBar(
                backgroundColor = Color(0xFFFBF7F5),
                elevation = 0.dp
            ) {
                Row() {
                    if (urlBaixada){
                        if (!UserData.imagemUrl.isNullOrEmpty()){
                            Surface(
                                modifier = Modifier
                                    .clickable {
                                        scope.launch {
                                            scaffoldState.drawerState.open()
                                        }
                                    }
                                    .size(36.dp),
                                shape = RoundedCornerShape(30.dp)
                            ){
                                loadImage(
                                    path = UserData.imagemUrl,
                                    contentDescription = "Mini imagem do usuário para abrir o Drawer",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier)
                            }
                        }else{
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        scaffoldState.drawerState.open()
                                    }
                                },
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(start = 5.dp)
                                    .padding(top = 15.dp)
                            ){
                                Image(ImageVector.vectorResource(id = R.drawable.ic_drawermenu),
                                    contentDescription = "Publicar",)
                            }
                        }
                    }
                    else{
                        Surface(
                            modifier = Modifier
                                .size(30.dp)
                                .padding(start = 1.dp)
                                .padding(top = 1.dp),
                            shape = CircleShape
                        ) {
                            CircularProgressIndicator(
                                color = Color(9, 9, 9, 255),
                                strokeWidth = 5.dp
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(end = 34.dp, top = 4.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(60.dp)
                    ) {
                        loadImage(
                            path = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/logo_padrao.png",
                            contentDescription = "Mini Logo da Index",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                        )
                    }
                }

            } //fechamento TopBar


        },
        drawerContent = { drawerPersonalizado(urlBaixada,navController) },
        drawerBackgroundColor = Color.White,

        //Tô usando o content para mesclar o constraintLayout à aplicação em geral, assim ele fica em cima da bottomBar (tipo camadas).
        content = {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                ConstraintLayout(
                    modifier = Modifier.fillMaxWidth()

                ) {

                    val (geral, paravoce, paginaAtual, linhaestetica,bottomC) = createRefs()
                    var paginaIndex by remember { mutableStateOf(true) }


                    Text(text = "Geral",
                        fontSize = 36.sp,
                        fontFamily = Dongle,
                        color = Color.Black,
                        modifier = Modifier
                            .constrainAs(geral) {
                                start.linkTo(parent.start, margin = (-145).dp)
                                top.linkTo(parent.top)
                                end.linkTo(parent.end)
                            }
                    )

                    Text(text = "Para você",
                        fontSize = 34.sp,
                        fontFamily = Dongle,
                        color = Color.Black,
                        modifier = Modifier
                            .constrainAs(paravoce) {
                                start.linkTo(parent.start)
                                top.linkTo(parent.top)
                                end.linkTo(parent.end, margin = (-170).dp)
                            }
                    )

                    if (paginaIndex){
                        Row(
                            modifier = Modifier
                                .constrainAs(paginaAtual) {
                                    start.linkTo(parent.start, margin = (-145).dp)
                                    top.linkTo(geral.bottom, margin = (-10).dp)
                                    end.linkTo(parent.end)
                                }
                                .width(60.dp)
                                .size(3.dp)
                                .background(color = LARANJA)
                        ) {}
                    }

                    Row(
                        modifier = Modifier
                            .constrainAs(linhaestetica) {
                                top.linkTo(paravoce.bottom, margin = 3.dp)
                            }
                            .fillMaxWidth()
                            .size(2.dp)
                            .background(color = Color(209, 209, 209, 255))
                    ){}

                }


                //ConstraintLayout para o que precisar ser posicionado melhor
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    val (cardBackground, fotoPerfilPub,fecharFoto) = createRefs()

                    //Fundo da index
                    loadImage(
                        path = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/fundo_index.png",
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                    )

                    //A lógica da área das postagens aqui
                    Column(
                        modifier = Modifier
                            .padding(bottom = 60.dp)
                    ) {
                        ListaDePostagens(
                            postagens = postagensOrdenadas,
                            expandir = {resultado ->
                                expandirCard = resultado},
                            abrirFoto = { resultado ->
                                caminhoImagem = resultado
                                abrirFoto = !abrirFoto
                            },
                            postRef = { postagemRef ->
                                postagemReferencia = postagemRef
                            }
                        )
                    }
                    //Lógica para maximizar a foto de perfil da pub
                    if (abrirFoto){
                        //Essa primeira box é só para desfocar e adc. o click pra sair externo
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0, 0, 0, 136)) //Desfoque
                                .clickable {
                                    abrirFoto = !abrirFoto
                                }
                        ) {}

                        Card(
                            modifier = Modifier
                                .constrainAs(cardBackground) {
                                    start.linkTo(parent.start)
                                    top.linkTo(parent.top)
                                    end.linkTo(parent.end)
                                    bottom.linkTo(parent.bottom, margin = 140.dp)
                                }
                                .size(330.dp),
                            backgroundColor = Color.Black
                        ) {}

                        //A foto em si (já maximizada).
                        Box(
                            modifier = Modifier
                                .constrainAs(fotoPerfilPub) {
                                    start.linkTo(parent.start)
                                    top.linkTo(parent.top)
                                    end.linkTo(parent.end)
                                    bottom.linkTo(parent.bottom, margin = 140.dp)
                                }
                                .size(320.dp)
                        ) {
                            loadImage(
                                path = caminhoImagem,
                                contentDescription = "",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                            )
                        }
                        Text(
                            text = "Fechar",
                            fontSize = 34.sp,
                            fontFamily = Dongle,
                            color = Color.White,
                            lineHeight = (15).sp,
                            modifier = Modifier
                                .constrainAs(fecharFoto) {
                                    top.linkTo(fotoPerfilPub.bottom, margin = 5.dp)
                                    end.linkTo(fotoPerfilPub.end, margin = 5.dp)
                                }
                                .clickable {
                                    abrirFoto = !abrirFoto
                                },
                        )

                    }
                    indexState = false
                }//Fechamento do segundo Constraint
            }

        },

        bottomBar = {
            //Gambiarra para colocar sombra na bottomNavigation (a padrão dela por algum motivo nao estava indo).
            Surface(
                elevation = 6.dp,
                modifier = Modifier
                    .height(55.dp)
            ) {
                BottomNavigationBar(
                    items = listOf(
                        BottomNavItem(
                            nome = "Home",
                            route = "Index",
                            badgeCount = 0,
                            icon = ImageVector.vectorResource(id = R.drawable.ic_home),
                        ).withIconModifier(Modifier.size(25.dp)),
                        BottomNavItem(
                            nome = "Chat",
                            route = null,
                            badgeCount = 4,
                            icon = ImageVector.vectorResource(id = R.drawable.ic_chat)
                        ).withIconModifier(
                            Modifier
                                .size(32.dp)
                                .padding(end = 2.dp)),
                        BottomNavItem( //esse é uma gambiarra daquelas kkkk
                            nome = "",
                            route = null,
                            badgeCount = 0,
                            icon = ImageVector.vectorResource(id = R.drawable.ic_blank)
                        ).withIconModifier(Modifier.size(2.dp)),
                        BottomNavItem(
                            nome = "Notificações",
                            route = null,
                            badgeCount = notificacoes!!,
                            icon = ImageVector.vectorResource(id = R.drawable.ic_notificacoesindex)
                        ).withIconModifier(Modifier.size(32.dp)),
                        BottomNavItem(
                            nome = "Icone Usuário",
                            route = "Profile",
                            badgeCount = 0,
                            icon = ImageVector.vectorResource(id = R.drawable.ic_areausuario)
                        ).withIconModifier(Modifier.size(30.dp))
                    ),
                    navController = navController,
                    onClickItem = { item ->
                        item.route?.let { route ->
                            navController.navigate(route)
                        }
                    },
                    ModifierIcon = Modifier.size(35.dp),

                    )
            }
        },
        //propriedades do Scaffold
        backgroundColor = Color(0xFFFBF7F5),
        )


    //Gambiarra para colocar sombra no Button de publicar
    if (scaffoldState.drawerState.isClosed){
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val (publicar) = createRefs()
            Surface(
                shape = CircleShape,
                elevation = 10.dp,
                modifier = Modifier
                    .size(55.dp)
                    .constrainAs(publicar) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom, margin = 20.dp)
                    }
            ) {
                IconButton(
                    onClick = {
                        navController.navigate("Publicar")
                    },
                ){
                    Image(ImageVector.vectorResource(id = R.drawable.ic_publicar),
                        contentDescription = "Ir para publicar nova postagem")
                }
            }
        }
    }

    //Teste área de comentários (aqui vai ficar só o layout em si)
    if (expandirCard){
        //Pequena lógica para saber quem está tentando comentar


        var nomeUsuario = UserData.nomeEncontrado
        var apelido = remember{ mutableStateOf("") }
        var urlFoto = remember{ mutableStateOf("") }

        if (!UserData.apelidoUsuario.isNullOrEmpty()){
            apelido.value = UserData.apelidoUsuario
        }
        if (!UserData.imagemUrl.isNullOrEmpty()){
            urlFoto.value = UserData.imagemUrl
        }

        //chamando o layout com os dados necessários para subir a postagem (está no layoutsProntos)
        layoutComentarios(
            expandirCard,
            dropCard = {expandir->
                expandirCard = expandir
            },
            postagemID = postagemReferencia,
            nome = nomeUsuario,
            apelido = apelido.value,
            fotoPerfil = urlFoto.value,
        )
        println(postagemReferencia)
    }

}


@Composable
fun ListaDePostagens(postagens: List<PostagemData>, expandir: (Boolean) -> Unit, abrirFoto: (String) -> Unit, postRef : (String) -> Unit) {

    var cardState by remember { mutableStateOf(false) }

    var postagemRef  by remember { mutableStateOf("") }

    LazyColumn(){
        items(postagens) { postagemData ->
            Postagem(
                fotoPerfil = postagemData.fotoPerfil,
                nomeAutor = postagemData.nomeAutor,
                rm = postagemData.rm,
                cpsID = postagemData.cpsID,
                apelidoAutor = postagemData.apelidoAutor,
                textoPostagem = postagemData.textoPostagem,
                imagensPost = postagemData.imagensPost,
                tituloAutor = postagemData.tituloPost,
                turmasMarcadas = postagemData.turmasMarcadas,
                idPostagem = postagemData.idPostagem,
                expandir = {
                    cardState = true
                    expandir(cardState)},
                abrirFotoPostagem = {
                    abrirFoto(postagemData.fotoPerfil)
                },
                postagemRef = {postagemref ->
                    postagemRef = postagemref
                    postRef(postagemRef)
                    },
                paginas = postagemData.imagensPost.size,
                numerocurtidas = postagemData.curtidas
            )
        }
    }
}
