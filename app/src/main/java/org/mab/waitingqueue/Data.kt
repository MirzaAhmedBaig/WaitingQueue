package org.mab.waitingqueue

import org.mab.waitingqueue.waitingQueue.QueueData


/**
 * Created by Mirza Ahmed Baig on 17/11/18.
 * Avantari Technologies
 * mirza@avantari.org
 */
class Data : QueueData<Int> {
    private var data = 0
    override fun setData(data: Int) {
        this.data = data
    }

    override fun getData(): Int {
        return data
    }
}

