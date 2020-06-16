package com.application.dive

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private var tracks = ArrayList<Track>()

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getTracksFromStorage()

        rv_tracks.setLayoutManager(LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false))
        val trackListAdapter = TrackListAdapter(tracks) {
            //playTrack(it)
        }
        rv_tracks.setAdapter(trackListAdapter)
    }

    // Get tracks from internal storage
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun getTracksFromStorage() {
        tracks = ArrayList<Track>()
        val contentResolver = applicationContext.contentResolver
        val cursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
            MediaStore.Audio.Media.IS_MUSIC, null,
            MediaStore.Audio.Media.DEFAULT_SORT_ORDER)

        var trackName: String?
        var artistName: String?
        var trackDuration: String
        var trackId: Long

        if (cursor != null && cursor.moveToFirst()) {
            do {
                trackId = cursor.getLong(cursor.
                getColumnIndex(MediaStore.Audio.Media._ID))
                trackName = cursor.getString(cursor.
                getColumnIndex(MediaStore.Audio.Media.TITLE))
                artistName = cursor.getString(cursor.
                getColumnIndex(MediaStore.Audio.Media.ARTIST))
                trackDuration = cursor.getString(cursor.
                getColumnIndex(MediaStore.Audio.Media.DURATION))
                val duration = trackDuration
                val converted = duration.toInt()
                val mns = converted / 60000 % 60000
                val scs = converted % 60000 / 1000
                @SuppressLint("DefaultLocale") val songDuration =
                    String.format("%02d:%02d", mns, scs)
                tracks.add(Track(trackId, trackName, artistName, songDuration))
            } while (cursor.moveToNext())
            cursor.close()
        } else {
            Toast.makeText(applicationContext, "You've got 0 tracks", Toast.LENGTH_SHORT)
                .show()
        }
    }

}
