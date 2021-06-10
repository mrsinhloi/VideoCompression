package com.myxteam.videocompression

import android.content.Context
import android.os.Environment
import android.view.View
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.otaliastudios.transcoder.Transcoder
import com.otaliastudios.transcoder.TranscoderListener
import com.otaliastudios.transcoder.common.TrackType
import com.otaliastudios.transcoder.strategy.DefaultAudioStrategy
import com.otaliastudios.transcoder.strategy.DefaultVideoStrategy
import com.otaliastudios.transcoder.strategy.RemoveTrackStrategy
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class VideoCompression(
    var context: Context,
    var videoPath: String,
    var listener: CompressionListener,
    var progressIndicator: LinearProgressIndicator? = null,
    var framerate: Int = 24,
    var height: Int = 960,
    var width: Int = 540,
    var isMute: Boolean = false
) {
    fun showProgress() {
        progressIndicator?.run {
            progress = 0
            max = 100
            visibility = View.VISIBLE
        }
    }

    fun hideProgress() {
        progressIndicator?.run {
            progress = 0
            visibility = View.INVISIBLE
        }
    }

    lateinit var videoStrategy: DefaultVideoStrategy
    lateinit var audioStrategy: DefaultAudioStrategy

    init {
        showProgress()

        videoStrategy = DefaultVideoStrategy.exact(height, width)
            .frameRate(framerate)
            .build()

        audioStrategy = DefaultAudioStrategy.builder()
            .channels(DefaultAudioStrategy.CHANNELS_AS_INPUT)
            .sampleRate(DefaultAudioStrategy.SAMPLE_RATE_AS_INPUT)
            .build()

    }

    fun compress() {
        var compressed = createVideoFile(context)
        Transcoder.into(compressed.absolutePath)
            .addDataSource(TrackType.VIDEO, videoPath)
            .setVideoTrackStrategy(videoStrategy)
            .setAudioTrackStrategy(if (isMute) audioStrategy else RemoveTrackStrategy())
            .setListener(object : TranscoderListener {
                override fun onTranscodeProgress(progress: Double) {
                    val number = (progress * 100).toInt()
                    progressIndicator?.progress = number
                }

                override fun onTranscodeCompleted(successCode: Int) {
//                    val howlong = (SystemClock.elapsedRealtime() - start)/1000
//                    printFileSize(compressed.absolutePath, howlong)
//                    ProgressUtils.hide()
                    //return compressed.absolutePath
                    listener.onSuccess(compressed.absolutePath)
                }

                override fun onTranscodeCanceled() {
                    hideProgress()
                    listener.onCancel()
                }

                override fun onTranscodeFailed(exception: Throwable) {
                    hideProgress()
                    listener.onFailed(exception)
                }

            }).transcode()

    }

    fun createVideoFile(context: Context): File {
        // Create an video file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "VIDEO_COMPRESSED_$timeStamp.mp4"
        val getImage = context.externalCacheDir
        return if (getImage != null) {
            File(getImage.path, imageFileName)
        } else {
            val storageDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            File(storageDir.path, imageFileName)
        }
    }
}