package com.connectstudent.projeto.layoutsprontos


import android.app.AlertDialog
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.connectstudent.projeto.ui.theme.Dongle
import com.connectstudent.projeto.ui.theme.LARANJA
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import com.connectstudent.projeto.datasource.UserData
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.connectstudent.projeto.bottomNavigation.BottomNavItem
import com.connectstudent.projeto.ui.theme.AZULCLARO
import com.connectstudent.projeto.R
import com.connectstudent.projeto.datasource.ComentariosData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


//Carregar uma imagem do github:
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun loadImage(path:String, contentDescription:String, contentScale: ContentScale, modifier: Modifier){
    GlideImage(
        model = path,
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier
    )

}


//TESTE COIL:
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun loadCoil(imagensPost: List<String>, contentDescription:String){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // HorizontalPager para exibir imagens individuais
        val pagerState = rememberPagerState()
        HorizontalPager(
            state = pagerState,
            pageCount = imagensPost.size,
        ) { pageIndex ->
            val imagemUrl = imagensPost[pageIndex]
            // Carrega e exibe a imagem usando o Coil ou Glide aqui
            AsyncImage(
                model = imagemUrl, // Passa uma única URL de imagem
                contentDescription = contentDescription,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

}


//O outlined do email
@Composable
fun OutlinedEmail(value:String, onValueChange: (String) -> Unit, label:String, keyboardOptions: KeyboardOptions, visualTransformation: VisualTransformation, leadingIcon: @Composable (() -> Unit)? = null){

    OutlinedTextField(
        value = value,
        onValueChange,
        label = {
            Text(text = label,
                fontFamily = Dongle,
                fontSize = 27.sp,
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = Color.White,
            focusedBorderColor = LARANJA,
            focusedLabelColor = LARANJA,
            backgroundColor = Color(0xFFF2f2f2),
            cursorColor = LARANJA,
        ),
        keyboardOptions = keyboardOptions,
        maxLines = 1,
        singleLine = true,
        shape = RoundedCornerShape(50.dp),
        visualTransformation = visualTransformation,
        leadingIcon = leadingIcon,
        modifier = Modifier
            .padding(horizontal = 40.dp)
            .padding(bottom = 7.dp)
    )

}

@Composable
fun OutlinedSenha(value:String, onValueChange: (String) -> Unit, label:String, keyboardOptions: KeyboardOptions, leadingIcon: @Composable (() -> Unit)? = null){

    var senhaVisibilidade by remember { mutableStateOf(false) }
    println("aqui começou $senhaVisibilidade")


    OutlinedTextField(
        value = value,
        onValueChange,
        label = {
            Text(text = label,
                fontFamily = Dongle,
                fontSize = 27.sp,
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = Color.White,
            focusedBorderColor = LARANJA,
            focusedLabelColor = LARANJA,
            backgroundColor = Color(0xFFF2f2f2),
            cursorColor = LARANJA,
        ),
        keyboardOptions = keyboardOptions,
        maxLines = 1,
        singleLine = true,
        shape = RoundedCornerShape(50.dp),
        visualTransformation =
        if (senhaVisibilidade){
            VisualTransformation.None
        }else
            PasswordVisualTransformation()
        ,
        leadingIcon = leadingIcon,
        trailingIcon = {
            if (senhaVisibilidade){
                IconButton(onClick = {
                    senhaVisibilidade = !senhaVisibilidade
                    println(senhaVisibilidade)
                }) {
                    Icon(
                        painterResource(id = R.drawable.ic_visibility),
                        contentDescription = "Ícone para mostrar ou ocultar a senha",
                        modifier = Modifier.size(22.dp))
                }
            }
            else{
                IconButton(onClick = {
                    senhaVisibilidade = !senhaVisibilidade
                    println(senhaVisibilidade)
                }) {
                    Icon(
                        painterResource(id = R.drawable.ic_visibilityoff),
                        contentDescription = "Ícone para mostrar ou ocultar a senha",
                        modifier = Modifier.size(22.dp))
                }
            }
        },
        modifier = Modifier
            .padding(horizontal = 40.dp)
            .padding(bottom = 7.dp)
    )

}

@Composable
fun OutlinedRegistro(value: String, onValueChange: (String) -> Unit, label:String, keyboardOptions: KeyboardOptions,visualTransformation: VisualTransformation,leadingIcon: @Composable (() -> Unit)? = null){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(text = label,
                fontFamily = Dongle,
                fontSize = 35.sp,
            )
        },
        keyboardOptions = keyboardOptions,
        maxLines = 1,
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = Color(0xA1000000),
            focusedBorderColor = LARANJA,
            focusedLabelColor = LARANJA,
            backgroundColor = Color(0xFFFFFFFF),
            cursorColor = LARANJA,
        ),
        visualTransformation = visualTransformation,
        shape = RoundedCornerShape(50.dp),
        leadingIcon = leadingIcon,
        modifier = Modifier
            .padding(bottom = 0.dp)
            .padding(bottom = 5.dp)
            .padding(horizontal = 0.dp)
            .fillMaxWidth()
            .height(60.dp)

    )
}

@Composable
fun BotaoEscolha(onClick: () -> Unit, text:String, fontSize: TextUnit = 36.sp, imageVector: ImageVector, descricao:String, imageVector2: ImageVector, modifier: Modifier, spacerWidth: Dp){

    Button(onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFFF2f2f2),
        ),
        shape = RoundedCornerShape(25.dp),
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier

    ) {
        IconButton(
            onClick = onClick,

            ) {

            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = onClick,
                    // Ação ao clicar no ícone

                ) {
                    Image(
                        imageVector = imageVector,
                        contentDescription = descricao,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = text,
                    fontFamily = Dongle,
                    color = Color(0xFFF5E5E5E),
                    fontSize = fontSize,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 5.dp, start = (0).dp)
                )

                //Spacer para separar o nome do play:
                Spacer(modifier = Modifier
                    .width(spacerWidth)
                    .border(1.dp, Color.Red))


                IconButton(
                    onClick = onClick,
                    // Ação ao clicar no ícone
                ) {
                    Image(
                        imageVector = imageVector2,
                        contentDescription = "",
                        modifier = Modifier.size(20.dp)
                    )
                }

            }
        }
    }
}

