package com.ismin.opendataapp

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


class ListDataAdapter(private val listItems: List<Item>, private val fragmentInteractionListener: ListFragment.OnFragmentInteractionListener?, private val context: Context?) :
    RecyclerView.Adapter<ListDataViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListDataViewHolder {
        val row = LayoutInflater.from(parent.context).inflate(R.layout.fragment_list_item, parent, false)

        return ListDataViewHolder(row)
    }

    override fun onBindViewHolder(viewholder: ListDataViewHolder, position: Int) {
        val item = listItems[position]

        viewholder.itemTitle.text = item.titre
        viewholder.itemCity.text = item.lieux

        val client = OkHttpClient().newBuilder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build()

        if (context != null) {
            val picasso = Picasso.Builder(context)
                .listener { _, _, e -> e.printStackTrace() }
                .downloader(OkHttp3Downloader(client))
                .build()
            picasso.load(Uri.parse(item.apercu))
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_file_unknown)
                .resize(500, 500)
                .centerCrop()
                .onlyScaleDown()
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