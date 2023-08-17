package com.example.projeto.views

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                contentColor = Color.Black,
                backgroundColor = Color.White
            )
        },
        modifier = Modifier.fillMaxSize(),

    ) {
        
        //Box para scalonar o background laranja
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
        //

        
        
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = 45.dp)
                .padding(bottom = 70.dp)
        ) {
            //Já que eu estava tendo problemas com o tamanho da imagem, coloquei ela dentro de uma box e modifiquei o tamanho da box em si:

            Box(
                modifier = Modifier
                    .size(140.dp)
                    .padding(top = 30.dp)
            ){

                loadImage(path = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/capelo.png",
                    contentDescription = "Capelo Aluno",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                )
            }

            Spacer(modifier = Modifier.height(70.dp)) //isso é para dar um espaço entre as boxs

            //Box branca que ficará os textfields:
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White,
                        shape = RoundedCornerShape(15.dp)
                    )

            ){
                Text(text = "A box está aqui")
            }

         }



    }
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Login()
}