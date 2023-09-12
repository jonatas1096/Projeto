package com.example.projeto.views

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.projeto.R
import com.example.projeto.layoutsprontos.CardPostagem
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Publicar(navController: NavController) {

    //toda a palhaçada do jetpack só pra abrir o bottomshet
    val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)
    val scope = rememberCoroutineScope()
    //

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetContent = {
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
                            //contentAlignment = CenterStart,
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color.Red)
                                .clickable(
                                    onClick = {
                                        

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
        { //e a partir destas chaves é a parte de cima
            Card(modifier = Modifier.fillMaxSize(),
                elevation = 10.dp,
                backgroundColor = Color(0xFFFAFAFA)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(10.dp)
                        .border(2.dp, Color.Black)
                ) {
                    Text("Container top")

                }
            }
        }

        //ConstraintLayout para posicionar o que precisa
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (fecharcard) = createRefs()

            IconButton(
                onClick = {

                },
                modifier = Modifier
                    .constrainAs(fecharcard) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .size(60.dp)
            ){
                Image(ImageVector.vectorResource(id = R.drawable.ic_fechar),
                    contentDescription = "Fechar o Card",
                )
            }
        }



    }


}

@Composable
fun abrir(){
    val context = LocalContext.current

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()){
        imageUri = it
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        IconButton(
            onClick = {
                launcher.launch("image/*") },

            ) {
            Image(
                ImageVector.vectorResource(id = R.drawable.ic_publicar),
                contentDescription = "")
            Text(text = "Clica ai seu mané")
        }

        Spacer(modifier = Modifier.height(20.dp))

        imageUri?.let {
            bitmap = if (Build.VERSION.SDK_INT < 28){
                MediaStore.Images.Media.getBitmap(context.contentResolver,it)
            }
            else{
                val source = ImageDecoder.createSource(context.contentResolver,it)
                ImageDecoder.decodeBitmap(source)
            }

            Image(bitmap = bitmap?.asImageBitmap()!!, contentDescription = "",
                modifier = Modifier.size(400.dp))
        }
    }
}