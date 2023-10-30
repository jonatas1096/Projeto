package com.connectstudent.projeto.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.connectstudent.projeto.layoutsprontos.BotaoEscolha
import androidx.compose.material.Text
import com.connectstudent.projeto.layoutsprontos.loadImage
import androidx.compose.runtime.Composable
import com.connectstudent.projeto.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import com.connectstudent.projeto.ui.theme.Dongle
import com.connectstudent.projeto.ui.theme.LARANJA


@Composable
fun Registrar(navController: NavController) {

    //Background
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        loadImage(path = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/backgroundoficial.png",
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
        val (boxPrincipal,boxSOMBRA, texto1, texto2, arrow) = createRefs()

        //Arrow voltar
        Box(modifier = Modifier.constrainAs(arrow){
            top.linkTo(parent.top, margin = 8.dp)
            start.linkTo(parent.start, margin = 18.dp)
        }
            .size(30.dp)
            .clickable(onClick = {
                navController.popBackStack()
            })
        ) {
            loadImage(path = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/arrow.png",
                contentDescription = "Icone para voltar de página",
                contentScale = ContentScale.Crop,
                modifier = Modifier)
        }


        //Começando com a gambiarra brasileira né
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
                .width(348.dp)
                .height(360.dp)
        ){}

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
                .width(348.dp)
                .height(360.dp)
                .padding(horizontal = 15.dp)
                .padding(top = 50.dp)
        ){
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                BotaoEscolha(onClick = {
                    navController.navigate("RegistroAluno")
                },
                    text = "Aluno",
                    fontSize = 34.sp,
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_aluno),
                    descricao = "Icone aluno",
                    imageVector2 = ImageVector.vectorResource(id = R.drawable.ic_play),
                    modifier = Modifier.fillMaxWidth(),
                    spacerWidth = 180.dp
                )

                //Separar os dois botões, tive que meter mais essa gambiarra tb
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                )

                BotaoEscolha(onClick = {
                    navController.navigate("RegistroCPS")
                },
                    text = "Professor e Equipe escolar",
                    fontSize = 27.sp,
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_professor),
                    descricao = "Icone professor e equipe escolar",
                    imageVector2 = ImageVector.vectorResource(id = R.drawable.ic_play),
                    modifier = Modifier.fillMaxWidth(),
                    spacerWidth = 0.dp
                )




            }

        }

        //"Logue-se aqui"

        Text(text = "Já possui uma conta?",
            fontFamily = Dongle,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            color = Color(0xFFF5E5E5E),
            modifier = Modifier
                .constrainAs(texto1) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top, margin = 30.dp)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
                //.padding(start = 30.dp)
                .clickable {
                    navController.navigate("Login")
                }
                .padding(0.dp)
        )

        Text(text = "Logue-se aqui!",
            fontFamily = Dongle,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = LARANJA,
            modifier = Modifier
                .constrainAs(texto2) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top, margin = 88.dp)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
                .clickable {
                    navController.navigate("Login")
                }
                .padding(0.dp)
        )


    }



}


