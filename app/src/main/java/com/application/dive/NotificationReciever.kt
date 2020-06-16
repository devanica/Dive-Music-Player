package com.application.dive

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.application.dive.MediaPlayerService.Companion.player

class NotificationReceiver : BroadcastReceiver() {
    var closeIntent = Intent("close_app")
    override fun onReceive(context: Context, intent: Intent) {
        player.stop()
        LocalBroadcastManager.getInstance(context).sendBroadcast(closeIntent)
    }
}
