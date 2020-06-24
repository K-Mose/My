package com.example.eattoday

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        val rList = arrayListOf("well", "solo", "cost_effect", "tasty", "atmosphere", "dating")
        val cList = arrayListOf("sensible", "good", "cost_effect", "tasty", "atmosphere", "dating")
        var tag_r1: String? = null
        var tag_r2: String? = null
        var tag_r3: String? = null
        var tag_c1: String? = null
        var tag_c2: String? = null
        var tag_c3: String? = null
        val rating_List_r = ArrayList<RatingR>()
        val rating_List_c = ArrayList<RatingR>()
        var colorList = arrayListOf<Int>() // 컬러 리스트 생성
        var searchValue: String = ""
        var mySQLURL: String? = ""
        var mySQLid: String? = ""
        var mySQLpwd: String? = ""
        var table = true
        var button = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // meta-data 가져와서 넣기
        button = false
        val appinfo: ApplicationInfo = packageManager.getApplicationInfo(packageName, 128) // 플래그 에러 무시 가능
        mySQLURL = appinfo.metaData.getString("com.example.eattoday.DbTaskClass.dbURL")
        mySQLid = appinfo.metaData.getString("com.example.eattoday.DbTaskClass.dbID")
        mySQLpwd = appinfo.metaData.getString("com.example.eattoday.DbTaskClass.dbPWD")

        // 태그 섞은 뒤 변수에 넣기
        rList.shuffle()
        cList.shuffle()
        tag_r1 = rList[0]
        tag_r2 = rList[1]
        tag_r3 = rList[2]
        tag_c1 = cList[0]
        tag_c2 = cList[1]
        tag_c3 = cList[2]

        // 라디오버튼
        Log.d("-table :: ", "$table")

        setContentView(R.layout.activity_main)
        // 컬러 리스트 값 초기화 해줌
        colorList = arrayListOf(
            getColor(R.color.h100),
            getColor(R.color.h90),
            getColor(R.color.h80),
            getColor(R.color.h70),
            getColor(R.color.h60),
            getColor(R.color.h50),
            getColor(R.color.h40),
            getColor(R.color.h30),
            getColor(R.color.h20),
            getColor(R.color.h10),
            getColor(R.color.h05),
            getColor(R.color.h00)
        )

        // 디비 생성
        Log.d("-pre-connection :", "con = $tag_r1 $tag_r2 $tag_r3")
        val taskR = DBConnector(rating_List_r, tag_r1, tag_r2, tag_r3, table = true)
        val taskC = DBConnector(rating_List_c, tag_c1, tag_c2, tag_c3, table = false)
        Log.d("-connection execute:", "${taskR.execute()}")
        Log.d("-connection execute:", "${taskC.execute()}")


        // 탭 레이아웃 탭 추가
        eatTab.addTab(eatTab.newTab().setText("음식점"))
        eatTab.addTab(eatTab.newTab().setText("카페"))


        // 탭 클릭하면 페이지도 바뀌게
        eatTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                rating_pager.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                rating_pager.currentItem = tab!!.position
            }
        })

        // 페이지가 이동했을 때 탭도 이동
        rating_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(eatTab))

        //https://developer.android.com/training/keyboard-input/style
        // 키패드 검색 버튼 클릭
        search_text.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                (EditorInfo.IME_ACTION_SEARCH) -> {
                    actionSearch()
                    true
                }
                else -> {
                    false
                }
            }

        }

        // 검색 클릭
        search_img.setOnClickListener {
            // Intent(this, to Activity)
            actionSearch()
        }


    }

    fun actionSearch() {
        if (search_text.text.length < 1) {
            Toast.makeText(this@MainActivity, "검색어를 입력해 주세요", Toast.LENGTH_SHORT).show()
        } else if (button == false) {
            Toast.makeText(this@MainActivity, "유형을 선택해 주세요", Toast.LENGTH_SHORT).show()
        } else {
            searchValue = search_text.text.toString()
            intent = Intent(this@MainActivity, SearchViewList::class.java)
            intent.putExtra("search", searchValue) // name, value
            startActivity(intent)
        }
    }

    // 리스트 불러오는 디비 연결하는고
    inner class DBConnector(
        val list: ArrayList<RatingR>,
        val t1: String? = null,
        val t2: String? = null,
        val t3: String? = null,
        val search: String? = null,
        val table: Boolean
    ) : AsyncTask<Unit, Unit, String>() {
        override fun onPostExecute(result: String?) {
            //  onPostExecute에서 UI를 업데이트 한다.
            val pagerAdapter = PagerAdapter(supportFragmentManager, 2, context = this@MainActivity)
            rating_pager.adapter = pagerAdapter
        }

        override fun doInBackground(vararg params: Unit?): String? {
            DbTaskClass(list, t1, t2, t3, search, table).doDB()
            return null
        }
    }



    // 페이저 어뎁터
    inner class PagerAdapter(
        fragmentManager: FragmentManager,
        val tabCount: Int,
        val context: Context
    ) : FragmentStatePagerAdapter(fragmentManager) {
        override fun getItem(position: Int): Fragment {
            // 프래그먼트에 값을 넘겨주어야??
            when (position) {
                0 -> {
                    table = true
                    return Fragment(rating_List_r, tag_r1, tag_r2, tag_r3, this@MainActivity, table)
                }
                1 -> {
                    table = false
                    return Fragment(rating_List_c, tag_c1, tag_c2, tag_c3, this@MainActivity, table)
                }
                else -> {
                    table = true
                    return Fragment(rating_List_r, tag_r1, tag_r2, tag_r3, this@MainActivity, table)
                }
            }
        }

        override fun getCount(): Int {
            return tabCount
        }
    }

    fun onRadioButtonClicked(view:View){
        if (view is RadioButton){
            val checked = view.isChecked

            when(view.getId()){
                R.id.radio_cafe ->{
                    if(checked)
                        table = false
                    button = true
                }
                R.id.radio_restaurant ->{
                    if (checked)
                        table = true
                    button = true
                }
                else -> button = false
            }
        }
    }
}
