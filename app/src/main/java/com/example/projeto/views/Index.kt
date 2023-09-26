package com.example.projeto.views

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.projeto.R
import com.example.projeto.bottomNavigation.BottomNavItem
import com.example.projeto.bottomNavigation.withIconModifier
import com.example.projeto.datasource.PostagemData
import com.example.projeto.datasource.UserData
import com.example.projeto.layoutsprontos.*
import com.example.projeto.listener.ListenerPublicacao
import com.example.projeto.viewmodel.PublicacaoViewModel
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.LocalDateTime


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Index(navController: NavController, viewModel: PublicacaoViewModel = hiltViewModel()) {

    val postagensOrdenadas = remember { mutableStateListOf<PostagemData>() }


    //Variaveis para o funcionamento das publicações na index
    val scrollState = rememberScrollState()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()

    // A instância do firebase firestore:
    val firestore = Firebase.firestore  // Também funcionaria assim: val firestore = FirebaseFirestore.getInstance()


    // A instância do firebase storage e as variáveis que vou precisar:
    val storage = Firebase.storage
    val storageRef = storage.reference
    val alunoRM = UserData.rmEncontrado
    val cpsID = UserData.cpsIDEncontrado

    //Uma variavel para auxiliar no armazenamento da URL do aluno/professor
    val imagemUrl = remember { mutableStateOf<String?>(null) }


    //Aqui é primordial, é dessa forma que os dados bases (tipo RM) chegam na index.
    viewModel.usuarioEncontrado(object : ListenerPublicacao{
        override fun onSucess(rm:String, cpsID:String, nome:String) {
            println("o usuario que vem do listener tem o rm: $rm, ou o cpsID $cpsID e o nome: $nome")
            UserData.setUserData(rm, cpsID, nome)
        }
        override fun onFailure(erro: String) {
            "Nenhum usuario encontrado."
        }

    })

    // Agora uma lógica para pegar a URL da imagem que o usuário vai subir pro Firebase (eu renderizo na index para conseguir usar em qualquer lugar do app)
    if (!alunoRM.isNullOrEmpty()) {
        val alunoRef = storageRef.child("Alunos/Fotos de Perfil").child(alunoRM)
        alunoRef.downloadUrl
            .addOnSuccessListener { uri ->
                val url = uri.toString()
                println("URL obtida: $url")
                imagemUrl.value = url
                UserData.updateUrl(url)
            }
            .addOnFailureListener { exception ->
                println("A URL não pôde ser obtida. Erro: $exception")
            }
    } else if (!cpsID.isNullOrEmpty()) {
        val cpsRef = storageRef.child("CPS/Fotos de Perfil").child(cpsID)
        cpsRef.downloadUrl
            .addOnSuccessListener { uri ->
                val url = uri.toString()
                println("URL obtida: $url")
                imagemUrl.value = url
                UserData.updateUrl(url)
            }
            .addOnFailureListener { exception ->
                println("A URL não pôde ser obtida. Erro: $exception")
            }
    }


    //println("Fora: Usuario rm ${UserData.rmEncontrado}, cpsID ${UserData.cpsIDEncontrado}  nome: ${UserData.nomeEncontrado}, imagem: ${UserData.imagemUrl}")

    LaunchedEffect(Unit){
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
                    val texto = posts.getString("texto") ?: ""
                    println("Coletou o texto: $texto")
                    val titulo = posts.getString("titulo") ?: ""


                    println("Agora vai armazenar os dados na val postagemData.")
                    val postagemData = PostagemData(
                        fotoPerfil = fotoPerfil,
                        nomeAutor = nome,
                        textoPostagem = texto,
                        imagensPost = imagensPostagem,
                        tituloPost = titulo
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
                    .zIndex(2F)
            ) {

                val (publicar) = createRefs()


                //Gambiarra para colocar sombra no Button de publicar
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
                            //cardState = true
                           navController.navigate("Publicar")
                        },

                    ){
                        Image(ImageVector.vectorResource(id = R.drawable.ic_publicar),
                            contentDescription = "Ir para publicar nova postagem")
                    }

                }
            }//Fechamento do Constraint


            //Começando a lógica da área das postagens


            Column(
                modifier = Modifier.border(2.dp, Color.Red))
            {
                ListaDePostagens(postagens = postagensOrdenadas, navController = navController)
            }




        },

       bottomBar = {
           //Gambiarra para colocar sombra na bottomNavigation (a padrão dela por algum motivo nao estava indo).
           Surface(
               elevation = 6.dp,
               //shape = RoundedCornerShape(35.dp),
               modifier = Modifier
                   /*.padding(horizontal = 30.dp)
                   .padding(bottom = 15.dp)*/
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

    println("Fora: Usuario rm ${UserData.rmEncontrado}, cpsID ${UserData.cpsIDEncontrado}  nome: ${UserData.nomeEncontrado}")

}


@Composable
fun ListaDePostagens(postagens: List<PostagemData>, navController: NavController) {
    LazyColumn(){
        items(postagens) { postagemData ->
            println("Antes de criar a Postagem ${postagemData.imagensPost}")
            Postagem(
                fotoPerfil = postagemData.fotoPerfil,
                nomeAutor = postagemData.nomeAutor,
                textoPostagem = postagemData.textoPostagem,
                imagensPost = postagemData.imagensPost,
                tituloAutor = postagemData.tituloPost,
                navController = navController
            )
            println("Depois de criar a Postagem ${postagemData.imagensPost}")
        }
    }
}
