package com.fireguard.android.myurbandictionarysampleapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


const val EXTRA_MESSAGE = "com.fireguard.android.mysampleapplication"

const val API_KEY_DICTIONARY = "db50d29118msh4172e370618d643p1294b3jsnc9607fc9cec0"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    fun sendMessage(view: View) {

        // val editText = findViewById<EditText>(R.id.editText)
        // val message = editText.text.toString()

        /* val intent = Intent(this, DisplayMessageActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, message)
        } */

        // Log.d("This is an alert", "activity send message")

        //startActivity(intent)
    }
}

class TermViewModel(val term: String) : ViewModel() {
    val definitions : MutableLiveData<List<DefinitionItem>> = MutableLiveData()

    fun getData() : LiveData<List<DefinitionItem>> = definitions
}

data class DefinitionItem(
    val definition: String,
    val permalink: String,
    val thumbs_up: Int,
    val sound_urls: List<String>,
    val author: String,
    val word: String,
    val defid: Int,
    val current_vote: Any,
    val written_on : String,
    val example : String,
    val thumbs_down : Int
)