package com.example.disaster_helper.Home

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.disaster_helper.R
import com.example.disaster_helper.databinding.FragmentHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Calendar
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader
import java.text.SimpleDateFormat

data class item_typhoon(
    val numOfRows: Int = 0,
    val pageNo: Int = 0,
    val totalCount: Int = 0,
    val resultCode: String = "",
    val resultMsg: String = "",
    val dataType: String = "",
    val img: String = "",
    val tmFc: String = "",
    val typSeq: String = "",
    val tmSeq: String = "",
    val typTm: String = "",
    val typLat: Double = 0.0,
    val typLon: Double = 0.0,
    val typLoc: String = "",
    val typDir: String = "",
    val typSp: String = "",
    val typPs: String = "",
    val typWs: String = "",
    val typ15: String = "",
    val typ15ed: String = "",
    val typ15er: String = "",
    val typ25: String = "",
    val typ25ed: String = "",
    val typ25er: String = "",
    val typName: String = "",
    val typEn: String = "",
    val rem: String = "",
    val other: String = ""
)

data class item_earthquake(
    val numOfRows: Int = 0,
    val pageNo: Int = 0,
    val totalCount: Int = 0,
    val resultCode: String = "",
    val resultMsg: String = "",
    val dataType: String = "",
    val stnId: String = "",
    val fcTp: String = "",
    val mapImage: String = "",
    val tmFc: String = "",
    val tmSeq: String = "",
    val cnt: String = "",
    val tmEqk: String = "",
    val tmMsc: String = "",
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val loc: String = "",
    val mt: String = "",
    val inT: String = "",
    val dep: String = "",
    val rem: String = "",
    val cor: String = ""
)


class HomeFragment : Fragment(R.layout.fragment_home) {

    //test
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var noticeAdapter: NoticeAdapter  //test
    private val noticeList = mutableListOf<NoticeModel>() //test
    private var binding: FragmentHomeBinding? = null //test


    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        val fragmentHomeBinding = FragmentHomeBinding.bind(view)
        binding = fragmentHomeBinding


