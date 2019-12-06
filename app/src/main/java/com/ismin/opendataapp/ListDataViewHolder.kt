package com.ismin.opendataapp

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListDataViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {

    var itemIcon: ImageView
    var itemTitle: TextView
    var itemCity: TextView

    init {
        this.itemIcon = rootView.findViewById(R.id.f_list_item_icon)
        this.itemTitle = rootView.findViewById(R.id.f_list_item_title)
        this.itemCity = rootView.findViewById(R.id.f_list_item_city)
    }
}