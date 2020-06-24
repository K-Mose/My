package com.example.eattoday

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.rating_layout.view.*

// 통합 프래그먼트
class Fragment(val rating_list:ArrayList<RatingR>, val tag1:String?, val tag2:String?, val tag3:String?,
               val context:MainActivity, val table:Boolean ):Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.rating_layout, container, false)

        // 리뷰 리스트 어댑터
        val adapter = RatingView(
            rating_list, LayoutInflater.from(context),
            tag1,
            tag2,
            tag3
        )

        // 누구의 ListView지 알려주야 null 이 안됨 ..?
        rootView.listViewR.adapter = adapter


        rootView.listViewR.setOnItemClickListener { parent, view, position, id ->
            // 어댑터 뷰 : parent, 현재 뷰?: 뷰, 위치: position, long??
            Log.d("-ListViewClick", "${adapter.getStore_name(position)}")
            DbSelected(adapter.getStore_name(position)).execute()
        }


        return rootView
    }


    //
    inner class DbSelected(val store_name:String): AsyncTask<Unit, Unit, String>(){
        override fun onPostExecute(result: String?) {
            val intentTOmap = Intent(context, KakaoMap::class.java)
            startActivity(intentTOmap)
            super.onPostExecute(result)
        }

        override fun doInBackground(vararg params: Unit?): String? {
            DbTasksetMapAttribute(store_name, KakaoMap.mapItemList, table).doDB()
            return null
        }
    }

}