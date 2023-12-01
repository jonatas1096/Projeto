package com.connectstudent.projeto.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.connectstudent.projeto.datasource.PostagemData
import com.connectstudent.projeto.datasource.UserData
import com.connectstudent.projeto.layoutsprontos.arrowVoltar
import com.connectstudent.projeto.layoutsprontos.layoutComentarios
import com.connectstudent.projeto.layoutsprontos.loadImage
import com.connectstudent.projeto.ui.theme.Dongle
import com.connectstudent.projeto.ui.theme.Jomhuria
import com.connectstudent.projeto.ui.theme.LARANJA
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

@Composable
fun minhasPublicacoes(navController: NavController){

    val postagensOrdenadas = remember { mutableStateListOf<PostagemData>() }

    val firestore = Firebase.firestore
    val scope = rememberCoroutineScope()
    var pubState = remember{ mutableStateOf(false) }

    LaunchedEffect(Unit){
        scope.launch {
            //Parte para trazer as postagens
            val postagensRef = firestore.collection("Postagens")

            val filaOrdenar = postagensRef
                .orderBy("ultimaAtualizacao", Query.Direction.DESCENDING)

            filaOrdenar.get()
                .addOnSuccessListener {postagens ->
                    println("Entrou no onSucess (ordenando os dados)")
                    println("Tamanho de postagens: ${postagens.size()}")
                    val postagensData = mutableListOf<PostagemData>()
                    for (posts in postagens){
                        val fotoPerfil = posts.getString("fotoPerfil") ?: ""
                        val imagensPostagem = posts.get("imagensPostagem") as? List<String> ?: emptyList()
                        val nome = posts.getString("nome") ?: ""
                        val rm = posts.getString("RM") ?: ""
                        val cpsID = posts.getString("cpsID") ?: ""
                        val apelido = posts.getString("apelido") ?: ""
                        val texto = posts.getString("texto") ?: ""
                        val titulo = posts.getString("titulo") ?: ""
                        val turmas = posts.get("turmasMarcadas") as? List<String> ?: emptyList()
                        val idPost = posts.getString("idPost") ?: ""
                        //o numero de curtidas eu vou converter de long para int.
                        var Curtidas = 0
                        //Além disso, a validação do campo foi meio que necessário para nao quebrar o código:
                        if (posts.contains("curtidas")){
                            val numeroCurtidas = posts.getLong("curtidas")?.toInt()
                            if (numeroCurtidas != null) {
                                Curtidas = numeroCurtidas
                            }
                        }
                        var Comentarios = 0

                        if (posts.contains("comentarios")){
                            val comentarios = posts.get("comentarios")as? List<String>
                            val numeroComentarios = comentarios?.size ?: 0
                            Comentarios = numeroComentarios
                        }




                        println("Agora vai armazenar os dados na val postagemData.")
                        val postagemData = PostagemData(
                            fotoPerfil = fotoPerfil,
                            nomeAutor = nome,
                            rm = rm,
                            cpsID = cpsID,
                            //apelidoAutor = apelido,
                            textoPostagem = texto,
                            imagensPost = imagensPostagem,
                            tituloPost = titulo,
                            turmasMarcadas = turmas,
                            idPostagem = idPost,
                            curtidas = Curtidas,
                            comentarios = Comentarios,
                        )

                        postagensData.add(postagemData) //O conteúdo da postagem está todo aqui
                        println("O id do post é $idPost")
                    }
                    postagensOrdenadas.clear()
                    postagensOrdenadas.addAll(postagensData)
                }
                .addOnFailureListener{erro ->
                    println("Não foi possivel coletar os dados $erro")
                }

            pubState.value = true
        }
    }


    if (!pubState.value){
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(255, 255, 255, 163))
        ){
            val (circularProgress, logo, text) = createRefs()
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

            Text(
                text = "Carregando as suas publicações...",
                fontSize = 30.sp,
                fontFamily = Jomhuria,
                modifier = Modifier
                    .constrainAs(text) {
                        start.linkTo(parent.start)
                        top.linkTo(logo.bottom, margin = 15.dp)
                        end.linkTo(parent.end)
                    }
            )
        }
    }else{
        var abrirFoto by remember{ mutableStateOf(false) }
        var expandirCard by remember{ mutableStateOf(false) }
        var caminhoImagem by remember { mutableStateOf("") }
        var postagemReferencia by remember { mutableStateOf("") }
        var quantidadePostagens = remember{ mutableStateOf(0) }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            //ConstraintLayout para o que precisar ser posicionado melhor
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                //Fundo da index
                loadImage(
                    path = "https://raw.githubusercontent.com/jonatas1096/Projeto/master/app/src/main/res/drawable/fundo_index.png",
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                )

                val (card, postagens) = createRefs()

                //Topo para voltar
                Card(
                    modifier = Modifier
                        .constrainAs(card) {
                            //start.linkTo(parent.start)
                            top.linkTo(parent.top)
                        }
                        .fillMaxWidth(),
                    backgroundColor = Color.White,
                    elevation = 8.dp
                ){
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier/*.fillMaxWidth()*/.padding(top = 7.dp)) {
                            arrowVoltar(
                                onClick = {
                                    navController.navigate("Index")
                                },
                                color = Color.Black,
                                modifier = Modifier
                                    .size(30.dp)
                            )
                        }

                        Row(modifier = Modifier.padding(start = 5.dp)) {
                            Text(
                                text = "Home", fontSize = 33.sp, fontFamily = Jomhuria,
                                modifier = Modifier.clickable {
                                    navController.navigate("Index")
                                }
                            )
                        }
                    }


                }


                //A lógica da área das postagens aqui
                Column(
                    modifier = Modifier.constrainAs(postagens){
                        top.linkTo(card.bottom, margin = 15.dp)
                    }
                ) {
                        MinhasPostagens(
                            postagens = postagensOrdenadas,
                            abrirFoto = { resultado ->
                                caminhoImagem = resultado
                                abrirFoto = !abrirFoto
                            },
                            expandir = {resultado ->
                                expandirCard = resultado
                            },
                            postRef = {postagemRef ->
                                postagemReferencia = postagemRef
                                println("post referencia $postagemReferencia")
                            },
                            postsBack = {contagem ->
                                quantidadePostagens.value = contagem
                            }
                        )

                        if (quantidadePostagens.value == 0){
                            Column(modifier = Modifier.padding(horizontal = 12.dp).padding(top = 14.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                                val nome = UserData.nomeEncontrado

                                Text(
                                    text = "$nome, você não publicou nada ainda 😓",
                                    fontSize = 25.sp, color = Color.Black, fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Eu estou pronto para publicar alguma coisa!",
                                    fontSize = 20.sp, color = LARANJA, fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .padding(top = 16.dp)
                                        .clickable { navController.navigate("Publicar") }
                                )
                            }

                        }
                }
            }
        }

        if (abrirFoto){
            ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (cardBackground, fotoPerfilPub, fecharFoto) = createRefs()

            //Essa primeira box é só para desfocar e adc. o click pra sair externo
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0, 0, 0, 136)) //Desfoque
                    .clickable {
                        abrirFoto = !abrirFoto
                    }
            ) {}

            Card(
                modifier = Modifier
                    .constrainAs(cardBackground) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom, margin = 140.dp)
                    }
                    .size(330.dp),
                backgroundColor = Color.Black
            ) {}

            //A foto em si (já maximizada).
            Box(
                modifier = Modifier
                    .constrainAs(fotoPerfilPub) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom, margin = 140.dp)
                    }
                    .size(320.dp)
            ) {
                loadImage(
                    path = caminhoImagem,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                )
            }
            Text(
                text = "Fechar",
                fontSize = 34.sp,
                fontFamily = Dongle,
                color = Color.White,
                lineHeight = (15).sp,
                modifier = Modifier
                    .constrainAs(fecharFoto) {
                        top.linkTo(fotoPerfilPub.bottom, margin = 5.dp)
                        end.linkTo(fotoPerfilPub.end, margin = 5.dp)
                    }
                    .clickable {
                        abrirFoto = !abrirFoto
                    },
            )

            }
        }


        //Abrir os comentários
        if (expandirCard){
            //Pequena lógica para saber quem está tentando comentar
            var nomeUsuario = UserData.nomeEncontrado
            var apelido = remember{ mutableStateOf("") }
            var urlFoto = remember{ mutableStateOf("") }
            if (!UserData.apelidoUsuario.isNullOrEmpty()){
                apelido.value = UserData.apelidoUsuario
            }
            if (!UserData.imagemUrl.isNullOrEmpty()){
                urlFoto.value = UserData.imagemUrl
            }

            println("dentro do if $postagemReferencia")
            layoutComentarios(
                expandirCard,
                dropCard = {expandir->
                    expandirCard = expandir
                },
                postagemID = postagemReferencia,
                nome = nomeUsuario,
                apelido = apelido.value,
                fotoPerfil = urlFoto.value,
            )
        }
    }



}