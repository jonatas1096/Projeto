package com.example.projeto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projeto.views.Login
import com.example.projeto.views.Registrar
import com.example.projeto.views.RegistroAluno

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

<<<<<<< HEAD
    NavHost(navController = navController, startDestination = "Login"){
=======
    NavHost(navController = navController, startDestination = "RegistroAluno"){
>>>>>>> 4e4e55f91197b7476c5b59d0d3ad30e1994c0c11
        //tela de login principal
        composable("Login"){
            Login(navController)
        }

        //tela para se registrar
        composable("Registrar"){
            Registrar(navController)
        }
        
        //tela registro alunos
        composable("RegistroAluno"){
            RegistroAluno(navController)
        }
    }

}
