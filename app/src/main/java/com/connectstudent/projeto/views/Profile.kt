package com.connectstudent.projeto.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import com.connectstudent.projeto.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.connectstudent.projeto.datasource.UserData
import com.connectstudent.projeto.layoutsprontos.arrowVoltar
import com.connectstudent.projeto.layoutsprontos.loadImage
import com.connectstudent.projeto.ui.theme.Dongle
import com.connectstudent.projeto.ui.theme.Jomhuria
import com.connectstudent.projeto.viewmodel.PublicacaoViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


@SuppressLint("UnrememberedMutableState", "SuspiciousIndentation")
@Composable
fun Profile(navController: NavController, viewModel: PublicacaoViewModel = hiltViewModel()) {

    // Unica forma que eu consegui pra abrir a galeria sendo uma função composable
    // aqui é só a lógica da galeria
    var galeriaState by remember { mutableStateOf(false) }
    var exibirImagemPadrao by remember { mutableStateOf(true) }


    // A instância do firebase firestore:
    val firestore = Firebase.firestore  // Também funcionaria assim: val firestore = FirebaseFirestore.getInstance()


    // A instância do firebase storage e as variáveis que vou precisar:
    val storage = Firebase.storage
    val storageRef = storage.reference
    val alunoRM = UserData.rmEncontrado
    val cpsID = UserData.cpsIDEncontrado
    val context = LocalContext.current


    //Coroutine
    val coroutineScope = rememberCoroutineScope()

    // As variáveis para usar no perfil:
    var imagemUrl by remember { mutableStateOf<String?>("") }
    var urlBaixada by remember{ mutableStateOf(false) }
    var apelidoState by remember{ mutableStateOf(false) }
    var inserirApelido by remember{ mutableStateOf(false) }
    var outlinedApelido by remember{ mutableStateOf("") }
    val maxCaracteres = 25
    var atualCaracteres = maxCaracteres - outlinedApelido.length
    //

    val scroll = rememberScrollState()

    // Aqui é uma lógica para tentar puxar a imagem do perfil do firebase do usuário.
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            if (!alunoRM.isNullOrEmpty()) {
                val alunoRef = storageRef.child("Alunos/Fotos de Perfil").child(alunoRM)
                alunoRef.downloadUrl
                    .addOnSuccessListener { uri ->
                        val url = uri.toString()
                        println("URL obtida: $url")
                        imagemUrl = url
                        UserData.updateUrl(url)
                    }
                    .addOnFailureListener { exception ->
                        println("A URL não pôde ser obtida. Erro: $exception")
                    }
            } else if (!cpsID.isNullOrEmpty()) {
                val cpsRef = storageRef.child("CPS/Fotos de Perfil").child(cpsID)
                cpsRef.downloadUrl
                    .addOnSuccessListener { uri ->
                        val url = uri.toString()
                        println("URL obtida: $url")
                        imagemUrl = url
                        UserData.updateUrl(url)
                    }
                    .addOnFailureListener { exception ->
                        println("A URL não pôde ser obtida. Erro: $exception")
                    }
            }
            delay(1000)
            urlBaixada = true
        }
    }
    //
    /////////




    // Para "quebrar" textos muito grandes.
    val nomeMaxCaracteres = 20
    val emailMaxCaracteres = 29


    // Fundo
    if (!UserData.rmEncontrado.isNullOrEmpty()){ // " ! " na negativa, ou seja, não está vazio.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F7F7))
        ) {
            loadImage(path = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/backgroundoficial.png",
                contentDescription = "Background Profile - Aluno",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }else{
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Red)
        ) {
            loadImage(path = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/backgroundcps.png",
                contentDescription = "Background Profile - CPS",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }




    // Box da tela
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 15.dp)
        .padding(bottom = 20.dp)
        .padding(top = 20.dp)
    ) {
        //Aqui tem 2 constraint layout porque é uma gambiarra que eu estou com preguiça de explicar
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val (cardCinza) = createRefs()
            Surface(
                elevation = 8.dp,
                shape = RoundedCornerShape(30.dp),
                color = Color(241, 241, 241, 255),
                modifier = Modifier
                    .fillMaxSize()
                    .constrainAs(cardCinza) {
                        top.linkTo(parent.top)
                    }

            ){}
        }
        // Constraint do quadrado e do conteúdo de dentro
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scroll)
        ) {
            val (card, areaFoto, nomeUsuario, email, backtohome, arrow2,sobreMim,
                iconApelido, tituloApelido, apelido, iconNome, tituloNome, nome, gambiarratextfield,
            ) = createRefs()

            val (fecharTextField, confirmarTextField, maxCaracteresConstraint, editarApelido,
                iconRMCPS,rmoucpsTitulo, rmoucps, iconTurma, turmaTitulo, turma) = createRefs()


            // Fundo laranja/aluno
            if (!UserData.rmEncontrado.isNullOrEmpty()){ // " ! " na negativa, ou seja, não está vazio.
                Card(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier
                        .height(320.dp)
                        .constrainAs(card) {
                            top.linkTo(parent.top)
                        }
                ) {
                    loadImage(
                        path = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/background_profile.png",
                        contentDescription = "Background do Profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }else{//CPS
                Card(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier
                        .height(320.dp)
                        .constrainAs(card) {
                            top.linkTo(parent.top)
                        }
                ){
                    loadImage(
                        path = "https://i.imgur.com/qjUyjn2.png",
                        contentDescription = "Background do Profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }







            // Area de mudar a foto + lógica do progressCircular
            if (urlBaixada){ //Se a url ja foi totalmente baixada eu vou tentar exibir ela ou uma padrão.
                Surface(
                    shape = CircleShape,
                    modifier = Modifier
                        .constrainAs(areaFoto) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top, margin = 35.dp)
                            end.linkTo(parent.end)
                        }
                        .size(135.dp)
                        .clickable(onClick = {
                            galeriaState = true
                            exibirImagemPadrao = false
                        })
                ) {
                    // Aqui é uma lógica para a foto de perfil.
                    // Primeiro, eu tento exibir a imagem que em teoria está no firebase do usuario
                    // Caso não exista imagem, eu exibo uma padrão do github.
                    imagemUrl?.let {
                        loadImage(
                            path = imagemUrl!!,
                            contentDescription = "Imagem default do usuário",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    if (UserData.imagemUrl.isNullOrEmpty()){
                        loadImage(
                            path = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/imagemdefault.jpg",
                            contentDescription = "Imagem default do usuário",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                }
            }
            else{ //Senão, eu exibo um circulo de progresso indicando que ainda está baixando
                Surface(
                    shape = CircleShape,
                    modifier = Modifier
                        .constrainAs(areaFoto) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top, margin = 35.dp)
                            end.linkTo(parent.end)
                        }
                        .size(135.dp)
                        .clickable(onClick = {
                            galeriaState = true
                            exibirImagemPadrao = false
                        })
                ){
                    if (UserData.cpsIDEncontrado.isNullOrEmpty()){ //exibiremos a do aluno se nao tiver cpsID
                        CircularProgressIndicator(
                            color = Color(230, 17, 77, 255),
                            strokeWidth = 5.dp
                        )
                    }
                    else{ //Se não estiver vazio, o do professor.
                        CircularProgressIndicator(
                            color = Color(33, 156, 238, 255),
                            strokeWidth = 5.dp
                        )
                    }

                }
            }



            if (UserData.apelidoUsuario.isNullOrEmpty()){ //Se não existir apelido, exibiremos o nome do firebase.
                if (UserData.nomeEncontrado.length > nomeMaxCaracteres){
                    Text(
                        text = "${UserData.nomeEncontrado.substring(0,nomeMaxCaracteres) + "..."}",
                        fontSize = 46.sp,
                        color = Color.White,
                        fontFamily = Dongle,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.constrainAs(nomeUsuario){
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(areaFoto.bottom, margin = 0.dp)
                        }
                    )
                }
                else{
                    Text(
                        text = UserData.nomeEncontrado,
                        fontSize = 46.sp,
                        color = Color.White,
                        fontFamily = Dongle,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.constrainAs(nomeUsuario){
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(areaFoto.bottom, margin = 0.dp)
                        }
                    )

                }
            }
            else{
                Text(
                    text = "${UserData.apelidoUsuario}",
                    fontSize = 46.sp,
                    color = Color.White,
                    fontFamily = Dongle,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.constrainAs(nomeUsuario){
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(areaFoto.bottom, margin = 8.dp)
                    }
                )
            }


            if (UserData.emailEncontrado.length > emailMaxCaracteres){
                Text(
                    text = "${UserData.emailEncontrado.substring(0,emailMaxCaracteres) + "..."}",
                    fontSize = 34.sp,
                    color = Color.White,
                    fontFamily = Dongle,
                    modifier = Modifier.constrainAs(email){
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(areaFoto.bottom, margin = 55.dp)
                    }
                )
            }
            Text(
                text = UserData.emailEncontrado,
                fontSize = 34.sp,
                color = Color.White,
                fontFamily = Dongle,
                modifier = Modifier.constrainAs(email){
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(areaFoto.bottom, margin = 55.dp)
                }
            )


            Button(
                onClick = {
                    navController.navigate("Index")
                },
                modifier = Modifier
                    .constrainAs(backtohome) {
                        start.linkTo(parent.start)
                        top.linkTo(card.bottom, margin = (-35).dp)
                        end.linkTo(parent.end)
                    }
                    .width(225.dp)
                    .height(55.dp),
                elevation = ButtonDefaults.elevation(8.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFFCFCFC),

                    )
            ) {
                Text(text = "Home",
                    fontFamily = Dongle,
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp,
                    color = Color(0xFF838383),
                )
            }

            arrowVoltar(
                onClick = {
                    navController.navigate("Index")
                },
                modifier = Modifier
                    .constrainAs(arrow2) {
                        end.linkTo(backtohome.start, margin = (-45).dp)
                        top.linkTo(card.bottom, margin = (-20.dp))
                    }
                    .size(30.dp),
                color = Color(0xFF838383) //nesse color tu pode escolher a cor da arrow, pode ser uma padrão tipo: Color.Black,
                // ou hexadecimal/rgb como deixei aí, blz?
            )

            Text(text = "Sobre mim",
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                modifier = Modifier
                    .constrainAs(sobreMim) {
                        start.linkTo(parent.start, margin = 20.dp)
                        top.linkTo(card.bottom, margin = 30.dp)
                    }
                    .fillMaxWidth()
            )

            //Conjunto do apelido
            Icon(
                painterResource(id = R.drawable.ic_apelidoaluno),
                contentDescription = "Ícone do Apelido do Aluno",
                modifier = Modifier
                    .constrainAs(iconApelido) {
                        start.linkTo(parent.start, margin = 20.dp)
                        top.linkTo(sobreMim.bottom, margin = 30.dp)
                    }
                    .size(40.dp)
            )
            Text(text = "Apelido",
                fontSize = 40.sp,
                fontFamily = Dongle,
                modifier = Modifier.constrainAs(tituloApelido){
                    start.linkTo(iconApelido.end, margin = 10.dp)
                    top.linkTo(sobreMim.bottom, margin = 18.dp)
                }
            )
            if(apelidoState){

                //MaxCaracteres para inserir o apelido:
                Text(text = "Max.Caracteres: $atualCaracteres",
                    fontSize = 25.sp,
                    fontFamily = Dongle,
                    color = Color(0xFF838383),
                    modifier = Modifier.constrainAs(maxCaracteresConstraint){
                        start.linkTo(tituloApelido.end, margin = 5.dp)
                        top.linkTo(sobreMim.bottom, margin = 29.dp)
                    }
                )
                //Text field para inserir o apelido
                if (outlinedApelido.length <= maxCaracteres){
                    BasicTextField(
                        value = outlinedApelido,
                        onValueChange = {
                            if (it.length <= maxCaracteres){
                                outlinedApelido = it
                            }
                        },
                        textStyle = TextStyle(
                            color = Color(0xFF1370FD),
                            fontSize = 32.sp,
                            fontFamily = Dongle,
                            textDecoration = TextDecoration.None
                        ),
                        maxLines = 1,
                        modifier = Modifier
                            .constrainAs(apelido) {
                                start.linkTo(iconApelido.end, margin = 20.dp)
                                top.linkTo(sobreMim.bottom, margin = 50.dp)
                            }
                            .width(170.dp)
                    )
                }


                //Gambiara para marcar a area do textfield
                Surface(
                    modifier = Modifier
                        .constrainAs(gambiarratextfield) {
                            top.linkTo(apelido.bottom, margin = (-7).dp)
                            start.linkTo(iconApelido.end, margin = 10.dp)
                        }
                        .height(1.dp)
                        .width(170.dp),
                    color = Color(0xFF838383)
                ) {}
                //Botões para cancelar ou confirmar:
                //Cancelar:
                IconButton(
                    onClick = {
                        apelidoState = false
                    },
                    modifier = Modifier
                        .constrainAs(fecharTextField) {
                            start.linkTo(gambiarratextfield.end, margin = 10.dp)
                            top.linkTo(tituloApelido.bottom, margin = (-8).dp)
                        }
                        .size(22.dp)
                ){
                    Image(
                        ImageVector.vectorResource(id = R.drawable.ic_fechar2),
                        contentDescription = "Ícone para cancelar a atualização do apelido",
                        colorFilter = ColorFilter.tint(Color.Red)
                    )
                }
                //Confirmar:
                IconButton(
                    onClick = {
                        // dialogo.value = true
                        inserirApelido = true
                    },
                    modifier = Modifier
                        .constrainAs(confirmarTextField) {
                            start.linkTo(fecharTextField.end, margin = 25.dp)
                            top.linkTo(tituloApelido.bottom, margin = (-12).dp)
                        }
                        .size(30.dp)
                ){
                    Image(
                        ImageVector.vectorResource(id = R.drawable.ic_confirmar),
                        contentDescription = "Ícone para confirmar a atualização do apelido",
                        colorFilter = ColorFilter.tint(Color.Green)
                    )
                }
            }
            else{
                if (UserData.apelidoUsuario.isNotEmpty()){
                    Text(text = UserData.apelidoUsuario,
                        fontSize = 31.sp,
                        fontFamily = Dongle,
                        color = Color(0xFF838383),
                        modifier = Modifier.constrainAs(apelido){
                            start.linkTo(iconApelido.end, margin = 20.dp)
                            top.linkTo(sobreMim.bottom, margin = 53.dp)
                        }
                    )

                    //Editar apelido existente:
                    IconButton(
                        onClick = {
                            apelidoState = true
                        },
                        modifier = Modifier
                            .constrainAs(editarApelido) {
                                start.linkTo(tituloApelido.end, margin = 10.dp)
                                bottom.linkTo(apelido.top, margin = (5.dp))
                            }
                            .size(25.dp)
                    ){
                        Image(
                            ImageVector.vectorResource(id = R.drawable.ic_lapis),
                            contentDescription = "Ícone para editar o apelido",
                            //colorFilter = ColorFilter.tint(Color.Green)
                        )
                    }
                }
                else{
                    Text(text = "<Inserir>",
                        fontSize = 34.sp,
                        fontFamily = Dongle,
                        color = Color(0xFF1370FD),
                        modifier = Modifier
                            .constrainAs(apelido) {
                                start.linkTo(iconApelido.end, margin = 20.dp)
                                top.linkTo(sobreMim.bottom, margin = 50.dp)
                            }
                            .clickable(
                                onClick = {
                                    apelidoState = true
                                }
                            )
                    )
                }

            }

            /////////////////////

            //Conjunto do Nome
            Icon(
                painterResource(id = R.drawable.ic_nomealuno),
                contentDescription = "Ícone do Nome do Aluno",
                modifier = Modifier
                    .constrainAs(iconNome) {
                        start.linkTo(parent.start, margin = 20.dp)
                        top.linkTo(iconApelido.bottom, margin = 35.dp)
                    }
                    .size(40.dp)
            )

            Text(text = "Nome",
                fontSize = 40.sp,
                fontFamily = Dongle,
                modifier = Modifier.constrainAs(tituloNome){
                    start.linkTo(iconNome.end, margin = 10.dp)
                    top.linkTo(iconApelido.bottom, margin = 35.dp)
                }
            )
            if (UserData.nomeEncontrado.length > 20){ //JulioCarecaJulioCarecaJulioCareca
                Text(text = "${UserData.nomeEncontrado.substring(0,nomeMaxCaracteres) + "..."}",
                    fontSize = 30.sp,
                    fontFamily = Dongle,
                    color = Color(0xFF838383),
                    modifier = Modifier.constrainAs(nome){
                        start.linkTo(iconNome.end, margin = 20.dp)
                        top.linkTo(sobreMim.bottom, margin = 140.dp)
                    }
                )
            }
            else{
                Text(text = "${UserData.nomeEncontrado}",
                    fontSize = 30.sp,
                    fontFamily = Dongle,
                    color = Color(0xFF838383),
                    modifier = Modifier.constrainAs(nome){
                        start.linkTo(iconNome.end, margin = 20.dp)
                        top.linkTo(sobreMim.bottom, margin = 140.dp)
                    }
                )
            }

            /////////////////////

            //Conjunto do RM/CPSID
            if (!alunoRM.isNullOrEmpty()){ //está na negativa "!", então não pode estar vazio ou nullo.
                Icon(
                    painterResource(id = R.drawable.ic_rmoucps),
                    contentDescription = "Ícone do RM do Aluno",
                    modifier = Modifier
                        .constrainAs(iconRMCPS) {
                            start.linkTo(parent.start, margin = 20.dp)
                            top.linkTo(iconNome.bottom, margin = 40.dp)
                        }
                        .size(38.dp)
                )
                Text(text = "RM",
                    fontSize = 40.sp,
                    fontFamily = Dongle,
                    modifier = Modifier.constrainAs(rmoucpsTitulo){
                        start.linkTo(iconRMCPS.end, margin = 10.dp)
                        top.linkTo(iconNome.bottom, margin = 35.dp)
                    }
                )
                Text(text = UserData.rmEncontrado,
                    fontSize = 30.sp,
                    fontFamily = Dongle,
                    color = Color(0xFF838383),
                    modifier = Modifier.constrainAs(rmoucps){
                        start.linkTo(iconRMCPS.end, margin = 20.dp)
                        top.linkTo(sobreMim.bottom, margin = 210.dp)
                    }
                )
            }
            else{
                Icon(
                    painterResource(id = R.drawable.ic_rmoucps),
                    contentDescription = "Ícone do id CPS",
                    modifier = Modifier
                        .constrainAs(iconRMCPS) {
                            start.linkTo(parent.start, margin = 20.dp)
                            top.linkTo(iconNome.bottom, margin = 40.dp)
                        }
                        .size(38.dp)
                )
                Text(text = "cpsID",
                    fontSize = 40.sp,
                    fontFamily = Dongle,
                    modifier = Modifier.constrainAs(rmoucpsTitulo){
                        start.linkTo(iconRMCPS.end, margin = 10.dp)
                        top.linkTo(iconNome.bottom, margin = 35.dp)
                    }
                )
                Text(text = UserData.cpsIDEncontrado,
                    fontSize = 30.sp,
                    fontFamily = Dongle,
                    color = Color(0xFF838383),
                    modifier = Modifier.constrainAs(rmoucps){
                        start.linkTo(iconRMCPS.end, margin = 20.dp)
                        top.linkTo(sobreMim.bottom, margin = 215.dp)
                    }
                )
            }

            //Conjunto da turma. Separei do de cima e fiz o "if" novamente para ficar mais organizado
            if (!alunoRM.isNullOrEmpty()){
                Icon(
                    painterResource(id = R.drawable.ic_turma),
                    contentDescription = "Ícone da turma do Aluno",
                    modifier = Modifier
                        .constrainAs(iconTurma) {
                            start.linkTo(parent.start, margin = 20.dp)
                            top.linkTo(iconRMCPS.bottom, margin = 40.dp)
                        }
                        .size(38.dp)
                )
                Text(text = "Turma",
                    fontSize = 40.sp,
                    fontFamily = Dongle,
                    modifier = Modifier.constrainAs(turmaTitulo){
                        start.linkTo(iconTurma.end, margin = 10.dp)
                        top.linkTo(iconRMCPS.bottom, margin = 35.dp)
                    }
                )
                Text(text = UserData.turmaEncontrada,
                    fontSize = 30.sp,
                    fontFamily = Dongle,
                    color = Color(0xFF838383),
                    modifier = Modifier.constrainAs(turma){
                        start.linkTo(iconTurma.end, margin = 20.dp)
                        top.linkTo(sobreMim.bottom, margin = 290.dp)
                    }
                )
            }

        }
    }

    //Constraint Layout exclusivo para a arrow de cima
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (arrow) = createRefs()
        // Arrow voltar superior (seta que volta)
        arrowVoltar(
            onClick = {
                navController.navigate("Index")
            },
            modifier = Modifier
                .constrainAs(arrow) {
                    start.linkTo(parent.start, margin = 30.dp)
                    top.linkTo(parent.top, margin = 30.dp)
                }
                .size(34.dp),
            color = Color(0xFFFFFFFF)
        )
    }
    ////
    // Lógica para abrir a galeria/subir a imagem para o Firebase
    if (galeriaState) {
        println("Chamou a função $galeriaState")
       /* SelecionarImagemProfile {  imagem ->
            if (imagem != null) {
                println("Adicionou a imagem.")
                galeriaState = false
                Toast.makeText(context, "Isso pode demorar um pouco. Aguarde, por favor...",Toast.LENGTH_LONG).show()

                val outputStream = ByteArrayOutputStream()
                imagem.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                val imageData = outputStream.toByteArray()

                //Começando com o aluno, infelizmente vai ficar dois blocão de texto.
                if (cpsID.isEmpty()) { //se o cpsID estiver vazio, entendemos que é um aluno que está logado:
                    val alunoPastaRef = storageRef.child("Alunos/Fotos de Perfil/$alunoRM")

                    alunoPastaRef.putBytes(imageData)
                        .addOnSuccessListener { taskSnapshot ->
                            // Primeiro subindo para o Firebase Storage
                            val imageUrl = taskSnapshot.metadata?.reference?.downloadUrl.toString()

                            // Agora vou subir o resultado disso (a URL) para o Firebase Firestore também:
                            val alunoDocument = firestore.collection("Alunos").document(alunoRM)

                            val alunoFoto = hashMapOf(
                                "fotoURL" to imageUrl,
                                "ultimaAtualizacao" to FieldValue.serverTimestamp() // Adiciona a data/hora da atualização
                            )

                            alunoDocument.set(alunoFoto, SetOptions.merge())
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Foto de perfil atualizada com sucesso!", Toast.LENGTH_SHORT).show()
                                    navController.navigate("Profile")
                                }
                                .addOnFailureListener { exception ->
                                    Toast.makeText(context, "Erro ao atualizar a foto de perfil: $exception", Toast.LENGTH_SHORT).show()
                                }
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(context, "Erro ao fazer upload da imagem: $exception", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // Já que o cpsID não está vazio, entendemos que é um professor:
                    val cpsPastaRef = storageRef.child("CPS/Fotos de Perfil/$cpsID")

                    cpsPastaRef.putBytes(imageData)
                        .addOnSuccessListener { taskSnapshot ->
                            // Primeiro subindo para o Firebase Storage
                            val imageUrl = taskSnapshot.metadata?.reference?.downloadUrl.toString()

                            // Agora vou subir o resultado disso (a URL) para o Firebase Firestore também:
                            val cpsDocument = firestore.collection("Cps").document(cpsID)

                            val cpsFoto = hashMapOf(
                                "fotoURL" to imageUrl,
                                "ultimaAtualizacao" to FieldValue.serverTimestamp() // Adiciona a data/hora da atualização
                            )

                            cpsDocument.set(cpsFoto, SetOptions.merge())
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Foto de perfil atualizada com sucesso!", Toast.LENGTH_SHORT).show()
                                    navController.navigate("Profile")
                                }
                                .addOnFailureListener { exception ->
                                    Toast.makeText(context, "Erro ao atualizar a foto de perfil: $exception", Toast.LENGTH_SHORT).show()
                                }
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(context, "Erro ao fazer upload da imagem: $exception", Toast.LENGTH_SHORT).show()
                        }
                }

            }
        }*/
        SelecionarImagemProfile(
            onImageSelected = {imagem ->
                if (imagem != null) {
                    println("Adicionou a imagem.")
                    galeriaState = false
                    Toast.makeText(context, "Isso pode demorar um pouco. Aguarde, por favor...",Toast.LENGTH_LONG).show()

                    val outputStream = ByteArrayOutputStream()
                    imagem.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    val imageData = outputStream.toByteArray()

                    //Começando com o aluno, infelizmente vai ficar dois blocão de texto.
                    if (cpsID.isEmpty()) { //se o cpsID estiver vazio, entendemos que é um aluno que está logado:
                        val alunoPastaRef = storageRef.child("Alunos/Fotos de Perfil/$alunoRM")

                        alunoPastaRef.putBytes(imageData)
                            .addOnSuccessListener { taskSnapshot ->
                                // Primeiro subindo para o Firebase Storage
                                val imageUrl = taskSnapshot.metadata?.reference?.downloadUrl.toString()

                                // Agora vou subir o resultado disso (a URL) para o Firebase Firestore também:
                                val alunoDocument = firestore.collection("Alunos").document(alunoRM)

                                val alunoFoto = hashMapOf(
                                    "fotoURL" to imageUrl,
                                    "ultimaAtualizacao" to FieldValue.serverTimestamp() // Adiciona a data/hora da atualização
                                )

                                alunoDocument.set(alunoFoto, SetOptions.merge())
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "Foto de perfil atualizada com sucesso!", Toast.LENGTH_SHORT).show()
                                        navController.navigate("Profile")
                                    }
                                    .addOnFailureListener { exception ->
                                        Toast.makeText(context, "Erro ao atualizar a foto de perfil: $exception", Toast.LENGTH_SHORT).show()
                                    }
                            }
                            .addOnFailureListener { exception ->
                                Toast.makeText(context, "Erro ao fazer upload da imagem: $exception", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        // Já que o cpsID não está vazio, entendemos que é um professor:
                        val cpsPastaRef = storageRef.child("CPS/Fotos de Perfil/$cpsID")

                        cpsPastaRef.putBytes(imageData)
                            .addOnSuccessListener { taskSnapshot ->
                                // Primeiro subindo para o Firebase Storage
                                val imageUrl = taskSnapshot.metadata?.reference?.downloadUrl.toString()

                                // Agora vou subir o resultado disso (a URL) para o Firebase Firestore também:
                                val cpsDocument = firestore.collection("Cps").document(cpsID)

                                val cpsFoto = hashMapOf(
                                    "fotoURL" to imageUrl,
                                    "ultimaAtualizacao" to FieldValue.serverTimestamp() // Adiciona a data/hora da atualização
                                )

                                cpsDocument.set(cpsFoto, SetOptions.merge())
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "Foto de perfil atualizada com sucesso!", Toast.LENGTH_SHORT).show()
                                        navController.navigate("Profile")
                                    }
                                    .addOnFailureListener { exception ->
                                        Toast.makeText(context, "Erro ao atualizar a foto de perfil: $exception", Toast.LENGTH_SHORT).show()
                                    }
                            }
                            .addOnFailureListener { exception ->
                                Toast.makeText(context, "Erro ao fazer upload da imagem: $exception", Toast.LENGTH_SHORT).show()
                            }
                    }

                }
            },
            cancelar = { state ->
                galeriaState = state

            })
    }


    if (inserirApelido){
        inserirApelido(outlinedApelido, navController)
    }
}


