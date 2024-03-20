package com.example.disaster_helper.Home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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