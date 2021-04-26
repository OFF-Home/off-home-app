package com.offhome.app.ui.explore

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.GsonBuilder
import com.offhome.app.R
import com.offhome.app.ui.otherprofile.OtherProfileActivity

/**
 * Class that defines the fragment to holds explore and seach users
 * @property viewModel references the ViewModel class
 * @author Pau Cuesta Arcos
 */
class ExploreFragment : Fragment() {

    /**
     * It has gets the instance of the fragment
     */
    companion object {
        fun newInstance() = ExploreFragment()
    }

    private lateinit var viewModel: ExploreViewModel

    /**
     * it is called when creating view
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.explore_fragment, container, false)
    }

    /**
     * It is called when the activity is created
     * @param savedInstanceState it has the last instance of the view
     * It observe the profileInfo to start the new activity
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ExploreViewModel::class.java)
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
        inflater.inflate(R.menu.search_and_sort_buttons, menu)
        super.onCreateOptionsMenu(menu, inflater)
        val menuItem = menu.findItem(R.id.search)
        val searchView = menuItem.actionView as SearchView
        searchView.maxWidth = Int.MAX_VALUE
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrBlank())
                    Toast.makeText(context, getString(R.string.error_search_user), Toast.LENGTH_LONG).show()
                else
                    viewModel.searchUser(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

}
