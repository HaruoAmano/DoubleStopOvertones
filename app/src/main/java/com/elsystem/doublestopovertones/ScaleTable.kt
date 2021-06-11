package com.elsystem.doublestopovertones

import kotlin.math.pow

//*内部処理の基本となるScaleテーブルを作成する。
//最高音１オクターブをリテラルデータとして持たせ、
//そこから７オクターブ分増幅してテーブル（scaleTable）を完成している。
//管理しているのは以下の４項目
//chromaticStep：画面内容から導出した0.5刻みの数値（昇順となっておりToneと逆である点注意）
// chromaticTone：１２進数化した音高（ex.5B）
// chromaticFreq：周波数 一旦440HZ基準でテーブル作成し、ThirdNoteTable設定時にPreference指定に合わせ変調する。
// chromaticName：音解名（ex.B6）
class ScaleTable {
    //増幅先
    var chromaticStep:MutableList<Float> = mutableListOf(0f)
    var chromaticTone:MutableList<String> = mutableListOf("")
    var chromaticFreq:MutableList<Float> = mutableListOf(0f)
    var chromaticName:MutableList<String> = mutableListOf("")
    //増幅のもととなる表
    private val stepBase:List<Float> = listOf(0f,1f,1.5f,2f,2.5f,3f,3.5f,4f,5f,5.5f,6f,6.5f)
    private val toneBase:List<String> = listOf( "80","7B","7A","79","78","77","76","75","74","73","72","71")
    private val freqBase: List<Float> = listOf(8372.018f,7902.132f,7458.62f,7040f,6644.876f,6271.926f,5919.91f,5587.652f,5274.04f,4978.032f,4698.636f,4434.922f)
    private val nameBase: List<String> = listOf(" C9"," B8","#A8"," A8","#G8"," G8","#F8"," F8"," E8","#D8"," D8","#C8")
    //各インデックスについて
    //k:増幅先を指定するインデックス
    //i:オクターブを指すインデックス（各値の算出のみに使用）
    //j:12音を指すインデックス
    init{
        //ステップ
        var k=0
        for (i in 0..7) {
            for (j in 0..11) {
                chromaticStep.add(k,stepBase[j] + 7f * i)
                k += 1
            }
        }
        //トーン
        var charArray1: CharArray
        var tenPlace: String
        var onePlace: String
        k = 0
        for (i in 0..7) {
            for (j in 0..11) {
                charArray1 = toneBase[j].toCharArray()
                tenPlace = charArray1[0].toString()
                onePlace = charArray1[1].toString()
                chromaticTone.add(k,(tenPlace.toInt() - i).toString() + onePlace)
                k += 1
            }
        }
        //周波数
        k = 0

        for (i in 0..7) {
            for (j in 0..11) {
                chromaticFreq.add(k, freqBase[j] / (2.0.pow(i.toDouble())).toFloat())
                k += 1
            }
        }
        //音名
        var charArray2: CharArray
        var toneName: String
        var toneOctave: String
        k = 0
        for (i in 0..7) {
            for (j in 0..11) {
                charArray2 = nameBase[j].toCharArray()
                toneName = charArray2[0].toString() + charArray2[1].toString()
                toneOctave = charArray2[2].toString()
                chromaticName.add(k, toneName + (toneOctave.toInt() - i).toString())
                k += 1
            }
        }
//        for (i in 0..95){
//            Log.i("ScaleTable","${chromaticStep[i]},${chromaticTone[i]},${chromaticFreq[i]},${chromaticName[i]}")
//        }
    }
}




