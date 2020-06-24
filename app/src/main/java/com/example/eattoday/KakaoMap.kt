package com.example.eattoday

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.TableLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_kakao_map.*
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapReverseGeoCoder
import net.daum.mf.map.api.MapView

// 프래그먼트로 부르고, 프래그먼트에서 아래 값을 할당해 줌
class KakaoMap : AppCompatActivity() {
    companion object{
        val mapItemList = arrayListOf<MapItem>()
        val mapPI:ArrayList<MapPOIItem> = arrayListOf()
        var store_name = ""
        var longitude:Double = 0.0
        var latitude:Double = 0.0
        var tel = ""
        var address = ""
        var category = ""
        lateinit var mapView:MapView
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kakao_map)
        mapPI.clear()
        mapView = MapView(this@KakaoMap)

        // 클레스 리스트에 저장되어있는 리스트 정보 가져오기
//        val p = intent.getIntExtra("position", 0)
//        Log.d("-connect_c  :: ", "ExtraPosition :  $p")
//        val ratingValue = RatingView.companionRatingList[p]
//        Log.d("-connect_c  :: ", "ExtraPosition's Value :  $ratingValue")
//
//        // 태그 정보 저장하기
//        tag1.setText("잘하는")
//        tag1?.setTextColor(RatingView.colorCode.get(setColor(ratingValue.tag1)))
//        tag2.setText("혼밥")
//        tag2?.setTextColor(RatingView.colorCode.get(setColor(ratingValue.tag2)))
//        tag3.setText("가성비")
//        tag3?.setTextColor(RatingView.colorCode.get(setColor(ratingValue.tag3)))
//        tag4.setText("맛있는")
//        tag4?.setTextColor(RatingView.colorCode.get(setColor(ratingValue.tag4)))
//        tag5.setText("분위기")
//        tag5?.setTextColor(RatingView.colorCode.get(setColor(ratingValue.tag5)))
//        tag6.setText("데이트")
//        tag6?.setTextColor(RatingView.colorCode.get(setColor(ratingValue.tag6)))

        Log.d("-connection_c lists :", "list = "+ mapItemList)

//        val mapPI = MapPOIItem()
        var cnt = 0
        var MARKER_POINT = MapPoint.mapPointWithGeoCoord(latitude, longitude)
        mapItemList.forEach {
            Log.d("-TabLayout :: ", "Present mapItem - ${it.branch}")
            MARKER_POINT = MapPoint.mapPointWithGeoCoord(it.latitude, it.longitude) // 지도 좌표
            // 지점이 많으면 리스트로 만들어 추가해줌
            val mapPOIItem = MapPOIItem()
            mapPOIItem.itemName = store_name + " ${it.branch?:" "}"
            mapPOIItem.tag = 0
            mapPOIItem.mapPoint = MARKER_POINT
            mapPOIItem.markerType = MapPOIItem.MarkerType.BluePin
            mapPOIItem.selectedMarkerType = MapPOIItem.MarkerType.RedPin
            mapPI.add(mapPOIItem)
            mapView.addPOIItem(mapPI[cnt])
            cnt += 1
        }
        nameView.setText(store_name)

        mapView.setZoomLevel(4, true)
        mapView.setMapCenterPoint(mapPI[0].mapPoint, true) // 맵포인터가 지도 중앙으로 오게 설정

        val mapViewContainer = map_view
        mapViewContainer.removeAllViews()
        mapViewContainer.addView(mapView)  // 맵뷰에 지도 표시

        Log.d("-TabCount :: ", "${mapItemList.size}")

        val pagerAdapter = PagerAdapter(supportFragmentManager, mapItemList.size,this@KakaoMap)
        map_pager.adapter = pagerAdapter
        toNaver.setOnClickListener {
            val search_branch = pagerAdapter.getBranch(categoryTab.selectedTabPosition) ?: ""
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://m.map.naver.com/search2/search.nhn?query=노원+$store_name+$search_branch#/map/"))
            startActivity(intent)
        }



        mapItemList.forEach {
            Log.d("-connection_c lists :", "branch = ${it.branch}")
            // 지점 수 만큼 탭뷰를 추가
            if(it.branch == null){
                categoryTab.addTab(categoryTab.newTab().setText("$store_name"))
            }else{
                categoryTab.addTab(categoryTab.newTab().setText(it.branch))
            }
        }
//        else {
//            if(mapItemList[0].branch != null){
//                categoryTab.addTab(categoryTab.newTab().setText(mapItemList[0].branch))
//            }
//            else{
//                categoryTab.addTab(categoryTab.newTab().setText("$store_name"))
//            }
//        }

        categoryTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
                map_pager.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
//                mapView.setMapCenterPoint(mapPI[pagerAdapter.getItemPosition()].mapPoint, true) // 맵포인터가 지도 중앙으로 오게 설정
                Log.d("-TabLayout :: " , "TabPostion ::: ${tab!!.position}")
                mapView.selectPOIItem(mapPI[tab!!.position], true)
                mapView.setMapCenterPoint(mapPI[tab!!.position].mapPoint, true)
                map_pager.currentItem = tab!!.position
            }
        })
        map_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(categoryTab))

    }

    inner class PagerAdapter(
        fragmentManager: FragmentManager,
        val tabCount:Int,
        val context: Context
    ):FragmentStatePagerAdapter(fragmentManager){
        override fun getItem(position: Int): Fragment {
            return Frag_Map(mapItemList[position].tel, mapItemList[position].address, position)
        }

        override fun getCount(): Int {
            return tabCount
        }

        fun getBranch(position: Int):String?{
            return mapItemList[position].branch
        }
    }

    // 색 넣는 함수
    fun setColor(dValue: Double): Int {

        var colorCode = 0
        if (dValue > 0.9) {
            colorCode = 1
        } else if (dValue > 0.8) {
            colorCode = 2
        } else if (dValue > 0.7) {
            colorCode = 3
        } else if (dValue > 0.6) {
            colorCode = 4
        } else if (dValue > 0.5) {
            colorCode = 5
        } else if (dValue > 0.4) {
            colorCode = 6
        } else if (dValue > 0.3) {
            colorCode = 7
        } else if (dValue > 0.2) {
            colorCode = 8
        } else if (dValue > 0.1) {
            colorCode = 9
        } else if (0.1 >= dValue) {
            colorCode = 10
        }else{
            colorCode = 11
        }
        return colorCode
    }
}

// 지점이 여러 개일 수 도 있으니 클래스 리스트로 저장한다.
// 첫 화면에는 처음에 저장된 매장만 띄운다.
class MapItem(val store_name:String, val address:String, val tel:String, val latitude:Double, val longitude:Double, val branch:String?)