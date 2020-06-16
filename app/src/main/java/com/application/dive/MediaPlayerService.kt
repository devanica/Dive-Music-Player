package com.application.dive

import android.app.PendingIntent
import android.app.Service
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.media.MediaPlayer.OnPreparedListener
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
    private lateinit var notificationManagerCompat: NotificationManagerCompat

    companion object {
        lateinit var player: MediaPlayer
    }

    override fun onCreate() {
        super.onCreate()
        notificationManagerCompat =
            NotificationManagerCompat.from(this)

        player = MediaPlayer()
        player.setOnPreparedListener(this)
        player.setOnCompletionListener(this)
        player.setOnErrorListener(this)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        track = intent.getParcelableExtra("track")
        playTrack(track.id, applicationContext)
        triggerNotificationChannelOne(track)

        return super.onStartCommand(intent, flags, startId)
    }

    private fun triggerNotificationChannelOne(track: Track?) {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val broadcastIntent = Intent(this, NotificationReceiver::class.java)
        broadcastIntent.putExtra("track", track)
        val closeIntent =
            PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification =
            NotificationCompat.Builder(this, NOTIF_CHANNEL_ID)
                .setContentTitle("Dive")
                .setContentText(track!!.trackName)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_launcher_foreground, "stop", closeIntent)
                .build()
        notificationManagerCompat.notify(1, notification)
    }

    override fun onCompletion(mediaPlayer: MediaPlayer) {}
    override fun onError(mediaPlayer: MediaPlayer, i: Int, i1: Int): Boolean {
        return false
    }

    override fun onPrepared(mediaPlayer: MediaPlayer) {
        player.start()
        Log.v("player", "start")
    }

    override fun onDestroy() {
        // Unregister since the activity is about to be closed.
        // LocalBroadcastManager.getInstance(this).unregisterReceiver(onTrackSelect);
        super.onDestroy()
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

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}