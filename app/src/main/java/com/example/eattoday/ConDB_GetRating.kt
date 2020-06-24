package com.example.eattoday

import android.util.Log
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

// 디비 연결을 디비 클레스:AsyncTask 에서 Activity에서 AsyncTask로 호출한 뒤, onPostExecute로 UI에 바로 적용한다.
class DbTaskClass (
    val list:ArrayList<RatingR>,
    val t1:String? = null,
    val t2:String? = null,
    val t3:String? = null,
    val search:String? = null,
    val tableWhich:Boolean   = true
){
    companion object{
        var recodes = ""
        // 메타데이터로 받아옵니다.
//        val dbURL = "jdbc:mysql://210.123.255.206:3306/eating?serverTimezone=UTC"
        val dbURL = MainActivity.mySQLURL
        val dbUSER = MainActivity.mySQLid
        val dbPWD = MainActivity.mySQLpwd
        val tableR = "rating_r" //
        val tableC = "rating_c" //
    }
    fun doDB(){

        val table = if(tableWhich) tableR else tableC
        try{
            val jdbc = Class.forName("com.mysql.jdbc.Driver")
            val conn: Connection = DriverManager.getConnection(dbURL,dbUSER,dbPWD)
            // 나중에 cafe DB도 완성되면 boolean 받아서 카페 음식점 선택되게 변경
            val table = if(tableWhich) tableR else tableC
            val tagList = if(tableWhich) arrayListOf("well", "solo", "cost_effect", "tasty", "atmosphere", "dating") else arrayListOf("sensible", "good", "cost_effect", "tasty", "atmosphere", "dating")
            val info = if(tableWhich) "info_r" else "info_c"
            Log.d("-connection :", "get tag $t1, $t2, $t3")
            val addSearch = "left join $info on $table.store_name = $info.store_name where ($table.store_name like '%$search%' or $info.category like '%$search%') and $info.address like \"서울특별시 노원구%\" "
        val sql =
            if(search != null){
                if (t1 == null && t2 == null && t3 == null)
                    "SELECT distinct  $table.store_name , ${tagList[0]}, ${tagList[1]}, ${tagList[2]}, ${tagList[3]}, ${tagList[4]}, ${tagList[5]}  from $table $addSearch limit 30"
                else if(t1 != null && t2 == null && t3 == null)
                    "SELECT distinct  $table.store_name , $t1, $t2, $t3  from $table $addSearch  order by $t1*1 desc limit 30"
                else if(t1 != null && t2 != null && t3 == null)
                    "SELECT distinct  $table.store_name , $t1, $t2, $t3  from $table $addSearch order by $t1*1 + $t2*0.75 desc limit 30"
                else
                    "SELECT distinct store_name , $t1, $t2, $t3  from $table $addSearch order by $t1*1 + $t2*0.75 + $t3*0.5 desc limit 30"
            }else{
                if(t1 != null && t2 == null && t3 == null)
                    "SELECT distinct store_name , $t1, $t2, $t3  from $table order by $t1*1 desc limit 30"
                else if(t1 != null && t2 != null && t3 == null)
                    "SELECT distinct store_name , $t1, $t2, $t3  from $table order by $t1*1 + $t2*0.75 desc limit 30"
                else
                    "SELECT distinct store_name , $t1, $t2, $t3 from $table order by $t1*1 + $t2*0.75 + $t3*0.5 desc limit 30"
            }
            Log.d("-SQL tag:", "$t1 $t2 $t3")
            Log.d("-SQL :", "$sql")
            val statement: Statement = conn.createStatement()
            val result = statement.executeQuery(sql)
            Log.d("results Size :: " , "${result.fetchSize}")
            while (result.next()){
                recodes += result.getString(1) + " " + result.getString(2) + " " + result.getString(3) + " " + result.getString(4) + "\n"
                // 직접 접근 하지 않고 리스트 값을 받아서 접근하도록 변경
                if(t1 == null){
                    list.add(
                        RatingR(
                            store_name = result.getString(1),
                            tag1 = result.getDouble(2),
                            tag2 = result.getDouble(3),
                            tag3 = result.getDouble(4),
                            tag4 = result.getDouble(5),
                            tag5 = result.getDouble(6),
                            tag6 = result.getDouble(7)
                        )
                    )
                }else{
                    list.add(
                        RatingR(
                            store_name = result.getString(1),
                            tag1 = result.getDouble(2),
                            tag2 = result.getDouble(3),
                            tag3 = result.getDouble(4)
                        )
                    )
                }

            }
            Log.d("-connection :", "values = $recodes")

            Log.d("-mysqlDB", recodes)
            result.close() // 쿼리 종료
            statement.close() // 커넥션 종료

        }catch (e:Exception){
            val error:String = e.toString()
            Log.d("-error1", "MySQL Connection Error :: $error")
        }
    }
}