package com.fireguard.android.myurbandictionarysampleapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

enum class DictionaryApiStatus { LOADING, ERROR, DONE }

class DefinitionViewModel : ViewModel() {

    private val TAG = DefinitionViewModel::class.java.canonicalName

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<DictionaryApiStatus>()

    // The external immutable LiveData for the request status
    val status: LiveData<DictionaryApiStatus>
        get() = _status

    // Internally, we use a MutableLiveData, because we will be updating the List of MarsProperty
    // with new values
    private val _properties = MutableLiveData<List<DefinitionItem>>()

    // The external LiveData interface to the property is immutable, so only this class can modify
    val definitions: LiveData<List<DefinitionItem>>
        get() = _properties


    fun getTermDefinitions(term: String) {

        coroutineScope.launch {
            // Get the Deferred object for our Retrofit request
            val getTermsDeferred = DictionaryApi.retrofitService.getTermAsync(term)
            try {
                _status.value = DictionaryApiStatus.LOADING
                // this will run on a thread managed by Retrofit
                val listResult = getTermsDeferred.await()
                _status.value = DictionaryApiStatus.DONE
                _properties.value = listResult.list
                Log.d(TAG, "we got a list from the server: ${listResult.list.size}")
            } catch (e: Exception) {
                _status.value = DictionaryApiStatus.ERROR
                _properties.value = ArrayList()
            }
        }

    }

    /**
     * When the [ViewModel] is finished, we cancel our coroutine [viewModelJob], which tells the
     * Retrofit service to stop.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}