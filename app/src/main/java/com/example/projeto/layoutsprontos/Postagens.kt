package com.example.projeto.layoutsprontos


import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.projeto.R
import com.example.projeto.datasource.UserData
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await


@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("SuspiciousIndentation")
@Composable
fun Postagem(fotoPerfil:String, nomeAutor:String, textoPostagem:String, imagensPost: List<String>, tituloAutor:String, paginas:Int, navController: NavController) {

    val iconecurtir = painterResource(id = R.drawable.ic_curtir)
    val iconecomentarios = painterResource(id = R.drawable.ic_comentarios)
    val iconecompartilhar = painterResource(id = R.drawable.ic_compartilhar)

    val scroll = rememberScrollState()
    val pagerState =  rememberPagerState()


    //Container principal da postagem. Esse é o retângulo que vai guardar tudo
    Box(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxWidth()
            .size(410.dp)
            .padding(bottom = 15.dp)
    ) {

    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {

        val (foto, nome, titulo, texto, imagemPost, fotoReacao, comentarios, compartilhamentos,
            linha, numeroReacoes, curtir, comentar, compartilhar, linhaestetica2) = createRefs()


            //Essa é uma box para guardar a imagem do perfil do usuário.
            Box(
                modifier = Modifier
                    .constrainAs(foto) {
                        start.linkTo(parent.start, margin = 7.dp)
                        top.linkTo(parent.top, margin = 7.dp)
                    }
                    .size(50.dp)
                    .clip(CircleShape)
            ){
                loadImage(path = fotoPerfil,
                    contentDescription = "Foto",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                )
            }


        //Nome do usuário
            Text(text = nomeAutor,
                color = Color(56, 56, 56, 255),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            modifier = Modifier.constrainAs(nome){
                start.linkTo(foto.end, margin = 7.dp)
                top.linkTo(parent.top, margin = 5.dp)
            })

        //Titulo da publicação
            Text(text = tituloAutor,
            color = Color(221, 114, 0, 255),
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            modifier = Modifier.constrainAs(titulo){
                start.linkTo(foto.end, margin = 7.dp)
                top.linkTo(parent.top, margin = 30.dp)
            })


        //Texto da publicação
        Row(
            modifier = Modifier
                .constrainAs(texto) {
                    start.linkTo(parent.start)
                    top.linkTo(foto.bottom, margin = 5.dp)
                }
                .padding(start = 10.dp)
                .padding(end = 20.dp)
        ) {
            Text(text = textoPostagem,
                fontSize = 16.sp,
                color = Color(39, 39, 39, 255),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }


        //Imagem da publicação
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            HorizontalPager(
                state = pagerState,
                pageCount = paginas
            ) { pageIndex ->
                val imagemUrl = imagensPost.getOrNull(pageIndex) // Obtém a URL da imagem com base na página atual
                imagemUrl?.let { url ->
                    // Carrega e exibe a imagem usando o Coil ou Glide aqui
                    loadCoil(
                        imagensPost  = imagensPost,
                        contentDescription = "Imagem da página $pageIndex"
                    )
                }
            }
        }


        //Fotinha
        Box(
            modifier = Modifier
                .constrainAs(fotoReacao) {
                    start.linkTo(parent.start, margin = 8.dp)
                    top.linkTo(imagemPost.bottom, margin = 5.dp)
                }
                .size(28.dp)
                .clip(CircleShape)
        ) {
            loadImage(path = "https://nerdhits.com.br/wp-content/uploads/2023/06/buggy-one-piece-768x402.jpg",
                contentDescription = "Foto",
                contentScale = ContentScale.Crop,
                modifier = Modifier)
        }

        //Numero de reações
        Text(text = "Luffy + 847", //11
            fontSize = 12.sp,
            color = Color(82, 81, 81, 255),
            modifier = Modifier.constrainAs(numeroReacoes){
                start.linkTo(fotoReacao.end, margin = 5.dp)
                top.linkTo(imagemPost.bottom, margin = 12.dp)
            }
        )

        //Comentarios
        Text(text = "211 Comentários", //15
            fontSize = 12.sp,
            color = Color(82, 81, 81, 255),

            modifier = Modifier.constrainAs(comentarios){
                end.linkTo(parent.end, margin = 142.dp)
                top.linkTo(imagemPost.bottom, margin = 12.dp)
            }
        )

        //Compartilhamentos
        Text(text = "48 Compartilhamentos", //20
            fontSize = 12.sp,
            color = Color(82, 81, 81, 255),

            modifier = Modifier.constrainAs(compartilhamentos){
                end.linkTo(parent.end, margin = 10.dp)
                top.linkTo(imagemPost.bottom, margin = 12.dp)
            }
        )


        //Linha só para estética
        Row(
            modifier = Modifier
                .constrainAs(linha) {
                    top.linkTo(fotoReacao.bottom, margin = 6.dp)
                }
                .fillMaxWidth()
                .size(1.dp)
                .background(color = Color(209, 209, 209, 255))
        ) {

        }


        //Botão de like
        IconButton(onClick = {

        },
        modifier = Modifier
            .constrainAs(curtir) {
                start.linkTo(parent.start, margin = 20.dp)
                top.linkTo(linha.bottom)
                bottom.linkTo(parent.bottom, margin = 3.dp)
            }

        ) {
                Row() {
                    Icon(
                        painter = iconecurtir,
                        contentDescription = "Icone para curtir",
                        modifier = Modifier
                            .size(24.dp)
                            .padding(end = 5.dp)
                    )

                    Text(text = "Curtir",
                        fontSize = 15.sp,
                        color = Color(68, 68, 68, 255),

                    )
                }

        }
        //


        //Botão de Comentários
        IconButton(onClick = {

        },
            modifier = Modifier
                .constrainAs(comentar) {
                    start.linkTo(parent.start, margin = 123.dp)
                    top.linkTo(linha.bottom)
                    bottom.linkTo(parent.bottom, margin = 3.dp)
                }

        ) {
            Row() {
                Icon(
                    painter = iconecomentarios,
                    contentDescription = "Icone para os comentários",
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 5.dp)
                )

                Text(text = "Comentar",
                    fontSize = 15.sp,
                    color = Color(68, 68, 68, 255),

                    )
            }

        }
        //

        //Botão de Compartilhar
        IconButton(onClick = {

        },
            modifier = Modifier
                .constrainAs(compartilhar) {
                    start.linkTo(parent.start, margin = 250.dp)
                    top.linkTo(linha.bottom)
                    bottom.linkTo(parent.bottom, margin = 3.dp)
                }

        ) {
            Row() {
                Icon(
                    painter = iconecompartilhar,
                    contentDescription = "Icone para Compartilhar",
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 5.dp)
                )

                Text(text = "Compartilhar",
                    fontSize = 15.sp,
                    color = Color(68, 68, 68, 255),

                    )
            }

        }
        //

        Row(
            modifier = Modifier
                .constrainAs(linhaestetica2) {
                    top.linkTo(compartilhar.bottom)
                }
                .fillMaxWidth()
                .size(8.dp)
                .background(color = Color(209, 209, 209, 255))
        ) {

        }
        }

    }

}

