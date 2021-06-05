package com.example.doublestopovertones

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import androidx.core.view.isVisible
import com.example.doublestopovertones.com.example.doublestopovertones.DisplayCtl

/*  当アプリ内部では、画面上（譜面上）の音符の位置を１２進の値に変換して演算が行われる。
 *  　　１２進に変換した値（音高）：chromaticTone　
 *  当アプリ内部ではOvertoneとは各音に対する倍音律を差し、共通の倍音は第３音（3rdNote）と呼ぶ。
 *  ただし、共通倍音は複数あり当アプリとしても第３音は複数持つ。
 */
//トップレベルでの定義。 他の.ktファイルから参照が必要なものについてここで定義。

//Preferenceの値を管理する。
var prefNumNotes = 3
var prefBaseFreq = 3
//var prefSpBtnDisp = false

//NoteViewをいくつに分割するか？ここを変更した場合はActivity_Mainで枕サイズの変更が必要！
const val divisionNumber = 25

//NoteViewの１Step分の高さを設定する項目。
var oneLineHeight = 0f

//五線譜を描画し始めるライン数
const val startOfStuffNotation = 10
const val endOfStuffNotation = 18

//NoteViewの幅を設定する項目。
var noteViewWidth = 0

//音符イメージビューの高さの半分を設定する項目。
var ivNoteHeightHalf = 0

//フォーマット(noteFormat)について
//    -2(Vn + 15) :ト音記号＋15用フォーマット　1ライン目がXX
//    -1(Vn + 8) :ト音記号＋8用フォーマット　1ライン目がXX
//    0(Vn) :ト音記号用フォーマット　1ライン目がXX
//    1(Va) :ハ音記号用フォーマット　XXライン目がXX
//    2(Vc) :へ音記号用フォーマット　XXライン目がXX
var noteFormat = 0

//accidentalSignとは臨時記号のこと。
//Flat,Natural,Sharpそれぞれのボタンから使用される項目のため、各リスナ処理の上位で定義する。
//フラットは"1",ナチュラルは"0",シャープは"-1"
var accidentalSign1st = 0
var accidentalSign2nd = 0
var chromaticTone1st = "00"
var chromaticTone2nd = "00"
const val MY_COLOR_PINK = "#E67098"
const val MY_COLOR_ORANGE = "#F49330"
const val MY_PEAL_GREEN = "#538E1F"
const val MY_MOREPEAL_GREEN = "#87BC2F"
const val MY_DARK_GRAY = "#606060"
const val MY_DARK_BLUE = "#406585"
const val MY_PAPER_YELLOW = "#EDE8D6"
const val MY_BLUE_GRAY = "#597594"
const val MY_PALE_BLUE = "#ADE3ED"

//トップレベルでの定義。 他の.ktファイルから参照が必要なものについてここで定義。
val thirdNoteFreq: MutableList<Float> = mutableListOf(0f)

