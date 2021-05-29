package com.offhome.app.ui.inviteChoosePerson



import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import com.offhome.app.MainActivity
import com.offhome.app.R
import com.offhome.app.common.Constants
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.model.ActivityDataForInvite
import com.offhome.app.model.Message
import com.offhome.app.model.profile.UserInfo
import com.offhome.app.model.profile.UserSummaryInfo
import com.offhome.app.ui.chats.singleChat.SingleChatActivity

class InviteActivity : AppCompatActivity() {

    private lateinit var viewModel: InviteViewModel
    private lateinit var fab: FloatingActionButton
    private var usersListFullInfo: List<UserInfo> = ArrayList()
    // private var usersList: List<UserSummaryInfo> = ArrayList()
    private var usersList: MutableList<UserSummaryInfo> = ArrayList() // potser acabara sent userInfo i ya.
    private lateinit var usersListAdapter: UsersListRecyclerViewAdapter
    private var nMaxRecipients: Int = 999
    private lateinit var activityInfo: ActivityDataForInvite
    private lateinit var textMaxRecipients: TextView
    private lateinit var textNRecipients: TextView
    private lateinit var textRecipientList: TextView
    private var selectedRecipientList: List<UserSummaryInfo> = ArrayList()

    private var tracker: SelectionTracker<Long>? = null

