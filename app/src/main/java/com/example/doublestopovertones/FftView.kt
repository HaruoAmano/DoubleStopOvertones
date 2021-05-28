package com.example.doublestopovertones

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View

class FftView : View {

    private val tagMsg :String ="MyInfo_FftView"
    // ----------------------------
    // 変数
    // ----------------------------
    // Viewのサイズ
    private var currentWidth_ = 0
    private var currentHeight_ = 0

    // FFTデータ
    private var fftData_: DoubleArray? = null

    // FFTデータの色
    private var fftDataPaint_: Paint? = null

    // 対数グリッドの座標データ
    private lateinit var logGridDataX_: FloatArray
    private lateinit var logGridDataY_: FloatArray

    // 対数グリッドの色
    private var logGridPaint_: Paint? = null

    // サンプリングレート
    private var samplingRate_ = 0

    // 表示するバンドの数
    private var bandNumber_ = 0

    // バンドの矩形
    private lateinit var bandRects_: Array<RectF?>

    // バンドを表示するか(非表示でパルスを描画)
    private var isBandEnabled_ = false

    // 対数の範囲 (10^xでいうxの数)
    private var minLogarithm_ = 0
    private var maxLogarithm_ = 0

    // 対数の区間あたりの幅 (e.g. 10^1から10^2と，10^2から10^3の描画幅は一緒)
    private var logBlockWidth_ = 0f

    // X方向の表示オフセット
    private var logOffsetX_ = 0f

    // バンド全体のX方向の表示域
    private var bandRegionMinX_ = 0
    private var bandRegionMaxX_ = 0

    // 個々のバンドの幅
    private var bandWidth_ = 0

    // バンドのデータ
    private lateinit var bandFftData_: FloatArray

    // データ表示用のシェーダ
    private var fftDataShader_: LinearGradient? = null

    // コンストラクタ
    constructor(context: Context?) : super(context) {
        initialize()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initialize()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize()
    }

    // 初期化
    private fun initialize() {
        // 変数設定
        bandNumber_ = BAND_DEFAULT_NUMBER
        isBandEnabled_ = false
        // バーの領域確保
        bandRects_ = arrayOfNulls(bandNumber_)
        for (i in 0 until bandNumber_) {
            bandRects_[i] = RectF()
        }
        // データを格納する配列
        bandFftData_ = FloatArray(bandNumber_)
        // ペイントの設定
        fftDataPaint_ = Paint()
        fftDataPaint_!!.strokeWidth = 3f
        fftDataPaint_!!.color = resources.getColor(R.color.holo_blue_dark)
//        fftDataPaint_!!.isAntiAlias = true
        logGridPaint_ = Paint()
        logGridPaint_!!.strokeWidth = 1f
        logGridPaint_!!.isAntiAlias = true
        logGridPaint_!!.color = resources.getColor(LOG_GRID_COLOR_ID)
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        // Viewの高さ，幅が取れるのでそれらに依存した計算を行う
        currentHeight_ = height
        currentWidth_ = width
        calculateViewSizeDependedData()
    }

