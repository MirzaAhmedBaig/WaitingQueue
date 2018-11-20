package org.mab.waitingqueue.waitingQueue

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import java.lang.reflect.Type
import java.util.*
import kotlin.properties.Delegates


/**
 * Created by Mirza Ahmed Baig on 17/11/18.
 *
 * Avantari Technologies
 * mirza@avantari.org
 */
class WaitingQueue(val baseContext: Context) {
    private val TAG = WaitingQueue::class.java.simpleName


    companion object {
        val SUCCESS = 1
        val FAILED = 0
        var STATUS_CODE = "statuscode"
        var ACK_BROADCAST = "org.mab.waitingqueue.waitingQueue.ack_broadcast"
    }

    private var currentObject: QueueData? = null
    private val dataList: Queue<QueueData> = LinkedList()
    private var waitingQueueListener: WaitingQueueListener? = null

    fun submitData(data: QueueData) {
        dataList.add(data)
        if (dataList.size == 1) {
            sendData()
        }
    }

    private fun sendData() {
        if (dataList.isNotEmpty()) {
            currentObject = dataList.element()
            waitingQueueListener?.onSendDataRequest(currentObject!!)
        }
    }

    fun registerACKBroadcast() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(ACK_BROADCAST)
        LocalBroadcastManager.getInstance(baseContext).registerReceiver(ackBroadcastReceiver, intentFilter)
    }

    fun unregisterACKBroadcast() {
        LocalBroadcastManager.getInstance(baseContext).unregisterReceiver(ackBroadcastReceiver)
    }

    fun subscribeListener(waitingQueueListener: WaitingQueueListener) {
        this.waitingQueueListener = waitingQueueListener
    }

    var broadCastIntent: Intent? by Delegates.observable<Intent?>(null) { _, _, newValue ->
        newValue?.let {
            LocalBroadcastManager.getInstance(baseContext).sendBroadcast(newValue)
        }
    }

    private val ackBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        val TAG = BroadcastReceiver::class.java.simpleName
        override fun onReceive(context: Context, intent: Intent) {
            val statusCode = intent.getIntExtra(STATUS_CODE, FAILED)
            Log.d(TAG, "OnReceive :$statusCode")
            when (statusCode) {
                SUCCESS -> {
                    dataList.poll()
                    sendData()
                }
                FAILED -> {
                    Handler().postDelayed({
                        waitingQueueListener?.onSendDataRequest(currentObject!!)
                    }, 1000)
                }
            }
        }
    }

}