@Composable
fun CheckBoxPersonalizada(onCheckedChange : (Boolean) -> Unit){
    var isChecked by remember { mutableStateOf(false) }

    Checkbox(
        checked = isChecked,
        onCheckedChange = {
            isChecked = it
            onCheckedChange(it)
        },
        colors = CheckboxDefaults.colors(
            checkedColor = LARANJA,
            uncheckedColor = Color.Gray,
        ),

        )
}

@Composable
fun BotaoRegistrar(onClick: () -> Unit, corBotao: Color, fontSize: TextUnit = 22.sp) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = corBotao
        ),
        shape = RoundedCornerShape(30.dp),
        modifier = Modifier.width(200.dp)
    ) {
        Text(text = "Registrar-se",
            color = Color.White,
            fontSize = fontSize)
    }
}

@Composable
fun TextDuasCores(color1: Color, color2:Color, texto1: String, texto2: String, fontSize: TextUnit, onclick: () -> Unit) {
    val text = buildAnnotatedString {
        withStyle(style = SpanStyle(color = color1)) {
            append(texto1)
        }
        withStyle(style = SpanStyle(color = color2)) {
            append(texto2)
        }
    }



    Text(
        text = text,
        fontSize = fontSize,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        modifier = Modifier
            .padding(top = 13.dp, end = 17.dp)
            .clickable(onClick = {
                onclick()

            })
    )
}


