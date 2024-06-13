package com.example.qoutely.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.qoutely.R
import com.example.qoutely.ui.theme.InterFontFamily
import com.example.qoutely.viewmodel.RegisterViewModel
import com.example.qoutely.viewmodel.presentation.RegistrationFormEvent

@Composable
fun RegisterScreen(viewModel: RegisterViewModel){
    val context = LocalContext.current
    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect{ event ->
            when(event){
                is RegisterViewModel.ValidationEvent.Success -> {
                    viewModel.registerUser(
                        email = viewModel.state.email,
                        password = viewModel.state.password,
                        firstName = viewModel.state.firstName,
                        lastName = viewModel.state.lastName,
                        context = context
                    )
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 30.dp, end = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TitleSection()
        FirstnameOutlinedTextField(viewModel = viewModel)
        Spacer(modifier = Modifier.height(16.dp))
        LastnameOutlinedTextField(viewModel = viewModel)
        Spacer(modifier = Modifier.height(16.dp))
        EmailOutlinedTextField(viewModel = viewModel)
        Spacer(modifier = Modifier.height(16.dp))
        PasswordOutlinedTextField(viewModel = viewModel)
        Spacer(modifier = Modifier.height(16.dp))
        RepeatedPasswordOutlinedTextField(viewModel = viewModel)
        Spacer(modifier = Modifier.height(16.dp))
        SignupButton(viewModel = viewModel)
        Spacer(modifier = Modifier.height(16.dp))
        NavigationSection(viewModel = viewModel)
    }
}

@Composable
fun TitleSection(){
    Text(
        text = "Create an Account",
        // style = MaterialTheme.typography.titleLarge,
        fontSize = 30.sp,
        modifier = Modifier.padding(bottom = 20.dp),
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirstnameOutlinedTextField(viewModel: RegisterViewModel){
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = /*firstName*/ viewModel.state.firstName,
        onValueChange = { viewModel.onEvent(RegistrationFormEvent.FirstnameChanged(it)) },
        label = {
            Text(
                text = "Firstname",
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = if(viewModel.state.firstNameError != null) MaterialTheme.colorScheme.error else Color.Black
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Email icon",
                tint = if(viewModel.state.firstNameError != null) MaterialTheme.colorScheme.error else Icons.Default.Person.tintColor
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        isError = viewModel.state.firstNameError != null,
        textStyle = TextStyle.Default.copy(
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
        ),
        maxLines = 1,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Black,
            cursorColor = Color.Black
        )
    )
    if(viewModel.state.firstNameError != null){
        Text(
            text = viewModel.state.firstNameError.toString(),
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp),
            textAlign = TextAlign.End,
            fontSize = 10.sp,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Normal,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LastnameOutlinedTextField(viewModel: RegisterViewModel){
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = /*firstName*/ viewModel.state.lastName,
        onValueChange = { viewModel.onEvent(RegistrationFormEvent.LastnameChanged(it)) },
        label = {
            Text(
                text = "Lastname",
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = if(viewModel.state.lastNameError != null)  MaterialTheme.colorScheme.error else Color.Black
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "person icon",
                tint = if(viewModel.state.lastNameError != null) MaterialTheme.colorScheme.error else Icons.Default.Person.tintColor
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        isError = viewModel.state.lastNameError != null,
        textStyle = TextStyle.Default.copy(
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
        ),
        maxLines = 1,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Black,
            cursorColor = Color.Black
        )
    )
    if(viewModel.state.lastNameError != null){
        Text(
            text = viewModel.state.lastNameError.toString(),
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp),
            textAlign = TextAlign.End,
            fontSize = 10.sp,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Normal,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailOutlinedTextField(viewModel: RegisterViewModel){
    OutlinedTextField(
        value = viewModel.state.email,
        onValueChange = { viewModel.onEvent(RegistrationFormEvent.EmailChanged(it)) },
        label = {
            Text(
                text = "E-mail",
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = if(viewModel.state.emailError != null)  MaterialTheme.colorScheme.error else Color.Black
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
            .fillMaxWidth(),
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
            fontSize = 10.sp,
            //style = MaterialTheme.typography.labelSmall
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Normal,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordOutlinedTextField(viewModel: RegisterViewModel){
    val passwordVisibility: Boolean = viewModel.state.passwordVisibility

    val icon = if(passwordVisibility)
        painterResource(id = R.drawable.eye_96)
    else
        painterResource(id = R.drawable.invisible_96)

    OutlinedTextField(
        value = viewModel.state.password,
        onValueChange = { viewModel.onEvent(RegistrationFormEvent.PasswordChanged(it)) },
        label = {
            Text(
                text = "Password",
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = if(viewModel.state.passwordError != null)  MaterialTheme.colorScheme.error else Color.Black
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
                //passwordVisibility = viewModel.changePasswordVisiblity(passwordVisibility)!!
                viewModel.changePasswordVisibility()
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
        visualTransformation =
            if(passwordVisibility)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
        isError = viewModel.state.passwordError != null,
        textStyle = TextStyle.Default.copy(
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
        ),
        maxLines = 1,
        modifier = Modifier
            .fillMaxWidth(),
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
            fontSize = 10.sp,
            //style = MaterialTheme.typography.labelSmall
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Normal,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepeatedPasswordOutlinedTextField(viewModel: RegisterViewModel){
    val passwordVisibility: Boolean = viewModel.state.passwordVisibility /*by remember { mutableStateOf(viewModel.state.passwordVisibility) }*/

    val icon = if(passwordVisibility)
        painterResource(id = R.drawable.eye_96)
    else
        painterResource(id = R.drawable.invisible_96)

    OutlinedTextField(
        value = viewModel.state.repeatedPassword,
        onValueChange = { viewModel.onEvent(RegistrationFormEvent.RepeatedPasswordChanged(it)) },
        label = {
            Text(
                text = "Repeat Password",
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = if(viewModel.state.repeatedPasswordError != null)  MaterialTheme.colorScheme.error else Color.Black
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = "Email icon",
                tint = if(viewModel.state.repeatedPasswordError != null) MaterialTheme.colorScheme.error else Icons.Default.Lock.tintColor
            )
        },
        trailingIcon = {
            IconButton(onClick = {
                //passwordVisibility = !passwordVisibility
                //passwordVisibility = viewModel.changePasswordVisiblity(passwordVisibility)!!
                viewModel.changePasswordVisibility()
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
        visualTransformation =
        if(passwordVisibility)
            VisualTransformation.None
        else
            PasswordVisualTransformation(),
        isError = viewModel.state.repeatedPasswordError != null,
        textStyle = TextStyle.Default.copy(
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
        ),
        maxLines = 1,
        modifier = Modifier
            .fillMaxWidth(),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Black,
            cursorColor = Color.Black
        )
    )
    if(viewModel.state.repeatedPasswordError != null){
        Text(
            text = viewModel.state.repeatedPasswordError.toString(),
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp, horizontal = 20.dp),
            textAlign = TextAlign.End,
            fontSize = 10.sp,
            //style = MaterialTheme.typography.labelSmall
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Normal,
        )
    }
}

@Composable
fun SignupButton(viewModel: RegisterViewModel){
    Button(
        onClick = {
            viewModel.onEvent(RegistrationFormEvent.Submit)
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(5.dp), // Rounded corners
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black, // Background color
            contentColor = Color.White // Text color
        )
    ) {
        Text(
            text = "Sign up",
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
    }
}

@Composable
fun NavigationSection(viewModel: RegisterViewModel){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = Color.Black,
            thickness = 1.dp
        )
        Text(
            text = "or",
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = Color.Black
        )
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
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        Text(
            text = "Already have an account? ",
            textAlign = TextAlign.Center,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = Color.Black
        )
        ClickableText(
            text = AnnotatedString("Sign in"),
            onClick = {
                viewModel.navigateToLoginScreen()
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
