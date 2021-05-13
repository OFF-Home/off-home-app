package com.offhome.app.ui.inviteChoosePerson

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
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
import java.util.Collections.addAll

class InviteActivity : AppCompatActivity() {

    private lateinit var viewModel: InviteViewModel
    private lateinit var fab: FloatingActionButton
    private var usersList: List<UserSummaryInfo> = ArrayList()  //todo acabara sent userInfo i ya.
    private lateinit var usersListAdapter: UsersListRecyclerViewAdapter
    private var nMaxRecipients:Int = 200
    private lateinit var textMaxRecipients:TextView
    private lateinit var textNRecipients:TextView
    private lateinit var textRecipientList:TextView
    private var selectedRecipientList: List<UserSummaryInfo> = ArrayList()

    //3r intent
    private var tracker: SelectionTracker<Long>? = null

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
        nMaxRecipients = activityInfo.maxParticipant //todo n_persones remaining

        textMaxRecipients = findViewById(R.id.text_max_recipients)
        textMaxRecipients.text = getString(R.string.max_recipients_banner, nMaxRecipients.toString())
        textNRecipients = findViewById(R.id.text_n_recipients)
        textNRecipients.text = getString(R.string.n_recipients_banner, "0", nMaxRecipients.toString())
        textRecipientList = findViewById(R.id.text_recipient_ist)
        textRecipientList.text = ""

        //en procés
        usersListAdapter = UsersListRecyclerViewAdapter(this)
        val recyclerView = findViewById<RecyclerView>(R.id.RecyclerViewInvite)
        recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.setHasFixedSize(true)  //improves performance.
        recyclerView.adapter = usersListAdapter

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

        //3r intent

        tracker = SelectionTracker.Builder<Long>(
            "mySelection",
            recyclerView,
            StableIdKeyProvider(recyclerView),
            RecipientItemDetailsLookup(recyclerView),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(object : SelectionTracker.SelectionPredicate<Long>() {
            override fun canSelectMultiple(): Boolean {
                return true
            }
            override fun canSetStateForKey(key: Long, nextState: Boolean): Boolean {

                return (! (nextState && tracker?.selection!!.size() >= nMaxRecipients))
            }
            override fun canSetStateAtPosition(position: Int, nextState: Boolean): Boolean {
                return true
            }
        }).build()

        usersListAdapter.tracker = tracker

        tracker?.addObserver(
            object : SelectionTracker.SelectionObserver<Long>() {
                override fun onSelectionChanged() {
                    super.onSelectionChanged()
                    val nItems = tracker?.selection!!.size()
                    textNRecipients.text = getString(R.string.n_recipients_banner, nItems.toString(), nMaxRecipients.toString())

                    //omplim el textView amb la llista de recipients seleccionats
                    var recipientListString = ""

                    val list = tracker?.selection!!.map {
                        usersListAdapter.userList[it.toInt()]
                    }.toList()

                    var isThe1stOne = true

                    for(user in list) {
                        if (!isThe1stOne)
                            recipientListString += ", "

                        recipientListString += user.username

                        isThe1stOne = false
                    }

                    textRecipientList.text = recipientListString

                    //aixo se suposa que clona
                    selectedRecipientList = ArrayList(list)

                    if (nItems <= 0)
                        fab.visibility = View.GONE
                    else
                        fab.visibility = View.VISIBLE
                }
            })

    }

    //fer-lo visible <=> hi hagi algun destinatari seleccionat
    private fun iniFab() {

        fab.setOnClickListener { view ->
            //canviar per algo util
            var recipientListString = ""
            var isThe1stOne = true
            for(user in selectedRecipientList) {
                if (!isThe1stOne)
                    recipientListString += ", "

                recipientListString += user.username

                isThe1stOne = false
            }
            Snackbar.make(view, "Selected recipients: $recipientListString", Snackbar.LENGTH_LONG).show()
        }
        fab.visibility = View.GONE
    }

    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.,menu)
        return true
    }*/
}
