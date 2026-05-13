package com.example.arogyanidhi

data class Eligibility(
    val maxIncome: Int? = null,
    val bplRequired: Boolean? = null,
    val occupations: List<String>? = null
)

data class Scheme(
    val id: String,
    val name: String,
    val description: String,
    val documents: List<String>,
    val eligibility: Eligibility
)

data class Hospital(
    val id: String,
    val name: String,
    val district: String,
    val schemes: List<String>,
    val type: String
)

data class QuizState(
    val income: String = "",
    val bpl: String = "",
    val occupation: String = "",
    val district: String = ""
)
