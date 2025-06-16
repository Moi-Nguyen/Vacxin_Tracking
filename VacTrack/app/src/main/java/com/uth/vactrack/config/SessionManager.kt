package com.uth.vactrack.config

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.FirebaseAuth
import com.uth.vactrack.ui.UILogin.LoginActivity

class SessionManager(private val context: Context) {
    private val SESSION_TIMEOUT = 30 * 60 * 1000 // 30 minutes in milliseconds
    private val handler = Handler(Looper.getMainLooper())
    private var sessionTimeoutRunnable: Runnable? = null

    fun startSession() {
        // Cancel any existing timeout
        stopSession()
        
        // Create new timeout
        sessionTimeoutRunnable = Runnable {
            // Sign out from Firebase
            FirebaseAuth.getInstance().signOut()
            
            // Navigate to login screen
            val intent = Intent(context, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            context.startActivity(intent)
        }

        // Schedule the timeout
        handler.postDelayed(sessionTimeoutRunnable!!, SESSION_TIMEOUT.toLong())
    }

    fun stopSession() {
        sessionTimeoutRunnable?.let {
            handler.removeCallbacks(it)
            sessionTimeoutRunnable = null
        }
    }

    fun resetSession() {
        stopSession()
        startSession()
    }
} 