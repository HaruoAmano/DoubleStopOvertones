package com.example.doublestopovertones

import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

class IvNoteTouchListener (context: Context, viewGroup: ViewGroup) : View.OnTouchListener {
    //プロパティ変数
    var viewCornerX: Int = 0
    var viewCornerY: Int = 0
    var viewCornerUnitY: Int = 0
    var screenY: Int = 0
    var step = 0
    var stepNow = 0
    var util = Util()
    var vG = viewGroup
    val ivNote1st = viewGroup.findViewById<ImageView>(R.id.ivNote1st)
    val ivNote2nd = viewGroup.findViewById<ImageView>(R.id.ivNote2nd)
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        //当アプリで使用するツール群
        val y = event.rawY.toInt()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                //音符を移動させる際はまず、ナチュラルに戻す。
                if (view == ivNote1st) {
                    ivNote1st.setImageResource(R.drawable.zenonpu)
                    accidentalSign1st = 0
                } else {
                    ivNote2nd.setImageResource(R.drawable.zenonpu)
                    accidentalSign2nd = 0
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
                //臨時記号ボタンをドラッグ後状態に適合させる。
                if (view == ivNote1st) {
                    util.ctlAccidentalBtnVisible(ivNote1st, noteFormat, accidentalSign1st, vG)
                } else {
                    util.ctlAccidentalBtnVisible(ivNote2nd, noteFormat, accidentalSign2nd, vG)
                }
                stepNow = step
                return true
            }
        }
        return true
    }
    companion object {
        private const val tagMsg = "Myinfo:NoteTouchList:"
    }
}