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
class GenreListFragment : Fragment() {

    // Data class for each genre in the recyclerview
    data class Genre(
        val uri: Uri,
        val genre: String
    )

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_song_list, container, false)



        val genreClassList = mutableListOf<Genre>()

        val collection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Audio.Genres.getContentUri(
                    MediaStore.VOLUME_EXTERNAL
                )
            } else {
                MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI
            }

        val projection = arrayOf(
            MediaStore.Audio.Genres._ID,
            MediaStore.Audio.Genres.NAME
        )
        val sortOrder = "${MediaStore.Audio.Genres.NAME} ASC"

        val genres = requireActivity().contentResolver.query(
            collection,
            projection,
            null,
            null,
            sortOrder
        )
        genres?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Genres._ID)
            val genreColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.NAME)

            val genreArtist = mutableListOf<String>()

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val genre = cursor.getString(genreColumn)

                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                if (genre !in genreArtist && isCharacter(genre[0])) {
                    genreClassList += Genre(contentUri, genre)
                }
            }
        }

        // List of genres on device
        val genreList = addGenresToList(genreClassList)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                adapter = SongRecyclerViewAdapter(genreList)
                layoutManager = LinearLayoutManager(this.context)
            }
        }
        return view
    }

    // Adds genres from the device of the Genre class into a list of CardItems for the recyclerview
    private fun addGenresToList(genreList: MutableList<Genre>): List<CardItem> {
        val list = ArrayList<CardItem>()

        for (i in genreList) {
            val drawable = R.drawable.ic_baseline_music_note_24
            val item = CardItem(drawable, i.genre, null)
            list += item
        }
        return list
    }

    private fun isCharacter(char: Char): Boolean {
        return char in "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstyvwxyz1234567890"
    }
}