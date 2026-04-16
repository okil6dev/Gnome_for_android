package com.okil.gnomeforandroid

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import android.view.accessibility.AccessibilityWindowInfo

class GnomeAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName?.toString()
            if (packageName != null) {
                val intent = Intent("com.okil.gnomeforandroid.APP_STATE_CHANGED")
                intent.putExtra("package_name", packageName)
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            }
        }
    }

    override fun onInterrupt() {}

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_POWER_OFF -> {
                performGlobalAction(GLOBAL_ACTION_POWER_DIALOG)
            }
            ACTION_RESTART -> {
                // GLOBAL_ACTION_RESTART is 15
                if (!performGlobalAction(15)) {
                    // Fallback to power dialog if restart action isn't supported
                    performGlobalAction(GLOBAL_ACTION_POWER_DIALOG)
                }
            }
            ACTION_GO_HOME -> {
                performGlobalAction(GLOBAL_ACTION_HOME)
            }
        }
        return START_STICKY
    }
    
    companion object {
        const val ACTION_POWER_OFF = "com.okil.gnomeforandroid.ACTION_POWER_OFF"
        const val ACTION_RESTART = "com.okil.gnomeforandroid.ACTION_RESTART"
        const val ACTION_GO_HOME = "com.okil.gnomeforandroid.ACTION_GO_HOME"
    }
}
