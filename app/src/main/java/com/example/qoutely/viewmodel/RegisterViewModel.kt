package com.example.qoutely.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotApplyResult
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.qoutely.domain.use_case.ValidateEmail
import com.example.qoutely.domain.use_case.ValidateFirstname
import com.example.qoutely.domain.use_case.ValidateLastname
import com.example.qoutely.domain.use_case.ValidatePassword
import com.example.qoutely.domain.use_case.ValidateRepeatedPassword
import com.example.qoutely.model.RegisterModel
import com.example.qoutely.viewmodel.presentation.RegistrationFormEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val navController: NavController,
    private val validateEmail: ValidateEmail = ValidateEmail(),
    private val validatePassword: ValidatePassword = ValidatePassword(),
    private val validateRepeatedPassword: ValidateRepeatedPassword = ValidateRepeatedPassword(),
    private val validateFirstname: ValidateFirstname = ValidateFirstname(),
    private val validateLastname: ValidateLastname = ValidateLastname()
) : ViewModel() {
    //val registerModel = RegisterModel(firstName = null, lastName = null, email = null, password = null)
    val db = Firebase.firestore

    var state by mutableStateOf(RegisterModel())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onEvent(event: RegistrationFormEvent){
        when(event){
            is RegistrationFormEvent.EmailChanged ->{
                state = state.copy(email = event.email)
            }
            is RegistrationFormEvent.PasswordChanged -> {
                state = state.copy(password = event.password)
            }
            is RegistrationFormEvent.RepeatedPasswordChanged -> {
                state = state.copy(repeatedPassword = event.repeatedPassword)
            }
            is RegistrationFormEvent.FirstnameChanged -> {
                state = state.copy(firstName = event.firstName)
            }
            is RegistrationFormEvent.LastnameChanged -> {
                state = state.copy(lastName = event.lastname)
            }
            is RegistrationFormEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData() {
        val emailResult = validateEmail.execute(state.email)
        val passwordResult = validatePassword.execute(state.password)
        val repeatedPasswordResult = validateRepeatedPassword.execute(
            password = state.password,
            repeatedPassword = state.repeatedPassword
        )
        val firstnameResult = validateFirstname.execute(state.firstName)
        val lastnameResult = validateLastname.execute(state.lastName)

        val hasError = listOf(
            emailResult,
            passwordResult,
            repeatedPasswordResult,
            firstnameResult,
            lastnameResult
        ).any { !it.successful }

        state = state.copy(
            emailError = emailResult.errorMessage,
            passwordError = passwordResult.errorMessage,
            repeatedPasswordError = repeatedPasswordResult.errorMessage,
            firstNameError = firstnameResult.errorMessage,
            lastNameError = lastnameResult.errorMessage
        )

        if(hasError){
            return
        }

        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)
        }

    }

    fun registerUser(email:String, password:String, firstName: String, lastName: String, context: Context){
        if (email.isEmpty() || password.isEmpty()) {
            Log.e("Register user", "Email or password is empty")
            return
        }
       FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{task ->
                if(task.isSuccessful){
                    Toast.makeText(context, "Registration in process", Toast.LENGTH_SHORT).show()

                    val user = hashMapOf(
                        "Lastname" to lastName,
                        "Name" to firstName,
                        "email" to email,
                        "password" to password
                    )

                    db.collection("users")
                        .add(user)
                        .addOnSuccessListener {
                            //navController.navigate("login_screen")
                            navigateToLoginScreen()
                        }
                        .addOnFailureListener{
                            Log.d("Register", "FAILED")
                        }

                }
                else{
                    when (val exception = task.exception){
                        is FirebaseAuthUserCollisionException ->{
                            Log.d("Register user", "Email already exists")
                            state = state.copy(
                                emailError = "There is already user with this email"
                            )
                        }
                        else ->{
                            Log.d("Register user", "ERROR: ${exception?.message}")
                        }
                    }
                }
            }
    }

    fun changePasswordVisibility(){
        state = state.copy(passwordVisibility = !state.passwordVisibility)
    }

    fun navigateToLoginScreen(){
        navController.popBackStack()
    }

    sealed class ValidationEvent{
        object Success: ValidationEvent()
    }
}