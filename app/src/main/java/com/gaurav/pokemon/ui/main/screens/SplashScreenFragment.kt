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
    private val encryptPrefs: EncryptPrefUtils by inject()

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

        mainViewModel.fetchTokenInfo.observe(viewLifecycleOwner, {

            Timber.d("fetchTokenInfo called !!!")
            Timber.d("token expires at : ${it?.expiresAt} \n current time : ${System.currentTimeMillis()}")

            if (it == null || it.expiresAt < System.currentTimeMillis()) {
                // Token has expired fetch a new one using api
                Timber.d("getNewApiToken")
                getNewApiToken()
            } else {

                Timber.d("navigateToMain")
                navigateToMain()
            }
          /*  encryptPrefs.saveApiToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOiJSUjZucUtHMDJQUEpOUk5jV0oyMjRRMmlJdGgyIiwiaWF0IjoxNjMwNzU5NDI4LCJleHAiOjE2MzA3NjMwMjh9.UUKJzomqxBn643kkXloU4Mz_WNxz9Hro_fB5hG61hYc")*/
           /* navigateToMain()*/
        })
    }

    private val fetchApiTokenObserver =
        Observer<ResponseHandler<ApiTokenInfo>> { onFetchAuthInfo(it) }


    private fun onFetchAuthInfo(apiResponse: ResponseHandler<ApiTokenInfo>) {

        when (apiResponse) {

            is ResponseHandler.Success -> {
                apiResponse.data?.let { getTokenInfoResponse ->

                    mainViewModel.apiTokenInfoLiveData.removeObserver(fetchApiTokenObserver)

                    Timber.d("Success token ${getTokenInfoResponse.token}")
                    encryptPrefs.saveApiToken(getTokenInfoResponse.token)
                }
            }

            is ResponseHandler.Error -> {
                Timber.d("Get token info error response: $apiResponse")
                handleApiError(apiResponse, requireActivity())
            }

            is ResponseHandler.Loading -> {
            }
        }
    }

    private fun getNewApiToken() {
        mainViewModel.apiTokenInfoLiveData.observe(requireActivity(), fetchApiTokenObserver)
    }

    private fun navigateToMain() =
        navController.navigate(R.id.action_splashScreenFragment_to_mainFragment)
}