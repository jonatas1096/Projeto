package com.example.projeto.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.projeto.R
import com.example.projeto.layoutsprontos.*
import com.example.projeto.ui.theme.Dongle
import com.example.projeto.ui.theme.LARANJA
import com.example.projeto.viewmodel.AuthViewModel

@Composable
fun RegistroAluno(navController: NavController, viewModel: AuthViewModel = hiltViewModel()) {

    //iniciando as variaveis para o cadastro:
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var rm by remember { mutableStateOf("") }
    var codigoturma by remember { mutableStateOf("") }

    //Background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)
    ) {
        loadImage(path = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/backgroundoficial.png",
            contentDescription = "Background Registrar",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }


    //Começo constraint
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (boxRegistroAluno, identificacao) = createRefs()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .background(Color.White, shape = RoundedCornerShape(25.dp))
                .constrainAs(boxRegistroAluno) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }


        ){
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(top = 40.dp)
                        .padding(horizontal = 15.dp)
                ) {

                    //Nome
                    OutlinedRegistro(
                        value = nome,
                        onValueChange = {nome = it},
                        label = "Nome / Apelido",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text
                        ),
                        visualTransformation = VisualTransformation.None,
                        leadingIcon = {
                            Icon(
                                painterResource(id = R.drawable.ic_aluno),
                                contentDescription = "Ícone de Aluno no registro",
                                modifier = Modifier.size(28.dp))
                        }
                    )
                    //Email
                    OutlinedRegistro(
                        value = email,
                        onValueChange = {email = it},
                        label = "Email",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
                        visualTransformation = VisualTransformation.None,
                        leadingIcon = {
                            Icon(
                                painterResource(id = R.drawable.ic_email),
                                contentDescription = "Ícone de Email no registro",
                                modifier = Modifier.size(28.dp))
                        }
                    )
                    //Senha
                    OutlinedRegistro(
                        value = senha,
                        onValueChange = {senha = it},
                        label = "Senha",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        visualTransformation = PasswordVisualTransformation(),
                        leadingIcon = {
                            Icon(
                                painterResource(id = R.drawable.ic_senha),
                                contentDescription = "Ícone de Senha no registro",
                                modifier = Modifier.size(28.dp))
                        }
                    )
                    //RM
                    OutlinedRegistro(
                        value = rm,
                        onValueChange = {rm = it},
                        label = "RM",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        visualTransformation = VisualTransformation.None,
                        leadingIcon = {
                            Icon(
                                painterResource(id = R.drawable.ic_rm),
                                contentDescription = "Ícone de RM no registro",
                                modifier = Modifier.size(28.dp))
                        }
                    )
                    //Código turma
                    OutlinedRegistro(
                        value = codigoturma,
                        onValueChange = {codigoturma = it},
                        label = "Código da Turma",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text
                        ),
                        visualTransformation = VisualTransformation.None,
                        leadingIcon = {
                            Icon(
                                painterResource(id = R.drawable.ic_codigoturma),
                                contentDescription = "Ícone de Codigo de Turma no registro",
                                modifier = Modifier.size(28.dp))
                        }
                    )
                    //Termos e Condições
                    Row {
                        CheckBoxPersonalizada()
                        //gambiarra p deixar clicável mais facil

                            TextDuasCores(color1 = Color(0xFFF5E5E5E),
                                color2 = LARANJA,
                                texto1 = "Eu li e concordo com os ",
                                texto2 = "Termos & Condições")
                    }
                    //Botão registrar
                    BotaoRegistrar(corBotao = LARANJA)
                    //Pandinha

                    
                }

        }

        /*Identificação
        Row(
            modifier = Modifier
                .constrainAs(identificacao){
                    top.linkTo(parent.top, margin = 220.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_aluno),
                contentDescription = "Você está na área do Aluno.",
                modifier = Modifier.size(80.dp))
            
            Text(text = "Aluno",
                fontSize = 44.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontFamily = Dongle)
        }*/
    }


}



@Preview(showBackground = true)
@Composable
fun PreviewAluno(){
    RegistroAluno(navController = rememberNavController())
}