package com.example.doublestopovertones.com.example.doublestopovertones

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.doublestopovertones.*

class DisplayCtl (viewGroup: ViewGroup){
    val util = Util()
    val ivNote1st = viewGroup.findViewById<ImageView>(R.id.ivNote1st)
    val ivNote2nd = viewGroup.findViewById<ImageView>(R.id.ivNote2nd)
    val tvDesc = viewGroup.findViewById<TextView>(R.id.TvDesc)
    val tvBack3rd = viewGroup.findViewById<TextView>(R.id.tvBack3rd)
    val firstNoteView = viewGroup.findViewById<View>(R.id.FirstNoteView)
    val secondNoteView = viewGroup.findViewById<View>(R.id.SecondNoteView)
    val thirdNoteView = viewGroup.findViewById<View>(R.id.ThirdNoteView)
    val ivClef3rd =viewGroup.findViewById<ImageView>(R.id.ivClef3rd)
    val ivNote3rd1 = viewGroup.findViewById<ImageView>(R.id.ivNote3rd1)
    val ivNote3rd2 = viewGroup.findViewById<ImageView>(R.id.ivNote3rd2)
    val ivNote3rd3 = viewGroup.findViewById<ImageView>(R.id.ivNote3rd3)
    val ivNote3rd4 = viewGroup.findViewById<ImageView>(R.id.ivNote3rd4)
    val ivNote3rd5 = viewGroup.findViewById<ImageView>(R.id.ivNote3rd5)
    val btnSpeaker1 = viewGroup.findViewById<Button>(R.id.btnSpeaker1)
    val btnSpeaker2 = viewGroup.findViewById<Button>(R.id.btnSpeaker2)
    val btnSpeaker3 = viewGroup.findViewById<Button>(R.id.btnSpeaker3)
    val btnSpeaker4 = viewGroup.findViewById<Button>(R.id.btnSpeaker4)
    val btnSpeaker5 = viewGroup.findViewById<Button>(R.id.btnSpeaker5)
    val tvDesc1st1 = viewGroup.findViewById<TextView>(R.id.tvDesc1st1)
    val tvDesc1st2 = viewGroup.findViewById<TextView>(R.id.tvDesc1st2)
    val tvDesc1st3 = viewGroup.findViewById<TextView>(R.id.tvDesc1st3)
    val tvDesc1st4 = viewGroup.findViewById<TextView>(R.id.tvDesc1st4)
    val tvDesc1st5 = viewGroup.findViewById<TextView>(R.id.tvDesc1st5)
    val tvDesc2nd1 = viewGroup.findViewById<TextView>(R.id.tvDesc2nd1)
    val tvDesc2nd2 = viewGroup.findViewById<TextView>(R.id.tvDesc2nd2)
    val tvDesc2nd3 = viewGroup.findViewById<TextView>(R.id.tvDesc2nd3)
    val tvDesc2nd4 = viewGroup.findViewById<TextView>(R.id.tvDesc2nd4)
    val tvDesc2nd5 = viewGroup.findViewById<TextView>(R.id.tvDesc2nd5)
    val thirdNoteTitle = viewGroup.findViewById<TextView>(R.id.thirdNoteTitle)
    val ivFlat1st = viewGroup.findViewById<ImageView>(R.id.ivFlat1st)
    val ivNatural1st = viewGroup.findViewById<ImageView>(R.id.ivNatural1st)
    val ivSharp1st = viewGroup.findViewById<ImageView>(R.id.ivSharp1st)
    val ivFlat2nd = viewGroup.findViewById<ImageView>(R.id.ivFlat2nd)
    val ivNatural2nd = viewGroup.findViewById<ImageView>(R.id.ivNatural2nd)
    val ivSharp2nd = viewGroup.findViewById<ImageView>(R.id.ivSharp2nd)
    val btnBack = viewGroup.findViewById<Button>(R.id.btnBack)
    val btnOvertone = viewGroup.findViewById<Button>(R.id.btnOvertone)
    //音符選択画面とオーバートン画面切り替え時の個々Viewの表示設定を行う。
    fun modeChange(viewGroup: ViewGroup, mode : String){


        if (mode == "select") {
            //セレクト画面（初期画面）の画面表示を指定する。

            //最初にオーバートーン画面（のみ）項目を削除する。
            //音符
            ivNote3rd1.visibility = View.INVISIBLE
            ivNote3rd2.visibility = View.INVISIBLE
            ivNote3rd3.visibility = View.INVISIBLE
            ivNote3rd4.visibility = View.INVISIBLE
            ivNote3rd5.visibility = View.INVISIBLE
            //インターバル文言
            tvDesc1st1.visibility = View.INVISIBLE
            tvDesc1st2.visibility = View.INVISIBLE
            tvDesc1st3.visibility = View.INVISIBLE
            tvDesc1st4.visibility = View.INVISIBLE
            tvDesc1st5.visibility = View.INVISIBLE
            tvDesc2nd1.visibility = View.INVISIBLE
            tvDesc2nd2.visibility = View.INVISIBLE
            tvDesc2nd3.visibility = View.INVISIBLE
            tvDesc2nd4.visibility = View.INVISIBLE
            tvDesc2nd5.visibility = View.INVISIBLE
            //五線譜
            thirdNoteView.visibility = View.INVISIBLE
            //音部記号
            ivClef3rd.visibility = View.INVISIBLE
            //見出し
            thirdNoteTitle.visibility = View.INVISIBLE
            //スピーカーボタン
            btnSpeaker1.visibility = View.INVISIBLE
            btnSpeaker2.visibility = View.INVISIBLE
            btnSpeaker3.visibility = View.INVISIBLE
            btnSpeaker4.visibility = View.INVISIBLE
            btnSpeaker5.visibility = View.INVISIBLE
            //オーバートーンボタン
            btnOvertone.visibility = View.VISIBLE
            //戻るボタン
            btnBack.visibility = View.INVISIBLE

            //説明文
            tvDesc.visibility = View.VISIBLE

            //背景色を変更
            firstNoteView.setBackgroundColor(Color.parseColor(MY_PAPER_YELLOW))
            secondNoteView.setBackgroundColor(Color.parseColor(MY_PAPER_YELLOW))
            tvBack3rd.setBackgroundColor(Color.parseColor(MY_BLUE_GRAY))
        }else{
            //オーバートーン画面の画面表示を規定する。
            //ivNote,tvDesc1st・2ndについては、個々に判断しながらVISIBLEに変更していくため
            //ここでは一旦全てINVISIBLEとする。
            ivNote3rd1.visibility = View.INVISIBLE
            ivNote3rd2.visibility = View.INVISIBLE
            ivNote3rd3.visibility = View.INVISIBLE
            ivNote3rd4.visibility = View.INVISIBLE
            ivNote3rd5.visibility = View.INVISIBLE
            tvDesc1st1.visibility = View.INVISIBLE
            tvDesc1st2.visibility = View.INVISIBLE
            tvDesc1st3.visibility = View.INVISIBLE
            tvDesc1st4.visibility = View.INVISIBLE
            tvDesc1st5.visibility = View.INVISIBLE
            tvDesc2nd1.visibility = View.INVISIBLE
            tvDesc2nd2.visibility = View.INVISIBLE
            tvDesc2nd3.visibility = View.INVISIBLE
            tvDesc2nd4.visibility = View.INVISIBLE
            tvDesc2nd5.visibility = View.INVISIBLE

            //説明文を非表示にする。
            tvDesc.visibility = View.INVISIBLE

            //第３音五線譜
            thirdNoteView.visibility = View.VISIBLE
            //第３音音部記号
            ivClef3rd.visibility = View.VISIBLE
            //第３音見出し
            thirdNoteTitle.visibility = View.VISIBLE
            //スピーカーボタンを表示する。（PreferenceでONになっていた場合）
//            if (prefSpBtnDisp) {
                btnSpeaker1.visibility = View.VISIBLE
                btnSpeaker2.visibility = View.VISIBLE
                btnSpeaker3.visibility = View.VISIBLE
                btnSpeaker4.visibility = View.VISIBLE
                btnSpeaker5.visibility = View.VISIBLE
//            }
            //オーバートーンボタン
            btnOvertone.visibility = View.INVISIBLE
            //戻るボタン
            btnBack.visibility = View.VISIBLE

            //背景色を変更
            firstNoteView.setBackgroundColor(Color.parseColor(MY_BLUE_GRAY))
            secondNoteView.setBackgroundColor(Color.parseColor(MY_BLUE_GRAY))
            tvBack3rd.setBackgroundColor(Color.parseColor(MY_PAPER_YELLOW))
        }
    }
    //臨時記号のボタンの表示・非表示を制御する。
    //引き渡された画面上の情報から表示されている音を導出し、選択可能なボタンのみ表示するようにする。
    fun ctlAccidentalBtnVisible(
        ivNote: ImageView,
        fmt: Int,
        accidentalSign: Int,
        viewGroup: ViewGroup) {

        //引き渡された情報から、chromaticTone（12進)を導出する。
        val chromaticTone = util.convPhysicalstep2Chromatictone(ivNote, fmt, accidentalSign)
        //12進を1桁目と2桁目に分解。
        val charArray: CharArray = chromaticTone.toCharArray()
        val placeOne: String = charArray[1].toString()
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
                when (placeOne) {
                    "3", "A" ->
                        ivSharp.visibility = View.INVISIBLE
                }
            }
            //♮
            0 -> {
                ivNatural.visibility = View.INVISIBLE
                when (placeOne) {
                    "0", "5" -> ivFlat.visibility = View.INVISIBLE
                    "4", "B" -> ivSharp.visibility = View.INVISIBLE
                }
            }
            -1 -> {
                ivSharp.visibility = View.INVISIBLE
                when (placeOne) {
                    "1", "6" ->
                        ivFlat.visibility = View.INVISIBLE
                }
            }
        }
    }


}