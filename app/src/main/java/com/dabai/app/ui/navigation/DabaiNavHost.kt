package com.dabai.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dabai.app.ui.screens.camera.CameraScreen
import com.dabai.app.ui.screens.home.HomeScreen
import com.dabai.app.ui.screens.login.LoginScreen
import com.dabai.app.ui.screens.person.PersonDetailScreen
import com.dabai.app.ui.screens.report.HealthReportScreen
import com.dabai.app.ui.screens.splash.SplashScreen
import com.dabai.app.ui.screens.todo.TodoScreen

@Composable
fun DabaiNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Splash.route
    ) {
        composable(NavRoutes.Splash.route) {
            SplashScreen(onNavigateToLogin = {
                navController.navigate(NavRoutes.Login.route) {
                    popUpTo(NavRoutes.Splash.route) { inclusive = true }
                }
            }, onNavigateToHome = {
                navController.navigate(NavRoutes.Home.route) {
                    popUpTo(NavRoutes.Splash.route) { inclusive = true }
                }
            })
        }

        composable(NavRoutes.Login.route) {
            LoginScreen(onLoginSuccess = {
                navController.navigate(NavRoutes.Home.route) {
                    popUpTo(NavRoutes.Login.route) { inclusive = true }
                }
            })
        }

        composable(NavRoutes.Home.route) {
            HomeScreen(
                onCameraClick = { mode ->
                    navController.navigate(NavRoutes.Camera.createRoute(mode))
                },
                onPersonClick = { personId ->
                    navController.navigate(NavRoutes.PersonDetail.createRoute(personId))
                }
            )
        }

        composable(
            route = NavRoutes.Camera.route,
            arguments = listOf(navArgument("mode") { type = NavType.StringType })
        ) { backStackEntry ->
            val mode = backStackEntry.arguments?.getString("mode") ?: "normal"
            CameraScreen(
                mode = mode,
                onNewPerson = { faceToken, photoPath ->
                    navController.navigate(NavRoutes.NewPerson.createRoute(faceToken, photoPath)) {
                        popUpTo(NavRoutes.Home.route)
                    }
                },
                onKnownPerson = { personId ->
                    navController.navigate(NavRoutes.PersonDetail.createRoute(personId)) {
                        popUpTo(NavRoutes.Home.route)
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = NavRoutes.PersonDetail.route,
            arguments = listOf(navArgument("personId") { type = NavType.StringType })
        ) { backStackEntry ->
            val personId = backStackEntry.arguments?.getString("personId") ?: return@composable
            PersonDetailScreen(
                personId = personId,
                onTakePhoto = {
                    navController.navigate(NavRoutes.Camera.createRoute("normal")) {
                        popUpTo(NavRoutes.Home.route)
                    }
                },
                onViewReport = { recordId ->
                    navController.navigate(NavRoutes.HealthReport.createRoute(personId, recordId))
                },
                onViewTodo = {
                    navController.navigate(NavRoutes.TodoList.createRoute(personId))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = NavRoutes.HealthReport.route,
            arguments = listOf(
                navArgument("personId") { type = NavType.StringType },
                navArgument("recordId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val personId = backStackEntry.arguments?.getString("personId") ?: return@composable
            val recordId = backStackEntry.arguments?.getString("recordId") ?: return@composable
            HealthReportScreen(
                personId = personId,
                recordId = recordId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = NavRoutes.TodoList.route,
            arguments = listOf(navArgument("personId") { type = NavType.StringType })
        ) { backStackEntry ->
            val personId = backStackEntry.arguments?.getString("personId") ?: return@composable
            TodoScreen(
                personId = personId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = NavRoutes.NewPerson.route,
            arguments = listOf(
                navArgument("faceToken") { type = NavType.StringType },
                navArgument("photoPath") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val faceToken = backStackEntry.arguments?.getString("faceToken") ?: return@composable
            val photoPath = backStackEntry.arguments?.getString("photoPath") ?: return@composable
            // NewPersonScreen is handled inside CameraScreen flow
            navController.navigate(NavRoutes.Home.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }
}
