package com.yoavs.flickerbrowser

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.content_main.*

private const val TAG = "MainActivity"

class MainActivity : BaseActivity(), GetRawData.OnDowloadComplete, GetFlickerJsonData.OnDataAvailable,
    RecyclerItemClickListener.OnRecyclerClickListener {
    override fun onItemClick(view: View, position: Int) {
        Log.d(tag(), "onItemClick starts")
        Toast.makeText(this, "Item Normal Click", Toast.LENGTH_SHORT).show()
    }

    override fun onItemLongClick(view: View, position: Int) {
        Log.d(tag(), "onItemLongClick starts")
        val photo = flickrRecycleViewAdapter.getPhoto(position)
        if(photo != null){
            val intent = Intent(this, PhotoDetailActivity::class.java)
            intent.putExtra(PHOTO_TRANSFER, photo)
            startActivity(intent)
        }
    }

    private val flickrRecycleViewAdapter = FlickrRecyclerViewAdapter(ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activateToolbar(false)

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.addOnItemTouchListener(RecyclerItemClickListener(this, recycler_view, this))
        recycler_view.adapter = flickrRecycleViewAdapter

        Log.d(TAG, "onCreate ends")

    }

    private fun createUri(searchCriteria: String = "", lang: String = "en-us", matchAll: Boolean = true): String {
        Log.d(TAG, "createUri starts")
        val initialUri = "https://api.flickr.com/services/feeds/photos_public.gne?format=json&nojsoncallback=true"
        val builder = Uri.parse(initialUri).buildUpon().appendQueryParameter("tagnode", if (matchAll) "ALL" else "ANY")
            .appendQueryParameter("lang", lang)
        if (searchCriteria.isNotEmpty()) builder.appendQueryParameter("tags", searchCriteria)
        val uri = builder.toString()
        Log.d(TAG, "createUri ends, url: $uri")
        return uri
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.d(TAG, "onCreateOptionsMenu called")
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "onOptionsItemSelected called")
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_search -> {
                startActivity(Intent(this, SearchActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDownloadComplete(data: String, status: DownloadStatus) {
        if (status == DownloadStatus.OK) {
            Log.d(TAG, "onDownloadComplete called")
            val parser = GetFlickerJsonData(this)
            parser.execute(data)
        } else {
            Log.d(TAG, "onDownloadComplete called: Download failed with status: $status, error message: $data")
        }
    }

    override fun onDataAvailable(data: List<Photo>) {
        Log.d(TAG, "onDataAvailable called with data: $data")
        flickrRecycleViewAdapter.loadNewData(data)
        Log.d(TAG, "onDataAvailable ends")
    }

    override fun onError(exception: Exception) {
        Log.d(TAG, "onError called with exception ${exception.message}")
    }

    override fun onResume() {
        Log.d(tag(), "onResume starts")
        super.onResume()

        val sharedPreference = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val queryResult:String = sharedPreference.getString(FLICKR_QUERY, "")

        if(queryResult.isNotEmpty()){
            val getRawData = GetRawData(this)
            val url = createUri(queryResult)
            getRawData.execute(url)
        }

        Log.d(tag(), "onResume ends")
    }


}
