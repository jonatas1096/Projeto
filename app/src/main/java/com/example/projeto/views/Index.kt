package com.example.projeto.views

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.projeto.R
import com.example.projeto.bottomNavigation.BottomNavItem
import com.example.projeto.bottomNavigation.withIconModifier
import com.example.projeto.datasource.UserData
import com.example.projeto.layoutsprontos.*
import com.example.projeto.listener.ListenerPublicacao
import com.example.projeto.viewmodel.PublicacaoViewModel
import kotlinx.coroutines.launch



@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Index(navController: NavController, viewModel: PublicacaoViewModel = hiltViewModel()) {

    val scrollState = rememberScrollState()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()

    var usuarioObtido = ""
    var nomeObtido = ""

   /* //Variaveis que fazem parte da identificação do usuário
    var usuarioEncontrado = ""
    var nomeEncontrado = ""
    //
    viewModel.usuarioEncontrado(object : ListenerPublicacao{
        override fun onSucess(usuario: String, nome:String) {
            usuarioEncontrado = usuario
            println("O código é (agora ja na index) $usuarioEncontrado")
            nomeEncontrado = nome
            println("nome: $nomeEncontrado")
        }

        override fun onFailure(erro: String) {
            "Nenhum usuario encontrado."
        }

    })*/
    viewModel.usuarioEncontrado(object : ListenerPublicacao{
        override fun onSucess(rm:String, cpsID:String, nome:String) {
            println("o usuario que vem do listener tem o rm: $rm, ou o cpsID $cpsID e o nome: $nome")
            UserData.setUserData(rm, cpsID, nome)
        }
        override fun onFailure(erro: String) {
            "Nenhum usuario encontrado."
        }

    })

    println("Fora: Usuario rm ${UserData.rmEncontrado}, cpsID ${UserData.cpsIDEncontrado}  nome: ${UserData.nomeEncontrado}")

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
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                Postagem()
                Postagem()
                Postagem()
                Postagem()
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
