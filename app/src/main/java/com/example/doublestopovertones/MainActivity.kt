package com.example.doublestopovertones

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(tagMsg, "---onCreate実行---!")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //当アプリで使用するツール群
        var util = Util()
        //画面ウイジェットの取得
        var viewGroupe = findViewById<ViewGroup>(R.id.ViewGroupe)
        //第一音のIDを取得する。
        val ivNote1st = findViewById<ImageView>(R.id.ivNote1st)
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
            Log.i(tagMsg, "---observer実行---!")
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
//*****NOTEの移動に対する処理*******************
        //音符リスナの設定。（指先追従メソッド）
        //音符を新たに動かす場合は、すでに付加されている♭・＃を外しておきたいため、
        //setOnLongClickListnerを呼んでナチュラルにしている。
        //実行順はACTION_DOWN　→　setOnLongClickListener　→　ACTION_UP
        val listner1st = IvNoteTouchListener(ivNote1st)
        ivNote1st.setOnTouchListener(listner1st)
        val listner2nd = IvNoteTouchListener(ivNote2nd)
        ivNote2nd.setOnTouchListener(listner2nd)

//*****Clef選択ボタン**************************
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
                1 -> {
                    ivClef1st.setImageResource(R.drawable.cclef)
                    ivClef2nd.setImageResource(R.drawable.cclef)
                    util.moveNote(ivNote1st, (oneLineHeight * 12).toInt())
                    util.moveNote(ivNote2nd, (oneLineHeight * 12).toInt())
                }
                2 -> {
                    ivClef1st.setImageResource(R.drawable.fclef)
                    ivClef2nd.setImageResource(R.drawable.fclef)
                    util.moveNote(ivNote1st, (oneLineHeight * 13).toInt())
                    util.moveNote(ivNote2nd, (oneLineHeight * 13).toInt())
                }
            }
        }
//*****Overtoneボタン**************************
        //当ボタンの押下により、現在の画面情報から第一音、第二音の音高を調べ、
        //オーバートーンを取得、続いて第３音画面の描画を行う。
        //あわせて、スピーカーボタンとマイクボタンを有効にする。
        var listenerOvertone = BtnOvertoneClickListener(this, viewGroupe)
        btnOvertone.setOnClickListener(listenerOvertone)
//*****Speakerボタン***************************
        var listenerSpeaker = BtnSpeakerClickListner()
        ivSpeaker.setOnClickListener(listenerSpeaker)
//*****"b"、"#"ボタン***************************
        //1st♭
        ivFlat1st.setOnClickListener {
            ivNote1st.setImageResource(R.drawable.zenonpu_flat)
            accidentalSign1st = 1
        }
        //1st♮
        ivNatural1st.setOnClickListener {
            ivNote1st.setImageResource(R.drawable.zenonpu)
            accidentalSign1st = 0
        }
        //1st♯
        ivSharp1st.setOnClickListener {
            ivNote1st.setImageResource(R.drawable.zenonpu_sharp)
            accidentalSign1st = -1
        }
        //2nd♭
        ivFlat2nd.setOnClickListener {
            ivNote2nd.setImageResource(R.drawable.zenonpu_flat)
            accidentalSign2nd = 1
        }
        //2nd♮
        ivNatural2nd.setOnClickListener {
            ivNote2nd.setImageResource(R.drawable.zenonpu)
            accidentalSign2nd = 0
        }
        //2nd♯
        ivSharp2nd.setOnClickListener {
            ivNote2nd.setImageResource(R.drawable.zenonpu_sharp)
            accidentalSign2nd = -1
        }
    }
//*****ノートタッチリスナー***************************
    private inner class IvNoteTouchListener(view: ImageView) : View.OnTouchListener {
        //プロパティ変数
        var chromaticTone: String = "00"
        var viewCornerX: Int = 0
        var viewCornerY: Int = 0
        var viewCornerUnitY: Int = 0
        var screenY: Int = 0
        var step = 0
        var stepNow = 0
        override fun onTouch(view: View, event: MotionEvent): Boolean {
            //当アプリで使用するツール群
            var util = Util()
            val ivNote1st = findViewById<ImageView>(R.id.ivNote1st)
            val ivNote2nd = findViewById<ImageView>(R.id.ivNote2nd)
            var ivFlat1st = findViewById<ImageView>(R.id.ivFlat1st)
            var ivSharp1st = findViewById<ImageView>(R.id.ivSharp1st)
            var ivFlat2nd = findViewById<ImageView>(R.id.ivFlat2nd)
            var ivSharp2nd = findViewById<ImageView>(R.id.ivSharp2nd)

            val y = event.rawY.toInt()

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (view == ivNote1st) {
                        ivNote1st.setImageResource(R.drawable.zenonpu)
                    } else {
                        ivNote2nd.setImageResource(R.drawable.zenonpu)
                    }
                    //音符の現在位置（左上端）を変数に確保。
                    viewCornerX = view.getLeft()
                    viewCornerY = view.getTop()
                    screenY = y
                    return true
                }
                MotionEvent.ACTION_MOVE -> {
                    val diffY: Int = screenY - y
                    step = (viewCornerY / oneLineHeight).toInt()
                    viewCornerY = viewCornerY - diffY
                    viewCornerUnitY = step * oneLineHeight.toInt()

                    view.layout(
                        viewCornerX,
                        viewCornerUnitY,
                        viewCornerX + view.getWidth(),
                        viewCornerUnitY + view.getHeight()
                    )
                    screenY = y
                    return true
                }
                MotionEvent.ACTION_UP -> {
                    //ドラッグされた音高がシとミの場合は#ボタンを、ファとドの場合は♭ボタンを隠す。
                    var accidentalSign = 0
                    chromaticTone = util.convPhisicalstep2Chromatictone(view, noteFormat, accidentalSign)
                    //12進を1桁目と2桁目に分解。
                    var charArray: CharArray = chromaticTone.toCharArray()
                    var _1Place: String = charArray[1].toString()
                    Log.i(tagMsg, "_1Place: ${_1Place}")

                    when (_1Place) {
                        "0", "5" -> {
                            if (view == ivNote1st) {
                                ivFlat1st.visibility = View.INVISIBLE
                                ivSharp1st.visibility = View.VISIBLE
                            } else {
                                ivFlat2nd.visibility = View.INVISIBLE
                                ivSharp2nd.visibility = View.VISIBLE
                            }
                        }
                        "4", "B" -> {
                            if (view == ivNote1st) {
                                ivSharp1st.visibility = View.INVISIBLE
                                ivFlat1st.visibility = View.VISIBLE
                            } else {
                                ivSharp2nd.visibility = View.INVISIBLE
                                ivFlat2nd.visibility = View.VISIBLE
                            }
                        }
                        else ->  {
                            if (view == ivNote1st) {
                                ivSharp1st.visibility = View.VISIBLE
                                ivFlat1st.visibility = View.VISIBLE
                            } else {
                                ivSharp2nd.visibility = View.VISIBLE
                                ivFlat2nd.visibility = View.VISIBLE
                            }
                        }

                    }
                    stepNow = step
                    //音程に応じ、♭・シャープボタンの有効無効を制御する。
                    //例えばシの時は#は表示しないとか、、、、、
                    //でもどうやってやればいい？
                    return true
                }
            }
            return true
        }
    }

    companion object {
        private const val tagMsg = "MyInfo_MainActivity : "
    }
}




