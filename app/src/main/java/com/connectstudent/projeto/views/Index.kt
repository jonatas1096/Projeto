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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.navigation.NavController
import com.connectstudent.projeto.bottomNavigation.BottomNavItem
import com.connectstudent.projeto.bottomNavigation.withIconModifier
import com.connectstudent.projeto.datasource.PostagemData
import com.connectstudent.projeto.datasource.UserData
import com.connectstudent.projeto.layoutsprontos.*
import com.connectstudent.projeto.listener.ListenerPublicacao
import com.connectstudent.projeto.ui.theme.Dongle
import com.connectstudent.projeto.viewmodel.PublicacaoViewModel
import com.connectstudent.projeto.R
import com.connectstudent.projeto.ui.theme.Jomhuria
import com.connectstudent.projeto.ui.theme.LARANJA
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
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

    //Para guardar o caminho da foto de perfil (tipo uma box, e é só das postagens, nao do usuário atual).
    var fotoUsuario by remember { mutableStateOf<String?>("") }

    //Aqui é primordial, é dessa forma que os dados bases (tipo RM) chegam na index.
    LaunchedEffect(Unit){

        scope.launch { //scopo principal

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
            }
            delay(1500) //esse delay serve para dar tempo do rm ser guardado na classe UserData e conserguirmos fazer as lógicas abaixo.


            scope.launch {
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
                            var Comentarios = 0

                            if (posts.contains("comentarios")){
                                val comentarios = posts.get("comentarios")as? List<String>
                                val numeroComentarios = comentarios?.size ?: 0
                                Comentarios = numeroComentarios
                            }


                            //Buscando a imagem em tempo real do usuario que postou (aqui é só para as postagens)
                            if (!rm.isNullOrEmpty()){ //nao está vazio.
                                val storageRef = storage.reference.child("Alunos/Fotos de Perfil/$rm")
                                storageRef.downloadUrl
                                    .addOnSuccessListener { uri ->
                                        val imageUrl = uri.toString()
                                        fotoUsuario = imageUrl
                                        println("chegou assim: $fotoUsuario")
                                    }
                                    .addOnFailureListener { exception ->
                                        println("Erro ao obter o URL da foto de perfil: $exception")
                                    }
                            }else{
                                val storageRef = storage.reference.child("CPS/Fotos de Perfil/$cpsID")
                                storageRef.downloadUrl
                                    .addOnSuccessListener { uri ->
                                        val imageUrl = uri.toString()
                                        fotoUsuario = imageUrl
                                        println("chegou assim: $fotoUsuario")
                                    }
                                    .addOnFailureListener { exception ->
                                        println("Erro ao obter o URL da foto de perfil: $exception")
                                    }
                            }

                            println("Agora vai armazenar os dados na val postagemData.")
                            println("pronto para armazenar: $fotoUsuario")
                            val postagemData = PostagemData(
                                fotoPerfil = if (fotoUsuario.isNullOrEmpty()) "" else fotoUsuario!!,
                                nomeAutor = nome,
                                rm = rm,
                                cpsID = cpsID,
                                //apelidoAutor = apelido,
                                textoPostagem = texto,
                                imagensPost = imagensPostagem,
                                tituloPost = titulo,
                                turmasMarcadas = turmas,
                                idPostagem = idPost,
                                curtidas = Curtidas,
                                comentarios = Comentarios,
                            )

                            postagensData.add(postagemData) //O conteúdo da postagem está todo aqui
                            println("O id do post é $idPost")
                        }
                        postagensOrdenadas.clear()
                        postagensOrdenadas.addAll(postagensData)
                    }
                    .addOnFailureListener{erro ->
                        println("Não foi possivel coletar os dados $erro")
                    }
            }


            //

            scope.launch {
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
            }


            delay(500)

            scope.launch {
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
            }

            delay(950)
            println("passou o delay.")
            urlBaixada = true // a lógica pro progressIndicator
            indexState = false
        }
    }


    var expandirCard by remember { mutableStateOf(false) }
    var abrirFoto by remember { mutableStateOf(false) }
    var caminhoImagem by remember { mutableStateOf("") }
    var postagemReferencia by remember { mutableStateOf("") }
    var refExpandirCard by remember { mutableStateOf("") } //Essa aqui foi a solução que achei para abrir o card com a referencia da postagem

    if (indexState){
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(255, 255, 255, 163))
        ){
            val (circularProgress, logo, nomes) = createRefs()
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

            Column(
                modifier = Modifier.constrainAs(nomes){
                    top.linkTo(circularProgress.bottom, margin = 12.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            ) {
                Text(
                    text = "Anahi Mamani",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Beatriz Witer",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Joissi Airane",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Jonatas Bahia",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )

            }
        }
    }
    else{
        val pagerState = remember { mutableStateOf(0) }
        val tabTitles = listOf("Geral", "Para voce")
        var pubState = remember { mutableStateOf(false) }

        //Começo do layout
        Scaffold(
            scaffoldState = scaffoldState,

            topBar = {
                Column() {
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
                                            contentDescription = "DrawerContent",)
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

                    }

                    TabRow(
                        selectedTabIndex = pagerState.value,
                        backgroundColor = Color(0xFFFBF7F5),
                    ) {
                        tabTitles.forEachIndexed { index, title ->
                            Tab(
                                selected = pagerState.value == index ,
                                onClick = {
                                    scope.launch {
                                        pagerState.value = index
                                    }
                                },
                            ) {
                                Text(
                                    text = title,
                                    fontSize = 34.sp,
                                    fontFamily = Jomhuria,

                                )
                            }

                        }
                    }
                }
                //fechamento TopBar
            },
            drawerContent = { drawerPersonalizado(urlBaixada,navController)}, //Aqui é o drawerContent que fica do lado esquerdo (menuzinho).
            drawerBackgroundColor = Color.White,

            //Tô usando o content para mesclar o constraintLayout à aplicação em geral, assim ele fica em cima da bottomBar (tipo camadas).
            content = {
                when (pagerState.value) {
                    0 -> Geral(abrirFoto, caminhoImagem, postagensOrdenadas, postagemReferencia,
                        onExpandir = {expandir ->
                        expandirCard = expandir
                    },
                        refUnica = {ref ->
                            refExpandirCard = ref
                        }
                    )


                    1 -> ParaVoce(abrirFoto, caminhoImagem, postagensOrdenadas, postagemReferencia,
                        onExpandir = {expandir ->
                            expandirCard = expandir
                        },
                        refUnica = {ref ->
                            refExpandirCard = ref
                        }
                    )
                }


            },

            bottomBar = {
                var dialog by remember{ mutableStateOf(false) }
                val context = LocalContext.current
                //Gambiarra para colocar sombra na bottomNavigation (a padrão dela por algum motivo nao estava indo).
                Surface(
                    elevation = 6.dp,
                    modifier = Modifier
                        .height(55.dp)
                ) {
                    if(!UserData.cpsIDEncontrado.isNullOrEmpty()){ // " ! " para negação, ou seja, não está vazio.
                        BottomNavigationBar(
                            items = listOf(
                                BottomNavItem(
                                    nome = "Home",
                                    route = "Index",
                                    badgeCount = 0,
                                    icon = ImageVector.vectorResource(id = R.drawable.ic_home),
                                ).withIconModifier(
                                    Modifier
                                        .size(25.dp)),
                                BottomNavItem(
                                    nome = "Chat",
                                    route = null,
                                    badgeCount = 4,
                                    icon = ImageVector.vectorResource(id = R.drawable.ic_chat)
                                ).withIconModifier(
                                    Modifier
                                        .size(32.dp)
                                        .padding(end = 2.dp)
                                        .clickable {
                                            Toast
                                                .makeText(context, "Em breve!", Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                ),
                                BottomNavItem( //esse é uma gambiarra daquelas kkkk
                                    nome = "Publicar",
                                    route = "Publicar",
                                    badgeCount = 0,
                                    icon = ImageVector.vectorResource(id = R.drawable.ic_publicar)
                                ).withIconModifier(Modifier.size(30.dp)),
                                BottomNavItem(
                                    nome = "Notificações",
                                    route = null,
                                    badgeCount = notificacoes!!,
                                    icon = ImageVector.vectorResource(id = R.drawable.ic_notificacoesindex)
                                ).withIconModifier(
                                    Modifier
                                        .size(32.dp)
                                        .clickable { dialog = true }),
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
                    }else{
                        BottomNavigationBar(
                            items = listOf(
                                BottomNavItem(
                                    nome = "Home",
                                    route = "Index",
                                    badgeCount = 0,
                                    icon = ImageVector.vectorResource(id = R.drawable.ic_home),
                                ).withIconModifier(
                                    Modifier
                                        .size(25.dp)),
                                BottomNavItem(
                                    nome = "Chat",
                                    route = null,
                                    badgeCount = 4,
                                    icon = ImageVector.vectorResource(id = R.drawable.ic_chat)
                                ).withIconModifier(
                                    Modifier
                                        .size(32.dp)
                                        .padding(end = 2.dp)
                                        .clickable {
                                            Toast
                                                .makeText(context, "Em breve!", Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                ),
                                BottomNavItem(
                                    nome = "Notificações",
                                    route = null,
                                    badgeCount = notificacoes!!,
                                    icon = ImageVector.vectorResource(id = R.drawable.ic_notificacoesindex)
                                ).withIconModifier(
                                    Modifier
                                        .size(32.dp)
                                        .clickable { dialog = true }),
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

                    if (dialog){
                        limparNotificacoes(
                            onDismiss = {dialog = false}, //Esse aqui é só para o de clicar fora do dialog. É para nao misturar, existem 3 onDismiss.
                            onDismissRequest = {
                                dialog = false
                                pagerState.value = 1
                                notificacoes = 0
                            },
                            onZerado = {dialog = false},
                            notificacoes!!,
                            navController
                        )
                    }
                }
            },
            //propriedades do Scaffold
            backgroundColor = Color(0xFFFBF7F5),
        )


    /*    //Gambiarra para colocar sombra no Button de publicar
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
        } */

        //Teste área de comentários (aqui vai ficar só o layout em si)
        if (expandirCard){

            //Pequena lógica para saber quem está tentando comentar
            var nomeUsuario = UserData.nomeEncontrado
            var apelido = remember{ mutableStateOf("") }
            var urlFoto = remember{ mutableStateOf("") }
            val imagemPadrao = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/imagemdefault.jpg"
            if (!UserData.apelidoUsuario.isNullOrEmpty()){
                apelido.value = UserData.apelidoUsuario
            }

            //Aqui é exclusivo para buscar a imagem direto do storage em tempo real.
            if (!UserData.rmEncontrado.isNullOrEmpty()){ //nao está vazio.
                val storageRefExpandir = storage.reference.child("Alunos/Fotos de Perfil/${UserData.rmEncontrado}")
                storageRefExpandir.downloadUrl
                    .addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()
                        urlFoto.value = imageUrl
                    }
                    .addOnFailureListener { exception ->
                        println("Erro ao obter uma URL de foto de perfil: $exception")
                        println("vamos deixar a imagem padrao.")
                        urlFoto.value = imagemPadrao
                    }
            }else{
                val storageRefExpandir = storage.reference.child("CPS/Fotos de Perfil/${UserData.cpsIDEncontrado}")
                storageRefExpandir.downloadUrl
                    .addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()
                        urlFoto.value = imageUrl
                        println("chegou assim: $fotoUsuario")
                    }
                    .addOnFailureListener { exception ->
                        println("Erro ao obter uma URL de foto de perfil: $exception")
                        println("vamos deixar a imagem padrao.")
                        urlFoto.value = imagemPadrao
                    }
            }

            layoutComentarios(
                expandirCard,
                dropCard = {expandir->
                    expandirCard = expandir
                },
                postagemID = refExpandirCard,
                nome = nomeUsuario,
                apelido = apelido.value,
                fotoPerfil = urlFoto.value,
            )
        }

        if (pubState.value){
            navController.navigate("MinhasPublicacoes")
        }
    }


}


//Aqui é a lista de postagens em Geral
@Composable
fun PostagensGerais(postagens: List<PostagemData>, expandir: (Boolean) -> Unit, abrirFoto: (String) -> Unit, postRef : (String) -> Unit) {

    var cardState by remember { mutableStateOf(false) }

    var postagemRef  by remember { mutableStateOf("") }

    var imagemPadrao = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/imagemdefault.jpg"
    LazyColumn(){
        items(postagens) { postagemData ->
            var fotoPerfil by remember { mutableStateOf("") }

            Postagem(
                fotoPerfil = {url ->
                        fotoPerfil = url!!
                },
                nomeAutor = postagemData.nomeAutor,
                rm = postagemData.rm,
                cpsID = postagemData.cpsID,
                textoPostagem = postagemData.textoPostagem,
                imagensPost = postagemData.imagensPost,
                tituloAutor = postagemData.tituloPost,
                turmasMarcadas = postagemData.turmasMarcadas,
                idPostagem = postagemData.idPostagem,
                expandir = {
                    cardState = true
                    expandir(cardState)},
                abrirFotoPerfil = {
                    if (!fotoPerfil.isNullOrEmpty()){ //nao está vazia
                        abrirFoto(fotoPerfil)
                    }else{
                        abrirFoto(imagemPadrao)
                    }
                },
                postagemRef = {postagemref ->
                    postagemRef = postagemref
                    postRef(postagemRef)
                    },
                numerocurtidas = postagemData.curtidas,
                numerocomentarios = postagemData.comentarios
            )
        }
    }
}


//Aqui é as postagens para turmas especificas
@Composable
fun PostagensTurmas(postagens: List<PostagemData>, expandir: (Boolean) -> Unit, abrirFoto: (String) -> Unit, postRef : (String) -> Unit, postsExistente:(Boolean) -> Unit) {

    var cardState by remember { mutableStateOf(false) }

    var postagemRef  by remember { mutableStateOf("") }
    var postsCont by remember { mutableStateOf(false) }

    var imagemPadrao = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/imagemdefault.jpg"
    LazyColumn(){
        items(postagens) { postagemData ->
            var fotoPerfil by remember { mutableStateOf("") }

            if (postagemData.turmasMarcadas.contains(UserData.turmaEncontrada)){
                postsCont = true //existe ao menos uma postagem, entao vamos retornar true
                Postagem(
                    fotoPerfil = {url ->
                        fotoPerfil = url!!
                    },
                    nomeAutor = postagemData.nomeAutor,
                    rm = postagemData.rm,
                    cpsID = postagemData.cpsID,
                    textoPostagem = postagemData.textoPostagem,
                    imagensPost = postagemData.imagensPost,
                    tituloAutor = postagemData.tituloPost,
                    turmasMarcadas = postagemData.turmasMarcadas,
                    idPostagem = postagemData.idPostagem,
                    expandir = {
                        cardState = true
                        expandir(cardState)},
                    abrirFotoPerfil = {
                        if (!fotoPerfil.isNullOrEmpty()){ //nao está vazia
                            abrirFoto(fotoPerfil)
                        }else{
                            abrirFoto(imagemPadrao)
                        }
                    },
                    postagemRef = {postagemref ->
                        postagemRef = postagemref
                        postRef(postagemRef)
                    },
                    numerocurtidas = postagemData.curtidas,
                    numerocomentarios = postagemData.comentarios
                )
                postsExistente(postsCont)
            }
        }
    }
}

@Composable
fun MinhasPostagens(postagens: List<PostagemData>, expandir: (Boolean) -> Unit, abrirFoto: (String) -> Unit, postRef : (String) -> Unit, postsBack:(Int) -> Unit) {

    var postagemRef  by remember { mutableStateOf("") }
    var postsCont = 0

    var cardState by remember { mutableStateOf(false) }
    var imagemPadrao = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/imagemdefault.jpg"

    LazyColumn(){
        items(postagens) { postagemData ->
            var fotoPerfil by remember { mutableStateOf("") }

            if (!UserData.rmEncontrado.isNullOrEmpty()){
                if (postagemData.rm.contains(UserData.rmEncontrado)){
                    Postagem(
                        fotoPerfil = {url ->
                            fotoPerfil = url!!
                        },
                        nomeAutor = postagemData.nomeAutor,
                        rm = postagemData.rm,
                        cpsID = postagemData.cpsID,
                        //apelidoAutor = postagemData.apelidoAutor,
                        textoPostagem = postagemData.textoPostagem,
                        imagensPost = postagemData.imagensPost,
                        tituloAutor = postagemData.tituloPost,
                        turmasMarcadas = postagemData.turmasMarcadas,
                        idPostagem = postagemData.idPostagem,
                        expandir = {
                            cardState = true
                            expandir(cardState)},
                        abrirFotoPerfil = {
                            if (!fotoPerfil.isNullOrEmpty()){ //nao está vazia
                                abrirFoto(fotoPerfil)
                            }else{
                                abrirFoto(imagemPadrao)
                            }
                        },
                        postagemRef = {postagemref ->
                            postagemRef = postagemref
                            postRef(postagemRef)
                        },
                        numerocurtidas = postagemData.curtidas,
                        numerocomentarios = postagemData.comentarios
                    )
                    postsCont++
                    postsBack(postsCont)
                }
            }else{
                if (postagemData.cpsID.contains(UserData.cpsIDEncontrado)){
                    Postagem(
                        fotoPerfil = {url ->
                            fotoPerfil = url!!
                        },
                        nomeAutor = postagemData.nomeAutor,
                        rm = postagemData.rm,
                        cpsID = postagemData.cpsID,
                        //apelidoAutor = postagemData.apelidoAutor,
                        textoPostagem = postagemData.textoPostagem,
                        imagensPost = postagemData.imagensPost,
                        tituloAutor = postagemData.tituloPost,
                        turmasMarcadas = postagemData.turmasMarcadas,
                        idPostagem = postagemData.idPostagem,
                        expandir = {}, //nao precisamos usar
                        abrirFotoPerfil = {
                            if (!fotoPerfil.isNullOrEmpty()){ //nao está vazia
                                abrirFoto(fotoPerfil)
                            }else{
                                abrirFoto(imagemPadrao)
                            }
                        },
                        postagemRef = {postagemref ->
                            postagemRef = postagemref
                            postRef(postagemRef)
                        },
                        numerocurtidas = postagemData.curtidas,
                        numerocomentarios = postagemData.comentarios
                    )
                    postsCont++
                    postsBack(postsCont)
                }
            }

        }
    }
}
@Composable
fun Geral(abrir:(Boolean),caminho:(String), postagens: List<PostagemData>, postagemRef:(String), onExpandir:(Boolean) -> Unit, refUnica:(String) -> Unit){

    var abrirFoto by remember{ mutableStateOf(abrir) }
    var caminhoImagem by remember { mutableStateOf(caminho) }
    val postagensOrdenadas = remember { postagens}
    var postagemReferencia by remember { mutableStateOf(postagemRef) }
    var expandirCard by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
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
                PostagensGerais(
                    postagens = postagensOrdenadas,
                    postRef = { postagemRef ->
                        postagemReferencia = postagemRef
                        refUnica(postagemReferencia) //solução para o abrir card
                    },
                    expandir = {resultado ->
                        expandirCard = resultado
                        onExpandir(expandirCard)
                               },
                    abrirFoto = { resultado ->
                        caminhoImagem = resultado
                        abrirFoto = !abrirFoto
                    },
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
        }//Fechamento do segundo Constraint
    }
}

@Composable
fun ParaVoce(abrir:(Boolean),caminho:(String), postagens: List<PostagemData>, postagemRef:(String), onExpandir:(Boolean) -> Unit, refUnica:(String) -> Unit) {
    var abrirFoto by remember{ mutableStateOf(abrir) }
    var caminhoImagem by remember { mutableStateOf(caminho) }
    val postagensOrdenadas = remember { postagens}
    var postagemReferencia by remember { mutableStateOf(postagemRef) }
    var expandirCard by remember { mutableStateOf(false) }
    var quantidadePostagens = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
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

                PostagensTurmas(
                    postagens = postagensOrdenadas,
                    postRef = { postagemRef ->
                        postagemReferencia = postagemRef
                        refUnica(postagemReferencia) //solução para o abrir card
                    },
                    expandir = {resultado ->
                        expandirCard = resultado
                        onExpandir(expandirCard)},
                    abrirFoto = { resultado ->
                        caminhoImagem = resultado
                        abrirFoto = !abrirFoto
                    },
                    postsExistente = {contagem ->
                        quantidadePostagens.value = contagem
                    }
                )
                println("quantidade de postagens é $quantidadePostagens")

                if (!quantidadePostagens.value){
                    println("entrou: $quantidadePostagens")
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Nada para você ainda 😓", fontSize = 40.sp, fontFamily = Jomhuria)
                    }
                }
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
        }//Fechamento do segundo Constraint
    }
}