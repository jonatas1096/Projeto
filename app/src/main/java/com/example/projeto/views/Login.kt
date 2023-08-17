package com.example.projeto.views

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.projeto.layoutsprontos.loadImage


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Login() {
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


        ConstraintLayout(
            modifier = Modifier.fillMaxSize()

        ) {
            val (capeloBox,areaLogin) = createRefs()

            //Estava tendo problemas com o tamanho da imagem, então coloquei dentro de uma box e scalonei pela box:
            Box(
                modifier = Modifier
                    .constrainAs(capeloBox) {
                        top.linkTo(parent.top, margin = 50.dp)
                        start.linkTo(parent.start, margin = 100.dp)
                        end.linkTo(parent.end, margin = 100.dp)
                    }
                    .size(150.dp)
            ) {
                loadImage(path = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/capelo.png",
                    contentDescription = "Capelo de aluno",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                )
            }

            //Box que vai guardar email e senha:
            Box(
                modifier = Modifier
                    .constrainAs(areaLogin) {
                        top.linkTo(parent.top, margin = 130.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .width(310.dp)
                    .height(430.dp)
                    .background(Color.White)

            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    Text(text = "Bem vindo!",
                        color = Color.Black,
                        fontSize = 26.sp)

                    Text(text = "Lorem!",
                        color = Color(49, 48, 48, 255),
                        fontSize = 26.sp,
                    modifier = Modifier.padding(top = 10.dp))
                }

            }


        }




}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Login()
}