    // Viewのサイズを基に対数グリッドとバーの座標を計算
    private fun calculateViewSizeDependedData() {
        // 対数の範囲を計算
        minLogarithm_ = Math.floor(Math.log10(DISPLAY_MINIMUM_HZ.toDouble())).toInt()
        maxLogarithm_ = Math.ceil(Math.log10(DISPLAY_MAXIMUM_HZ.toDouble())).toInt()
        // 対数の区間あたりの幅
        logBlockWidth_ = (width / (Math.log10(DISPLAY_MAXIMUM_HZ.toDouble()) - Math.log10(
            DISPLAY_MINIMUM_HZ.toDouble()))).toFloat()
        // X方向の表示オフセット
        logOffsetX_ = (Math.log10(DISPLAY_MINIMUM_HZ.toDouble()) * logBlockWidth_).toFloat()

        // グリッドの線の数を数えて領域を確保，座標を計算して格納
        // 縦
        var lineNumberX = 10 - (DISPLAY_MINIMUM_HZ / Math.pow(10.0, minLogarithm_.toDouble())).toInt()
        lineNumberX += 9 * (maxLogarithm_ - minLogarithm_ - 2)
        lineNumberX += (DISPLAY_MAXIMUM_HZ / Math.pow(10.0, (maxLogarithm_ - 1).toDouble())).toInt()
        logGridDataX_ = FloatArray(lineNumberX)
        var logGridDataCounterX = 0
        val left = left
        val right = right
        for (i in minLogarithm_ until maxLogarithm_) {
            for (j in 1..9) {
                val x = Math.log10(Math.pow(10.0, i.toDouble()) * j).toFloat() * logBlockWidth_ - logOffsetX_
                if (x >= left && x <= right) {
                    logGridDataX_[logGridDataCounterX] = x
                    logGridDataCounterX++
                }
            }
        }
        // 横
        val lineNumberY = Math.ceil((-DISPLAY_MINIMUM_DB / 10).toDouble()).toInt()
        logGridDataY_ = FloatArray(lineNumberY)
        val top = top
        for (i in 0 until lineNumberY) {
            logGridDataY_[i] = top + (height / -DISPLAY_MINIMUM_DB * 10) * i
        }
        // 各々のバンドの座標を計算
        bandRegionMinX_ = (Math.log10(BAND_MINIMUM_HZ.toDouble()) * logBlockWidth_ - logOffsetX_).toInt()
        bandRegionMaxX_ = (Math.log10(BAND_MAXIMUM_HZ.toDouble()) * logBlockWidth_ - logOffsetX_).toInt()
        bandWidth_ = (bandRegionMaxX_ - bandRegionMinX_) / bandNumber_
        val bottom = bottom
        for (i in 0 until bandNumber_) {
            bandRects_[i]!!.bottom = bottom.toFloat()
            bandRects_[i]!!.top = bottom.toFloat() // バーが表示されないように
            bandRects_[i]!!.left = bandRegionMinX_ + bandWidth_ * i + BAND_INNER_OFFSET
            bandRects_[i]!!.right = bandRects_[i]!!.left + bandWidth_ - BAND_INNER_OFFSET
        }

//        // シェーダーを設定
//        val color0 = resources.getColor(FFT_DATA_SHADER_START_COLOR_ID)
//        val color1 = resources.getColor(FFT_DATA_SHADER_END_COLOR_ID)
//        fftDataShader_ = LinearGradient(0f, bottom.toFloat(), 0f, top.toFloat(), color0, color1, Shader.TileMode.CLAMP)
//        fftDataPaint_!!.shader = fftDataShader_
    }

    // サンプリングレート
    var samplingRate: Int
        get() = samplingRate_
        set(samplingRateInMilliHz) {
            samplingRate_ = samplingRateInMilliHz / 1000
        }

    // 更新
    fun update(doubels: DoubleArray?) {
        Log.i(tagMsg, "==>update実行")
        fftData_ = doubels
        invalidate()
//                                    Log.i(tagMsg,"FFTdata fft後: ${fftData_?.toList()}")
        Log.i(tagMsg, "invalidate実行")
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        Log.i(tagMsg,"onDraw実行")
        // Viewのサイズ変更があった場合，再計算
        if (currentWidth_ != width || currentHeight_ != height) {
            calculateViewSizeDependedData()
        }
        // グリッド描画
        drawLogGrid(canvas)
        // 波形データがない場合には処理を行わない
        if (fftData_ == null) {
            return
        }
        // FFTデータの描画
        drawFft(canvas)
    }

