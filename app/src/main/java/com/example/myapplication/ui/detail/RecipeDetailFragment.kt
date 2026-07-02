package com.example.myapplication.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.FragmentRecipeDetailBinding
import com.example.myapplication.viewmodel.RecipeViewModel

class RecipeDetailFragment : Fragment() {

    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecipeViewModel by activityViewModels()

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

        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        viewModel.selectedRecipe.observe(viewLifecycleOwner) { recipe ->
            recipe?.let {
                binding.detailTitle.text = it.title
                binding.detailCategory.text = it.category
                binding.detailIngredients.text = it.ingredients.joinToString("\n• ", prefix = "• ")
                binding.detailInstructions.text = it.instructions

                Glide.with(this)
                    .load(it.imageUrl)
                    .centerCrop()
                    .into(binding.detailImage)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
