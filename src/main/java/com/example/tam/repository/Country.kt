package com.example.tam.repository

data class Country(
    val name: Name,
    val capital: List<String>,
    val flags: Map<String, String>,
    val population: Int,
    val languages: Map<String, String>
)

data class Name(
    val common: String,
    val official: String
)
