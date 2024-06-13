package com.example.qoutely.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.qoutely.R
import com.example.qoutely.domain.use_case.ValidateEmail
import com.example.qoutely.domain.use_case.ValidatePassword
import com.example.qoutely.model.LoginModel
import com.example.qoutely.model.RegisterModel
import com.example.qoutely.viewmodel.presentation.LoginFormEvent
import com.example.qoutely.viewmodel.presentation.RegistrationFormEvent
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val navController: NavController,
    private val validateEmail: ValidateEmail = ValidateEmail(),
    private val validatePassword: ValidatePassword = ValidatePassword()
) : ViewModel() {
    val loginModel = LoginModel()

    var state by mutableStateOf(LoginModel())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onEvent(event: LoginFormEvent){
        when(event){
            is LoginFormEvent.EmailChanged ->{
                state = state.copy(email = event.email)
            }
            is LoginFormEvent.PasswordChanged -> {
                state = state.copy(password = event.password)
            }
            is LoginFormEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData() {
        val emailResult = validateEmail.execute(state.email)
        val passwordResult = validatePassword.execute(state.password)

        val hasError = listOf(
            emailResult,
            passwordResult,
        ).any { !it.successful }

        state = state.copy(
            emailError = emailResult.errorMessage,
            passwordError = passwordResult.errorMessage
        )
        if(hasError){
            // Added
            viewModelScope.launch {
                validationEventChannel.send(ValidationEvent.Fail)
            }
            return
        }



        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)
        }
    }

    fun changePasswordVisiblity(passwordVisiblity: Boolean): Boolean?{
        return !passwordVisiblity
    }

    fun signInUser(email:String, password:String){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{task->
                if(task.isSuccessful){
                    state = state.copy(
                        loginError = false,
                        loginErrorMessage = null
                    )
                    navController.navigate("home_screen")
                }else{
                    state = state.copy(
                        loginError = true,
                        loginErrorMessage = "Incorrect credentials!"
                    )
                    Log.d("Login User", "Login FAILED")
                }
            }
    }

    fun navigateToRegisterScreen(){
        navController.navigate("register_screen")
    }

    sealed class ValidationEvent{
        object Success: ValidationEvent()
        //added
        object Fail: ValidationEvent()
    }
}