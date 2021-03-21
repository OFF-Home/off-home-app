package com.offhome.app.data

import com.offhome.app.model.Activity

class ActivityDataSet {

    fun createActivitiesList(): List<Activity>{
        return listOf(Activity(123, "Running in La Barceloneta", "12/03/2021", "La Barceloneta, Barcelona, Spain"),
        Activity(1234, "Yoga in Plaça Catalunya", "16/03/2021", "Plaça Catalunya, Barcelona, Spain"))
    }
}