    // chat messages
    val database = Firebase.database
    private lateinit var myRef: DatabaseReference
    private var currentUID: String = String() //
    private var exists = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite)
        // setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar?.setDisplayHomeAsUpEnabled(false) // todo algun dia arreglar lo de que el upButton fa que peti

        title = "Invite to activity:"

        viewModel = ViewModelProvider(this).get(InviteViewModel::class.java)

        val arguments = intent.extras
        val activityInfoString = arguments?.getString("activity")
        activityInfo = GsonBuilder().create().fromJson(activityInfoString, ActivityDataForInvite::class.java)

        fab = findViewById(R.id.fab)
        iniFab()
        nMaxRecipients = activityInfo.nRemainingParticipants

        textMaxRecipients = findViewById(R.id.text_max_recipients)
        textMaxRecipients.text = getString(R.string.max_recipients_banner, nMaxRecipients.toString())
        textNRecipients = findViewById(R.id.text_n_recipients)
        textNRecipients.text = getString(R.string.n_recipients_banner, "0", nMaxRecipients.toString())
        textRecipientList = findViewById(R.id.text_recipient_ist)
        textRecipientList.text = ""

        currentUID = SharedPreferenceManager.getStringValue(Constants().PREF_UID).toString()

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

                usersListFullInfo = it
                for (user in usersListFullInfo) {
                    usersList.add(UserSummaryInfo(username = user.username, email = user.email, uid = user.uid))
                }
                // TODO no sé si funciona pq no puc testejar pero confiem

                usersListAdapter.setData(usersList)
            }
        )

        // stub, per que per ara no segueixo a ningú. pero treient aquest stub hauria de funcionar
        usersList =
            listOf(UserSummaryInfo(email = "agnesmgomez@gmail.com", username = "agnes", uid = "NujR0SvhtLUICj9BmJPOeUoeqA33"), UserSummaryInfo(email = "ferran.iglesias.barenys@estudiantat.upc.edu", username = "ferran3", uid = "cWSvMtQAczPKujgMqnljP44kbHX2"), UserSummaryInfo(email = "agnesmgomez@gmail.com", username = "agnes", uid = "NujR0SvhtLUICj9BmJPOeUoeqA33"), UserSummaryInfo(email = "ferran.iglesias.barenys@estudiantat.upc.edu", username = "ferran3", uid = "cWSvMtQAczPKujgMqnljP44kbHX2"), UserSummaryInfo(email = "agnesmgomez@gmail.com", username = "agnes", uid = "NujR0SvhtLUICj9BmJPOeUoeqA33"), UserSummaryInfo(email = "ferran.iglesias.barenys@estudiantat.upc.edu", username = "ferran3", uid = "cWSvMtQAczPKujgMqnljP44kbHX2"), UserSummaryInfo(email = "agnesmgomez@gmail.com", username = "agnes", uid = "NujR0SvhtLUICj9BmJPOeUoeqA33"), UserSummaryInfo(email = "ferran.iglesias.barenys@estudiantat.upc.edu", username = "ferran3", uid = "cWSvMtQAczPKujgMqnljP44kbHX2"), UserSummaryInfo(email = "agnesmgomez@gmail.com", username = "agnes", uid = "NujR0SvhtLUICj9BmJPOeUoeqA33"), UserSummaryInfo(email = "ferran.iglesias.barenys@estudiantat.upc.edu", username = "ferran3", uid = "cWSvMtQAczPKujgMqnljP44kbHX2"), UserSummaryInfo(email = "agnesmgomez@gmail.com", username = "agnes", uid = "NujR0SvhtLUICj9BmJPOeUoeqA33"), UserSummaryInfo(email = "ferran.iglesias.barenys@estudiantat.upc.edu", username = "ferran3", uid = "cWSvMtQAczPKujgMqnljP44kbHX2"))
                as MutableList<UserSummaryInfo>
        usersListAdapter.setData(usersList)

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

    // és visible <=> hi ha algun destinatari seleccionat
    private fun iniFab() {

        fab.setOnClickListener { view ->

            if (selectedRecipientList.size > 1) { // si hi ha multiples destinataris, posem snackbar.

                var recipientListString = ""
                var isThe1stOne = true
                for (user in selectedRecipientList) {
                    if (!isThe1stOne)
                        recipientListString += ", "
                    recipientListString += user.username
                    isThe1stOne = false
                }
                Snackbar.make(view, getString(R.string.sending_invitations_snackbar, recipientListString), Snackbar.LENGTH_LONG).show()
            }

            for (recipient in selectedRecipientList) {
                sendMessage(recipient.uid)
            }

            if (selectedRecipientList.size == 1) {
                // todo: acabar els 2 intents. potser he de fer servir view enlloc de this
                val intent = Intent(this, SingleChatActivity::class.java)
                // intent.putExtra("algo", GsonBuilder().create().toJson(/*un objecte*/))    //cal?

                intent.putExtra("uid", selectedRecipientList.first().uid)
                intent.putExtra("username", selectedRecipientList.first().username)
                startActivity(intent)
            } else {
                val intent = Intent(this, /*Chats*/MainActivity::class.java)
                // intent.putExtra("algo", GsonBuilder().create().toJson(/*un objecte*/))    //cal?
                startActivity(intent)
            }
        }
        fab.visibility = View.GONE
    }

    private fun sendMessage(recipientUID: String) {
        val userUid = recipientUID // oi?
        var numMessages: Int = 0

        Log.d("mssg", "sender UID = " + currentUID + ". recipient UID = " + userUid)
        if (currentUID < userUid) myRef = database.getReference("xatsIndividuals/${currentUID}_$userUid")
        else myRef = database.getReference("xatsIndividuals/${userUid}_$currentUID")
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    exists = false
                }
                if (!exists) {
                    val referenceUser1 = database.getReference("usuaris/$userUid")
                    val referenceUser2 = database.getReference("usuaris/$currentUID")
                    referenceUser1.push().setValue(currentUID)
                    referenceUser2.push().setValue(userUid)
                    exists = true
                }
                val linkGenerator = AuxGenerateDynamicLink()
                val dynamicLinkUri: Uri = linkGenerator.generateDynamicLink(activityInfo)
                ++numMessages
                val message = Message(
                    getString(
                        R.string.share_activity_message,
                        /*activityInfo.titol + "\n" +
                            "Category: " + activityInfo.categoria + "\n" +
                            "description: " + activityInfo.descripcio + "\n" +
                            "Created by: " + activityInfo.usuariCreador + "\n" +
                            "at: " + activityInfo.dataHoraIni*/
                        dynamicLinkUri
                    ),
                    currentUID,
                    System.currentTimeMillis()
                )
                // aixo envia el message basically
                if (currentUID < userUid) myRef = database.getReference("xatsIndividuals/${currentUID}_$userUid")
                else myRef = database.getReference("xatsIndividuals/${userUid}_$currentUID")
                myRef.child("m$numMessages").setValue(message)
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "something was cancelled", Toast.LENGTH_SHORT).show()
            }
        })
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
    // a mitjes i blocked: el buscador. no crec que l'acabi
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_button, menu)

        val menuItem = menu?.findItem(R.id.search)
        val searchView = menuItem?.actionView as SearchView
        searchView.maxWidth = Int.MAX_VALUE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                // if (newText != null && newText.isNotEmpty()) {  //si hi ha algo a la bar. todo descomentar
                // get de tots els users i buscar entre ells
                // val completeUserList: List<UserInfo>

                // viewModel.getAllUsers()

                usersListAdapter.performFiltering(newText, /*completeUserList*/)
                // }
                return false
            }
        })

        return true
    }
}
