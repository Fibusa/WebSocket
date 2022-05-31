package com.example.websocket.stocklist.domain

import android.util.Log
import androidx.lifecycle.*
import com.example.websocket.stocklist.data.MainRepository

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


@ExperimentalCoroutinesApi
class MainViewModel(
    private val repository: MainRepository
    ): ViewModel() {

    val flowData = MutableLiveData<MainState>(MainState.Load)

    private val searchLiveData = MutableStateFlow("")

    val page = MutableStateFlow(0)

    fun startObserve(){
        CoroutineScope(Dispatchers.IO).launch {
            searchLiveData.debounce(700)
                .flatMapLatest {
                    flowData.postValue(MainState.Load)
                    repository.startObserve(it,page.value)
                }.collect{
                    Log.d("flow2","$it")
                    flowData.postValue(it)
            }
        }
    }


    fun setSearchBy(value: String) {
        if (searchLiveData.value == value) return
        searchLiveData.value = value
    }



    fun stopFlow(){
        repository.stopObserve()
    }

}