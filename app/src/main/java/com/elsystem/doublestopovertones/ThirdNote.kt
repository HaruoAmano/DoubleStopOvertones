package com.elsystem.doublestopovertones

import android.util.Log

class ThirdNote {
    //引き渡された第１音、第２音から第３音を導出する。
    //まず、当モジュールに管理されるspanOvertone、spanOvertoneDescを使用して。
    //第３音の音高、説明文を取得する。（実用性を考え、第３音は５番目までの取得にとどめる。）
    //その後、求まった第３音の音高を元にscaleTableよりステップ、周波数、音名を取得する。
    private var util = Util()
    private val scaleTable = ScaleTable()
    //基音からの音高を12進で管理。22倍音まで管理。
    private val spanOvertone:List<String> = listOf(
        "00", "10", "17", "20", "24", "27", "2A",
        "30", "32", "34", "36", "37", "38", "3A",
        "3B", "40", "41", "42", "43", "44", "45", "46")
    //基音からの音高を言葉で説明したものを管理。
    private val spanOvertoneDesc:List<String> = listOf(
        "same tone", "1 octave", "1 octave + 5th", "2 octave", "2 octave + major3rd", "2 octave + 5th", "2 octave + 7th",
        "3 octave", "3 octave + 2nd", "3 octave + major3rd", "3 octave + augment4th", "3 octave + 5th", "3 octave + minor6th",
        "3 octave + 7th", "3 octave + major7th", "4 octave", "4 octave + minor2nd", "4 octave + 2nd", "4 octave + minor3rd",
        "4 octave + major3rd", "4 octave + 4th", "4 octave + augment4th")
    var thirdNoteChromaticTone:MutableList<String> = mutableListOf("")
    var thirdNoteDesc1stTable:MutableList<String> = mutableListOf("")
    var thirdNoteDesc2ndTable:MutableList<String> = mutableListOf("")
    var thirdNoteChromaticStep:MutableList<Float> = mutableListOf(0f)
    var thirdNoteName:MutableList<String> = mutableListOf("")
    fun getThirdNote(chromaticTone1st: String, chromaticTone2nd: String) {
        //最初に第３音の音高および説明を取得する。
        //kは第３音格納先テーブルのインデックス。
        var k = 0
        var thirdNoteIdxOver = false
        var overtone: String
        var overtone1st: String
        var overtone2nd: String
        //第２２倍音まで当アプリでは取り扱う。
        //第１音のループ処理を行う。
        for (i in 0..21) {
            if (thirdNoteIdxOver) {
                break
            }
            //画面で指定された音高（chromaticTone1st）の倍音（基音にspanOvertoneを順次加算）を求める。
            overtone = util.additionTwelve(chromaticTone1st, spanOvertone[i])
            //共通倍音が”80”を超えていた場合は、テーブルが対応不可のため、"TH"(Too High)を設定し、
            //非表示にする。
            if (overtone <= "80"){
                overtone1st = overtone
            } else {
                overtone1st = "TH"
            }
            //第２音のループ処理を行う。
            for (j in 0..21) {
                if (thirdNoteIdxOver) {
                    break
                }
                //画面で指定された音高（chromaticTone2nd）の倍音（基音にspanOvertoneを順次加算）を求める。
                overtone = util.additionTwelve(chromaticTone2nd, spanOvertone[j])
                //共通倍音が”80”を超えていた場合は、テーブルが対応不可のため、"TH"(Too High)を設定し、
                //非表示にする。
                if (overtone <= "80"){
                    overtone2nd = overtone
                } else {
                    overtone2nd = "TH"
                }
//                Log.i(tagMsg,"overtone2nd $overtone2nd")
                //第１音と第２音で共通であればthirdNote各Tableに設定する。
                //ただし、同音同士の場合は基音は除外する。
                if (overtone1st == overtone2nd) {
                    if (chromaticTone1st == chromaticTone2nd && overtone1st == chromaticTone1st) {
                    } else {
                        thirdNoteChromaticTone.add(k, overtone1st)
                        if (overtone1st != "TH") {
                            thirdNoteDesc1stTable.add(k, spanOvertoneDesc[i])
                            thirdNoteDesc2ndTable.add(k, spanOvertoneDesc[j])
                        }else{
                            thirdNoteDesc1stTable.add(k, "")
                            thirdNoteDesc2ndTable.add(k, "")
                        }
                        k += 1
                        if (k > 4) {
                            thirdNoteIdxOver = true
                        }
                    }
                }
            }
        }
        Log.i(tagMsg,"thirdNoteChromaticTone $thirdNoteChromaticTone")
        //第３音のステップ（ノート上の位置）および周波数・名前をScaleTableより取得する。
        var coefficient = 0f
        when (prefBaseFreq) {
            1 -> coefficient = (440f / 440f)  //coefficient:係数
            2 -> coefficient = (441f / 440f)
            3 -> coefficient = (442f / 440f)
            4 -> coefficient = (443f / 440f)
            5 -> coefficient = (444f / 440f)
            6 -> coefficient = (445f / 440f)
        }
        for (l in 0..4) {
            if (thirdNoteChromaticTone[l] == "TH" || thirdNoteChromaticTone[l] == "NG") {
                thirdNoteChromaticStep.add(l, 0f)
                thirdNoteFreq.add(l, 0f)
                thirdNoteName.add(l, "")
            } else {
                for (m in 0..95) {
                    if (thirdNoteChromaticTone[l] == scaleTable.chromaticTone[m]) {
                        thirdNoteChromaticStep.add(l, scaleTable.chromaticStep[m])
                        thirdNoteFreq.add(l, scaleTable.chromaticFreq[m] * coefficient)
                        thirdNoteName.add(l, scaleTable.chromaticName[m])
                    }
                }
            }
        }
        for (l in 0..4) {
            Log.i("My_thirdNote","${thirdNoteChromaticTone[l]},${thirdNoteChromaticStep[l]}," +
                    "${thirdNoteFreq[l]},${thirdNoteName[l]},${thirdNoteDesc1stTable[l]},${thirdNoteDesc2ndTable[l]}")
        }
    }
    companion object {
        private const val tagMsg = "My_ThirdNote"
    }
}
