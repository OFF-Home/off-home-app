package com.offhome.app.model



import android.graphics.drawable.Icon
import com.offhome.app.model.profile.UserInfo
import java.util.*
import kotlin.collections.ArrayList

data class GroupChat(
    val title: String,
    val members: ArrayList<UserInfo>,
    val created_at: Date,
    val group_photo: Icon
)
