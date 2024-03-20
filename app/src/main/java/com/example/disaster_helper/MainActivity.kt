package com.example.disaster_helper

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.disaster_helper.Home.HomeFragment
import com.example.disaster_helper.Search.SearchFragment
import com.example.disaster_helper.Setting.SettingFragment
import com.example.disaster_helper.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    lateinit var navigationView: NavigationView
    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val homeFragment = HomeFragment()
        val searchFragment = SearchFragment()
        val settingFragment = SettingFragment()

        val bottomNavigationView = binding.navigationView

        replaceFragment(homeFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                //home
                R.id.homeFragment -> { replaceFragment(homeFragment) }

                //search
                R.id.searchFragment -> replaceFragment(searchFragment)


                //setting
                R.id.settingFragment -> replaceFragment(settingFragment)
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction() // 트랜젝션 : 작업을 시작한다고 알려줌;
            .apply {
                replace(R.id.fragmentContainer, fragment)
                commit() // 트랜잭션 끝.
            }
    }
}