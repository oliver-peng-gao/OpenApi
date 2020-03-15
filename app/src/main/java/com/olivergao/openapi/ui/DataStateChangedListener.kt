package com.olivergao.openapi.ui

interface DataStateChangedListener {

    fun onDataStateChanged(dataState: DataState<*>?)
}