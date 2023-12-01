package com.connectstudent.projeto.views

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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.connectstudent.projeto.R
import com.connectstudent.projeto.layoutsprontos.OutlinedEmail
import com.connectstudent.projeto.layoutsprontos.OutlinedSenha
import com.connectstudent.projeto.layoutsprontos.loadImage
import com.connectstudent.projeto.listener.ListenerAuth
import com.connectstudent.projeto.ui.theme.Dongle
import com.connectstudent.projeto.ui.theme.Jomhuria
import com.connectstudent.projeto.ui.theme.LARANJA
import com.connectstudent.projeto.viewmodel.AuthViewModel
import com.connectstudent.projeto.viewmodel.AuthViewModelCPS
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Login(navController: NavController, viewModel: AuthViewModel, viewModelCPS: AuthViewModelCPS) {

    var alunoLogado = viewModel.verificarUsuarioLogado().collectAsState(initial = false).value
    var cpsLogado = viewModelCPS.verificarUsuarioLogado().collectAsState(initial = false).value
    var loadingState by remember{ mutableStateOf(true) }



    var email by remember { mutableStateOf("") }
    var emailRedefinir by remember { mutableStateOf("") }
    var confirmarEmail by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    val context = LocalContext.current

    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    var redefinirState by remember{ mutableStateOf(false) }
    var mensagemRedefinir = remember{ mutableStateOf(false) }





    //Lógica da tela de carregamento
    if (loadingState){
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(255, 255, 255, 163))
                .zIndex(1f)
        ){
            val (circularProgress, logo, nomes) = createRefs()
            CircularProgressIndicator(
                modifier = Modifier
                    .constrainAs(circularProgress) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .size(150.dp),
                color = Color(43, 41, 41, 233),
                strokeWidth = 10.dp
            )
            Box(
                modifier = Modifier
                    .constrainAs(logo) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .size(80.dp)
            ){
                loadImage(
                    path = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/logo_padrao.png",
                    contentDescription = "logo do App",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                )
            }

            Column(
                modifier = Modifier.constrainAs(nomes){
                    top.linkTo(circularProgress.bottom, margin = 12.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            ) {
                Text(
                    text = "Anahi Mamani",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Beatriz Witer",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Joissi Airane",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Jonatas Bahia",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )

            }
        }
    }
    else{
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
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            val (logo, areaLogin) = createRefs()

            //Estava tendo problemas com o tamanho da imagem, então coloquei dentro de uma box e scalonei pela box:
            Box(
                modifier = Modifier
                    .constrainAs(logo) {
                        top.linkTo(parent.top, margin = 0.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .size(260.dp)
            ) {
                loadImage(path = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/logo_ofc.png",
                    contentDescription = "Logo do app",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                )
            }


            //Card que vai guardar email e senha:
            Card(
                modifier = Modifier
                    .constrainAs(areaLogin) {
                        top.linkTo(logo.bottom, margin = (20).dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)

                    }
                    .width(310.dp),
                backgroundColor = Color.White,
                shape = RoundedCornerShape(30.dp),
                elevation = 8.dp
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
                            .padding(bottom = 16.dp)

                    )


                    //Email
                    OutlinedEmail(value = email,
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
                        },
                    )


                    //Senha
                    OutlinedSenha(value = senha,
                        onValueChange = {senha = it},
                        label = "Senha",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        leadingIcon = {
                            Icon(
                                painterResource(id = R.drawable.ic_senha),
                                contentDescription = "Ícone da senha",
                                modifier = Modifier.size(22.dp))
                        }

                    )



                    //Esqueceu a senha

                    Text(text = "Esqueceu a sua senha?",
                        fontSize = 34.sp,
                        fontFamily = Jomhuria,
                        color = LARANJA,
                        modifier = Modifier
                            .padding(bottom = 0.dp)
                            .clickable {
                                redefinirState = true
                            }

                    )

                    //Gambiarrazinha p colocar a linha entre as opções
                    Surface(
                        modifier = Modifier
                            .border(2.dp, Color(0xFF9C9C9C))
                            .height(2.dp)
                            .width(130.dp)
                            .border(2.dp, Color.Black)
                            .padding(start = 30.dp)
                    ) {}


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
                        fontSize = 31.sp,
                        modifier = Modifier
                            //.padding(start = 30.dp)
                            .clickable {
                                navController.navigate("Registrar")
                            }
                            .padding(0.dp)
                    )

                    //Gambiarrazinha p colocar a linha entre as opções
                    Surface(
                        modifier = Modifier
                            .border(2.dp, Color(0xFF9C9C9C))
                            .height(2.dp)
                            .width(130.dp)
                            .border(2.dp, Color.Black)
                            .padding(start = 30.dp)
                    ) {}
                Text(
                    text = "Sobre o app",
                    color = LARANJA,
                    fontSize = 32.sp,
                    fontFamily = Jomhuria,
                    modifier = Modifier.clickable {
                        navController.navigate("Detalhes")
                    }
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
                            .padding(horizontal = 80.dp)
                            .padding(top = 6.dp)
                            .padding(bottom = 6.dp)
                    ) {
                        Text(
                            text = "Login",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }


                }

            }
        }

        if (redefinirState){
            redefinirSenha(onDismissRequest = {
                redefinirState = false},
                emailRedefinir, confirmarEmail, mensagemRedefinir)
        }

        if (mensagemRedefinir.value){
            mensagemAposRedefinir()
        }
    }



    LaunchedEffect(alunoLogado, cpsLogado) {
        scope.launch {
            if (alunoLogado || cpsLogado) {
                navController.navigate("Index")
                delay(1500)
                loadingState = false
            }
            delay(1000)
            loadingState = false
        }

    }
}

