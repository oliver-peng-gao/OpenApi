package com.olivergao.openapi.ui

interface DataStateChangedListener {

    fun onDataStateChanged(dataState: DataState<*>?)

    fun expandAppBar()
}
