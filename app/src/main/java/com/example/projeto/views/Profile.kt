package com.example.projeto.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.projeto.datasource.UserData
import com.example.projeto.layoutsprontos.arrowVoltar
import com.example.projeto.layoutsprontos.loadImage
import com.example.projeto.ui.theme.Dongle
import com.example.projeto.viewmodel.PublicacaoViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


@SuppressLint("UnrememberedMutableState")
@Composable
fun Profile(navController: NavController, viewModel: PublicacaoViewModel = hiltViewModel()) {

    // Unica forma que eu consegui pra abrir a galeria sendo uma função composable
    // aqui é só a lógica da galeria
    val imagemUrl = remember { mutableStateOf<String?>(null) }
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

    // Lógica para pegar a URL da imagem que o usuário vai subir pro Firebase (o início da lógica não é aqui, eu só renderizo antes)
    if (!alunoRM.isNullOrEmpty()) {
        val alunoRef = storageRef.child("Alunos/Fotos de Perfil").child(alunoRM)
        alunoRef.downloadUrl
            .addOnSuccessListener { uri ->
                val url = uri.toString()
                println("URL obtida: $url")
                imagemUrl.value = url
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
                imagemUrl.value = url
            }
            .addOnFailureListener { exception ->
                println("A URL não pôde ser obtida. Erro: $exception")
            }
    }





    // Fundo
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Imagem que vai ficar de fundo
        loadImage(
            path = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/background_perfil_prtofessor.png",
            contentDescription = "Background do Login",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }

    // Box da tela
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp)
    ) {
        // Constraint do quadrado e do conteúdo de dentro
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (card, arrow, areaFoto, titulo, nomeUsuario, julio, ines) = createRefs()

                // Firebase upar foto (teste)
                // Área que vai guardar as informações
                Card(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(25.dp),
                    modifier = Modifier.fillMaxSize()
                        .constrainAs(card){
                        }
                ) {
                }

            // Arrow voltar (seta que volta)
            arrowVoltar(
                onClick = {
                    navController.navigate("Index")
                },
                modifier = Modifier.constrainAs(arrow){
                    start.linkTo(card.start, margin = 15.dp)
                    top.linkTo(card.top)
                }
                    .size(40.dp)
                    .border(2.dp, Color.Red),
                color = Color(0xFF000000) //nesse color tu pode escolher a cor da arrow, pode ser uma padrão tipo: Color.Black,
                // ou hexadecimal/rgb como deixei aí, blz?
            )

            // Area de mudar a foto
            Surface(
                shape = CircleShape,
                modifier = Modifier
                    .constrainAs(areaFoto) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top, margin = 45.dp)
                        end.linkTo(parent.end)
                    }
                    .size(135.dp)
                    .border(2.dp, Color.Black)
                    .clickable(onClick = {
                        galeriaState = true
                        exibirImagemPadrao = false
                    })
            ) {
                // Aqui é uma lógica para a foto de perfil.
                // Primeiro, antes de qualquer outra coisa eu já carrego a imagem padrão do github.
                // Depois, uma imagem será carregada no lugar caso a imagemURL não for "null".
                // Se for null, a padrão vai continuar por lá
                loadImage(
                    path = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/imagemdefault.jpg",
                    contentDescription = "Imagem default do usuário",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                imagemUrl.value?.let { url ->
                    if (imagemUrl.value != null) {
                        loadImage(
                            path = url,
                            contentDescription = "Imagem default do usuário",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            Text(
                text = "Meu Perfil",
                fontSize = 40.sp,
                fontFamily = Dongle,
                modifier = Modifier.constrainAs(titulo){
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(areaFoto.top, margin = (-5).dp)
                }
            )

            Text(
                text = "Usuário: ${UserData.nomeEncontrado}",
                fontSize = 40.sp,
                fontFamily = Dongle,
                modifier = Modifier.constrainAs(nomeUsuario){
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(areaFoto.bottom, margin = 0.dp)
                }
            )


            // Estudos Anahi
            loadImage(
                path = "https://static.wikia.nocookie.net/cocorico/images/e/e3/Julio-careca.jpg/revision/latest?cb=20211011002720&path-prefix=pt-br",
                contentDescription = "julio careca",
                contentScale = ContentScale.None,
                modifier = Modifier
                    .size(120.dp)
                    .constrainAs(julio) {
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
                    .width(360.dp)
                    .height(765.dp)
            )
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "AAAAAAAAAAA")
                    Box(
                        modifier = Modifier
                            .background(Color.Black, shape = RoundedCornerShape(20.dp))
                            .width(100.dp)
                            .height(100.dp)
                            .padding(horizontal = (70.dp))
                    )
                }

            loadImage(
                path = "https://rd1.com.br/wp-content/uploads/2016/06/In%C3%AAs-Brasil-32-810x442.jpg",
                contentDescription = "ines",
                contentScale = ContentScale.None,
                modifier = Modifier
                    .size(120.dp)
                    .constrainAs(ines) {
                        start.linkTo(julio.end) // Primeiro membro: Ines // Segundo membro: a referência dela
                        // Em outras palavras, o start da Ines (a esquerda) vai começar à direita do Julio.
                        bottom.linkTo(parent.bottom, margin = 10.dp)
                    }
            )
        }
    }

    // Lógica para abrir a galeria/subir a imagem para o Firebase
    if (galeriaState) {
        println("Chamou a função $galeriaState")
        SelecionarImagemProfile { imagem ->
            if (imagem != null) {
                // imagemSelecionada = imagem
                println("Adicionou a imagem.")
                galeriaState = false

                val outputStream = ByteArrayOutputStream()
                imagem.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                val imageData = outputStream.toByteArray()

                //Começando com o aluno, infelizente vai ficar dois blocão de texto.
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
        }
    }


}


//Função que faz a galeria abrir
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SelecionarImagemProfile(onImageSelected: (Bitmap?) -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var imageUri by remember { mutableStateOf<Uri?>(null) }

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
}
