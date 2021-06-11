package com.elsystem.doublestopovertones

import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.view.isVisible

//画面上の各音符の位置を判定し、各音の高さをStuffNotationクラスに引き渡す。
//以下のケースで当クラスを呼び出す。
//・第１音、第２音ドラッグ時
//・オーバートーンボタン押下時
//注意点！！！
//当モジュールの経由せずonDraw処理はアプリケーションより実行される場合がある。（onCreate処理、アプリケーション切替等）
//不測の事態を避けるため、当モジュールを経過しない場合はonDraw処理内で描画を回避する。
class DrawLine() {
    //最終的にはマップ形式で引き渡す予定
    var notePhysicalStep:MutableList<Int> = mutableListOf(0)
    fun drawline(viewGroup: ViewGroup){
        val ivNote1st: ImageView = viewGroup.findViewById(R.id.ivNote1st)
        val ivNote2nd: ImageView = viewGroup.findViewById(R.id.ivNote2nd)
        val ivNote3rd1 = viewGroup.findViewById<ImageView>(R.id.ivNote3rd1)
        val ivNote3rd2 = viewGroup.findViewById<ImageView>(R.id.ivNote3rd2)
        val ivNote3rd3 = viewGroup.findViewById<ImageView>(R.id.ivNote3rd3)
        val ivNote3rd4 = viewGroup.findViewById<ImageView>(R.id.ivNote3rd4)
        val ivNote3rd5 = viewGroup.findViewById<ImageView>(R.id.ivNote3rd5)
        val btnOvertone = viewGroup.findViewById<Button>(R.id.btnOvertone)
        //表示中のもののみを対象とする。

        //音符の中心が画面上の何ライン目かを算出する。
        //もし非表示要素であった場合は99を設定する。
        notePhysicalStep.add(0, ((ivNote1st.top + noteHeightHalf) / oneLineHeight).toInt())
        notePhysicalStep.add(1, ((ivNote2nd.top + noteHeightHalf) / oneLineHeight).toInt())
        if (ivNote3rd1.isVisible) {
            notePhysicalStep.add(2, ((ivNote3rd1.top + noteHeightHalf) / oneLineHeight).toInt())
        } else {
            notePhysicalStep.add(2, 99)
        }
        if (ivNote3rd2.isVisible) {
            notePhysicalStep.add(3, ((ivNote3rd2.top + noteHeightHalf) / oneLineHeight).toInt())
        } else {
            notePhysicalStep.add(3, 99)
        }
        if (ivNote3rd3.isVisible) {
            notePhysicalStep.add(4, ((ivNote3rd3.top + noteHeightHalf) / oneLineHeight).toInt())
        } else {
            notePhysicalStep.add(4, 99)
        }
        if (ivNote3rd4.isVisible) {
            notePhysicalStep.add(5, ((ivNote3rd4.top + noteHeightHalf) / oneLineHeight).toInt())
        } else {
            notePhysicalStep.add(5, 99)
        }
        if (ivNote3rd5.isVisible) {
            notePhysicalStep.add(6, ((ivNote3rd5.top + noteHeightHalf) / oneLineHeight).toInt())
        } else {
            notePhysicalStep.add(6, 99)
        }
        //新しい音符位置での五線譜の再描画を行う。
        //第１音、第２音は常に表示するが第３音は、オーバートーン画面のみで表示するため、
        //オーバートン画面かどうかを判定し、NoteViewに引き渡す。
        //オーバートーン画面かどうかはオーバートーンボタンが見えなければオーバートーン画面と判断する。
        stuffNotation?.kickOnDraw(notePhysicalStep,!btnOvertone.isVisible)
    }
}


