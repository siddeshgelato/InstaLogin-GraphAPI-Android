package com.crazy.instalogin_graphapi.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.crazy.instalogin_graphapi.R
import com.crazy.instalogin_graphapi.model.MediaData

import java.util.*

class InstagramGalleryAdapter(private val context: Context, var photoList: ArrayList<MediaData>?) :
    RecyclerView.Adapter<InstagramGalleryAdapter.ViewHolder>() {

    var onItemClick: ((MediaData) -> Unit)? = null
    var selectedMediaData: MediaData? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_image_instagram, viewGroup, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            photoList?.get(viewHolder.adapterPosition)?.let {
                selectedMediaData?.selected = false
                selectedMediaData = it
                selectedMediaData?.selected = true
                notifyDataSetChanged()
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        Glide.with(context).load(photoList?.get(i)?.mediaUrl ?: "").into(viewHolder.imageView)

        if (photoList?.get(i)?.selected == true) {
            viewHolder.imageView.alpha = 0.4f
            viewHolder.imgSelect.visibility = View.VISIBLE
        } else {
            viewHolder.imageView.alpha = 0.9f
            viewHolder.imgSelect.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return photoList?.size ?: 0
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imageView: ImageView = view.findViewById(R.id.imageView)
        var imgSelect: ImageView = view.findViewById(R.id.image_select)

    }


    fun updateData(photoList: ArrayList<MediaData>?){
        this.photoList = photoList
        notifyDataSetChanged()
    }
}