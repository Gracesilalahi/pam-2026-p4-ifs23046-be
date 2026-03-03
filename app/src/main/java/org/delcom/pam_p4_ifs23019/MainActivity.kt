package org.delcom.pam_p4_ifs23019

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.delcom.pam_p4_ifs23019.ui.UIApp
import org.delcom.pam_p4_ifs23019.ui.theme.DelcomTheme
import org.delcom.pam_p4_ifs23019.ui.viewmodels.DessertViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val DessertViewModel: DessertViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DelcomTheme {
                UIApp(
                    DessertViewModel = DessertViewModel
                )
            }
        }
    }
}