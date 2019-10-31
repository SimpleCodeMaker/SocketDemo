package com.casual.socketdemo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Contacts
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread
import kotlin.coroutines.CoroutineContext
import android.net.wifi.WifiInfo
import android.content.Context.WIFI_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import android.net.wifi.WifiManager


class MainActivity : AppCompatActivity() {
    var job: Deferred<Socket>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        currentIP.text = getLocalIpAddress(this)
        set_port.setOnClickListener {
            job?.cancel()
            job = CoroutineScope(Dispatchers.IO).async {
                start()
            }
        }
    }

    suspend fun start(): Socket {
        val serverSocket = ServerSocket(port.text.toString().toInt())
        while (true) {
            val socket = serverSocket.accept()
//                    val ip = socket.inetAddress.hostAddress
            val inputStream = socket.getInputStream()
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            var line: String? = null
            val stringBuilder = StringBuilder()
            do {
                line = bufferedReader.readLine()
                if (line == null) {
                    break
                }
                stringBuilder.append(line)
            } while (true)
            content_receive.post {
                content_receive.text = stringBuilder.toString()
            }
            socket.close()
        }
    }

    /**
     * 获取当前ip地址
     *
     * @param context
     * @return
     */
    fun getLocalIpAddress(context: Context): String {
        try {

            val wifiManager = context
                .getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wifiInfo = wifiManager.connectionInfo
            val i = wifiInfo.ipAddress
            return int2ip(i)
        } catch (ex: Exception) {
            return " 获取IP出错鸟!!!!请保证是WIFI,或者请重新打开网络!\n" + ex.message
        }

        // return null;
    }

    /**
     * 将ip的整数形式转换成ip形式
     */
    fun int2ip(ipInt: Int): String {
        val sb = StringBuilder()
        sb.append(ipInt and 0xFF).append(".")
        sb.append(ipInt shr 8 and 0xFF).append(".")
        sb.append(ipInt shr 16 and 0xFF).append(".")
        sb.append(ipInt shr 24 and 0xFF)
        return sb.toString()
    }

}
