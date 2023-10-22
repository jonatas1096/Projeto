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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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



    //Container principal da postagem. Esse é o retângulo que vai guardar tudo
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(55.dp, 35.dp, 15.dp, 2.dp)
                //.border(2.dp, Color.Red)
        ) {

            val (boxPostagem, foto, nome, apelido, titulo, tagTurmas, texto, imagemPost, fotoReacao, comentarios,
                linha, numeroReacoes, curtir, comentar) = createRefs()


            //Essa é uma box para guardar a imagem do perfil do usuário.
            Box(
                modifier = Modifier
                    .constrainAs(foto) {
                        //start.linkTo(parent.start, margin = (-52).dp)
                        end.linkTo(boxPostagem.start, margin = 3.dp)
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

            //Essa box abaixo é a que vai guardar a postagem do nome até o texto em si
            Box(
                modifier = Modifier
                    .constrainAs(boxPostagem) {
                        start.linkTo(parent.start)
                    }
                    .clip(RoundedCornerShape(20.dp))
            ) {
                Column(
                    modifier = Modifier
                        .background(Color(0xB3FFFFFF)),
                        verticalArrangement = Arrangement.spacedBy((-12).dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 12.dp),
                    ) {
                        //Nome do usuário
                        if (nomeAutor.length > maxCaracteresNome) {
                            Text(
                                text = nomeAutor.substring(0, maxCaracteresNome) + "..",
                                color = if (rm in setOf("23627", "12345")) {
                                    Color(0xFF9B26BB)
                                } else {
                                    Color(70, 70, 70, 255)
                                },
                                fontWeight = FontWeight.Bold,
                                fontSize = 34.sp,
                                fontFamily = Dongle,
                                overflow = TextOverflow.Ellipsis,
                                /*modifier = Modifier.constrainAs(nome) {
                                    start.linkTo(foto.end, margin = 7.dp)
                                    top.linkTo(parent.top, margin = 5.dp)
                                }*/
                            )
                        } else {
                            Text(
                                text = nomeAutor,
                                color = if (rm in setOf("23627", "12345")) {
                                    Color(0xFF9B26BB)
                                } else {
                                    Color(70, 70, 70, 255)
                                },
                                fontWeight = FontWeight.Bold,
                                fontSize = 34.sp,
                                fontFamily = Dongle,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                /*modifier = Modifier.constrainAs(nome) {
                                    start.linkTo(foto.end, margin = 7.dp)
                                    top.linkTo(parent.top, margin = 5.dp)
                                }*/
                            )
                        }

                        //Apelido (se houver)
                        if (!apelidoAutor.isNullOrEmpty()) { // " ! " de negação, ou seja, não está vazio ou nullo.
                            if (apelidoAutor.length > 16) {
                                Text(
                                    text = "($apelidoAutor)".substring(0, maxCaracteresApelido) + "..)",
                                    color = Color(148, 148, 148, 255),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 28.sp,
                                    fontFamily = Dongle,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    /*modifier = Modifier.constrainAs(apelido) {
                                        start.linkTo(nome.end, margin = 7.dp)
                                        top.linkTo(parent.top, margin = 7.dp)
                                    }*/
                                    modifier = Modifier.padding(start = 3.dp, top = 3.dp)
                                )
                            } else {
                                Text(
                                    text = "($apelidoAutor)",
                                    color = Color(148, 148, 148, 255),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 28.sp,
                                    fontFamily = Dongle,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    /*modifier = Modifier.constrainAs(apelido) {
                                        start.linkTo(nome.end, margin = 7.dp)
                                        top.linkTo(parent.top, margin = 7.dp)
                                    }*/
                                    modifier = Modifier.padding(start = 3.dp, top = 3.dp)
                                )
                            }
                        }
                    }


                    //Turmas que foram marcadas
                    Text(
                        text = if (turmasMarcadas.isNullOrEmpty()) "[Geral]" else "$turmasMarcadas",
                        fontSize = 26.sp,
                        fontFamily = Jomhuria,
                        color = LARANJA,
                        lineHeight = (15).sp,
                        /*modifier = Modifier
                            .constrainAs(tagTurmas) {
                                top.linkTo(nome.bottom, margin = (-14).dp)
                                start.linkTo(foto.end, margin = 7.dp)
                            }*/
                        modifier = Modifier.padding(horizontal = 12.dp)
                            .padding(bottom = 8.dp)
                    )

                    //Titulo da publicação
                    Text(
                        text = tituloAutor,
                        color = Color(0, 0, 0, 255),
                        fontWeight = FontWeight.Bold,
                        fontSize = 29.sp,
                        fontFamily = Dongle,
                        /*modifier = Modifier.constrainAs(titulo) {
                            start.linkTo(foto.end, margin = 7.dp)
                            top.linkTo(tagTurmas.bottom, margin = (-8).dp)
                        }*/
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )


                    //Texto da publicação
                    Row(
                        /*modifier = Modifier
                            .constrainAs(texto) {
                                start.linkTo(foto.end, margin = 7.dp)
                                top.linkTo(titulo.bottom, margin = (-10).dp)
                            }*/
                        modifier = Modifier.padding(horizontal = 12.dp)
                            .padding(bottom = 8.dp)
                    ) {
                        var maxCaracteresTexto = rememberSaveable() { mutableStateOf(250) }
                        if (textoPostagem.length > maxCaracteresTexto.value) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = textoPostagem.substring(
                                        0,
                                        maxCaracteresTexto.value
                                    ) + "... ",
                                    fontSize = 27.sp,
                                    color = Color(39, 39, 39, 255),
                                    fontFamily = Dongle,
                                    lineHeight = (16).sp,
                                )
                                if (maxCaracteresTexto.value < textoPostagem.length) {
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
                                fontSize = 27.sp,
                                color = Color(39, 39, 39, 255),
                                fontFamily = Dongle,
                                lineHeight = (16).sp,
                            )
                        }
                    }


                    //Imagem da publicação (se houver)
                    if (!imagensPost.isNullOrEmpty()) {
                        Card(
                            modifier = Modifier
                                /*.constrainAs(imagemPost) {
                                    top.linkTo(texto.bottom, margin = (-5).dp)
                                }*/
                                .fillMaxWidth()
                                .size(220.dp)
                            ,
                            shape = RoundedCornerShape(18.dp)
                        ) {
                            loadCoil(imagensPost = imagensPost, contentDescription = "")
                        }
                    }


                }
            }




            /*//Fotinha
            Box(
                modifier = Modifier
                    .constrainAs(fotoReacao) {
                        start.linkTo(curtir.end, margin = 10.dp)
                        top.linkTo(linha.bottom, margin = (2).dp)
                    }
                    .size(23.dp)
                    .clip(CircleShape)
            ) {
                loadImage(
                    path = "https://nerdhits.com.br/wp-content/uploads/2023/06/buggy-one-piece-768x402.jpg",
                    contentDescription = "Foto",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                )
            }


            //Numero de reações
            Text(text = "Luffy + 847", //11
                fontSize = 20.sp,
                fontFamily = Dongle,
                color = Color(82, 81, 81, 255),
                modifier = Modifier.constrainAs(numeroReacoes) {
                    start.linkTo(fotoReacao.end, margin = 5.dp)
                    top.linkTo(linha.bottom, margin = 3.dp)
                }
            )

            //Comentarios
            Text(text = "211 Comentários", //15
                color = Color(82, 81, 81, 255),
                fontSize = 20.sp,
                fontFamily = Dongle,
                modifier = Modifier.constrainAs(comentarios) {
                    start.linkTo(numeroReacoes.end, margin = 26.dp)
                    top.linkTo(linha.bottom, margin = 3.dp)
                }
            )


            //Botão de like
            IconButton(
                onClick = {

                },
                modifier = Modifier
                    .constrainAs(curtir) {
                        start.linkTo(parent.start, margin = 20.dp)
                        top.linkTo(fotoReacao.bottom, margin = (-8).dp)
                    }
            ) {
                Row() {
                    Icon(
                        painter = iconecurtir,
                        contentDescription = "Icone para curtir",
                        modifier = Modifier
                            .size(22.dp)
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
                        start.linkTo(numeroReacoes.end, margin = 26.dp)
                        top.linkTo(comentarios.bottom, margin = (-10).dp)
                    }
            ) {
                Row() {
                    Icon(
                        painter = iconecomentarios,
                        contentDescription = "Icone para os comentários",
                        modifier = Modifier
                            .size(22.dp)
                            .padding(end = 5.dp)
                    )
                    Text(
                        text = "Comentar",
                        fontSize = 15.sp,
                        color = Color(68, 68, 68, 255),

                        )
                }

            }*/
            //


        }

}

