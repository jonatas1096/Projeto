package com.example.projeto.views

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.projeto.R
import com.example.projeto.layoutsprontos.*
import com.example.projeto.listener.ListenerAuth
import com.example.projeto.ui.theme.Dongle
import com.example.projeto.ui.theme.LARANJA
import com.example.projeto.viewmodel.AuthViewModel

@SuppressLint("SuspiciousIndentation")
@Composable
fun RegistroAluno(navController: NavController, viewModel: AuthViewModel = hiltViewModel()) {

    //iniciando as variaveis para o cadastro:
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var rm by remember { mutableStateOf("") }
    var codigoturma by remember { mutableStateOf("") }

    var context = LocalContext.current

    var checkboxmarcada by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

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
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        val (boxRegistroAluno, pandasapeca, elipseAluno, icAluno, identificacao, arrow) = createRefs()

        //Arrow voltar
        Box(modifier = Modifier
            .constrainAs(arrow) {
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





        //Box principal
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(643.dp)
                .background(Color.White, shape = RoundedCornerShape(25.dp))
                .constrainAs(boxRegistroAluno) {
                    top.linkTo(parent.top, margin = 71.dp)
                }


        ){
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(top = 97.dp)
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
                        CheckBoxPersonalizada{isChecked ->

                           checkboxmarcada = isChecked
                        }

                        val dialogo = remember { mutableStateOf(false) } //variavel para a lógica dos termos e condições

                            TextDuasCores(
                                color1 = Color(0xFFF5E5E5E),
                                color2 = LARANJA,
                                texto1 = "Eu li e concordo com os ",
                                texto2 = "Termos & Condições",
                                onclick = {
                                    dialogo.value = true

                                },
                                fontSize = 13.sp
                            )

                        if (dialogo.value){
                            AlertDialogPersonalizado(
                                dialogo = dialogo,
                                onDismissRequest = {
                                    dialogo.value = false
                                },
                                cor = LARANJA
                            )

                        }
                    }

                    //Botão registrar
                    if (checkboxmarcada){
                        BotaoRegistrar(
                            onClick = {
                                //Ao clicar existe duas possibilidades de mensagens que coloquei no "Listener"
                                viewModel.cadastro(nome, email, senha,rm, codigoturma, object : ListenerAuth{
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
                    else{
                        BotaoRegistrar(
                            onClick = {
                               Toast.makeText(context,"Você deve concordar com os termos para prosseguir!",Toast.LENGTH_SHORT).show()
                            },
                            corBotao = LARANJA)
                        Spacer(modifier = Modifier
                            .height(130.dp))
                    }




                }

        }
        /*Pandinha
        Box(
            modifier = Modifier
                .constrainAs(pandasapeca) {
                    top.linkTo(identificacao.bottom, margin = 395.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .size(116.dp)
        ) {
            loadImage(path = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/pandinha.png",
                contentDescription = "panda sapeca",
                contentScale = ContentScale.Fit,
                modifier = Modifier)
        }*/

        //Elipse do aluno
        Box(
            modifier = Modifier
                .constrainAs(elipseAluno) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top, margin = (-14).dp)
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
            contentDescription = "Ícone de identificação Alunos no registro",
            modifier = Modifier
                .constrainAs(icAluno) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end, margin = 5.dp)
                    top.linkTo(parent.top, margin = 25.dp)
                }
                .size(90.dp))//
        
        //Identificação da página
        Text(text = "Aluno",
            fontSize = 46.sp,
            color = LARANJA,
            fontFamily = Dongle,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(identificacao){
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top, margin = 138.dp)
            }

        )
    }


}
