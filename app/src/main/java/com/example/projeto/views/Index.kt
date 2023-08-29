package com.example.projeto.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun Index(navController: NavController) {
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue)
    ) {
        Text(text = "Isso é tudo.",
        fontSize = 30.sp,
        color = Color.White)

        Text(text = "Eu preciso descansar, sair, fazer o que eu gosto.", fontSize = 30.sp,
            color = Color.White)
        Text(text = "Esse final de semana eu vou S-A-I-R", fontSize = 40.sp,
            color = Color.White)
    }
}
