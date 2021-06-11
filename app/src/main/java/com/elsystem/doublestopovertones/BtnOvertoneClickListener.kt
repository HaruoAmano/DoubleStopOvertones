package com.elsystem.doublestopovertones

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class BtnOvertoneClickListener(context: Context,viewGroup: ViewGroup) : View.OnClickListener {
    //オーバートーンボタン押下時の処理。
    //画面より第１音・第２音の音符情報を取得し12進に変換後ThirdNoteクラスで各倍音列を生成後
    //それらに共通する第３音情報を取得する。
    //その後、第３音情報を第３音ビューエリアに表示するまでを行う。
    private var util = Util()
    private var controlDisplay = ControlDisplay(viewGroup)
    private val context = context
    private val viewGroup = viewGroup.findViewById<ViewGroup>(R.id.viewGroup)
    private val ivNote1st = viewGroup.findViewById<ImageView>(R.id.ivNote1st)
    private val ivNote2nd = viewGroup.findViewById<ImageView>(R.id.ivNote2nd)
    private val ivClef3rd =viewGroup.findViewById<ImageView>(R.id.ivClef3rd)
    private val ivNote3rd1 = viewGroup.findViewById<ImageView>(R.id.ivNote3rd1)
    private val ivNote3rd2 = viewGroup.findViewById<ImageView>(R.id.ivNote3rd2)
    private val ivNote3rd3 = viewGroup.findViewById<ImageView>(R.id.ivNote3rd3)
    private val ivNote3rd4 = viewGroup.findViewById<ImageView>(R.id.ivNote3rd4)
    private val ivNote3rd5 = viewGroup.findViewById<ImageView>(R.id.ivNote3rd5)
    private val tvDesc1st1 = viewGroup.findViewById<TextView>(R.id.tvDesc1st1)
    private val tvDesc1st2 = viewGroup.findViewById<TextView>(R.id.tvDesc1st2)
    private val tvDesc1st3 = viewGroup.findViewById<TextView>(R.id.tvDesc1st3)
    private val tvDesc1st4 = viewGroup.findViewById<TextView>(R.id.tvDesc1st4)
    private val tvDesc1st5 = viewGroup.findViewById<TextView>(R.id.tvDesc1st5)
    private val tvDesc2nd1 = viewGroup.findViewById<TextView>(R.id.tvDesc2nd1)
    private val tvDesc2nd2 = viewGroup.findViewById<TextView>(R.id.tvDesc2nd2)
    private val tvDesc2nd3 = viewGroup.findViewById<TextView>(R.id.tvDesc2nd3)
    private val tvDesc2nd4 = viewGroup.findViewById<TextView>(R.id.tvDesc2nd4)
    private val tvDesc2nd5 = viewGroup.findViewById<TextView>(R.id.tvDesc2nd5)
    private val btnSpeaker1 = viewGroup.findViewById<Button>(R.id.btnSpeaker1)
    private val btnSpeaker2 = viewGroup.findViewById<Button>(R.id.btnSpeaker2)
    private val btnSpeaker3 = viewGroup.findViewById<Button>(R.id.btnSpeaker3)
    private val btnSpeaker4 = viewGroup.findViewById<Button>(R.id.btnSpeaker4)
    private val btnSpeaker5 = viewGroup.findViewById<Button>(R.id.btnSpeaker5)

    override fun onClick(view: View) {
        //第１音の音高を取得する。
        chromaticTone1st = util.convPhysicalstep2Chromatictone(ivNote1st,accidentalSign1st)
        //第２音の音高を取得する。
        chromaticTone2nd = util.convPhysicalstep2Chromatictone(ivNote2nd,accidentalSign2nd)
        //getThirdNoteメソッドにより、引数とした２つの音の共通倍音テーブルを取得。
        thirdNote.getThirdNote(chromaticTone1st,chromaticTone2nd)
        //第３音エリアへのビューの表示
        controlDisplay.ctlDispModeChange("overtone")
        //スピーカーボタンに対してリスナ設定を行う。
        val listenerSpeaker = BtnSpeakerClickListener(view,viewGroup)
        btnSpeaker1.setOnClickListener(listenerSpeaker)
        btnSpeaker2.setOnClickListener(listenerSpeaker)
        btnSpeaker3.setOnClickListener(listenerSpeaker)
        btnSpeaker4.setOnClickListener(listenerSpeaker)
        btnSpeaker5.setOnClickListener(listenerSpeaker)
        //ノート上の位置（ScaleStep)を算出する。
        //整数のものはScaleTableのままであるが、小数点のものはシフトさせる。
        //第１音または第２音に♭が含まれている場合は♭にシフトさせる。
        //それ以外は♯にシフトさせる。
        val thirdNoteIntegerStep:MutableList<Int> = mutableListOf(0)
        val accidentalSign3rd:MutableList<Int> = mutableListOf(0)
        for (i in 0..4) {
            if (thirdNote.thirdNoteChromaticStep[i] % 1 == 0f) {
                thirdNoteIntegerStep.add(i, thirdNote.thirdNoteChromaticStep[i].toInt())
                accidentalSign3rd.add(i, 0)
            } else {
                //どちらかが♭だったら。
                if (accidentalSign1st == 1 || accidentalSign2nd ==1){
                    val postAdjust = thirdNote.thirdNoteChromaticStep[i] - 0.5f
                    thirdNoteIntegerStep.add(i, postAdjust.toInt())
                    accidentalSign3rd.add(i, 1)
                } else {
                    val postAdjust = thirdNote.thirdNoteChromaticStep[i] + 0.5f
                    thirdNoteIntegerStep.add(i, postAdjust.toInt())
                    accidentalSign3rd.add(i, -1)
                }
            }
        }

        //取得した第３音（の最低音）を元にレイアウト（画面上の高さ）を決定する。
        //第１音から第５音までまで表示するには高さに幅が必要なため第１音が低い位置に表示されるように設定する。
        //ただしそれでは見にくいため、NumTonesで１を選択した場合は、高い位置に設定し、できるだけ
        //通常のト音記号で表示されるようにする。
        val thirdNotePhysicalStep:MutableList<Int> = mutableListOf(0)
        if (prefNumNotes == 1) {
            when (thirdNote.thirdNoteChromaticStep[0]) {
                in 0f..17f -> {
                    ivClef3rd.setImageResource(R.drawable.gclef8)
                    for (i in 0..4) {
                        thirdNotePhysicalStep.add(i, thirdNoteIntegerStep[i] + 6)
                    }
                }
                in 17.5f..38f -> {
                    ivClef3rd.setImageResource(R.drawable.gclef)
                    for (i in 0..4) {
                        thirdNotePhysicalStep.add(i, thirdNoteIntegerStep[i] - 15)
                    }
                }
                in 38.5f..55.5f -> {
                    ivClef3rd.setImageResource(R.drawable.fclef)
                    for (i in 0..4) {
                        thirdNotePhysicalStep.add(i, thirdNoteIntegerStep[i] - 29)
                    }
                }
            }
        } else {
            //　　　第３音が G6以上 ト音記号 +22
            //　　　　　　　 G5以上 ト音記号 +15
            //　　　　　　　 G4以上 ト音記号 +8
            //　　　　　　　 G3以上 ト音記号
            //            上記未満　　　へ音記号
            //実際の画面に表示する高さをthirdNotePhysicalStepに設定する。
            //ト音記号+15はなじみがないため、ト音記号+8の範囲を広くしている。
            when (thirdNote.thirdNoteChromaticStep[0]) {
                in 0f..17f -> {
                    ivClef3rd.setImageResource(R.drawable.gclef22)
                    for (i in 0..4) {
                        //physicalStep3rdの設定（thirdNoteNaturalStep[i]を第３音画面に合わせスライドさせる。）
                        thirdNotePhysicalStep.add(i, thirdNoteIntegerStep[i] + 6)
                    }
                }
                in 17.5f..24f -> {
                    ivClef3rd.setImageResource(R.drawable.gclef15)
                    for (i in 0..4) {
                        thirdNotePhysicalStep.add(i, thirdNoteIntegerStep[i] - 1)
                    }
                }
                in 24.5f..31f -> {
                    ivClef3rd.setImageResource(R.drawable.gclef8)
                    for (i in 0..4) {
                        thirdNotePhysicalStep.add(i, thirdNoteIntegerStep[i] - 8)
                    }
                }
                in 31.5f..38f -> {
                    ivClef3rd.setImageResource(R.drawable.gclef)
                    for (i in 0..4) {
                        thirdNotePhysicalStep.add(i, thirdNoteIntegerStep[i] - 15)
                    }
                }
                in 38.5f..55.5f -> {
                    ivClef3rd.setImageResource(R.drawable.fclef)
                    for (i in 0..4) {
                        thirdNotePhysicalStep.add(i, thirdNoteIntegerStep[i] - 29)
                    }
                }
            }
        }

        //第３音のレイアウト（描画位置指定）
        var ivNote3rd = ivNote3rd1
        var tvDesc1st = tvDesc1st1
        var tvDesc2nd = tvDesc2nd1
        for (i in 0..4) {
            when (i){
                1 -> {
                    ivNote3rd = ivNote3rd2
                    tvDesc1st = tvDesc1st2
                    tvDesc2nd = tvDesc2nd2
                }
                2 -> {
                    ivNote3rd = ivNote3rd3
                    tvDesc1st = tvDesc1st3
                    tvDesc2nd = tvDesc2nd3
                } 3 -> {
                    ivNote3rd = ivNote3rd4
                    tvDesc1st = tvDesc1st4
                    tvDesc2nd = tvDesc2nd4
                } 4 -> {
                    ivNote3rd = ivNote3rd5
                    tvDesc1st = tvDesc1st5
                    tvDesc2nd = tvDesc2nd5
                }
            }
            ivNote3rd.layout(
                ivNote3rd.left,
                thirdNotePhysicalStep[i] * oneLineHeight.toInt() - noteHeightHalf,
                ivNote3rd.left + ivNote3rd.width,
                ((thirdNotePhysicalStep[i] * oneLineHeight.toInt()) - noteHeightHalf) + ivNote3rd.height
            )
            Log.i("PosHight","thirdNotePhysicalStep[i] * oneLineHeight ${thirdNotePhysicalStep[i] * oneLineHeight}")
            when (accidentalSign3rd[i] ) {
                1 -> ivNote3rd.setImageResource(R.drawable.zenonpu_flat)
                0 -> ivNote3rd.setImageResource(R.drawable.zenonpu)
                -1 -> ivNote3rd.setImageResource(R.drawable.zenonpu_sharp)
            }
            //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
            //新しい音符位置での五線譜の再描画を行う。
            val drawLine = DrawLine()
            drawLine.drawline(viewGroup)

            //インターバル文言（1 octave aboveとか)を設定する。
            tvDesc1st.text = thirdNote.thirdNoteDesc1stTable[i]
            tvDesc2nd.text = thirdNote.thirdNoteDesc2ndTable[i]
        }
    }

    companion object {
        private const val tagMsg = "MyInfo_BtnOvertone : "
    }
}