    // FFTの内容を描画
    private fun drawFft(canvas: Canvas?) {
        Log.i(tagMsg,"drawFft実行")
        // Viewのサイズ情報
        val top = top
        val height = height
        // データの個数
        val fftNum = fftData_!!.size / 2
        // データをバンドに加工して表示
        if (isBandEnabled_) {
            // バンド表示
            // データの初期化
            for (i in 0 until bandNumber_) {
                bandFftData_[i] = 0F
            }
            // データを順に見ていく
            for (i in 1 until fftNum) {
                // 注目しているデータの周波数
                val frequency = (i * samplingRate_ / 2).toFloat() / fftNum
                // 表示位置から対応するバンドのインデックスを計算
                val x = (Math.log10(frequency.toDouble()) * logBlockWidth_).toFloat() - logOffsetX_
                val index = (x - bandRegionMinX_).toInt() / bandWidth_
                if (index in 0 until bandNumber_) {
                    // 振幅スペクトルを計算
                    val amplitude = Math.sqrt(Math.pow(fftData_!![i * 2] , 2.0) + Math.pow(fftData_!![i * 2 + 1] , 2.0)).toFloat()
                    if (amplitude > 0) {
                        // 対応する区間で一番大きい値を取っておく
                        if (bandFftData_[index] < amplitude) {
                            bandFftData_[index] = amplitude
                        }
                    }
                }
            }
            // バーの高さを計算して描画
            for (i in 0 until bandNumber_) {
                Log.i(tagMsg,"bandFftData: ${bandFftData_[i]}")
                val db = (20.0f * Math.log10((bandFftData_[i] / FFT_PEAK_VALUE).toDouble())).toFloat()
                bandRects_[i]!!.top = (top - db / -DISPLAY_MINIMUM_DB * height)
                canvas?.drawRect(bandRects_[i]!!, fftDataPaint_!!)
            }
            // データをそのまま線分で表示
        } else {
            val bottom = bottom
            val right = right
            val left = left
            var oldX = 0f
            var oldY = 0f
            // 直流成分(0番目)は計算しない
            for (i in 1 until fftNum) {
                // 注目しているデータの周波数
                val frequency = (i * samplingRate_ / 2).toFloat() / fftNum
                Log.i(tagMsg,"frequency: ${frequency}")
//                Log.i(tagMsg,"fftNum: ${fftNum}")
                // 振幅スペクトルからデシベル数を計算
                val amplitude = Math.sqrt(Math.pow(fftData_!![i * 2] , 2.0) + Math.pow(fftData_!![i * 2 + 1] , 2.0)).toFloat()
                Log.i(tagMsg,"amplitude: ${amplitude}")
//                val db = (20.0f * Math.log10((amplitude / FFT_PEAK_VALUE).toDouble())).toFloat()
                val db = (20.0f * Math.log10((amplitude).toDouble())).toFloat()
//                Log.i(tagMsg,"FFT_PEAK_VALUE: ${FFT_PEAK_VALUE}")
                // 描画
                val x = (Math.log10(frequency.toDouble()) * logBlockWidth_).toFloat() - logOffsetX_
                if (x >= left && x <= right) {
//                    canvas?.drawLine(x, bottom.toFloat(), x, (top - db / -DISPLAY_MINIMUM_DB * height).toFloat(), fftDataPaint_!!)
                    canvas?.drawLine(oldX, oldY, x + 1, (bottom - (db / 120) * height).toFloat(), fftDataPaint_!!)
                    oldX = x; oldY = (bottom - (db / -DISPLAY_MINIMUM_DB) * height).toFloat()
                    Log.i(tagMsg,"db: ${db}")
//                    Log.i(tagMsg,"bottom.toFloat(): ${bottom.toFloat()}")
//                    Log.i(tagMsg,"top: ${top}")
//                    Log.i(tagMsg,"topからマイナスするもの: ${(top - db / -DISPLAY_MINIMUM_DB * height)}")
//                    Log.i(tagMsg,"height: ${height}")
                }
            }
        }
    }

    // グリッド描画
    private fun drawLogGrid(canvas: Canvas?) {
        // 横方向
        val bottom = bottom
        val top = top
        for (x in logGridDataX_) {
            canvas?.drawLine(x, bottom.toFloat(), x, top.toFloat(), logGridPaint_!!)
        }
        // 縦方向
        val width = width
        for (y in logGridDataY_) {
            canvas?.drawLine(0f, y, width.toFloat(), y, logGridPaint_!!)
        }
    }

    companion object {
        // ----------------------------
        // 定数
        // ----------------------------
        // ピーク値
        private val FFT_PEAK_VALUE = (128 * Math.sqrt(2.0)).toFloat()

        // 表示デシベル数の下限
        private const val DISPLAY_MINIMUM_DB = -30f

        // 表示する最小周波数
        private const val DISPLAY_MINIMUM_HZ = 1000f

        // 表示する最大周波数
        private const val DISPLAY_MAXIMUM_HZ = 18000f

        // バンド表示の最小周波数
        private const val BAND_MINIMUM_HZ = 40f

        // バンド表示の最大周波数
        private const val BAND_MAXIMUM_HZ = 28000f

        // バンドのデフォルト数
        private const val BAND_DEFAULT_NUMBER = 16

        // バンドの内側の表示オフセット
        private const val BAND_INNER_OFFSET = 4f

        // FFTデータの描画色ID
        private const val FFT_DATA_SHADER_START_COLOR_ID = R.color.holo_blue_dark
        private const val FFT_DATA_SHADER_END_COLOR_ID = R.color.white

        // 対数グリッドの色ID
        private const val LOG_GRID_COLOR_ID = R.color.secondary_text_light
    }
}