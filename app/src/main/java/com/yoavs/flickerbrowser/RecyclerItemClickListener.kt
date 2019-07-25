package com.yoavs.flickerbrowser

import android.content.Context
import android.support.v4.view.GestureDetectorCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

class RecyclerItemClickListener(context: Context, recyclerView: RecyclerView, private val listener: OnRecyclerClickListener):TagProvider, RecyclerView.SimpleOnItemTouchListener() {

    private val gestureDetector = GestureDetectorCompat(context, object: GestureDetector.SimpleOnGestureListener(){
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            Log.d(tag(), "onSingleTapUp called")
            val childView = recyclerView.findChildViewUnder(e.x, e.y)
            Log.d(tag(), "onSingleTapUp calling listener.onItemClick")
            if(childView != null)
                listener.onItemClick(childView, recyclerView.getChildAdapterPosition(childView))
            return true
        }

        override fun onLongPress(e: MotionEvent) {
            Log.d(tag(), "onLongPress called")
            val childView = recyclerView.findChildViewUnder(e.x, e.y)
            Log.d(tag(), "onLongPress calling listener.onItemLongClick")
            if(childView != null)
                listener.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView))

            super.onLongPress(e)
        }
    })

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        Log.d(tag(), "onInterceptTouchEvent called with motion: $e")
        val result = gestureDetector.onTouchEvent(e)
        Log.d(tag(), "onInterceptTouchEvent returning: $result")
        return result
    }

    interface OnRecyclerClickListener{
        fun onItemClick(view: View, position: Int)
        fun onItemLongClick(view: View, position: Int)
    }
}