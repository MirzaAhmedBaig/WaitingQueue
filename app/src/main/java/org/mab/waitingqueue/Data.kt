package org.mab.waitingqueue

import org.mab.waitingqueue.waitingQueue.QueueData
import org.mab.waitingqueue.waitingQueue.TypeConstants


/**
 * Created by Mirza Ahmed Baig on 17/11/18.
 * Avantari Technologies
 * mirza@avantari.org
 */
data class Data(val data: Int) : QueueData {
    override fun getType(): String {
        return TypeConstants.INT_TYPE
    }
}

