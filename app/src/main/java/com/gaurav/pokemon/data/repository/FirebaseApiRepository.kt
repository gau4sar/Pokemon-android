package com.gaurav.pokemon.data.repository

import com.gaurav.pokemon.data.local.dao.FirebaseDao
import com.gaurav.pokemon.data.remote.firebase.FirebaseApiRemoteDataSource
import com.gaurav.pokemon.utils.responseLiveData

class FirebaseApiRepository(
    private val firebaseApiRemoteDataSource: FirebaseApiRemoteDataSource,
    private val firebaseDao: FirebaseDao
)  {

    val observeApiTokenInfo = responseLiveData(
        // Fetch data from room db
        roomQueryToRetrieveData = { firebaseDao.fetchTokenInfo() },
        // Response from network API
        networkRequest = { firebaseApiRemoteDataSource.getApiTokenInfo() },
        // Save data to Room db
        roomQueryToSaveData = { firebaseDao.insertTokenInfo(it) })

    val fetchTokenInfo = firebaseDao.fetchTokenInfo()

    val observeCommunityActivity = responseLiveData(
        roomQueryToRetrieveData = { firebaseDao.fetchFriendsList() },
        networkRequest = { firebaseApiRemoteDataSource.getCommunity() },
        roomQueryToSaveData = { firebaseDao.insertFriendsList(it.friends) })

    val observeMyTeam = responseLiveData(
        roomQueryToRetrieveData = { firebaseDao.fetchMyTeamList() },
        networkRequest = { firebaseApiRemoteDataSource.getMyTeam() },
        roomQueryToSaveData = { firebaseDao.insertMyTeamList(it.myTeamList) })

    val fetchMyTeamList = firebaseDao.fetchMyTeamList()
}