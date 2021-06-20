package com.elsystem.doublestopovertones

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup

class PreferenceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preference)
        val rgNumNotes = findViewById<RadioGroup>(R.id.rgNumNotes)
        val rbNumNotes1 = findViewById<RadioButton>(R.id.rbNumNotes1)
        val rbNumNotes2 = findViewById<RadioButton>(R.id.rbNumNotes2)
        val rbNumNotes3 = findViewById<RadioButton>(R.id.rbNumNotes3)
        val rbNumNotes4 = findViewById<RadioButton>(R.id.rbNumNotes4)
        val rbNumNotes5 = findViewById<RadioButton>(R.id.rbNumNotes5)
        val rgBaseFreq = findViewById<RadioGroup>(R.id.rgBaseFreq)
        val rbBaseFreq1 = findViewById<RadioButton>(R.id.rbBaseFreq1)
        val rbBaseFreq2 = findViewById<RadioButton>(R.id.rbBaseFreq2)
        val rbBaseFreq3 = findViewById<RadioButton>(R.id.rbBaseFreq3)
        val rbBaseFreq4 = findViewById<RadioButton>(R.id.rbBaseFreq4)
        val rbBaseFreq5 = findViewById<RadioButton>(R.id.rbBaseFreq5)
        val rbBaseFreq6 = findViewById<RadioButton>(R.id.rbBaseFreq6)
        val btnBack = findViewById<Button>(R.id.btnBack)
        // "PrefData"という名前でインスタンスを生成。書き込み先のデータ名称となる。
        val prefData: SharedPreferences =
            getSharedPreferences("PrefData", Context.MODE_PRIVATE)
        //Preferenceデータを読み込み、読み込んだ値を各Viewに反映する。
        rgNumNotes.check(rbNumNotes3.id)
        rgBaseFreq.check(rbBaseFreq3.id)
        when (prefData.getInt("NumNotes", 1)){
            1 -> rgNumNotes.check(rbNumNotes1.id)
            2 -> rgNumNotes.check(rbNumNotes2.id)
            3 -> rgNumNotes.check(rbNumNotes3.id)
            4 -> rgNumNotes.check(rbNumNotes4.id)
            5 -> rgNumNotes.check(rbNumNotes5.id)
        }
        when (prefData.getInt("BaseFreq", 3)){
            1 -> rgBaseFreq.check(rbBaseFreq1.id)
            2 -> rgBaseFreq.check(rbBaseFreq2.id)
            3 -> rgBaseFreq.check(rbBaseFreq3.id)
            4 -> rgBaseFreq.check(rbBaseFreq4.id)
            5 -> rgBaseFreq.check(rbBaseFreq5.id)
            6 -> rgBaseFreq.check(rbBaseFreq6.id)
        }
        btnBack.setOnClickListener {
            // 画面上に設定されている値を書き込む。
            val editor = prefData.edit()
            //チェックされたラジオボタンのビューを取得する。
            val rbNumNotesChecked = findViewById<RadioButton>(rgNumNotes.checkedRadioButtonId)
            val rbBaseFreqChecked = findViewById<RadioButton>(rgBaseFreq.checkedRadioButtonId)
            //ラジオグループ内の何番目かを取得する。
            prefNumNotes = rgNumNotes.indexOfChild(rbNumNotesChecked)
            prefBaseFreq = rgBaseFreq.indexOfChild(rbBaseFreqChecked)
//            prefSpBtnDisp = swDispSpeakerBtn.isChecked
            editor.putInt("NumNotes",prefNumNotes)
            editor.putInt("BaseFreq",prefBaseFreq)
//            editor.putBoolean("SpBtnDisp",prefSpBtnDisp)
            //applyは非同期、commitは同期で書き込み。applyの方が問題が少ないかも＞
            // /data/data/com.elsystem.doublestopovertones/shared_prefs/配下に保管される。
            // （View->Tool Window）Device File Explorerで確認できる。
            editor.apply()
            finish()
        }
    }
}