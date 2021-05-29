package com.example.doublestopoverTone

import android.util.Log

//*内部処理の基本となるScaleテーブルをdata classを使用して作成する。
//最高音１オクターブをdata class「ScaleTableBaseData」にデータを持たせ、
//そこから７オクターブ分を算出してテーブルを完成している。
//ScaleTableはあらかじめ全項目に初期値を設定しているが、String型Arrayの生成時のarrayOfNullsの
//使い方がよくわからかったため、とりあえずこうしている。
class ScaleTable {
    var scaleTable = ScaleTableData()
    var scaleTableBase = ScaleTableBaseData()
    var k = 0
    init{
        //スケールテーブル ー ステップ
        //８オクターブ
        for (i in 0..7) {
            //１２倍音
            for (j in 0..11) {
                scaleTable.chromaticStep[k] =  scaleTableBase.chromaticStep[j] + 7f * i
                k += 1
            }
        }
        //スケールテーブル ー トーン
        var charArray1: CharArray
        var tenPlace: String
        var onePlace: String
        k = 0
        for (i in 0..7) {
            for (j in 0..11) {
                charArray1 = scaleTableBase.chromaticTone[j].toCharArray()
                tenPlace = charArray1[0].toString()
                onePlace = charArray1[1].toString()
                scaleTable.chromaticTone[k] = (tenPlace.toInt() - i).toString() + onePlace
                k += 1
            }
        }
        //スケールテーブル ー 周波数
        k = 0
        for (i in 0..7) {
            for (j in 0..11) {
                scaleTable.chromaticFreq[k] = scaleTableBase.chromaticFreq[j] / (Math.pow(2.0, i.toDouble())).toFloat()
                k += 1
            }
        }
        //スケールテーブル ー 音名
        var charArray2: CharArray
        var toneName: String
        var toneOctave: String
        k = 0
        for (i in 0..7) {
            for (j in 0..11) {
                charArray2 = scaleTableBase.chromaticName[j].toCharArray()
                toneName = charArray2[0].toString() + charArray2[1].toString()
                toneOctave = charArray2[2].toString()
                scaleTable.chromaticName[k] = toneName + (toneOctave.toInt() - i).toString()
                k += 1
            }
        }
        for (i in 0..95){
            //Log.i("ScaleTable","${scaleTable.chromaticStep[i]},${scaleTable.chromaticTone[i]},${scaleTable.chromaticFreq[i]},${scaleTable.chromaticName[i]}")
        }
    }
    //最終的な表はここに作成される。
    //Stringがたについてはnull以外で初期化する方法がわからないので96個並べた。
    data class ScaleTableData (
            var chromaticStep: FloatArray = FloatArray (96),
            var chromaticTone: Array<String> = arrayOf
            ("00","00","00","00","00","00","00","00","00","00","00","00",
                    "00","00","00","00","00","00","00","00","00","00","00","00",
                    "00","00","00","00","00","00","00","00","00","00","00","00",
                    "00","00","00","00","00","00","00","00","00","00","00","00",
                    "00","00","00","00","00","00","00","00","00","00","00","00",
                    "00","00","00","00","00","00","00","00","00","00","00","00",
                    "00","00","00","00","00","00","00","00","00","00","00","00",
                    "00","00","00","00","00","00","00","00","00","00","00","00"),
            var chromaticFreq: FloatArray = FloatArray(96),
            var chromaticName: Array<String> = arrayOf
            ("00","00","00","00","00","00","00","00","00","00","00","00",
                    "00","00","00","00","00","00","00","00","00","00","00","00",
                    "00","00","00","00","00","00","00","00","00","00","00","00",
                    "00","00","00","00","00","00","00","00","00","00","00","00",
                    "00","00","00","00","00","00","00","00","00","00","00","00",
                    "00","00","00","00","00","00","00","00","00","00","00","00",
                    "00","00","00","00","00","00","00","00","00","00","00","00",
                    "00","00","00","00","00","00","00","00","00","00","00","00"))
    {
        //データクラスの流儀に則り、equelsとhashcodeメソッドをオーバーライドする。
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ScaleTableData

            if (!chromaticStep.contentEquals(other.chromaticStep)) return false
            if (!chromaticTone.contentEquals(other.chromaticTone)) return false
            if (!chromaticFreq.contentEquals(other.chromaticFreq)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = chromaticStep.contentHashCode()
            result = 31 * result + chromaticTone.contentHashCode()
            result = 31 * result + chromaticFreq.contentHashCode()
            return result
        }
    }
    //増幅のもととなる表
    data class ScaleTableBaseData (
            val chromaticStep: FloatArray = floatArrayOf
            (   0f,   1f,   1.5f,   2f,   2.5f,   3f,   3.5f,   4f,   5f,   5.5f,   6f,   6.5f),
            val chromaticTone: Array<String> = arrayOf
            ( "80", "7B", "7A", "79", "78", "77", "76", "75", "74", "73", "72", "71"),
            val chromaticFreq: FloatArray = floatArrayOf
            (8372.018f,7902.132f,7458.62f,7040f,6644.876f,6271.926f,5919.91f,5587.652f,5274.04f,4978.032f,4698.636f,4434.922f),
            val chromaticName: Array<String> = arrayOf
            (" C9"," H8","#A8"," A8","#G8"," G8","#F8"," F8"," E8","#D8"," D8","#C8",))
    {
        //データクラスの流儀に則り、equelsとhashcodeメソッドをオーバーライドする。
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ScaleTableBaseData

            if (!chromaticStep.contentEquals(other.chromaticStep)) return false
            if (!chromaticTone.contentEquals(other.chromaticTone)) return false
            if (!chromaticFreq.contentEquals(other.chromaticFreq)) return false

            return true
        }
        override fun hashCode(): Int {
            var result = chromaticStep.contentHashCode()
            result = 31 * result + chromaticTone.contentHashCode()
            result = 31 * result + chromaticFreq.contentHashCode()
            return result
        }
    }
}




