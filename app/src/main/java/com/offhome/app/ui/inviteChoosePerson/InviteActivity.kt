package com.offhome.app.ui.inviteChoosePerson

import android.os.Bundle
import android.text.InputFilter
import android.view.Menu
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.offhome.app.R
import com.offhome.app.model.ActivityFromList
import com.offhome.app.model.profile.UserSummaryInfo
import com.offhome.app.ui.activitieslist.ActivitiesListRecyclerViewAdapter
import com.offhome.app.ui.profile.ProfileFragmentViewModel

class InviteActivity : AppCompatActivity() {

    private lateinit var viewModel: InviteViewModel
    private lateinit var fab: FloatingActionButton
    private var usersList: List<UserSummaryInfo> = ArrayList()  //todo acabara sent userInfo i ya.
    private lateinit var usersListAdapter: UsersListRecyclerViewAdapter
    private lateinit var textMaxRecipients:TextView
    //to select multiple contacts:
    private var selectionTracker: SelectionTracker<Long>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite)
        //setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this).get(InviteViewModel::class.java)

        val arguments = intent.extras
        val activityInfoString = arguments?.getString("activity")
        val activityInfo = GsonBuilder().create().fromJson(activityInfoString, ActivityFromList::class.java)    //todo canviar per algo q em passi nomes el qui vull? (id de la activity, n_participants.) i el num de persones ja apuntades

        fab = findViewById(R.id.fab)
        iniFab()
        textMaxRecipients = findViewById(R.id.text_max_recipients)
        textMaxRecipients.text = getString(R.string.max_recipients_banner, activityInfo.maxParticipant.toString()) //todo n_persones remaining


        //en procés
        usersListAdapter = UsersListRecyclerViewAdapter(this)
        val recyclerView = findViewById<RecyclerView>(R.id.RecyclerViewInvite)
        recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.setHasFixedSize(true)  //TODO improves performance. (size vol dir tal qual, a la pantalla). descomentar quan sapiga que funciona
        recyclerView.adapter = usersListAdapter

        //to select multiple contacts:
        selectionTracker = SelectionTracker.Builder<Long>(
            "selection-1",
            recyclerView,
            StableIdKeyProvider(recyclerView),
            UsersListRecyclerViewAdapter.InviteLookup(recyclerView),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()

        //restores the state if there was one (the activity was destroyed or whatever)
        if(savedInstanceState != null)
            selectionTracker?.onRestoreInstanceState(savedInstanceState)

        usersListAdapter.setTracker(selectionTracker)

        viewModel.getFollowedUsers()
        viewModel.followedUsers.observe(
            this,
            Observer {
                usersList = it
                usersListAdapter.setData(usersList)
            }
        )

        //stub
        usersList = listOf(UserSummaryInfo(email = "ferran@yes.true", username = "ferran"), UserSummaryInfo(email = "aaaaaaaaaa@yes.true", username = "AAAAAAAAAAAA"), UserSummaryInfo(email = "ferran@yes.true", username = "ferran"), UserSummaryInfo(email = "aaaaaaaaaa@yes.true", username = "AAAAAAAAAAAA"),UserSummaryInfo(email = "ferran@yes.true", username = "ferran"), UserSummaryInfo(email = "aaaaaaaaaa@yes.true", username = "AAAAAAAAAAAA"), UserSummaryInfo(email = "ferran@yes.true", username = "ferran"), UserSummaryInfo(email = "aaaaaaaaaa@yes.true", username = "AAAAAAAAAAAA"), UserSummaryInfo(email = "ferran@yes.true", username = "ferran"), UserSummaryInfo(email = "aaaaaaaaaa@yes.true", username = "AAAAAAAAAAAA"),UserSummaryInfo(email = "ferran@yes.true", username = "ferran"), UserSummaryInfo(email = "aaaaaaaaaa@yes.true", username = "AAAAAAAAAAAA"), UserSummaryInfo(email = "ferran@yes.true", username = "ferran"), UserSummaryInfo(email = "aaaaaaaaaa@yes.true", username = "AAAAAAAAAAAA"), UserSummaryInfo(email = "ferran@yes.true", username = "ferran"), UserSummaryInfo(email = "aaaaaaaaaa@yes.true", username = "AAAAAAAAAAAA"),UserSummaryInfo(email = "ferran@yes.true", username = "ferran"), UserSummaryInfo(email = "aaaaaaaaaa@yes.true", username = "AAAAAAAAAAAA"))
        usersListAdapter.setData(usersList)



    }

    private fun iniFab() {
        //fer-lo visible <=> hi hagi algun destinatari seleccionat
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {    //funciona fent Bundle enlloc de Bundle?.
        super.onSaveInstanceState(outState)

        if(outState != null)
            selectionTracker?.onSaveInstanceState(outState)
    }


    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.,menu)
        return true
    }*/
}
