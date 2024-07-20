package com.google.mapi.android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Redirecting to FirebaseUIActivity immediately.
        // Add conditions here if needed.
        startActivity(
            Intent(
                this@MainActivity,
                FirebaseUIActivity::class.java
            )
        )
        // Consider setting a view or using setContent if this activity should display content.
    }
}