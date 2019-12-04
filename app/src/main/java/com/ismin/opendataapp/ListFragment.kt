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
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.util.Locale.filter
import com.google.android.gms.common.data.DataHolder




class ListFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ListDataAdapter
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
        val searchField: EditText = rootview.findViewById(R.id.f_list_research)

        itemDao = AppDataBase.getAppDatabase(this.requireContext())
            .getItemDao()

        items = itemDao.getAll()

        adapter = ListDataAdapter(items, listener, context)
        recyclerView.adapter = adapter
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager

        searchField.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                filter(s.toString())
            }
        })

        return rootview
    }

    fun filter(text: String) {
        val temp = arrayListOf<Item>()
        var lowerText = text.toLowerCase()
        for (d in items) {
            if (d.titre.toLowerCase().contains(lowerText) ||
                d.legende.toLowerCase().contains(lowerText) ||
                d.lieux.toLowerCase().contains(lowerText)) {
                temp.add(d)
            }
        }
        adapter.updateList(temp)
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
        adapter.updateList(items)
    }

    interface OnFragmentInteractionListener {
        fun onItemClicked(item: Item)
        fun displayURL(url: String)
    }
}
