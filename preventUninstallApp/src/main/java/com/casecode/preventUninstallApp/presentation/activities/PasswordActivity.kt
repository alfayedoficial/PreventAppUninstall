package com.casecode.preventUninstallApp.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.alfayedoficial.preventuninstalladmin.ui.theme.MyDeviceAdminReceiverTheme
import com.casecode.preventUninstallApp.R
import com.casecode.preventUninstallApp.data.ACTION_PASSWORD_RESULT
import com.casecode.preventUninstallApp.data.preference.getInflatedPasswordActivity
import com.casecode.preventUninstallApp.data.preference.saveCounterOne
import com.casecode.preventUninstallApp.data.preference.saveInflatedPasswordActivity
import com.casecode.preventUninstallApp.model.ServiceResult
import com.casecode.preventUninstallApp.presentation.screens.LockPasswordScreen

class PasswordActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyDeviceAdminReceiverTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LockPasswordScreen { result ->
                        Toast.makeText(
                            this,
                            getString(R.string.password_entered_successfully).takeIf { result == ServiceResult.SUCCESS }
                                ?: getString(R.string.password_entered_failed),
                            Toast.LENGTH_SHORT).show()
                        val returnIntent = Intent(ACTION_PASSWORD_RESULT)
                        if (result == ServiceResult.SUCCESS) {
                            saveInflatedPasswordActivity(true)
                            sendBroadcast(returnIntent)
                            finish()
                            saveCounterOne(value = 0)
                        } else {
                            saveInflatedPasswordActivity(false)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        saveCounterOne(value = 0)
        super.onDestroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val returnIntent = Intent(ACTION_PASSWORD_RESULT)
            sendBroadcast(returnIntent)
            if (getInflatedPasswordActivity()) {
                finish()
                saveCounterOne(value = 0)
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}



