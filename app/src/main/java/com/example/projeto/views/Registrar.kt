package com.example.projeto.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.projeto.layoutsprontos.loadImage



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
                .padding(35.dp, 220.dp, 35.dp, 240.dp)

        ) {
            val (box,beto) = createRefs()

            //Box principal para registro
            Box(
                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(25.dp))
                    .fillMaxSize()
                    .constrainAs(box) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }

            ){

            }

        }



    //Constraint das imagens
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (beto) = createRefs()



        //Box da gambiarra de imagem
        Box(
            modifier = Modifier.size(140.dp)
                .constrainAs(beto){
                    top.linkTo(parent.top, margin = 60.dp)
                }
                .border(2.dp, Color.Black)
        ) {
            loadImage(path = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/betoprovisorio.png",
                contentDescription = "Béto",
                contentScale = ContentScale.Crop,
                modifier = Modifier)
        }
    }
}

