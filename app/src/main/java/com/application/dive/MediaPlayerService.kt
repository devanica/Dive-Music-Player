package com.application.dive

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.media.MediaPlayer.OnPreparedListener
import android.os.Build
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.io.IOException

class MediaPlayerService : Service(),
                           OnCompletionListener,
                           OnPreparedListener,
                           MediaPlayer.OnErrorListener {

    private lateinit var track: Track

    companion object {
        lateinit var player: MediaPlayer
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        player = MediaPlayer()
        player.setOnPreparedListener(this)
        player.setOnCompletionListener(this)
        player.setOnErrorListener(this)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        track = intent.getParcelableExtra("track")
        playTrack(track.id, applicationContext)
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Dive")
            .setContentText(track.trackName)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        val notification: Notification = builder.build()
        startForeground(notificationId, notification)

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCompletion(mediaPlayer: MediaPlayer) {

    }

    override fun onError(mediaPlayer: MediaPlayer, i: Int, i1: Int): Boolean {
        return false
    }

    override fun onPrepared(mediaPlayer: MediaPlayer) {
        player.start()
        Log.v("player", "start")
    }

    fun playTrack(l: Long, context: Context?) {
        try {
            // Return to idle state.
            player.reset()
            Log.v("player", "reset")
            // Here the player object we created earlier is initialized once we call setDataSource.
            player.setDataSource(
                context!!,
                ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    l
                )
            )
            // Prepare method must be called before the start method.
            player.prepare()
        } catch (e: IOException) {
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_LOW

            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        // Unregister since the activity is about to be closed.
        // LocalBroadcastManager.getInstance(this).unregisterReceiver(onTrackSelect);
        super.onDestroy()
    }
}