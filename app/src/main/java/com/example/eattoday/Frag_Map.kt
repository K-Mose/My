package com.example.eattoday

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.store_detail.view.*



class Frag_Map(
    val tel: String,
    val address: String,
    val position:Int
) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.store_detail, container, false)

        // 리뷰 리스트 어댑터
        rootView.store_address.setText(address)
        rootView.store_tel.setText(tel)

        // 누구의 ListView지 알려주야 null 이 안됨 ..?
        return rootView
    }
}