package com.example.disaster_helper.Search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.disaster_helper.R
import com.example.disaster_helper.databinding.FragmentSearchBinding
import com.example.disaster_helper.databinding.FragmentSettingBinding
import net.daum.mf.map.api.MapView

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentSearchBinding.inflate(inflater, container, false)

        val mapView = MapView(context)
        binding.mapView.addView(mapView)

        return binding.root
    }
}