@Composable
fun AlertDialogPersonalizado(
    dialogo: MutableState<Boolean>,
    onDismissRequest: () -> Unit,
    cor: Color
) {
    val scrollState = rememberScrollState()

    if (dialogo.value) {

        Dialog(
            onDismissRequest = { onDismissRequest() },
            properties = DialogProperties(
                dismissOnClickOutside = true,
            ),
        ) {
            // Conteúdo
            //Esse card serve para nao bugar e ficar sem fundo
            Card(
                elevation = 5.dp,
                shape = RoundedCornerShape(16.dp),
                backgroundColor = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(460.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(scrollState)
                ) {
                    Text(text = "Termos e Condições",
                        fontSize = 37.sp,
                        fontFamily = Dongle,
                        color = cor,
                        fontWeight = FontWeight.Bold
                    )
                    //Linha apenas para estética
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(2.dp)
                            .background(color = Color(209, 209, 209, 255))
                    ) {}
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Bem-vindo à nossa rede social móvel para alunos e professores da [ETEC Zona Leste]. Estes termos e condições regem o uso do nosso aplicativo. Ao utilizá-lo, você concorda expressamente com os seguintes termos e condições:",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 18.dp)
                    )
                    //Privacidade
                    Text(text = "1. Privacidade:", fontSize = 16.sp, color = cor, modifier = Modifier.padding(bottom = 5.dp))
                    Text(text = "Respeitamos sua privacidade e comprometemo-nos a proteger seus dados pessoais. Para obter informações detalhadas sobre como os coletamos, usamos e protegemos suas informações pessoais, consulte algum dos desenvolvedores."
                        ,fontSize = 16.sp, modifier = Modifier.padding(bottom = 10.dp))
                    //Cadastro de usuário
                    Text(text = "2. Cadastro de usuário:", fontSize = 16.sp, color = cor, modifier = Modifier.padding(bottom = 5.dp))
                    Text(text = "Para utilizar nosso aplicativo, você deve criar uma conta. Você é responsável por manter a confidencialidade de suas credenciais de login e por todas as atividades que ocorrerem em sua conta durante o uso."
                        ,fontSize = 16.sp, modifier = Modifier.padding(bottom = 10.dp))
                    //Uso Aceitável
                    Text(text = "3. Uso Aceitável:", fontSize = 16.sp, color = cor, modifier = Modifier.padding(bottom = 5.dp))
                    Text(text = "Você concorda em utilizar nosso aplicativo de maneira respeitosa e ética. Comportamentos inadequados como assédio, machismo, racismo ou qualquer outro tipo de discurso de ódio que viole os direitos de terceiros, não será tolerado."
                        ,fontSize = 16.sp, modifier = Modifier.padding(bottom = 10.dp))
                    //Propriedade Intelectual:
                    Text(text = "4. Propriedade Intelectual:", fontSize = 16.sp, color = cor, modifier = Modifier.padding(bottom = 5.dp))
                    Text(text = "Todo o conteúdo gerado pelos usuários como postagens, fotos, vídeos ou comentários, pertence aos respectivos criadores. Você não tem permissão para usar esse conteúdo sem a devida autorização."
                        ,fontSize = 16.sp, modifier = Modifier.padding(bottom = 10.dp))
                    //Responsabilidade:
                    Text(text = "5. Responsabilidade:", fontSize = 16.sp, color = cor, modifier = Modifier.padding(bottom = 5.dp))
                    Text(text = "Você reconhece que os desenvolvedores não são responsáveis por qualquer dano, perda, inconveniência ou prejuízo causado pelo uso de nosso aplicativo. Utilize-o por sua conta e risco."
                        ,fontSize = 16.sp, modifier = Modifier.padding(bottom = 10.dp))
                    //Encerramento de Conta:
                    Text(text = "6. Encerramento de conta:", fontSize = 16.sp, color = cor, modifier = Modifier.padding(bottom = 5.dp))
                    Text(text = "Você pode encerrar sua conta a qualquer momento contactando qualquer um dos desenvolvedores. Isso resultará na exclusão permanente de seus dados, ou seja, não podemos recuperar informações de contas excluídas."
                        ,fontSize = 16.sp, modifier = Modifier.padding(bottom = 10.dp))
                    //Diretrizes de Conteúdo:
                    Text(text = "7. Diretrizes de Conteúdo:", fontSize = 16.sp, color = cor, modifier = Modifier.padding(bottom = 5.dp))
                    Text(text = "É estritamente proibido qualquer tipo de postagem que inclua conteúdo ilegal, como discurso de ódio, nudez, violência, etc. O usuário irá arcar com as consequências caso o mesmo ocorra."
                        ,fontSize = 16.sp, modifier = Modifier.padding(bottom = 10.dp))
                    //Rescisão de Serviço:
                    Text(text = "8. Rescisão de Serviço:", fontSize = 16.sp, color = cor, modifier = Modifier.padding(bottom = 5.dp))
                    Text(text = "Reservamos o direito para que os desenvolvedores possam encerrar ou modificar o serviço a qualquer momento, com ou sem aviso prévio."
                        ,fontSize = 16.sp, modifier = Modifier.padding(bottom = 10.dp))


                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "Entendido",
                        color = cor,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .clickable(
                                onClick = { onDismissRequest() }
                            )
                            .padding(start = 200.dp)
                    )
                }
            }
        }



    }
}

