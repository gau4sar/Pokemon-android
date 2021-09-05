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
import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView
import com.gaurav.pokemon.adapter.MyTeamAdapter
import com.gaurav.pokemon.data.model.MyTeam
import com.gaurav.pokemon.data.remote.ResponseHandler
import com.gaurav.pokemon.ui.main.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [MyTeamFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyTeamFragment : Fragment() {

    private var _binding: FragmentMyTeamBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel by sharedViewModel<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyTeamBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelWorks()

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        binding.rvMyTeam.layoutManager = layoutManager

        Timber.d("onViewCreated called !!!")
    }

    private fun viewModelWorks() {

        mainViewModel.observeMyTeam.observe(viewLifecycleOwner, { apiResponse ->

            when (apiResponse) {

                is ResponseHandler.Success -> {

                    apiResponse.data?.let { observeMyTeam ->
                        Timber.d("observeMyTeam $observeMyTeam")
                    }
                }

                is ResponseHandler.Error -> {
                    Timber.e("Get token info error response: $apiResponse")
                    //handleApiError(apiResponse, this)
                }

                is ResponseHandler.Loading -> {
                }
            }
        })

        mainViewModel.fetchMyTeamList.observe(
            viewLifecycleOwner,
            {
                Timber.d("fetchMyTeamList $it")
                binding.rvMyTeam.adapter = MyTeamAdapter(it, requireActivity())
            })

    }
}