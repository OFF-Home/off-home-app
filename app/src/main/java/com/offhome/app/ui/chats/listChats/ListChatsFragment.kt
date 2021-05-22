package com.offhome.app.ui.chats.listChats

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.offhome.app.R
import com.offhome.app.model.ChatInfo
import com.offhome.app.ui.chats.singleChat.SingleChatViewModelFactory

/**
 * Class that defines the fragment to show the Chats
 * @property viewModel references the viewmodel of this fragment
 * @property adapter is the adapter for the RecyclerView of the chats
 * @property chats is the list of chats
 */
class ListChatsFragment : Fragment() {

    companion object {
        fun newInstance() = ListChatsFragment()
    }

    private lateinit var viewModel: ListChatsViewModel
    private lateinit var adapter: ListChatsRecyclerViewAdapter
    private var chats = ArrayList<ChatInfo>()

    /**
     * Called when view created
     * @param inflater is the Layout inflater to inflate the view
     * @param container is the part which contains the view
     * @param savedInstanceState is the last saved instance of the view
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_chats_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, SingleChatViewModelFactory()).get(ListChatsViewModel::class.java)
        adapter = ListChatsRecyclerViewAdapter()
        val recycler = requireView().findViewById<RecyclerView>(R.id.listChats)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter

        viewModel.getChats(viewLifecycleOwner).observe(viewLifecycleOwner, {
            chats = it as ArrayList<ChatInfo>
            adapter.setData(chats)
        })
    }

}
