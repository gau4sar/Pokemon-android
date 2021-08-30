package com.gaurav.pokemon.data.repository

import androidx.annotation.WorkerThread
import com.gaurav.pokemon.data.local.dao.FirebaseApiDao
import com.gaurav.pokemon.data.remote.FirebaseApiRemoteDataSource
import com.gaurav.pokemon.utils.responseLiveData

class FirebaseApiRepository(
    private val firebaseApiRemoteDataSource: FirebaseApiRemoteDataSource,
    private val firebaseApiDao: FirebaseApiDao
)  {

    val observeApiTokenInfo = responseLiveData(
        // Fetch data from room db
        roomQueryToRetrieveData = { firebaseApiDao.fetchTokenInfo() },
        // Response from network API
        networkRequest = { firebaseApiRemoteDataSource.getToken() },
        // Save data to Room db
        roomQueryToSaveData = { firebaseApiDao.insertTokenInfo(it) })

    val getApiTokenInfo = firebaseApiDao.fetchExpiresAt()
}