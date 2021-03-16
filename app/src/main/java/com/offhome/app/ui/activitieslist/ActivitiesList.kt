package com.offhome.app.ui.activitieslist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.offhome.app.R

class ActivitiesList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activities_list)

        val imgLike = findViewById<ImageView>(R.id.imageViewIconLike)
        imgLike.setOnClickListener {
         Toast.makeText(this, "hola", Toast.LENGTH_SHORT).show()
        }
    }
}