class MainActivity : AppCompatActivity() {
    //プロパティ変数

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(tagMsg, "---onCreate実行---!")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Preferenceの値を取得する。
        val prefData: SharedPreferences = getSharedPreferences("PrefData", Context.MODE_PRIVATE)
        prefNumNotes = prefData.getInt("NumNotes", 3)
        prefBaseFreq = prefData.getInt("BaseFreq", 3)
//        prefSpBtnDisp = prefData.getBoolean("SpBtnDisp", false)
        //当アプリで使用するツール群
//        var util = Util()
        //画面ウイジェットの取得
        val viewGroup = findViewById<ViewGroup>(R.id.viewGroup)
        //第一音を取得する。
        val ivNote1st = findViewById<ImageView>(R.id.ivNote1st)
        //第二音を取得する。
        val ivNote2nd = findViewById<ImageView>(R.id.ivNote2nd)
        //第一音エリアのビューを取得する。
        val firstNoteView = findViewById<NoteView>(R.id.FirstNoteView)
        val ivClef1st = findViewById<ImageView>(R.id.ivClef1st)
        val ivClef2nd = findViewById<ImageView>(R.id.ivClef2nd)
        //各種ボタンビューの取得
        val ivSelClef = findViewById<ImageView>(R.id.ivSelClef)
        val btnOvertone = findViewById<Button>(R.id.btnOvertone)
        val ivFlat1st = findViewById<ImageView>(R.id.ivFlat1st)
        val ivNatural1st = findViewById<ImageView>(R.id.ivNatural1st)
        val ivSharp1st = findViewById<ImageView>(R.id.ivSharp1st)
        val ivFlat2nd = findViewById<ImageView>(R.id.ivFlat2nd)
        val ivNatural2nd = findViewById<ImageView>(R.id.ivNatural2nd)
        val ivSharp2nd = findViewById<ImageView>(R.id.ivSharp2nd)
        val secondNoteView = viewGroup.findViewById<View>(R.id.SecondNoteView)
        val thirdNoteView = viewGroup.findViewById<View>(R.id.ThirdNoteView)
        val btnBack = viewGroup.findViewById<View>(R.id.btnBack)
        val btnPreference = findViewById<ImageView>(R.id.btnPreference)
        val util = Util()
        val ctlViewDisplay = DisplayCtl(viewGroup)
        //observerを使用してView要素の配置完了を確認する。
        val observer: ViewTreeObserver = firstNoteView.viewTreeObserver
        observer.addOnGlobalLayoutListener {
//*****画面の１行の幅を算出する。*****************
            //現在のViewの高さの1/divisionNumberを高さの単位　OneHeightとする。
            oneLineHeight = (firstNoteView.height / divisionNumber).toFloat()
            //音符ImageViewの高さの半分を求める。
            ivNoteHeightHalf = (ivNote1st.height / 2)
            noteViewWidth = firstNoteView.width
//*****画面の初期表示を設定する。*****************
            //音符選択画面項目の表示（オーバートーン画面項目の非表示）を行う。
            ctlViewDisplay.modeChange(viewGroup, "select")
            //音符の初期位置を設定。
            util.moveNote(ivNote1st, (oneLineHeight * 11).toInt())
            util.moveNote(ivNote2nd, (oneLineHeight * 11).toInt())
            //音部記号の位置を設定。
            util.moveNote(ivClef1st, (oneLineHeight * 5).toInt())
            util.moveNote(ivClef2nd, (oneLineHeight * 5).toInt())
            //♭・♮・＃ボタンの初期表示。最初はドなのでシャープのみ表示
            ctlViewDisplay.ctlAccidentalBtnVisible(
                ivNote1st,
                noteFormat,
                accidentalSign1st,
                viewGroup
            )
            ctlViewDisplay.ctlAccidentalBtnVisible(
                ivNote2nd,
                noteFormat,
                accidentalSign1st,
                viewGroup
            )
        }
//*****音符のドラッグに対する処理*******************
        //音符リスナの設定。（指先追従メソッド）
        //音符を新たに動かす場合は、すでに付加されている♭・＃を外しておきたいため、
        //setOnLongClickListenerを呼んでナチュラルにしている。
        //実行順はACTION_DOWN　→　setOnLongClickListener　→　ACTION_UP
        val listener1st = IvNoteTouchListener(this, viewGroup)
        ivNote1st.setOnTouchListener(listener1st)
        val listener2nd = IvNoteTouchListener(this, viewGroup)
        ivNote2nd.setOnTouchListener(listener2nd)
//*****Clef選択ボタン**************************
        val listenerClef = BtnClefClickListener(this, viewGroup)
        ivSelClef.setOnClickListener(listenerClef)
//*****Overtoneボタン**************************
        //当ボタンの押下により、現在の画面情報から第一音、第二音の音高を調べ、
        //オーバートーンを取得、続いて第３音画面の描画を行う。
        //あわせて、スピーカーボタンとマイクボタンを有効にする。
        val listenerOvertone = BtnOvertoneClickListener(this, viewGroup)
        btnOvertone.setOnClickListener(listenerOvertone)
//*****"b"、"#"ボタン***************************
        //1st♭
        ivFlat1st.setOnClickListener {
            ivNote1st.setImageResource(R.drawable.zenonpu_flat)
            accidentalSign1st = 1
            ctlViewDisplay.ctlAccidentalBtnVisible(
                ivNote1st,
                noteFormat,
                accidentalSign1st,
                viewGroup
            )
        }
        //1st♮
        ivNatural1st.setOnClickListener {
            ivNote1st.setImageResource(R.drawable.zenonpu)
            accidentalSign1st = 0
            ctlViewDisplay.ctlAccidentalBtnVisible(
                ivNote1st,
                noteFormat,
                accidentalSign1st,
                viewGroup
            )
        }
        //1st♯
        ivSharp1st.setOnClickListener {
            ivNote1st.setImageResource(R.drawable.zenonpu_sharp)
            accidentalSign1st = -1
            ctlViewDisplay.ctlAccidentalBtnVisible(
                ivNote1st,
                noteFormat,
                accidentalSign1st,
                viewGroup
            )
        }
        //2nd♭
        ivFlat2nd.setOnClickListener {
            ivNote2nd.setImageResource(R.drawable.zenonpu_flat)
            accidentalSign2nd = 1
            ctlViewDisplay.ctlAccidentalBtnVisible(
                ivNote2nd,
                noteFormat,
                accidentalSign2nd,
                viewGroup
            )
        }
        //2nd♮
        ivNatural2nd.setOnClickListener {
            ivNote2nd.setImageResource(R.drawable.zenonpu)
            accidentalSign2nd = 0
            ctlViewDisplay.ctlAccidentalBtnVisible(
                ivNote2nd,
                noteFormat,
                accidentalSign2nd,
                viewGroup
            )
        }
        //2nd♯
        ivSharp2nd.setOnClickListener {
            ivNote2nd.setImageResource(R.drawable.zenonpu_sharp)
            accidentalSign2nd = -1
            ctlViewDisplay.ctlAccidentalBtnVisible(
                ivNote2nd,
                noteFormat,
                accidentalSign2nd,
                viewGroup
            )
        }
//*****戻るボタン*********************************
        btnBack.setOnClickListener {
            ctlViewDisplay.modeChange(viewGroup, "select")
        }
//*****Preferenceボタン**************************
        val btnPreferenceListener = BtnOnClickListener()
        btnPreference.setOnClickListener(btnPreferenceListener)
    }

    //Preferencu画面への移動
    private inner class BtnOnClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            val intent = Intent(applicationContext, PreferenceActivity::class.java)
            startActivityForResult(intent, 200)
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }
    }
    //Preference画面から戻ってきたときの処理。
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //スピーカーボタンの表示
        //（オーバートーン画面の時のみの表示項目）
//        if (thirdNoteView?.isVisible == true) {
//            if (prefSpBtnDisp) {
//                btnSpeaker1?.visibility = View.VISIBLE
//                btnSpeaker2?.visibility = View.VISIBLE
//                btnSpeaker3?.visibility = View.VISIBLE
//                btnSpeaker4?.visibility = View.VISIBLE
//                btnSpeaker5?.visibility = View.VISIBLE
//            } else {
//                btnSpeaker1?.visibility = View.INVISIBLE
//                btnSpeaker2?.visibility = View.INVISIBLE
//                btnSpeaker3?.visibility = View.INVISIBLE
//                btnSpeaker4?.visibility = View.INVISIBLE
//                btnSpeaker5?.visibility = View.INVISIBLE
//            }
//        }
    }

    companion object {
        private const val tagMsg = "MyInfo_MainActivity : "
    }
}