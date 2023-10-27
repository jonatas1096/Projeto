package com.example.projeto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projeto.viewmodel.AuthViewModel
import com.example.projeto.viewmodel.AuthViewModelCPS
import com.example.projeto.viewmodel.PublicacaoViewModel
import com.example.projeto.views.*
import com.google.firebase.FirebaseApp
import android.app.Application
import com.example.projeto.layoutsprontos.Postagem
import com.google.firebase.firestore.FirebaseFirestore

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

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


    NavHost(navController = navController, startDestination = "Index"){
        //tela de login principal
        composable("Login"){
            Login(navController, authViewModel, authViewModelCPS)
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

        //Profile (estudos Anahi)
        composable("Profile"){
            Profile(navController, publicacaoViewModel)
        }

    }

}
