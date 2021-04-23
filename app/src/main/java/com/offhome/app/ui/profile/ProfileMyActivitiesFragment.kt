package com.offhome.app.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.offhome.app.R
import com.offhome.app.model.ActivityFromList
import com.offhome.app.ui.activitieslist.ActivitiesListRecyclerViewAdapter

class ProfileMyActivitiesFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileMyActivitiesFragment()
    }

    private lateinit var viewModel: ProfileMyActivitiesViewModel        //TODO té pinta que la classe ProfileMyActivitiesViewModel la borraré i faré servir ProfileFragmentViewModel
    private lateinit var profileVM:ProfileFragmentViewModel             //fem servir el viewModel de Profil
    private var activitiesList: List<ActivityFromList> = ArrayList()
    private lateinit var activitiesListAdapter: ActivitiesListRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_my_activities_fragment, container, false)

        rotateArrowDrawables()

        //tot lo del recycler ho he robat descaradament de ActivitiesList
        activitiesListAdapter = ActivitiesListRecyclerViewAdapter()
        val recyclerView = view.findViewById<RecyclerView>(R.id.RecyclerViewProfileActivities)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = activitiesListAdapter

        val profileFragment: ProfileFragment = parentFragment as ProfileFragment
        profileVM = profileFragment.getViewModel()
        profileVM.myActivities.observe(
            viewLifecycleOwner,
            Observer {
                val myActivitiesVM = it ?: return@Observer
                //copiat de ActivitiesList
                Log.d("MyActivities", "my activities got to the fragment")
                activitiesList = myActivitiesVM

                activitiesListAdapter.setData(activitiesList)
                //com que això ho he copiat nose si se li assigna un listener...
            })

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileMyActivitiesViewModel::class.java)
    }

    //no ho vaig aconseguir
    private fun rotateArrowDrawables() {/*
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
