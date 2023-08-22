package com.example.projeto.views

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.projeto.R
import com.example.projeto.layoutsprontos.OutlinedLogin
import com.example.projeto.layoutsprontos.loadImage
import com.example.projeto.ui.theme.Dongle
import com.example.projeto.ui.theme.Jomhuria
import com.example.projeto.ui.theme.LARANJA



fun Registrar(navController: NavController) {


    @Composable
    fun Registrar(){

        //Background
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
           loadImage(path = "",
               contentDescription = "Background Registrar",
               contentScale = ContentScale.Crop,
               modifier = Modifier.fillMaxSize()
           )
        }

        //Constraint
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {


        }
    }

}