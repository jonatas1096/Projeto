package com.example.projeto.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.projeto.layoutsprontos.BotaoEscolha
import androidx.compose.material.Icon
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import com.example.projeto.layoutsprontos.loadImage
import androidx.compose.runtime.Composable
import com.example.projeto.R



@Composable
fun Registrar(navController: NavController) {
        //Eu vou testar com dois constraints porque sim.


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

        ) {
            val (boxPrincipal,boxSOMBRA,beto,coruja) = createRefs()

            /*Começando com a gambiarra brasileira né
            Surface(
                shape = RoundedCornerShape(25.dp),
                elevation = 18.dp,
                modifier = Modifier
                    .constrainAs(boxSOMBRA) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
                    .width(310.dp)
                    .height(380.dp)
            ){}*/

            //Box principal para registro
            Box(
                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(25.dp))
                    .constrainAs(boxPrincipal) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
                    .width(328.dp)
                    .height(340.dp)
                    .padding(horizontal = 20.dp)
                    .padding(top = 50.dp)
            ){
                Column {

                    BotaoEscolha(onClick = {

                    },
                        text = "Aluno",
                        fontSize = 34.sp,
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_aluno),
                        descricao = "Icone aluno",
                        imageVector2 = ImageVector.vectorResource(id = R.drawable.ic_play),
                        modifier = Modifier.fillMaxWidth()
                        )

                    //Separar os dois botões, tive que meter mais essa gambiarra tb
                    Spacer(modifier = Modifier.fillMaxWidth()
                        .height(20.dp)
                    )

                    BotaoEscolha(onClick = {

                    },
                        text = "Professor e Equipe escolar",
                        fontSize = 24.sp,
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_professor),
                        descricao = "Icone professor e equipe escolar",
                        imageVector2 = ImageVector.vectorResource(id = R.drawable.ic_play),
                        modifier = Modifier.fillMaxWidth()
                    )


                }

            }


            //Box da gambiarra de imagem para o Béto
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .constrainAs(beto) {
                        bottom.linkTo(boxPrincipal.top, margin = (-110).dp)
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
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
                        bottom.linkTo(boxPrincipal.bottom, margin = (-3).dp)
                        start.linkTo(boxPrincipal.start, margin = 232.dp)
                    }

            ) {
                loadImage(path = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/coruja1.png",
                    contentDescription = "Coruja",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier)
            }
        }



}

