package com.gaurav.pokemon.data.local

import androidx.room.TypeConverter
import com.gaurav.pokemon.data.model.Pokemon
import com.gaurav.pokemon.data.model.PokemonDetails
import com.gaurav.pokemon.data.model.pokemon.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.java.KoinJavaComponent.inject

class ListConverters {

    private val gson: Gson by inject(Gson::class.java)

    /**
     * Pokemon details list converters
     */

    @TypeConverter
    fun pokemonDetailsObjectToJson(detailsList: PokemonDetails?): String? {
        val type = object : TypeToken<PokemonDetails>() {}.type
        return gson.toJson(detailsList, type)
    }

    @TypeConverter
    fun pokemonDetailsJsonToObject(pokemonDetailsString: String): PokemonDetails? {
        val type = object : TypeToken<PokemonDetails>() {}.type
        return gson.fromJson(pokemonDetailsString, type)
    }

    @TypeConverter
    fun abilityListToJson(abilityList: List<Ability>?): String? {
        val type = object : TypeToken<Ability>() {}.type
        return gson.toJson(abilityList, type)
    }

    @TypeConverter
    fun abilityJsonToList(abilityString: String): List<Ability>? {
        val type = object : TypeToken<List<Ability>>() {}.type
        return gson.fromJson(abilityString, type)
    }

    @TypeConverter
    fun formListToJson(formList: List<Form>?): String? {
        val type = object : TypeToken<Form>() {}.type
        return gson.toJson(formList, type)
    }

    @TypeConverter
    fun formJsonToList(formString: String): List<Form>? {
        val type = object : TypeToken<List<Form>>() {}.type
        return gson.fromJson(formString, type)
    }

    @TypeConverter
    fun gameIndicesListToJson(gameIndicesList: List<GameIndice>?): String? {
        val type = object : TypeToken<GameIndice>() {}.type
        return gson.toJson(gameIndicesList, type)
    }

    @TypeConverter
    fun gameIndicesJsonToList(gameIndicesString: String): List<GameIndice>? {
        val type = object : TypeToken<List<GameIndice>>() {}.type
        return gson.fromJson(gameIndicesString, type)
    }

    @TypeConverter
    fun moveListToJson(moveList: List<Move>?): String? {
        val type = object : TypeToken<Move>() {}.type
        return gson.toJson(moveList, type)
    }

    @TypeConverter
    fun moveJsonToList(moveString: String): List<Move>? {
        val type = object : TypeToken<List<Move>>() {}.type
        return gson.fromJson(moveString, type)
    }

    @TypeConverter
    fun statListToJson(statList: List<Stat>?): String? {
        val type = object : TypeToken<Stat>() {}.type
        return gson.toJson(statList, type)
    }

    @TypeConverter
    fun statJsonToList(statString: String): List<Stat>? {
        val type = object : TypeToken<List<Stat>>() {}.type
        return gson.fromJson(statString, type)
    }

    @TypeConverter
    fun typeListToJson(typeList: List<Type>?): String? {
        val type = object : TypeToken<Type>() {}.type
        return gson.toJson(typeList, type)
    }

    @TypeConverter
    fun typeJsonToList(typeString: String): List<Type>? {
        val type = object : TypeToken<List<Type>>() {}.type
        return gson.fromJson(typeString, type)
    }

    @TypeConverter
    fun spritesObjectToJson(sprites: Sprites): String? {
        val type = object : TypeToken<Sprites>() {}.type
        return gson.toJson(sprites, type)
    }

    @TypeConverter
    fun spritesJsonToList(spritesString: String): Sprites? {
        val type = object : TypeToken<Sprites>() {}.type
        return gson.fromJson(spritesString, type)
    }

    @TypeConverter
    fun speciesObjectToJson(species: Species): String? {
        val type = object : TypeToken<Species>() {}.type
        return gson.toJson(species, type)
    }

    @TypeConverter
    fun speciesJsonToList(spritesString: String): Species? {
        val type = object : TypeToken<Species>() {}.type
        return gson.fromJson(spritesString, type)
    }

    /**
     * Pokemon object
     */

    @TypeConverter
    fun pokemonObjectToJson(pokemon: Pokemon): String? {
        val type = object : TypeToken<Pokemon>() {}.type
        return gson.toJson(pokemon, type)
    }

    @TypeConverter
    fun pokemonJsonToList(pokemonString: String): Pokemon? {
        val type = object : TypeToken<Pokemon>() {}.type
        return gson.fromJson(pokemonString, type)
    }
}