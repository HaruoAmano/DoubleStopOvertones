package com.example.doublestopovertones

import android.util.Log

//トップレベルでの定義。 他の.ktファイルから参照が必要なものについてここで定義。
//スピーカーで鳴らすための周波数を確保するためのフィールド。BtnSpeakerClickListnerで使用する。
var thirdNoteFreq1 = 0.0

class ThirdNote {
    /**引き渡された第１音、第２音から第３音を導出する。
     * OvertoneTableは各音の倍音律のベースとなるデータを保有し、
     * ThirdNoteTableには当クラス内で算出した共通倍音を設定する。
     */
    val overtoneTable = ThirdNote.OvertoneTable()
    var thirdNoteTable = ThirdNoteTable()
    var tw = TwelveArray()

    fun getThirdNote(chromaticTone1st: String, chromaticTone2nd: String): ThirdNoteTable {
        var k: Int = 0
        var thirdNoteIdxOver = false
        //第２２倍音まで当アプリでは取り扱う。
        for (i in 0..21) {
            if (thirdNoteIdxOver == true) {
                break
            }
            for (j in 0..21) {
                if (thirdNoteIdxOver == true) {
                    break
                }
                //画面で指定された音高（chromaticTone1st）の倍音（overtoneTableをサーチ）が、
                //第１音と第２音で共通であればthirdNoteTableに設定する。
                if (tw.additionTwelve(chromaticTone1st, overtoneTable.overtone[i])
                        == tw.additionTwelve(chromaticTone2nd, overtoneTable.overtone[j])) {
                    thirdNoteTable.thirdNoteTone[k] = tw.additionTwelve(chromaticTone1st, overtoneTable.overtone[i])
                    thirdNoteTable.thirdNotedesc1[k] = overtoneTable.desc[i]
                    thirdNoteTable.thirdNotedesc2[k] = overtoneTable.desc[j]
                    //第３音のノート上の位置および周波数・名前をScaleTableより取得する。
                    for (l in 0..95) {
                        if (thirdNoteTable.thirdNoteTone[k] == scaleTable.scaleTable.chromaticTone[l])
                        {
                            thirdNoteTable.thirdNoteStep[k] = scaleTable.scaleTable.chromaticStep[l]
                            thirdNoteTable.thirdNoteFreq[k] = scaleTable.scaleTable.chromaticFreq[l]
                            thirdNoteTable.thirdNoteName[k] = scaleTable.scaleTable.chromaticName[l]
                        }
                    }
                    thirdNoteFreq1 = thirdNoteTable.thirdNoteFreq[0].toDouble()
                    k += 1
                    if (k > 5) {
                        thirdNoteIdxOver = true
                    }
                }
            }
        }
        for (k in 0..5) {
            Log.i("thirdNoteTable","${thirdNoteTable.thirdNoteTone[k]},${thirdNoteTable.thirdNoteStep[k]}," +
                    "${thirdNoteTable.thirdNoteFreq[k]},${thirdNoteTable.thirdNoteName[k]},${thirdNoteTable.thirdNotedesc1[k]},${thirdNoteTable.thirdNotedesc2[k]}")
        }
        return thirdNoteTable
    }


    data class OvertoneTable(
            val overtone: Array<String> = arrayOf
            ("00", "10", "17", "20", "24", "27", "2A",
                    "30", "32", "34", "36", "37", "38", "3A",
                    "3B", "40", "41", "42", "43", "44", "45",
                    "46"),
            val desc: Array<String> = arrayOf
            ("same tone", "1 octave above", "1 octave + 5th above", "2 octave above", "2 octave + major3rd above", "2 octave + 5th above", "2 octave + 7th above",
                    "3 octave above", "3 octave + 2nd above", "3 octave + major3rd above", "3 octave + augment4th above", "3 octave + 5th above", "3 octave + minor6th above",
                    "3 octave + 7th above", "3 octave + major7th above", "4 octave above", "4 octave + minor2nd above", "4 octave + 2nd above", "4 octave + minor3rd above",
                    "4 octave + major3rd above", "4 octave + 4th above", "4 octave + augment4th above"),
    )
    //流儀に従い以下のメソッドを追加。
    {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as OvertoneTable

            if (!overtone.contentEquals(other.overtone)) return false
            if (!desc.contentEquals(other.desc)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = overtone.contentHashCode()
            result = 31 * result + desc.contentHashCode()
            return result
        }
    }

    data class ThirdNoteTable(
            var thirdNoteTone: Array<String> = arrayOf
            ("00", "00", "00", "00", "00", "00"),
            var thirdNoteStep: FloatArray = FloatArray(6),
            //第１音と基音との距離
            var thirdNotedesc1: Array<String> = arrayOf
            ("", "", "", "", "", ""),
            //第２音と基音との距離
            var thirdNotedesc2: Array<String> = arrayOf
            ("", "", "", "", "", ""),
            var thirdNoteFreq: FloatArray = FloatArray(6),
            var thirdNoteName: Array<String> = arrayOf
            ("   ", "   ", "   ", "   ", "   ", "   "))
    //流儀に従い以下のメソッドを追加。

}
