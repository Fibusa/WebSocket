package com.example.websocket.stockinfo.domain

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.websocket.stockinfo.data.InfoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InfoViewModel(
    private val repository: InfoRepository): ViewModel() {

    val livedata = MutableLiveData<ScreenState>(ScreenState.Load)

    val buttonLiveData = MutableLiveData<Int>()


    fun loadData(symbol:String) {
        CoroutineScope(Dispatchers.IO).launch {
            buttonLiveData.postValue(repository.getFavorite(symbol))
            livedata.postValue(ScreenState.Load)
            livedata.postValue(repository.getInfo(symbol))
        }
    }

    fun setFavorite(symbol: String) {
        buttonLiveData.value = (buttonLiveData.value!! + 1) % 2
        Log.d("favor","${buttonLiveData.value}")
        CoroutineScope(Dispatchers.IO).launch {
            repository.setFavorite(buttonLiveData.value!!, symbol)
        }
    }
}