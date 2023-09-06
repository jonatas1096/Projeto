package com.example.projeto.views

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Index(navController: NavController) {

    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {

        val (bottomBox) = createRefs()
        val bottomRectangle = createGuidelineFromBottom(0.1f) // Ajuste a porcentagem conforme necessário

        Box(
            modifier = Modifier
                .constrainAs(bottomBox) {
                    top.linkTo(bottomRectangle)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .background(Color.Black)
                .padding(16.dp) // Ajuste a margem conforme necessário
        ) {
            Text(text = "aaaaaa")
        }
    }

}
