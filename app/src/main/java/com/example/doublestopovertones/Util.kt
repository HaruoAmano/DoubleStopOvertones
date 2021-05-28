package com.example.doublestopovertones

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class Util {
//    音符が初期状態の位置に戻ってしまうことを防ぐため。
//    音符の画像を切り替える（♭♮#が押された）際はこの関数を呼び出すこと。
    fun reLayout(ivNote: ImageView) {
        ivNote.layout(
                ivNote.getLeft(),
                ivNote.getTop(),
                ivNote.getLeft() + ivNote.getWidth(),
                ivNote.getTop() + ivNote.getHeight())
    }
    //画面上の指定Step位置に音符を移動する・
    fun moveNote(ivNote: ImageView,step:Int) {
        ivNote.layout(
                ivNote.getLeft(),
                step,
                ivNote.getLeft() + ivNote.getWidth(),
                step + ivNote.getHeight())
    }
    //画面上の物理的な位置から実際の音高（１２進）を導出する。
    fun convStepChrome(ivNote: View, fmt: Int, accidentalSign: Int) :String {
        //phisicalStepを求めた上でフォーマットおよび臨時号を加味し、ScaleTable上のChromaticStepに変換する。
        //NoteViewのphisicalStepを求める。
        var imageTop = ivNote.getTop()
        var phisicalStep = (imageTop + ivNoteHeightHalf) / oneLineHeight.toInt()
        //以下で（♭、＃を考慮しない）ノート上の位置を取得する。
        var naturalStep = 0
        when (fmt) {
            0 -> naturalStep = phisicalStep + 15
            1 -> naturalStep = phisicalStep + 21
            2 -> naturalStep = phisicalStep + 27
        }
        //以下でフラットの場合は0.5、シャープの場合は-0.5を加算する。
        //（Stepは小さいほど音が高くなる点に注意！）
        //ただし、シとミにシャープがついている場合は １減算し、ドとファにフラットがついている場合は
        //１加算する。（一旦0.5加減算後に上書き。）
        //入力時点でこれらは排除したいが、やり方がわからないので今はやむなし。
        var adjustmentNum = 0f
        var scaleStep = 0f
        adjustmentNum=accidentalSign.toFloat() / 2f
        when (naturalStep % 7) {
            //ド
            0 -> if (accidentalSign == 1) {
                adjustmentNum = 1.0f
            }
            //シ
            1 -> if (accidentalSign == -1) {
                adjustmentNum = -1.0f
            }
            //ファ
            4 -> if (accidentalSign == 1) {
                adjustmentNum = 1.0f
            }
            //ミ
            5 -> if (accidentalSign == -1) {
                adjustmentNum = -1.0f
            }
        }
        scaleStep = naturalStep + adjustmentNum

        //もし下記でテーブルにHITしなかった場合、結果が明確に出るよう80を設定する。
        var chromaticTone = "80"
        //ScaleTableより該当するchromaticTone（１２進）を取得する。
        for (i in 0..95) {
            if (scaleTable.scaleTable.chromaticStep[i] == scaleStep) {
                chromaticTone = scaleTable.scaleTable.chromaticTone[i]
                break
            }
        }
        return chromaticTone
    }
    companion object {
        private const val tagMsg = "MyInfo_Util : "
    }
}