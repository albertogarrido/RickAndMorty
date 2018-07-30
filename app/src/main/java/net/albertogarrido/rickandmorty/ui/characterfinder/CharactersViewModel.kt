package net.albertogarrido.rickandmorty.ui.characterfinder

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import net.albertogarrido.rickandmorty.network.factory.CharacterDataFactory
import net.albertogarrido.rickandmorty.network.RickAndMortyCharacter
import net.albertogarrido.rickandmorty.util.Status
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import android.arch.lifecycle.Transformations
import net.albertogarrido.rickandmorty.network.factory.SearchDataFactory

class CharactersViewModel(term: String = "") : ViewModel() {

    private var executor: Executor = Executors.newFixedThreadPool(5)

    private val characterDataFactory = CharacterDataFactory()
    private val searchDataFactory = SearchDataFactory(term)

    var networkState: LiveData<Status>

    var characterLiveData: LiveData<PagedList<RickAndMortyCharacter>>

    val searchLiveData: LiveData<PagedList<RickAndMortyCharacter>>

    private val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(10)
            .setPageSize(20)
            .build()

    init {
        networkState = Transformations.switchMap(characterDataFactory.mutableLiveData) { dataSource -> dataSource.statusLiveData }

        characterLiveData = LivePagedListBuilder(characterDataFactory, pagedListConfig)
                .setFetchExecutor(executor)
                .build()

        searchLiveData = LivePagedListBuilder(searchDataFactory, pagedListConfig)
                .setFetchExecutor(executor)
                .build()
    }
}