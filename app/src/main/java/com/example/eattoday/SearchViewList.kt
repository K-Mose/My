package com.example.eattoday

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Adapter
import android.widget.RadioButton
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_search_view_list.*
import kotlinx.android.synthetic.main.activity_search_view_list.search_img
import kotlinx.android.synthetic.main.activity_search_view_list.search_text

class SearchViewList : AppCompatActivity() {
    companion object {
        val searchDbList = arrayListOf<RatingR>()
        val tagList: ArrayList<String> = arrayListOf("", "", "")
        var searchValue: String? = null//arrayListOf<String>()
        val allTagList = if(MainActivity.table) arrayListOf("well", "solo", "cost_effect", "tasty", "atmosphere", "dating") else arrayListOf("sensible", "good", "cost_effect", "tasty", "atmosphere", "dating")
        var button = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_view_list)
        tagList[0] = ""
        tagList[1] = ""
        tagList[2] = ""
        button = false
        searchDbList.clear()
        searchValue = null
        Log.d("-table SearchView", "${MainActivity.table}")
        val tagMap = hashMapOf(
            "잘하는" to "well",
            "맛있는" to "tasty",
            "혼밥" to "solo",
            "분위기" to "atmosphere",
            "데이트" to "dating",
            "가성비" to "cost_effect",
            "well" to "well",
            "tasty" to "tasty",
            "solo" to "solo",
            "atmosphere" to "atmosphere",
            "dating" to "dating",
            "cost_effect" to "cost_effect"
        )
        val search: String = intent.getStringExtra("search")
        val splitedSearchValue = search.split(" ")
        search_text.setText(search)

        var idx = 0
        splitedSearchValue.forEach {
            Log.d("-it it :", "$it")
            if (it.startsWith('#')) {
                Log.d("-it:", "$it")
                tagList.set(idx, it.replace('#', ' ').trim())
            } else {
                searchValue = it
            }
            idx += 1
        }
        Log.d("-it:", "$tagList")
        Log.d("-it map:", "${tagMap[tagList[0]]} ${tagMap[tagList[1]]} ${tagMap[tagList[2]]}")
        val conDB = DBConnector(
            searchDbList,
            tagMap[tagList[0]],
            tagMap[tagList[1]],
            tagMap[tagList[2]],
            searchValue, MainActivity.table
        )
        conDB.execute()

        // 키패드 검색버튼
        //https://developer.android.com/training/keyboard-input/style
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

    // 디비 연결을 AsyncTask로 불러온다.
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
            var adapter: Adapter
            if (tagList[0].equals("")) {
                adapter = RatingView(
                    searchDbList,
                    LayoutInflater.from(this@SearchViewList),
                    t1 = allTagList[0],
                    t2 = allTagList[1],
                    t3 = allTagList[2],
                    t4 = allTagList[3],
                    t5 = allTagList[4],
                    t6 = allTagList[5]
                )

            } else {
                adapter = RatingView(
                    searchDbList, LayoutInflater.from(this@SearchViewList),
                    t1 = tagList[0], t2 = tagList[1], t3 = tagList[2]
                )
            }
            searchList.adapter = adapter

            searchList.setOnItemClickListener { parent, view, position, id ->
                // 어댑터 뷰 : parent, 현재 뷰?: 뷰, 위치: position, long??
                Log.d("-ListViewClick", "${adapter.getStore_name(position)}")
                DbSelected(adapter.getStore_name(position), position).execute()
//                val intentTOmap = Intent(this@SearchViewList, KakaoMap::class.java)
//                startActivity(intentTOmap)
            }
        }

        override fun doInBackground(vararg params: Unit?): String? {
            DbTaskClass(list, t1, t2, t3, search, table).doDB()
            return null
        }
    }

    // packageContext 상속을 위해 inner 클래스로 작성
    inner class DbSelected(val store_name: String, val position: Int) :
        AsyncTask<Unit, Unit, String>() {
        override fun onPostExecute(result: String?) {
            val intentTOmap = Intent(this@SearchViewList, KakaoMap::class.java)
            intentTOmap.putExtra("position", position)
            startActivity(intentTOmap)
            super.onPostExecute(result)
        }

        override fun doInBackground(vararg params: Unit?): String? {
            DbTasksetMapAttribute(
                store_name,
                KakaoMap.mapItemList,
                table = MainActivity.table
            ).doDB()
            return null
        }
    }

    fun actionSearch() {
        if (search_text.text.length < 1) {
            Toast.makeText(this@SearchViewList, "검색어를 입력해 주세요", Toast.LENGTH_SHORT).show()
        } else if (button == false) {
            Toast.makeText(this@SearchViewList, "유형을 선택해 주세요", Toast.LENGTH_SHORT).show()
        } else {
            MainActivity.searchValue = search_text.text.toString()
            intent = Intent(this@SearchViewList, SearchViewList::class.java)
            intent.putExtra("search", MainActivity.searchValue) // name, value
            startActivity(intent)
            finish()
        }
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked

            when (view.getId()) {
                R.id.radio_cafe_SearchView -> {
                    if (checked)
                        MainActivity.table = false
                    button = true
                }
                R.id.radio_restaurant_SearchView -> {
                    if (checked)
                        MainActivity.table = true
                    button = true
                }
                else -> button = false
            }
        }
    }
}
