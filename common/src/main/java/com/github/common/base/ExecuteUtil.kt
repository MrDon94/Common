package com.github.common.base

import com.blankj.utilcode.util.NetworkUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

/**
 * @author: cd
 * @Description:  任务执行工具类
 * @date: 2020/12/24
 */
class ExecuteUtil private constructor() {

    val statusMap: ConcurrentHashMap<String, Int> = ConcurrentHashMap()

    private var scope:CoroutineScope = CoroutineScope(Dispatchers.IO)

    companion object{

        private val EXECUTE_UTIL: ExecuteUtil = ExecuteUtil()

        @JvmStatic
        fun get() = EXECUTE_UTIL
    }

    fun execute(task: BaseTask){
        //防止上传重复任务
        if (statusMap.containsKey(task.getTag())){
            return
        }
        //无网络连接直接返回
        if (!NetworkUtils.isConnected()){
            return
        }
        scope.launch {
            task.start()
        }
    }

    fun shutdown(){
        scope.cancel()
        statusMap.clear()
        //协程作用域取消后就失效了，所以需要重新创建协程作用域
        scope = CoroutineScope(Dispatchers.IO)
    }
}