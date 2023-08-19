package com.example.projeto.views

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
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



    //Background ocupando toda a tela
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


        ConstraintLayout(
            modifier = Modifier.fillMaxSize()

        ) {
            val (capeloBox, areaLogin,areaLoginSOMBRA, titulo, pauloroberto,esqueceusenha) = createRefs()

            //Estava tendo problemas com o tamanho da imagem, então coloquei dentro de uma box e scalonei pela box:
            Box(
                modifier = Modifier
                    .constrainAs(capeloBox) {
                        top.linkTo(parent.top, margin = 30.dp)
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
                    top.linkTo(parent.top, margin = 90.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                ,
                fontSize = 78.sp,
                color = Color.White,
                fontFamily = Jomhuria,
                )


        //Essa surface é uma gambiarra do carai só pra colocar uma sombra na Box abaixo, infelizmente o elevation padrão fica bugado:
            Surface(
                shape = RoundedCornerShape(30.dp),
                elevation = 15.dp,
                modifier = Modifier
                    .constrainAs(areaLoginSOMBRA) {
                        top.linkTo(parent.top, margin = 130.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .width(310.dp)
                    .height(430.dp)
            ){}


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


                        Text(
                            text = "Bem vindo!",
                            color = Color.Black,
                            fontSize = 26.sp,
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .padding(bottom = 0.dp)
                        )

                        Text(
                            text = "Logue-se para participar!",
                            color = Color(49, 48, 48, 255),
                            fontSize = 16.sp,
                            modifier = Modifier
                                .padding(bottom = 30.dp)
                        )


                        //Email
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = {
                                Text(text = "Email")
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                unfocusedBorderColor = Color.White,
                                focusedBorderColor = LARANJA,
                                focusedLabelColor = LARANJA,
                                backgroundColor = Color(0xFFF2f2f2),
                                cursorColor = LARANJA,
                            ),
                            keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                            ),
                            maxLines = 1,
                            shape = RoundedCornerShape(50.dp),
                            singleLine = true,
                            modifier = Modifier
                                .padding(horizontal = 40.dp)
                                .padding(bottom = 5.dp)
                        )


                        //Senha
                        OutlinedTextField(
                            value = senha,
                            onValueChange = { senha = it },
                            label = {
                                Text(
                                    text = "Senha",
                                )
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                unfocusedBorderColor = Color.White,
                                focusedBorderColor = LARANJA,
                                focusedLabelColor = LARANJA,
                                backgroundColor = Color(0xFFF2f2f2),
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password
                            ),
                            maxLines = 1,
                            visualTransformation = PasswordVisualTransformation(),
                            shape = RoundedCornerShape(50.dp),
                            singleLine = true,
                            modifier = Modifier
                                .padding(horizontal = 40.dp)

                        )



                            //Esqueceu a senha

                                Text(text = "Esqueceu a sua senha?",
                                    fontSize = 28.sp,
                                    fontFamily = Jomhuria,
                                    color = LARANJA,
                                    modifier = Modifier.padding(bottom = 50.dp)
                                        .clickable {

                                        }

                                )



                        //Logar
                        Button(
                            onClick = {

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
                            Text(
                                text = "Logar!",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }



                        //Registrar-se:
                        //aqui existe essa pequena função para colocar estilos diferentes no mesmo "text". Usei para colocar laranja no "Registre-se!"
                        val textodiferente = buildAnnotatedString {
                            withStyle(style = SpanStyle(Color.Black)){
                                append("Ainda não possui uma conta?")
                            }
                            withStyle(style = SpanStyle(LARANJA)){
                                append(" Registre-se!")
                            }
                        }
                        Text(text = textodiferente,
                        fontFamily = Jomhuria,
                        fontSize = 22.sp,
                        modifier = Modifier
                            .padding(start = 30.dp)
                            .clickable {

                            }
                        )




                    }

                }



            //Paulo Roberto
            Box(
                modifier = Modifier
                    .constrainAs(pauloroberto) {
                        top.linkTo(parent.top, margin = 390.dp)
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