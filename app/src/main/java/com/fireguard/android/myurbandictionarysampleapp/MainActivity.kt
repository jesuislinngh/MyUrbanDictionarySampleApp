package com.android.myurbandictionarysampleapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.android.myurbandictionarysampleapp.databinding.ActivityMainBinding
import com.fireguard.android.myurbandictionarysampleapp.DefinitionItemAdapter
import com.fireguard.android.myurbandictionarysampleapp.DefinitionViewModel


class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.canonicalName

    private val viewModel: DefinitionViewModel by lazy {
        ViewModelProviders.of(this).get(DefinitionViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.definitionList.adapter =
        DefinitionItemAdapter(DefinitionItemAdapter.OnClickListener {
            Log.d(TAG, "We got here...")
        })

    }

    fun searchTerm(view: View) {
        val editText = findViewById<EditText>(R.id.editText)
        val message = editText.text.toString()

        if (message.length > 1) {

            viewModel.getTermDefinitions("hey")

        } else {
            Log.d(TAG, "no term to look for")
        }
    }
}
