package com.gaurav.pokemon.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.gaurav.pokemon.ui.main.screens.*
import timber.log.Timber

private const val TOTAL_TABS = 4

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return TOTAL_TABS
    }

    override fun createFragment(position: Int): Fragment {

        Timber.d("VPAdapter createFragment position %s", position)

        return when (position) {
            0 -> ExploreFragment()
            1 -> CommunityFragment()
            2 -> MyTeamFragment()
            3 -> CapturedFragment()

            else -> MainFragment()
        }
    }
}