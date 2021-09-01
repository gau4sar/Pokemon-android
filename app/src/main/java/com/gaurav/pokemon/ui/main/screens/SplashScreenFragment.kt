package com.gaurav.pokemon.ui.main.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.gaurav.pokemon.R
import com.gaurav.pokemon.data.remote.ResponseHandler
import com.gaurav.pokemon.ui.main.MainViewModel
import com.gaurav.pokemon.utils.DataStorePreferences
import com.gaurav.pokemon.utils.handleApiError
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber


/**
 * A [Fragment] subclass which displays the SplashScreen when the app launches.
 */

class SplashScreenFragment : Fragment() {

    private val mainViewModel by sharedViewModel<MainViewModel>()

    private lateinit var navController: NavController

    private val dataStorePreferences: DataStorePreferences by inject()


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

        mainViewModel.getApiTokenInfo?.observe(viewLifecycleOwner, {

            Timber.d("token expires at : ${it?.expiresAt} \n current time : ${System.currentTimeMillis()}")

            val token : String
            
            if(it == null || it.expiresAt < System.currentTimeMillis()) {
                token = ""
                // Token has expired fetch a new one using api
                getApiTokenInfo()
            } else {
                token = it.token
                navigateToMain()
            }

            lifecycleScope.launch {
                Timber.d("saveApiToken called !!!")
                dataStorePreferences.saveApiToken(token)
            }
        })
    }

    private fun getApiTokenInfo() {


        mainViewModel.apiTokenInfoLiveData?.observe(viewLifecycleOwner, { apiResponse ->

            Timber.d("Api token info response : $apiResponse")

            when (apiResponse) {

                is ResponseHandler.Success -> {
                    apiResponse.data?.let { getTokenInfoResponse ->
                        Timber.d("Get token info response $getTokenInfoResponse")

                    }

                    navigateToMain()
                }

                is ResponseHandler.Error -> {
                    Timber.d("Get token info error response: $apiResponse")
                    handleApiError(apiResponse, requireActivity())
                }

                is ResponseHandler.Loading -> { }
            }
        })
    }

    private fun navigateToMain() = navController.navigate(R.id.action_splashScreenFragment_to_mainFragment)
}