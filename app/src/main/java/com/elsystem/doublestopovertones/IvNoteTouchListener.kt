package com.elsystem.doublestopovertones

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

class IvNoteTouchListener(val context: Context, viewGroup: ViewGroup) : View.OnTouchListener {
    //プロパティ変数
    private var viewLeft: Int = 0
    private var viewTop: Int = 0
    private var screenY: Int = 0
    private var oldStep = 0
    private var viewGroup = viewGroup
    private var displayCtl = ControlDisplay(viewGroup)
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
                viewLeft = view.left
                viewTop = view.top
                screenY = y
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                //画面上の移動距離
                val diffY: Int = screenY - y
                //前回イベント発生時のビュー位置より移動距離を差し引く
                viewTop -= diffY
                //画面上のステップ数を算出
                val newStep = (viewTop + noteHeightHalf) / oneLineHeight
                //移動後のViewTopを算出
                val newViewTop = newStep * oneLineHeight - noteHeightHalf
                //音高が変わった場合に音符の描画を行う。
                if (newStep != oldStep && 4 < newStep && newStep < 24) {
                    view.layout(
                        viewLeft,
                        newViewTop,
                        viewLeft + view.width,
                        newViewTop + view.height
                    )
                    oldStep = newStep
                }
                //前回イベント発生時の高さとして確保。
                screenY = y
                //新しい音符位置での五線譜の再描画を行う。
                val drawLine = DrawLine()
                drawLine.drawline(viewGroup)
                return true
            }
            MotionEvent.ACTION_UP -> {
                //臨時記号ボタンをドラッグ後状態に適合させる。
                if (view == ivNote1st) {
                    displayCtl.ctlDispAccidentalBtnVisible(ivNote1st, accidentalSign1st)
                } else {
                    displayCtl.ctlDispAccidentalBtnVisible(ivNote2nd, accidentalSign2nd)
                }
                return true
            }
        }
        return true
    }

    companion object {
        private const val tagMsg = "Myinfo:NoteTouchList:"
    }
}