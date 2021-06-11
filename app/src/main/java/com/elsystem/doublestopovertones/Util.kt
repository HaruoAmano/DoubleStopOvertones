package com.elsystem.doublestopovertones

import android.view.View
import android.widget.ImageView
import java.util.*

class Util {
    private val scaleTable = ScaleTable()
    //再描画関数
    //    音符が初期状態の位置に戻ってしまうことを防ぐため。
    //    音符の画像を切り替える（♭♮#が押された）際はこの関数を呼び出すこと。
    fun reLayout(ivNote: ImageView) {
        ivNote.layout(
            ivNote.left,
            ivNote.top,
            ivNote.left + ivNote.width,
            ivNote.top + ivNote.height
        )
    }

    //画面上の指定Step位置に音符を移動する。
    fun moveNote(ivNote: ImageView, step: Int) {
        ivNote.layout(
            ivNote.left,
            step,
            ivNote.left + ivNote.width,
            step + ivNote.height
        )
    }

    //*****画面上の物理的な位置から実際の音高（１２進）を導出する。******************************
    fun convPhysicalstep2Chromatictone(ivNote: View, accidentalSign: Int): String {
        //画面上の音符の位置より、chromaticTone(12進)を導出する。
        //①画面上の音符位置からscaleTableに管理されているchromaticToneを導出する。
        //以下でフラットの場合は0.5、シャープの場合は-0.5を加算する。（ + accidentalSign.toFloat() / 2f )
        //（Stepは小さいほど音が高くなる点に注意！）
        //fmt  0:ト音記号　1:ハ音記号　2:へ音記号
        var chromaticStep = 0f
        when (noteFormat) {
            0 -> chromaticStep =
                ((ivNote.top + noteHeightHalf) / oneLineHeight).toInt() + (accidentalSign.toFloat() / 2f) + 15
            1 -> chromaticStep =
                ((ivNote.top + noteHeightHalf) / oneLineHeight).toInt() + (accidentalSign.toFloat() / 2f) + 21
            2 -> chromaticStep =
                ((ivNote.top + noteHeightHalf) / oneLineHeight).toInt() + (accidentalSign.toFloat() / 2f) + 27
        }
        //もし下記でテーブルにHITしなかった場合、結果が明確に出るよう80を設定する。
        var chromaticTone = "80"
        //②ScaleTableよりchromaticStepに該当するchromaticTone（１２進）を取得する。
        for (i in 0..95) {
            if (scaleTable.chromaticStep[i] == chromaticStep) {
                chromaticTone = scaleTable.chromaticTone[i]
                break
            }
        }
        return chromaticTone
    }


//*****12進同士の加算を行う。**************************************
//加算はカレンダーで処理させている。
//インプットは１２進形式(ex."4A")で、カレンダーに渡すために１０進＋１０進に変換する。(ex."04" "11")
//twelve1, twelve2とも上記変換後カレンダーにひき渡す。
//カレンダーで加算後、１２進形式に戻してリターンする。
    fun additionTwelve(twelve1: String, twelve2: String): String {
        //12進を1桁目と2桁目に分解。
        val charArray1: CharArray = twelve1.toCharArray()
        val placeTen1: String = charArray1[0].toString()
        val placeOne1: String = charArray1[1].toString()
        val charArray2: CharArray = twelve2.toCharArray()
        val placeTen2: String = charArray2[0].toString()
        val placeOne2: String = charArray2[1].toString()

        //12進2桁目を２桁の10進に変換。
        val placeTen2digit1 = conv1to2digit(placeTen1)
        val placeTen2digit2 = conv1to2digit(placeTen2)
        //12進1桁目を２桁の10進に変換。
        val placeOne2digit1 = conv1to2digit(placeOne1)
        val placeOne2digit2 = conv1to2digit(placeOne2)

        //カレンダーを使用して12進の加減算を行う。
        val inpYear = placeTen2digit1 + 2000
        val calender = Calendar.getInstance()
            calender.set(inpYear, placeOne2digit1, 1)
            calender.add(Calendar.YEAR, placeTen2digit2)
            calender.add(Calendar.MONTH, placeOne2digit2)
        val outYear = calender.get(Calendar.YEAR) - 2000
        val outMonth = calender.get(Calendar.MONTH)

        //10進2桁を12進の2桁目に変換。
        val digitTwo10Place = conv2to1digit(outYear)
        //10進2桁を12進の1桁目に変換。
        val digitTwo1place = conv2to1digit(outMonth)

        return digitTwo10Place + digitTwo1place

    }

    //12進（１桁）を10進（２桁）に変換。(ex.A→10)
    private fun conv1to2digit(oneDigit: String): Int {
        return when (oneDigit) {
            "A" -> 10
            "B" -> 11
            else -> oneDigit.toInt()
        }
    }

    //10進（２桁）を12進（１桁）に変換。(ex.10→A)
    private fun conv2to1digit(_2digit: Int): String {
        return when (_2digit) {
            10 -> "A"
            11 -> "B"
            else -> _2digit.toString()
        }
    }

    companion object {
        private const val tagMsg = "MyInfo_Util : "
    }
}

