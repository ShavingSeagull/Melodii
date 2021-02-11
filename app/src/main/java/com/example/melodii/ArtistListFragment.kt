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
class ArtistListFragment : Fragment() {

    // Data class for each song in the recyclerview
    data class Artist(
        val uri: Uri,
        val artist: String
    )

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_song_list, container, false)



        val artistClassList = mutableListOf<Artist>()

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
            MediaStore.Audio.Media.ARTIST
        )
        val sortOrder = "${MediaStore.Audio.Media.ARTIST} ASC"

        val songs = requireActivity().contentResolver.query(
            collection,
            projection,
            null,
            null,
            sortOrder
        )
        songs?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)

            val artistArray = mutableListOf<String>()

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val artist = cursor.getString(artistColumn)

                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                if (artist !in artistArray) {
                    artistClassList += Artist(contentUri, artist)
                    artistArray += artist
                }
            }
        }

        // List of songs on device
        val artistList = addArtistsToList(artistClassList)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                adapter = SongRecyclerViewAdapter(artistList)
                layoutManager = LinearLayoutManager(this.context)
            }
        }
        return view
    }

    // Adds songs from the device of the Song class into a list of CardItems for the recyclerview
    private fun addArtistsToList(artistList: MutableList<Artist>): List<CardItem> {
        val list = ArrayList<CardItem>()

        for (i in artistList) {
            val drawable = R.drawable.ic_baseline_music_note_24
            val item = CardItem(drawable, i.artist, null)
            list += item
        }
        return list
    }
}