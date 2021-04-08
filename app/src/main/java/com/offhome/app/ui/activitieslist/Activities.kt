package com.offhome.app.ui.activitieslist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.offhome.app.R

class Activities : AppCompatActivity() {

    lateinit var categoryName: String

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

        if (savedInstanceState == null) {
            //esto es lo que hace que cambie de fragment, usarlo para el boton de list - map
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MapsFragment.newInstance())
                .commitNow()
        }
    }
}