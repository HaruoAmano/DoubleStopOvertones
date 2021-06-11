package com.elsystem.doublestopovertones

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.elsystem.doublestopovertones.*

class ControlDisplay (viewGroup: ViewGroup){
    private val util = Util()
    private val viewGroup = viewGroup.findViewById<ViewGroup>(R.id.viewGroup)
    private val ivNote1st = viewGroup.findViewById<ImageView>(R.id.ivNote1st)
    private val ivNote2nd = viewGroup.findViewById<ImageView>(R.id.ivNote2nd)
    private val appTitle = viewGroup.findViewById<TextView>(R.id.appTitle)
    private val tvDesc = viewGroup.findViewById<TextView>(R.id.TvDesc)
    private val firstNoteView = viewGroup.findViewById<View>(R.id.FirstNoteView)
    private val secondNoteView = viewGroup.findViewById<View>(R.id.SecondNoteView)
    private val thirdNoteView = viewGroup.findViewById<View>(R.id.ThirdNoteView)
    private val ivClef3rd = viewGroup.findViewById<ImageView>(R.id.ivClef3rd)
    private val ivNote3rd1 = viewGroup.findViewById<ImageView>(R.id.ivNote3rd1)
    private val ivNote3rd2 = viewGroup.findViewById<ImageView>(R.id.ivNote3rd2)
    private val ivNote3rd3 = viewGroup.findViewById<ImageView>(R.id.ivNote3rd3)
    private val ivNote3rd4 = viewGroup.findViewById<ImageView>(R.id.ivNote3rd4)
    private val ivNote3rd5 = viewGroup.findViewById<ImageView>(R.id.ivNote3rd5)
    private val ivSelClef = viewGroup.findViewById<ImageView>(R.id.ivSelClef)
    private val btnSpeaker1 = viewGroup.findViewById<Button>(R.id.btnSpeaker1)
    private val btnSpeaker2 = viewGroup.findViewById<Button>(R.id.btnSpeaker2)
    private val btnSpeaker3 = viewGroup.findViewById<Button>(R.id.btnSpeaker3)
    private val btnSpeaker4 = viewGroup.findViewById<Button>(R.id.btnSpeaker4)
    private val btnSpeaker5 = viewGroup.findViewById<Button>(R.id.btnSpeaker5)
    private val tvDesc1st1 = viewGroup.findViewById<TextView>(R.id.tvDesc1st1)
    private val tvDesc1st2 = viewGroup.findViewById<TextView>(R.id.tvDesc1st2)
    private val tvDesc1st3 = viewGroup.findViewById<TextView>(R.id.tvDesc1st3)
    private val tvDesc1st4 = viewGroup.findViewById<TextView>(R.id.tvDesc1st4)
    private val tvDesc1st5 = viewGroup.findViewById<TextView>(R.id.tvDesc1st5)
    private val tvDesc2nd1 = viewGroup.findViewById<TextView>(R.id.tvDesc2nd1)
    private val tvDesc2nd2 = viewGroup.findViewById<TextView>(R.id.tvDesc2nd2)
    private val tvDesc2nd3 = viewGroup.findViewById<TextView>(R.id.tvDesc2nd3)
    private val tvDesc2nd4 = viewGroup.findViewById<TextView>(R.id.tvDesc2nd4)
    private val tvDesc2nd5 = viewGroup.findViewById<TextView>(R.id.tvDesc2nd5)
    private val thirdNoteTitle = viewGroup.findViewById<TextView>(R.id.thirdNoteTitle)
    private val ivFlat1st = viewGroup.findViewById<ImageView>(R.id.ivFlat1st)
    private val ivNatural1st = viewGroup.findViewById<ImageView>(R.id.ivNatural1st)
    private val ivSharp1st = viewGroup.findViewById<ImageView>(R.id.ivSharp1st)
    private val ivFlat2nd = viewGroup.findViewById<ImageView>(R.id.ivFlat2nd)
    private val ivNatural2nd = viewGroup.findViewById<ImageView>(R.id.ivNatural2nd)
    private val ivSharp2nd = viewGroup.findViewById<ImageView>(R.id.ivSharp2nd)
    private val btnBack = viewGroup.findViewById<Button>(R.id.btnBack)
    private val btnOvertone = viewGroup.findViewById<Button>(R.id.btnOvertone)
    //音符選択画面とオーバートン画面切り替え時の個々Viewの表示設定を行う。
    fun ctlDispModeChange(mode : String){
        val drawLine = DrawLine()
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
            appTitle.visibility = View.VISIBLE
            tvDesc.visibility = View.VISIBLE

            //下部ボタンを有効にする
            ivSelClef.visibility = View.VISIBLE
            ctlDispAccidentalBtnVisible(ivNote1st, accidentalSign1st)
            ctlDispAccidentalBtnVisible(ivNote2nd, accidentalSign1st)
            //音符のドラッグを無効にする。
            ivNote1st.isEnabled = true
            ivNote2nd.isEnabled = true

            //背景色を変更
            firstNoteView.setBackgroundColor(Color.parseColor(MY_PAPER_YELLOW))
            secondNoteView.setBackgroundColor(Color.parseColor(MY_PAPER_YELLOW))
            thirdNoteView.setBackgroundColor(Color.parseColor(MY_MY_DARK_BLUE_TEXT_VIEW))
            //新しい音符位置での五線譜の再描画を行う。
            drawLine.drawline(viewGroup)
        }else{
            //オーバートーン画面の画面表示を規定する。
            //Preferenceの音数指定が影響するものはctlDispWithPrefNumNotesで設定する。
            ctlDispWithPrefNumNotes(thirdNote)

            //説明文を非表示にする。
            appTitle.visibility = View.INVISIBLE
            tvDesc.visibility = View.INVISIBLE

            //使用しない下部ボタンを無効にする
            ivSelClef.visibility = View.INVISIBLE
            ivFlat1st.visibility = View.INVISIBLE
            ivNatural1st.visibility = View.INVISIBLE
            ivSharp1st.visibility = View.INVISIBLE
            ivFlat2nd.visibility = View.INVISIBLE
            ivNatural2nd.visibility = View.INVISIBLE
            ivSharp2nd.visibility = View.INVISIBLE
            //第３音五線譜
            thirdNoteView.visibility = View.VISIBLE
            //第３音音部記号
            ivClef3rd.visibility = View.VISIBLE
            //第３音見出し
            thirdNoteTitle.visibility = View.VISIBLE
            //オーバートーンボタン
            btnOvertone.visibility = View.INVISIBLE
            //戻るボタン
            btnBack.visibility = View.VISIBLE
            //音符のドラッグを無効にする。
            ivNote1st.isEnabled = false
            ivNote2nd.isEnabled = false

            //背景色を変更
            firstNoteView.setBackgroundColor(Color.parseColor(MY_PALE_BLUEGRAY))
            secondNoteView.setBackgroundColor(Color.parseColor(MY_PALE_BLUEGRAY))
            thirdNoteView.setBackgroundColor(Color.parseColor(MY_PAPER_YELLOW))
            //新しい音符位置での五線譜の再描画を行う。
            drawLine.drawline(viewGroup)
        }
    }
    //臨時記号のボタンの表示・非表示を制御する。
    //引き渡された画面上の情報から表示されている音を導出し、選択可能なボタンのみ表示するようにする。
    fun ctlDispAccidentalBtnVisible(ivNote: ImageView, accidentalSign: Int) {
        //引き渡された情報から、chromaticTone（12進)を導出する。
        val chromaticTone = util.convPhysicalstep2Chromatictone(ivNote, accidentalSign)
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
    fun ctlDispWithPrefNumNotes(thirdNote: ThirdNote) {
        //ivNote,tvDesc1st・2ndについては、個々に判断しながらVISIBLEに変更していくため
        //ここでは一旦全てINVISIBLEとする。
        //第３音
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
        //スピーカーボタン
        btnSpeaker1.visibility = View.INVISIBLE
        btnSpeaker2.visibility = View.INVISIBLE
        btnSpeaker3.visibility = View.INVISIBLE
        btnSpeaker4.visibility = View.INVISIBLE
        btnSpeaker5.visibility = View.INVISIBLE
        //Toneが80を超えるものは非表示にする。
        //またpreferenceで指定した音数のみ表示する。
        //、、、、ビューの配列化ができればもっと、シンプルで安全に組めるはずだが、、、、
        if (thirdNote.thirdNoteChromaticTone[0] != "TH") {
            ivNote3rd1.visibility = View.VISIBLE
            util.reLayout(ivNote3rd1)
            tvDesc1st1.visibility = View.VISIBLE
            tvDesc2nd1.visibility = View.VISIBLE
            btnSpeaker1.visibility = View.VISIBLE
        }
        if (thirdNote.thirdNoteChromaticTone[1] != "TH" && prefNumNotes > 1) {
            ivNote3rd2.visibility = View.VISIBLE
            util.reLayout(ivNote3rd2)
            tvDesc1st2.visibility = View.VISIBLE
            tvDesc2nd2.visibility = View.VISIBLE
            btnSpeaker2.visibility = View.VISIBLE

        }
        if (thirdNote.thirdNoteChromaticTone[2] != "TH" && prefNumNotes > 2) {
            ivNote3rd3.visibility = View.VISIBLE
            util.reLayout(ivNote3rd3)
            tvDesc1st3.visibility = View.VISIBLE
            tvDesc2nd3.visibility = View.VISIBLE
            btnSpeaker3.visibility = View.VISIBLE
        }
        if (thirdNote.thirdNoteChromaticTone[3] != "TH" && prefNumNotes > 3) {
            ivNote3rd4.visibility = View.VISIBLE
            util.reLayout(ivNote3rd4)
            tvDesc1st4.visibility = View.VISIBLE
            tvDesc2nd4.visibility = View.VISIBLE
            btnSpeaker4.visibility = View.VISIBLE
        }
        if (thirdNote.thirdNoteChromaticTone[4] != "TH" && prefNumNotes > 4) {
            ivNote3rd5.visibility = View.VISIBLE
            util.reLayout(ivNote3rd5)
            tvDesc1st5.visibility = View.VISIBLE
            tvDesc2nd5.visibility = View.VISIBLE
            btnSpeaker5.visibility = View.VISIBLE
        }
    }
}