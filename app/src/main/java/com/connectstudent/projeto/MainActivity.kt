package com.connectstudent.projeto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.viewpager.widget.ViewPager
import com.connectstudent.projeto.viewmodel.AuthViewModel
import com.connectstudent.projeto.viewmodel.AuthViewModelCPS
import com.connectstudent.projeto.viewmodel.PublicacaoViewModel
import com.connectstudent.projeto.views.*

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Main()

        }

    }
}

@Composable
fun Main() {

    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val authViewModelCPS: AuthViewModelCPS = hiltViewModel()
    val publicacaoViewModel: PublicacaoViewModel = hiltViewModel()


    NavHost(navController = navController, startDestination = "Login"){
        //tela de login principal
        composable("Login"){
            Login(navController, authViewModel, authViewModelCPS)
        }

        //tela para os detalhes do app
        composable("Detalhes"){
            Detalhes(navController)
        }

        //tela para se registrar
        composable("Registrar"){
            Registrar(navController)
        }
        
        //tela registro alunos
        composable("RegistroAluno"){
            RegistroAluno(navController, authViewModel)
        }

        //tela registro de professores/administração
        composable("RegistroCPS"){
            RegistroCPS(navController, authViewModelCPS)
        }

        //Index
        composable("Index"){
            Index(navController, publicacaoViewModel)
        }

        //Publicar
        composable("Publicar"){
            Publicar(navController, publicacaoViewModel)
        }

        //Profile
        composable("Profile"){
            Profile(navController, publicacaoViewModel)
        }

        //Minhas publicações
        composable("MinhasPublicacoes"){
            minhasPublicacoes(navController)
        }

    }

}
