package com.example.doublestopovertones

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

class BtnClefClickListener(context: Context, viewGroup: ViewGroup) : View.OnClickListener {
    val vG = viewGroup
    val ivClef1st = viewGroup.findViewById<ImageView>(R.id.ivClef1st)
    val ivClef2nd = viewGroup.findViewById<ImageView>(R.id.ivClef2nd)
    val ivNote1st = viewGroup.findViewById<ImageView>(R.id.ivNote1st)
    val ivNote2nd = viewGroup.findViewById<ImageView>(R.id.ivNote2nd)
    val util = Util()
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
                util.moveNote(ivNote1st, (oneLineHeight * 11).toInt())
                util.moveNote(ivNote2nd, (oneLineHeight * 11).toInt())
            }
            1 -> {
                ivClef1st.setImageResource(R.drawable.cclef)
                ivClef2nd.setImageResource(R.drawable.cclef)
                util.moveNote(ivNote1st, (oneLineHeight * 12).toInt())
                util.moveNote(ivNote2nd, (oneLineHeight * 12).toInt())
            }
            2 -> {
                ivClef1st.setImageResource(R.drawable.fclef)
                ivClef2nd.setImageResource(R.drawable.fclef)
                util.moveNote(ivNote1st, (oneLineHeight * 13).toInt())
                util.moveNote(ivNote2nd, (oneLineHeight * 13).toInt())
            }
        }
        //上記で移動された音符位置に適合する臨時記号ボタンを表示する。
        ivNote1st.setImageResource(R.drawable.zenonpu)
        accidentalSign1st = 0
        util.ctlAccidentalBtnVisible(ivNote1st, noteFormat, accidentalSign1st, vG)
        ivNote2nd.setImageResource(R.drawable.zenonpu)
        accidentalSign2nd = 0
        util.ctlAccidentalBtnVisible(ivNote2nd, noteFormat, accidentalSign2nd, vG)
    }

}