package com.example.doublestopovertones

import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.doublestopovertones.com.example.doublestopovertones.DisplayCtl

class IvNoteTouchListener(context: Context, viewGroup: ViewGroup) : View.OnTouchListener {
    //プロパティ変数
    private var viewCornerX: Int = 0
    private var viewCornerY: Int = 0
    private var viewCornerUnitY: Int = 0
    private var screenY: Int = 0
    private var step = 0
    private var oldStep = 0
    private var physicalStep = 0
    private var oldUnderLineUpperStep = 0
    private var viewGroup = viewGroup
    private var displayCtl = DisplayCtl(viewGroup)
    private val ivNote1st: ImageView = viewGroup.findViewById(R.id.ivNote1st)
    private val ivNote2nd: ImageView = viewGroup.findViewById(R.id.ivNote2nd)
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        //onTouchイベント発生時の指がタッチした画面上の高さをyとして確保。
        //ACTION_MOVEで前回イベント発生時の高さ（screenY)との差分を算出し移動量としている。
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
                viewCornerX = view.left
                viewCornerY = view.top
                screenY = y
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val diffY: Int = screenY - y
                step = (viewCornerY / oneLineHeight).toInt()
                viewCornerY -= diffY
                viewCornerUnitY = step * oneLineHeight.toInt()
                //音高が変わった場合に音符の描画を行う。
                if (step != oldStep && 2 < step && step < 22) {
                    view.layout(
                        viewCornerX,
                        viewCornerUnitY,
                        viewCornerX + view.width,
                        viewCornerUnitY + view.height
                    )
                    oldStep = step
                    //下線の描画を行う。
                    //音符の現在位置（ライン数）
                    physicalStep = ((view.top + ivNoteHeightHalf) / oneLineHeight).toInt()

                    val underLineUpperStep = ((startOfStuffNotation - physicalStep) / 2)
                    Log.i("underline", "underLineUpperStep: $underLineUpperStep")
                    Log.i("underline", "oldUnderLineUpperStep: $oldUnderLineUpperStep")
                    if (underLineUpperStep >= 0) {
                        if (underLineUpperStep != oldUnderLineUpperStep) {
                            Log.i("underline", "上部下線を描きます。")
                            oldUnderLineUpperStep = underLineUpperStep
                        }
                    }
                    if (endOfStuffNotation + 1 < physicalStep) {
                        Log.i("underline", "五線譜の下にいます。")
                    }
                }
                screenY = y
                return true
            }
            MotionEvent.ACTION_UP -> {
                //臨時記号ボタンをドラッグ後状態に適合させる。
                if (view == ivNote1st) {
                    displayCtl.ctlAccidentalBtnVisible(ivNote1st, noteFormat, accidentalSign1st, viewGroup)
                } else {
                    displayCtl.ctlAccidentalBtnVisible(ivNote2nd, noteFormat, accidentalSign2nd, viewGroup)
                }
//                stepNow = step
                return true
            }
        }
        return true
    }

    companion object {
        private const val tagMsg = "Myinfo:NoteTouchList:"
    }
}