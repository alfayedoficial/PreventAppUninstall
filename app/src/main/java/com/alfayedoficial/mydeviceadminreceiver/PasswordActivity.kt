package com.alfayedoficial.mydeviceadminreceiver

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.alfayedoficial.mydeviceadminreceiver.ui.theme.MyDeviceAdminReceiverTheme

class PasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyDeviceAdminReceiverTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    LockPasswordScreen { result ->
                        Toast.makeText(this, "Password entered successfully".takeIf { result == ServiceResult.SUCCESS } ?:"Password entered failed", Toast.LENGTH_SHORT).show()
                        val returnIntent = Intent(ACTION)
                        returnIntent.putExtra("result", result.value)
                        sendBroadcast(returnIntent)
                        finish()
                    }
                }
            }
        }
    }


    override fun onDestroy() {
        saveCounterOne(0)
        super.onDestroy()
    }

    override fun onBackPressed() {
        val returnIntent = Intent(ACTION)
        returnIntent.putExtra("result", ServiceResult.FAILURE.value)
        sendBroadcast(returnIntent)
        super.onBackPressed()
    }

}



