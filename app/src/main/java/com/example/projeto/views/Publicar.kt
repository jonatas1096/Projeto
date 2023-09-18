package com.example.projeto.views

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.projeto.R
import com.example.projeto.viewmodel.PublicacaoViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Publicar(navController: NavController, viewModel: PublicacaoViewModel = hiltViewModel()) {
    ////////////////////////////////

    //toda a palhaçada do jetpack só pra abrir o bottomshet
    val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)
    val scope = rememberCoroutineScope()
    //

    //unica forma que eu consegui pra abrir a galeria sendo uma função composable
    var galeriaState by remember { mutableStateOf(false) }
    //a lista das imagens p ser exibidas
    val imagensColuna = remember { mutableStateListOf<Bitmap>() }



    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = { //essa parte do sheetContent é a parte de baixo (kkkkkk vai entender)
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(240.dp))
            {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 10.dp)
                        .padding(end = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    //botão para subir o bottomsheet
                    IconButton(onClick = {
                        scope.launch {
                            if (sheetState.isCollapsed){
                                sheetState.expand()
                            }
                            else{
                                sheetState.collapse()
                            }

                        }
                    }) {
                        Image(ImageVector.vectorResource(id = R.drawable.ic_minus),
                            contentDescription = "Subir o BottomSheet",
                            modifier = Modifier
                                .size(80.dp),
                            colorFilter = ColorFilter.tint(Color(0xFFC5C4C4))
                        )
                    }


                    //Midias
                    Row(

                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.Red)
                            .clickable(
                                onClick = {
                                    galeriaState = true
                                })
                    ) {
                        //Imagem da imagem
                        Image(ImageVector.vectorResource(id = R.drawable.ic_imagem),
                            contentDescription = "Adicionar foto ou video",
                            modifier = Modifier
                                .size(38.dp)
                            /*colorFilter = ColorFilter.tint(Color(0xFFC5C4C4)
                            )*/
                        )

                        //Só pra espaçar um pouco a imagem e o texto
                        Spacer(modifier = Modifier
                            .width(20.dp)
                            .border(2.dp, Color.Green))

                        //Texto da Imagem
                        Text(text = "Foto/vídeo",
                            fontSize = 25.sp,
                            color = Color(0xFF303030),
                        )

                    }

                }


            }
        },
        sheetBackgroundColor = Color(0xFFFFFFFF),
        sheetShape = RoundedCornerShape(25.dp, 25.dp,0.dp, 0.dp),
        sheetElevation = 8.dp,
    )
    {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {

            //Começo constraint layout.
            //Eu vou começar por ele pra que as coisas que eu posicionar aqui tenham um menor hierarquia nas camadas em geral.
            //Tô optando por ele porque a forma padrão tava bugando dms
            val (areaPublicar, areaTexto,areaTitulo, boxImagem) = createRefs()

            var titulo by remember { mutableStateOf("") }
            var texto by remember { mutableStateOf("") }


            Box(
                modifier = Modifier
                    .constrainAs(areaPublicar) {
                        top.linkTo(parent.top)
                    }
                    .fillMaxWidth()
                    .size(50.dp)

            ) {
                Row(
                    Modifier
                        .fillMaxSize()
                        .border(1.dp, Color.Green),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Criar Publicação",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Button(
                        onClick = {

                    },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFFE7E6E6)
                        ),
                        modifier = Modifier
                            .padding(5.dp)
                            .padding(start = 20.dp)
                    ) {
                        Text(text = "Publicar",
                        color = Color(0xFFBDBBBB))
                    }
                }
            }
            //Area do título
            OutlinedTextField(
                value = titulo,
                onValueChange = {
                    titulo = it
                },
                label = {
                    Text(text = "Título da publicação",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = Color(0xFFB4CEE7)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(areaTitulo) {
                        top.linkTo(parent.top, margin = 50.dp)
                    }
                    .height(60.dp)
            )


            //Area para escrever o texto da publicação
            OutlinedTextField(
                value = texto,
                onValueChange = {
                    texto = it
                },
                label = {
                        Text(text = "Digite")
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = Color(0xFFFAFAFA)
                ),
                modifier = Modifier
                    .constrainAs(areaTexto) {
                        top.linkTo(areaTitulo.bottom)
                    }
                    .fillMaxWidth()
                    .height(150.dp)
            )

                LazyColumn(
                    modifier = Modifier
                        .constrainAs(boxImagem) {
                            top.linkTo(areaTexto.bottom)
                        }
                        .fillMaxWidth()
                        .height(220.dp)
                        .border(2.dp, Color.Green)
                ) {
                    items(imagensColuna) { imagem ->
                        println("executou $galeriaState")
                        Image(
                            bitmap = imagem.asImageBitmap(),
                            contentDescription = "Imagem Selecionada",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxSize()
                                //Esse modificador abaixo serviu para adequar a imagem totalmente à coluna.
                                .aspectRatio(
                                    ratio = imagem.width.toFloat() / imagem.height.toFloat(),
                                    matchHeightConstraintsFirst = false
                                )

                        )
                    }

                }


            //}

            //Fim Constraint.
            //

        }


    }

    if (galeriaState) {
        println("chamou a função $galeriaState")
        SelecionarImagem{ imagem ->
            if (imagem != null) {
                imagensColuna.add(imagem)
                println("Adicinou a imagem.")
                galeriaState = false
            }
        }
    }



}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SelecionarImagem(onImageSelected: (Bitmap?) -> Unit) {
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