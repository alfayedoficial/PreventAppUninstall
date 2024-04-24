package com.alfayedoficial.mydeviceadminreceiver.presentation.activities

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
import com.alfayedoficial.mydeviceadminreceiver.data.preference.saveCounterOne
import com.alfayedoficial.mydeviceadminreceiver.domain.model.ServiceResult
import com.alfayedoficial.mydeviceadminreceiver.presentation.screens.LockPasswordScreen
import com.alfayedoficial.mydeviceadminreceiver.service.ACTION_PASSWORD_RESULT
import com.alfayedoficial.mydeviceadminreceiver.ui.theme.MyDeviceAdminReceiverTheme

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
                            "Password entered successfully".takeIf { result == ServiceResult.SUCCESS }
                                ?: "Password entered failed",
                            Toast.LENGTH_SHORT).show()
                        val returnIntent = Intent(ACTION_PASSWORD_RESULT)
                        returnIntent.putExtra(RESULT_EXTRA, result.value)
                        sendBroadcast(returnIntent)
                        finish()
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
            returnIntent.putExtra(RESULT_EXTRA, ServiceResult.FAILURE.value)
            sendBroadcast(returnIntent)
            finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    companion object {
        private const val RESULT_EXTRA = "result"
    }
}



