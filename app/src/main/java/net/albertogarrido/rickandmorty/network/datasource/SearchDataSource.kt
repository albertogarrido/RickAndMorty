package net.albertogarrido.rickandmorty.network.datasource

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PageKeyedDataSource
import net.albertogarrido.rickandmorty.di.provideRetrofitClient
import net.albertogarrido.rickandmorty.network.CharactersResponse
import net.albertogarrido.rickandmorty.network.RickAndMortyApiService
import net.albertogarrido.rickandmorty.network.RickAndMortyCharacter
import net.albertogarrido.rickandmorty.util.Status
import net.albertogarrido.rickandmorty.util.enqueue
import retrofit2.Retrofit

class SearchDataSource(private val term: String) : PageKeyedDataSource<Int, RickAndMortyCharacter>() {

    private val retrofit: Retrofit = provideRetrofitClient()

    private val rickAndMortyApiService: RickAndMortyApiService by lazy {
        retrofit.create(RickAndMortyApiService::class.java)
    }

    private val statusLiveData: MutableLiveData<Status> = MutableLiveData()
    private val charactersLiveData: MutableLiveData<List<RickAndMortyCharacter>> = MutableLiveData()

    override fun loadInitial(params: PageKeyedDataSource.LoadInitialParams<Int>,
                             callback: PageKeyedDataSource.LoadInitialCallback<Int, RickAndMortyCharacter>) {

        statusLiveData.postValue(Status.LOADING)

        rickAndMortyApiService.search(term, 1).enqueue(
                { response ->
                    when {
                        response.body() == null -> statusLiveData.postValue(Status.EMPTY_DATA)
                        response.isSuccessful -> {
                            // this cannot be null, but smart cast complaints, hence the double !!
                            val charsResponse: CharactersResponse = response.body()!!
                            if (charsResponse.results.isEmpty()) {
                                statusLiveData.postValue(Status.END)
                            } else {
                                callback.onResult(charsResponse.results, null, 2)
                                charactersLiveData.postValue(charsResponse.results)
                                statusLiveData.postValue(Status.LOADED)
                            }
                        }
                        else -> {
                            if (response.code() == 404) {
                                statusLiveData.postValue(Status.END)
                            } else {
                                statusLiveData.postValue(Status.ERROR)
                            }
                        }
                    }
                },
                { _ ->
                    statusLiveData.postValue(Status.ERROR)
                }
        )
    }


    override fun loadBefore(params: PageKeyedDataSource.LoadParams<Int>,
                            callback: PageKeyedDataSource.LoadCallback<Int, RickAndMortyCharacter>) {

    }

    override fun loadAfter(params: PageKeyedDataSource.LoadParams<Int>,
                           callback: PageKeyedDataSource.LoadCallback<Int, RickAndMortyCharacter>) {

        statusLiveData.postValue(Status.LOADING)

        rickAndMortyApiService.search(term, params.key).enqueue(
                { response ->
                    when {
                        response.body() == null -> statusLiveData.postValue(Status.EMPTY_DATA)
                        response.isSuccessful -> {
                            // this cannot be null, but smart cast complaints, hence the double !!
                            val charsResponse: CharactersResponse = response.body()!!
                            if (charsResponse.results.isEmpty()) {
                                statusLiveData.postValue(Status.END)
                            } else {
                                callback.onResult(charsResponse.results, params.key.inc())
                                charactersLiveData.postValue(charsResponse.results)
                                statusLiveData.postValue(Status.LOADED)
                            }
                        }
                        else -> {
                            if (response.code() == 404) {
                                statusLiveData.postValue(Status.END)
                            } else {
                                statusLiveData.postValue(Status.ERROR)
                            }
                        }
                    }
                },
                { _ ->
                    statusLiveData.postValue(Status.ERROR)
                }
        )
    }
}