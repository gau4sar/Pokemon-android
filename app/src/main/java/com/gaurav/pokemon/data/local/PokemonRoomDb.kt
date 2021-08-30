package com.gaurav.pokemon.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gaurav.pokemon.data.local.dao.FirebaseApiDao
import com.gaurav.pokemon.data.model.ApiToken
import com.gaurav.pokemon.utils.Constants.POKEMON_ROOM_DB_NAME


@Database(
    entities = [ApiToken::class],
    version = 2
)

abstract class PokemonRoomDb : RoomDatabase() {

    abstract fun getFirebaseApiDao(): FirebaseApiDao

    companion object {
        private var instance: PokemonRoomDb? = null

        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDataBase(context).also { instance = it }
        }

        private fun buildDataBase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                PokemonRoomDb::class.java,
                POKEMON_ROOM_DB_NAME
            ).fallbackToDestructiveMigration().build()
    }
}
