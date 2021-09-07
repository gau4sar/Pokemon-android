package com.gaurav.pokemon.ui.main.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gaurav.pokemon.databinding.FragmentMainBinding
import com.gaurav.pokemon.ui.main.ViewPagerAdapter
import com.gaurav.pokemon.utils.EncryptPrefUtils
import com.gaurav.pokemon.utils.GeneralUtils
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.ext.android.inject
import timber.log.Timber

/**
 * A [Fragment] subclass which displays the Main Screen of the app and contains the viewpager as well
 */
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    // tab titles
    private val titles = arrayOf("Explore", "Community", "MyTeam", "Captured")

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
        }

    }

}