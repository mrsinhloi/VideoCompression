package com.myxteam.videocompression

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        //User
        VideoCompression(this,"", object : CompressionListener {
            override fun onSuccess(path: String) {
                TODO("Not yet implemented")
            }

            override fun onCancel() {
                TODO("Not yet implemented")
            }

            override fun onFailed(exception: Throwable) {
                TODO("Not yet implemented")
            }

        }).compress()

    }
}