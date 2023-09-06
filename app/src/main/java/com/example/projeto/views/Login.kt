package com.example.projeto.views

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.projeto.R
import com.example.projeto.layoutsprontos.OutlinedLogin
import com.example.projeto.layoutsprontos.loadImage
import com.example.projeto.listener.ListenerAuth
import com.example.projeto.ui.theme.Dongle
import com.example.projeto.ui.theme.Jomhuria
import com.example.projeto.ui.theme.LARANJA
import com.example.projeto.viewmodel.AuthViewModel


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Login(navController: NavController, viewModel: AuthViewModel) {

    var email by remember {
        mutableStateOf("")
    }

    var senha by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    val scrollState = rememberScrollState()

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
                .verticalScroll(scrollState)
        ) {
            val (capeloBox, areaLogin,areaLoginSOMBRA, titulo, pauloroberto) = createRefs()

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
                },
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
                        top.linkTo(titulo.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .width(310.dp)
                    .height(430.dp)
            ){}


            //Box que vai guardar email e senha:

                Box(
                    modifier = Modifier
                        .constrainAs(areaLogin) {
                            top.linkTo(titulo.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)

                        }
                        .width(310.dp)
                        //.height(430.dp)
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
                            text = "Bem vindo(a)!",
                            color = Color(0xFF858585),
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Dongle,
                            modifier = Modifier
                                .padding(top = 5.dp)
                                .padding(bottom = 0.dp)

                        )

                        Text(
                            text = "Logue-se para participar!",
                            color = Color(0xFF8D8D8D),
                            fontSize = 26.sp,
                            fontFamily = Dongle,
                            modifier = Modifier
                                .padding(bottom = 20.dp)

                        )


                        //Email
                        OutlinedLogin(value = email,
                            onValueChange = {email = it},
                            label = "Email",
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email
                            ),
                            visualTransformation = VisualTransformation.None,
                            leadingIcon = {
                                Icon(painterResource(id = R.drawable.ic_email),
                                    contentDescription = "Ícone de email",
                                    modifier = Modifier.size(22.dp))
                            }
                        )


                        //Senha
                        OutlinedLogin(value = senha,
                            onValueChange = {senha = it},
                            label = "Senha",
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password
                            ),
                            visualTransformation = PasswordVisualTransformation(),
                            leadingIcon = {
                                Icon(
                                    painterResource(id = R.drawable.ic_senha),
                                    contentDescription = "Ícone da senha",
                                modifier = Modifier.size(22.dp))
                            }

                        )



                            //Esqueceu a senha

                                Text(text = "Esqueceu a sua senha?",
                                    fontSize = 30.sp,
                                    fontFamily = Jomhuria,
                                    color = LARANJA,
                                    modifier = Modifier
                                        .padding(bottom = 0.dp)
                                        .clickable {

                                        }

                                )

                        //Gambiarrazinha p colocar a linha entre as opções
                        Surface(
                            modifier = Modifier
                                .border(2.dp, Color(0xFF9C9C9C))
                                .height(1.dp)
                                .width(100.dp)
                                .border(2.dp, Color.Black)
                                .padding(start = 30.dp)
                        ) {

                        }


                        //Registrar-se:
                        //aqui existe essa pequena função para colocar estilos diferentes no mesmo "text". Usei para colocar laranja no "Registre-se!"
                        val textodiferente = buildAnnotatedString {
                            withStyle(style = SpanStyle(Color(0xFF8D8D8D))){
                                append("Não possui uma conta?")
                            }
                            withStyle(style = SpanStyle(LARANJA)){
                                append(" Registre-se!")
                            }
                        }
                        Text(text = textodiferente,
                            fontFamily = Jomhuria,
                            fontSize = 30.sp,
                            modifier = Modifier
                                //.padding(start = 30.dp)
                                .clickable {
                                    navController.navigate("Registrar")
                                }
                                .padding(0.dp)
                        )






                        //Logar
                        Button(
                            onClick = {
                                viewModel.login(email,senha, object : ListenerAuth{
                                    override fun onSucess(mensagem: String) {
                                        Toast.makeText(context, mensagem, Toast.LENGTH_SHORT).show()
                                        navController.navigate("Index")
                                    }

                                    override fun onFailure(erro: String) {
                                        Toast.makeText(context, erro, Toast.LENGTH_SHORT).show()
                                    }

                                })
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = LARANJA,
                                contentColor = Color.White,
                            ),
                            shape = RoundedCornerShape(30.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 90.dp)
                                .padding(top = 38.dp)
                        ) {
                            Text(
                                text = "Login",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }







                    }

                }



            //Paulo Roberto
            Box(
                modifier = Modifier
                    .constrainAs(pauloroberto) {
                        start.linkTo(areaLogin.end, margin = (-359).dp)
                        top.linkTo(areaLogin.bottom, margin = (-100).dp)

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


