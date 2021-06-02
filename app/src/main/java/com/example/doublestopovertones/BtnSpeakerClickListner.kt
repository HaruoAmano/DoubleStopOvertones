package com.example.doublestopovertones

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.media.AudioTrack.WRITE_NON_BLOCKING
import android.util.Log
import android.view.View

class BtnSpeakerClickListner : View.OnClickListener {
    var audioTrack: AudioTrack? = null
    var buffer: ShortArray? = null

    override fun onClick(v: View) {
        Log.i("speaker","Click")
        if (audioTrack == null){
            Log.i("speaker","prepareAudiotrack")
            prepareAudiotrack()
        }
        if (audioTrack!!.playState != AudioTrack.PLAYSTATE_PLAYING) {
            Log.i("speaker","playAudiotrack")
            playAudiotrack()
        }else {
            stopAudiotrack ()
        }
    }
    fun playAudiotrack(){
        audioTrack!!.reloadStaticData()
        audioTrack!!.setLoopPoints(0,SAMPLERATE*10,2)
        audioTrack!!.write(buffer!!, 0, buffer!!.count(), WRITE_NON_BLOCKING)
        audioTrack!!.play()
    }
    fun stopAudiotrack (){
        if (buffer != null) {
            buffer = null
        }
        if (audioTrack != null) {
            audioTrack!!.stop()
            audioTrack!!.release() // release buffer
            audioTrack = null
        }
    }
    fun prepareAudiotrack() {
        generateBuffer()
        val bufferSizeInBytes = SAMPLERATE * duration.toInt() * 2
        audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                        AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
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
    fun generateBuffer() {
        val SAMPLES = (duration * SAMPLERATE).toInt()
        buffer = ShortArray(SAMPLES * CHANNEL)
        var signal = 0.0
        for (i in 0 until SAMPLES) {
            signal = generateSignal(i)
            buffer!![i] = (signal * Short.MAX_VALUE).toShort()
        }
    }
    fun calcAmplification():Double{
        //高音ほど大きな音で聞こえるため聴感上均一にするため周波数の逆数を乗じる。
        //今は勘で設定している。
        var amplification = 1 / Math.pow(thirdNoteFreq[0].toDouble(),1.2) * amplificationParm
        return amplification
    }
    fun generateSignal(sample: Int): Double {
        val t = sample.toDouble() / SAMPLERATE
        return calcAmplification() * Math.sin(2.0 * Math.PI * thirdNoteFreq[0].toDouble() * t)
    }

    companion object {
        private const val TAG = "AudioActivity"
        protected const val CHANNEL = 1 // 1:MONO, 2:STEREO
        protected const val BITRATE = 16 // [bit/sec]
        protected const val SAMPLERATE = 44100

        // signal funcion params
        private const val amplificationParm = 500
        private const val duration = 10.0 // [sec]
    }
}