package com.tanya.core_ui.permissions

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.tanya.core_resources.R

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPermission(
    content: @Composable () -> Unit
) {
    val cameraPermissionState = rememberPermissionState(
        permission = android.Manifest.permission.CAMERA
    )
    when (cameraPermissionState.status) {
        PermissionStatus.Granted -> content()
        is PermissionStatus.Denied -> PermissionDenied {
            cameraPermissionState.launchPermissionRequest()
        }
    }
}

@Composable
internal fun PermissionDenied(
    onPermissionRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { /*do nothing*/ },
        title = { stringResource(id = R.string.camera_permission_rationale) },
        text = { stringResource(id = R.string.camera_permission_rationale) },
        confirmButton = {
            Button(onClick = onPermissionRequest) {
                onPermissionRequest()
                Text(text = stringResource(id = R.string.button_ok))
            }
        }
    )
}