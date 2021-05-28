package com.example.doublestopovertones

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.ImageView
import com.example.doublestopoverTone.ScaleTable


/**ユーザーインターフェースに関わることは基本、ActivityMainに記述することとする。
 *
 * 音の高さはActivityMainではstepと呼ぶこととする。
 * ただし、stepには画面上の位置を表すものと、実際の音の高さに(ScaleTableDataで）紐づくものがあり、
 * 以下のように区別する。
 *     画面上の位置を表すもの:phisicalStep
 * 　　実際の高さに紐づくもの:scaleStep
 *  当アプリ内部では、画面上（譜面上）の音符の位置を１２進の値に変換して演算が行われる。
 *  　　１２進に変換した値（音高）：chromaticTone　
 *  当アプリ内部ではOvertoneとは各音に対する倍音律を差し、共通の倍音は第３音（3rdNote）と呼ぶ。
 *  ただし、共通倍音は複数あり当アプリとしても第３音は複数持つ。
*/
//トップレベルでの定義。 他の.ktファイルから参照が必要なものについてここで定義。

//NoteViewをいくつに分割するか？ここを変更した場合はActivity_Mainで枕サイズの変更が必要！
val divisionNumber = 25
//NoteViewの１Step分の高さを設定する項目。
var oneLineHeight = 0f
//五線譜を描画し始めるライン数
val biginingOfStuffNotation = 10
//NoteViewの幅を設定する項目。
var noteViewWidth = 0
//音符イメージビューの高さの半分を設定する項目。
var ivNoteHeightHalf = 0

val scaleTable = ScaleTable()
//フォーマット(noteFormat)について
//    -2(Vn + 15) :ト音記号＋15用フォーマット　1ライン目がXX
//    -1(Vn + 8) :ト音記号＋8用フォーマット　1ライン目がXX
//    0(Vn) :ト音記号用フォーマット　1ライン目がXX
//    1(Va) :ハ音記号用フォーマット　XXライン目がXX
//    2(Vc) :へ音記号用フォーマット　XXライン目がXX
var noteFormat = 0
var noteFormat3rd = 0
//accidentalSignとは臨時記号のこと。
//Flat,Natural,Sharpそれぞれのボタンから使用される項目のため、各リスナ処理の上位で定義する。
//フラットは"1",ナチュラルは"0",シャープは"-1"
var accidentalSign1st = 0
var accidentalSign2nd = 0

var chromaticTone1st = "00"
var chromaticTone2nd = "00"

val COLOR_PINK = "#E67098"
val COLOR_ORANGE = "#F8C189"

class MainActivity : AppCompatActivity() {

    //プロパティ変数

    @SuppressLint("ClickableViewAccessibility") //ivNote1st.setOnTouchListener(listner)への対処。
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(tagMsg,"---onCreate実行---!")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var viewGroupe = findViewById<ViewGroup>(R.id.ViewGroupe)
        //当アプリで使用するツール群
        var util = Util()
        //第一音のIDを取得する。
        val ivNote1st= findViewById<ImageView>(R.id.ivNote1st)
        //第二音のIDを取得する。
        val ivNote2nd = findViewById<ImageView>(R.id.ivNote2nd)
        //第一音エリアのビューを取得する。
        val firstNoteView = findViewById<NoteView>(R.id.FirstNoteView)

        val ivClef1st = findViewById<ImageView>(R.id.ivClef1st)
        val ivClef2nd = findViewById<ImageView>(R.id.ivClef2nd)

        //第二音エリアのビューを取得する。
        val secondNoteView = findViewById<NoteView>(R.id.secondNoteView)


        //各種ボタンビューの取得
        val ivSelClef = findViewById<ImageView>(R.id.ivSelClef)
        val btnOvertone = findViewById<Button>(R.id.btnOvertone)
        val ivSpeaker = findViewById<ImageView>(R.id.ivSpeaker)
        ivSpeaker.isEnabled = false
        val ivMic = findViewById<ImageView>(R.id.ivMic)
        ivMic.isEnabled = false
        val ivFlat1st = findViewById<ImageView>(R.id.ivFlat1st)
        val ivNatural1st = findViewById<ImageView>(R.id.ivNatural1st)
        val ivSharp1st = findViewById<ImageView>(R.id.ivSharp1st)
        val ivFlat2nd = findViewById<ImageView>(R.id.ivFlat2nd)
        val ivNatural2nd = findViewById<ImageView>(R.id.ivNatural2nd)
        val ivSharp2nd = findViewById<ImageView>(R.id.ivSharp2nd)

