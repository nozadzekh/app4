package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.Recipe
import com.example.myapplication.model.RecipeRepository

class RecipeViewModel : ViewModel() {
    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> get() = _recipes

    private val _selectedRecipe = MutableLiveData<Recipe?>()
    val selectedRecipe: LiveData<Recipe?> get() = _selectedRecipe

    init {
        loadRecipes()
    }

    private fun loadRecipes() {
        _recipes.value = RecipeRepository.getRecipes()
    }

    fun selectRecipe(recipe: Recipe) {
        _selectedRecipe.value = recipe
    }

    fun clearSelection() {
        _selectedRecipe.value = null
    }
}
