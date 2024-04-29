package com.casecode.preventUninstallApp.presentation.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.alfayedoficial.preventuninstalladmin.ui.theme.MyDeviceAdminReceiverTheme
import com.casecode.preventUninstallApp.presentation.screens.SubmitLockPasswordScreen
import com.casecode.preventUninstallApp.utils.AndroidUtils.configurePreventUninstallApp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyDeviceAdminReceiverTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SubmitLockPasswordScreen {
                        configurePreventUninstallApp(this)
                    }
                }
            }
        }
    }

}
