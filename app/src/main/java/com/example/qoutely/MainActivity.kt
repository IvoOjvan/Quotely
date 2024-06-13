package com.example.qoutely

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.qoutely.bottomNavigation.MyBottomAppBar
import com.example.qoutely.ui.theme.QoutelyTheme
import com.example.qoutely.view.HomeScreen
import com.example.qoutely.view.LoginScreen
import com.example.qoutely.view.RegisterScreen
import com.example.qoutely.viewmodel.LoginViewModel
import com.example.qoutely.viewmodel.RegisterViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QoutelyTheme(darkTheme = false) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "login_screen"
                    ){
                        composable("login_screen"){
                            //BackgroundImage(modifier = Modifier)
                            LoginScreen(viewModel = remember {
                                LoginViewModel(navController)
                            })
                        }
                        composable("register_screen"){
                            RegisterScreen(viewModel = remember {
                                RegisterViewModel(navController)
                            })
                        }
                        composable("home_screen"){
                            MyBottomAppBar(navController)
                        }
                    }
                    //LoginScreen(LoginViewModel())
                    //RegisterScreen(viewModel = RegisterViewModel())


                }
            }
        }
    }
}

