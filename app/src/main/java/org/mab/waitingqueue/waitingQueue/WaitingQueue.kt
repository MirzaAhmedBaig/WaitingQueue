package org.mab.waitingqueue

import android.os.Handler
import android.util.Log
import java.util.*


/**
 * Created by Mirza Ahmed Baig on 17/11/18.
 *
 * Avantari Technologies
 * mirza@avantari.org
 */
class WaitingQueue : DataACEListener {
    private val TAG = WaitingQueue::class.java.simpleName


    companion object {
        val SUCCESS = 1
        val FAILED = 0
    }

    private var currentObject: QueueData<Int>? = null
    private val dataList: Queue<QueueData<Int>> = LinkedList()
    private var waitingQueueListener: WaitingQueueListener? = null

    fun submitData(data: QueueData<Int>) {
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


    override fun onACKReceived(statusCode: Int) {
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

    fun subscribeListener(waitingQueueListener: WaitingQueueListener) {
        this.waitingQueueListener = waitingQueueListener
    }

}