package com.gaurav.pokemon.ui.main.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.gaurav.pokemon.R
import com.gaurav.pokemon.data.model.ApiTokenInfo
import com.gaurav.pokemon.data.remote.ResponseHandler
import com.gaurav.pokemon.ui.main.MainViewModel
import com.gaurav.pokemon.utils.EncryptPrefUtils
import com.gaurav.pokemon.utils.handleApiError
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber


/**
 * A [Fragment] subclass which displays the SplashScreen when the app launches.
 */

class SplashScreenFragment : Fragment() {

    private val mainViewModel by sharedViewModel<MainViewModel>()

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        mainViewModel.tokenInfoLiveData.observe(viewLifecycleOwner, {

            if (it == null || it.expiresAt < System.currentTimeMillis()) {
                // Token has expired fetch a new one using api
                Timber.d("Token expired !!! fetchNewApiToken")
                mainViewModel.fetchTokenInfoApi()
            } else {
                navController.navigate(R.id.action_splashScreenFragment_to_mainFragment)
            }
        })
    }
}