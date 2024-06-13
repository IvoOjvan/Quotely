package com.example.qoutely.viewmodel.presentation

sealed class RegistrationFormEvent {
    data class EmailChanged(val email: String) : RegistrationFormEvent()
    data class PasswordChanged(val password: String): RegistrationFormEvent()
    data class RepeatedPasswordChanged(val repeatedPassword: String) : RegistrationFormEvent()
    data class FirstnameChanged(val firstName: String): RegistrationFormEvent()
    data class LastnameChanged(val lastname: String): RegistrationFormEvent()

    object Submit: RegistrationFormEvent()
}