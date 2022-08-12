package com.atomicrobot.carbon.ui.compose

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import com.atomicrobot.carbon.util.Common
import kotlinx.coroutines.CoroutineScope

@Composable
fun RequestPermission(
        permission: String,
        onShowRationale: suspend CoroutineScope.(String) -> PermissionRationaleResult
            = { PermissionRationaleResult.ActionPerformed },
        onPermissionResult: (PermissionRequestResult) -> Unit) {
    val activity = LocalActivity.current

    // Create a permission request launcher that will received the result of the perm. request
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            onPermissionResult(PermissionRequestResult.Granted)
        } else {
            onPermissionResult(PermissionRequestResult.Denied)
        }
    }

    when {
        Common.hasPermission(activity, permission) ->
            onPermissionResult(PermissionRequestResult.Granted)
        activity.shouldShowRequestPermissionRationale(permission) -> {
            LaunchedEffect(permission) {
                if (onShowRationale(permission) == PermissionRationaleResult.ActionPerformed) {
                    permissionLauncher.launch(permission)
                } else {
                    onPermissionResult(PermissionRequestResult.Denied)
                }
            }
        }
        else -> {
            SideEffect {
                permissionLauncher.launch(permission)
            }
        }
    }
}

@Composable
fun RequestPermissions(
    permissions: Array<String>,
    onShowRationale: suspend CoroutineScope.(String) -> PermissionRationaleResult,
    onPermissionResult: (PermissionRequestResult) -> Unit
) {
    val activity = LocalActivity.current

    // Create a permission request launcher that will received the result of the perm. request
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { results: Map<String, Boolean> ->
        if (results.all(Map.Entry<String, Boolean>::value)) {
            onPermissionResult(PermissionRequestResult.Granted)
        } else {
            onPermissionResult(PermissionRequestResult.Denied)
        }
    }

    when {
        permissions.all { Common.hasPermission(activity, it) } ->
            onPermissionResult(PermissionRequestResult.Granted)
        else -> {
            permissions.forEach {
                if (activity.shouldShowRequestPermissionRationale(it)) {
                    LaunchedEffect(it) {
                        if (onShowRationale(it) == PermissionRationaleResult.ActionPerformed) {
                            permissionLauncher.launch(permissions)
                        } else {
                            onPermissionResult(PermissionRequestResult.Denied)
                        }
                    }
                }
            }

            SideEffect {
                permissionLauncher.launch(permissions)
            }
        }
    }
}

sealed class PermissionRequestResult {
    object Granted : PermissionRequestResult()
    object Denied : PermissionRequestResult()
}

sealed class PermissionRationaleResult {
    object Dismissed : PermissionRationaleResult()
    object ActionPerformed : PermissionRationaleResult()
}
