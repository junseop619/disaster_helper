package com.example.disaster_helper.Setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.disaster_helper.R
import com.example.disaster_helper.databinding.FragmentSettingBinding


class SettingFragment : Fragment() {

    private lateinit var  binding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentSettingBinding.inflate(inflater, container, false)

        return binding.root
    }

}