@Composable
fun BottomNavigationBar(items: List<BottomNavItem>, navController: NavController, modifier: Modifier = Modifier, onClickItem: (BottomNavItem) -> Unit, ModifierIcon : Modifier){

    val backStackEntry = navController.currentBackStackEntryAsState()

    BottomNavigation(
        modifier = modifier,
        backgroundColor = Color(0xFFFBF7F5),
        elevation = 20.dp
    ) {
        items.forEach{ item ->

            val selected = item.route == backStackEntry.value?.destination?.route
            BottomNavigationItem(
                selected = selected,
                onClick = {onClickItem(item)},
                selectedContentColor = AZULCLARO,
                unselectedContentColor = Color(0xFF383838),
                icon = {
                    Column(
                        horizontalAlignment = CenterHorizontally
                    ) {
                        if (item.badgeCount > 0){
                            BadgedBox(
                                badge = {
                                    Surface(
                                        color = AZULCLARO,
                                        shape = CircleShape,
                                        modifier = Modifier
                                            .size(20.dp)
                                    ) {
                                        Column(
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = CenterHorizontally,
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            if (item.badgeCount > 99){
                                                Text(text = "99+",
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Bold,
                                                    textAlign = TextAlign.Center,
                                                    fontSize = 11.sp

                                                )
                                            }
                                            else{
                                                Text(text = item.badgeCount.toString(),
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Bold,
                                                    textAlign = TextAlign.Center,
                                                    fontSize = 11.sp

                                                )
                                            }

                                        }

                                    }

                                }
                            ) {
                                Icon(imageVector = item.icon,
                                    contentDescription = item.nome,
                                    modifier = item.iconModifier ?: Modifier
                                )
                            }
                        }
                        else{
                            Icon(imageVector = item.icon,
                                contentDescription = item.nome,
                                modifier = item.iconModifier ?: Modifier
                            )
                        }
                        if (selected){
                            Text(text = item.nome,
                                textAlign = TextAlign.Center,
                                fontSize = 10.sp
                            )
                        }
                    }

                }
            )
        }
    }

}


