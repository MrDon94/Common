package com.github.common.event

import com.jeremyliao.liveeventbus.LiveEventBus
import com.jeremyliao.liveeventbus.core.LiveEvent

/**
 * @author: cd
 * @Description:  事件基类livedata版
 * @date: 2020/12/24
 */
open class BaseLiveEvent: LiveEvent {
    fun post(){
        LiveEventBus.get<BaseLiveEvent>(this::class.java.simpleName).post(this)
    }
}