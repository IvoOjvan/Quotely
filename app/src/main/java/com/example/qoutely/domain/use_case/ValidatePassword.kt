package com.example.qoutely.domain.use_case

class ValidatePassword {
    fun execute(password: String): ValidationResult{
        if(password.length < 8){
            return ValidationResult(
                successful = false,
                errorMessage = "Password must be at least 8 characters."
            )
        }

        val containesLettersAndDigits = password.any { it.isDigit() } && password.any { it.isLetter() }
        if(!containesLettersAndDigits){
            return ValidationResult(
                successful = false,
                errorMessage = "The password needs to contain at least one letter or digit"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}