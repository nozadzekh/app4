package com.example.myapplication.model

data class Recipe(
    val id: Int,
    val title: String,
    val description: String,
    val ingredients: List<String>,
    val instructions: String,
    val imageUrl: String,
    val category: String
)
