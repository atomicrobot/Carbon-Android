package com.atomicrobot.carbon.util

import kotlinx.coroutines.delay

object CoroutineUtils {
    suspend fun <T> delayAtLeast(delayMs: Long, networkCall: suspend () -> T): T {
        val startTime = System.currentTimeMillis()
        val response = networkCall.invoke()
        val elapsedTime = System.currentTimeMillis() - startTime
        if (elapsedTime < delayMs) delay(delayMs - elapsedTime)
        return response
    }
}