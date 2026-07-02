package com.example.myapplication.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.myapplication.R
import com.example.myapplication.adapter.RecipeAdapter
import com.example.myapplication.databinding.FragmentRecipeListBinding
import com.example.myapplication.ui.detail.RecipeDetailFragment
import com.example.myapplication.viewmodel.RecipeViewModel

class RecipeListFragment : Fragment() {

    private var _binding: FragmentRecipeListBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: RecipeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = RecipeAdapter { recipe ->
            viewModel.selectRecipe(recipe)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, RecipeDetailFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.recipeRecyclerView.adapter = adapter

        viewModel.recipes.observe(viewLifecycleOwner) { recipes ->
            adapter.submitList(recipes)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
