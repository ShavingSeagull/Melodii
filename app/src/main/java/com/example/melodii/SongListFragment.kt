package com.example.melodii

import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * A fragment representing a list of Items.
 */
class SongListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_song_list, container, false)

        data class Song(
            val uri: Uri,
            val name: String
        )

        val songList = mutableListOf<Song>()

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
            MediaStore.Audio.Media.DISPLAY_NAME
        )
        val sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"

        val songs = requireActivity().contentResolver.query(
            collection,
            projection,
            null,
            null,
            sortOrder
        )
        songs?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)

                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                songList += Song(contentUri, name)
            }
        }

        for (i in songList) {
            Log.d("MUSIC SONG", i.name)
        }

        // Dummy data list
        val dummyList = generateDummyList(100)
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                adapter = SongRecyclerViewAdapter(dummyList)
                layoutManager = LinearLayoutManager(this.context)
            }
        }
        return view
    }

    private fun generateDummyList(size: Int): List<CardItem> {
        val list = ArrayList<CardItem>()

        for (i in 0 until size) {
            val drawable = R.drawable.ic_baseline_music_note_24
            val item = CardItem(drawable, "Song $i", "Subtitle")
            list += item
        }
        return list
    }
}