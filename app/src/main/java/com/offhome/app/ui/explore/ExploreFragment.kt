package com.offhome.app.ui.explore



import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.offhome.app.R
import com.offhome.app.data.Result
import com.offhome.app.ui.activitieslist.ActivitiesListRecyclerViewAdapter
import androidx.lifecycle.ViewModelProvider
import com.offhome.app.common.Constants
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.data.model.ActivityFromList
import com.offhome.app.data.model.ChatInfo
import com.offhome.app.ui.activitieslist.ActivitiesViewModel
import com.offhome.app.ui.otherprofile.OtherProfileActivity

/**
 * Class that defines the fragment to holds explore and seach users
 * @property viewModel references the ViewModel class
 * @author Pau Cuesta Arcos
 */
class ExploreFragment : Fragment() {
    private lateinit var viewModel: ExploreViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewFriends: RecyclerView
    private var activitiesList: List<ActivityFromList> = ArrayList()
    private var activitiesListFriends: List<ActivityFromList> = ArrayList()
    private var likedList = ArrayList<Boolean>()
    private var likedActivitiesList: MutableList<ActivityFromList>? = ArrayList()
    private lateinit var activitiesListAdapter: ActivitiesListRecyclerViewAdapter
    private lateinit var activitiesListFiendsAdapter: ActivitiesFriendsListRecyclerViewAdapter

    /**
     * It has gets the instance of the fragment
     */
    companion object {
        fun newInstance() = ExploreFragment()
    }


    /**
     * it is called when creating view
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.explore_fragment, container, false)
        activitiesListAdapter = ActivitiesListRecyclerViewAdapter(context, ActivitiesViewModel())
        recyclerView = view.findViewById(R.id.RecyclerViewExploreSuggested)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = activitiesListAdapter

        activitiesListFiendsAdapter = ActivitiesFriendsListRecyclerViewAdapter(context)
        recyclerViewFriends = view.findViewById(R.id.RecyclerViewExploreFriendActivities)
        recyclerViewFriends.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewFriends.adapter = activitiesListFiendsAdapter
        viewModel = ViewModelProvider(this).get(ExploreViewModel::class.java)

        viewModel.getLikedActivitiesList(SharedPreferenceManager.getStringValue(Constants().PREF_EMAIL).toString()).observe(
            viewLifecycleOwner,
            Observer {
                if (it is Result.Success) {
                    likedActivitiesList = it.data as MutableList<ActivityFromList>
                    viewModel.getSuggestedActivities()
                    viewModel.suggestedActivities.observe(
                        viewLifecycleOwner,
                        Observer {
                            if (it is Result.Success) {
                                activitiesList = it.data
                                likedList.clear()
                                for (item in activitiesList) {
                                    //mirar que activities ya tienen like y ponerlo en la lista con los bools
                                    val found = likedActivitiesList?.find { element -> element.usuariCreador == item.usuariCreador && element.dataHoraIni == item.dataHoraIni }
                                    if (found != null) likedList.add(true)
                                    else likedList.add(false)
                                }
                                activitiesListAdapter.setData(activitiesList, likedList)
                            }

                        })
                }
            }
        )

        viewModel.getFriendsActivities()
        viewModel.friendsActivities.observe(
            viewLifecycleOwner,
            Observer {
                if (it is Result.Success) {
                    activitiesListFriends = it.data
                    for ((index, activity) in activitiesListFriends.withIndex()) {
                        getInfoActivity(activity, index)
                    }
                    activitiesListFiendsAdapter.setData(activitiesListFriends)
                } else if (it is Result.Error) {
                    if (it.exception is NoActivitiesException) {
                        Toast.makeText(context, getString(R.string.no_activities_friends_found), Toast.LENGTH_LONG).show()
                    }
                }
            })

        return view
    }

    private fun getInfoActivity(activity: ActivityFromList, index: Int) {
        viewModel.getUserInfo(activity.usuariCreador).observe(requireActivity(), {
            if (it is Result.Success) {
                activitiesListFriends[index].usernameCreador = it.data.username
                activitiesListFiendsAdapter.setData(activitiesListFriends)
            }
        })
    }

    /**
     * It is called when the activity is created
     * @param savedInstanceState it has the last instance of the view
     * It observe the profileInfo to start the new activity
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.profileInfo.observe(viewLifecycleOwner, {
            if (it != null) {
                val intent = Intent(activity, OtherProfileActivity::class.java)
                val userInfoJSON = GsonBuilder().create().toJson(it)
                intent.putExtra("user_info", userInfoJSON)
                startActivity(intent)
            } else {
                Toast.makeText(context, getString(R.string.user_not_found), Toast.LENGTH_LONG).show()
            }
        })
    }

    /**
     * It creates the options menu and it is called when the user opens the menu for the first time
     * @param menu is the menu provided in the callback
     * @param inflater it inflates the menu and adds items
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_button, menu)
        super.onCreateOptionsMenu(menu, inflater)
        val menuItem = menu.findItem(R.id.search)
        val searchView = menuItem.actionView as SearchView
        searchView.maxWidth = Int.MAX_VALUE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrBlank())
                    Toast.makeText(context, getString(R.string.error_search_user), Toast.LENGTH_LONG).show()
                else
                    viewModel.searchUser(query).observe(
                        viewLifecycleOwner,
                        {
                            if (it is Result.Success) {
                                val intent = Intent(activity, OtherProfileActivity::class.java)
                                val userInfoJSON = GsonBuilder().create().toJson(it.data)
                                intent.putExtra("user_info", userInfoJSON)
                                startActivity(intent)
                            } else if (it is Result.Error) {
                                Toast.makeText(context, getString(R.string.user_not_found), Toast.LENGTH_LONG).show()
                            }
                        }
                    )
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }
}

