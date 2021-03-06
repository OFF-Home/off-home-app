package com.offhome.app.ui.profile



import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.offhome.app.R
import com.offhome.app.common.Constants
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.data.Result
import com.offhome.app.data.model.ActivityFromList
import com.offhome.app.ui.activitieslist.ActivitiesListRecyclerViewAdapter
import com.offhome.app.ui.activitieslist.ActivitiesViewModel
import com.offhome.app.ui.oldandliked.LikedActivities
import com.offhome.app.ui.oldandliked.OldActivities
import kotlin.collections.ArrayList

/**
 * Class *ProfileMyActivitiesFragment*
 *
 * Fragment for the "my activities" section (page) of the Profile screen.
 * This class is one of the Views in this screen's MVVM's
 *
 * @author Ferran with borrowed code
 * @property profileVM reference to the ViewModel object of the entire Profile.
 * @property activitiesList list of activities to be displayed
 * @property activitiesListAdapter references the adapter for the RecyclerView of the activities
 */
class ProfileMyActivitiesFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileMyActivitiesFragment()
    }

    private lateinit var profileVM: ProfileFragmentViewModel
    private var activitiesList: List<ActivityFromList> = ArrayList()
    private lateinit var activitiesViewModel: ActivitiesViewModel
    private lateinit var activitiesListAdapter: ActivitiesListRecyclerViewAdapter
    lateinit var buttonold: Button
    lateinit var buttonliked: Button

    /**
     * Override the onCreateView method
     *
     * Does the fragment inflation
     * Initializes the attributes
     *
     * observes the VM's live data for the result of the call made by the ProfileFragment to obtain the user's data, which includes their activities.
     * The activities will be inserted in the recyclerview
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return returns the view
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_my_activities_fragment, container, false)

        rotateArrowDrawables()

        activitiesViewModel = ViewModelProvider(this).get(ActivitiesViewModel::class.java)

        buttonold = view.findViewById<Button>(R.id.buttonOlderActivities)
        buttonliked = view.findViewById<Button>(R.id.buttonLikedActivities)

        // nos lleva a la pantalla con las actividades viejas
        buttonold.setOnClickListener {
            val intent = Intent(context, OldActivities::class.java)
            context?.startActivity(intent)
        }


        //nos lleva a la pantalla con las actividades guardadas
        buttonliked.setOnClickListener {
            val intent = Intent(context, LikedActivities::class.java)
            context?.startActivity(intent)
        }

        // tot lo del recycler ho he robat descaradament de ActivitiesList
        activitiesListAdapter = ActivitiesListRecyclerViewAdapter(context, activitiesViewModel)
        val recyclerView = view.findViewById<RecyclerView>(R.id.RecyclerViewProfileActivities)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = activitiesListAdapter

        val profileFragment: ProfileFragment = parentFragment as ProfileFragment
        profileVM = profileFragment.getViewModel()
        profileVM.getLikedActivitiesList(SharedPreferenceManager.getStringValue(Constants().PREF_EMAIL).toString())
        profileVM.likedActivities.observe(
            viewLifecycleOwner,
            Observer {
                if (it is Result.Success) {
                    val likedActivitiesList = it.data as MutableList<ActivityFromList>
                    profileVM.myActivities.observe(
                        viewLifecycleOwner,
                        Observer {
                            if (it is Result.Success) {
                                activitiesList = it.data
                                val likedList = ArrayList<Boolean>()
                                for (item in activitiesList) {
                                    //mirar que activities ya tienen like y ponerlo en la lista con los bools
                                    val found = likedActivitiesList.find { element -> element == item }
                                    if (found == item) likedList.add(true)
                                    else likedList.add(false)
                                }
                                activitiesListAdapter.setData(activitiesList, likedList)
                            }

                        })
                }
            }
        )
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    // no ho vaig aconseguir
    /**
     * Rotates the "arrow" icon drawables on the two buttons of the layout
     */
    private fun rotateArrowDrawables() { /*
        //val dr: Drawable = resources.getDrawable(android.R.drawable.abc_vector_test)
        val dr2: Drawable = resources.getDrawable(R.drawable.abc_vector_test)

         //?attr/actionModeCloseDrawable
        //Reference:	@drawable/abc_vector_test
        //abc_vector_test.xml
        val bitmap: Bitmap = (dr2 as BitmapDrawable).bitmap

        editIconDrawable = BitmapDrawable(resources, Bitmap.crea)




        val icon = BitmapFactory.decodeResource(resources, R.drawable.abc_vector_test)
        val rotatedBitmap = icon.rotate(180)

        var d: Drawable = BitmapDrawable(resources, rotatedBitmap)

        //yVals.add(Entry(hour.toFloat(), reading_temperature.toFloat(), d))
*/
    }
}
