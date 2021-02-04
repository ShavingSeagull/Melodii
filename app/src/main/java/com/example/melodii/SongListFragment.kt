package com.example.melodii

import android.os.Bundle
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