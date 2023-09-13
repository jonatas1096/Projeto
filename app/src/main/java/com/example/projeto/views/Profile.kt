package com.example.projeto.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.projeto.layoutsprontos.loadImage
import kotlinx.coroutines.NonDisposableHandle.parent


@Composable
fun Profile(navController: NavController){
    //box do background
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        loadImage(
            path = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/background_perfil_prtofessor.png",
            contentDescription = "Background do Login",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
    //box da tela
    Box(modifier = Modifier.fillMaxSize())
    {
        //Constraint do quadradao e do conteudo de dentro
            ConstraintLayout(modifier = Modifier.fillMaxSize())
            {
                val (boxPrincipal, arrow)= createRefs()

                //Arrow voltar (seta q volta)
                Box(modifier = Modifier.constrainAs(arrow){
                    top.linkTo(parent.top, margin = 10.dp)//toquei de 8dp para 10pdp (questao estetica apenas)
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
                //BOX DO QUADRADO 100%nofake
                Box(modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(20.dp))
                    .constrainAs(boxPrincipal) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top, margin = 30.dp)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
                    .width(360.dp)
                    .height(765.dp)
                    .padding(horizontal = (70.dp))
                )
                {
                    Column() {
                        Text(text = "AAAAAAAAAAA")
                    }
                }
            }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout (modifier = Modifier.fillMaxSize()){

            val (julio,ines) = createRefs() //criou as referencias

            loadImage(path = "https://static.wikia.nocookie.net/cocorico/images/e/e3/Julio-careca.jpg/revision/latest?cb=20211011002720&path-prefix=pt-br",
                contentDescription = "julio careca",
                contentScale = ContentScale.None,
                modifier = Modifier
                    .size(120.dp)
                    .constrainAs(julio) {
                        bottom.linkTo(parent.bottom)
                    }

            )
            loadImage(path = "https://rd1.com.br/wp-content/uploads/2016/06/In%C3%AAs-Brasil-32-810x442.jpg",
                contentDescription = "ines",
                contentScale = ContentScale.None,
                modifier = Modifier
                    .size(120.dp)
                    .constrainAs(ines) {

                        start.linkTo(julio.end) //primeiro membro: Ines // segundo membro: a referencia dela
                        //em outras pálavras, o start da ines (a esquerda) vai começar à direita do julio.
                        bottom.linkTo(parent.bottom, margin = 10.dp)
                    }

            )

        }


    }

}