//Função que faz a galeria abrir
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SelecionarImagemProfile(onImageSelected: (Bitmap?) -> Unit, cancelar:(Boolean) -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var cancelarState by remember { mutableStateOf(false) }
    var loadingState by remember { mutableStateOf(true) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        imageUri = it
    }

    LaunchedEffect(Unit) {
        scope.launch {
            launcher.launch("image/*")
        }
    }

    DisposableEffect(imageUri) {
        if (imageUri != null) {
            val bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, imageUri!!)
                ImageDecoder.decodeBitmap(source)
            }
            onImageSelected(bitmap)
        }
        onDispose {
            // Serve para executar alguma coisa como limpeza quando a execução acaba, nao sei como usar
        }
    }

    //TESTE LOADING
    if (loadingState){
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(255, 255, 255, 163))
        ){
            val (circularProgress, logo, cancelar) = createRefs()

            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {},
                backgroundColor = Color(0, 0, 0, 136) //Desfoque

            ) {}

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
            Card(
                modifier = Modifier
                    .constrainAs(cancelar) {
                        top.linkTo(circularProgress.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .clickable {
                        cancelar(cancelarState)
                        loadingState = false
                    },
                backgroundColor = Color.White,
                elevation = 8.dp
            ) {
                Text(
                    text = "Cancelar",
                    fontSize = 34.sp,
                    fontFamily = Jomhuria,
                    lineHeight = (15.sp),
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .clickable {
                            cancelar(cancelarState)
                            loadingState = false
                        }
                )
            }

        }
    }

}


