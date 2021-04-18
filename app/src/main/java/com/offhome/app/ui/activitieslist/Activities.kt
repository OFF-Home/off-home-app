package com.offhome.app.ui.activitieslist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ToggleButton
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.RoundedCornerTreatment
import com.google.android.material.shape.ShapePathModel
import com.offhome.app.R

class Activities : AppCompatActivity() {

    lateinit var categoryName: String
    //lateinit var buttonScreens: ToggleButton
    lateinit var buttonlist: Button
    lateinit var buttonmap: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activities_activity)

        //recibir nombre categoria seleccionada
        val arguments = intent.extras
        //categoryName = arguments?.getString("category").toString()
        categoryName = "Running"
        title = categoryName

        //poner botón hacia atrás
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        buttonlist = findViewById<Button>(R.id.btn_list)
        buttonmap = findViewById<Button>(R.id.btn_map)

        //buttonScreens = findViewById<ToggleButton>(R.id.toggleButton)

        //esto es lo que hace que cambie de fragment, al clicar a list
        //primero cambio colores botones
        buttonlist.setOnClickListener {
            if (savedInstanceState == null) {
                buttonlist.setBackgroundColor(getColor(R.color.secondary))
                buttonmap.setBackgroundColor(getColor(R.color.secondary_light))
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ActivitiesListFragment.newInstance())
                    .commitNow()
            }
        }

        //esto es lo que hace que cambie de fragment, al clicar a map
        //primero cambio colores botones
        buttonmap.setOnClickListener {
            if (savedInstanceState == null) {
                buttonmap.setBackgroundColor(getColor(R.color.secondary))
                buttonlist.setBackgroundColor(getColor(R.color.secondary_light))
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MapsFragment.newInstance())
                    .commitNow()
            }
        }
    }
}