@Composable
fun drawerPersonalizado(urlbaixada:Boolean, navController: NavController){

    val context = LocalContext.current
    var dialogo = remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFF16202a))
        .padding(25.dp, 60.dp, 10.dp, 30.dp),
        verticalArrangement = Arrangement.spacedBy((-10).dp)
    ) {

        //Surface para a foto de perfil ou foto padrão
        Surface(
            shape = CircleShape,
            border = BorderStroke(3.dp, Color.White),
            modifier = Modifier
                .size(120.dp)
                .clickable(onClick = {
                    navController.navigate("Profile")
                })
        ){
            if (urlbaixada){
                if (!UserData.imagemUrl.isNullOrEmpty()){ // " ! " para negação, ou seja, não está vazio
                    loadImage(
                        path = UserData.imagemUrl,
                        contentDescription = "Imagem de perfil do Usuário",
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

        //Validação para nome ou apelido do usuário (dependendo da ordem)
        if (!UserData.apelidoUsuario.isNullOrEmpty()){// na negativa "!", ou seja, nao está vazio ou nullo.
            Text(
                text = UserData.apelidoUsuario,
                fontFamily = Dongle,
                color = Color.White,
                fontSize = 47.sp,
                modifier = Modifier.padding(top = 22.dp)
            )
            Text(
                text = UserData.nomeEncontrado,
                fontFamily = Dongle,
                color = Color(0xFF83898c),
                fontSize = 34.sp,
            )
        }
        else{
            Text(
                text = UserData.nomeEncontrado,
                fontFamily = Dongle,
                color = Color(0xFF83898c),
                fontSize = 30.sp,
                modifier = Modifier.padding(top = 22.dp)
            )
        }

        //RM ou cpsID
        if (!UserData.rmEncontrado.isNullOrEmpty()){// na negativa "!", ou seja, nao está vazio ou nullo.
            Text(
                text = "RM: ${UserData.rmEncontrado}",
                fontFamily = Dongle,
                color = Color(0xFF83898c),
                fontSize = 30.sp,
                modifier = Modifier.padding(bottom = 15.dp)
            )
        }else{
            Text(
                text = "cpsID: ${UserData.cpsIDEncontrado}",
                fontFamily = Dongle,
                color = Color(0xFF83898c),
                fontSize = 30.sp,
                modifier = Modifier.padding(bottom = 15.dp)
            )
        }


        //Linha estética
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .size(1.dp)
                .padding(end = 30.dp)
                .background(color = Color(107, 107, 107, 255))
        ){}


        //Parte das opções selecionáveis
        Text(
            text = "Minhas Publicações",
            fontFamily = Dongle,
            color = Color.White,
            fontSize = 34.sp,
            modifier = Modifier
                .padding(top = 25.dp)
                .clickable {
                    Toast
                        .makeText(context, "Em breve!", Toast.LENGTH_SHORT)
                        .show()
                }
        )

        Text(
            text = "Documentações",
            fontFamily = Dongle,
            color = Color.White,
            fontSize = 34.sp,
            modifier = Modifier
                .padding(top = 10.dp)
                .clickable {
                    Toast
                        .makeText(context, "Em breve!", Toast.LENGTH_SHORT)
                        .show()
                }
        )

        Text(
            text = "Secretaria",
            fontFamily = Dongle,
            color = Color.White,
            fontSize = 34.sp,
            modifier = Modifier
                .padding(top = 10.dp)
                .padding(bottom = 30.dp) //← TA FUNCIONANDO, caso queira aumentar mais o gap entre eles
                .clickable {
                    Toast
                        .makeText(context, "Em breve!", Toast.LENGTH_SHORT)
                        .show()
                }
        )

        //Linha estética
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .size(1.dp)
                .padding(end = 30.dp)
                .background(color = Color(107, 107, 107, 255))
        ){}

        Text(
            text = "Termos de confiança",
            fontFamily = Dongle,
            color = Color.White,
            fontSize = 34.sp,
            modifier = Modifier
                .padding(top = 10.dp)
                .clickable {
                    dialogo.value = true
                }
        )


        //Textbutton para deslogar da conta
        TextButton(
            onClick = {
                val alertDialog = AlertDialog.Builder(context)
                alertDialog.setTitle("Deslogar conta")
                alertDialog.setMessage("Deseja deslogar a conta do aplicativo?")
                alertDialog.setPositiveButton("Sim"){_,_ ->
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("Login")

                }
                alertDialog.setNegativeButton("Não"){_,_ ->

                }
                    .show()
            },
            modifier = Modifier.padding(top = 160.dp)
        ) {
            Text(
                text = "Sair",
                fontSize = 28.sp,
                //fontFamily = Dongle,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
        }
    }

    if (dialogo.value){
        AlertDialogPersonalizado(
            dialogo = dialogo,
            onDismissRequest = {
                dialogo.value = false
            },
            cor = LARANJA)
    }

}


@Composable
fun arrowVoltar(onClick: () -> Unit, modifier: Modifier = Modifier, color:Color){

    IconButton(
        onClick = onClick,
        modifier = modifier
    ){
        Image(ImageVector.vectorResource(id = R.drawable.ic_arrow),
            contentDescription = "Ícone para voltar de página",
            colorFilter = ColorFilter.tint(color)
        )
    }
}


@Composable
fun layoutComentarios(expandirCard:(Boolean), dropCard:(Boolean) -> Unit, postagemID:String, nome:String, apelido:String, fotoPerfil:String){

    val firestore = Firebase.firestore // Instância do firebase

    var expandir by remember { mutableStateOf(false) }
    var listaComentarios  by remember { mutableStateOf<List<ComentariosData>>(emptyList()) }

    comentariosLoad(
        idPost = postagemID,
        listaPreenchida = { resultado ->
            listaComentarios = resultado
        }
    )

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val (comentariosConstraint) = createRefs()
        var comentario by remember { mutableStateOf("") }
        val context = LocalContext.current

        val cardOffset = if (expandirCard) {
            IntOffset(0, 0) // A posição "final" do Card quando estiver expandido
        } else {
            IntOffset(0, -200) // A posição "inicial" do Card quando estiver recolhido
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0, 0, 0, 119)) //Desfoque
                .clickable {
                    dropCard(expandir)
                }
        ) {}

        Card(modifier = Modifier
            .constrainAs(comentariosConstraint) {
                bottom.linkTo(parent.bottom, margin = 1.dp)
            }
            .offset { cardOffset }
            .height(460.dp)
            .zIndex(1f)
            .fillMaxWidth(),
            backgroundColor = Color(252, 252, 252, 255)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 5.dp)
                    .padding(horizontal = 5.dp),
                horizontalAlignment = CenterHorizontally,
            ) {
                IconButton(
                    onClick = {
                        dropCard(expandir)
                    },
                    modifier = Modifier
                        .size(50.dp)
                )
                {
                    Image(ImageVector.vectorResource(id = R.drawable.ic_droparcard),
                        contentDescription = "Ícone para dropar o card",
                    )
                }
                Text(
                    text = "Comentários",
                    fontSize = 32.sp,
                    fontFamily = Dongle,

                    )
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .size(2.dp)
                    .background(color = Color(206, 202, 202, 255))
                ){}

                //Os comentários em si
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(310.dp)
                        .background(Color(240, 238, 238, 255))
                ) {
                    items(listaComentarios){ comentario->
                        boxComentario(
                            usuarioFoto = comentario.fotoPerfil,
                            comentario = comentario.comentario,
                            nome = comentario.nome,
                            apelido = comentario.apelido
                        )
                    }
                }

                //  Inserir o comentário
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                    ) {
                        loadImage(
                            path = UserData.imagemUrl,
                            contentDescription = "Foto comentário",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                        )
                    }
                    OutlinedTextField(
                        value = comentario,
                        onValueChange = {comentario = it},
                        shape = RoundedCornerShape(30.dp),
                        label = {
                            Text(
                                text = "O que deseja dizer?",
                                color = Color(197, 194, 194, 255),
                                fontSize = 29.sp,
                                fontFamily = Dongle
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    if (!comentario.isNullOrEmpty()){ // " ! " para negação, ou seja, não está vazio.
                                        //Parte da lógica para subir o comentário
                                        val postagemRef = firestore.collection("Postagens")
                                        postagemRef.whereEqualTo("idPost", postagemID)
                                            .get()
                                            .addOnSuccessListener {documentSnapshot ->
                                                if (!documentSnapshot.isEmpty) {
                                                    val postagemDoc = documentSnapshot.documents[0]
                                                    val comentariosArray = postagemDoc.get("comentarios") as? ArrayList<HashMap<String, String>> ?: arrayListOf()

                                                    val novoComentario = hashMapOf(
                                                        "nome" to nome,
                                                        "apelido" to apelido,
                                                        "fotoPerfil" to fotoPerfil,
                                                        "comentario" to comentario
                                                    )
                                                    comentariosArray.add(novoComentario)

                                                    // Atualiza o documento da postagem com o novo array de comentários
                                                    postagemDoc.reference.update("comentarios", comentariosArray)
                                                        .addOnSuccessListener {
                                                            println("Novo comentário adicionado com sucesso.")
                                                        }
                                                        .addOnFailureListener { e ->
                                                            println("Erro ao adicionar o comentário: $e")
                                                        }

                                                } else {
                                                    println("Nenhum documento encontrado com o ID da postagem.")
                                                }
                                            }
                                            .addOnFailureListener {
                                                println("Não foi possivel encontrar a coleção. $it")
                                            }
                                    }else{
                                        Toast.makeText(context,"Escreva algo antes de enviar!",Toast.LENGTH_SHORT).show()
                                    }
                                }) {
                                if (!comentario.isNullOrEmpty()){ // " ! " para negação, ou seja, não está vazio.
                                    Icon(
                                        painterResource(id = R.drawable.ic_enviarcomentario),
                                        contentDescription = "Ícone para enviar o comentário",
                                        modifier = Modifier.size(22.dp))

                                }
                                else{
                                    Icon(
                                        painterResource(id = R.drawable.ic_comentariovazio),
                                        contentDescription = "Ícone de comentário vazio",
                                        tint = Color(199, 194, 194, 255),
                                        modifier = Modifier.size(22.dp))
                                }

                            }
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            cursorColor = Color.Black,
                            focusedBorderColor = Color(162, 159, 159, 255),
                            focusedLabelColor = Color(162, 159, 159, 255)
                        ),
                        maxLines = 1,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 4.dp)

                    )
                }

            }


        }

    }
}


