package org.mab.waitingqueue


/**
 * Created by Mirza Ahmed Baig on 17/11/18.
 * Avantari Technologies
 * mirza@avantari.org
 */
interface DataACEListener {
    fun onACKReceived(statusCode:Int)
}