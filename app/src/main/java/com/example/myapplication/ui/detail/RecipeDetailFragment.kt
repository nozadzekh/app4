package com.example.myapplication.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.FragmentRecipeDetailBinding
import com.example.myapplication.model.Recipe

class RecipeDetailFragment : Fragment() {

    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!
    
    private val args: RecipeDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipe = args.recipe
        setupUI(recipe)

        binding.detailToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupUI(recipe: Recipe) {
        binding.detailTitle.text = recipe.title
        binding.detailCategory.text = recipe.category
        binding.detailIngredients.text = recipe.ingredients.joinToString("\n• ", prefix = "• ")
        binding.detailInstructions.text = recipe.instructions
        binding.detailTime.text = recipe.prepTime
        binding.detailDifficulty.text = recipe.difficulty

        Glide.with(this)
            .load(recipe.imageUrl)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .centerCrop()
            .into(binding.detailImage)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
