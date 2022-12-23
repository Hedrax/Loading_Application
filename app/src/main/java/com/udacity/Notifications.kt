package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

private var ID:Int =  0

fun NotificationManager.send(message: String,applicationContext: Context,status: String,fileName: String)
{
    val intent = Intent(applicationContext, DetailActivity::class.java)
    intent.putExtra("FileNameEXTRA", fileName)
    intent.putExtra("DownloadStatus", status)
    val contentIntent = intent
    val contentPendingIntent = PendingIntent
        .getActivity(applicationContext,ID,contentIntent,PendingIntent.FLAG_UPDATE_CURRENT)
    // Build the notification
    val builder = NotificationCompat.Builder(applicationContext,applicationContext.getString(R.string.notification_ID_Channel))
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentIntent(contentPendingIntent)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setAutoCancel(true)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .addAction(
            R.drawable.ic_assistant_black_24dp,
            applicationContext.getString(R.string.notification_button),
            contentPendingIntent)
    notify(ID, builder.build())
}