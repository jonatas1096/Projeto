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
import com.example.projeto.R
import com.example.projeto.datasource.UserData
import com.example.projeto.ui.theme.Dongle
import com.example.projeto.ui.theme.Jomhuria
import com.example.projeto.ui.theme.LARANJA
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@SuppressLint("SuspiciousIndentation")
@Composable
fun Postagem(fotoPerfil:String, nomeAutor:String, rm:String, apelidoAutor:String, textoPostagem:String, imagensPost: List<String>, tituloAutor:String, turmasMarcadas: List<String>,
             idPostagem:String, numerocurtidas:Int, paginas:Int) {

    val iconecurtir = painterResource(id = R.drawable.ic_curtir)
    val iconecomentarios = painterResource(id = R.drawable.ic_comentarios)


    val maxCaracteresNome = 18
    val maxCaracteresApelido = 16

    //Lógica da curtida
    var curtirState by remember{ mutableStateOf(false) }
    var numeroCurtidas by remember { mutableStateOf(numerocurtidas) }

    //Container principal da postagem. Esse é o retângulo que vai guardar tudo
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(55.dp, 35.dp, 15.dp, 2.dp)
        ) {

            val (boxPostagem, foto, fotoReacao, fotoReacao2, curtir, comentar, linhaestetica) = createRefs()


            //Essa é uma box para guardar a imagem do perfil do usuário.
            Box(
                modifier = Modifier
                    .constrainAs(foto) {
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
                        modifier = Modifier
                            .fillMaxWidth()
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
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .padding(bottom = 8.dp)
                    )

                    //Titulo da publicação
                    Text(
                        text = tituloAutor,
                        color = Color(0, 0, 0, 255),
                        fontWeight = FontWeight.Bold,
                        fontSize = 27.sp,
                        fontFamily = Dongle,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )


                    //Texto da publicação
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
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
                                    fontSize = 29.sp,
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
                                fontSize = 29.sp,
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
                                .fillMaxWidth()
                                .size(220.dp),
                            shape = RoundedCornerShape(18.dp)
                        ) {
                            loadCoil(imagensPost = imagensPost, contentDescription = "")
                        }
                    }


                }
            }


            //Parte das reações ao post
            //Botão de like
            IconButton(
                onClick = {
                    println("o id do post é: $idPostagem")
                    curtirState = true
                },
                modifier = Modifier
                    .constrainAs(curtir) {
                        start.linkTo(boxPostagem.start, margin = 8.dp)
                        top.linkTo(boxPostagem.bottom)
                    }
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Icon(
                        painter = iconecurtir,
                        contentDescription = "Icone para curtir",
                        modifier = Modifier
                            .size(28.dp)
                    )
                    Text(
                        text = "$numeroCurtidas",
                        fontSize = 36.sp,
                        fontFamily = Dongle,
                    )
                }

            }

            //Fotinha
            Box(
                modifier = Modifier
                    .constrainAs(fotoReacao) {
                        start.linkTo(curtir.end, margin = 6.dp)
                        top.linkTo(boxPostagem.bottom, margin = 5.dp)
                    }
                    .size(32.dp)
                    .clip(CircleShape)
            ) {
                loadImage(
                    path = "https://nerdhits.com.br/wp-content/uploads/2023/06/buggy-one-piece-768x402.jpg",
                    contentDescription = "Foto",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                )
            }
            //Fotinha2
            Box(
                modifier = Modifier
                    .constrainAs(fotoReacao2) {
                        start.linkTo(fotoReacao.end, margin = (-16).dp)
                        top.linkTo(boxPostagem.bottom, margin = 5.dp)
                    }
                    .size(32.dp)
                    .clip(CircleShape)
            ) {
                loadImage(
                    path = "https://i.imgur.com/5r65eEe.png",
                    contentDescription = "Foto",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                )
            }
            /////////////////////



            //Parte dos Comentários
            IconButton(
                onClick = {
            },
                modifier = Modifier
                    .constrainAs(comentar) {
                        start.linkTo(curtir.end, margin = 80.dp)
                        top.linkTo(boxPostagem.bottom, /*margin = (1).dp*/)
                    }
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(
                        painter = iconecomentarios,
                        contentDescription = "Icone para os comentários",
                        modifier = Modifier
                            .size(28.dp)
                    )
                    Text(
                        text = "32",
                        fontSize = 32.sp,
                        fontFamily = Dongle,
                        modifier = Modifier
                    )
                }

            }

            //Linha estética final
            Row(
                modifier = Modifier
                    .constrainAs(linhaestetica) {
                        top.linkTo(curtir.bottom, margin = (-4).dp)
                    }
                    .fillMaxWidth()
                    .size(2.dp)
                    .background(color = Color(92, 92, 92, 255))
            ){}


        }

    if (curtirState){
        curtirPublicacao(idPostagem, onCurtir = {novoNumeroCurtidas ->
            numeroCurtidas = novoNumeroCurtidas
            curtirState = false
            println("atualizando para $numeroCurtidas")
        })

    }

}


