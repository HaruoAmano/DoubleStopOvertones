package com.example.doublestopovertones

import android.content.Context
import android.util.Log
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
                return false
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
    fun ctlAccidentalBtn(view:View){
        var accidentalSign = 0
        if (view.id == ivNote1stId) {
            chromaticTone1st = util.convStepChrome(view, noteFormat, accidentalSign)

        }else{
        }
        Log.i(tagMsg,"view.id : $view.id")
        Log.i(tagMsg,"ivNote1stId : $ivNote1stId")
    }
    companion object{
        private const val tagMsg = "Myinfo_IvNoteListener"
    }
}