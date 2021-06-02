package com.offhome.app



import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.GsonBuilder
import com.offhome.app.data.profilejson.AchievementList
import com.offhome.app.ui.achievements.AuxShowAchievementSnackbar
import com.offhome.app.ui.createactivity.CreateActivity

class MainActivity : AppCompatActivity() {

    private lateinit var layout:ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_activities,
                R.id.navigation_explore,
                R.id.navigation_chats,
                R.id.navigation_profile
            )
        )

        val buttonCreate = findViewById<FloatingActionButton>(R.id.buttonCreateActivity)

        buttonCreate.setOnClickListener {
            val intent = Intent(this, CreateActivity::class.java)
            startActivity(intent)
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        layout = findViewById(R.id.container)

        if (intent.extras != null && intent.extras!!.getString("achievement_list") != null) { // si tenim intent.extras (és a dir, venim de createActivity i ens ha passat achievements)
            Log.d("mainActvty Achievements", "MainActivity: tinc intent.extra. vaig a snackbar")

            val arguments = intent.extras
            val achieveString = arguments?.getString("achievement_list")

            val achievements = GsonBuilder().create().fromJson(achieveString, AchievementList::class.java)

            val auxSnack = AuxShowAchievementSnackbar()
            auxSnack.showAchievementSnackbarObject(layout, this, achievements.result)
        }
    }
}
