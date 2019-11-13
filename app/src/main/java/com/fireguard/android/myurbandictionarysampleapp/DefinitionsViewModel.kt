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
    private val _definitions = MutableLiveData<List<DefinitionItem>>()

    // The external LiveData interface to the property is immutable, so only this class can modify
    val definitions: LiveData<List<DefinitionItem>>
        get() = _definitions

    fun getTermDefinitions(term: String?, callback: (enable: Boolean) -> Unit) {

        coroutineScope.launch {
            // Get the Deferred object for our Retrofit request
            val getTermsDeferred = DictionaryApi.retrofitService.getTermAsync(term)
            try {
                _status.value = DictionaryApiStatus.LOADING
                // this will run on a thread managed by Retrofit
                val listResult = getTermsDeferred.await()
                _status.value = DictionaryApiStatus.DONE
                _definitions.value = listResult.list

                Log.d(TAG, "we got a list from the server: ${listResult.list.size}")

                callback(true)

            } catch (e: Exception) {
                _status.value = DictionaryApiStatus.ERROR
                _definitions.value = ArrayList()

                callback(false)
            }
        }

    }

    fun orderTermsByThumbs(thumbUp: Boolean) {

        //_definitions.value = _definitions?.value?.sortedWith(compareBy({ it.thumbs_down }, { it.thumbs_down }))
          //  ?.asReversed()

        _definitions.value = _definitions?.value?.sortedBy { if (thumbUp) it.thumbs_up else it.thumbs_down }

    }

    private fun mergeSortingList(list: MutableList<DefinitionItem>) : MutableList<DefinitionItem> {

        if (list.size <= 1) return list

        val middle: Int = (list.size / 2) -1

        var left = mutableListOf<DefinitionItem>()
        var index = 0

        while (index <= middle) {
            left.add(list.get(0))
            list.removeAt(0)
            index++
        }

        left = mergeSortingList(left)

        var right = mutableListOf<DefinitionItem>()
        index = 0

        while (index <= middle) {
            right.add(list.get(0))
            list.removeAt(0)
            index++
        }

        right = mergeSortingList(right)

        return merge(left, right);

    }

    private fun merge(
        left: MutableList<DefinitionItem>,
        right: MutableList<DefinitionItem>
    ): MutableList<DefinitionItem> {

        var result = mutableListOf<DefinitionItem>()
        var leftIndex = 0
        var rightIndex = 0

        /* while (leftIndex < left.size && rightIndex < right.size) {
             if (left.get(leftIndex).compareTo(right.get(rightIndex)) < 1) {
                 result.add(left.get(leftIndex));
                 leftIndex++;
             } else {
                 result.add(right.get(rightIndex));
                 rightIndex++;
             }
         }


         while(leftIndex < left.size() && rightIndex < right.size()) {

             if (left.get(leftIndex).compareTo(right.get(rightIndex)) < 1) {
                 result.add(left.get(leftIndex));
                 leftIndex++;
             } else {
                 result.add(right.get(rightIndex));
                 rightIndex++;
             }

         }

         if(leftIndex < left.size())
             result.addAll(left.subList(leftIndex, left.size()));

         if(rightIndex < right.size())
             result.addAll(right.subList(rightIndex, right.size()));

         return result;*/

        return mutableListOf<DefinitionItem>()
    }

    override fun onCleared() {
        super.onCleared()

        viewModelJob.cancel()
    }
}