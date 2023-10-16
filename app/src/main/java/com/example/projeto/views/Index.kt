package com.example.projeto.views

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.projeto.R
import com.example.projeto.bottomNavigation.BottomNavItem
import com.example.projeto.bottomNavigation.withIconModifier
import com.example.projeto.datasource.PostagemData
import com.example.projeto.datasource.UserData
import com.example.projeto.layoutsprontos.*
import com.example.projeto.listener.ListenerPublicacao
import com.example.projeto.ui.theme.Dongle
import com.example.projeto.ui.theme.Jomhuria
import com.example.projeto.viewmodel.PublicacaoViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch



@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Index(navController: NavController, viewModel: PublicacaoViewModel = hiltViewModel()) {

    val postagensOrdenadas = remember { mutableStateListOf<PostagemData>() }


    //Variaveis para o funcionamento das publicações na index
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()

    // A instância do firebase firestore:
    val firestore = Firebase.firestore  // Também funcionaria assim: val firestore = FirebaseFirestore.getInstance()
    //Instância do authentication:
    val auth = FirebaseAuth.getInstance()

    // A instância do firebase storage e as variáveis que vou precisar:
    val UIDref = FirebaseAuth.getInstance().currentUser?.uid.toString()
    val email = auth.currentUser?.email

    //Lógica do carregamento
    var indexState by remember{ mutableStateOf(true) }
    println("Index state valor inicial: $indexState")





    LaunchedEffect(Unit){
    //Aqui é primordial, é dessa forma que os dados bases (tipo RM) chegam na index.
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



        println("Entrou no LaunchedEffect e vai coletar os dados do firebase.")
        val postagensRef = firestore.collection("Postagens")

        val filaOrdenar = postagensRef
            .orderBy("ultimaAtualizacao", Query.Direction.DESCENDING)

        filaOrdenar.get()
            .addOnSuccessListener {postagens ->
                println("Entrou no onSucess (ordenado os dados)")
                println("Tamanho de postagens: ${postagens.size()}")
                val postagensData = mutableListOf<PostagemData>()
                for (posts in postagens){
                    println("Entrou no for.")
                    val fotoPerfil = posts.getString("fotoPerfil") ?: ""
                    println("Coletou uma foto de perfil $fotoPerfil")
                    val imagensPostagem = posts.get("imagensPostagem") as? List<String> ?: emptyList()
                    println("Coletou as seguintes imagens: $imagensPostagem")
                    val nome = posts.getString("nome") ?: ""
                    println("Coletou o nome: $nome")
                    val rm = posts.getString("RM") ?: ""
                    println("Coletou o rm: $rm")
                    val apelido = posts.getString("apelido") ?: ""
                    println("Coletou o apelido: $apelido")
                    val texto = posts.getString("texto") ?: ""
                    println("Coletou o texto: $texto")
                    val titulo = posts.getString("titulo") ?: ""
                    println("Coletou o titulo: $titulo")
                    val turmas = posts.get("turmasMarcadas") as? List<String> ?: emptyList()
                    println("Coletou as turmas: $turmas")

                    println("Agora vai armazenar os dados na val postagemData.")
                    val postagemData = PostagemData(
                        fotoPerfil = fotoPerfil,
                        nomeAutor = nome,
                        rm = rm,
                        apelidoAutor = apelido,
                        textoPostagem = texto,
                        imagensPost = imagensPostagem,
                        tituloPost = titulo,
                        turmasMarcadas = turmas,
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

    }


    Scaffold(
        scaffoldState = scaffoldState,

       topBar = {
           TopAppBar(
               backgroundColor = Color(0xFFFAFAFA),
           ) {

           } //fechamento TopBar
            botaoDrawer(
                onClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                }
            )
       },
        drawerContent = { drawerPersonalizado(navController) },
        drawerBackgroundColor = Color.White,

        //Tô usando o content para mesclar o constraintLayout à aplicação em geral, assim ele fica em cima da bottomBar (tipo camadas).
        content = {

            //ConstraintLayout para o que precisar ser posicionado melhor
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                val (paravoce, linhaestetica, postagens) = createRefs()

                //Começando a lógica da área das postagens
                Row(
                    modifier = Modifier
                        .constrainAs(paravoce) {
                            top.linkTo(parent.top)
                        }
                        .fillMaxWidth()
                        .padding(top = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    Text(text = "Para você",
                        fontSize = 36.sp,
                        fontFamily = Dongle,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(text = "Minhas postagens",
                        fontSize = 34.sp,
                        fontFamily = Dongle,
                        color = Color.Black
                    )
                }

                Row(
                    modifier = Modifier
                        .constrainAs(linhaestetica) {
                            top.linkTo(paravoce.bottom)
                        }
                        .fillMaxWidth()
                        .size(8.dp)
                        .background(color = Color(209, 209, 209, 255))
                ) {}


                Column(
                    modifier = Modifier
                        .constrainAs(postagens) {
                            top.linkTo(linhaestetica.bottom)
                        }
                        .padding(bottom = 110.dp)
                )
                {
                    Column() {
                        ListaDePostagens(postagens = postagensOrdenadas, navController = navController)
                        //Um spacer para separar a ultima postagem do menu de icones
                    }
                    indexState = false
                    println("Index state após as postagens: $indexState")
                }

            }//Fechamento do Constraint



        },

       bottomBar = {
           //Gambiarra para colocar sombra na bottomNavigation (a padrão dela por algum motivo nao estava indo).
           Surface(
               elevation = 6.dp,
               //shape = RoundedCornerShape(35.dp),
               modifier = Modifier
                   .height(55.dp)
           ) {
               BottomNavigationBar(
                   items = listOf(
                       BottomNavItem(
                           nome = "Home",
                           route = "Index",
                           badgeCount = 0,
                           icon = ImageVector.vectorResource(id = R.drawable.ic_home)
                       ).withIconModifier(Modifier.size(25.dp)),
                       BottomNavItem(
                           nome = "Chat",
                           route = "Chat",
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
                       ).withIconModifier(Modifier.size(10.dp)),
                       BottomNavItem(
                           nome = "Notificações",
                           route = "Notificacoes",
                           badgeCount = 210,
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
        backgroundColor = Color.White
    )

    //Gambiarra para colocar sombra no Button de publicar
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
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


@Composable
fun ListaDePostagens(postagens: List<PostagemData>, navController: NavController) {
    var expandir by remember { mutableStateOf(false) }


    if (expandir != true){
        LazyColumn(
            modifier = Modifier.clickable(
                onClick = {
                    expandir = true
                }
            )
        ){
            items(postagens) { postagemData ->
                println("Antes de criar a Postagem ${postagemData.imagensPost}")
                Postagem(
                    fotoPerfil = postagemData.fotoPerfil,
                    nomeAutor = postagemData.nomeAutor,
                    rm = postagemData.rm,
                    apelidoAutor = postagemData.apelidoAutor,
                    textoPostagem = postagemData.textoPostagem,
                    imagensPost = postagemData.imagensPost,
                    tituloAutor = postagemData.tituloPost,
                    turmasMarcadas = postagemData.turmasMarcadas,
                    paginas = postagemData.imagensPost.size,
                )
                println("Depois de criar a Postagem ${postagemData.imagensPost}")
            }
        }
    }
    else{
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Magenta)
        ) {

        }
    }

}