//Primeiro a função para obter os comentários do post em si:
@Composable
fun comentariosLoad(idPost:String, listaPreenchida:(List<ComentariosData>) -> Unit){

    val firestore = Firebase.firestore // Instância do firebase

    println("iniciou o comentariosLoad")
    //Primeiro puxamos os dados base de indetificação e o comentário que o usuário postou
    val postagensCollection = firestore.collection("Postagens")
    postagensCollection.whereEqualTo("idPost", idPost) //Buscando a postagem pelo ID dela
        .get()
        .addOnSuccessListener { postagens ->
            println("Encontramos a postagem")
            if (!postagens.isEmpty) { // Verifica se a coleção não está vazia
                println("Não está vazia")
                val postagemEncontrada = postagens.documents[0]
                val comentarios = postagemEncontrada.get("comentarios") as? ArrayList<Map<String, Any>> ?: ArrayList()

                println("recuperamos o comentario. $comentarios")
                val listaComentarios = mutableListOf<ComentariosData>() //Armazenar os dados

                comentarios.forEach { comentario ->
                    val apelido = comentario["apelido"] ?: ""
                    val textoComentario = comentario["comentario"] ?: ""
                    val fotoPerfil = comentario["fotoPerfil"] ?: ""
                    val nome = comentario["nome"] ?: ""

                    val comentarioBox = ComentariosData(
                        nome = nome as? String ?: "",
                        apelido = apelido as? String ?: "",
                        fotoPerfil = fotoPerfil as? String ?: "",
                        comentario = textoComentario as? String ?: ""
                    )

                    listaComentarios.add(comentarioBox)
                }



                println("Preparando o callback")
                listaPreenchida(listaComentarios)
            }
        }


}

//Agora o layout dos comentários:
@Composable
fun boxComentario(usuarioFoto: String, comentario:String, nome:String, apelido: String){

    Row(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .clickable {

                }
        ) {
            loadImage(
                path = usuarioFoto,
                contentDescription = "Foto de perfil do usuário que comentou",
                contentScale = ContentScale.Crop,
                modifier = Modifier)
        }
        Column() {
            Row() {
                Text(
                    text = nome,
                    fontSize = 17.sp,
                    fontFamily = Dongle,
                )
                Text(
                    text = apelido,
                    fontSize = 16.sp,
                    fontFamily = Dongle,
                )
            }


            Text(
                text = comentario,
                fontSize = 23.sp,
                fontFamily = Dongle,
            )
        }


    }
}
//Preview:
@Composable
@Preview(showBackground = true)
fun previewLayouts(){

}