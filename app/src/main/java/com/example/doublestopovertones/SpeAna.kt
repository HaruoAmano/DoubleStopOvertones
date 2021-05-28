package com.example.doublestopovertones

//import org.jtransforms.fft.DoubleFFT_1D
import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.app.ActivityCompat
import org.jtransforms.fft.*

//import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;
class SpeAna : Activity() {

    val tagMsg: String = "MyInfo_SpeAna"

    //画面オブジェクトの初期化
    var _button: Button? = null
    var _fftView : FftView? = null
    //AudioRecord用変数の初期設定
    var audioRec: AudioRecord? = null
    var _isRecording = false
    var bufSize = 0
    val SAMPLING_RATE = 44100
    //FFT用変数の初期設定
    val FFT_SIZE = 16384 //4096
    //非同期処理用判定フラグ
    var _recordTask: SpeAna.RecordTask? = null

    //onCreate処理
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spe_ana)
        Log.i(tagMsg,"@@@Start Activity@@@")

        //マイクアクセスに対するPermission。未許可の場合は端末に許可を求めるポップアップが表示される。
        if(ActivityCompat.checkSelfPermission
                (this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            //WRITE_EXTERNAL_STRAGEの許可を求めるダイアログを表示。
            val permissions = arrayOf(Manifest.permission.RECORD_AUDIO)
            ActivityCompat.requestPermissions(this, permissions, 2000)
        }

        //画面部品FftViewの取得、および描画機能の実装。
        _fftView = findViewById<FftView>(R.id.fftView)
        _fftView!!.setWillNotDraw(false)

        //AudioRecordの書き込みに使用する配列のサイズを設定。
        //  AudioRecordに必要なサイズとFFTバッファーサイズを比較して、大きいほうが採用される。
        bufSize = AudioRecord.getMinBufferSize(
            FFT_SIZE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        if (FFT_SIZE > bufSize) bufSize = FFT_SIZE

        //画面部品Buttonの取得、およびリスナの設定。
        _button = findViewById(R.id.buttonStart) as Button
        _button?.setOnClickListener {
            if (_isRecording) {
                stopRecord()
                Log.i(tagMsg, "Stopボタン押下")
            }
            else {
                Log.i(tagMsg, "Startボタン押下")
                doRecord()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i(tagMsg, "onStart!!")
    }

    override fun onResume() {
        super.onResume()
        Log.i(tagMsg, "onResume!!")
    }
    //録音停止処理。
    private fun stopRecord() {
        _isRecording = false
        _button?.text = "start"
        _recordTask?.cancel(true)
        Log.i(tagMsg, "Tascをキャンセルしました。")
    }
    //録音開始処理。
    private fun doRecord() {
        _isRecording = true
        _button?.text = "stop"

        // AsyncTaskの生成。
        _recordTask = RecordTask()
        _recordTask?.execute()
    }

    //録音～描画までの処理は非同期処理とする。
    inner class RecordTask : AsyncTask<Void, DoubleArray, Void>() {
        override fun doInBackground(vararg params: Void): Void? {
            //AudioRecordの作成
            audioRec = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                SAMPLING_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufSize * 2
            )
            //録音用バッファの生成。
            val buf = ShortArray(bufSize)
            //録音開始
            audioRec!!.startRecording()
            _isRecording = true
            try {
                while (_isRecording) {
                    audioRec!!.read(buf, 0, buf.size)
                    Log.i(tagMsg, buf.size.toString())
//                          Log.i(tagMsg,"buf（AudioRecordの取得した値: ${buf.toList()}")
                    val fft = DoubleFFT_1D(FFT_SIZE.toLong())
                    val FFTdata = DoubleArray(FFT_SIZE)
                    for (i in 0 until FFT_SIZE) {
                        FFTdata[i] = buf[i].toDouble()
                    }
                    fft.realForward(FFTdata)   //fftをやっているみたい。
//                          Log.i(tagMsg,"FFTdata fft後: ${FFTdata.toList()}")
                    44100000.also { _fftView?.samplingRate = it }
                    _fftView?.update(FFTdata)
                    Log.i(tagMsg, "upDate実行")
                }
                // 録音停止
            } finally{
                Log.i(tagMsg, "finally通過")
                audioRec!!.stop()
                audioRec!!.release()
            }
            return null
        }
    }
}