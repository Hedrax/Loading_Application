package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
class MainActivity : AppCompatActivity() {

    private var fileName: String = ""
    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private lateinit var redio_Group: ViewGroup
    private lateinit var downloadManager:DownloadManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        redio_Group = findViewById(R.id.radioGroup)
        notificationManager =ContextCompat.getSystemService(
                this,NotificationManager::class.java) as NotificationManager

        createChannel(
            channelID = getString(R.string.notification_ID_Channel),
            channelName = getString(R.string.notification_NAME_Channel)
        )

        loadingButton.setOnClickListener {
            val buttonSelected = radioGroup!!.checkedRadioButtonId
            if (radioGroup!!.checkedRadioButtonId == -1)
                Toast.makeText(applicationContext,"please pick a choice",
                    Toast.LENGTH_SHORT).show()
             else {
                     loadingButton.buttonState(ButtonState.Loading)
                     pickingSelectedFile(buttonSelected)
            }
        }

    }

    private fun createChannel(channelID: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelID,channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {setShowBadge(false)}
            channel.enableVibration(true)
            channel.description = getString(R.string.notification_description)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun pickingSelectedFile(id:Int) {
        try {


            when (id) {
                R.id.loadApp_Radio_Button -> {
                    fileName = "LoadApp"
                    download(URL_LOADAPP)
                }
                R.id.retrofit_Radio_Button -> {
                    fileName = "Retrofit"
                    download(URL_RETROFIT)
                }
                R.id.glide_Radio_button -> {
                    fileName = "Glide"
                    download(URL_GLIDE)
                }
            }
        }

        catch (e:java.lang.Exception){
            Log.i("lol", e.toString())
        }
    }
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        context?.let {
            notificationManager.send(
                getString(R.string.notification_description),
                it, DownloadState(id!!), fileName
            )
        }
        loadingButton.buttonState(ButtonState.Completed)
    }
    }

    private fun DownloadState(Ref: Long): String {
        val query = DownloadManager.Query()
        query.setFilterById(Ref)
        val cursor: Cursor = downloadManager.query(query)
        if (cursor.moveToFirst()) {
            val colIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
            val status = cursor.getInt(colIndex)
            return when (status) {
                DownloadManager.STATUS_FAILED -> DownloadStatus.FALIED
                DownloadManager.STATUS_PAUSED -> DownloadStatus.PAUSED
                DownloadManager.STATUS_PENDING -> DownloadStatus.PENDING
                DownloadManager.STATUS_RUNNING -> DownloadStatus.RUNNING
                DownloadManager.STATUS_SUCCESSFUL -> DownloadStatus.SUCCESSFUL
                else -> DownloadStatus.NOSTATUS
            }.toString()
        }
        return DownloadStatus.NOSTATUS.toString()
    }
    private fun download(URL:String) {
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID = downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }
}
