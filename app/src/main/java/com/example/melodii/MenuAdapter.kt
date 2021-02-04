package com.example.melodii

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class MenuAdapter(context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val mContext = context

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> SongListFragment()
            1 -> SongListFragment()
            2 -> SongListFragment()
            3 -> SongListFragment()
            // May add playlists in as a fragment later
            // Sets Songs as the default because it is the first tab. Should never hit the default!
            else -> SongListFragment()
        }
    }

    override fun getCount(): Int {
        return 4
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return mContext.getString(R.string.artist_tab)
            1 -> return mContext.getString(R.string.song_tab)
            2 -> return mContext.getString(R.string.album_tab)
            3 -> return mContext.getString(R.string.genre_tab)
            // Sets Americas as the default because it is the first tab. Should never hit the default!
            else -> return mContext.getString(R.string.song_tab)
        }
    }
}