package com.example.arogyanidhi

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ArogyaViewModel : ViewModel() {

    private val _quizData = MutableStateFlow(QuizState())
    val quizData = _quizData.asStateFlow()

    private val _eligibleSchemes = MutableStateFlow<List<Scheme>>(emptyList())
    val eligibleSchemes = _eligibleSchemes.asStateFlow()

    // Form inputs state
    fun updateQuizField(field: String, value: String) {
        _quizData.update { current ->
            when (field) {
                "income" -> current.copy(income = value)
                "bpl" -> current.copy(bpl = value)
                "occupation" -> current.copy(occupation = value)
                "district" -> current.copy(district = value)
                else -> current
            }
        }
    }

    fun calculateSchemes() {
        val data = _quizData.value
        val incomeNum = data.income.filter { it.isDigit() }.toIntOrNull() ?: 0
        val isBpl = data.bpl == "Yes"

        val matched = LocalData.SCHEMES.filter { scheme ->
            var eligible = true
            
            // Check BPL
            if (scheme.eligibility.bplRequired == true && !isBpl) {
                eligible = false
            }
            // Check Income
            if (scheme.eligibility.maxIncome != null && incomeNum > scheme.eligibility.maxIncome) {
                eligible = false
            }
            // Check Occupation
            if (scheme.eligibility.occupations != null && !scheme.eligibility.occupations.contains(data.occupation)) {
                eligible = false
            }
            
            eligible
        }
        _eligibleSchemes.value = matched
    }

    fun resetQuiz() {
        _quizData.value = QuizState()
        _eligibleSchemes.value = emptyList()
    }
}
