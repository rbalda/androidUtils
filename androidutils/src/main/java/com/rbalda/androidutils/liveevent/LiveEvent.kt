package com.rbalda.androidutils.liveevent

import androidx.annotation.MainThread
import androidx.collection.ArraySet
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

class LiveEvent<Event>: MediatorLiveData<Event>(){
    private val observers = ArraySet<ObserverWrapper<in Event>>()

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in Event>) {
        val wrapper = ObserverWrapper(observer)
        observers.add(wrapper)
        super.observe(owner, wrapper)
    }

    @MainThread
    override fun removeObserver(observer: Observer<in Event>) {
        if (observers.remove(observer as Observer<*>)) {
            super.removeObserver(observer)
            return
        }
        val iterator = observers.iterator()
        while (iterator.hasNext()) {
            val wrapper = iterator.next()
            if (wrapper.observer == observer) {
                iterator.remove()
                super.removeObserver(wrapper)
                break
            }
        }
    }

    @MainThread
    override fun setValue(t: Event?) {
        observers.forEach { it.newValue() }
        super.setValue(t)
    }

    private class ObserverWrapper<T>(val observer: Observer<T>) : Observer<T> {

        private var pending = false

        override fun onChanged(t: T?) {
            if (pending) {
                pending = false
                observer.onChanged(t)
            }
        }

        fun newValue() {
            pending = true
        }
    }

    interface Event{
        fun value():String?
    }

    open class SuccessEvent(var value: String?): Event{
        override fun value(): String? {
             return value
        }
    }

    open class FailureEvent(var value: String?): Event{
        override fun value(): String? {
            return value
        }
    }

    open class LoadingEvent(var value: String?): Event{
        override fun value(): String? {
            return value
        }
    }

}