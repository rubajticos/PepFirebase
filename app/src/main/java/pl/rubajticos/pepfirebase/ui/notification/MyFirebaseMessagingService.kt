package pl.rubajticos.pepfirebase.ui.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import pl.rubajticos.pepfirebase.R
import pl.rubajticos.pepfirebase.ui.screen.BOOK_DETAILS_URI
import pl.rubajticos.pepfirebase.ui.screen.MainActivity
import timber.log.Timber

const val NOTIFICATION_BOOK_ID = "bookId"
const val NOTIFICATION_TITLE = "title"
const val NOTIFICATION_TEXT = "text"
const val CHANNEL = "test_channel"


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("MRMR New token! $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Timber.d("MRMR Notification received ! ${message.data}")
        val bookId = message.data[NOTIFICATION_BOOK_ID] ?: ""
        val notificationTitle = message.data[NOTIFICATION_TITLE].orEmpty()
        val notificationText = message.data[NOTIFICATION_TEXT].orEmpty()

        val bookDetailIntent = Intent(
            Intent.ACTION_VIEW,
            "$BOOK_DETAILS_URI$bookId".toUri(),
            applicationContext,
            MainActivity::class.java
        )
        val openBookIntent: PendingIntent = TaskStackBuilder.create(applicationContext).run {
            addNextIntentWithParentStack(bookDetailIntent)
            getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            registerNotificationChannel()

        val notification = NotificationCompat.Builder(this, CHANNEL)
            .setSmallIcon(R.drawable.ic_baseline_menu_book_24)
            .setContentTitle(notificationTitle)
            .setContentText(notificationText)
            .setContentIntent(openBookIntent)
            .setPriority(NotificationCompat.PRIORITY_MAX)

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notification.build())

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun registerNotificationChannel() {
        val id = CHANNEL
        val name = CHANNEL
        val description = CHANNEL
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(id, name, importance)
        channel.description = description
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}