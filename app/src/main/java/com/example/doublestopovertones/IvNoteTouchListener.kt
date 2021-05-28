package com.example.doublestopovertones

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView


class IvNoteTouchListener(context: Context, viewGroupe: ViewGroup) : View.OnTouchListener{

    //プロパティ変数
    var chromaticTone :String = "00"
    var viewCornerX: Int = 0
    var viewCornerY: Int = 0
    var viewCornerUnitY: Int = 0
    var screenY: Int = 0
    var step=0
    var stepNow =0

    override fun onTouch(view: View, event: MotionEvent): Boolean {

        //当アプリで使用するツール群
        var util = Util()

        val y = event.rawY.toInt()

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                //音符の現在位置（左上端）を変数に確保。
                viewCornerX = view.getLeft()
                viewCornerY = view.getTop()
                screenY = y
            }
            MotionEvent.ACTION_MOVE -> {
                //音符を動かすときには♭、#を外し、ナチュラルに戻す。
                //でもどうやってやればいい？？
                //OnTouchListenerはViewで定義されたインターフェースだが、
                //setImageResourceはImageViewのメソッドなのでここでは使えない！！

                val diffY: Int = screenY - y
                step =(viewCornerY/oneLineHeight).toInt()
                viewCornerY = viewCornerY - diffY
                viewCornerUnitY = step* oneLineHeight.toInt()

                view.layout(
                        viewCornerX,
                        viewCornerUnitY ,
                        viewCornerX + view.getWidth(),
                        viewCornerUnitY + view.getHeight()
                )
                screenY = y
            }
            MotionEvent.ACTION_UP -> {
                stepNow = step
                //音程に応じ、♭・シャープボタンの有効無効を制御する。
                //例えばシの時は#は表示しないとか、、、、、
                //でもどうやってやればいい？

            }
        }
        return true
    }
}