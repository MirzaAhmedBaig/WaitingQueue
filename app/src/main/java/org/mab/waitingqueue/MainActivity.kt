package org.mab.waitingqueue

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import java.util.*

class MainActivity : AppCompatActivity(), WaitingQueueListener {
    override fun onSendDataRequest(data: QueueData<Int>) {
        Log.d(TAG,"Data List size : ${dataList.size}")
        if (dataList.size >= max) {
            waitingQueue.onACKReceived(WaitingQueue.FAILED)
        } else {
            dataList.add(data)
            waitingQueue.onACKReceived(WaitingQueue.SUCCESS)
        }

    }

    private val TAG = MainActivity::class.java.simpleName
    private val dataList: Queue<QueueData<Int>> = LinkedList()
    private val max = 5

    private val waitingQueue = WaitingQueue()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handler.post(generateDataRunnable)
        printingHandler.postDelayed(printingRunnable, 200)
        waitingQueue.subscribeListener(this@MainActivity)
    }


    private val printingHandler = Handler()
    private val printingRunnable = object : Runnable {
        override fun run() {
            if (dataList.size != 0) {
                Log.d(TAG, "Data sent Successfully : ${dataList.remove().getData()}")
            }
            printingHandler.postDelayed(this, 5000)
        }

    }


    private val handler = Handler()
    private var data: QueueData<Int> = Data()
    private var counter = 0
    private val generateDataRunnable = object : Runnable {
        override fun run() {
            data.setData(counter)
            waitingQueue.submitData(data)
            counter++
            handler.postDelayed(this, 3000)
        }

    }
}
