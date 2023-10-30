package com.connectstudent.projeto.bottomNavigation

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val nome: String,
    val route: String? = null,
    val icon: ImageVector,
    val badgeCount: Int,
    val iconModifier: Modifier? = null
)

fun BottomNavItem.withIconModifier(modifier: Modifier) = copy(iconModifier = modifier)
