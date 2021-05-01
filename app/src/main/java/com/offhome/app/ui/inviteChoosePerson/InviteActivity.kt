package com.offhome.app.ui.inviteChoosePerson

import android.os.Bundle
import android.text.InputFilter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.offhome.app.R
import com.offhome.app.model.ActivityFromList
import com.offhome.app.model.profile.UserSummaryInfo
import com.offhome.app.ui.activitieslist.ActivitiesListRecyclerViewAdapter
import com.offhome.app.ui.profile.ProfileFragmentViewModel

class InviteActivity : AppCompatActivity() {

    private lateinit var viewModel: InviteViewModel
    private lateinit var fab: FloatingActionButton
    private var usersList: List<UserSummaryInfo> = ArrayList()  //todo acabara sent userInfo i ya.
    //private lateinit var usersListAdapter: UsersListRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite)
        //setSupportActionBar(findViewById(R.id.toolbar))

        viewModel = ViewModelProvider(this).get(InviteViewModel::class.java)
        fab = findViewById(R.id.fab)
        iniFab()


        //en procés
        /*usersListAdapter = UsersListRecyclerViewAdapter(this)
        val recyclerView = findViewById<RecyclerView>(R.id.RecyclerViewInvite)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = usersListAdapter

        viewModel.getFollowedUsers()
        viewModel.followedUsers.observe(
            this,
            Observer {
                usersList = it
                usersListAdapter.setData(usersList)
            }
        )*/

        //stub
        usersList = listOf(UserSummaryInfo(email = "ferran@yes.true", username = "ferran"), UserSummaryInfo(email = "aaaaaaaaaa@yes.true", username = "AAAAAAAAAAAA"))
        //usersListAdapter.setData(usersList)



    }

    private fun iniFab() {
        //fer-lo visible <=> hi hagi algun destinatari seleccionat
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }
}
