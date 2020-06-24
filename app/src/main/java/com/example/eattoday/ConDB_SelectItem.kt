package com.example.eattoday

import android.util.Log
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

class DbTasksetMapAttribute(val store_name: String, val list: ArrayList<MapItem>, val table:Boolean) {
    companion object {
        var recodes = ""
        val dbURL = MainActivity.mySQLURL
        val dbUSER = MainActivity.mySQLid
        val dbPWD = MainActivity.mySQLpwd
        val tableR = "rating_r" //
        val tableC = "rating_c" //
    }

    fun doDB() {
        try {
            val jdbc = Class.forName("com.mysql.jdbc.Driver")
            val conn: Connection = DriverManager.getConnection(
                dbURL,
                dbUSER,
                dbPWD
            )
            Log.d("-connection_c :", "values = $store_name")
            val tableStore = if(table) tableR else tableC
            val tableInfo = if(table) "info_r" else "info_c"
            val sql =
                "SELECT distinct $tableStore.store_name, $tableInfo.tel , $tableInfo.address , $tableInfo.longitude , $tableInfo.latitude, $tableInfo.branch, $tableInfo.category from $tableStore left join $tableInfo " +
                        "on $tableStore.store_name = $tableInfo.store_name where $tableStore.store_name = '$store_name'  and $tableInfo.address like \"서울특별시 노원구%\""

            Log.d("-connection_c SQL :", "sql = $sql")
            val statement: Statement = conn.createStatement()
            val result = statement.executeQuery(sql)

            result.next()
            KakaoMap.store_name = result.getString(1)
            KakaoMap.tel = result.getString(2)
            KakaoMap.address = result.getString(3)
            KakaoMap.longitude = result.getString(4).toDouble()
            KakaoMap.latitude = result.getString(5).toDouble()
            KakaoMap.category = result.getString(7)

            Log.d(
                "-connection_c Value :",
                "값: ${result.getString(1)} ${result.getString(2)} ${result.getString(3)} ${result.getString(
                    4
                )} ${result.getString(5)} ${result.getString(6)} ${result.getString(7)}"
            )
            list.clear() // 리스트 초기화

            // 다시 처음으로
            result.beforeFirst()

            while (result.next()) {
                // 1.이름  2.전화번호  3.주소  4.위도  5.경도  6.지점  7.카테고리
                list.add(
                    MapItem(
                        store_name = result.getString(1),
                        tel = result.getString(2),
                        address = result.getString(3),
                        longitude = result.getString(4).toDouble(),
                        latitude = result.getString(5).toDouble(),
                        branch = result.getString(6) // 없으면 null
                    )
                )
                recodes += result.getString(1) + " " + result.getString(6) + "\n"
            }
            Log.d("-connection_c recodes :", "레코드값 :: \n$recodes")

            result.close() // 쿼리 종료
            statement.close() // 커넥션 종료
        } catch (e: Exception) {
            val error: String = e.toString()
            Log.d("-error2", "MySQL Connection Error :: $error")
        }
    }
}