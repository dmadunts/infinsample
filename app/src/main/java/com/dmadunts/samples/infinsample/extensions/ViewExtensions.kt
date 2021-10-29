package com.dmadunts.samples.infinsample.extensions

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.hide() {
    this.visibility = View.INVISIBLE
}

fun ImageView.loadImage(url: String) {
    Glide.with(this).load(url).thumbnail(0.2f).into(this)
}