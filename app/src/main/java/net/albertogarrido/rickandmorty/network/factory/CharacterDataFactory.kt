package net.albertogarrido.rickandmorty.network.factory

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import net.albertogarrido.rickandmorty.network.datasource.CharacterDataSource
import net.albertogarrido.rickandmorty.network.RickAndMortyCharacter


class CharacterDataFactory : DataSource.Factory<Int, RickAndMortyCharacter>() {

    val mutableLiveData: MutableLiveData<CharacterDataSource> = MutableLiveData()

    private val characterDataSource: CharacterDataSource by lazy {
        CharacterDataSource()
    }

    override fun create(): DataSource<Int, RickAndMortyCharacter> {
        mutableLiveData.postValue(characterDataSource)
        return characterDataSource
    }
}