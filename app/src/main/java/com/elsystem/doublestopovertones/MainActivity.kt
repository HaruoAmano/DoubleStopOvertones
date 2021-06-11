package com.elsystem.doublestopovertones

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.AudioTrack
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import androidx.core.view.isVisible

/*  当アプリ内部では、画面上（譜面上）の音符の位置を１２進の値に変換して演算が行われる。
 *  　　１２進に変換した値（音高）：chromaticTone　
 *  当アプリ内部ではOvertoneとは各音に対する倍音律を差し、共通の倍音は第３音（3rdNote）と呼ぶ。
 *  ただし、共通倍音は複数あり当アプリとしても第３音は複数持つ。
 */
//トップレベルでの定義。 他の.ktファイルから参照が必要なものについてここで定義。

//Preferenceの値を管理する。
var prefNumNotes = 1
var prefBaseFreq = 3
//NoteViewをいくつに分割するか？ここを変更した場合はActivity_Mainで枕サイズの変更が必要！
const val divisionNumber = 25
//画面の各ビューの相対位置を取得するための項目
var viewGroupWidth = 0
var firstNoteViewStartX = 0
var firstNoteViewWidth = 0
var secondNoteViewStartX = 0
var thirdNoteViewStartX = 0
var thirdNoteViewWidth = 0
//NoteViewの１Step分の高さを設定する項目。
var oneLineHeight = 0
//NoteViewの幅を設定する項目。
var noteViewWidth = 0
//音符イメージビューの高さの半分を設定する項目。
var noteHeightHalf = 0
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
//各ウイジェットの色はレイアウトで決めるが、画面切り替えによりプログラムでの変更が必要なものについて
//ここでレイアウトと同じ色を再定義している。
const val MY_PAPER_YELLOW = "#EDE8D6"
const val MY_BLUE_GRAY = "#597594"
const val MY_PALE_BLUEGRAY = "#C2D0E3"
const val MY_MY_DARK_BLUE_TEXT_VIEW = "#597594"
//オーバートーンの周波数はbtnSpeakerClickListenerで使用される。
val thirdNoteFreq: MutableList<Float> = mutableListOf(0f)
var stuffNotation :StuffNotation? = null
//ThirdNoteはPreferenceActivityから戻ってきたタイミングで使用することがあるため、ここでインスタンス化する。
val thirdNote = ThirdNote()
var audioTrack : AudioTrack? = null
class MainActivity : AppCompatActivity() {
    //プロパティ変数
    val drawLine = DrawLine()
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(tagMsg, "---onCreate実行---!")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //五線譜描画用にContentViewを追加する。
        stuffNotation = StuffNotation(this)
        addContentView(stuffNotation,
            ViewGroup.LayoutParams
                (ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.FILL_PARENT))
        Log.i(tagMsg, "---ContentViewの設定---!")
        //Preferenceの値を取得する。
        val prefData: SharedPreferences = getSharedPreferences("PrefData", Context.MODE_PRIVATE)
        prefNumNotes = prefData.getInt("NumNotes", 3)
        prefBaseFreq = prefData.getInt("BaseFreq", 3)
        //当アプリで使用するツール群
//        var util = Util()
        //画面ウイジェットの取得
        val viewGroup = findViewById<ViewGroup>(R.id.viewGroup)
        //第一音を取得する。
        val ivNote1st = findViewById<ImageView>(R.id.ivNote1st)
        //第二音を取得する。
        val ivNote2nd = findViewById<ImageView>(R.id.ivNote2nd)
        //第一音エリアのビューを取得する。
        val firstNoteView = findViewById<View>(R.id.FirstNoteView)
        val secondNoteView = findViewById<View>(R.id.SecondNoteView)
        val thirdNoteView = findViewById<View>(R.id.ThirdNoteView)
        val ivClef1st = findViewById<ImageView>(R.id.ivClef1st)
        val ivClef2nd = findViewById<ImageView>(R.id.ivClef2nd)
        //各種ボタンビューの取得
        val ivSelClef = findViewById<ImageView>(R.id.ivSelClef)
//        val ivSelClef = findViewById<Button>(R.id.ivSelClef)
        val btnOvertone = findViewById<Button>(R.id.btnOvertone)
        val ivFlat1st = findViewById<ImageView>(R.id.ivFlat1st)
        val ivNatural1st = findViewById<ImageView>(R.id.ivNatural1st)
        val ivSharp1st = findViewById<ImageView>(R.id.ivSharp1st)
        val ivFlat2nd = findViewById<ImageView>(R.id.ivFlat2nd)
        val ivNatural2nd = findViewById<ImageView>(R.id.ivNatural2nd)
        val ivSharp2nd = findViewById<ImageView>(R.id.ivSharp2nd)
        val btnBack = viewGroup.findViewById<View>(R.id.btnBack)
        val ivPreference = findViewById<ImageView>(R.id.ivPreference)
        val util = Util()
        val displayCtl = ControlDisplay(viewGroup)
        //observerを使用してView要素の配置完了を確認する。
        val observer: ViewTreeObserver = viewGroup.viewTreeObserver
            observer.addOnGlobalLayoutListener {
            Log.i(tagMsg, "---ViewTreeObserver---!")
//*****画面の１行の幅を算出する。*****************
            //現在のアプリで使用する描画領域全体のサイズを取得する。
            viewGroupWidth = viewGroup.width
            firstNoteViewStartX = 0
            firstNoteViewWidth = firstNoteView.width
            secondNoteViewStartX = secondNoteView.left
            thirdNoteViewStartX = thirdNoteView.left
            thirdNoteViewWidth = thirdNoteView.width
            //現在のViewの高さの1/divisionNumberを高さの単位　OneHeightとする。
            oneLineHeight = firstNoteView.height / divisionNumber
            //音符ImageViewの高さの半分を求める。
            noteHeightHalf = (ivNote1st.height / 2)
            noteViewWidth = firstNoteView.width
            //*****画面の初期表示を設定する。*****************
            //音符の初期位置を設定。
            util.moveNote(ivNote1st, (oneLineHeight * 13) - noteHeightHalf)
            util.moveNote(ivNote2nd, (oneLineHeight * 13) - noteHeightHalf)
            //♭・♮・＃ボタンの初期表示。最初はドなのでシャープのみ表示
            displayCtl.ctlDispAccidentalBtnVisible(
                ivNote1st, accidentalSign1st)
            displayCtl.ctlDispAccidentalBtnVisible(
                ivNote2nd, accidentalSign1st)
            Log.i(tagMsg,"MainActivityから描画")
            drawLine.drawline(viewGroup)
        }
//*****音符のドラッグに対する処理*******************
        val listener1st = IvNoteTouchListener(this, viewGroup)
        ivNote1st.setOnTouchListener(listener1st)
        val listener2nd = IvNoteTouchListener(this, viewGroup)
        ivNote2nd.setOnTouchListener(listener2nd)
//*****Clef選択ボタン**************************
        val listenerClef = BtnSelClefClickListener(this, viewGroup)
        ivSelClef.setOnClickListener(listenerClef)
//*****Overtoneボタン**************************
        //当ボタンの押下により、現在の画面情報から第一音、第二音の音高を調べ、
        //オーバートーンを取得、続いて第３音画面の描画を行う。
        //あわせて、スピーカーボタンとマイクボタンを有効にする。
        val listenerOvertone = BtnOvertoneClickListener(this ,viewGroup)
        btnOvertone.setOnClickListener(listenerOvertone)
//*****"b"、"#"ボタン***************************
        //1st♭
        ivFlat1st.setOnClickListener {
            ivNote1st.setImageResource(R.drawable.zenonpu_flat)
            accidentalSign1st = 1
            displayCtl.ctlDispAccidentalBtnVisible(ivNote1st, accidentalSign1st)
        }
        //1st♮
        ivNatural1st.setOnClickListener {
            ivNote1st.setImageResource(R.drawable.zenonpu)
            accidentalSign1st = 0
            displayCtl.ctlDispAccidentalBtnVisible(ivNote1st,accidentalSign1st)
        }
        //1st♯
        ivSharp1st.setOnClickListener {
            ivNote1st.setImageResource(R.drawable.zenonpu_sharp)
            accidentalSign1st = -1
            displayCtl.ctlDispAccidentalBtnVisible(ivNote1st, accidentalSign1st)
        }
        //2nd♭
        ivFlat2nd.setOnClickListener {
            ivNote2nd.setImageResource(R.drawable.zenonpu_flat)
            accidentalSign2nd = 1
            displayCtl.ctlDispAccidentalBtnVisible(ivNote2nd,  accidentalSign2nd)
        }
        //2nd♮
        ivNatural2nd.setOnClickListener {
            ivNote2nd.setImageResource(R.drawable.zenonpu)
            accidentalSign2nd = 0
            displayCtl.ctlDispAccidentalBtnVisible(ivNote2nd,  accidentalSign2nd)
        }
        //2nd♯
        ivSharp2nd.setOnClickListener {
            ivNote2nd.setImageResource(R.drawable.zenonpu_sharp)
            accidentalSign2nd = -1
            displayCtl.ctlDispAccidentalBtnVisible(ivNote2nd,  accidentalSign2nd)
        }
//*****戻るボタン*********************************
        btnBack.setOnClickListener {
            displayCtl.ctlDispModeChange("select")
        }
//*****Preferenceボタン**************************
        val ivPreferenceListener = BtnPrefOnClickListener()
        ivPreference.setOnClickListener(ivPreferenceListener)
    }

    override fun onPause() {
        super.onPause()
        if (audioTrack != null) {
            audioTrack!!.stop()
            audioTrack!!.release() // release buffer
            audioTrack = null
        }
    }

    override fun onResume() {
        super.onResume()
        val viewGroup = findViewById<ViewGroup>(R.id.viewGroup)
        val btnOverTone = findViewById<Button>(R.id.btnOvertone)
        if (!btnOverTone.isVisible) {
            val controlDisplay = ControlDisplay(viewGroup)
            controlDisplay.ctlDispWithPrefNumNotes(thirdNote)
        }
    }

    private inner class BtnPrefOnClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            val intent = Intent(applicationContext, PreferenceActivity::class.java)
            startActivityForResult(intent, 200)
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }
    }
    //Preference画面から戻ってきたときの処理。
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val viewGroup = findViewById<ViewGroup>(R.id.viewGroup)
        val btnOverTone = findViewById<Button>(R.id.btnOvertone)
        //オーバートーン画面からpreferenceでNumTonesに変更があった場合、
        //設定内容を即座に反映する。
        if (!btnOverTone.isVisible) {
            val controlDisplay = ControlDisplay(viewGroup)
            controlDisplay.ctlDispWithPrefNumNotes(thirdNote)
            drawLine.drawline(viewGroup)
        }
    }
    companion object {
        private const val tagMsg = "MyInfo_MainActivity : "
    }
}