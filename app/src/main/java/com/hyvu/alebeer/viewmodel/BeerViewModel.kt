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
    val favoriteBeersSection by lazy { ListSection<BeerItem>() }

    private val _beers: MutableLiveData<Event<List<BeerItem>>> = MutableLiveData()
    val beers: LiveData<Event<List<BeerItem>>>
        get() = _beers

    private val _isLoadMore: MutableLiveData<Boolean> = MutableLiveData()
    val isLoadMore: LiveData<Boolean>
        get() = _isLoadMore

    private val _localBeers: MutableLiveData<Event<List<BeerItem>>> = MutableLiveData()
    val localBeers: LiveData<Event<List<BeerItem>>>
        get() = _localBeers

    // int is position of beer fragment
    private val _onDelete: MutableLiveData<Event<BeerItem>> = MutableLiveData()
    val onDelete: LiveData<Event<BeerItem>>
        get() = _onDelete

    init {
        fetchBeers(1)
    }

    fun fetchBeers(page: Int) {
        viewModelScope.launch {
            // only load from local when page = 1
            if (page == 1) {
                val localBeers = beerRepo.getBeersFromDb()
                _localBeers.postValue(Event(localBeers.map { BeerItem.mapData(it, 0L) }))
            }
            // load from server
            val response = beerRepo.fetchBeers(page)

            if (response is BaseResponse.Success) {
                val beerItems: ArrayList<BeerItem> = ArrayList()
                val beers = response.data.data
                // check if data exist in database
                beers.forEach { beerData ->
                    val localItem = beerRepo.getIfExistLocalItem(beerData.id)
                    if (localItem != null) {
                        beerItems.add(localItem)
                    } else {
                        beerItems.add(BeerItem.mapData(beerData, ""))
                    }
                }
                beerRepo.addBeerItems(beerItems)
                _beers.postValue(Event(beerItems))
                _isLoadMore.postValue(response.data.loadMore)
            } else {

            }
        }
    }

    fun saveBeer(beer: BeerItem, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val isInserted = beerRepo.insertBeersToDb(beer)
            onComplete.invoke(isInserted)
        }
    }

    fun deleteBeerFromDb(item: BeerItem, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val beerItem = beerRepo.deleteBeerFromDb(item)
            if (beerItem != null) {
                _onDelete.postValue(Event(beerItem))
                onComplete.invoke(true)
            } else  {
                onComplete.invoke(false)
            }
        }
    }

}