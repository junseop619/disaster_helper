package com.example.disaster_helper

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.disaster_helper.Home.HomeFragment
import com.example.disaster_helper.search.SearchFragment
import com.example.disaster_helper.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    lateinit var navigationView: NavigationView
    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getKeyHash2()

        val homeFragment = HomeFragment()
        val searchFragment = SearchFragment()

        val bottomNavigationView = binding.navigationView

        replaceFragment(homeFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                //home
                R.id.homeFragment -> { replaceFragment(homeFragment) }

                //search
                R.id.searchFragment -> replaceFragment(searchFragment)
            }
            true
        }
    }

    /*
    fun getKeyHash() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val packageInfo = requireContext().packageManager.getPackageInfo(requireContext().packageName, PackageManager.GET_SIGNING_CERTIFICATES)
            for (signature in packageInfo.signingInfo.apkContentsSigners) {
                try {
                    val md = MessageDigest.getInstance("SHA")
                    md.update(signature.toByteArray())
                    Log.d("getKeyHash", "key hash: ${Base64.encodeToString(md.digest(), Base64.NO_WRAP)}")
                } catch (e: NoSuchAlgorithmException) {
                    Log.w("getKeyHash", "Unable to get MessageDigest. signature=$signature", e)
                }
            }
        }
    }*/

    fun getKeyHash2() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                val md = MessageDigest.getInstance("SHA")
                md.update(packageName.toByteArray())
                Log.d("getKeyHash", "key hash: ${Base64.encodeToString(md.digest(), Base64.NO_WRAP)}")
            } catch (e: NoSuchAlgorithmException) {
                Log.w("getKeyHash", "Unable to get MessageDigest.", e)
            }
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