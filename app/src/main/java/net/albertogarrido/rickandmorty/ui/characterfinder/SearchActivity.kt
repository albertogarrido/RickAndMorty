package net.albertogarrido.rickandmorty.ui.characterfinder

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.arch.paging.PagedList
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.WindowManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.content_search_activity.*
import net.albertogarrido.rickandmorty.R
import net.albertogarrido.rickandmorty.di.getNetworkStatusLiveData
import net.albertogarrido.rickandmorty.network.NetworkStatusLiveData
import net.albertogarrido.rickandmorty.network.RickAndMortyCharacter
import net.albertogarrido.rickandmorty.util.ErrorType
import net.albertogarrido.rickandmorty.util.gone
import net.albertogarrido.rickandmorty.util.onTextChanged
import net.albertogarrido.rickandmorty.util.visible

class SearchActivity : AppCompatActivity(), CharacterSearchPresenter.CharactersSearchView {

    private val adapter = CharacterAdapter()

    private val presenter: CharacterSearchPresenter by lazy {
        CharacterSearchPresenter(this, createViewModel())
    }

    private val networkStatusLiveData: NetworkStatusLiveData by lazy {
        getNetworkStatusLiveData(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        start()
        configureToolbar()
        configureRecyclerView()
        configureViews()
    }

    private fun configureViews() {
        searchInput.onTextChanged { term, _, _ ->
            if (term.length > 3) {
                presenter.searchCharactersLiveData(term).observe(this,
                        Observer { resource ->
                            if (NetworkStatusLiveData.isConnected(this)) {
                                presenter.handleResponse(resource!!)
                            } else {
                                Toast.makeText(this, "Not connected to internet", Toast.LENGTH_SHORT).show()
                            }
                        }
                )
            } else if (term.isEmpty()) {
                start()
            }
        }
    }

    private fun start() {
        observeConnectivityChanges()
        if (!NetworkStatusLiveData.isConnected(this)) {
            showError(ErrorType.NETWORK, "There is no connection to the internet, please try again later")
        }
    }

    private fun configureToolbar() {
        setSupportActionBar(toolbar)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private fun observeConnectivityChanges() {
        networkStatusLiveData.observe(this, Observer { isConnected ->
            if (isConnected == null || !isConnected) {
                showError(ErrorType.NETWORK, "There is no connection to the internet, please try again later")
            } else {
                observeCharacters()
            }
        })
    }

    private fun observeCharacters() {
        presenter.getStatusLiveData().observe(this,
                Observer { status ->
                    presenter.handleStatusChanged(status!!)
                })
        presenter.getCharactersLiveData().observe(this,
                Observer { resource ->
                    presenter.handleResponse(resource!!)
                })
    }

    private fun createViewModel() =
            ViewModelProviders.of(this).get(CharactersViewModel::class.java)

    override fun populateResults(characters: PagedList<RickAndMortyCharacter>) {
        adapter.submitList(characters)
    }

    private fun configureRecyclerView() {
        charactersList.adapter = adapter
        charactersList.setHasFixedSize(true)
        charactersList.layoutManager = StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL)
    }

    override fun showError(errorType: ErrorType, optionalMessage: String?) {
        Toast.makeText(this, optionalMessage, Toast.LENGTH_SHORT).show()
    }

    override fun showNoMoreResults() {
        Toast.makeText(this, "No more results to load", Toast.LENGTH_SHORT).show()
    }

    override fun toggleLoading(show: Boolean) {
        if (show) {
            progressBarContainer.visible()
        } else {
            progressBarContainer.gone()
        }
    }
}
