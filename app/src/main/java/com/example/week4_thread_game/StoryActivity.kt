package com.example.week4_thread_game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.week4_thread_game.adapter.MainViewPagerAdapter
import com.example.week4_thread_game.databinding.ActivityStoryBinding

class StoryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityStoryBinding

    private lateinit var mMainViewPager : MainViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPager()
    }

    private fun viewPager(){
        mMainViewPager = MainViewPagerAdapter(this)
        binding.viewPager.adapter = mMainViewPager
        binding.viewPager.offscreenPageLimit = 5

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.progressBar.progress = position + 1
            }
        })

    }
}