package com.example.melodii

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class MenuAdapter(context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val mContext = context

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return SongFragment()
            //1 -> return ArtistsFragment()
            //2 -> return GenreFragment()
            // May add playlists in as a fragment later
            // Sets Songs as the default because it is the first tab. Should never hit the default!
            else -> return SongFragment()
        }
    }

    override fun getCount(): Int {
        return 1
    }

    // Likely won't need this!
//    override fun getPageTitle(position: Int): CharSequence? {
//        when (position) {
//            0 -> return mContext.getString(R.string.continent_americas)
//            1 -> return mContext.getString(R.string.continent_europe)
//            2 -> return mContext.getString(R.string.continent_asia_oceania)
//            3 -> return mContext.getString(R.string.continent_africa)
//            // Sets Americas as the default because it is the first tab. Should never hit the default!
//            else -> return mContext.getString(R.string.continent_americas)
//        }
//    }
}