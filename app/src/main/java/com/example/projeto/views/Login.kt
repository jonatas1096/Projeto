package com.example.projeto.views

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.projeto.R
import com.example.projeto.layoutsprontos.loadImage


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Login() {


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Sou aluno")
                },
                contentColor = Color.White
            )
        },
        modifier = Modifier.fillMaxSize(),

    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ){
            loadImage(
                path = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/backgroundlaranja.png",
                contentDescription = "Background Laranja",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()

            )
        }

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            loadImage(path = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/capeloaluno.png",
                contentDescription = "Capelo Aluno",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(10.dp)
            )

        }



    }
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Login()
}