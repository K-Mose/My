package com.example.eattoday

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class MapFragment (
    val store_name:String,
    val longitude:Double,
    val latitude:Double,
    val tel:String,
    val address:String
):Fragment(){
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val kakaoMap = KakaoMap(store_name, longitude, latitude, tel,address)
        return inflater.inflate(R.layout.activity_kakao_map, container, false)
//        return super.onCreateView(inflater, container, savedInstanceState)
    }
}