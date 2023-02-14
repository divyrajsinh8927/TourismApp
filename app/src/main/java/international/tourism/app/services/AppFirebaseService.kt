package international.tourism.app.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import international.tourism.app.R

class AppFirebaseService : FirebaseMessagingService()
{
    private lateinit var database: SQLiteDatabase
    override fun onNewToken(token: String)
    {
        database = configureDatabase()

        val query = "INSERT INTO userToken (Token) VALUES (?)"
        database.execSQL(query, arrayOf(token))

    }

    override fun onMessageReceived(message: RemoteMessage)
    {
        val title = message.notification!!.title
        val body = message.notification!!.body

        val channelId = "fcm_default_channel"
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val channel = NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notification)
    }
    private fun configureDatabase(): SQLiteDatabase
    {
        val database = openOrCreateDatabase("ShivTourism_db", MODE_PRIVATE, null)
        database.execSQL("CREATE TABLE IF NOT EXISTS userToken (Id INTEGER PRIMARY KEY AUTOINCREMENT, Token TEXT NOT NULL)")

        return database
    }
}