package com.example.doublestopovertones

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class BtnOvertoneClickListener(context: Context, viewGroupe: ViewGroup) : View.OnClickListener {
    val ivNote1st = viewGroupe.findViewById<ImageView>(R.id.ivNote1st)
    val ivNote2nd = viewGroupe.findViewById<ImageView>(R.id.ivNote2nd)
    val tvDesc = viewGroupe.findViewById<TextView>(R.id.TvDesc)
    val tvBack3rd = viewGroupe.findViewById<TextView>(R.id.tvBack3rd)
    val thirdNoteView = viewGroupe.findViewById<View>(R.id.ThirdNoteView)
    val ivClef3rd =viewGroupe.findViewById<ImageView>(R.id.ivClef3rd)
    val ivNote3rd1 = viewGroupe.findViewById<ImageView>(R.id.ivNote3rd1)
    val ivNote3rd2 = viewGroupe.findViewById<ImageView>(R.id.ivNote3rd2)
    val ivNote3rd3 = viewGroupe.findViewById<ImageView>(R.id.ivNote3rd3)
    val ivSpeaker = viewGroupe.findViewById<ImageView>(R.id.ivSpeaker)
    val tvDesc1st0 = viewGroupe.findViewById<TextView>(R.id.tvDesc1st0)
    val tvDesc1st1 = viewGroupe.findViewById<TextView>(R.id.tvDesc1st1)
    val tvDesc1st2 = viewGroupe.findViewById<TextView>(R.id.tvDesc1st2)
    val tvDesc2nd0 = viewGroupe.findViewById<TextView>(R.id.tvDesc2nd0)
    val tvDesc2nd1 = viewGroupe.findViewById<TextView>(R.id.tvDesc2nd1)
    val tvDesc2nd2 = viewGroupe.findViewById<TextView>(R.id.tvDesc2nd2)

    override fun onClick(v: View) {
        Log.i(tagMsg,"@@@@@@@@@@OVERTONE@@@@@@@@@@@ ボタン押下")
        var util = Util()
        //第１音の音高を取得する。
        chromaticTone1st = util.convPhisicalstep2Chromatictone(ivNote1st,noteFormat,accidentalSign1st)
        //第２音の音高を取得する。
        chromaticTone2nd = util.convPhisicalstep2Chromatictone(ivNote2nd,noteFormat,accidentalSign2nd)
        //Overtoneクラスの生成。
        var findThirdNote = ThirdNote()
        //getThirdNoteメソッドにより引数とした２つの音の共通倍音テーブルを取得。
        var thirdNoteTable = findThirdNote.getThirdNote(chromaticTone1st,chromaticTone2nd)
        for (i in 0..0){
        }

        //第３音エリアへのビューの表示
        tvDesc.visibility = View.INVISIBLE
        thirdNoteView.visibility = View.VISIBLE
        ivClef3rd.visibility = View.VISIBLE        //音部記号の位置を設定。
        util.moveNote(ivClef3rd, (oneLineHeight * 5).toInt())
        //背景色を変更
        tvBack3rd.setBackgroundColor(Color.rgb(200,231,237))
        thirdNoteView.setBackgroundColor(Color.rgb(200,231,237))

        //第３音の表示
        ivNote3rd1.setVisibility(View.VISIBLE)
        ivNote3rd2.setVisibility(View.VISIBLE)
        ivNote3rd3.setVisibility(View.VISIBLE)
        util.reLayout(ivNote3rd1)
        util.reLayout(ivNote3rd2)
        util.reLayout(ivNote3rd3)

        //ノート上の位置（ScaleStep)を算出する。
        //整数のものはScaleTabeleのままであるが、小数点のものはシフトさせる。
        //＠＠＠＠一旦すべて#にシフトさせる。＠＠＠＠＠
        var thirdNoteNaturalStep = floatArrayOf(0f,0f,0f,0f,0f,0f)
        var accidentalSign3rd = intArrayOf(0,0,0,0,0,0,)
        for (i in 0..5) {
            thirdNoteNaturalStep[i] = thirdNoteTable.thirdNoteStep[i]
            if (thirdNoteTable.thirdNoteStep[i] % 1 != 0f) {
                thirdNoteNaturalStep[i] += 0.5f
                accidentalSign3rd[i] = -1
            }
        }
        //取得した第３音（の最低音）を元にレイアウトを決定する。
        //　　　第３音が C6(50）以上 ト音記号 +15
        //　　　　　　　 C5(40) 以上 ト音記号 +8
        //　　　　　　　 C4(30) 以上 ト音記号
        //実際の画面に表示する高さをthirdNotePhisacalStepに設定する。
        var thirdNotePhisacalStep = floatArrayOf(0f,0f,0f,0f,0f,0f)
        //ト音記号+15はなじみがないため、ト音記号+8の範囲を広くしている。
        if (thirdNoteTable.thirdNoteTone[0] >= "55"){
            ivClef3rd.setImageResource(R.drawable.gclef15)

            for (i in 0..5) {
                //phisicalStep3rdの設定（thirdNoteNaturalStep[i]を第３音画面に合わせスライドさせる。）
                thirdNotePhisacalStep[i] = thirdNoteNaturalStep[i] - 3f
            }
        }else if (thirdNoteTable.thirdNoteTone[0] >= "37"){
            ivClef3rd.setImageResource(R.drawable.gclef8)

            for (i in 0..5) {
                thirdNotePhisacalStep[i] = thirdNoteNaturalStep[i] - 10f
            }
        }else if (thirdNoteTable.thirdNoteTone[0] >= "27"){
            ivClef3rd.setImageResource(R.drawable.gclef)
            for (i in 0..5) {
                thirdNotePhisacalStep[i] = thirdNoteNaturalStep[i] - 17f
            }
        }else{
            ivClef3rd.setImageResource(R.drawable.fclef)
            for (i in 0..5) {
                thirdNotePhisacalStep[i] = thirdNoteNaturalStep[i] - 29f
            }
        }
        for (i in 0..2) {
            //Log.i(tagMsg,"PhisacalStep :${thirdNotePhisacalStep[i]}")
        }
        //第３音のレイアウト　ImageViewの配列化がわからないので、べたで３つまで配置する。
        //最低倍音
        ivNote3rd1.layout(
                ivNote3rd1.left,
                (thirdNotePhisacalStep[0]).toInt() *  oneLineHeight.toInt(),
                ivNote3rd1.left + ivNote3rd1.getWidth(),
                ((thirdNotePhisacalStep[0]).toInt() *  oneLineHeight.toInt()) + ivNote3rd1.getHeight())
        if (accidentalSign3rd[0] == -1){
            ivNote3rd1.setImageResource(R.drawable.zenonpu_sharp)
        }else{
            ivNote3rd1.setImageResource(R.drawable.zenonpu)
        }
        //２番目の倍音
        ivNote3rd2.layout(
                ivNote3rd2.left,
                (thirdNotePhisacalStep[1]).toInt() *  oneLineHeight.toInt(),
                ivNote3rd2.left + ivNote3rd2.getWidth(),
                ((thirdNotePhisacalStep[1]).toInt() *  oneLineHeight.toInt()) + ivNote3rd2.getHeight())
        if (accidentalSign3rd[1] == -1){
            ivNote3rd2.setImageResource(R.drawable.zenonpu_sharp)
        }else{
            ivNote3rd2.setImageResource(R.drawable.zenonpu)
        }
        ivNote3rd2.setColorFilter(Color.parseColor(COLOR_PINK), PorterDuff.Mode.SRC_IN)
        //３番目の倍音
        ivNote3rd3.layout(
                ivNote3rd3.left,
                (thirdNotePhisacalStep[2]).toInt() *  oneLineHeight.toInt(),
                ivNote3rd3.left + ivNote3rd3.getWidth(),
                ((thirdNotePhisacalStep[2]).toInt() *  oneLineHeight.toInt()) + ivNote3rd3.getHeight())
        if (accidentalSign3rd[2] == -1){
            ivNote3rd3.setImageResource(R.drawable.zenonpu_sharp)
        }else{
            ivNote3rd3.setImageResource(R.drawable.zenonpu)
        }
        ivNote3rd3.setColorFilter(Color.parseColor(COLOR_ORANGE), PorterDuff.Mode.SRC_IN)

        //インターバル文言（1 octave aboveとか)を設定する。
        tvDesc1st0.setText(thirdNoteTable.thirdNotedesc1[0])
        tvDesc1st1.setText(thirdNoteTable.thirdNotedesc1[1])
        tvDesc1st2.setText(thirdNoteTable.thirdNotedesc1[2])
        tvDesc2nd0.setText(thirdNoteTable.thirdNotedesc2[0])
        tvDesc2nd1.setText(thirdNoteTable.thirdNotedesc2[1])
        tvDesc2nd2.setText(thirdNoteTable.thirdNotedesc2[2])


        //スピーカーボタンを有効にする。
        ivSpeaker.isEnabled = true
        ivSpeaker.setColorFilter(R.color.purple_500, PorterDuff.Mode.SRC_IN)
    }
    companion object {
        private const val tagMsg = "MyInfo_BtnOvertone : "
    }
}