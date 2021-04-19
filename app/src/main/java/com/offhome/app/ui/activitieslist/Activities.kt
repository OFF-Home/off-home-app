package com.offhome.app.ui.activitieslist

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.button.MaterialButtonToggleGroup.OnButtonCheckedListener
import com.offhome.app.R


class Activities : AppCompatActivity() {

    lateinit var categoryName: String
    lateinit var buttonScreens: MaterialButtonToggleGroup
    lateinit var buttonlist: Button
    lateinit var buttonmap: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activities_activity)

        // recibir nombre categoria seleccionada
        val arguments = intent.extras
        categoryName = arguments?.getString("category").toString()
        //categoryName = "Running"
        title = categoryName

        // poner botón hacia atrás
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        buttonlist = findViewById<Button>(R.id.btn_list)
        buttonmap = findViewById<Button>(R.id.btn_map)

        buttonScreens = findViewById<MaterialButtonToggleGroup>(R.id.toggleButton)

        /* cargar por defecto el listado de actividades
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ActivitiesListFragment.newInstance())
                .commitNow()
        }*/

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

        /*
        buttonScreens.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // The toggle is enabled, activities list
                if (savedInstanceState == null) {
                    buttonlist.setBackgroundColor(getColor(R.color.secondary))
                    buttonmap.setBackgroundColor(getColor(R.color.secondary_light))
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, ActivitiesListFragment.newInstance())
                        .commitNow()
                }
            } else {
                // The toggle is disabled, map with activities
                if (savedInstanceState == null) {
                    buttonmap.setBackgroundColor(getColor(R.color.secondary))
                    buttonlist.setBackgroundColor(getColor(R.color.secondary_light))
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, MapsFragment.newInstance())
                        .commitNow()
                }
            }
        }


        // esto es lo que hace que cambie de fragment, al clicar a list
        // primero cambio colores botones
        buttonlist.setOnClickListener {
            if (savedInstanceState == null) {
                buttonlist.setBackgroundColor(getColor(R.color.secondary))
                buttonmap.setBackgroundColor(getColor(R.color.secondary_light))
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ActivitiesListFragment.newInstance())
                    .commitNow()
            }
        }

        // esto es lo que hace que cambie de fragment, al clicar a map
        // primero cambio colores botones
        buttonmap.setOnClickListener {
            if (savedInstanceState == null) {
                buttonmap.setBackgroundColor(getColor(R.color.secondary))
                buttonlist.setBackgroundColor(getColor(R.color.secondary_light))
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MapsFragment.newInstance())
                    .commitNow()
            }
        }*/
    }
}