        //observerを使用してView要素の配置完了を確認する。
        val observer: ViewTreeObserver = firstNoteView.viewTreeObserver
        observer.addOnGlobalLayoutListener {
            Log.i(tagMsg,"---observer実行---!")
            //現在のViewの高さの1/divisionNumberを高さの単位　OneHeightとする。
            oneLineHeight = (firstNoteView.height / divisionNumber).toFloat()
            //音符ImageViewの高さの半分を求める。
            ivNoteHeightHalf = (ivNote1st.height / 2)
            noteViewWidth = firstNoteView.width

            //音符の初期位置を設定。
            util.moveNote(ivNote1st, (oneLineHeight * 11).toInt())
            util.moveNote(ivNote2nd, (oneLineHeight * 11).toInt())
            //音部記号の位置を設定。
            util.moveNote(ivClef1st, (oneLineHeight * 5).toInt())
            util.moveNote(ivClef2nd, (oneLineHeight * 5).toInt())
        }
        //NOTEに対する処理
        //音符リスナの設定。（指先追従メソッド）
        val listner1st = IvNoteTouchListener(this,viewGroupe)
        ivNote1st.setOnTouchListener(listner1st)
        val listner2nd = IvNoteTouchListener(this,viewGroupe)
        ivNote2nd.setOnTouchListener(listner2nd)
        //Clef選択ボタン
        ivSelClef.setOnClickListener {
            noteFormat += 1
            if (noteFormat > 2) {
                noteFormat = 0
            }
            //音部記号を入れ替えるとともに、各音部記号のドの音にノートを移動。
            when (noteFormat) {
                0 -> {
                      ivClef1st.setImageResource(R.drawable.gclef)
                      ivClef2nd.setImageResource(R.drawable.gclef)
                      util.moveNote(ivNote1st, (oneLineHeight * 11).toInt())
                      util.moveNote(ivNote2nd, (oneLineHeight * 11).toInt())
                }
                1 -> {ivClef1st.setImageResource(R.drawable.cclef)
                      ivClef2nd.setImageResource(R.drawable.cclef)
                      util.moveNote(ivNote1st, (oneLineHeight * 12).toInt())
                      util.moveNote(ivNote2nd, (oneLineHeight * 12).toInt())
                }
                2 -> {ivClef1st.setImageResource(R.drawable.fclef)
                      ivClef2nd.setImageResource(R.drawable.fclef)
                      util.moveNote(ivNote1st, (oneLineHeight * 13).toInt())
                      util.moveNote(ivNote2nd, (oneLineHeight * 13).toInt())
                }
            }
        }
        //Overtoneボタン。
        //当ボタンの押下により、現在の画面情報から第一音、第二音の音高を調べ、
        //オーバートーンを取得、続いて第３音画面の描画を行う。
        //あわせて、スピーカーボタンとマイクボタンを有効にする。
        var listenerOvertone = BtnOvertoneClickListener(this,viewGroupe)
        btnOvertone.setOnClickListener(listenerOvertone)
        //Speakerボタン。
        var listenerSpeaker = BtnSpeakerClickListner()
        ivSpeaker.setOnClickListener(listenerSpeaker)
        //Micボタン。
        ivMic.setOnClickListener {
        //スペアナへ移動。
        val intent = Intent(applicationContext, SpeAna::class.java)
        startActivity(intent)
        }
        //全音符に"b"または"#"をつける。
        //1st♭
        ivFlat1st.setOnClickListener {
            ivNote1st.setImageResource(R.drawable.zenonpu_flat)
//            util.reLayout(ivNote1st, R.drawable.zenonpu_flat)
            accidentalSign1st = 1
        }
        //1st♮
        ivNatural1st.setOnClickListener {
            ivNote1st.setImageResource(R.drawable.zenonpu)
//            util.reLayout(ivNote1st, R.drawable.zenonpu)
            accidentalSign1st = 0
        }
        //1st♯
        ivSharp1st.setOnClickListener {
            ivNote1st.setImageResource(R.drawable.zenonpu_sharp)
//            util.reLayout(ivNote1st, R.drawable.zenonpu_sharp)
            accidentalSign1st = -1
        }
        //2nd♭
        ivFlat2nd.setOnClickListener {
            ivNote2nd.setImageResource(R.drawable.zenonpu_flat)
//            util.reLayout(ivNote2nd, R.drawable.zenonpu_flat)
            accidentalSign2nd = 1

        }
        //2nd♮
        ivNatural2nd.setOnClickListener {
            ivNote2nd.setImageResource(R.drawable.zenonpu)
//            util.reLayout(ivNote2nd, R.drawable.zenonpu)
            accidentalSign2nd = 0
        }
        //2nd♯
        ivSharp2nd.setOnClickListener {
            ivNote2nd.setImageResource(R.drawable.zenonpu_sharp)
//            util.reLayout(ivNote2nd, R.drawable.zenonpu_sharp)
            accidentalSign2nd = -1
        }
    }
    companion object {
        private const val tagMsg = "MyInfo_MainActivity : "
    }
}




