package com.yoavs.flickerbrowser

import android.os.Bundle
import android.support.design.widget.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_photo_detail.*
import kotlinx.android.synthetic.main.content_photo_detail.*

class PhotoDetailActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_detail)
        activateToolbar(true)

        val photo = intent.extras.getParcelable<Photo>(PHOTO_TRANSFER) //as Photo

        photo_title.text = resources.getString(R.string.photo_title_text, photo.title)
        photo_author.text = photo.author
        photo_tags.text = resources.getString(R.string.photo_tags_text, photo.tags)
        Picasso.with(this).load(photo.link)
            .error(R.drawable.place_holder)
            .placeholder(R.drawable.place_holder)
            .into(photo_image)

    }

}
