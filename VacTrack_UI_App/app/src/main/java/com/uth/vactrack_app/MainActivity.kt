package com.uth.vactrack_app

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.uth.vactrack_app.ui.auth.LoginActivity
import com.uth.vactrack_app.utils.AppColors
import com.uth.vactrack_app.utils.AppDimensions
import com.uth.vactrack_app.utils.SharedPrefsManager

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPrefsManager: SharedPrefsManager
    private lateinit var rootLayout: ConstraintLayout
    private lateinit var welcomeTextView: TextView
    private lateinit var userInfoTextView: TextView
    private lateinit var logoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPrefsManager = SharedPrefsManager(this)

        // Kiểm tra đã đăng nhập chưa
        if (!sharedPrefsManager.isLoggedIn()) {
            navigateToLogin()
            return
        }

        createUI()
        loadUserInfo()
        setupClickListeners()
    }

    private fun createUI() {
        // Root layout
        rootLayout = ConstraintLayout(this).apply {
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(AppColors.WHITE)
            setPadding(
                AppDimensions.PADDING_LARGE,
                AppDimensions.PADDING_LARGE,
                AppDimensions.PADDING_LARGE,
                AppDimensions.PADDING_LARGE
            )
        }

        // Welcome text
        welcomeTextView = TextView(this).apply {
            id = View.generateViewId()
            text = "Welcome to VacTrack!"
            textSize = 24f
            setTextColor(AppColors.PRIMARY_BLUE)
            typeface = Typeface.DEFAULT_BOLD
            gravity = Gravity.CENTER
        }

        // User info text
        userInfoTextView = TextView(this).apply {
            id = View.generateViewId()
            text = "Loading user info..."
            textSize = 16f
            setTextColor(AppColors.DARK_GRAY)
            gravity = Gravity.CENTER
        }

        // Logout button
        logoutButton = Button(this).apply {
            id = View.generateViewId()
            text = "Logout"
            setBackgroundColor(AppColors.ERROR_RED)
            setTextColor(AppColors.WHITE)
            textSize = 16f
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                AppDimensions.BUTTON_HEIGHT * 2
            )
        }

        rootLayout.addView(welcomeTextView)
        rootLayout.addView(userInfoTextView)
        rootLayout.addView(logoutButton)

        setContentView(rootLayout)
        setupConstraints()
    }

    private fun setupConstraints() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(rootLayout)

        // Welcome text constraints (center vertically, top part)
        constraintSet.connect(welcomeTextView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        constraintSet.connect(welcomeTextView.id, ConstraintSet.BOTTOM, userInfoTextView.id, ConstraintSet.TOP)
        constraintSet.connect(welcomeTextView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        constraintSet.connect(welcomeTextView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)

        // User info text constraints (center)
        constraintSet.connect(userInfoTextView.id, ConstraintSet.TOP, welcomeTextView.id, ConstraintSet.BOTTOM, 24)
        constraintSet.connect(userInfoTextView.id, ConstraintSet.BOTTOM, logoutButton.id, ConstraintSet.TOP)
        constraintSet.connect(userInfoTextView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        constraintSet.connect(userInfoTextView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)

        // Logout button constraints (bottom)
        constraintSet.connect(logoutButton.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 100)
        constraintSet.connect(logoutButton.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        constraintSet.connect(logoutButton.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)

        constraintSet.applyTo(rootLayout)
    }

    private fun loadUserInfo() {
        val user = sharedPrefsManager.getUser()
        if (user != null) {
            userInfoTextView.text = """
                Name: ${user.name}
                Email: ${user.email}
                Role: ${user.role}
                ID: ${user.id}
            """.trimIndent()
        } else {
            userInfoTextView.text = "No user information available"
        }
    }

    private fun setupClickListeners() {
        logoutButton.setOnClickListener {
            // Clear user data
            sharedPrefsManager.logout()

            // Navigate back to login
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onResume() {
        super.onResume()
        // Kiểm tra lại login status khi quay lại app
        if (!sharedPrefsManager.isLoggedIn()) {
            navigateToLogin()
        }
    }
}
