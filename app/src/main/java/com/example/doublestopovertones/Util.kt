package com.example.doublestopovertones

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.doublestopoverTone.ScaleTable
import java.util.*

class Util {
    val scaleTable = ScaleTable()

    //再描画関数
    //    音符が初期状態の位置に戻ってしまうことを防ぐため。
    //    音符の画像を切り替える（♭♮#が押された）際はこの関数を呼び出すこと。
    fun reLayout(ivNote: ImageView) {
        ivNote.layout(
            ivNote.getLeft(),
            ivNote.getTop(),
            ivNote.getLeft() + ivNote.getWidth(),
            ivNote.getTop() + ivNote.getHeight()
        )
    }

    //画面上の指定Step位置に音符を移動する・
    fun moveNote(ivNote: ImageView, step: Int) {
        ivNote.layout(
            ivNote.getLeft(),
            step,
            ivNote.getLeft() + ivNote.getWidth(),
            step + ivNote.getHeight()
        )
    }

    //*****画面上の物理的な位置から実際の音高（１２進）を導出する。******************************
    fun convPhisicalstep2Chromatictone(ivNote: View, fmt: Int, accidentalSign: Int): String {
        //画面上の音符の位置より、chromaticTone(12進)を導出する。
        //①画面上の音符位置からscaleTableに管理されているchromaticToneを導出する。
        //以下でフラットの場合は0.5、シャープの場合は-0.5を加算する。（ + accidentalSign.toFloat() / 2f )
        //（Stepは小さいほど音が高くなる点に注意！）
        //fmt  0:ト音記号　1:ハ音記号　2:へ音記号
        var chromaticStep = 0f
        Log.i("phisical", "ivNote.top : ${ivNote.top.toInt()}")
        Log.i("phisical", "step: ${((ivNote.top + ivNoteHeightHalf) / oneLineHeight)}")
        when (fmt) {
            0 -> chromaticStep =
                ((ivNote.top + ivNoteHeightHalf) / oneLineHeight).toInt() + (accidentalSign.toFloat() / 2f) + 15
            1 -> chromaticStep =
                ((ivNote.top + ivNoteHeightHalf) / oneLineHeight).toInt() + (accidentalSign.toFloat() / 2f) + 21
            2 -> chromaticStep =
                ((ivNote.top + ivNoteHeightHalf) / oneLineHeight).toInt() + (accidentalSign.toFloat() / 2f) + 27
        }
        Log.i("phisical", "chromaticStep : ${chromaticStep}")

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

    //臨時記号のボタンの表示・非表示を制御する。
    //引き渡された画面上の情報から表示されている音を導出し、選択可能なボタンのみ表示するようにする。
    fun ctlAccidentalBtnVisible(
        ivNote: ImageView,
        fmt: Int,
        accidentalSign: Int,
        viewGroup: ViewGroup
    ) {
        val ivNote1st = viewGroup.findViewById<ImageView>(R.id.ivNote1st)
        val ivNote2nd = viewGroup.findViewById<ImageView>(R.id.ivNote2nd)
        var ivFlat1st = viewGroup.findViewById<ImageView>(R.id.ivFlat1st)
        var ivNatural1st = viewGroup.findViewById<ImageView>(R.id.ivNatural1st)
        var ivSharp1st = viewGroup.findViewById<ImageView>(R.id.ivSharp1st)
        var ivFlat2nd = viewGroup.findViewById<ImageView>(R.id.ivFlat2nd)
        var ivNatural2nd = viewGroup.findViewById<ImageView>(R.id.ivNatural2nd)
        var ivSharp2nd = viewGroup.findViewById<ImageView>(R.id.ivSharp2nd)
        //引き渡された情報から、chrromaticTone（12進)を導出する。
        var chromaticTone = convPhisicalstep2Chromatictone(ivNote, fmt, accidentalSign)
        //12進を1桁目と2桁目に分解。
        var charArray: CharArray = chromaticTone.toCharArray()
        var _1Place: String = charArray[1].toString()
        //選択可能なボタンのみを表示する。
        var ivFlat = ivFlat1st
        var ivNatural = ivNatural1st
        var ivSharp = ivSharp1st
        if (ivNote != ivNote1st) {
            ivFlat = ivFlat2nd
            ivNatural = ivNatural2nd
            ivSharp = ivSharp2nd
        }
        ivFlat.visibility = View.VISIBLE
        ivNatural.visibility = View.VISIBLE
        ivSharp.visibility = View.VISIBLE
        when (accidentalSign) {
            //b
            1 -> {
                ivFlat.visibility = View.INVISIBLE
                when (_1Place) {
                    "3", "A" ->
                        ivSharp.visibility = View.INVISIBLE
                }
            }
            //♮
            0 -> {
                ivNatural.visibility = View.INVISIBLE
                when (_1Place) {
                    "0", "5" -> ivFlat.visibility = View.INVISIBLE
                    "4", "B" -> ivSharp.visibility = View.INVISIBLE
                }
            }
            -1 -> {
                ivSharp.visibility = View.INVISIBLE
                when (_1Place) {
                    "1", "6" ->
                        ivFlat.visibility = View.INVISIBLE
                }
            }
        }
    }


//*****12進同士の加算を行う。**************************************
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
        val _10Place2digit1 = conv1to2degit(_10Place1)
        val _10Place2digit2 = conv1to2degit(_10Place2)
        //12進1桁目を２桁の10進に変換。
        val _1Place2digit1 = conv1to2degit(_1Place1)
        val _1Place2digit2 = conv1to2degit(_1Place2)

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
        val _2digit10Place = conv2to1degit(outYear)
        //10進2桁を12進の1桁目に変換。
        val _2digit1place = conv2to1degit(outMonth)

        return _2digit10Place + _2digit1place

    }

    //12進（１桁）を10進（２桁）に変換。(ex.A→10)
    private fun conv1to2degit(_1digit: String): Int {
        val _2digit: Int = when (_1digit) {
            "A" -> 10
            "B" -> 11
            else -> _1digit.toInt()
        }
        return _2digit
    }

    //10進（２桁）を12進（１桁）に変換。(ex.10→A)
    private fun conv2to1degit(_2digit: Int): String {
        val _1digit: String = when (_2digit) {
            10 -> "A"
            11 -> "B"
            else -> _2digit.toString()
        }
        return _1digit
    }

    companion object {
        private const val tagMsg = "MyInfo_Util : "
    }
}

