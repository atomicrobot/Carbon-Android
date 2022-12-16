package com.atomicrobot.carbon.util

import kotlinx.coroutines.delay

object CoroutineUtils {
    suspend fun <T> delayAtLeast(delayMs: Long, networkCall: suspend () -> T): T {
        val endTime = System.currentTimeMillis() + delayMs
        val response = networkCall.invoke()
        if (System.currentTimeMillis() < endTime)
            delay((endTime - System.currentTimeMillis()).coerceAtLeast(0L))
        return response
    }
}
