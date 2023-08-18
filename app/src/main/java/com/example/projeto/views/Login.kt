package com.example.projeto.views

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.projeto.layoutsprontos.loadImage
import com.example.projeto.ui.theme.Jomhuria
import com.example.projeto.ui.theme.LARANJA


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Login() {

    var email by remember {
        mutableStateOf("")
    }

    var senha by remember {
        mutableStateOf("")
    }


    Box(
        modifier = Modifier.fillMaxSize()
    ){
        loadImage(
            path = "",
            contentDescription = "Background Laranja",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()

        )
    }


        ConstraintLayout(
            modifier = Modifier.fillMaxSize()

        ) {
            val (capeloBox, areaLogin, titulo, pauloroberto) = createRefs()

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

            Text(text = "ConnectSTUDENT",
                modifier = Modifier.constrainAs(titulo){
                    top.linkTo(parent.top, margin = 130.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                ,
                fontSize = 78.sp,
                color = Color.White,
                fontFamily = Jomhuria
                )


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
                    .background(
                        Color.White,
                        shape = RoundedCornerShape(30.dp)
                    )

            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {



                    Text(text = "Bem vindo!",
                        color = Color.Black,
                        fontSize = 26.sp,
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .padding(bottom = 0.dp))

                    Text(text = "Lorem ipsum lorem ipsu lorem!",
                        color = Color(49, 48, 48, 255),
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(bottom = 80.dp)
                    )


                    //Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = {email = it},
                        placeholder = {
                            Text(text = "Email",
                                fontSize = 19.sp)
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            unfocusedBorderColor = LARANJA
                        ),
                        shape = RoundedCornerShape(40.dp),
                        modifier = Modifier
                            .padding(horizontal = 40.dp)
                            .padding(bottom = 20.dp)
                    )


                    //Senha
                    OutlinedTextField(
                        value = senha,
                        onValueChange = { senha = it},
                        placeholder = {
                            Text(text = "Senha",
                                fontSize = 19.sp)
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            unfocusedBorderColor = LARANJA
                        ),
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .padding(horizontal = 40.dp)
                            .padding(bottom = 55.dp)
                    )



                    //Logar
                    Button(onClick = {

                    },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = LARANJA,
                            contentColor = Color.White,
                        ),
                        shape = RoundedCornerShape(30.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 90.dp)
                    ) {
                        Text(text = "Logar!",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold)
                    }
                }

            }



            //Paulo Roberto
            Box(
                modifier = Modifier.constrainAs(pauloroberto){
                    top.linkTo(parent.top, margin = 420.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end, margin = 270.dp)
                    bottom.linkTo(parent.bottom)

                }
                    .size(140.dp)
            ) {
                loadImage(path = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/pauloroberto.png",
                    contentDescription = "Aqui está o Paulo Roberto",
                    contentScale = ContentScale.None,
                    modifier = Modifier)
            }


        }




}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Login()
}