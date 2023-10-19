package com.example.projeto.layoutsprontos


import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.Icon
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.example.projeto.ui.theme.Dongle
import com.example.projeto.ui.theme.LARANJA
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.example.projeto.R
import com.example.projeto.bottomNavigation.BottomNavItem
import com.example.projeto.ui.theme.AZULCLARO
import com.google.firebase.auth.FirebaseAuth


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
                //.border(2.dp,Color.Red)
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
        backgroundColor = Color(0xFFFAFAFA),
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
fun botaoDrawer(onClick: () -> Unit){

    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(40.dp)
            .padding(start = 5.dp)
            .padding(top = 15.dp)
    ){
        Image(ImageVector.vectorResource(id = R.drawable.ic_drawermenu),
            contentDescription = "Publicar",)
    }
}


@Composable
fun drawerPersonalizado(navController: NavController){

    val context = LocalContext.current

    Column(modifier = Modifier
        .fillMaxSize()
        .border(2.dp, Color.Black)
        .zIndex(1F)) {
        Text(text = "Texto1")
        Text(text = "Texto2")
        Text(text = "Texto3")

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
        }) {
            Text(
                text = "Sair",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,

            )
        }
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




//Preview:
@Composable
@Preview(showBackground = true)
fun previewLayouts(){

}