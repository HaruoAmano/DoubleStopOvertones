package com.elsystem.doublestopovertones

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View

//五線譜の罫線処理を行う。
//activity_main.xmlとは別レイヤー（addContentView）とし、
//DrawLineクラスからは各音の音高のみを受け取り、それ以外の描画要素は当モジュールで管理する。
//音高の受け渡しはListでおこなう。
//音高は以下の７つ
//0:fiistNote
//1:secondNote
//2:thirdNote1
//3:thirdNote2
//4:thirdNote3
//5:thirdNote4
//6:thirdNote5
open class StuffNotation : View{

    // コンストラクタ
    // * プログラムから動的に生成する場合に使われる
    constructor(context: Context?) : super(context) {}
    // * レイアウトファイルから生成する場合に使われる
    // * android:layout_width="MATCH_PARENT"
    // * android:layout_height="MATCH_PARENT"
    // * 指定した属性値が attrs に入る
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        // * スタイルを指定した場合はdefStyleを持つ3つめのコンストラクタが利用されます。
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    }
    private var notePhysicalStep:MutableList<Int> = mutableListOf(0)
    private var underlineHeight = 0f
    private var overToneMode = true
    private var reDrawSw = false
    var linePaint = Paint()
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //五線譜を開始するステップ
        val startOfStuffNotation = 10
        //罫線の開始、終了位置
        val firstNoteLineStartX = (firstNoteViewWidth * 0.15).toFloat()
        val firstNoteLineEndX = (firstNoteViewWidth - (firstNoteViewWidth * 0.05)).toFloat()
        var secondNoteLineStartX = (secondNoteViewStartX + firstNoteViewWidth  * 0.1).toFloat()
        var secondNoteLineEndX = (secondNoteViewStartX + firstNoteViewWidth - (firstNoteViewWidth * 0.15)).toFloat()
        var thirdNoteLineStartX = (thirdNoteViewStartX + (thirdNoteViewWidth * 0.25)).toFloat()
        var thirdNoteLineEndX = (thirdNoteViewStartX + thirdNoteViewWidth - (thirdNoteViewWidth * 0.25)).toFloat()
        //下線の開始、終了位置（各罫線開始位置からの相対位置で指定）
        val lineUnderelineGap = firstNoteViewWidth * 0.45
        val lineUndereline3rdGap = thirdNoteViewWidth * 0.17
        val thirdNoteEachGap = thirdNoteViewWidth * 0.058
        val underLineWidth = firstNoteViewWidth * 0.2f
        var underLineStartX:List<Float> = listOf(
            (firstNoteLineStartX + lineUnderelineGap).toFloat(),
            (secondNoteLineStartX + lineUnderelineGap).toFloat(),
            (thirdNoteLineStartX + lineUndereline3rdGap).toFloat(),
            (thirdNoteLineStartX + lineUndereline3rdGap + thirdNoteEachGap).toFloat(),
            (thirdNoteLineStartX + lineUndereline3rdGap + thirdNoteEachGap * 2).toFloat(),
            (thirdNoteLineStartX + lineUndereline3rdGap + thirdNoteEachGap * 3).toFloat(),
            (thirdNoteLineStartX + lineUndereline3rdGap + thirdNoteEachGap * 4).toFloat())
        var underLineEndX:List<Float> = listOf(
        underLineStartX[0] + underLineWidth,
        underLineStartX[1] + underLineWidth,
        underLineStartX[2] + underLineWidth,
        underLineStartX[3] + underLineWidth,
        underLineStartX[4] + underLineWidth,
        underLineStartX[5] + underLineWidth,
        underLineStartX[6] + underLineWidth)
        //DrawLineクラスから呼び出した場合のみ描画を行う。
        if (reDrawSw) {
            Log.i("test", "onDraw実行")
//****ベースとなる五線譜の描画*************************************
            linePaint.color = resources.getColor(R.color.MY_DARK_GRAY)
            linePaint.strokeWidth = 2f
            for (i in 0..4) {
                val baseHeight = ((startOfStuffNotation + (i * 2)) * oneLineHeight).toFloat()
                canvas?.drawLine(
                    firstNoteLineStartX, baseHeight,
                    firstNoteLineEndX, baseHeight,
                    linePaint
                )
                canvas?.drawLine(
                    secondNoteLineStartX, baseHeight,
                    secondNoteLineEndX, baseHeight,
                    linePaint
                )
                if (overToneMode) {
                    canvas?.drawLine(
                        thirdNoteLineStartX, baseHeight,
                        thirdNoteLineEndX, baseHeight,
                        linePaint
                    )
                }
            }
//*****下線の描画***********************************************
            //インデックス"i"は第１音、第２音、第３音１から５の識別に使用
            //インデックス"j"はステップに使用
            linePaint.strokeWidth = 2f
            //”すでに線が引かれている”の判断に使用する。
            var numUpperLineWritten3rd = 9
            var numLowerLineWritten3rd = 9
            for (i in 0..6) {
                when (i) {
                    0,1 -> linePaint.color = Color.BLACK
                    2 -> linePaint.color = resources.getColor(R.color.MY_3rdTone1)
                    3 -> linePaint.color = resources.getColor(R.color.MY_3rdTone2)
                    4 -> linePaint.color = resources.getColor(R.color.MY_3rdTone3)
                    5 -> linePaint.color = resources.getColor(R.color.MY_3rdTone4)
                    6 -> linePaint.color = resources.getColor(R.color.MY_3rdTone5)
                }
                //
                when (notePhysicalStep[i]) {
                    //下線対象
                    in 0..8 -> {
                        //五線の上部から音符の現在ステップまで下線を引く。
                        for (j in 8 downTo notePhysicalStep[i]) {
                            //ただし、奇数ステップは除く
                            if ((j % 2) == 0) {
                                when (i) {
                                    in 0..1 -> {
                                        drawLine(canvas,i,j,underLineStartX,underLineEndX)
                                    }
                                    in 2..6 -> {
                                            drawLine(canvas, i,j,underLineStartX,underLineEndX)
                                    }
                                }
                            }
                        }
                    }
                    //下線対象（下側）
                    in 20..23 -> {
                        //五線の下部から音符の現在ステップまで下線を引く。
                        for (j in 20..notePhysicalStep[i]) {
                            //ただし、奇数ステップは除く
                            if ((j % 2) == 0) {
                                when (i) {
                                    in 0..1 -> {
                                        drawLine(canvas,i,j,underLineStartX,underLineEndX)
                                    }
                                    in 2..6 -> {
                                        drawLine(canvas, i,j,underLineStartX,underLineEndX)
                                    }
                                }
                            }
                        }
                    }
                    //INVISIBLE要素は99が設定されている。
                    in 99..99 -> {}
                }
            }

        }
    }
    //各音符の音高を当モジュールに持ち込むためのメソッド
    fun kickOnDraw(notePhysicalStep : MutableList<Int>,overToneMode : Boolean)  {
//        Log.i("test","drawLine実行")
        this.notePhysicalStep = notePhysicalStep
        this.overToneMode = overToneMode
        reDrawSw = true
        invalidate()
    }
    fun drawLine(canvas: Canvas?,idxI:Int,idxJ:Int,underLineStartX:List<Float>,underLineEndX:List<Float>) {
        underlineHeight = (idxJ * oneLineHeight).toFloat()
        canvas?.drawLine(
            underLineStartX[idxI], underlineHeight,
            underLineEndX[idxI], underlineHeight,
            linePaint)
    }
}