@Composable
fun inserirApelido(apelidoUsuario:String, navController: NavController){

    val coroutineScope = rememberCoroutineScope()

    val firestore = Firebase.firestore
    val alunoRM = UserData.rmEncontrado
    val cpsID = UserData.cpsIDEncontrado

    val context = LocalContext.current

    LaunchedEffect(Unit){
        println("Entrou no launched")
        coroutineScope.launch {
            if (cpsID.isNullOrEmpty()){ //entendemos que é um aluno
                println("Achou um aluno")
                val usuarioColecao = firestore.collection("Alunos")
                val alunoDocument = usuarioColecao.document("$alunoRM")

                alunoDocument.get()
                    .addOnSuccessListener {Document->
                        val atualizarApelido = hashMapOf(
                            "apelido" to apelidoUsuario
                        )

                        //Mesclando os dados para nao excluir o que já está lá
                        alunoDocument.set(atualizarApelido, SetOptions.merge())
                            .addOnSuccessListener {
                                Toast.makeText(context,"Apelido atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                                UserData.apelidoUsuario = apelidoUsuario
                            }
                            .addOnFailureListener {exception ->
                                Toast.makeText(context,"Erro ao atualizar o apelido.", Toast.LENGTH_SHORT).show()
                                println(exception)
                            }
                    }
                    .addOnFailureListener{
                        println("O documento nao existe.")
                    }
            }
            else{ //entendemos que é um cps
                val usuarioColecao = firestore.collection("Cps")
                val alunoDocument = usuarioColecao.document("$cpsID")

                alunoDocument.get()
                    .addOnSuccessListener {Document->
                        val atualizarApelido = hashMapOf(
                            "apelido" to apelidoUsuario
                        )

                        //Mesclando os dados para nao excluir o que já está lá
                        alunoDocument.set(atualizarApelido, SetOptions.merge())
                            .addOnSuccessListener {
                                UserData.apelidoUsuario = apelidoUsuario
                                Toast.makeText(context,"Apelido atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {exception ->
                                Toast.makeText(context,"Erro ao atualizar o apelido.", Toast.LENGTH_SHORT).show()
                                println(exception)
                            }
                    }
                    .addOnFailureListener{
                        println("O documento nao existe.")
                    }
            }
            delay(1000)
            navController.navigate("Profile")
        }

    }

}

