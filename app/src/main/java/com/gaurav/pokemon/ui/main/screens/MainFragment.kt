package com.gaurav.pokemon.ui.main.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.gaurav.pokemon.databinding.FragmentMainBinding
import com.gaurav.pokemon.ui.main.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

/**
 * A [Fragment] subclass which displays the Main Screen of the app and contains the viewpager as well
 */
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    // tab titles
    private val titles = arrayOf(TAB_EXPLORE, TAB_COMMUNITY, TAB_MYTEAM, TAB_CAPTURED)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager = binding.pager
        val tabLayout = binding.tabLayout

        activity?.let {

            val viewPagerAdapter = ViewPagerAdapter(it.supportFragmentManager, it.lifecycle)
            viewPager.adapter = viewPagerAdapter

            // attaching tab mediator
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = titles[position]
            }.attach()

            it.onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    when {
                        viewPager.currentItem == 0 -> {
                            it.finish()
                        }
                        viewPager.currentItem == 1 -> {
                            viewPager.currentItem = 0
                        }
                        viewPager.currentItem == 2 -> {
                            viewPager.currentItem = 0
                        }
                        viewPager.currentItem == 3 -> {
                            viewPager.currentItem = 0
                        }
                        else -> {
                            it.finish()
                        }
                    }
                }
            })
        }
    }

    companion object {
        const val TAB_EXPLORE = "Explore"
        const val TAB_COMMUNITY = "Community"
        const val TAB_MYTEAM = "MyTeam"
        const val TAB_CAPTURED = "Captured"
    }

}