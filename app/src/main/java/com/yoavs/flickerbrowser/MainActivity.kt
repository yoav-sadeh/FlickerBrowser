package com.yoavs.flickerbrowser


import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.lang.Exception

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), GetRawData.OnDowloadComplete, GetFlickerJsonData.OnDataAvailable {

    private val flickrRecycleViewAdapter = FlickrRecyclerViewAdapter(ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = flickrRecycleViewAdapter

        val getRawData = GetRawData(this)
        val url = createUri()
        getRawData.execute(url)

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
            R.id.action_settings -> true
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
}
