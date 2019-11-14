package com.ismin.opendataapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.ismin.opendataapp.Item
import com.ismin.opendataapp.R



class ListDataAdapter(private val listItems: ArrayList<Item>, private val itemClickListener: ListFragment.OnFragmentInteractionListener?) :
    RecyclerView.Adapter<ListDataViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListDataViewHolder {
        val row = LayoutInflater.from(parent.context).inflate(R.layout.fragment_list_item, parent, false)

        return ListDataViewHolder(row)
    }

    override fun onBindViewHolder(viewholder: ListDataViewHolder, position: Int) {
        val item = this.listItems[position]


        viewholder.itemTitle.text = item.titre
        viewholder.itemCity.text = item.lieux
        viewholder.itemIcon.setImageResource(getIconFromType(item.type))

        viewholder.itemView.setOnClickListener {
            itemClickListener?.onItemClicked(item)
        }
    }


    override fun getItemCount(): Int {
        return this.listItems.size
    }

    private fun getIconFromType(type: String): Int {
        when (type){
            "PDF" -> return R.drawable.ic_file_pdf
            "JPEG" -> return R.drawable.ic_file_jpg
            "PNG" -> return R.drawable.ic_file_png
            else -> return R.drawable.ic_file_unknown
        }
    }
}