        //test
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())


        val noticeList = listOf(NoticeModel("title test1","text test1"),NoticeModel("title test2","text test2"))
        initializelist(noticeList)
        initNoticeRecyclerView()

        binding!!.refreshLayout.setOnRefreshListener{

            //val noticeList = listOf(NoticeModel("title test1","text test1"),NoticeModel("title test2","text test2"))


            //initializelist(noticeList)
            //initNoticeRecyclerView()

            binding!!.refreshLayout.isRefreshing=false
        }

        //대피 요령
        binding!!.allHelpBtn.setOnClickListener{
            showAlertDialog("통합 대피요령",0)
        }

        binding!!.earthHelpBtn.setOnClickListener {
            showAlertDialog("통합 대피요령",1)
        }

        binding!!.typhoonHelpBtn.setOnClickListener {
            showAlertDialog("통합 대피요령",2)
        }

        //# 위치 권한 확인
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
        } else {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    try {

                        // 좌표 처리
                        val latitude = location?.latitude ?: throw Exception("Location is null")
                        val longitude = location.longitude
                        val userLocation = Pair(latitude, longitude)
                        var grade : Int = 0
                        var totalGrade = 0


                        CoroutineScope(Dispatchers.IO).launch {
                            val earthquakeGrade = earthquake(userLocation, grade)
                            val typhoonGrade = typhoon(userLocation, grade)

                            withContext(Dispatchers.Main) {
                                totalGrade = earthquakeGrade + typhoonGrade
                                Log.d("Total Grade", "Total Grade: $totalGrade")
                            }

                            when(totalGrade){
                                0 -> {
                                    //binding!!.gradeImg.setImageResource(R.drawable.grade1)
                                    //binding!!.boardText.text = "주변이 안전합니다."
                                }
                                1 -> {
                                    binding!!.gradeImg.setImageResource(R.drawable.grade2)
                                    if(earthquakeGrade == 1){
                                        binding!!.boardText.text= "주변 50km 내에서 지진이 발생하였습니다."
                                    } else if(typhoonGrade == 1){
                                        binding!!.boardText.text = "주변 50km 내에서 태풍이 발생하였습니다."
                                    } else {
                                        binding!!.boardText.text = "주변 50km 내에서 지진 혹은 태풍이 발생하였습니다.."
                                    }

                                }
                                2 -> {
                                    binding!!.gradeImg.setImageResource(R.drawable.grade3)
                                    binding!!.boardText.text = "주변 50km 내에서 지진, 태풍이 발생하였습니다.."
                                }
                                else -> {
                                    binding!!.gradeImg.setImageResource(R.drawable.grade1)
                                    binding!!.boardText.text = "주변이 안전합니다."
                                }

                            }
                        }


                    } catch (e: Exception) {
                        Log.d("grade error2" , "${e.message}")
                    }


                }
        }
    }

    private fun showAlertDialog(message: String, alert_kind: Int) {
        // AlertDialog 생성
        val builder = AlertDialog.Builder(requireContext())
        // 사용자 지정 레이아웃을 설정
        val inflater = requireActivity().layoutInflater

        val view = when(alert_kind) {
            0 -> {
                inflater.inflate(R.layout.all_alert_layout, null)
            }
            1 -> {
                inflater.inflate(R.layout.earth_alert_layout, null)
            }
            2 -> {
                inflater.inflate(R.layout.typhoon_alert_layout, null)
            }
            else -> null
        }

        if(view != null){
            builder.setView(view)
            val alertDialog = builder.create()

            val closeButton = when(alert_kind) {
                0 -> {
                    view.findViewById<Button>(R.id.all_close_btn)

                }
                1 -> {
                    view.findViewById<Button>(R.id.earth_close_btn)
                }
                2 -> {
                    view.findViewById<Button>(R.id.typhoon_close_btn)
                }
                else -> null
            }

            closeButton?.setOnClickListener {
                alertDialog.dismiss() // AlertDialog를 닫음
            }

            // 크기 조절 (예: 가로 80%, 세로 WRAP_CONTENT)
            alertDialog.window?.setLayout((resources.displayMetrics.widthPixels * 0.8).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
            // AlertDialog 표시
            alertDialog.show()
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    fun initNoticeRecyclerView(){
        /*
        val noticeAdapter= NoticeAdapter() //어댑터 객체 만듦
        noticeAdapter.datalist= noticeList //데이터 넣어줌
        binding!!.noticeRecyclerView.adapter=noticeAdapter //리사이클러뷰에 어댑터 연결
        binding!!.noticeRecyclerView.layoutManager= LinearLayoutManager(context) //레이아웃 매니저 연결

         */
    }

    fun initializelist(noticeList: List<NoticeModel>?) {
        /*
        this.noticeList.clear() // Clear the current list
        noticeList?.let {
            this.noticeList.addAll(it) // Add the response data to the list
        }
         */
    }

    fun configureConnection(url: URL, apiKey: String): HttpURLConnection {
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("Content-Type", "application/json")
        connection.setRequestProperty("Accept", "application/json") // JSON 응답을 명시적으로 요청
        connection.setRequestProperty("serviceKey", apiKey)
        connection.setRequestProperty("pageNo", "1")
        connection.setRequestProperty("numOfRows", "")
        connection.setRequestProperty("dataType", "JSON")

        // 현재 날짜 설정, 올바른 포맷으로 설정해야 함
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyyMMdd")
        val currentDate = dateFormat.format(calendar.time)
        connection.setRequestProperty("fromTmFc", currentDate)
        connection.setRequestProperty("toTmFc", currentDate)

        return connection
    }

    suspend fun earthquake(userLocation: Pair<Double, Double>, grade: Int): Int {
        val url = URL("http://apis.data.go.kr/1360000/EqkInfoService/getEqkMsg")
        val apikey = "oPYjcxF2XWBY7g8B1y4jqvWobdX16oWqIOslr4J9RloIc0+0wWln02t2RW4vvfg06MJS4FtbNdm+kS2VbxCrQQ=="
        val connection = configureConnection(url, apikey)

        Log.d("before Grade", "earth Grade: $grade")

        // XML
        val xmlResponse = fetchXmlResponse(connection)
        val res = parseXmlResponse2(xmlResponse)

        if (res.isNotEmpty() && res[0].resultCode != "03" && res[0].resultMsg != "NO_DATA") {
            val earthquakePosition = Pair(res[0].lat, res[0].lon)

            Log.d("if1 Grade", "earth Grade: $grade")

            if (userLocation.first - 0.5 <= earthquakePosition.first && earthquakePosition.first <= userLocation.first + 0.5 &&
                userLocation.second - 0.5 <= earthquakePosition.second && earthquakePosition.second <= userLocation.second + 0.5) {
                grade + 3//1
                Log.d("if2 Grade", "earth Grade: $grade")
            } else {
                grade
                Log.d("if3 Grade", "earth Grade: $grade")
            }
        } else {
            // 데이터가 없는 경우
            Log.d("None_Earthquake", "None_Data")
            Log.d("if4 Grade", "earth Grade: $grade")
            grade
        }
        return grade
    }

    suspend fun typhoon(userLocation: Pair<Double, Double>, grade: Int): Int {
        val url = URL("http://apis.data.go.kr/1360000/TyphoonInfoService/getTyphoonInfo")
        val apikey = "oPYjcxF2XWBY7g8B1y4jqvWobdX16oWqIOslr4J9RloIc0+0wWln02t2RW4vvfg06MJS4FtbNdm+kS2VbxCrQQ=="
        val connection = configureConnection(url, apikey)

        Log.d("before Grade", "typhoon Grade: $grade")

        // XML
        val xmlResponse = fetchXmlResponse(connection)
        val res = parseXmlResponse(xmlResponse)

        /*Log.d("typhoon res" , "res.let : ${res[0].typLat}")
        Log.d("typhoon res" , "res.let : ${res[0].typLon}")*/

        if (res.isNotEmpty() && res[0].resultCode != "03" && res[0].resultMsg != "NO_DATA") {
            val typhoonPosition = Pair(res[0].typLat, res[0].typLon)

            Log.d("if1 Grade", "typhoon Grade: $grade")

            if (userLocation.first - 0.5 <= typhoonPosition.first && typhoonPosition.first <= userLocation.first + 0.5 &&
                userLocation.second - 0.5 <= typhoonPosition.second && typhoonPosition.second <= userLocation.second + 0.5) {
                grade + 3 //1
                Log.d("if2 Grade", "typhoon Grade: $grade")
            } else {
                grade
                Log.d("if3 Grade", "typhoon Grade: $grade")
            }
        } else {
            // 데이터가 없는 경우에 실행되는 부분. 이거 아무거나 넣어보셈
            Log.d("None","None_Data")
            Log.d("if4 Grade", "typhoon Grade: $grade")
            grade
        }
        return grade
    }

    suspend fun fetchXmlResponse(connection: HttpURLConnection): String {
        return withContext(Dispatchers.IO) {
            connection.connect()
            connection.inputStream.bufferedReader().use { it.readText() }
        }
    }

    fun parseXmlResponse(xmlString: String): List<item_typhoon> {
        val items = mutableListOf<item_typhoon>()

        val factory = XmlPullParserFactory.newInstance()
        val parser = factory.newPullParser()
        parser.setInput(StringReader(xmlString))

        var eventType = parser.eventType
        var currentItem: item_typhoon? = null

        // XML 파싱
        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> { // 시작 태그
                    if (parser.name == "item") {
                        currentItem = item_typhoon(
                            numOfRows = parser.getAttributeValue(null, "numOfRows")?.toIntOrNull() ?: 0,
                            pageNo = parser.getAttributeValue(null, "pageNo")?.toIntOrNull() ?: 0,
                            totalCount = parser.getAttributeValue(null, "totalCount")?.toIntOrNull() ?: 0,
                            resultCode = parser.getAttributeValue(null, "resultCode") ?: "",
                            resultMsg = parser.getAttributeValue(null, "resultMsg") ?: "",
                            dataType = parser.getAttributeValue(null, "dataType") ?: "",
                            img = parser.getAttributeValue(null, "img") ?: "",
                            tmFc = parser.getAttributeValue(null, "tmFc") ?: "",
                            typSeq = parser.getAttributeValue(null, "typSeq") ?: "",
                            tmSeq = parser.getAttributeValue(null, "tmSeq") ?: "",
                            typTm = parser.getAttributeValue(null, "typTm") ?: "",
                            typLat = parser.getAttributeValue(null, "typLat")?.toDoubleOrNull() ?: 0.0,
                            typLon = parser.getAttributeValue(null, "typLon")?.toDoubleOrNull() ?: 0.0,
                            typLoc = parser.getAttributeValue(null, "typLoc") ?: "",
                            typDir = parser.getAttributeValue(null, "typDir") ?: "",
                            typSp = parser.getAttributeValue(null, "typSp") ?: "",
                            typPs = parser.getAttributeValue(null, "typPs") ?: "",
                            typWs = parser.getAttributeValue(null, "typWs") ?: "",
                            typ15 = parser.getAttributeValue(null, "typ15") ?: "",
                            typ15ed = parser.getAttributeValue(null, "typ15ed") ?: "",
                            typ15er = parser.getAttributeValue(null, "typ15er") ?: "",
                            typ25 = parser.getAttributeValue(null, "typ25") ?: "",
                            typ25ed = parser.getAttributeValue(null, "typ25ed") ?: "",
                            typ25er = parser.getAttributeValue(null, "typ25er") ?: "",
                            typName = parser.getAttributeValue(null, "typName") ?: "",
                            typEn = parser.getAttributeValue(null, "typEn") ?: "",
                            rem = parser.getAttributeValue(null, "rem") ?: "",
                            other = parser.getAttributeValue(null, "other") ?: ""
                        )
                    }
                }
                XmlPullParser.END_TAG -> { // 종료 태그
                    if (parser.name == "item" && currentItem != null) {
                        items.add(currentItem)
                        currentItem = null // item_typhoon 객체 초기화
                    }
                }
            }
            eventType = parser.next()
        }

        return items // 리스트를 반환
    }

    fun parseXmlResponse2(xmlString: String): List<item_earthquake> {
        val items = mutableListOf<item_earthquake>()

        val factory = XmlPullParserFactory.newInstance()
        val parser = factory.newPullParser()
        parser.setInput(StringReader(xmlString))

        var eventType = parser.eventType
        var currentItem: item_earthquake? = null

        // XML 파싱
        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> { // 시작 태그
                    if (parser.name == "item") {
                        currentItem = item_earthquake(
                            numOfRows = parser.getAttributeValue(null, "numOfRows")?.toIntOrNull() ?: 0,
                            pageNo = parser.getAttributeValue(null, "pageNo")?.toIntOrNull() ?: 0,
                            totalCount = parser.getAttributeValue(null, "totalCount")?.toIntOrNull() ?: 0,
                            resultCode = parser.getAttributeValue(null, "resultCode") ?: "",
                            resultMsg = parser.getAttributeValue(null, "resultMsg") ?: "",
                            dataType = parser.getAttributeValue(null, "dataType") ?: "",
                            stnId = parser.getAttributeValue(null, "stnId") ?: "",
                            fcTp = parser.getAttributeValue(null, "fcTp") ?: "",
                            mapImage = parser.getAttributeValue(null, "mapImage") ?: "",
                            tmFc = parser.getAttributeValue(null, "tmFc") ?: "",
                            tmSeq = parser.getAttributeValue(null, "tmSeq") ?: "",
                            cnt = parser.getAttributeValue(null, "cnt") ?: "",
                            tmEqk = parser.getAttributeValue(null, "tmEqk") ?: "",
                            tmMsc = parser.getAttributeValue(null, "tmMsc") ?: "",
                            lat = parser.getAttributeValue(null, "lat")?.toDoubleOrNull() ?: 0.0,
                            lon = parser.getAttributeValue(null, "lon")?.toDoubleOrNull() ?: 0.0,
                            loc = parser.getAttributeValue(null, "loc") ?: "",
                            mt = parser.getAttributeValue(null, "mt") ?: "",
                            inT = parser.getAttributeValue(null, "inT") ?: "",
                            dep = parser.getAttributeValue(null, "dep") ?: "",
                            rem = parser.getAttributeValue(null, "rem") ?: "",
                            cor = parser.getAttributeValue(null, "cor") ?: ""
                        )
                    }
                }
                XmlPullParser.END_TAG -> { // 종료 태그
                    if (parser.name == "item" && currentItem != null) {
                        items.add(currentItem)
                        currentItem = null // item_earthquake 객체 초기화
                    }
                }
            }
            eventType = parser.next()
        }

        return items // 리스트를 반환
    }

}


