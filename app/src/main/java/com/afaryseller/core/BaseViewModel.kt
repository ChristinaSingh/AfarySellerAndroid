package com.afaryseller.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

open class BaseViewModel @Inject constructor(): ViewModel(){
    val internetAvailable : MutableLiveData<Boolean> = MutableLiveData()

    fun checkInternet() : LiveData<Boolean>{
       return internetAvailable
    }

}