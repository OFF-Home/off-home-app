package com.offhome.app.ui.chats.listChats

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.offhome.app.R

class ListChatsFragment : Fragment() {

    companion object {
        fun newInstance() = ListChatsFragment()
    }

    private lateinit var viewModel: ListChatsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_chats_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListChatsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
