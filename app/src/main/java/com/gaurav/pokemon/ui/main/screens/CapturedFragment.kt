package com.gaurav.pokemon.ui.main.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.gaurav.pokemon.adapter.CapturedPokemonAdapter
import com.gaurav.pokemon.data.model.PokemonLocationInfo
import com.gaurav.pokemon.databinding.FragmentCapturedBinding
import com.gaurav.pokemon.ui.main.MainViewModel
import com.gaurav.pokemon.utils.Constants
import com.gaurav.pokemon.utils.observeOnce
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

/**
 * CapturedFragment - A [Fragment] subclass used to display the Captured page on screen.
 */
class CapturedFragment : Fragment() {

    private val mainViewModel by sharedViewModel<MainViewModel>()

    private var _binding: FragmentCapturedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCapturedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.capturedListLiveData.observe(viewLifecycleOwner, {
            if (it == null) {
                mainViewModel.fetchCapturedList()
            } else {
                setupRecyclerView(it)
            }
        })
    }

    private fun setupRecyclerView(pokemonLocationInfoList: List<PokemonLocationInfo>) {

        val capturedPokemonAdapter =
            context?.let {
                CapturedPokemonAdapter(
                    requireActivity(),
                    pokemonLocationInfoList
                )
            }

        val recyclerViewCaptured = binding.rvCaptured

        val gridLayoutManager = GridLayoutManager(context, 3)
        recyclerViewCaptured.setHasFixedSize(true)
        recyclerViewCaptured.layoutManager = gridLayoutManager
        recyclerViewCaptured.adapter = capturedPokemonAdapter
    }
}