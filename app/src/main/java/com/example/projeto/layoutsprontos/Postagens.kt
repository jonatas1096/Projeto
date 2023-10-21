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
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.projeto.R
import com.example.projeto.datasource.UserData
import com.example.projeto.ui.theme.Dongle
import com.example.projeto.ui.theme.Jomhuria
import com.example.projeto.ui.theme.LARANJA
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await


@SuppressLint("SuspiciousIndentation")
@Composable
fun Postagem(fotoPerfil:String, nomeAutor:String, rm:String, apelidoAutor:String, textoPostagem:String, imagensPost: List<String>, tituloAutor:String, turmasMarcadas: List<String>, paginas:Int) {

    val iconecurtir = painterResource(id = R.drawable.ic_curtir)
    val iconecomentarios = painterResource(id = R.drawable.ic_comentarios)
    val iconecompartilhar = painterResource(id = R.drawable.ic_compartilhar)


    val maxCaracteresNome = 18
    val maxCaracteresApelido = 16


//
    //Container principal da postagem. Esse é o retângulo que vai guardar tudo
    Box(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxWidth()
            // .size( if(!imagensPost.isNullOrEmpty()) 470.dp else 240.dp)
            .padding(40.dp, 10.dp, 15.dp, 0.dp)
            .border(2.dp, Color.Black)
    ) {

        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
                .border(2.dp, Color.Black)
        ) {

            val (foto, nome, apelido, titulo, tagTurmas, texto, imagemPost, fotoReacao, comentarios, compartilhamentos,
                linha, numeroReacoes, curtir, comentar, compartilhar, linhaestetica2) = createRefs()


            //Essa é uma box para guardar a imagem do perfil do usuário.
            Box(
                modifier = Modifier
                    .constrainAs(foto) {
                        start.linkTo(parent.start, margin = -35.dp)
                        top.linkTo(parent.top, margin = 7.dp)
                    }
                    .size(50.dp)
                    .clip(CircleShape)
            ) {
                loadImage(
                    path = fotoPerfil,
                    contentDescription = "Foto",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                )
                if (fotoPerfil.isNullOrEmpty()) {
                    loadImage(
                        path = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/imagemdefault.jpg",
                        contentDescription = "Foto",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                    )
                }
            }


            //Nome do usuário
            if (nomeAutor.length > maxCaracteresNome) {
                Text(text = nomeAutor.substring(0, maxCaracteresNome) + "..",
                    color = if (rm in setOf("23627", "12345")) {
                        Color(0xFF9B26BB)
                    } else {
                        Color(70, 70, 70, 255)
                    },
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.constrainAs(nome) {
                        start.linkTo(foto.end, margin = 7.dp)
                        top.linkTo(parent.top, margin = 5.dp)
                    })
            } else {
                Text(text = nomeAutor,
                    color = if (rm in setOf("23627", "12345")) {
                        Color(0xFF9B26BB)
                    } else {
                        Color(70, 70, 70, 255)
                    },
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.constrainAs(nome) {
                        start.linkTo(foto.end, margin = 7.dp)
                        top.linkTo(parent.top, margin = 5.dp)
                    })

            }

            //Turmas que foram marcadas
            Text(text = if (turmasMarcadas.isNullOrEmpty()) "[Geral]" else "$turmasMarcadas",
                fontSize = 24.sp,
                fontFamily = Jomhuria,
                color = LARANJA,
                modifier = Modifier
                    .constrainAs(tagTurmas) {
                        top.linkTo(nome.bottom, margin = (-3).dp)
                        start.linkTo(foto.end, margin = 7.dp)
                    }
                    .background(color = Color.White)
            )

            //Titulo da publicação
            Text(text = tituloAutor,
                color = Color(0, 0, 0, 255),
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                fontFamily = Dongle,
                modifier = Modifier.constrainAs(titulo) {
                    start.linkTo(foto.end, margin = 7.dp)
                    top.linkTo(tagTurmas.bottom, margin = (-10).dp)
                })


            //Apelido (se houver)
            if (!apelidoAutor.isNullOrEmpty()) { // " ! " de negação, ou seja, não está vazio ou nullo.
                if (apelidoAutor.length > 16){
                    Text(text = "($apelidoAutor)".substring(0, maxCaracteresApelido) + "..)",
                        color = Color(148, 148, 148, 255),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.constrainAs(apelido) {
                            start.linkTo(nome.end, margin = 7.dp)
                            top.linkTo(parent.top, margin = 5.dp)
                        })
                }
               else{
                    Text(text = "($apelidoAutor)",
                        color = Color(148, 148, 148, 255),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.constrainAs(apelido) {
                            start.linkTo(nome.end, margin = 7.dp)
                            top.linkTo(parent.top, margin = 5.dp)
                        })
                }
            }





            //Texto da publicação
            Row(
                modifier = Modifier
                    .constrainAs(texto) {
                        start.linkTo(foto.end, margin = 7.dp)
                        top.linkTo(titulo.bottom, margin = (-8).dp)
                    }
            ) {
                var maxCaracteresTexto = rememberSaveable() { mutableStateOf(250) }
                if (textoPostagem.length > maxCaracteresTexto.value) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = textoPostagem.substring(0, maxCaracteresTexto.value) + "... ",
                            fontSize = 25.sp,
                            color = Color(39, 39, 39, 255),
                            fontFamily = Dongle,
                            lineHeight = (12).sp,
                        )
                        if (maxCaracteresTexto.value < textoPostagem.length){
                            Text(
                                text = "<Ver mais>",
                                fontSize = 18.sp,
                                color = LARANJA,
                                modifier = Modifier.clickable {
                                    maxCaracteresTexto.value = textoPostagem.length
                                }
                            )
                        }
                    }

                } else {
                    Text(
                        text = textoPostagem,
                        fontSize = 25.sp,
                        color = Color(39, 39, 39, 255),
                        fontFamily = Dongle,
                        lineHeight = (12).sp,
                    )
                }
            }



            // }

            //Imagem da publicação (se houver)
            if (!imagensPost.isNullOrEmpty()){
                Box(
                    modifier = Modifier
                        .constrainAs(imagemPost) {
                           // start.linkTo(foto.end, /*margin = 7.dp*/)*/
                            top.linkTo(texto.bottom, margin = (-5).dp)
                            //end.linkTo(parent.end)
                        }
                        .fillMaxWidth()
                        .size(220.dp)
                ) {
                    loadCoil(imagensPost = imagensPost, contentDescription = "")
                }
            }



            //Fotinha
            Box(
                modifier = Modifier
                    .constrainAs(fotoReacao) {
                        /*start.linkTo(parent.start, margin = 8.dp)*/

                        if (!imagensPost.isNullOrEmpty()) {
                            top.linkTo(imagemPost.bottom, margin = 5.dp)
                        } else {
                            top.linkTo(tagTurmas.bottom, margin = 5.dp)
                        }


                    }
                    .size(28.dp)
                    .clip(CircleShape)
            ) {
                loadImage(
                    path = "https://nerdhits.com.br/wp-content/uploads/2023/06/buggy-one-piece-768x402.jpg",
                    contentDescription = "Foto",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                )
            }

            //Linha só para estética
            Row(
                modifier = Modifier
                    .constrainAs(linha) {
                        top.linkTo(fotoReacao.bottom, margin = 6.dp)
                    }
                    .fillMaxWidth()
                    .size(1.dp)
                    .background(color = Color(209, 209, 209, 255))
            ) {}

            //Numero de reações
            Text(text = "Luffy + 847", //11
                fontSize = 12.sp,
                color = Color(82, 81, 81, 255),
                modifier = Modifier.constrainAs(numeroReacoes) {
                    start.linkTo(fotoReacao.end, margin = 5.dp)

                    if (!imagensPost.isNullOrEmpty()) {
                        top.linkTo(imagemPost.bottom, margin = 12.dp)
                    } else {
                        top.linkTo(tagTurmas.bottom, margin = 12.dp)
                    }

                }
            )

            //Comentarios
            Text(text = "211 Comentários", //15
                fontSize = 12.sp,
                color = Color(82, 81, 81, 255),

                modifier = Modifier.constrainAs(comentarios) {
                    end.linkTo(parent.end, margin = 162.dp)
                    if (!imagensPost.isNullOrEmpty()) {
                        top.linkTo(imagemPost.bottom, margin = 12.dp)
                    } else {
                        top.linkTo(tagTurmas.bottom, margin = 12.dp)
                    }
                }
            )

            //Compartilhamentos
            Text(text = "48 Compartilhamentos", //20
                fontSize = 12.sp,
                color = Color(82, 81, 81, 255),

                modifier = Modifier.constrainAs(compartilhamentos) {
                    end.linkTo(parent.end, margin = 10.dp)

                    if (!imagensPost.isNullOrEmpty()) {
                        top.linkTo(imagemPost.bottom, margin = 12.dp)
                    } else {
                        top.linkTo(tagTurmas.bottom, margin = 12.dp)
                    }
                }
            )




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

                    Text(
                        text = "Curtir",
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

                    Text(
                        text = "Comentar",
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

                    Text(
                        text = "Compartilhar",
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
                    .size(10.dp)
                    .background(color = Color(209, 209, 209, 255))
            ) {}
        }

    }

}

