package com.example.disaster_helper.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.disaster_helper.databinding.FragmentSearchBinding
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import okhttp3.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.StringReader

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        val mapView = MapView(context)
        binding.mapView.addView(mapView)

        fetchEarthquakeShelters()

        return binding.root
    }

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

        mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading // 현재 위치를 기준점으로
        // mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(37.5665, 126.9780), 10, true)

        val markers = shelters.map { shelter ->
            val marker = net.daum.mf.map.api.MapPOIItem()
            marker.itemName = shelter.facilityName
            marker.tag = shelter.id.toInt() // You can use the shelter id as a tag for the marker
            marker.mapPoint = MapPoint.mapPointWithGeoCoord(shelter.latitude, shelter.longitude)
            // marker.markerType = net.daum.mf.map.api.MapPOIItem.MarkerType.BluePin
            marker
        }.toTypedArray()

        mapView.removeAllPOIItems()
        mapView.addPOIItems(markers)
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