package com.connectstudent.projeto.views

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
import com.connectstudent.projeto.R
import com.connectstudent.projeto.layoutsprontos.*
import com.connectstudent.projeto.listener.ListenerAuth
import com.connectstudent.projeto.ui.theme.AZULCLARO
import com.connectstudent.projeto.ui.theme.Dongle
import com.connectstudent.projeto.viewmodel.AuthViewModelCPS

@Composable
fun RegistroCPS(navController: NavController, viewModel: AuthViewModelCPS = hiltViewModel()) {

    //iniciando as variaveis para o cadastro:
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var id by remember { mutableStateOf("") }
    var codigoEtec by remember { mutableStateOf("") }

    val context = LocalContext.current

    var checkboxmarcada by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    //Background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)
    ) {
        loadImage(path = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/backgroundcps.png",
            contentDescription = "Background RegistrarCPS",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }


    //Começo constraint
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        val (boxRegistroCPS,elipseAluno, icAluno, identificacao, arrow) = createRefs()

        //Arrow voltar
        Box(modifier = Modifier.constrainAs(arrow){
            top.linkTo(parent.top, margin = 20.dp)
            start.linkTo(parent.start, margin = 20.dp)
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
                .height(540.dp)
                .background(Color.White, shape = RoundedCornerShape(25.dp))
                .constrainAs(boxRegistroCPS) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top, margin = 140.dp)
                    end.linkTo(parent.end)
                }


        ){
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(top = 125.dp)
                    .padding(horizontal = 15.dp)
            ) {

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
                    },
                    cor = Color(17, 100, 172, 255)
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
                    },
                    cor = Color(17, 100, 172, 255)
                )
                //RM
                OutlinedRegistro(
                    value = id,
                    onValueChange = {id = it},
                    label = "ID",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    visualTransformation = VisualTransformation.None,
                    leadingIcon = {
                        Icon(
                            painterResource(id = R.drawable.ic_rmoucps),
                            contentDescription = "Ícone de ID no registro",
                            modifier = Modifier.size(28.dp))
                    },
                    cor = Color(17, 100, 172, 255)
                )
                //Código turma
                OutlinedRegistro(
                    value = codigoEtec,
                    onValueChange = {codigoEtec = it},
                    label = "Código da ETEC",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    visualTransformation = VisualTransformation.None,
                    leadingIcon = {
                        Icon(
                            painterResource(id = R.drawable.ic_codigoturma),
                            contentDescription = "Ícone de Codigo da ETEC no registro",
                            modifier = Modifier.size(28.dp))
                    },
                    cor = Color(17, 100, 172, 255)
                )
                //Termos e Condições
                Row {
                    CheckBoxPersonalizada{isChecked ->

                        checkboxmarcada = isChecked
                    }
                    val dialogo = remember { mutableStateOf(false) } //variavel para a lógica dos termos e condições

                    TextDuasCores(color1 = Color(0xFFF5E5E5E),
                        color2 = AZULCLARO,
                        texto1 = "Eu li e concordo com as ",
                        texto2 = "Políticas de Privacidade",
                        onclick = {
                            dialogo.value = true
                        },
                        fontSize = 13.sp
                    )

                    if (dialogo.value){
                        if (dialogo.value){
                            AlertDialogPersonalizado(
                                dialogo = dialogo,
                                onDismissRequest = {
                                    dialogo.value = false
                                },
                                cor = AZULCLARO
                            )

                        }

                    }
                }
                Spacer(modifier = Modifier
                    .height(10.dp))

                //Botão registrar
                if (checkboxmarcada){
                    BotaoRegistrar(
                        onClick = {
                            //Ao clicar existe duas possibilidades de mensagens que coloquei no "Listener"
                            viewModel.cpsCadastro(/*nome,*/ email, senha,id, codigoEtec, object : ListenerAuth{
                                override fun onSucess(mensagem: String) {
                                    Toast.makeText(context,mensagem, Toast.LENGTH_SHORT).show()
                                    navController.navigate("Login")
                                }

                                override fun onFailure(erro: String) {
                                    Toast.makeText(context,erro, Toast.LENGTH_SHORT).show()
                                }

                            })
                        },
                        corBotao = AZULCLARO)

                }
                else{
                    BotaoRegistrar(
                        onClick = {
                            Toast.makeText(context,"Você deve concordar com os termos para prosseguir!",Toast.LENGTH_SHORT).show()
                        },
                        corBotao = AZULCLARO)
                }




            }

        }


        //Elipse do Professor
        Box(
            modifier = Modifier
                .constrainAs(elipseAluno) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top, margin = 30.dp)
                }
                .size(170.dp)
        ) {
            loadImage(path = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/professorescirculo.png",
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier)
        }

        //Icone de identificação para aluno
        Icon(
            painterResource(id = R.drawable.ic_professor),
            contentDescription = "Ícone de identificação Professores ou Administração",
            modifier = Modifier
                .constrainAs(icAluno) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end, margin = 5.dp)
                    bottom.linkTo(boxRegistroCPS.top, margin = (-20).dp)
                }
                .size(90.dp))//

        //Identificação da página
        Text(text = "Professores e Administração",
            fontSize = 36.sp,
            color = AZULCLARO,
            fontFamily = Dongle,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(identificacao){
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(elipseAluno.bottom)
            }

        )
    }


}

