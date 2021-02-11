package com.example.melodii

import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi

/**
 * A fragment representing a list of Items.
 */
class SongListFragment : Fragment() {

    // Data class for each song in the recyclerview
    data class Song(
        val uri: Uri,
        val songName: String,
        val artist: String
    )

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_song_list, container, false)



        val songClassList = mutableListOf<Song>()

        val collection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Audio.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL
                )
            } else {
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST
        )
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

        val songs = requireActivity().contentResolver.query(
            collection,
            projection,
            null,
            null,
            sortOrder
        )
        songs?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val artist = cursor.getString(artistColumn)

                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                songClassList += Song(contentUri, name, artist)
            }
        }

        // List of songs on device
        val songList = addSongsToList(songClassList)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                adapter = SongRecyclerViewAdapter(songList)
                layoutManager = LinearLayoutManager(this.context)
            }
        }
        return view
    }

    // Adds songs from the device of the Song class into a list of CardItems for the recyclerview
    private fun addSongsToList(songList: MutableList<Song>): List<CardItem> {
        val list = ArrayList<CardItem>()

        for (i in songList) {
            val drawable = R.drawable.ic_baseline_music_note_24
            val item = CardItem(drawable, i.songName, i.artist)
            list += item
        }
        return list
    }
}