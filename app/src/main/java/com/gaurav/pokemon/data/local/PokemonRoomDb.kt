package com.gaurav.pokemon.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gaurav.pokemon.data.local.dao.FirebaseDao
import com.gaurav.pokemon.data.local.dao.PokemonDao
import com.gaurav.pokemon.data.model.*
import com.gaurav.pokemon.data.remote.responses.FriendsAndFoes
import com.gaurav.pokemon.utils.Constants.POKEMON_ROOM_DB_NAME

@Database(
    entities = [ApiTokenInfo::class, Friend::class, Foe::class,
        FriendPokemon::class, FoePokemon::class, PokemonLocationInfo::class,
        PokemonList::class, MyTeam::class, FriendsAndFoes::class],
    version = 12
)

@TypeConverters(ListConverters::class)
abstract class PokemonRoomDb : RoomDatabase() {

    // Declaring dao objects
    abstract fun firebaseDao(): FirebaseDao
    abstract fun pokemonDao(): PokemonDao

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
