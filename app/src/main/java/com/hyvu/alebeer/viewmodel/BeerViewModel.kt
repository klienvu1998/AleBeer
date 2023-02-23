package com.hyvu.alebeer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyvu.alebeer.data.remote.BaseResponse
import com.hyvu.alebeer.model.BeerItem
import com.hyvu.alebeer.repository.BeerRepository
import com.hyvu.alebeer.utils.Event
import kotlinx.coroutines.launch
import mva3.adapter.ListSection

class BeerViewModel(
    private val beerRepo: BeerRepository
): ViewModel() {

    val beerSection by lazy { ListSection<BeerItem>() }

    private val _beers: MutableLiveData<Event<List<BeerItem>>> = MutableLiveData()
    val beers: LiveData<Event<List<BeerItem>>>
        get() = _beers

    private val _isLoadMore: MutableLiveData<Boolean> = MutableLiveData()
    val isLoadMore: LiveData<Boolean>
        get() = _isLoadMore

    init {
        fetchBeers(1)
    }

    fun fetchBeers(page: Int) {
        viewModelScope.launch {
            val response = beerRepo.fetchBeers(page)
            if (response is BaseResponse.Success) {
                val beers = response.data.data
                _beers.postValue(Event(beers.map { BeerItem.mapData(it, "") }))
                _isLoadMore.postValue(response.data.loadMore)
            } else {

            }
        }
    }

}