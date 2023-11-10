package com.connectstudent.projeto.layoutsprontos


import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.runtime.Composable
import androidx.constraintlayout.compose.ConstraintLayout
import com.connectstudent.projeto.datasource.UserData
import com.connectstudent.projeto.ui.theme.Dongle
import com.connectstudent.projeto.ui.theme.Jomhuria
import com.connectstudent.projeto.ui.theme.LARANJA
import com.connectstudent.projeto.R
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("SuspiciousIndentation")
@Composable
fun Postagem(fotoPerfil:(String?) -> Unit, nomeAutor:String, rm:String, cpsID: String, /*apelidoAutor:String,*/ textoPostagem:String, imagensPost: List<String>, tituloAutor:String, turmasMarcadas: List<String>,
             idPostagem:String, numerocurtidas:Int, numerocomentarios:Int, postagemRef: (String) -> Unit, expandir: (Boolean) -> Unit , abrirFotoPerfil:(Boolean) -> Unit) {

    val iconecurtir = painterResource(id = R.drawable.ic_curtir)
    val iconecurtido = painterResource(id = R.drawable.ic_curtido)
    val iconecomentarios = painterResource(id = R.drawable.ic_comentarios)


    val maxCaracteresNome = 18
    val maxCaracteresApelido = 16

    //Abrir a imagem de perfil (pub)
    var imagemState by remember{ mutableStateOf(false) }
    //Lógica da curtida
    var curtirState by remember{ mutableStateOf(false) }
    var numeroCurtidas by remember { mutableStateOf(numerocurtidas) }
    //Número de comentários
    var numeroComentarios by remember { mutableStateOf(numerocomentarios) }
    //RM do usuario (também pertence à lógica da curtida)
    var usuarioCurtiu by remember{ mutableStateOf(false) }

    //As fotos (reações nas curtidas)
    var primeiraFotinha = remember { mutableStateOf<String?>("") }
    var segundaFotinha = remember { mutableStateOf<String?>("") }
    var fotosBaixadas by remember { mutableStateOf(false) }
    var fotinhasState by remember{ mutableStateOf(false) }
    //



    var comentariosState by remember { mutableStateOf(false) }

    //scope
    val scope = rememberCoroutineScope()
    //Storage
    val storage = Firebase.storage
    //Firestore
    val firestore = Firebase.firestore
    //O link da imagem do usuario
    var fotoUsuario = remember { mutableStateOf<String?>("") }
    //Apelido
    var apelidoAutor = remember { mutableStateOf<String?>("") }

    //Foto do usuario em tempo real -- apelido também
    LaunchedEffect(Unit){
        scope.launch{
            if (!rm.isNullOrEmpty()){ //nao está vazio.
                val storageRef = storage.reference.child("Alunos/Fotos de Perfil/$rm")
                storageRef.downloadUrl
                    .addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()
                        fotoUsuario.value = imageUrl

                        //Atualizando no firestore
                        // Agora vou subir o resultado disso (a URL) para o Firebase Firestore também:
                        val alunoDocument = firestore.collection("Alunos").document(rm)

                        val alunoFoto = hashMapOf(
                            "fotoURL" to fotoUsuario.value,
                            "ultimaAtualizacao" to FieldValue.serverTimestamp() // Adiciona a data/hora da atualização
                        )

                        alunoDocument.set(alunoFoto, SetOptions.merge())
                            .addOnSuccessListener {
                                println("uma imagem chegou na index, vamos mandar tambem para o firestore.")
                            }
                            .addOnFailureListener { exception ->
                                println("uma imagem chegou na index, mas obtemos um erro $exception.")
                            }
                    }
            }else{
                val storageRef = storage.reference.child("CPS/Fotos de Perfil/$cpsID")
                storageRef.downloadUrl
                    .addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()
                        fotoUsuario.value = imageUrl
                    }
                    .addOnFailureListener { exception ->
                        println("Erro ao obter o URL da foto de perfil: $exception")
                    }
            }

        }

        //Apelido
        scope.launch {
           if (!rm.isNullOrEmpty()){

               val alunoDocument = firestore.collection("Alunos").document(rm)
               alunoDocument.get()
                   .addOnSuccessListener {result ->
                       val apelido = result.getString("apelido")
                       if (!apelido.isNullOrEmpty()){
                           apelidoAutor.value = apelido
                       }
                   }
                   .addOnFailureListener { e ->
                       println("o documento nao existe")
                   }
           }else{
               val cpsDocument = firestore.collection("Cps").document(cpsID)
               cpsDocument.get()
                   .addOnSuccessListener {result ->
                       val apelido = result.getString("apelido")
                       if (!apelido.isNullOrEmpty()){
                           apelidoAutor.value = apelido
                       }
                   }
                   .addOnFailureListener { e ->
                       println("o documento nao existe")
                   }
           }
        }
    }


    checarEstado(
        idPost = idPostagem,
        estado = {estadoCurtida ->
            usuarioCurtiu = estadoCurtida
        })

    downloadFotosReacao(
        idPost = idPostagem,
        rm = rm,
        cpsID = cpsID,
        primeiraFoto = {fotoBaixada ->
            primeiraFotinha.value = fotoBaixada
        },
        segundaFoto = {fotoBaixada ->
            segundaFotinha.value = fotoBaixada
        },
        fotosBaixadas = {downloadState ->
            fotosBaixadas = downloadState
        }
    )



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
                .clickable {
                    imagemState = !imagemState
                    fotoPerfil(fotoUsuario.value)
                    abrirFotoPerfil(imagemState)
                }
        ) {
            fotoUsuario.value?.let {
                loadImage(
                    path = it,
                    contentDescription = "Foto",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                )
            }
            if (fotoUsuario.value.isNullOrEmpty()) {
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
                        .padding(horizontal = 12.dp)
                        .padding(top = 4.dp),
                ) {
                    //Nome do usuário
                    if (nomeAutor.length > maxCaracteresNome) {
                        Text(
                            text = nomeAutor.substring(0, maxCaracteresNome) + "..",
                            color = if (rm in setOf("23627", "15723", "23620", "23619", "12345")) {
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
                            color = if (rm in setOf("23627", "15723", "23620", "23619", "12345")) {
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
                    if (!apelidoAutor.value.isNullOrEmpty()) { // " ! " de negação, ou seja, não está vazio ou nullo.
                        if (apelidoAutor.value!!.length > 16) {
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
                                text = "(${apelidoAutor.value})",
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
                        Column(modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy((-8).dp)
                        ) {
                            textoPostagem.replace("[\\uD83C-\\uDBFF\\uDC00-\\uDFFF]+".toRegex(), "")
                            Text(
                                text = textoPostagem.substring(
                                    0,
                                    maxCaracteresTexto.value
                                ) + "... ",
                                fontSize = 29.sp,
                                color = Color(39, 39, 39, 255),
                                fontFamily = Dongle,
                                lineHeight = (17).sp,
                            )
                            if (maxCaracteresTexto.value < textoPostagem.length) {
                                Text(
                                    text = "<Ver mais>",
                                    fontSize = 18.sp,
                                    color = LARANJA,
                                    modifier = Modifier
                                        .clickable {
                                            maxCaracteresTexto.value = textoPostagem.length
                                        }
                                        .padding(bottom = 8.dp)
                                )
                            }
                        }

                    } else {
                        val textoSemEmojis = removeEmojis(textoPostagem)
                        Text(
                            text = textoSemEmojis,
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
                //Parte para saber se o usuario ja curtiu o post ou não
                if (usuarioCurtiu){
                    Icon(
                        painter = iconecurtido,
                        contentDescription = "Icone para post já curtido",
                        modifier = Modifier
                            .size(28.dp)
                    )
                    Text(
                        text = "$numeroCurtidas",
                        fontSize = 36.sp,
                        fontFamily = Dongle,
                    )
                }else{
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

        }

        if (fotosBaixadas){ //Ainda está baixando as imagens
            //Fotinha
            Box(
                modifier = Modifier
                    .constrainAs(fotoReacao) {
                        start.linkTo(curtir.end, margin = 6.dp)
                        top.linkTo(boxPostagem.bottom, margin = 5.dp)
                    }
                    .size(35.dp)
                    .clip(CircleShape)

            ) {
                loadImage(
                    path = "${primeiraFotinha.value}",
                    contentDescription = "Foto Reação 1",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                )
            }

            //Fotinha2
            Box(
                modifier = Modifier
                    .constrainAs(fotoReacao2) {
                        start.linkTo(fotoReacao.end, margin = (-11).dp)
                        top.linkTo(boxPostagem.bottom, margin = 5.dp)
                    }
                    .size(35.dp)
                    .clip(CircleShape)
            ) {
                loadImage(
                    path = "${segundaFotinha.value}",
                    contentDescription = "Foto Reação 2",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                )
            }
        }

        /////////////////////


        //Parte dos Comentários
        IconButton(
            onClick = {
                postagemRef(idPostagem)
                comentariosState = !comentariosState
                expandir(comentariosState)
            },
            modifier = Modifier
                .constrainAs(comentar) {
                    start.linkTo(curtir.end, margin = 90.dp)
                    top.linkTo(boxPostagem.bottom)
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
                    text = "$numeroComentarios",
                    fontSize = 32.sp,
                    fontFamily = Dongle,
                    modifier = Modifier
                        .clickable {
                            postagemRef(idPostagem)
                            comentariosState = !comentariosState
                            expandir(comentariosState)
                        }
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


    //Curtir a pub
    if (curtirState){
        curtirPublicacao(idPostagem, onCurtir = {novoNumeroCurtidas ->
            numeroCurtidas = novoNumeroCurtidas
            curtirState = false
            println("atualizando para $numeroCurtidas")
        },
            mudarIcone = {mudarIcone->
                usuarioCurtiu = mudarIcone
            }
        )

    }

    //Enviar as fotos de reações
    if (fotinhasState){
        downloadFotosReacao(
            idPost = idPostagem,
            rm = rm,
            cpsID = cpsID,
            primeiraFoto = {fotoBaixada ->
                primeiraFotinha.value = fotoBaixada
            },
            segundaFoto = {fotoBaixada ->
                segundaFotinha.value = fotoBaixada
            },
            fotosBaixadas = {downloadState ->
                fotosBaixadas = downloadState
            }
        )
    }

}

fun removeEmojis(text: String): String {
    val builder = StringBuilder()
    text.forEach { char ->
        if (!Character.isSurrogate(char)) {
            builder.append(char)
        }
    }
    return builder.toString()
}
@Composable
fun curtirPublicacao(idPostagem:String, onCurtir: (Int) -> Unit,  mudarIcone: (Boolean) -> Unit) {

    val firestore = Firebase.firestore // Instância do firebase

    //RM do usuario para marcar a curtida dele no array
    val rmUsuario = UserData.rmEncontrado
    val cpsID = UserData.cpsIDEncontrado
    var identificacaoUsuario = ""

    if (!rmUsuario.isNullOrEmpty()){ //" ! " para negar, ou seja, nao está vazio.
        identificacaoUsuario = rmUsuario
    }else{
        identificacaoUsuario = cpsID
    }

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

                        if (!usuariosCurtidas.contains(identificacaoUsuario)) { //" ! " para negar, ou seja, nao contem.
                            println("nao contem $identificacaoUsuario, entao vamos adicionar.")
                            usuariosCurtidas.add(identificacaoUsuario)
                            val usuarioCurtiu = true
                            mudarIcone(usuarioCurtiu)
                        } else {
                            println("contem $identificacaoUsuario, entao vamos remover.")
                            usuariosCurtidas.remove(identificacaoUsuario)
                            val usuarioCurtiu = false
                            mudarIcone(usuarioCurtiu)
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
                    } else {//Nao existe o array, entao vamos criar e adicionar o primeiro usuario.
                        val usuariosCurtidas = ArrayList<String>() //criamos o array
                        usuariosCurtidas.add(identificacaoUsuario)
                        val usuarioCurtiu = true
                        mudarIcone(usuarioCurtiu)

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
                                onCurtir(curtidasConversao)
                            }
                            .addOnFailureListener {
                                println("Erro ao curtir: $it")
                            }
                    } else { //não existe, vamos criar
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

@Composable
fun downloadFotosReacao(idPost:String, rm:String, cpsID:String,primeiraFoto: (String) -> Unit,segundaFoto: (String) -> Unit, fotosBaixadas: (Boolean) -> Unit ){

    val firestore = Firebase.firestore // Instância do firebase
    var usuarioCurtiu by remember{ mutableStateOf(false) }
    val storage = Firebase.storage
    val storageRef = storage.reference
    var primeiraReacao by remember { mutableStateOf<String?>("") }
    var segundaReacao by remember { mutableStateOf<String?>("") }
    var fotoBaixada by remember { mutableStateOf(false) }

    val idPost = idPost
    val postagensCollection = firestore.collection("Postagens")
    postagensCollection.whereEqualTo("idPost", idPost)
        .get()
        .addOnSuccessListener { postagens ->
            if (!postagens.isEmpty) { // Verifica se a coleção não está vazia
                val postagemEncontrada = postagens.documents[0]
                println("o post existe, é $postagemEncontrada")
                val usuariosCurtidas = postagemEncontrada.get("usuariosCurtidas") as? ArrayList<String> ?: ArrayList()
                if (usuariosCurtidas.contains(rm) || usuariosCurtidas.contains(cpsID)){
                    usuarioCurtiu = true //o estado é verdadeiro, o usuario curtiu o post
                }
                //Recuperar as fotos como reações
                println("vai iniciar a validação")
                if (usuariosCurtidas.size >= 1){
                    println("entrou no if")
                    val primeiroindice = usuariosCurtidas[0]

                    //Como eu não sei se a primeira foto vai ser de um Aluno ou CPS, vou ter que procurar nas duas pastas.
                    val pastaAlunos = storageRef.child("Alunos/Fotos de Perfil")
                    val pastaCPS = storageRef.child("CPS/Fotos de Perfil")

                    //Começando pela primeira pessoa que curtiu o post
                    pastaAlunos.listAll() //Listando todas as fotos que existe na pasta dos Alunos
                        .addOnSuccessListener {result->
                            for (item in result.items){
                                val caminhoFoto = item.path
                                if (caminhoFoto.endsWith(primeiroindice)){  //Se a foto for igual ao valor da curtida, eu vou baixar a url dela
                                    item.downloadUrl.addOnSuccessListener { uri ->
                                        primeiraReacao = uri.toString()
                                        primeiraFoto(primeiraReacao!!)
                                    }
                                }
                            }
                        }
                    //Procurando na pasta do CPS
                    pastaCPS.listAll()
                        .addOnSuccessListener {result->
                            for (item in result.items){
                                val caminhoFoto = item.path
                                if (caminhoFoto.endsWith(primeiroindice)){  //Se a foto for igual ao valor da curtida, eu vou baixar a url dela
                                    item.downloadUrl.addOnSuccessListener { uri ->
                                        primeiraReacao = uri.toString()
                                        primeiraFoto(primeiraReacao!!)
                                    }

                                }
                            }
                        }
                }

                if (usuariosCurtidas.size >= 2){
                    val segundoindice = usuariosCurtidas[1]
                    val pastaAlunos = storageRef.child("Alunos/Fotos de Perfil")
                    val pastaCPS = storageRef.child("CPS/Fotos de Perfil")

                    //Começando pela primeira pessoa que curtiu o post
                    pastaAlunos.listAll() //Listando todas as fotos que existe na pasta dos Alunos
                        .addOnSuccessListener {result->
                            for (item in result.items){
                                val caminhoFoto = item.path
                                if (caminhoFoto.endsWith(segundoindice)){  //Se a foto for igual ao valor da curtida, eu vou baixar a url dela
                                    item.downloadUrl.addOnSuccessListener { uri ->
                                        segundaReacao= uri.toString()
                                        segundaFoto(segundaReacao!!)
                                    }

                                }
                            }
                        }
                    //Procurando na pasta do CPS
                    pastaCPS.listAll()
                        .addOnSuccessListener {result->
                            for (item in result.items){
                                val caminhoFoto = item.path
                                if (caminhoFoto.endsWith(segundoindice)){  //Se a foto for igual ao valor da curtida, eu vou baixar a url dela
                                    item.downloadUrl.addOnSuccessListener { uri ->
                                        segundaReacao= uri.toString()
                                        segundaFoto(segundaReacao!!)
                                    }

                                }
                            }
                        }
                }
                fotoBaixada = true
                fotosBaixadas(fotoBaixada)
            } else {
                println("A coleção de documentos está vazia.")
            }

        }
}

//Essa função aqui vai servir para saber se o usuario ja curtiu a publicacao ou nao, isso quando a index iniciar.
@Composable
fun checarEstado(idPost:String, estado: (Boolean) -> Unit){

    val firestore = Firebase.firestore // Instância do firebase
    var usuarioCurtiu by remember{ mutableStateOf(false) }
    val rm = UserData.rmEncontrado
    val cpsID = UserData.cpsIDEncontrado
    println("aqui no checar estado RM $rm, id $cpsID")

    val idPost = idPost
    val postagensCollection = firestore.collection("Postagens")
    postagensCollection.whereEqualTo("idPost", idPost)
        .get()
        .addOnSuccessListener { postagens ->
            if (!postagens.isEmpty) { // Verifica se a coleção não está vazia
                val postagemEncontrada = postagens.documents[0]
                val usuariosCurtidas =
                    postagemEncontrada.get("usuariosCurtidas") as? ArrayList<String> ?: ArrayList()
                if (usuariosCurtidas.contains(rm) || usuariosCurtidas.contains(cpsID)) {
                    usuarioCurtiu = true
                    estado(usuarioCurtiu)
                }
            }
            else{
                usuarioCurtiu = false
                estado(usuarioCurtiu)
            }
        }
}
