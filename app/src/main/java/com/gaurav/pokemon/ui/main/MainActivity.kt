package com.gaurav.pokemon.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gaurav.pokemon.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

/**
 *
 * MainActivity - The Launcher activity which is first loaded into the app
 *
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // tab titles
    private val titles = arrayOf("Explore", "Community", "MyTeam", "Captured")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager = binding.pager
        val tabLayout = binding.tabLayout

        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = viewPagerAdapter

        // attaching tab mediator
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }
}