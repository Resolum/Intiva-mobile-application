package com.resolum.intiva.core.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Base ViewModel that provides a centralized coroutine exception handler
 * and a safe launch helper.
 */
abstract class BaseViewModel : ViewModel() {

    /**
     * A global exception handler for coroutines launched in this ViewModel.
     * It calls [handleError] when an uncaught exception occurs.
     */
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        handleError(throwable)
    }

    /**
     * Override to handle uncaught exceptions from [safeLaunch].
     */
    protected open fun handleError(throwable: Throwable) {
        throwable.printStackTrace()
    }

    /**
     * Launches a coroutine in [viewModelScope] with a global exception handler.
     */
    protected fun safeLaunch(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(exceptionHandler, block = block)
    }
}