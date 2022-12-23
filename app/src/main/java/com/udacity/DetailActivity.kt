package com.udacity

import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class DetailActivity : AppCompatActivity() {
    private lateinit var notificationManager: NotificationManager
    private lateinit var textFilename: TextView
    private lateinit var textStatus: TextView
    private lateinit var okButton: Button
    private lateinit var status: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        notificationManager =
            ContextCompat.getSystemService(
                this,
                NotificationManager::class.java
            ) as NotificationManager
        notificationManager.cancelAll()
        textFilename = findViewById(R.id.detail_fileName)
        textStatus = findViewById(R.id.detail_status)

        okButton = findViewById(R.id.button)
        val extras = intent.extras
        if (extras != null) {
            status = extras.getString("DownloadStatus")!!
            textFilename.text = extras.getString("FileNameEXTRA")!!
            textStatus.text = status
            if (status == DownloadStatus.SUCCESSFUL.toString())
                textStatus.setTextColor(Color.GREEN)
            else
                textStatus.setTextColor(Color.RED)
        } else {
            textStatus.text = DownloadStatus.NOSTATUS.toString()
            textFilename.text = DownloadStatus.NOSTATUS.toString()
        }
        okButton.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
    }

}
