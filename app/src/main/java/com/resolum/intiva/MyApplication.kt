package com.resolum.intiva

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * MyApplication is the base class for maintaining global application state. It is annotated with @HiltAndroidApp to enable Hilt's dependency injection throughout the app.
 */
@HiltAndroidApp
class MyApplication: Application()