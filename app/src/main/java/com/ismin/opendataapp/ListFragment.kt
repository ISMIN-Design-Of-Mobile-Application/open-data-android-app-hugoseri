package com.ismin.opendataapp

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    lateinit var recyclerView: RecyclerView
    lateinit var items: List<Item>
    lateinit var itemDao: ItemDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootview = inflater.inflate(R.layout.fragment_list, container, false)

        recyclerView = rootview.findViewById(R.id.f_list_recyclerview)

        itemDao = AppDataBase.getAppDatabase(this.requireContext())
            .getItemDao()

        items = itemDao.getAll()

        val adapter = ListDataAdapter(items, listener, context)
        recyclerView.adapter = adapter
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager

        return rootview
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    fun refreshItems(){
        items = itemDao.getAll()
        recyclerView.adapter?.notifyDataSetChanged()
    }

    interface OnFragmentInteractionListener {
        fun onItemClicked(item: Item)
        fun displayURL(url: String)
    }
}
