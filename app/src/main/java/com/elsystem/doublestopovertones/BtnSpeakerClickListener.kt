package com.elsystem.doublestopovertones

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.media.AudioTrack.WRITE_NON_BLOCKING
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlin.math.pow
import kotlin.math.sin

open class BtnSpeakerClickListener(view : View, viewGroup : ViewGroup) : View.OnClickListener {
    private var buffer: ShortArray? = null
    private var btnSpeaker1 = viewGroup.findViewById<Button>(R.id.btnSpeaker1)
    private var btnSpeaker2 = viewGroup.findViewById<Button>(R.id.btnSpeaker2)
    private var btnSpeaker3 = viewGroup.findViewById<Button>(R.id.btnSpeaker3)
    private var btnSpeaker4 = viewGroup.findViewById<Button>(R.id.btnSpeaker4)
    private var btnSpeaker5 = viewGroup.findViewById<Button>(R.id.btnSpeaker5)
    private var frec = 0f

    override fun onClick(view: View) {
        when (view){
            btnSpeaker1 -> frec = thirdNoteFreq[0]
            btnSpeaker2 -> frec = thirdNoteFreq[1]
            btnSpeaker3 -> frec = thirdNoteFreq[2]
            btnSpeaker4 -> frec = thirdNoteFreq[3]
            btnSpeaker5 -> frec = thirdNoteFreq[4]
        }
        if (audioTrack == null){
            prepareAudiotrack(frec)
        }
        if (audioTrack!!.playState != AudioTrack.PLAYSTATE_PLAYING) {
            playAudiotrack()
        }else {
            stopAudiotrack ()
        }
    }
    private fun playAudiotrack(){
        audioTrack!!.reloadStaticData()
        audioTrack!!.setLoopPoints(0,SAMPLERATE*10,5)
        audioTrack!!.write(buffer!!, 0, buffer!!.count(), WRITE_NON_BLOCKING)
        audioTrack!!.play()
    }
    private fun stopAudiotrack (){
        if (buffer != null) {
            buffer = null
        }
        if (audioTrack != null) {
            audioTrack!!.stop()
            audioTrack!!.release() // release buffer
            audioTrack = null
        }
    }
    private fun prepareAudiotrack(freq : Float) {
        generateBuffer(freq)
        val bufferSizeInBytes = SAMPLERATE * duration.toInt() * 2
        audioTrack = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_UNKNOWN)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(SAMPLERATE)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(bufferSizeInBytes)
            .setTransferMode(AudioTrack.MODE_STATIC)
            .build()
        //	write buffer
//        audioTrack!!.write(buffer!!, 0, buffer!!.size)
    }
    private fun generateBuffer(freq: Float) {
        val samples = (duration * SAMPLERATE).toInt()
        buffer = ShortArray(samples * CHANNEL)
        var signal: Double
        for (i in 0 until samples) {
            signal = generateSignal(i,freq)
            buffer!![i] = (signal * Short.MAX_VALUE).toInt().toShort()
        }
    }
    private fun calcAmplification(freq: Float): Double {
        //高音ほど大きな音で聞こえるため聴感上均一にするため周波数の逆数を乗じる。
        //今は勘で設定している。
        return 1 / freq.toDouble().pow(1.2) * amplificationParam
    }
    private fun generateSignal(sample: Int,freq: Float): Double {
        val t = sample.toDouble() / SAMPLERATE
        return calcAmplification(freq) * sin(2.0 * Math.PI * freq.toDouble() * t)
    }

    companion object {
        private const val TAG = "AudioActivity"
        private const val CHANNEL = 1 // 1:MONO, 2:STEREO
        private const val BITRATE = 16 // [bit/sec]
        private const val SAMPLERATE = 44100

        // signal function params
        private const val amplificationParam = 500
        private const val duration = 10.0 // [sec]
    }
}