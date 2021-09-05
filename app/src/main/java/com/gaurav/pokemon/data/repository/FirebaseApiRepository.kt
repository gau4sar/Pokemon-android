package com.gaurav.pokemon.data.repository

import com.gaurav.pokemon.data.local.dao.FirebaseDao
import com.gaurav.pokemon.data.model.ApiTokenInfo
import com.gaurav.pokemon.data.remote.firebase.FirebaseApiRemoteDataSource
import com.gaurav.pokemon.utils.responseLiveData

class FirebaseApiRepository(
    private val firebaseApiRemoteDataSource: FirebaseApiRemoteDataSource,
    private val firebaseDao: FirebaseDao
) {

    // Api Token Indo
    val fetchTokenInfo = firebaseDao.fetchTokenInfo()

    suspend fun saveTokenInfo(apiTokenInfo: ApiTokenInfo) =
        firebaseDao.insertTokenInfo(apiTokenInfo)

    suspend fun fetchTokenInfoApi() = firebaseApiRemoteDataSource.getApiTokenInfo()

    // Community info
    val observeCommunityActivity = responseLiveData(
        roomQueryToRetrieveData = { firebaseDao.fetchFriendsList() },
        networkRequest = { firebaseApiRemoteDataSource.getCommunity() },
        roomQueryToSaveData = {
            firebaseDao.insertFriendsList(it.friends)
            firebaseDao.insertFoesList(it.foes)
        })

    val fetchFoesList = firebaseDao.fetchFoesList()

    // MyTeam info
    val observeMyTeam = responseLiveData(
        roomQueryToRetrieveData = { firebaseDao.fetchMyTeamList() },
        networkRequest = { firebaseApiRemoteDataSource.getMyTeam() },
        roomQueryToSaveData = { firebaseDao.insertMyTeamList(it) })

    val fetchMyTeamList = firebaseDao.fetchMyTeamList()

    //Captured Info
    val observeCapturedInfo = responseLiveData(
        roomQueryToRetrieveData = { firebaseDao.fetchCapturedList() },
        networkRequest = { firebaseApiRemoteDataSource.getCaptured() },
        roomQueryToSaveData = { firebaseDao.insertCapturedList(it) })

    val fetchCapturedList = firebaseDao.fetchCapturedList()
}