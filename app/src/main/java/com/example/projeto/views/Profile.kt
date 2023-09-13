package com.example.projeto.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.projeto.layoutsprontos.loadImage
import kotlinx.coroutines.NonDisposableHandle.parent


@Composable
fun Profile(navController: NavController){
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        loadImage(
            path = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/backgroundlogin.png",
            contentDescription = "Background do Login",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()

        )
    }
    Box(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout (modifier = Modifier.fillMaxSize()){

            val (julio,ines) = createRefs() //criou as referencias

            loadImage(path = "https://static.wikia.nocookie.net/cocorico/images/e/e3/Julio-careca.jpg/revision/latest?cb=20211011002720&path-prefix=pt-br",
                contentDescription = "julio careca",
                contentScale = ContentScale.None,
                modifier = Modifier.size(120.dp)
                    .constrainAs(julio){
                        bottom.linkTo(parent.bottom)
                    }

            )
            loadImage(path = "https://rd1.com.br/wp-content/uploads/2016/06/In%C3%AAs-Brasil-32-810x442.jpg",
                contentDescription = "ines",
                contentScale = ContentScale.None,
                modifier = Modifier.size(120.dp)
                    .constrainAs(ines){

                        start.linkTo(julio.end) //primeiro membro: Ines // segundo membro: a referencia dela
                        //em outras pálavras, o start da ines (a esquerda) vai começar à direita do julio.
                        bottom.linkTo(parent.bottom, margin = 10.dp)
                    }

            )

        }


    }

}
