package com.casual.tcpclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.net.Socket
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        send.setOnClickListener {
            thread {
                try {
                    val socket = Socket("192.168.1.103", 2000)
                    val outputStream = socket.getOutputStream()
                    outputStream.write(input.text.toString().toByteArray());
                    socket.close()
                } catch (e: Exception) {
                    Log.d("Exception",e.message)
                }
            }
        }
    }
}