@Composable
fun redefinirSenha(onDismissRequest: () -> Unit, emailRedefinir:String, confirmarEmail:String, mensagemRedefinir: MutableState<Boolean>){

    val auth = FirebaseAuth.getInstance()

    var email by remember { mutableStateOf(emailRedefinir) }
    var confirmarEmail by remember { mutableStateOf(confirmarEmail) }

    var camposDiferentes by remember{ mutableStateOf(false) }
    println("inicio $camposDiferentes")

    val context = LocalContext.current

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        val (box) = createRefs()

        Box( modifier = Modifier.constrainAs(box){
            start.linkTo(parent.start, margin = 30.dp)
            top.linkTo(parent.top)
            end.linkTo(parent.end, margin = 30.dp)
            bottom.linkTo(parent.bottom)
        }
        ) {

            Dialog(
                onDismissRequest = {
                    onDismissRequest()
                },
                properties = DialogProperties(
                    dismissOnClickOutside = true,
                ),
            )
            {
                //Esse card serve para nao bugar e ficar sem fundo
                Card(
                    elevation = 5.dp,
                    shape = RoundedCornerShape(10.dp),
                    backgroundColor = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    //Esse é o conteúdo em si
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                            .padding(vertical = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Redifina a senha da sua conta",
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = Jomhuria
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 5.dp)
                        ) {
                            Text(text = "Insira e confirme o seu email para que procuremos a sua conta.",
                                fontSize = 17.sp,
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 15.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = email,
                                onValueChange = {
                                    email = it
                                },
                                label = {
                                    Text(text = "Email",
                                        fontSize = 18.sp
                                    )
                                },
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    unfocusedBorderColor = Color(0xFFE7E6E6),
                                    focusedBorderColor = LARANJA,
                                    focusedLabelColor = LARANJA,
                                    cursorColor = LARANJA,
                                ),
                                maxLines = 1,

                                )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 5.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = confirmarEmail,
                                onValueChange = {
                                    confirmarEmail = it
                                },
                                label = {
                                    Text(text = "Confirme o Email",
                                        fontSize = 18.sp
                                    )
                                },
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    unfocusedBorderColor = Color(0xFFE7E6E6),
                                    focusedBorderColor = LARANJA,
                                    focusedLabelColor = LARANJA,
                                    cursorColor = LARANJA,
                                ),
                                maxLines = 1
                            )
                        }
                        if (camposDiferentes){
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 5.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Text(
                                    text = "*Os campos não coincidem!",
                                    fontSize = 14.sp,
                                    color = Color.Red,
                                )
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = if (camposDiferentes) 1.dp else 25.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = {
                                    onDismissRequest()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color(0xFFE7E6E6),

                                    ),
                                modifier = Modifier.padding(end = 15.dp)
                            ) {
                                Text(text = "Cancelar",
                                    fontSize = 16.sp,
                                    color = Color(0xFF979797),
                                )
                            }
                            Button(
                                onClick = {
                                    if (email != confirmarEmail){
                                        camposDiferentes = true

                                    }
                                    else if(email.isNullOrEmpty() || confirmarEmail.isNullOrEmpty()){
                                        Toast.makeText(context, "Insira o email nos dois campos!",Toast.LENGTH_SHORT).show()
                                    }
                                    else{
                                        camposDiferentes = false
                                        auth.sendPasswordResetEmail(email)
                                            .addOnSuccessListener {
                                                println("email: $email, confirmar email : $confirmarEmail")
                                                Toast.makeText(context, "Email enviado com sucesso!",Toast.LENGTH_SHORT).show()
                                                onDismissRequest()
                                                mensagemRedefinir.value = true
                                            }
                                            .addOnFailureListener{e ->
                                                Toast.makeText(context, "Email não cadastrado no banco de dados!",Toast.LENGTH_SHORT).show()
                                                println("Erro. $e")
                                            }

                                    }

                                },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = LARANJA,
                                ),
                            ) {
                                Text(text = "Pesquisar",
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                            }
                        }

                    }
                }

            }
        }

    }
}

@Composable
fun mensagemAposRedefinir(){

    val context = LocalContext.current
    val alertDialog = android.app.AlertDialog.Builder(context)
    alertDialog.setTitle("Nós te enviamos um email!")
    alertDialog.setMessage("A sua solicitação de redefinição de senha foi atendida e um link foi enviado ao seu email. \nNão se esqueça de checar também o lixo eletrônico/spam, certo?")
    alertDialog.setPositiveButton("Entendido!"){_,_ ->
        FirebaseAuth.getInstance().signOut()
    }
        .show()
}


