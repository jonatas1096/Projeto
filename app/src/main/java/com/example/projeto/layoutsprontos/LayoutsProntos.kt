package com.example.projeto.layoutsprontos


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import com.example.projeto.R



//Carregar uma imagem do github:
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun loadImage(path:String, contentDescription:String, contentScale: ContentScale, modifier: Modifier){
    GlideImage(
    model = path,
    contentDescription = contentDescription,
    contentScale = contentScale,
    modifier = Modifier
    )

 }


@Composable
fun OutlinedLogin(value:String, onValueChange: (String) -> Unit, label:String, keyboardOptions: KeyboardOptions, visualTransformation: VisualTransformation, leadingIcon: @Composable (() -> Unit)? = null){

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
fun OutlinedRegistro(value: String, onValueChange: (String) -> Unit, label:String, keyboardOptions: KeyboardOptions,visualTransformation: VisualTransformation,leadingIcon: @Composable (() -> Unit)? = null){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(text = label,
                fontFamily = Dongle,
                fontSize = 35.sp,
                //modifier = Modifier.padding(top = 0.dp)
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
fun TextDuasCores(color1: Color, color2:Color, texto1: String, texto2: String) {
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
        fontSize = 13.sp,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        modifier = Modifier
            .padding(top = 13.dp, end = 17.dp)
            .clickable(onClick = {

            })
    )
}
