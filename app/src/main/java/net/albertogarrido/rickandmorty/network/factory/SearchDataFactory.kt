package net.albertogarrido.rickandmorty.network.factory

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import net.albertogarrido.rickandmorty.network.RickAndMortyCharacter
import net.albertogarrido.rickandmorty.network.datasource.SearchDataSource

class SearchDataFactory(private val term: String = "") : DataSource.Factory<Int, RickAndMortyCharacter>() {

    private val mutableLiveData: MutableLiveData<SearchDataSource> = MutableLiveData()

    private val searchDataFactory: SearchDataSource by lazy {
        SearchDataSource(term)
    }

    override fun create(): DataSource<Int, RickAndMortyCharacter> {
        mutableLiveData.postValue(searchDataFactory)
        return searchDataFactory
    }
}