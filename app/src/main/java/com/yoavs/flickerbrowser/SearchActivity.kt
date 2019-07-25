package com.yoavs.flickerbrowser

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.widget.SearchView

import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : BaseActivity() {
    private var searchView: SearchView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(tag(), "onCreate starts")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        activateToolbar(true)
        Log.d(tag(), "onCreate ends")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.d(tag(), "onCreateOptionsMenu called")

        menuInflater.inflate(R.menu.menu_search, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.app_bar_search).actionView as SearchView
        val searchableInfo = searchManager.getSearchableInfo(componentName)
        searchView?.setSearchableInfo(searchableInfo)
//        Log.d(tag(), "onCreateOptionsMenu $componentName")
//        Log.d(tag(), "onCreateOptionsMenu hint is: ${searchView?.queryHint}")
//        Log.d(tag(), "onCreateOptionsMenu $searchableInfo")

        searchView?.isIconified = false

        searchView?.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(tag(), "onQueryTextSubmit called")
                val sharedPreference = PreferenceManager.getDefaultSharedPreferences(applicationContext)
                sharedPreference.edit().putString(FLICKR_QUERY, query).apply()
                searchView?.clearFocus()
                finish()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        searchView?.setOnCloseListener(object: SearchView.OnCloseListener{
            override fun onClose(): Boolean {
                finish()
                return false
            }
        })

        return true
    }
}
