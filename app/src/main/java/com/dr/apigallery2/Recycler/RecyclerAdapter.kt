package com.dr.apigallery2.Recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dr.apigallery2.R
import io.reactivex.rxjava3.subjects.PublishSubject




public class RecyclerAdapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    var context: Context
    var urls: ArrayList<String>



    constructor(context: Context, urls: ArrayList<String>) {
        this.context = context
        this.urls = urls
    }

    public class ViewHolder: RecyclerView.ViewHolder {
        var imageView: ImageView

        constructor(itemView: View) : super(itemView) {
            imageView = itemView.findViewById(R.id.item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutInflater: LayoutInflater = LayoutInflater.from(context)
        var view: View = layoutInflater.inflate(R.layout.item, parent, false)
        var viewHolder: ViewHolder = ViewHolder(view)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return urls.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context)
            .load(urls.get(position))
            .placeholder(R.drawable.loading_layout)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.imageView)
    }
}