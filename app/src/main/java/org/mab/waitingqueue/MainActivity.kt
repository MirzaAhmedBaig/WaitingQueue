package org.mab.waitingqueue

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import org.mab.waitingqueue.waitingQueue.QueueData
import org.mab.waitingqueue.waitingQueue.WaitingQueue
import org.mab.waitingqueue.waitingQueue.WaitingQueueListener
import java.util.*

class MainActivity : AppCompatActivity(), WaitingQueueListener {
    override fun onSendDataRequest(data: QueueData) {
        waitingQueue.broadCastIntent = if (dataBuffer.size == max) {
            Intent(WaitingQueue.ACK_BROADCAST).apply {
                putExtra(WaitingQueue.STATUS_CODE, WaitingQueue.FAILED)
            }
        } else {
            dataBuffer.add(data)
            Intent(WaitingQueue.ACK_BROADCAST).apply {
                putExtra(WaitingQueue.STATUS_CODE, WaitingQueue.SUCCESS)
            }
        }
        Log.d(TAG, "Broadcast sent")
    }

    private val TAG = MainActivity::class.java.simpleName
    private val dataBuffer: Queue<QueueData> = LinkedList()
    private val max = 5

    private val waitingQueue by lazy {
        WaitingQueue(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        waitingQueue.subscribeListener(this@MainActivity)
        printingHandler.postDelayed(printingRunnable, 200)
        submit.setOnClickListener {
            waitingQueue.submitData(Data(counter))
            counter++
        }
    }

    override fun onResume() {
        super.onResume()
        waitingQueue.registerACKBroadcast()
    }

    override fun onPause() {
        super.onPause()
        waitingQueue.unregisterACKBroadcast()
    }

    private var counter = 0
    private val printingHandler = Handler()
    private val printingRunnable = object : Runnable {
        override fun run() {
            if (dataBuffer.size != 0) {
                val data: Data = dataBuffer.poll() as Data
                Log.d(TAG, "Data sent Successfully : ${data.data}")
            }
            printingHandler.postDelayed(this, 1000)
        }

    }


}
