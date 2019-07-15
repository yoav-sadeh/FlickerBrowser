package com.yoavs.flickerbrowser

import android.os.AsyncTask
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

class GetFlickerJsonData(private val listener: OnDataAvailable): AsyncTask<String, Void, ArrayList<Photo>>() {

    private val tag = this.javaClass.simpleName

    interface OnDataAvailable{
        fun onDataAvailable(data: List<Photo>)
        fun onError(exception: Exception)
    }

    override fun onPostExecute(result: ArrayList<Photo>) {
        Log.d(tag,"onPostExecute starts")
        listener.onDataAvailable(result)
        Log.d(tag,"onPostExecute ends")
    }

    override fun doInBackground(vararg params: String?): ArrayList<Photo> {
        Log.d(tag,"doInBackground starts")
        val photoList = ArrayList<Photo>()
        try{
            val jsonData = JSONObject(params[0])
            val itemsArray = jsonData.getJSONArray("items")
            for(i in 0 until itemsArray.length()){
                val photoJson = itemsArray.getJSONObject(i)
                val title = photoJson.getString("title")
                val author = photoJson.getString("author")
                val authorId = photoJson.getString("author_id")
                val tags = photoJson.getString("tags")
                val jsonMedia = photoJson.getJSONObject("media")
                val photoUrl = jsonMedia.getString("m")
                val link = photoUrl.replaceFirst("_m.jpg", "_b.jpg")

                val photo = Photo(title, author, authorId, link, tags, photoUrl)
                photoList.add(photo)
                Log.d(tag,"doInBackground: photo - $photo")
            }

        }catch (e: JSONException){
            e.printStackTrace()
            Log.e(tag, "doInBackground: parsing exception - ${e.message}")
            cancel(true)
            listener.onError(e)
        }
        catch(e: Exception){
            Log.e(tag, "doInBackground: exception - ${e.message}")
            cancel(true)
            listener.onError(e)
        }

        Log.d(tag,"doInBackground ends")
        return photoList
    }
}