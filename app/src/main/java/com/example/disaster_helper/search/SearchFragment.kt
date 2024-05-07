package com.example.disaster_helper.search

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import com.example.disaster_helper.R
import com.example.disaster_helper.databinding.FragmentSearchBinding
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.serialization.Serializable
import net.daum.mf.map.api.CalloutBalloonAdapter
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import okhttp3.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.StringReader

var real_name: String = ""
var real_address: String = ""

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var eventListener: MarkerEventListener

    var districtLatitude: Double =0.00
    var districtLongitude : Double = 0.00



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        val mapView = MapView(context)

        binding.mapView.addView(mapView)

        fetchEarthquakeShelters()

        val data: Array<String> = resources.getStringArray(R.array.space_kind)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, data)
        binding!!.spaceSpinner.adapter = adapter
        binding!!.spaceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                when(position){
                    0 -> {
                        //현위치
                    }

                    1 -> {
                        //강남구
                        districtLatitude = 127.0629852	  // 예시 위도
                        districtLongitude = 37.49664389 // 예시 경도
                        Log.d("강남구","강남구 선택")
                        Log.d("현재 좌표", "latitude: $districtLatitude, longitude: $districtLongitude")
                        //mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(districtLatitude, districtLongitude), 12, true);
                        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(districtLongitude, districtLatitude), 6, true);
                    }

                    2 -> {
                        //강동구
                        districtLatitude = 127.1470118 // 예시 위도
                        districtLongitude = 37.55045024  // 예시 경도
                        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(districtLongitude, districtLatitude), 6, true);
                    }

                    3 -> {
                        //강북구
                        districtLatitude = 127.011189 // 예시 위도
                        districtLongitude = 37.64347391 // 예시 경도
                        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(districtLongitude, districtLatitude), 6, true);
                    }

                    4 -> {
                        //강서구
                        districtLatitude = 126.822807  // 예시 위도
                        districtLongitude = 37.56123543 // 예시 경도
                        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(districtLongitude, districtLatitude), 6, true);
                    }

                    5 -> {
                        //관악구
                        districtLatitude = 126.9453372	 // 예시 위도
                        districtLongitude = 37.46737569  // 예시 경도
                        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(districtLongitude, districtLatitude), 6, true);
                    }

                    6 -> {
                        //광진구
                        districtLatitude = 127.0857435  // 예시 위도
                        districtLongitude = 37.54670608  // 예시 경도
                        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(districtLongitude, districtLatitude), 6, true);
                    }

                    7 -> {
                        //구로구
                        districtLatitude = 126.8563006  // 예시 위도
                        districtLongitude = 37.49440543  // 예시 경도
                        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(districtLongitude, districtLatitude), 6, true);
                    }

                    8 -> {
                        //금천구
                        districtLatitude = 126.9008202  // 예시 위도
                        districtLongitude = 37.46056756  // 예시 경도
                        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(districtLongitude, districtLatitude), 6, true);
                    }

                    9 -> {
                        //노원구
                        districtLatitude = 127.0750347  // 예시 위도
                        districtLongitude = 37.65251105  // 예시 경도
                        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(districtLongitude, districtLatitude), 6, true);
                    }

                    10 -> {
                        //도봉구
                        districtLatitude = 127.0323688  // 예시 위도
                        districtLongitude = 37.66910208  // 예시 경도
                        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(districtLongitude, districtLatitude), 6, true);
                    }

                    11 -> {
                        //동대문구
                        districtLatitude = 127.0548481  // 예시 위도
                        districtLongitude = 37.58195655  // 예시 경도
                        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(districtLongitude, districtLatitude), 6, true);
                    }

                    12 -> {
                        //동작구
                        districtLatitude = 126.9516415 // 예시 위도
                        districtLongitude = 37.49887688  // 예시 경도
                        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(districtLongitude, districtLatitude), 6, true);
                    }

                    13 -> {
                        //마포구
                        districtLatitude = 126.90827  // 예시 위도
                        districtLongitude = 37.55931349 // 예시 경도
                        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(districtLongitude, districtLatitude), 6, true);
                    }

                    14 -> {
                        //서대문구
                        districtLatitude = 126.9390631  // 예시 위도
                        districtLongitude = 37.57778531  // 예시 경도
                        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(districtLongitude, districtLatitude), 6, true);
                    }

                    15 -> {
                        //서초구
                        districtLatitude = 127.0312203  // 예시 위도
                        districtLongitude = 37.47329547  // 예시 경도
                        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(districtLongitude, districtLatitude), 6, true);
                    }

                    16 -> {
                        //성동구
                        districtLatitude = 127.0410585  // 예시 위도
                        districtLongitude = 37.55102969  // 예시 경도
                        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(districtLongitude, districtLatitude), 6, true);
                    }

                    17 -> {
                        //성북구
                        districtLatitude = 127.0175795  // 예시 위도
                        districtLongitude = 37.6057019  // 예시 경도
                        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(districtLongitude, districtLatitude), 6, true);
                    }

                    18 -> {
                        //송파구
                        districtLatitude = 127.115295  // 예시 위도
                        districtLongitude = 37.50561924  // 예시 경도
                        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(districtLongitude, districtLatitude), 6, true);
                    }

                    19 -> {
                        //양천구
                        districtLatitude = 126.8554777	  // 예시 위도
                        districtLongitude = 37.52478941  // 예시 경도
                        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(districtLongitude, districtLatitude), 6, true);
                    }

                    20 -> {
                        //영등포구
                        districtLatitude = 126.9101695	  // 예시 위도
                        districtLongitude = 37.52230829 // 예시 경도
                        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(districtLongitude, districtLatitude), 6, true);
                    }

                    21 -> {
                        //용산구
                        districtLatitude = 126.979907  // 예시 위도
                        districtLongitude = 37.53138497  // 예시 경도
                        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(districtLongitude, districtLatitude), 6, true);
                    }

                    22 -> {
                        //은평구
                        districtLatitude = 126.9270229	  // 예시 위도
                        districtLongitude = 37.61921128  // 예시 경도
                        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(districtLongitude, districtLatitude), 6, true);
                    }

                    23 -> {
                        //종로구
                        districtLatitude = 126.9773213  // 예시 위도
                        districtLongitude = 37.59491732 // 예시 경도
                        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(districtLongitude, districtLatitude), 6, true);
                    }

                    24 -> {
                        //중구
                        districtLatitude = 126.9959681	  // 예시 위도
                        districtLongitude = 37.56014356  // 예시 경도
                        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(districtLongitude, districtLatitude), 6, true);
                    }

                    25 -> {
                        //중량구
                        districtLatitude = 127.0928803  // 예시 위도
                        districtLongitude = 37.59780259  // 예시 경도
                        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(districtLongitude, districtLatitude), 6, true);
                    }


                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        return binding.root
    }

    /*
    private fun changeViewToDistrict(mapView: MapView, latitude: Double, longitude: Double, zoomLevel: Int = 10){
        val districtMapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude)

        // 지도 중심을 구청의 위치로 설정하고, 줌 레벨을 설정합니다.
        mapView.setMapCenterPointAndZoomLevel(districtMapPoint, zoomLevel, true)
        Log.d("강남구","강남구 함수 호출")
        Log.d("현재 좌표", "latitude: $districtLatitude, longitude: $districtLongitude")
    }*/

    private fun fetchEarthquakeShelters() {
        val seoulApi = "557a774c63746f753834674f707a79"
        val url = "http://openapi.seoul.go.kr:8088/$seoulApi/xml/TbEqkShelter/1/5/" // 이거 테스트용
        val request = Request.Builder()
            .url(url)
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                activity?.runOnUiThread {
                    Toast.makeText(context, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let { responseBody ->
                    val shelters = parseEarthquakeShelters(responseBody.string())
                    activity?.runOnUiThread {
                        displaySheltersOnMap(shelters)
                    }
                }
            }
        })
    }

    private fun parseEarthquakeShelters(xmlData: String): List<EarthquakeShelter> {
        val shelters = mutableListOf<EarthquakeShelter>()

        val factory = XmlPullParserFactory.newInstance()
        factory.isNamespaceAware = true
        val parser = factory.newPullParser()
        parser.setInput(StringReader(xmlData))

        var eventType = parser.eventType
        var shelter: EarthquakeShelter? = null

        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    when (parser.name) {
                        "row" -> shelter = EarthquakeShelter()
                        "SHELTER_ID" -> shelter?.id = parser.nextText()
                        "CTPRVN_NM" -> shelter?.provinceName = parser.nextText()
                        "SGG_NM" -> shelter?.cityName = parser.nextText()
                        "VT_ACMDFCLTY_NM" -> shelter?.facilityName = parser.nextText()
                        "DTL_ADRES" -> shelter?.address = parser.nextText()
                        "LON" -> shelter?.longitude = parser.nextText().toDouble()
                        "LAT" -> shelter?.latitude = parser.nextText().toDouble()
                        // Parsing other fields if needed
                    }
                }
                XmlPullParser.END_TAG -> {
                    if (parser.name == "row") {
                        shelter?.let { shelters.add(it) }
                    }
                }
            }
            eventType = parser.next()
        }

        return shelters
    }

    private fun displaySheltersOnMap(shelters: List<EarthquakeShelter>) {
        val mapView = binding.mapView.getChildAt(0) as MapView


        mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading

        /*
        if(districtLatitude == 0.00 && districtLongitude == 0.00){
            Log.d("현재 좌표", "latitude: $districtLatitude, longitude: $districtLongitude")
            mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
        } else {
            //changeViewToDistrict(mapView, districtLatitude, districtLongitude, 12)
            mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(districtLatitude, districtLongitude), 9, true);

            Log.d("현재 좌표", "latitude: $districtLatitude, longitude: $districtLongitude")
            Log.d("강남구","강남구 성공")
        }*/

        /*
        if(districtLatitude == 127.0629852 && districtLongitude==37.49664389){
            changeViewToDistrict(mapView, districtLatitude, districtLongitude, 22)//120
            Log.d("현재 좌표", "latitude: $districtLatitude, longitude: $districtLongitude")
            Log.d("강남구","강남구 성공")
        }*/

        mapView.setCalloutBalloonAdapter(CustomBalloonAdapter(layoutInflater))

        eventListener = MarkerEventListener(requireContext())
        mapView.setPOIItemEventListener(eventListener)

        // 마커 목록을 생성합니다.
        val markers = shelters.map { shelter ->
            val marker = MapPOIItem().apply {
                itemName = shelter.facilityName
                real_name = itemName
                itemName = shelter.address
                real_address = itemName


                tag = shelter.id.toInt()
                mapPoint = MapPoint.mapPointWithGeoCoord(shelter.latitude, shelter.longitude)
            }
            marker
        }.toTypedArray()



        mapView.removeAllPOIItems()
        mapView.addPOIItems(markers)

    }

    class CustomBalloonAdapter(inflater: LayoutInflater): CalloutBalloonAdapter {
        val mCalloutBalloon: View = inflater.inflate(R.layout.balloon_layout, null)
        val name: TextView = mCalloutBalloon.findViewById(R.id.ball_tv_name)
        val address: TextView = mCalloutBalloon.findViewById(R.id.ball_tv_address)

        override fun getCalloutBalloon(poiItem: MapPOIItem?): View {
            // 마커 클릭 시 나오는 말풍선
            //name.text = poiItem?.itemName
            //address.text =

            name.text = real_name
            address.text = real_address
            return mCalloutBalloon
        }


        override fun getPressedCalloutBalloon(poiItem: MapPOIItem?): View {
            // 말풍선 클릭 시
            /*
            Log.d("success","data sent successfully")
            //address.text = "getPressedCalloutBalloon"
            //name.text = poiItem?.itemName
            address.text = real_name*/
            return mCalloutBalloon
        }
    }

    // 마커 클릭 이벤트 리스너
    class MarkerEventListener(val context: Context): MapView.POIItemEventListener {
        override fun onPOIItemSelected(mapView: MapView?, poiItem: MapPOIItem?) {
            // 마커 클릭 시
            Log.d("success marker","data sent successfully")
        }

        override fun onCalloutBalloonOfPOIItemTouched(mapView: MapView?, poiItem: MapPOIItem?) {
            Log.d("success2","data sent successfully")
        }

        override fun onCalloutBalloonOfPOIItemTouched(mapView: MapView?, poiItem: MapPOIItem?, buttonType: MapPOIItem.CalloutBalloonButtonType?) {
            // 말풍선 클릭 시
            Log.d("success3","data sent successfully")
            val builder = AlertDialog.Builder(context)
            val itemList = arrayOf("걸어서", "차타고", "취소")
            //builder.setTitle("${poiItem?.itemName}")
            builder.setTitle("${real_name}")
            builder.setItems(itemList) { dialog, which ->
                when(which) {
                    0 -> {
                        val url2 : String ="kakaomap://route?sp=37.537229,127.005515&ep=37.4979502,127.0276368&by=FOOT"
                        var intent =  Intent(Intent.ACTION_VIEW, Uri.parse(url2))
                        intent.addCategory(Intent.CATEGORY_BROWSABLE)
                        val packageManager = context.packageManager  // `requireContext()`를 사용하여 `packageManager` 가져오기
                        var list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)

                        //카카오맵 어플리케이션이 사용자 핸드폰에 깔려있으면 바로 앱으로 연동
                        //그렇지 않다면 다운로드 페이지로 연결

                        if (list== null || list.isEmpty()){
                            //startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=net.daum.android.map")))
                            val marketIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=net.daum.android.map"))
                            startActivity(context, marketIntent, null)
                        }else{
                            //startActivity(intent)
                            startActivity(context, intent, null)
                        }
                    }
                    1 -> mapView?.removePOIItem(poiItem)    // 마커 삭제
                    2 -> dialog.dismiss()   // 대화상자 닫기
                }
            }
            builder.show()
        }

        override fun onDraggablePOIItemMoved(mapView: MapView?, poiItem: MapPOIItem?, mapPoint: MapPoint?) {
            // 마커의 속성 중 isDraggable = true 일 때 마커를 이동시켰을 경우
        }
    }
}

data class EarthquakeShelter(
    var id: String = "",
    var provinceName: String = "", // 시도명
    var cityName: String = "", // 시군구명
    var facilityName: String = "", // 시설명
    var address: String = "",
    var longitude: Double = 0.0,
    var latitude: Double = 0.0
)