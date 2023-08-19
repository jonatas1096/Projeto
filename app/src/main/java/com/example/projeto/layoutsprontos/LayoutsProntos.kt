package com.example.projeto.layoutsprontos


import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.projeto.ui.theme.Dongle
import com.example.projeto.ui.theme.LARANJA


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
        shape = RoundedCornerShape(50.dp),
        visualTransformation = visualTransformation,
        leadingIcon = leadingIcon,
        modifier = Modifier
            .padding(horizontal = 40.dp)
            .padding(bottom = 7.dp)
    )

}

