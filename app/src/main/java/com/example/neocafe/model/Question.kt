package com.example.neocafe.model

data class Question(
    val id: Int,
    val question: String
)

data class QuestionAnswer(
    val id: Int,
    val question: String,
    val answer: String
)