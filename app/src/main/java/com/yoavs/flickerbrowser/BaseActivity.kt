package com.yoavs.flickerbrowser

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View

internal const val PHOTO_TRANSFER = "PHOTO_TRANSFER"
internal const val FLICKR_QUERY = "FLICKR_QUERY"

@SuppressLint("Registered")
open class BaseActivity: AppCompatActivity(), TagProvider {

    internal fun activateToolbar(enableHome: Boolean){
        Log.d(tag(), "activateToolbar called")
        var toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(enableHome)
    }
}