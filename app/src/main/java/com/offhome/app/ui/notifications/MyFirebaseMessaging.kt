package com.offhome.app.ui.notifications



import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.offhome.app.R
import com.offhome.app.data.model.Message
import com.offhome.app.data.model.SendMessage
import com.offhome.app.ui.chats.groupChat.GroupChatActivity

/**
 * Base class for receiving messages from Firebase Cloud Messaging.
 */
class MyFirebaseMessaging : FirebaseMessagingService() {
    lateinit var titol: String
    lateinit var message: String
    var CHANNEL_ID = "CHANNEL"

    private lateinit var manager: NotificationManager

    /**
     * Called when a message is received.
     */
    override fun onMessageReceived(remotemessage: RemoteMessage) {
        super.onMessageReceived(remotemessage)
        titol = remotemessage.data.get("title").toString()
        message = remotemessage.data.get("body").toString()

        manager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        sendNotification()
    }

    /**
     * Called when a notification is going to be send
     */
    private fun sendNotification() {
        val intent = Intent(applicationContext, GroupChatActivity::class.java)

        intent.putExtra("title", titol)
        intent.putExtra("body", message)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "pushnotification",
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(titol)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setAutoCancel(true)
            .setContentText(message)

        val pendingintent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        builder.setContentIntent(pendingintent)
        manager.notify(0, builder.build())
    }
}
