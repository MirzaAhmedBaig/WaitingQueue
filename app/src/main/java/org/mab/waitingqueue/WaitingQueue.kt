package org.mab.waitingqueue

import java.util.*


/**
 * Created by Mirza Ahmed Baig on 17/11/18.
 * Avantari Technologies
 * mirza@avantari.org
 */
class WaitingQueue : DataACEListener {
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
        if(dataList.isNotEmpty()) {
            currentObject = dataList.remove()
            //broadcast current object and wait till response
            waitingQueueListener?.onSendDataRequest(currentObject!!)
        }
    }


    override fun onACKReceived(statusCode: Int) {
        when (statusCode) {
            SUCCESS -> {
                sendData()
            }
            FAILED -> {
                waitingQueueListener?.onSendDataRequest(currentObject!!)
            }
        }

    }

    fun subscribeListener(waitingQueueListener: WaitingQueueListener) {
        this.waitingQueueListener = waitingQueueListener
    }

}