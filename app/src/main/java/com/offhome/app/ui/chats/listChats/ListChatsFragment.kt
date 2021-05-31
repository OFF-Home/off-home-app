package com.offhome.app.ui.chats.listChats

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.offhome.app.R
import com.offhome.app.common.MyApp
import com.offhome.app.data.Result
import com.offhome.app.data.model.ChatInfo
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
    private var stringChats = ArrayList<String>()

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
        val recycler = requireView().findViewById<RecyclerView>(R.id.listChats)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter

        viewModel.getChats().observe(viewLifecycleOwner, {
            if (it is Result.Success) {
                stringChats = it.data as ArrayList<String>
                for ((index, chat) in stringChats.withIndex()) {
                    chats.add(ChatInfo(chat, "",  ""))
                    if (chat.contains("_")) {
                        getInfoChatGrupal(chat, index)
                    } else {
                        getInfoUser(chat, index)
                    }
                }
                adapter.setData(chats)
            } else {
                Toast.makeText(MyApp.getContext(), MyApp.getContext().getString(R.string.error_getting_chats), Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getInfoUser(uid: String, index: Int) {
        viewModel.getInfoUser(uid).observe(viewLifecycleOwner, {
            if (it is Result.Success) {
                chats[index].name = it.data.username
                chats[index].image = it.data.image
                adapter.setData(chats)
            }
        })
    }

    private fun getInfoChatGrupal(chat: String, index: Int) {
        viewModel.getInfoUser(chat.split("_")[0]).observe(viewLifecycleOwner, {
            if (it is Result.Success) {
                viewModel.getActivityInfo(it.data.email, chat.split("_")[1]).observe(viewLifecycleOwner, { activityResult ->
                    if (activityResult is Result.Success) {
                        chats[index].name = activityResult.data.titol
                        //chats[index].image = activityResult.data.
                        adapter.setData(chats)
                    }
                })
            }
        })
    }
}
