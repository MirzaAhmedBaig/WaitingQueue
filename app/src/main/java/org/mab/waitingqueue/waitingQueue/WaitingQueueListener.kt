package org.mab.waitingqueue


/**
 * Created by Mirza Ahmed Baig on 17/11/18.
 * Avantari Technologies
 * mirza@avantari.org
 */
interface WaitingQueueListener {
    fun onSendDataRequest(data: QueueData<Int>)
}