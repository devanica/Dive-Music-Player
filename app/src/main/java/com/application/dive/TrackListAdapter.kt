package com.application.dive

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.track_list_member.view.*

internal class TrackListAdapter(
val tracks: ArrayList<Track>,
val listener: (Track) -> Unit) :
RecyclerView.Adapter<TrackListAdapter.TrackHolder>() {

    fun selectTrack(position: Int): Track {
        val selectedTrack = tracks.find { it.id == position + 1 }
        return selectedTrack!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        return TrackHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.track_list_member, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TrackHolder, position: Int) {
        holder.bind(tracks[position])
        holder.trackListMember.setOnClickListener {
            listener(selectTrack(position))
        }
    }

    class TrackHolder(view: View): RecyclerView.ViewHolder(view) {
        val trackName: TextView = view.tv_trackName
        val artistName: TextView = view.tv_artistName
        val trackDuration: TextView = view.tv_trackDuration

        val trackListMember: CardView = view.cv_trackListMember

        //TODO: GET RESOURCE SOMEHOW HERE
        @SuppressLint("SetTextI18n")
        fun bind(mTrack: Track) {
            trackName.text = mTrack.trackName
            artistName.text = mTrack.artistName
            trackDuration.text = mTrack.trackDuration
        }
    }
}