package com.example.doublestopovertones

import java.util.*

class TwelveArray {
    //12進同士の加算を行う。
    //加算はカレンダーで処理させている。
    //インプットは１２進形式(ex."4A")で、カレンダーに渡すために１０進＋１０進に変換する。(ex."04" "11")
    //twelve1, twelve2とも上記変換後カレンダーにひき渡す。
    //カレンダーで加算後、１２進形式に戻してリターンする。
    fun additionTwelve(twelve1: String, twelve2: String): String {
        //12進を1桁目と2桁目に分解。
        val charArray1: CharArray = twelve1.toCharArray()
        val _10Place1: String = charArray1[0].toString()
        val _1Place1: String = charArray1[1].toString()
        val charArray2: CharArray = twelve2.toCharArray()
        val _10Place2: String = charArray2[0].toString()
        val _1Place2: String = charArray2[1].toString()

        //12進2桁目を２桁の10進に変換。
        val _10Place2digit1 = convert1to2degit(_10Place1)
        val _10Place2digit2 = convert1to2degit(_10Place2)
        //12進1桁目を２桁の10進に変換。
        val _1Place2digit1 = convert1to2degit(_1Place1)
        val _1Place2digit2 = convert1to2degit(_1Place2)

        //カレンダーを使用して12進の加減算を行う。
        val inpYear = _10Place2digit1 + 2000
        val inpMonth = _1Place2digit1
        val calender = Calendar.getInstance()
        calender.set(inpYear, inpMonth, 1)
            calender.add(Calendar.YEAR, _10Place2digit2)
            calender.add(Calendar.MONTH, _1Place2digit2)
        val outYear = calender.get(Calendar.YEAR) - 2000
        val outMonth = calender.get(Calendar.MONTH)

        //10進2桁を12進の2桁目に変換。
        val _2digit10Place = convert2to1degit(outYear)
        //10進2桁を12進の1桁目に変換。
        val _2digit1place = convert2to1degit(outMonth)

        return _2digit10Place + _2digit1place

    }
//    //12進(4B etc.)をもとに10進の加算を行う。結果は12進で返す。
//    //javaのカレンダーは０月から始まる点、要注意！！
//    fun findDistanceDecimal(twelve: String, decimal: Int): String {
//        //12進を1桁目と2桁目に分解。
//        val charArray: CharArray = twelve.toCharArray()
//        val tenPlace: String = charArray[0].toString()
//        val onePlace: String = charArray[1].toString()
//
//        //12進2桁目を２桁の10進に変換。
//        val tenPlace2digit = convert1to2degit(tenPlace)
//        //12進1桁目を２桁の10進に変換。
//        val onePlace2digit = convert1to2degit(onePlace)
//
//        //カレンダーを使用して12進の加減算を行う。
//        val inpYear = 2000 + tenPlace2digit
//        val inpMonth = onePlace2digit
//        val calender = Calendar.getInstance()
//        calender.set(inpYear, inpMonth, 1)
//        calender.add(Calendar.MONTH, decimal)
//        val outYear = calender.get(Calendar.YEAR) - 2000
//        val outMonth = calender.get(Calendar.MONTH)
//
//        //10進2桁を12進の2桁目に変換。
//        val _1digittenPlace = convert2to1degit(outYear)
//        //10進2桁を12進の1桁目に変換。
//        val _1digit1place = convert2to1degit(outMonth)
//
//        return _1digittenPlace + _1digit1place
//    }
//
//    //12進同士の加減算を行う。
//    //加減算はカレンダーで処理させている。
//    //インプットは１２進形式(ex."4A")で、カレンダーに渡すために１０進＋１０進に変換する。(ex."04" "11")
//    //twelve1, twelve2とも上記変換後カレンダーにひき渡す。
//    //カレンダーで加減算後、１２進形式に戻してリターンする。
//    fun findDistanceTwelve(twelve1: String, plusMinus: String, twelve2: String): String {
//        //12進を1桁目と2桁目に分解。
//        val charArray1: CharArray = twelve1.toCharArray()
//        val tenPlace1: String = charArray1[0].toString()
//        val onePlace1: String = charArray1[1].toString()
//        val charArray2: CharArray = twelve2.toCharArray()
//        val tenPlace2: String = charArray2[0].toString()
//        val onePlace2: String = charArray2[1].toString()
//
//        //12進2桁目を２桁の10進に変換。
//        val tenPlace2digit1 = convert1to2degit(tenPlace1)
//        val tenPlace2digit2 = convert1to2degit(tenPlace2)
//        //12進1桁目を２桁の10進に変換。
//        val onePlace2digit1 = convert1to2degit(onePlace1)
//        val onePlace2digit2 = convert1to2degit(onePlace2)
//
//        //カレンダーを使用して12進の加減算を行う。
//        val inpYear = tenPlace2digit1 + 2000
//        val inpMonth = onePlace2digit1
//        val calender = Calendar.getInstance()
//        calender.set(inpYear, inpMonth, 1)
//        if(plusMinus == "+") {
//            calender.add(Calendar.YEAR, tenPlace2digit2)
//            calender.add(Calendar.MONTH, onePlace2digit2)
//        } else {
//            calender.add(Calendar.YEAR, - tenPlace2digit2)
//            calender.add(Calendar.MONTH, - onePlace2digit2)
//        }
//        val outYear = calender.get(Calendar.YEAR) - 2000
//        val outMonth = calender.get(Calendar.MONTH)
//
//        //10進2桁を12進の2桁目に変換。
//        val _2digittenPlace = convert2to1degit(outYear)
//        //10進2桁を12進の1桁目に変換。
//        val _2digit1place = convert2to1degit(outMonth)
//
//        return _2digittenPlace + _2digit1place
//
//    }
    //12進（１桁）を10進（２桁）に変換。(ex.A→10)
    private fun convert1to2degit(_1digit: String): Int {
        val _2digit: Int = when (_1digit) {
            "A" -> 10
            "B" -> 11
            else -> _1digit.toInt()
        }
        return _2digit
    }
    //10進（２桁）を12進（１桁）に変換。(ex.10→A)
    private fun convert2to1degit(_2digit: Int): String {
        val _1digit: String = when (_2digit) {
            10 -> "A"
            11 -> "B"
            else -> _2digit.toString()
        }
        return _1digit
    }
}
