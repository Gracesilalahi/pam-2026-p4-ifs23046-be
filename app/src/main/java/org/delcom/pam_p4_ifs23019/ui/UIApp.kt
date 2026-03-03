package org.delcom.pam_p4_ifs23019.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.delcom.pam_p4_ifs23019.helper.ConstHelper
import org.delcom.pam_p4_ifs23019.ui.components.CustomSnackbar
import org.delcom.pam_p4_ifs23019.ui.screens.HomeScreen
import org.delcom.pam_p4_ifs23019.ui.screens.DessertsAddScreen
import org.delcom.pam_p4_ifs23019.ui.screens.DessertsDetailScreen
import org.delcom.pam_p4_ifs23019.ui.screens.DessertsEditScreen
import org.delcom.pam_p4_ifs23019.ui.screens.DessertsScreen
import org.delcom.pam_p4_ifs23019.ui.screens.ProfileScreen
import org.delcom.pam_p4_ifs23019.ui.viewmodels.DessertViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UIApp(
    navController: NavHostController = rememberNavController(),
    DessertViewModel: DessertViewModel
) {
    // Inisialisasi SnackbarHostState
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState){ snackbarData ->
            CustomSnackbar(snackbarData, onDismiss = { snackbarHostState.currentSnackbarData?.dismiss() })
        } },
    ) { _ ->
        NavHost(
            navController = navController,
            startDestination = ConstHelper.RouteNames.Home.path,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F8FA))

        ) {
            // Home
            composable(
                route = ConstHelper.RouteNames.Home.path,
            ) { _ ->
                HomeScreen(
                    navController = navController,
                )
            }

            // Profile
            composable(
                route = ConstHelper.RouteNames.Profile.path,
            ) { _ ->
                ProfileScreen(
                    navController = navController,
                    DessertViewModel = DessertViewModel
                )
            }

            // Desserts
            composable(
                route = ConstHelper.RouteNames.Desserts.path,
            ) { _ ->
                DessertsScreen(
                    navController = navController,
                    DessertViewModel = DessertViewModel
                )
            }

            // Desserts Add
            composable(
                route = ConstHelper.RouteNames.DessertsAdd.path,
            ) { _ ->
                DessertsAddScreen(
                    navController = navController,
                    snackbarHost = snackbarHostState,
                    DessertViewModel = DessertViewModel
                )
            }

            // Desserts Detail
            composable(
                route = ConstHelper.RouteNames.DessertsDetail.path,
                arguments = listOf(
                    navArgument("DessertId") { type = NavType.StringType },
                )
            ) { backStackEntry ->
                val DessertId = backStackEntry.arguments?.getString("DessertId") ?: ""

                DessertsDetailScreen(
                    navController = navController,
                    snackbarHost = snackbarHostState,
                    DessertViewModel = DessertViewModel,
                    DessertId = DessertId
                )
            }

            // Desserts Edit
            composable(
                route = ConstHelper.RouteNames.DessertsEdit.path,
                arguments = listOf(
                    navArgument("DessertId") { type = NavType.StringType },
                )
            ) { backStackEntry ->
                val DessertId = backStackEntry.arguments?.getString("DessertId") ?: ""

                DessertsEditScreen(
                    navController = navController,
                    snackbarHost = snackbarHostState,
                    DessertViewModel = DessertViewModel,
                    DessertId = DessertId
                )
            }
        }
    }

}