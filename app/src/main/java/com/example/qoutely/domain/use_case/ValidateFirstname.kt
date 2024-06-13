package com.example.qoutely.domain.use_case

class ValidateFirstname {
    fun execute(firstname: String): ValidationResult {
        if(firstname.isBlank()){
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