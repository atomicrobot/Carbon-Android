package com.mycompany.myapp.databinding

import android.databinding.BaseObservable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty

class ReadWriteBinding<T>(
        private val br: Int,
        private val backingPropertyResolver: () -> KMutableProperty0<T>) : ReadWriteProperty<BaseObservable, T> {
    override fun getValue(thisRef: BaseObservable, property: KProperty<*>): T {
        return backingPropertyResolver().get()
    }

    override fun setValue(thisRef: BaseObservable, property: KProperty<*>, value: T) {
        backingPropertyResolver().set(value)
        thisRef.notifyPropertyChanged(br)
    }
}