package net.albertogarrido.rickandmorty.ui.characterfinder

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList
import net.albertogarrido.rickandmorty.network.RickAndMortyCharacter
import net.albertogarrido.rickandmorty.ui.common.CharacterFinderView
import net.albertogarrido.rickandmorty.util.ErrorType
import net.albertogarrido.rickandmorty.util.Status

class CharacterSearchPresenter(private val view: CharactersSearchView, private val viewModel: CharactersViewModel) {

    interface CharactersSearchView : CharacterFinderView {
        fun populateResults(characters: PagedList<RickAndMortyCharacter>)
        fun showNoMoreResults()
    }

    fun getCharactersLiveData(): LiveData<PagedList<RickAndMortyCharacter>> =
            viewModel.characterLiveData

    fun searchCharactersLiveData(term: String): LiveData<PagedList<RickAndMortyCharacter>> =
            CharactersViewModel(term).searchLiveData

    fun getStatusLiveData(): LiveData<Status> =
            viewModel.networkState

    fun handleResponse(resource: PagedList<RickAndMortyCharacter>) {
        view.populateResults(resource)
    }

    fun handleStatusChanged(status: Status?) {
        when (status) {
            Status.SUCCESS -> view.toggleLoading(false)
            Status.EMPTY_DATA -> {
                view.toggleLoading(false)
                view.showError(ErrorType.NOT_FOUND, "There is no data to load")
            }
            Status.LOADING -> {
                view.toggleLoading(true)
            }
            Status.ERROR -> {
                view.toggleLoading(false)
                view.showError(ErrorType.OTHER, "Ops! it's been an error")
            }
            Status.LOADED -> view.toggleLoading(false)
            Status.END -> view.showNoMoreResults()
        }
    }

}