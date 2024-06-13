package com.example.qoutely.bottomNavigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

/*
sealed class Screens (val screen: String){
    data object Home: Screens("home_screen")
    data object Search: Screens("search_screen")
    data object Saved: Screens("saved_screen")
}*/

sealed class Screens(
    val screen: String,
    val title: String,
    val icon: ImageVector
){
    object Home: Screens(
        screen = "home_screen",
        title = "Home",
        icon = Icons.Default.Home
    )
    object Search: Screens(
        screen = "searchScreen",
        title = "Search",
        icon = Icons.Default.Search
    )
    object Saved: Screens(
        screen = "saved_screen",
        title = "Favourites",
        icon = Icons.Default.Favorite
    )
}