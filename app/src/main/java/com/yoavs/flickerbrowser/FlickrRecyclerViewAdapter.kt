package com.yoavs.flickerbrowser

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class FlickrRecyclerViewAdapter(private var photoList: List<Photo>) : RecyclerView.Adapter<FlickrImageViewHolder>() {
    private val tag = this.javaClass.simpleName.substring(0, 22)
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): FlickrImageViewHolder {
        Log.d(tag, "onCreateViewHolder - New view requested")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.browse, parent, false)
        return FlickrImageViewHolder(view)
    }

    fun loadNewData(newPhosts: List<Photo>) {
        photoList = newPhosts
        notifyDataSetChanged()
    }

    fun getPhoto(position: Int): Photo? {
        return if (photoList.isNotEmpty()) photoList[position] else null
    }

    override fun getItemCount(): Int {
        Log.d(tag, "getItemCount called")
        return if (photoList.isNotEmpty()) photoList.size else 1
    }

    override fun onBindViewHolder(viewHolder: FlickrImageViewHolder, position: Int) {
        if (photoList.isNotEmpty()) {
            val photoItem = photoList[position]
            Log.d(tag, "onBindViewHolder called with title: ${photoItem.title}")
            Picasso.with(viewHolder.thumbnail.context).load(photoItem.image)
                .error(R.drawable.place_holder)
                .placeholder(R.drawable.place_holder)
                .into(viewHolder.thumbnail)

            viewHolder.title.text = photoItem.title
        }else{
            viewHolder.thumbnail.setImageResource(R.drawable.place_holder)
            viewHolder.title.setText(R.string.no_photo_text)

            Log.d(tag, "onBindViewHolder calledon an empty list")
        }
    }
}

class FlickrImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var thumbnail: ImageView = view.findViewById(R.id.thumbnail)
    var title: TextView = view.findViewById(R.id.title)
}
