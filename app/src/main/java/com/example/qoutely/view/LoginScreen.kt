package com.example.qoutely.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qoutely.R
import com.example.qoutely.ui.theme.InterFontFamily
import com.example.qoutely.viewmodel.LoginViewModel
import com.example.qoutely.viewmodel.RegisterViewModel
import com.example.qoutely.viewmodel.presentation.LoginFormEvent
import com.example.qoutely.viewmodel.presentation.RegistrationFormEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: LoginViewModel){
    val channelId = "MyTestChannel"
    val notificationId = 0
    val bigText = "Hello, we're glad " +
            "to see you back! Start exploring new quotes and " +
            "revisit your saved favorites. Let the inspiration begin!"
    val context = LocalContext.current

    /*LaunchedEffect(Unit) {
        createNotificationChannel(channelId, context)
    }*/

    LaunchedEffect(key1 = context) {
        createNotificationChannel(channelId, context)
        viewModel.validationEvents.collect{ event ->
            when(event){
                is LoginViewModel.ValidationEvent.Success -> {
                    viewModel.signInUser(
                        email = viewModel.state.email,
                        password = viewModel.state.password
                    )
                    /*showLargeTextNorification(
                        context,
                        channelId,
                        notificationId,
                        "Welcome to Quotely",
                        bigText
                    )*/
                }
                is LoginViewModel.ValidationEvent.Fail -> {
                    showLargeTextNorification(
                        context,
                        channelId,
                        notificationId,
                        "Oops wrong credentials",
                        "You have entered wrong credentials!"
                    )
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        LogoSection()
        EmailOutlinedTextField(viewModel = viewModel)
        Spacer(modifier = Modifier.height(16.dp))
        PasswordOutlinedTextField(viewModel = viewModel)
        Spacer(modifier = Modifier.height(16.dp))
        SignInButton(viewModel = viewModel)
        Spacer(modifier = Modifier.height(30.dp))
        NavigationSection(viewModel = viewModel)
    }
}

@Composable
fun LogoSection(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.5f)
            .clipToBounds()
            .padding(16.dp)
    ) {
        Image(
            /*painter = painterResource(id = R.drawable.qoutely_logo),*/
            painter = painterResource(id = R.drawable.logo_no_background),
            contentDescription = "Logo image",
            modifier = Modifier.fillMaxSize(),
            alignment = Alignment.TopCenter
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailOutlinedTextField(viewModel: LoginViewModel){
    OutlinedTextField(
        value = /*email*/ viewModel.state.email,
        onValueChange = { viewModel.onEvent(LoginFormEvent.EmailChanged(it)) },
        label = {
            Text(
                text = "E-mail",
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Color.Black
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Email,
                contentDescription = "Email icon",
                tint = if(viewModel.state.emailError != null) MaterialTheme.colorScheme.error else Icons.Default.Email.tintColor
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Go
        ),
        isError = viewModel.state.emailError != null,
        textStyle = TextStyle.Default.copy(
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
        ),
        maxLines = 1,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Black,
            cursorColor = Color.Black
        )
    )
    if(viewModel.state.emailError != null){
        Text(
            text = viewModel.state.emailError.toString(),
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp, horizontal = 20.dp),
            textAlign = TextAlign.End,
            fontSize = 12.sp,
            //style = MaterialTheme.typography.labelSmall
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Normal,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordOutlinedTextField(viewModel: LoginViewModel){
    var passwordVisibility by remember { mutableStateOf(viewModel.loginModel.passwordVisibility!!) }

    var icon = if(passwordVisibility)
        painterResource(id = R.drawable.eye_96)
    else
        painterResource(id = R.drawable.invisible_96)

    OutlinedTextField(
        value = viewModel.state.password,
        onValueChange = { viewModel.onEvent(LoginFormEvent.PasswordChanged(it)) },
        label = {
            Text(
                text = "Password",
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Color.Black
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = "Email icon",
                tint = if(viewModel.state.passwordError != null) MaterialTheme.colorScheme.error else Icons.Default.Lock.tintColor
            )
        },
        trailingIcon = {
            IconButton(onClick = {
                //passwordVisibility = !passwordVisibility
                passwordVisibility = viewModel.changePasswordVisiblity(passwordVisibility)!!
            }) {
                Icon(
                    painter = icon,
                    contentDescription = "Visibility icon",
                    modifier=Modifier.size(24.dp)
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),
        visualTransformation = if(passwordVisibility) VisualTransformation.None
        else PasswordVisualTransformation(),
        isError = viewModel.state.passwordError != null,
        textStyle = TextStyle.Default.copy(
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
        ),
        maxLines = 1,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Black,
            cursorColor = Color.Black
        )
    )
    if(viewModel.state.passwordError != null){
        Text(
            text = viewModel.state.passwordError.toString(),
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp, horizontal = 20.dp),
            textAlign = TextAlign.End,
            fontSize = 12.sp,
            //style = MaterialTheme.typography.labelSmall
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Normal,
        )
    }
}

@Composable
fun SignInButton(viewModel: LoginViewModel){
    val context = LocalContext.current
    val channelId = "MyTestChannel"
    val notificationId = 0
    val bigText = "Hello, we're glad " +
            "to see you back! Start exploring new quotes and " +
            "revisit your saved favorites. Let the inspiration begin!"

    /*LaunchedEffect(Unit) {
        createNotificationChannel(channelId, context)
    }*/

    Button(
        onClick = {
            /*showLargeTextNorification(
                context,
                channelId,
                notificationId,
                "Welcome to Quotely",
                bigText
            )*/
            viewModel.onEvent(LoginFormEvent.Submit)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(50.dp),
        shape = RoundedCornerShape(5.dp), // Rounded corners
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black, // Background color
            contentColor = Color.White // Text color
        )
    ) {
        Text(
            text = "Sign in",
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
    }
    if(viewModel.state.loginErrorMessage != null){
        Text(
            text = viewModel.state.loginErrorMessage.toString(),
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp, horizontal = 20.dp),
            textAlign = TextAlign.End,
            fontSize = 12.sp,
            //style = MaterialTheme.typography.labelSmall
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Normal,
        )
    }
}

@Composable
fun NavigationSection(viewModel: LoginViewModel){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(start = 20.dp, end = 20.dp)
    ){
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = Color.Black,
            thickness = 1.dp
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        Text(
            text = "Don't have an account?",
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.width(4.dp))
        ClickableText(
            text = AnnotatedString("Sign up"),
            onClick = {
                viewModel.navigateToRegisterScreen()
            },
            style = TextStyle(
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Color.LightGray
            )
        )
    }
}