package com.example.qoutely.bottomNavigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.qoutely.view.HomeScreen
import com.example.qoutely.view.SavedScreen
import com.example.qoutely.view.SearchScreen
import com.example.qoutely.viewmodel.HomeViewModel
import com.example.qoutely.viewmodel.SavedViewModel
import com.example.qoutely.viewmodel.SearchViewModel

@Composable
fun MyBottomAppBar(
    navController: NavController
){
    val navigationController = rememberNavController()
    var selected = remember {
        mutableStateOf(Icons.Default.Home)
    }
    val screens = listOf(
        Screens.Home,
        Screens.Search,
        Screens.Saved
    )

    Scaffold(
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(70.dp).background(Color.White)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().background(Color.White),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ){
                    screens.forEach { screen ->
                        AddItem(
                            screen = screen,
                            selected = selected,
                            navController = navigationController)
                    }
                }
            }
        }
    ) { paddingValues ->
            NavHost(
                navController = navigationController,
                startDestination = Screens.Home.screen,
                modifier = Modifier.background(Color.White).padding(paddingValues)
            ){
                composable(Screens.Home.screen){
                    HomeScreen(HomeViewModel(navController))
                }
                composable(Screens.Search.screen){
                    SearchScreen(SearchViewModel())
                }
                composable(Screens.Saved.screen){
                    SavedScreen(SavedViewModel())
                }
            }
    }
}

@Composable
fun RowScope.AddItem(
    screen: Screens,
    selected: MutableState<ImageVector>,
    navController: NavHostController
){

    Column(
        modifier = Modifier.background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        IconButton(
            onClick = {
                selected.value = screen.icon
                navController.navigate(screen.screen){
                    popUpTo(0)
                }
            },
            //modifier = Modifier.weight(1f)
        ) {
            Icon(
                screen.icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = if(selected.value == screen.icon) Color.Black else Color.LightGray
            )
        }
        Text(
            text = screen.title,
            fontSize = 10.sp,
            color = if(selected.value == screen.icon) Color.Black else Color.LightGray
        )
    }

}