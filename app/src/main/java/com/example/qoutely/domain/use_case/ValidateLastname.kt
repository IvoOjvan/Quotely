package com.example.qoutely.domain.use_case

class ValidateLastname {
    fun execute(lastname: String): ValidationResult {
        if(lastname.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "The firstname can't be blank"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}