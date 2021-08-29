package com.gaurav.pokemon.ui.main.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gaurav.pokemon.R
import com.gaurav.pokemon.databinding.FragmentCommunityBinding
import com.gaurav.pokemon.databinding.FragmentMyTeamBinding
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 * Use the [MyTeamFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyTeamFragment : Fragment() {

    private var _binding: FragmentMyTeamBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyTeamBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Timber.d("onViewCreated called !!!")
    }
}