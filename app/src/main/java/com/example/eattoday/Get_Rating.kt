/*
* ListView 를 생성시키기 위한 뷰 홀더 및
* 평점 저장 클래스
* */

package com.example.eattoday

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat

class RatingView(
    val ratingList: ArrayList<RatingR>,
    val layoutInflater: LayoutInflater,
    var t1: String?,
    var t2: String?,
    var t3: String?,
    var t4: String? = null,
    var t5: String? = null,
    var t6: String? = null
) : BaseAdapter() {
    companion object {
        val colorCode = MainActivity.colorList // MainActivity 의 코드를 가져온다 .
        lateinit var companionRatingList:ArrayList<RatingR>
        // 태그를 맵핑하여서 쉽게 전환한다
        val tagMap = hashMapOf(
            "well" to "잘하는",
            "tasty" to "맛있는",
            "solo" to "혼밥",
            "atmosphere" to "분위기",
            "dating" to "데이트",
            "cost_effect" to "가성비",
            "good" to "좋은",
            "sensible" to "감각적",
            "잘하는" to "잘하는",
            "맛있는" to "맛있는",
            "혼밥" to "혼밥",
            "분위기" to "분위기",
            "데이트" to "데이트",
            "가성비" to "가성비",
            "좋은" to "좋은",
            "감각적" to "감각적"
        )
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolderR
        companionRatingList = ratingList
        if (convertView == null) {
            // 뷰가 비어있으면 홀더 추가
            view = layoutInflater.inflate(R.layout.rating_content, null)
            holder = ViewHolderR()
            holder.storeName = view.findViewById(R.id.store_name)
            holder.tag1 = view.findViewById(R.id.tag1)
            holder.tag2 = view.findViewById(R.id.tag2)
            holder.tag3 = view.findViewById(R.id.tag3)
            holder.tag4 = view.findViewById(R.id.tag4)
            holder.tag5 = view.findViewById(R.id.tag5)
            holder.tag6 = view.findViewById(R.id.tag6)

            // 뷰의 태그 설정
            view.tag = holder
        } else {
            holder = convertView.tag as ViewHolderR
            view = convertView
        }
        holder.storeName?.setText(ratingList.get(position).store_name)

        Log.d("-colorCode :", "${ratingList.get(position).store_name} #${tagMap.get(t1)} ${ratingList.get(position).tag1} ${setColor(ratingList.get(position).tag1)}")
        Log.d("-colorCode :", "${ratingList.get(position).store_name} #${tagMap.get(t2)} ${ratingList.get(position).tag2} ${setColor(ratingList.get(position).tag2)}")
        Log.d("-colorCode :", "${ratingList.get(position).store_name} #${tagMap.get(t3)} ${ratingList.get(position).tag3} ${setColor(ratingList.get(position).tag3)}")
        Log.d("-colorCode :", "${ratingList.get(position).store_name} #${tagMap.get(t4)} ${ratingList.get(position).tag1} ${setColor(ratingList.get(position).tag4)}")
        Log.d("-colorCode :", "${ratingList.get(position).store_name} #${tagMap.get(t5)} ${ratingList.get(position).tag2} ${setColor(ratingList.get(position).tag5)}")
        Log.d("-colorCode :", "${ratingList.get(position).store_name} #${tagMap.get(t6)} ${ratingList.get(position).tag3} ${setColor(ratingList.get(position).tag6)}")
        holder.tag1?.setTextColor(colorCode.get(setColor(ratingList.get(position).tag1)))
        holder.tag2?.setTextColor(colorCode.get(setColor(ratingList.get(position).tag2)))
        holder.tag3?.setTextColor(colorCode.get(setColor(ratingList.get(position).tag3)))
        holder.tag4?.setTextColor(colorCode.get(setColor(ratingList.get(position).tag4)))
        holder.tag5?.setTextColor(colorCode.get(setColor(ratingList.get(position).tag5)))
        holder.tag6?.setTextColor(colorCode.get(setColor(ratingList.get(position).tag6)))

        if(tagMap.get(t1)!=null){
            holder.tag1?.setText("#${tagMap.get(t1)}")
        }
        if(tagMap.get(t2)!=null){
            holder.tag2?.setText("#${tagMap.get(t2)}")
        }
        if(tagMap.get(t3)!=null){
            holder.tag3?.setText("#${tagMap.get(t3)}")
        }
        if(tagMap.get(t4)!=null){
            holder.tag4?.setText("#${tagMap.get(t4)}")
        }
        if(tagMap.get(t5)!=null){
            holder.tag5?.setText("#${tagMap.get(t5)}")
        }
        if(tagMap.get(t6)!=null){
            holder.tag6?.setText("#${tagMap.get(t6)}")
            holder.tag1?.setTextSize(15f)
            holder.tag2?.setTextSize(15f)
            holder.tag3?.setTextSize(15f)
            holder.tag4?.setTextSize(15f)
            holder.tag5?.setTextSize(15f)
            holder.tag6?.setTextSize(15f)
        }

        return view
    }

    fun getStore_name(position: Int):String{
        return ratingList.get(position).store_name
    }

    override fun getItem(position: Int): Any {
        return ratingList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return ratingList.size
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

// 리스트뷰 뷰홀더
class ViewHolderR {
    var storeName: TextView? = null
    var tag1: TextView? = null
    var tag2: TextView? = null
    var tag3: TextView? = null
    var tag4: TextView? = null
    var tag5: TextView? = null
    var tag6: TextView? = null
}

class RatingR( // tag 3개
    val store_name: String,
    val tag1: Double,
    val tag2: Double,
    val tag3: Double,
    val tag4: Double = 0.0,
    val tag5: Double = 0.0,
    val tag6: Double = 0.0
)