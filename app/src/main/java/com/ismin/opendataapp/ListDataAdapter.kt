package com.ismin.opendataapp

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


class ListDataAdapter(
    private val listItems: List<Item>,
    private val fragmentInteractionListener: ListFragment.OnFragmentInteractionListener?,
    private val context: Context?
) :
    RecyclerView.Adapter<ListDataViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListDataViewHolder {
        val row =
            LayoutInflater.from(parent.context).inflate(
                R.layout.fragment_list_item, parent,
                false
            )

        return ListDataViewHolder(row)
    }

    override fun onBindViewHolder(viewholder: ListDataViewHolder, position: Int) {
        val item = listItems[position]

        viewholder.itemTitle.text = item.titre
        viewholder.itemCity.text = item.lieux

        if (context != null) {
            Glide.with(context)
                .load(Uri.parse(item.apercu))
                .thumbnail(Glide.with(context).load(R.drawable.loading))
                .apply(RequestOptions().override(500, 500))
                .centerCrop()
                .error(R.drawable.ic_file_unknown)
                .into(viewholder.itemIcon)
            viewholder.itemIcon.setOnClickListener { _: View? ->
                (
                        fragmentInteractionListener?.displayURL(item.url)
                        )
            }
            viewholder.itemView.setOnClickListener {
                fragmentInteractionListener?.onItemClicked(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return this.listItems.size
    }
}