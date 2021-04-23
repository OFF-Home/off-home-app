package com.offhome.app.ui.profile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.offhome.app.R
import com.offhome.app.model.ActivityFromList

class ProfileMyActivitiesFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileMyActivitiesFragment()
    }

    private lateinit var viewModel: ProfileMyActivitiesViewModel
    private lateinit var profileVM:ProfileFragmentViewModel
    private var activitiesList: List<ActivityFromList> = ArrayList()
    private lateinit var myActivitiesRecycler: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_my_activities_fragment, container, false)

        rotateArrowDrawables()

        val profileFragment: ProfileFragment = parentFragment as ProfileFragment
        profileVM = profileFragment.getViewModel()
        profileVM.myActivities.observe(
            viewLifecycleOwner,
            Observer {
                val myActivitiesVM = it ?: return@Observer

                //copiat de ActivitiesList
                Log.d("MyActivities", "my activities got to the fragment")
                activitiesList = myActivitiesVM
                //myActivitiesRecycler.setDa
            })

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileMyActivitiesViewModel::class.java)
        // TODO: Use the ViewModel
    }

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
