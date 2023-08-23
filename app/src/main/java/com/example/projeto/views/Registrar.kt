package com.example.projeto.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.projeto.layoutsprontos.BotaoEscolha
import com.example.projeto.layoutsprontos.loadImage
import com.example.projeto.ui.theme.Dongle


@Composable
fun Registrar(navController: NavController) {
        //Eu vou testar com dois constraints porque sim.

        var escolhaTextField by remember { mutableStateOf("Aluno") }
        var escolhaTextField2 by remember { mutableStateOf("Professor e Equipe Escolar") }

        //Background
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
           loadImage(path = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/backgroundprovisorio.png",
               contentDescription = "Background Registrar",
               contentScale = ContentScale.Crop,
               modifier = Modifier.fillMaxSize()
           )
        }


        //Constraint principal de escolha
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(35.dp, 220.dp, 35.dp, 240.dp)

        ) {
            val (box,boxSOMBRA) = createRefs()

            //Começando com a gambiarra brasileira né
            Surface(
                shape = RoundedCornerShape(25.dp),
                elevation = 18.dp,
                modifier = Modifier
                    .fillMaxSize()
                    .constrainAs(boxSOMBRA) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }

            ){}

            //Box principal para registro
            Box(
                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(25.dp))
                    .padding(horizontal = 25.dp)
                    .padding(top = 40.dp)
                    .fillMaxSize()
                    .constrainAs(box) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
            ){
                Column(

                ) {
                    BotaoEscolha(onClick = {

                    },
                        text = "Aluno",
                    )


                    BotaoEscolha(onClick = {

                    },
                        text = "Professor e Equipe Escolar",
                        fontSize = 28   .sp
                    )
                }

            }


        }



    //Constraint das imagens
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (beto,coruja) = createRefs()



        //Box da gambiarra de imagem para o Béto
        Box(
            modifier = Modifier
                .size(130.dp)
                .constrainAs(beto) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom, margin = 510.dp)
                    end.linkTo(parent.end)
                }
        ) {
            loadImage(path = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/coala1.png",
                contentDescription = "Béto",
                contentScale = ContentScale.Crop,
                modifier = Modifier)
        }

        //Box da gambiarra de imagem para a Coruja-ruja
        Box(
            modifier = Modifier
                .size(150.dp)
                .constrainAs(coruja) {
                    top.linkTo(parent.top, margin = 230.dp)
                    start.linkTo(parent.start, margin = 290.dp)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }

        ) {
            loadImage(path = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/coruja1.png",
                contentDescription = "Coruja",
                contentScale = ContentScale.Fit,
                modifier = Modifier)
        }
    }
}

