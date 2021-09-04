package com.gaurav.pokemon.ui.main.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.gaurav.pokemon.adapter.CapturedPokemonAdapter
import com.gaurav.pokemon.databinding.FragmentCapturedBinding
import timber.log.Timber

/**
 * CapturedFragment - A [Fragment] subclass used to display the Captured page on screen.
 */
class CapturedFragment : Fragment() {

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

        Timber.d("onViewCreated called !!!")

        val capturedPokemonAdapter =
            context?.let {
                CapturedPokemonAdapter(
                    requireActivity()
                )
            }

        val recyclerViewCaptured = binding.rvCaptured

        val gridLayoutManager = GridLayoutManager(context, 3)
        recyclerViewCaptured.setHasFixedSize(true)
        recyclerViewCaptured.layoutManager = gridLayoutManager
        recyclerViewCaptured.adapter = capturedPokemonAdapter
    }
}