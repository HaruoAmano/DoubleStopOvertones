package com.elsystem.doublestopovertones

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

class BtnSelClefClickListener(context: Context, viewGroup: ViewGroup) : View.OnClickListener {
    private val viewGroup = viewGroup
    private val ivClef1st = viewGroup.findViewById<ImageView>(R.id.ivClef1st)
    private val ivClef2nd = viewGroup.findViewById<ImageView>(R.id.ivClef2nd)
    private val ivNote1st = viewGroup.findViewById<ImageView>(R.id.ivNote1st)
    private val ivNote2nd = viewGroup.findViewById<ImageView>(R.id.ivNote2nd)
    private val util = Util()
    private val displayCtl = ControlDisplay(viewGroup)
    override fun onClick(v: View?) {

        //音部記号ボタンはトグルスイッチとして動作する。（ト音→ハ音→へ音→ト音…）
        noteFormat += 1
        if (noteFormat > 2) {
            noteFormat = 0
        }
        //音部記号を入れ替えるとともに、各音部記号のドの音にノートを移動。
        when (noteFormat) {
            0 -> {
                ivClef1st.setImageResource(R.drawable.gclef)
                ivClef2nd.setImageResource(R.drawable.gclef)
                util.moveNote(ivNote1st, oneLineHeight * 13 - noteHeightHalf)
                util.moveNote(ivNote2nd, oneLineHeight * 13 - noteHeightHalf)
            }
            1 -> {
                ivClef1st.setImageResource(R.drawable.cclef)
                ivClef2nd.setImageResource(R.drawable.cclef)
                util.moveNote(ivNote1st, oneLineHeight * 14 - noteHeightHalf)
                util.moveNote(ivNote2nd, oneLineHeight * 14 - noteHeightHalf)
            }
            2 -> {
                ivClef1st.setImageResource(R.drawable.fclef)
                ivClef2nd.setImageResource(R.drawable.fclef)
                util.moveNote(ivNote1st, oneLineHeight * 15 - noteHeightHalf)
                util.moveNote(ivNote2nd, oneLineHeight * 15 - noteHeightHalf)
            }
        }
        //上記で移動された音符位置に適合する臨時記号ボタンを表示する。
        ivNote1st.setImageResource(R.drawable.zenonpu)
        accidentalSign1st = 0
        displayCtl.ctlDispAccidentalBtnVisible(ivNote1st, accidentalSign1st)
        ivNote2nd.setImageResource(R.drawable.zenonpu)
        accidentalSign2nd = 0
        displayCtl.ctlDispAccidentalBtnVisible(ivNote2nd, accidentalSign2nd)
        //新しい音符位置での五線譜の再描画を行う。
        val drawLine = DrawLine()
        drawLine.drawline(viewGroup)
    }
    companion object {
        private const val tagMsg = "MyInfo_BtnClefListner: "
    }

}