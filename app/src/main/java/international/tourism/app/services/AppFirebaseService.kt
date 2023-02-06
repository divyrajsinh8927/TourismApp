package international.tourism.app.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import international.tourism.app.R
import international.tourism.app.utils.ApiRequest
import okhttp3.OkHttpClient
import okhttp3.Request

class AppFirebaseService : FirebaseMessagingService()
{
    override fun onNewToken(token: String)
    {
        val sharedPref = getSharedPreferences("tourism_pref", MODE_PRIVATE)
        val id = sharedPref.getString("id", null)
        Log.d("Token", token)
        val request = Request.Builder().get().url(ApiRequest.BASE_URL.plus("/firebase/add-token.php?id=${id}&token=${token}")).build()
        OkHttpClient().newCall(request).execute()
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
}