package com.offhome.app.ui.achievements

import android.content.Context
import android.graphics.drawable.Drawable
import android.provider.Settings.Global.getString
import android.view.View
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.marginLeft
import com.google.android.material.snackbar.Snackbar
import com.offhome.app.R

class AuxShowAchievementSnackbar {

    fun showAchievementSnackbar(passLayout: View, passContext: Context, string:String) {
        val achievementSnackbar = Snackbar.make(
            passLayout,
            string,
            Snackbar.LENGTH_LONG
        )
        val imageView = ImageView(passContext)
        val drawable2: Drawable?
        if (string.contains("DIAMOND", true)) {
            drawable2 = ResourcesCompat.getDrawable(passContext.resources, R.drawable.trophy_diamond, passContext.theme)
        }
        else if (string.contains("PLATINUM", true)) {
            drawable2 = ResourcesCompat.getDrawable(passContext.resources, R.drawable.trophy_platinum, passContext.theme)
        }
        else if (string.contains("GOLD", true)) {
            drawable2 = ResourcesCompat.getDrawable(passContext.resources, R.drawable.trophy_gold, passContext.theme)
        }
        else if (string.contains("SILVER", true)) {
            drawable2 = ResourcesCompat.getDrawable(passContext.resources, R.drawable.trophy_silver, passContext.theme)
        }
        else {
            drawable2 = ResourcesCompat.getDrawable(passContext.resources, R.drawable.trophy_bronze, passContext.theme)
        }

        imageView.setImageDrawable(drawable2)
        imageView.id = R.id.trophy_one

        val snackbarView = achievementSnackbar.view as Snackbar.SnackbarLayout
        snackbarView.addView(imageView)
        snackbarView.findViewById<ImageView>(R.id.trophy_one).marginLeft      //.foregroundGravity = Gravity.END

        achievementSnackbar.show()
    }
}