@Composable
fun curtirPublicacao(idPostagem:String, onCurtir: (Int) -> Unit) {

    val firestore = Firebase.firestore // Instância do firebase

    //RM do usuario para marcar a curtida dele no array
    val rmUsuario = UserData.rmEncontrado
    val cpsID = UserData.cpsIDEncontrado
    //Scope
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {

        val idPost = idPostagem
        val postagensCollection = firestore.collection("Postagens")
        postagensCollection.whereEqualTo("idPost", idPost)
            .get()
            .addOnSuccessListener { postagens ->
                val postagemEncontrada = postagens.documents[0]
                val usuariosCurtidas = postagemEncontrada.get("usuariosCurtidas") as? ArrayList<String> ?: ArrayList()
                //Primeiro a parte do array dos usuarios que curtiram

                scope.launch {
                    if (postagemEncontrada.contains("usuariosCurtidas")) { //Ja existe um array usuariosCurtidas
                        println("array existe, é ${usuariosCurtidas.size}")
                        if (!rmUsuario.isNullOrEmpty()) {
                            if (!usuariosCurtidas.contains(rmUsuario)) { //" ! " para negar, ou seja, nao contem.
                                println("nao contem $rmUsuario, entao vamos adicionar.")
                                usuariosCurtidas.add(rmUsuario)
                            } else {
                                println("contem $rmUsuario, entao vamos remover.")
                                usuariosCurtidas.remove(rmUsuario)
                            }
                            val atualizarArray = hashMapOf(
                                "usuariosCurtidas" to usuariosCurtidas
                            )
                            postagemEncontrada.reference.set(atualizarArray, SetOptions.merge())
                                .addOnSuccessListener {
                                    println("A lista de curtidas foi atualizada com sucesso com o valor $usuariosCurtidas")
                                }
                                .addOnFailureListener {
                                    println("Erro ao curtir: $it")
                                }
                        } else {
                            if (!usuariosCurtidas.contains(cpsID)) { //" ! " para negar, ou seja, nao contem.
                                usuariosCurtidas.add(cpsID)
                            } else {
                                usuariosCurtidas.remove(cpsID)
                            }

                            val atualizarArray = hashMapOf(
                                "usuariosCurtidas" to usuariosCurtidas
                            )
                            postagemEncontrada.reference.set(atualizarArray, SetOptions.merge())
                                .addOnSuccessListener {
                                    println("A lista de curtidas foi atualizada com sucesso!")
                                }
                                .addOnFailureListener {
                                    println("Erro ao atualizar a lista: $it")
                                }
                        }
                    } else {//Nao existe o array, entao vamos criar e adicionar o primeiro usuario.
                        val usuariosCurtidas = ArrayList<String>() //criamos o array
                        if (!rmUsuario.isNullOrEmpty()) {
                            usuariosCurtidas.add(rmUsuario)
                        } else {
                            usuariosCurtidas.add(cpsID)
                        }

                        val atualizarArray = hashMapOf(
                            "usuariosCurtidas" to usuariosCurtidas
                        )
                        postagemEncontrada.reference.set(atualizarArray, SetOptions.merge())
                            .addOnSuccessListener {
                                println("A lista de curtidas foi atualizada com sucesso!")
                            }
                            .addOnFailureListener {
                                println("Erro ao atualizar a lista: $it")
                            }

                    }
                    delay(500)
                    println("saiu da primeira etapa, está com ${usuariosCurtidas.size}")
                    var arraySize = usuariosCurtidas.size
                    println("teste $arraySize")

                    //Agora o número de curtidas do post em si
                    //Validação do campo pré-existente "curtidas"
                    var curtidasConversao = 0 //Auxiliar na conversao das curtidas
                    if (postagemEncontrada.contains("curtidas")) { //Caso ja exista um campo previamente com curtidas

                        //Nesse ponto do código esse array abaixo ja existe, é certeza. Então é só utilizar ele.
                        println("o tamanho do arraySize é $arraySize")
                        println("ENTROU NO SEGUNDO SCOPE, O tamanho do array no momento é $arraySize, ele contem o valor $usuariosCurtidas")
                        val curtirPublicacao = hashMapOf(
                            "curtidas" to arraySize, //altera o valor da curtida para o tamanho do array usuariosCurtidas
                            //ou seja, cada rm ou cpsID é unico, entao se o numero de curtidas espelhar o tamanho desse array tambem vai ser.

                        )
                        //Subindo o valor:
                        postagemEncontrada.reference.set(curtirPublicacao, SetOptions.merge())
                            .addOnSuccessListener {
                                curtidasConversao = arraySize
                                println("curtidasConversao é de $curtidasConversao")
                                onCurtir(curtidasConversao)
                            }
                            .addOnFailureListener {
                                println("Erro ao curtir: $it")
                            }
                    } else { //não existe, vamos criar
                        println("ELE NAO EXISTE, ENTAO VAI SER 1.")
                        val curtirPublicacao = hashMapOf(
                            "curtidas" to 1 //adiciona o valor da curtida como 1, já que não existe nenhuma curtida no momento.
                        )
                        postagemEncontrada.reference.set(curtirPublicacao, SetOptions.merge())
                            .addOnSuccessListener {
                                println("A curtida foi adicionada com sucesso!")
                                curtidasConversao = 1
                                onCurtir(curtidasConversao)
                            }
                            .addOnFailureListener {
                                println("Erro ao curtir: $it")
                            }
                    }
                }
            }
            .addOnFailureListener {
                println("O documento não existe. $it")
            }

    }


}

