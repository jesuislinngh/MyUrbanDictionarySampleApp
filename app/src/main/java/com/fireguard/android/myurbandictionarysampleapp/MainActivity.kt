package com.android.myurbandictionarysampleapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.android.myurbandictionarysampleapp.databinding.ActivityMainBinding
import com.fireguard.android.myurbandictionarysampleapp.DefinitionItemAdapter
import com.fireguard.android.myurbandictionarysampleapp.DefinitionViewModel


class MainActivity : AppCompatActivity() {

    private var menu: Menu? = null

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        this.menu = menu

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.filterThumbsUp -> {
            // User chose the "Settings" item, show the app settings UI...
            Log.d(TAG, "thumbs up")
            viewModel.orderTermsByThumbs(true)

            true
        }

        R.id.filterThumbsDown -> {
            // User chose the "Favorite" action, mark the current item
            // as a favorite...
            Log.d(TAG, "thumbs down")
            viewModel.orderTermsByThumbs(false)

            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    fun searchTerm(view: View) {
        val editText = findViewById<EditText>(R.id.editText)
        val term = editText.text.toString()

        if (term.length > 1) {

            viewModel.getTermDefinitions(term) {
                this.menu?.getItem(0)?.setEnabled(it)
                this.menu?.getItem(1)?.setEnabled(it)
            }

        } else {
            Log.d(TAG, "no term to look for")
        }
    }
}
