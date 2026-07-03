package com.example.myapplication.viewmodel

import androidx.lifecycle.*
import com.example.myapplication.model.Recipe
import com.example.myapplication.model.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class RecipeViewModel : ViewModel() {
    private val repository = RecipeRepository()
    
    private val _searchQuery = MutableStateFlow("")
    private val _selectedCategory = MutableStateFlow("ყველა")

    val recipes: LiveData<List<Recipe>> = combine(
        repository.getRecipes(),
        _searchQuery,
        _selectedCategory
    ) { all, query, category ->
        all.filter { recipe ->
            val matchesQuery = recipe.title.lowercase().contains(query.lowercase()) || 
                               recipe.description.lowercase().contains(query.lowercase())
            val matchesCategory = category == "ყველა" || recipe.category == category
            matchesQuery && matchesCategory
        }
    }.asLiveData()

    private val _randomRecipe = MutableLiveData<Recipe?>()
    val randomRecipe: LiveData<Recipe?> get() = _randomRecipe

    init {
        repository.initializeDataIfNeeded()
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setCategory(category: String) {
        _selectedCategory.value = category
    }

    fun generateRandomRecipe() {
        val currentRecipes = recipes.value
        if (!currentRecipes.isNullOrEmpty()) {
            _randomRecipe.value = currentRecipes.random()
        }
    }
}
