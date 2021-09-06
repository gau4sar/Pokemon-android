package com.gaurav.pokemon.ui.main.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.gaurav.pokemon.R
import com.gaurav.pokemon.ui.main.MainViewModel
import com.gaurav.pokemon.utils.observeOnce
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
    }

    private fun viewModelObservers() {

        mainViewModel.tokenInfoLiveData.observeOnce {
            Timber.d("Api Token Info : $it")

            if (it == null || it.expiresAt < System.currentTimeMillis()) {
                // Token has expired fetch a new one using api
                mainViewModel.fetchTokenInfoApi()
                navigateToMain()
            } else {
                Timber.d("Token expires at : ${it.expiresAt}")
                navigateToMain()
            }
        }
    }

    private fun navigateToMain() {
        navController.navigate(R.id.action_splashScreenFragment_to_mainFragment)
    }

    override fun onResume() {
        super.onResume()

        viewModelObservers()
    }
}