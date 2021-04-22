package com.offhome.app.ui.activitieslist

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.button.MaterialButtonToggleGroup.OnButtonCheckedListener
import com.offhome.app.R


/**
 * Class *Activities*
 * This class is the one that displays the information about an Activity, using the List and Map Fragments
 * @author Emma Pereira
 * @property categoryName references the name of the category of which we want to display the activities
 * @property buttonScreens references the Material Toggle Group Button that allows us to change between fragments
 * @property buttonlist references the Button to display the list of activities
 * @property buttonmap references the Button to display the map with activities
 */
class Activities : AppCompatActivity() {

    lateinit var categoryName: String
    lateinit var buttonScreens: MaterialButtonToggleGroup
    lateinit var buttonlist: Button
    lateinit var buttonmap: Button

    /**
     * This is executed when the activity is launched for the first time or created again.
     * @param savedInstanceState is the instance of the saved State of the activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activities_activity)

        // recibir nombre categoria seleccionada
        val arguments = intent.extras
        categoryName = arguments?.getString("category").toString()
        title = categoryName

        // poner botón hacia atrás
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        buttonlist = findViewById<Button>(R.id.btn_list)
        buttonmap = findViewById<Button>(R.id.btn_map)

        buttonScreens = findViewById<MaterialButtonToggleGroup>(R.id.toggleButton)

        // cargar por defecto el listado de actividades
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ActivitiesListFragment.newInstance())
                .commitNow()
        }

        //cambiar fragment entre list y map
        buttonScreens.addOnButtonCheckedListener(OnButtonCheckedListener { buttonScreens, checkedId, isChecked ->
            if (buttonScreens.checkedButtonId == R.id.btn_list) {
                //Place code related to button1 here
                if (savedInstanceState == null) {
                    buttonlist.setBackgroundColor(getColor(R.color.secondary))
                    buttonmap.setBackgroundColor(getColor(R.color.secondary_light))
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, ActivitiesListFragment.newInstance())
                        .commitNow()
                }

            } else if (buttonScreens.checkedButtonId == R.id.btn_map) {
                //Place code related to button 2 here
                if (savedInstanceState == null) {
                    buttonmap.setBackgroundColor(getColor(R.color.secondary))
                    buttonlist.setBackgroundColor(getColor(R.color.secondary_light))
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, MapsFragment.newInstance())
                        .commitNow()
                }
            }
        })
    }
}
