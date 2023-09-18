package com.example.projeto.views

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.projeto.R
import com.example.projeto.datasource.UserData
import com.example.projeto.layoutsprontos.loadImage
import com.example.projeto.viewmodel.PublicacaoViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream


@SuppressLint("UnrememberedMutableState")
@Composable
fun Profile(navController: NavController, viewModel: PublicacaoViewModel = hiltViewModel()) {

    //unica forma que eu consegui pra abrir a galeria sendo uma função composable
    //aqui é só a lógica da galeria
    var galeriaState by remember { mutableStateOf(false) }
    var exibirImagemPadrao by remember { mutableStateOf(true) }
    //
    val imagemUrl = remember { mutableStateOf<String?>(null) }



    //A instância do firebase:
    val firestore = Firebase.firestore //tambem funciona o val firestore = FirebaseFirestore.getInstance(), normal.
    //A instancia do firebase storage e as variaveis que vou precisar:
    val storage = Firebase.storage //Iniciando o firebase storage
    val storageRef = storage.reference
    val alunoRM = UserData.rmEncontrado
    val cpsID = UserData.cpsIDEncontrado
    //
    //
    val context = LocalContext.current
    //

    //Lógica para pegar a url da imagem que o usuário vai subir pro firebase (o inicio da lógica não é aqui)
    val alunoRef = storageRef.child("Alunos").child(alunoRM)
    alunoRef.downloadUrl
        .addOnSuccessListener {uri ->
            val url = uri.toString()
            println("URL obtida: $url")
            imagemUrl.value = url
        }
        .addOnFailureListener { exception ->
            println("A url não pôde ser obtida. Erro: $exception")
        }
    //


    //Fundo
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        //Imagem que vai ficar de fundo
        loadImage(
            path = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/backgroundlogin.png",
            contentDescription = "Background do Login",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )


        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .border(2.dp, Color.Green)
                .padding(25.dp)
        )
        {

            val (areaFoto, julio, ines) = createRefs() //criou as referencias

            //Firebase upar foto (teste)
            //Área que vai guardar as informações
            Card(
                elevation = 8.dp,
                shape = RoundedCornerShape(25.dp),
                modifier = Modifier.fillMaxSize()
            ) {}

            //Area de mudar a foto
            Surface(
                shape = CircleShape,
                modifier = Modifier
                    .constrainAs(areaFoto) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top, margin = 10.dp)
                        end.linkTo(parent.end)
                    }
                    .size(140.dp)
                    .clickable(onClick = {
                        galeriaState = true
                        exibirImagemPadrao = false
                    })
            ) {//Aqui é uma lógica para a foto de perfil. Se a URL não estiver vazia, a imagem vem do firebase. Senão, uma padrão do github vai ser exibida no lugar (else).
                imagemUrl.value?.let {url ->
                    if (imagemUrl.value != null){
                        loadImage(
                            path = url,
                            contentDescription = "Imagem default do usuário",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    else{
                        loadImage(
                            path = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/imagemdefault.jpg",
                            contentDescription = "Imagem default do usuário",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                    }
                }

            }




            //////////////////
            //Estudos Anahi
            loadImage(path = "https://static.wikia.nocookie.net/cocorico/images/e/e3/Julio-careca.jpg/revision/latest?cb=20211011002720&path-prefix=pt-br",
                contentDescription = "julio careca",
                contentScale = ContentScale.None,
                modifier = Modifier
                    .size(120.dp)
                    .constrainAs(julio) {
                        bottom.linkTo(parent.bottom)
                    }

            )
            loadImage(path = "https://rd1.com.br/wp-content/uploads/2016/06/In%C3%AAs-Brasil-32-810x442.jpg",
                contentDescription = "ines",
                contentScale = ContentScale.None,
                modifier = Modifier
                    .size(120.dp)
                    .constrainAs(ines) {

                        start.linkTo(julio.end) //primeiro membro: Ines // segundo membro: a referencia dela
                        //em outras pálavras, o start da ines (a esquerda) vai começar à direita do julio.
                        bottom.linkTo(parent.bottom, margin = 10.dp)
                    }

            )
            //////////////////
        }

    }



    //
    //Lógica para abrir a galeria/subir a imagem para o firebase
    if (galeriaState) {
        println("chamou a função $galeriaState")
        SelecionarImagemProfile{ imagem ->
            if (imagem != null) {
               // imagemSelecionada = imagem
                println("Adicinou a imagem.")
                galeriaState = false

                val outputStream = ByteArrayOutputStream()
                imagem.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                val imageData = outputStream.toByteArray()



                if (cpsID.isEmpty()){
                    val alunoPastaRef = storageRef.child("Alunos/$alunoRM")

                    alunoPastaRef.putBytes(imageData)
                        .addOnSuccessListener { taskSnapshot ->
                            //Primeiro subindo para o firebase storage
                            val imageUrl = taskSnapshot.metadata?.reference?.downloadUrl.toString()


                            //Agora vou subir o resultado disso (a url) para o firebase firestore também:
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
                        .addOnFailureListener{exception ->
                            Toast.makeText(context, "Erro ao fazer upload da imagem: $exception", Toast.LENGTH_SHORT).show()
                        }

                }
                else{

                }


            }
        }
    }


    Text(text = "Usuário: ${UserData.nomeEncontrado}", fontSize = 40.sp)
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

