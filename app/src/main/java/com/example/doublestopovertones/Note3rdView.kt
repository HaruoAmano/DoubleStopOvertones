package com.example.doublestopovertones

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class Note3rdView : View{

    private var linePaint = Paint()
    // コンストラクタ
    constructor(context: Context?) : super(context) {
        initialize()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initialize()
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize()
    }
    override fun onDraw(canvas: Canvas?) {
//        drawGuideLine(canvas)
        drawLine(canvas)
    }

//    private fun drawGuideLine(canvas: Canvas?) {
//        linePaint.color = resources.getColor(R.color.guide)
//        var lineY = 0
//        for (i in 0..divisionNumber) {
//            canvas?.drawLine(
//                0f,
//                (oneLineHeight * lineY).toFloat(),
//                noteViewWidth.toFloat(),
//                (oneLineHeight * lineY).toFloat(),
//                linePaint
//            )
//            lineY += 2
//        }
//    }
    private fun drawLine(canvas: Canvas?) {
        linePaint.color = resources.getColor(R.color.black)
        var lineY = startOfStuffNotation       //五線譜を描画し始めるライン数
        for (i in 1..5) {
            canvas?.drawLine(
                0f, (oneLineHeight * lineY).toFloat(),
                noteViewWidth.toFloat(), (oneLineHeight * lineY).toFloat(), linePaint)
            lineY += 2
        }
    }

    // 初期化
    private fun initialize(){
        linePaint = Paint()
        linePaint.strokeWidth = 2f
        linePaint.color = resources.getColor(R.color.black)
    }

}
