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
import com.example.projeto.views.*
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

    NavHost(navController = navController, startDestination = "RegistroCPS"){
        //tela de login principal
        composable("Login"){
            Login(navController, authViewModel)
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
            RegistroCPS(navController, authViewModel)
        }

        //Index
        composable("Index"){
            Index(navController)
        }
    }

}
