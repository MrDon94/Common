package com.github.common.base

import com.github.common.event.SyncTaskEvent
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * @author: cd
 * @Description: 基类任务
 * @date: 2020/12/24
 */
abstract class BaseTask {

    companion object {
        const val SYNC_LOADING = 1//同步中
        const val SYNC_COMPLETE = 2//同步完成
        const val SYNC_ERROR = 3//同步失败
    }

    abstract suspend fun doWork():Boolean

    private fun sendEvent(syncStatus:Int){
        when(syncStatus){
            SYNC_LOADING -> ExecuteUtil.get().statusMap[getTag()] = syncStatus
            SYNC_ERROR -> ExecuteUtil.get().statusMap.remove(getTag())
            else -> ExecuteUtil.get().statusMap.remove(getTag())
        }
        SyncTaskEvent(getTag(), syncStatus).post()
    }

    fun getTag(): String = this::class.java.simpleName

    suspend fun start() = coroutineScope {
        launch {
            try {
                sendEvent(SYNC_LOADING)
                if (doWork()){
                    sendEvent(SYNC_COMPLETE)
                }else {
                    sendEvent(SYNC_ERROR)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                sendEvent(SYNC_ERROR)
            }
        }
    }
}