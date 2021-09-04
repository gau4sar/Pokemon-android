package com.gaurav.pokemon.ui.main.screens.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.gaurav.pokemon.data.remote.ResponseHandler
import com.gaurav.pokemon.databinding.FragmentCommunityBinding
import com.gaurav.pokemon.ui.main.MainViewModel
import com.gaurav.pokemon.utils.handleApiError
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [CommunityFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CommunityFragment : Fragment() {

    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel by sharedViewModel<MainViewModel>()

    private lateinit var friendsListAdapter: FriendsListAdapter

    private lateinit var foesListAdapter: FoesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        mainViewModel.fetchFriendsList.observe(viewLifecycleOwner, { response ->
            when (response) {

                is ResponseHandler.Success -> {
                    binding.progressFriends.show()

                    response.data?.let { friendsListResponse ->
                        friendsListAdapter.differ.submitList(friendsListResponse)
                    }
                }

                is ResponseHandler.Error -> {
                    binding.progressFriends.hide()
                    handleApiError(response, requireActivity())
                }

                is ResponseHandler.Loading -> {
                    binding.progressFriends.show()
                }
            }
        })


        mainViewModel.fetchFoesList.observe(viewLifecycleOwner, { foeList ->
            foesListAdapter.differ.submitList(foeList)
        })
    }

    private fun setupRecyclerView() {

        friendsListAdapter = FriendsListAdapter(requireActivity())

        binding.rvFriends.apply {
            adapter = friendsListAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }

        foesListAdapter = FoesListAdapter(requireActivity())

        binding.rvFoes.apply {
            adapter = foesListAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }
}