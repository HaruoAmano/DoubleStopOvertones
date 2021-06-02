package com.example.doublestopovertones

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class BtnOvertoneClickListener(context: Context, viewGroup: ViewGroup) : View.OnClickListener {
    //オーバートーンボタン押下時の処理。
    //画面より第１音・第２音の音符情報を取得し12進に変換後ThardNoteクラスで各倍音列を生成後
    //それらに共通する第３音情報を取得する。
    //その後、第３音情報を第３音ビューエリアに表示するまでを行う。
    var util = Util()
    val ivNote1st = viewGroup.findViewById<ImageView>(R.id.ivNote1st)
    val ivNote2nd = viewGroup.findViewById<ImageView>(R.id.ivNote2nd)
    val tvDesc = viewGroup.findViewById<TextView>(R.id.TvDesc)
    val tvBack3rd = viewGroup.findViewById<TextView>(R.id.tvBack3rd)
    val thirdNoteView = viewGroup.findViewById<View>(R.id.ThirdNoteView)
    val ivClef3rd =viewGroup.findViewById<ImageView>(R.id.ivClef3rd)
    val ivNote3rd1 = viewGroup.findViewById<ImageView>(R.id.ivNote3rd1)
    val ivNote3rd2 = viewGroup.findViewById<ImageView>(R.id.ivNote3rd2)
    val ivNote3rd3 = viewGroup.findViewById<ImageView>(R.id.ivNote3rd3)
    val ivNote3rd4 = viewGroup.findViewById<ImageView>(R.id.ivNote3rd4)
    val ivNote3rd5 = viewGroup.findViewById<ImageView>(R.id.ivNote3rd5)
    val ivSpeaker = viewGroup.findViewById<ImageView>(R.id.ivSpeaker)
    val tvDesc1st1 = viewGroup.findViewById<TextView>(R.id.tvDesc1st1)
    val tvDesc1st2 = viewGroup.findViewById<TextView>(R.id.tvDesc1st2)
    val tvDesc1st3 = viewGroup.findViewById<TextView>(R.id.tvDesc1st3)
    val tvDesc1st4 = viewGroup.findViewById<TextView>(R.id.tvDesc1st4)
    val tvDesc1st5 = viewGroup.findViewById<TextView>(R.id.tvDesc1st5)
    val tvDesc2nd1 = viewGroup.findViewById<TextView>(R.id.tvDesc2nd1)
    val tvDesc2nd2 = viewGroup.findViewById<TextView>(R.id.tvDesc2nd2)
    val tvDesc2nd3 = viewGroup.findViewById<TextView>(R.id.tvDesc2nd3)
    val tvDesc2nd4 = viewGroup.findViewById<TextView>(R.id.tvDesc2nd4)
    val tvDesc2nd5 = viewGroup.findViewById<TextView>(R.id.tvDesc2nd5)
    val thirdNote = ThirdNote()
//    val thirdNoteView = viewGroup.findViewById<com.example.doublestopovertones.NoteView>(R.id.ThirdNoteView)

    override fun onClick(v: View) {
        Log.i(tagMsg,"@@@@@@@@@@OVERTONE@@@@@@@@@@@ ボタン押下")
        //第１音の音高を取得する。
        chromaticTone1st = util.convPhisicalstep2Chromatictone(ivNote1st,noteFormat,accidentalSign1st)
        //第２音の音高を取得する。
        chromaticTone2nd = util.convPhisicalstep2Chromatictone(ivNote2nd,noteFormat,accidentalSign2nd)
        //getThirdNoteメソッドにより、引数とした２つの音の共通倍音テーブルを取得。
        thirdNote.getThirdNote(chromaticTone1st,chromaticTone2nd)

        //第３音エリアへのビューの表示
        tvDesc.visibility = View.INVISIBLE
        thirdNoteView.visibility = View.VISIBLE
        ivClef3rd.visibility = View.VISIBLE
        //音部記号の位置を設定。
        util.moveNote(ivClef3rd, (oneLineHeight * 5).toInt())
        //背景色を変更
        tvBack3rd.setBackgroundColor(Color.rgb(200,231,237))
        thirdNoteView.setBackgroundColor(Color.rgb(200,231,237))
        //第３音の表示
        //Toneが80を超えるものは非表示にする。
        //、、、、ビューの配列化ができればもっと、シンプルで安全に組めるはずだが、、、、
        if (thirdNote.thirdNoteChromaticTone[0] != "TH") {
            ivNote3rd1.setVisibility(View.VISIBLE)
            util.reLayout(ivNote3rd1)
        }
        if (thirdNote.thirdNoteChromaticTone[1] != "TH") {
            ivNote3rd2.setVisibility(View.VISIBLE)
            util.reLayout(ivNote3rd2)
        }
        if (thirdNote.thirdNoteChromaticTone[2] != "TH") {
            ivNote3rd3.setVisibility(View.VISIBLE)
            util.reLayout(ivNote3rd3)
        }
        if (thirdNote.thirdNoteChromaticTone[3] != "TH") {
            ivNote3rd4.setVisibility(View.VISIBLE)
            util.reLayout(ivNote3rd4)
        }
        if (thirdNote.thirdNoteChromaticTone[5] != "TH") {
            ivNote3rd5.setVisibility(View.VISIBLE)
            util.reLayout(ivNote3rd5)
        }
        //ノート上の位置（ScaleStep)を算出する。
        //整数のものはScaleTableのままであるが、小数点のものはシフトさせる。
        //第１音または第２音に♭が含まれている場合は♭にシフトさせる。
        //それ以外は♯にシフトさせる。
        var thirdNoteIntegerStep:MutableList<Int> = mutableListOf(0)
        var accidentalSign3rd:MutableList<Int> = mutableListOf(0)
        for (i in 0..4) {
//            Log.i(tagMsg,"thirdNote.thirdNoteStepTable[i]: $thirdNote.thirdNoteStepTable[i]")
            if (thirdNote.thirdNoteChromaticStep[i] % 1 == 0f) {
                thirdNoteIntegerStep.add(i, thirdNote.thirdNoteChromaticStep[i].toInt())
                accidentalSign3rd.add(i, 0)
            } else {
                //どちらかが♭だったら。
                if (accidentalSign1st == 1 || accidentalSign2nd ==1){
                    var postAdjust = thirdNote.thirdNoteChromaticStep[i] - 0.5f
                    thirdNoteIntegerStep.add(i, postAdjust.toInt())
                    accidentalSign3rd.add(i, 1)
                } else {
                    var postAdjust = thirdNote.thirdNoteChromaticStep[i] + 0.5f
                    thirdNoteIntegerStep.add(i, postAdjust.toInt())
                    accidentalSign3rd.add(i, -1)
                }
            }
        }

        //取得した第３音（の最低音）を元にレイアウトを決定する。
        //　　　第３音が G7(67）以上 ト音記号 +22
        //　　　　　　　 G6(57）以上 ト音記号 +15
        //　　　　　　　 D5(42) 以上 ト音記号 +8
        //　　　　　　　 G3(27) 以上 ト音記号
        //            上記未満　　　へ音記号
        //実際の画面に表示する高さをthirdNotePhisacalStepに設定する。
        var thirdNotePhisicalStep:MutableList<Int> = mutableListOf(0)
        //ト音記号+15はなじみがないため、ト音記号+8の範囲を広くしている。
        Log.i("thirdNoteChosa","thirdNote.thirdNoteToneTable[0]:$thirdNote.thirdNoteToneTable[0]")
        if (thirdNote.thirdNoteChromaticTone[0] >= "57"){
            ivClef3rd.setImageResource(R.drawable.gclef22)
            for (i in 0..4) {
                //phisicalStep3rdの設定（thirdNoteNaturalStep[i]を第３音画面に合わせスライドさせる。）
                thirdNotePhisicalStep.add(i, thirdNoteIntegerStep[i] + 6)
                Log.i("thirdNoteChosa","thirdNote.thirdNoteToneTable[i]:${thirdNote.thirdNoteChromaticTone[i]}")
                Log.i("thirdNoteChosa","thirdNoteIntegerStep[i]:$thirdNoteIntegerStep[i]")
                Log.i("thirdNoteChosa","thirdNotePhisacalStep[i]:$thirdNotePhisicalStep[i]")
            }
        }else if (thirdNote.thirdNoteChromaticTone[0] >= "47"){
            ivClef3rd.setImageResource(R.drawable.gclef15)
            for (i in 0..4) {
                thirdNotePhisicalStep.add(i, thirdNoteIntegerStep[i] - 1)
                Log.i("thirdNoteChosa","thirdNote.thirdNoteToneTable[i]:${thirdNote.thirdNoteChromaticTone[i]}")
                Log.i("thirdNoteChosa","thirdNoteIntegerStep[i]:$thirdNoteIntegerStep[i]")
                Log.i("thirdNoteChosa","thirdNotePhisacalStep[i]:$thirdNotePhisicalStep[i]")
            }
        }else if (thirdNote.thirdNoteChromaticTone[0] >= "37"){
            ivClef3rd.setImageResource(R.drawable.gclef8)
            for (i in 0..4) {
                thirdNotePhisicalStep.add(i, thirdNoteIntegerStep[i] - 8)
                Log.i("thirdNoteChosa","thirdNote.thirdNoteToneTable[i]:${thirdNote.thirdNoteChromaticTone[i]}")
                Log.i("thirdNoteChosa","thirdNoteIntegerStep[i]:$thirdNoteIntegerStep[i]")
                Log.i("thirdNoteChosa","thirdNotePhisacalStep[i]:$thirdNotePhisicalStep[i]")
            }
        }else if (thirdNote.thirdNoteChromaticTone[0] >= "27"){
            ivClef3rd.setImageResource(R.drawable.gclef)
            for (i in 0..4) {
                thirdNotePhisicalStep.add(i, thirdNoteIntegerStep[i] - 15)
                Log.i("thirdNoteChosa","thirdNote.thirdNoteToneTable[i]:${thirdNote.thirdNoteChromaticTone[i]}")
                Log.i("thirdNoteChosa","thirdNoteIntegerStep[i]:$thirdNoteIntegerStep[i]")
                Log.i("thirdNoteChosa","thirdNotePhisacalStep[i]:$thirdNotePhisicalStep[i]")
            }
        }else{
            ivClef3rd.setImageResource(R.drawable.fclef)
            for (i in 0..4) {
                thirdNotePhisicalStep.add(i, thirdNoteIntegerStep[i] - 29)
                Log.i("thirdNoteChosa","thirdNote.thirdNoteToneTable[i]:${thirdNote.thirdNoteChromaticTone[i]}")
                Log.i("thirdNoteChosa","thirdNoteIntegerStep[i]:$thirdNoteIntegerStep[i]")
                Log.i("thirdNoteChosa","thirdNotePhisacalStep[i]:$thirdNotePhisicalStep[i]")
            }
        }

        //第３音のレイアウト
        var ivNote3rd = ivNote3rd1
        var tvDesc1st = tvDesc1st1
        var tvDesc2nd = tvDesc2nd1
        for (i in 0..4) {
            when (i){
                1 -> {
                    ivNote3rd = ivNote3rd2
                    tvDesc1st = tvDesc1st2
                    tvDesc2nd = tvDesc2nd2
                    ivNote3rd.setColorFilter(Color.parseColor(MY_COLOR_PINK), PorterDuff.Mode.SRC_IN)

                }
                2 -> {
                    ivNote3rd = ivNote3rd3
                    tvDesc1st = tvDesc1st3
                    tvDesc2nd = tvDesc2nd3
                    ivNote3rd.setColorFilter(Color.parseColor(MY_COLOR_ORANGE), PorterDuff.Mode.SRC_IN)

                } 3 -> {
                    ivNote3rd = ivNote3rd4
                    tvDesc1st = tvDesc1st4
                    tvDesc2nd = tvDesc2nd4
                ivNote3rd.setColorFilter(Color.parseColor(MY_PEAL_GREEN), PorterDuff.Mode.SRC_IN)

                } 4 -> {
                    ivNote3rd = ivNote3rd5
                    tvDesc1st = tvDesc1st5
                    tvDesc2nd = tvDesc2nd5
                ivNote3rd.setColorFilter(Color.parseColor(MY_MOREPEAL_GREEN), PorterDuff.Mode.SRC_IN)

                }
            }
            ivNote3rd.layout(
                ivNote3rd.left,
                thirdNotePhisicalStep[i] * oneLineHeight.toInt() - ivNoteHeightHalf,
                ivNote3rd.left + ivNote3rd.width,
                ((thirdNotePhisicalStep[i] * oneLineHeight.toInt()) - ivNoteHeightHalf) + ivNote3rd.height
            )
            when (accidentalSign3rd[i] ) {
                1 -> ivNote3rd.setImageResource(R.drawable.zenonpu_flat)
                0 -> ivNote3rd.setImageResource(R.drawable.zenonpu)
                -1 -> ivNote3rd.setImageResource(R.drawable.zenonpu_sharp)
            }

            //インターバル文言（1 octave aboveとか)を設定する。
            tvDesc1st.setText(thirdNote.thirdNoteDesc1stTable[i])
            tvDesc2nd.setText(thirdNote.thirdNoteDesc2ndTable[i])
        }

        //スピーカーボタンを有効にする。
        ivSpeaker.isEnabled = true
        ivSpeaker.setColorFilter(R.color.purple_500, PorterDuff.Mode.SRC_IN)
    }
    companion object {
        private const val tagMsg = "MyInfo_BtnOvertone : "
    }
}