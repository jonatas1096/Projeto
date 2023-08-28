package com.example.projeto.views

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
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
import com.example.projeto.listener.ListenerAuth
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

    var context = LocalContext.current

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
        val (boxRegistroAluno, pandasapeca, elipseAluno,icAluno,identificacao) = createRefs()

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

                    //Spacer para descer os campos
                    Spacer(modifier = Modifier.height(80.dp))


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

                            TextDuasCores(color1 = Color(0xFFF5E5E5E),
                                color2 = LARANJA,
                                texto1 = "Eu li e concordo com os ",
                                texto2 = "Termos & Condições")
                    }

                    //Botão registrar
                    BotaoRegistrar(
                        onClick = { //Ao clicar existe duas possibilidades de mensagens que coloquei no "Listener"
                            viewModel.cadastro(email, senha, object : ListenerAuth{
                                override fun onSucess(mensagem: String) {
                                    Toast.makeText(context,mensagem, Toast.LENGTH_SHORT).show()
                                    navController.navigate("Login")
                                }

                                override fun onFailure(erro: String) {
                                    Toast.makeText(context,erro, Toast.LENGTH_SHORT).show()
                                }

                            })
                        },
                        corBotao = LARANJA)
                    Spacer(modifier = Modifier
                        .height(130.dp))



                }

        }
        //Pandinha
        Box(
            modifier = Modifier
                .constrainAs(pandasapeca) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top, margin = 525.dp)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .size(116.dp)
        ) {
            loadImage(path = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/pandinha.png",
                contentDescription = "panda sapeca",
                contentScale = ContentScale.Fit,
                modifier = Modifier)
        }

        //Elipse do aluno
        Box(
            modifier = Modifier
                .constrainAs(elipseAluno) {
                    top.linkTo(parent.top, margin = 10.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .size(170.dp)
        ) {
            loadImage(path = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/alunocirculo.png",
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier)
        }

        //Icone de identificação para aluno
        Icon(
            painterResource(id = R.drawable.ic_aluno),
            contentDescription = "Ícone de Codigo de Turma no registro",
            modifier = Modifier
                .constrainAs(icAluno) {
                    top.linkTo(parent.top, margin = 45.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end, margin = 4.dp)
                }
                .size(90.dp))
        
        //Identificação da página
        Text(text = "Aluno",
            fontSize = 42.sp,
            color = LARANJA,
            fontFamily = Dongle,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(identificacao){
                top.linkTo(elipseAluno.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )
    }


}



@Preview(showBackground = true)
@Composable
fun PreviewAluno(){
    RegistroAluno(navController = rememberNavController())
}