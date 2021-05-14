package com.offhome.app.ui.inviteChoosePerson



import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.offhome.app.R
import com.offhome.app.model.ActivityFromList
import com.offhome.app.model.profile.UserInfo
import com.offhome.app.model.profile.UserSummaryInfo

class InviteActivity : AppCompatActivity() {

    private lateinit var viewModel: InviteViewModel
    private lateinit var fab: FloatingActionButton
    private var usersListFullInfo: List<UserInfo> = ArrayList()
    //private var usersList: List<UserSummaryInfo> = ArrayList()  //todo acabara sent userInfo i ya.
    private var usersList: MutableList<UserSummaryInfo> = ArrayList()
    private lateinit var usersListAdapter: UsersListRecyclerViewAdapter
    private var nMaxRecipients: Int = 200
    private lateinit var textMaxRecipients: TextView
    private lateinit var textNRecipients: TextView
    private lateinit var textRecipientList: TextView
    private var selectedRecipientList: List<UserSummaryInfo> = ArrayList()

    // 3r intent
    private var tracker: SelectionTracker<Long>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite)
        // setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this).get(InviteViewModel::class.java)

        val arguments = intent.extras
        val activityInfoString = arguments?.getString("activity")
        val activityInfo = GsonBuilder().create().fromJson(activityInfoString, ActivityFromList::class.java) // todo canviar per algo q em passi nomes el qui vull? (id de la activity, n_participants.) i el num de persones ja apuntades

        fab = findViewById(R.id.fab)
        iniFab()
        nMaxRecipients = activityInfo.maxParticipant // todo n_persones remaining

        textMaxRecipients = findViewById(R.id.text_max_recipients)
        textMaxRecipients.text = getString(R.string.max_recipients_banner, nMaxRecipients.toString())
        textNRecipients = findViewById(R.id.text_n_recipients)
        textNRecipients.text = getString(R.string.n_recipients_banner, "0", nMaxRecipients.toString())
        textRecipientList = findViewById(R.id.text_recipient_ist)
        textRecipientList.text = ""

        // en procés
        usersListAdapter = UsersListRecyclerViewAdapter(this)
        val recyclerView = findViewById<RecyclerView>(R.id.RecyclerViewInvite)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true) // improves performance.
        recyclerView.adapter = usersListAdapter

        viewModel.getFollowedUsers()
        viewModel.followedUsers.observe(
            this,
            Observer {

                //usersList = it

                usersListFullInfo = it
                for (user in usersListFullInfo) {
                    usersList.add(UserSummaryInfo(username = user.username, email = user.email))
                }
                //TODO no sé si funciona pq no puc testejar pero confiem

                usersListAdapter.setData(usersList)
            }
        )

        //stub
        /*usersList =
            listOf(UserSummaryInfo(email = "ferran@yes.true", username = "ferran"), UserSummaryInfo(email = "aaaaaaaaaa@yes.true", username = "AAAAAAAAAAAA"), UserSummaryInfo(email = "ferran@yes.true", username = "ferran"), UserSummaryInfo(email = "aaaaaaaaaa@yes.true", username = "AAAAAAAAAAAA"),UserSummaryInfo(email = "ferran@yes.true", username = "ferran"), UserSummaryInfo(email = "aaaaaaaaaa@yes.true", username = "AAAAAAAAAAAA"), UserSummaryInfo(email = "ferran@yes.true", username = "ferran"), UserSummaryInfo(email = "aaaaaaaaaa@yes.true", username = "AAAAAAAAAAAA"), UserSummaryInfo(email = "ferran@yes.true", username = "ferran"), UserSummaryInfo(email = "aaaaaaaaaa@yes.true", username = "AAAAAAAAAAAA"),UserSummaryInfo(email = "ferran@yes.true", username = "ferran"), UserSummaryInfo(email = "aaaaaaaaaa@yes.true", username = "AAAAAAAAAAAA"), UserSummaryInfo(email = "ferran@yes.true", username = "ferran"), UserSummaryInfo(email = "aaaaaaaaaa@yes.true", username = "AAAAAAAAAAAA"), UserSummaryInfo(email = "ferran@yes.true", username = "ferran"), UserSummaryInfo(email = "aaaaaaaaaa@yes.true", username = "AAAAAAAAAAAA"),UserSummaryInfo(email = "ferran@yes.true", username = "ferran"), UserSummaryInfo(email = "aaaaaaaaaa@yes.true", username = "AAAAAAAAAAAA"))
                as MutableList<UserSummaryInfo>
        usersListAdapter.setData(usersList)*/

        // 3r intent

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

                    // omplim el textView amb la llista de recipients seleccionats
                    var recipientListString = ""

                    val list = tracker?.selection!!.map {
                        usersListAdapter.userList[it.toInt()]
                    }.toList()

                    var isThe1stOne = true

                    for (user in list) {
                        if (!isThe1stOne)
                            recipientListString += ", "

                        recipientListString += user.username

                        isThe1stOne = false
                    }

                    textRecipientList.text = recipientListString

                    // aixo se suposa que clona
                    selectedRecipientList = ArrayList(list)

                    if (nItems <= 0)
                        fab.visibility = View.GONE
                    else
                        fab.visibility = View.VISIBLE
                }
            })
    }

    // fer-lo visible <=> hi hagi algun destinatari seleccionat
    private fun iniFab() {

        fab.setOnClickListener { view ->
            // placeholder listener
            var recipientListString = ""
            var isThe1stOne = true
            for (user in selectedRecipientList) {
                if (!isThe1stOne)
                    recipientListString += ", "

                recipientListString += user.username

                isThe1stOne = false
            }
            Snackbar.make(view, "Selected recipients: $recipientListString", Snackbar.LENGTH_LONG).show()

            // el de veritat
            // si hi ha multiples destinataris, posem snackbar.
            if (selectedRecipientList.size == 1) {
                // todo: acabar els 2 intents despres del merge. potser he de fer servir view enlloc de this
                /*val intent = Intent(this, /*Chat concret*/::class.java)
                //intent.putExtra("algo", GsonBuilder().create().toJson(/*un objecte*/))    //cal?
                startActivity(intent)*/
            } else {
                Snackbar.make(view, getString(R.string.sending_invitations_snackbar, recipientListString), Snackbar.LENGTH_LONG).show()
                /*val intent = Intent(this, /*Chats*/::class.java)
                //intent.putExtra("algo", GsonBuilder().create().toJson(/*un objecte*/))    //cal?
                startActivity(intent)*/
            }
        }
        fab.visibility = View.GONE
    }

    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.,menu)
        return true
    }*/

    /**
     * Specifies the options menu for the activity
     * @param menu provided
     * @return true
     */
    //doing: el buscador
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_button,menu)

        val menuItem = menu?.findItem(R.id.search)
        val searchView = menuItem?.actionView as SearchView
        searchView.maxWidth = Int.MAX_VALUE
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                //if (newText != null && newText.isNotEmpty()) {  //si hi ha algo a la bar. todo descomentar
                    //get de tots els users i buscar entre ells
                    //val completeUserList: List<UserInfo>

                    //viewModel.getAllUsers()

                    usersListAdapter.performFiltering(newText, /*completeUserList*/)
                //}
                return false
            }
        })

        return true
    }
}
