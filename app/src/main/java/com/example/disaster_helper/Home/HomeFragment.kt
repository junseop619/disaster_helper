package com.example.disaster_helper.Home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.disaster_helper.R
import com.example.disaster_helper.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var noticeAdapter: NoticeAdapter  //test
    private val noticeList = mutableListOf<NoticeModel>() //test
    private var binding: FragmentHomeBinding? = null //test


    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        val fragmentHomeBinding = FragmentHomeBinding.bind(view)
        binding = fragmentHomeBinding

        val noticeList = listOf(NoticeModel("title test1","text test1"),NoticeModel("title test2","text test2"))
        initializelist(noticeList)
        initNoticeRecyclerView()

        binding!!.refreshLayout.setOnRefreshListener{

            //val noticeList = listOf(NoticeModel("title test1","text test1"),NoticeModel("title test2","text test2"))


            //initializelist(noticeList)
            //initNoticeRecyclerView()

            binding!!.refreshLayout.isRefreshing=false
        }

        val data: Array<String> = resources.getStringArray(R.array.disaster_kind)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, data)
        binding!!.frameSpinner.adapter = adapter
        binding!!.frameSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                /*
                if (position != 0) {
                    Toast.makeText(requireContext(), data[position], Toast.LENGTH_SHORT).show()
                }*/

                when(position){
                    0 -> {
                        binding!!.disasterDisplay.text = "1단계"
                    }

                    1 -> {
                        binding!!.disasterDisplay.text = "2단계"
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

    }

    fun initNoticeRecyclerView(){
        val noticeAdapter= NoticeAdapter() //어댑터 객체 만듦
        noticeAdapter.datalist= noticeList //데이터 넣어줌
        binding!!.noticeRecyclerView.adapter=noticeAdapter //리사이클러뷰에 어댑터 연결
        binding!!.noticeRecyclerView.layoutManager= LinearLayoutManager(context) //레이아웃 매니저 연결
    }

    fun initializelist(noticeList: List<NoticeModel>?) {
        this.noticeList.clear() // Clear the current list
        noticeList?.let {
            this.noticeList.addAll(it) // Add the response data to the list
        }
    }


}