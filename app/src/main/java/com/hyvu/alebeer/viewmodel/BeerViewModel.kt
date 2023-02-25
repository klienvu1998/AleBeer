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

    // int is position of beer fragment
    private val _onDelete: MutableLiveData<Event<BeerItem>> = MutableLiveData()
    val onDelete: LiveData<Event<BeerItem>>
        get() = _onDelete

    // int is position of beer fragment
    private val _onUpdate: MutableLiveData<Event<BeerItem>> = MutableLiveData()
    val onUpdate: LiveData<Event<BeerItem>>
        get() = _onUpdate

    private val _onSave: MutableLiveData<Event<BeerItem>> = MutableLiveData()
    val onSave: LiveData<Event<BeerItem>>
        get() = _onSave

    private val _isUpdateFavorite: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val isUpdateFavorite: LiveData<Event<Boolean>>
        get() = _isUpdateFavorite

    private val _isShowBeerEmptyScreen: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val isShowEmptyScreen: LiveData<Event<Boolean>>
        get() = _isShowBeerEmptyScreen

    val startTime = System.currentTimeMillis()

    init {
        viewModelScope.launch {
            beerRepo.getBeersFromDb()
            fetchBeers(1)
        }
    }

    fun fetchBeers(page: Int) {
        viewModelScope.launch {
            // load from server
            val response = beerRepo.fetchBeers(page)

            var isNeedUpdateLocal = false
            if (response is BaseResponse.Success) {
                val beerItems: ArrayList<BeerItem> = ArrayList()
                val beers = response.data.data
                // check if data exist in database
                beers.forEach { beerData ->
                    val localItem = beerRepo.getIfExistLocalItem(beerData.id)
                    if (localItem != null) {
                        if (localItem.isNeedUpdateFromServerToDb(beerData)) {
                            isNeedUpdateLocal = true
                            val item = beerRepo.updateBeerInfo(localItem, beerData)
                            beerItems.add(item)
                        } else {
                            beerItems.add(localItem)
                        }
                    } else {
                        beerItems.add(BeerItem.mapData(beerData, ""))
                    }
                }
                beerRepo.addBeerItems(beerItems)

                _beers.postValue(Event(beerItems))
                _isLoadMore.postValue(response.data.loadMore)
            } else {
                if (beerRepo.getAllBeer().isEmpty()) {
                    _isShowBeerEmptyScreen.postValue(Event(true))
                } else {
                    _isShowBeerEmptyScreen.postValue(Event(false))
                    beerSection.clear()
                    beerSection.addAll(beerRepo.getAllBeer())
                    _isLoadMore.postValue(false)
                }
            }
            favoriteBeersSection.clear()
            favoriteBeersSection.addAll(beerRepo.getBeerLocal())
            _isUpdateFavorite.postValue(Event(isNeedUpdateLocal))
        }
    }

    fun saveBeer(beer: BeerItem, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val isInserted = beerRepo.insertBeersToDb(beer)
            if (isInserted) {
                _onSave.postValue(Event(beer))
            }
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

    fun updateBeerNoteFromDb(item: BeerItem, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val beerItem = beerRepo.updateNoteFromDb(item)
            if (beerItem != null) {
                _onUpdate.postValue(Event(beerItem))
                onComplete.invoke(true)
            } else {
                onComplete.invoke(false)
            }
        }
    }

    fun refresh(onComplete: () -> Unit) {
        viewModelScope.launch {
            beerRepo.clearData()
            beerSection.clear()
            favoriteBeersSection.clear()
            beerRepo.getBeersFromDb()
            fetchBeers(1)
            onComplete.invoke()
        }
    }

}