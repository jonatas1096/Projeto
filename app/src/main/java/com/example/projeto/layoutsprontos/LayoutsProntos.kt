package com.example.projeto.layoutsprontos

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage



//Carregar uma imagem do github:
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun loadImage(path:String, contentDescription:String){
    GlideImage(model = path,
    contentDescription = contentDescription,
    contentScale = ContentScale.Crop,
     modifier = Modifier.fillMaxSize())
 }
