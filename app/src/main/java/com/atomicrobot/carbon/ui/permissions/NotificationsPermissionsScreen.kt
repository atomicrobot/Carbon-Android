package com.atomicrobot.carbon.ui.permissions

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.ui.components.CustomSnackbar
import com.atomicrobot.carbon.ui.components.TopBar
import java.util.*


@Composable
fun NotificationsPermissionsScreen(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
    onPermissionDelayed: () -> Unit,
) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    var inSettings: Boolean by remember { mutableStateOf(false) }
    var returnState: PreAskReturnState by remember { mutableStateOf(PreAskReturnState.INITIAL)}
    val activity = LocalContext.current as Activity
    var shouldShowNotificationsNotEnabled = true
    val context = LocalContext.current
    val intent = Intent()

    intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
    intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (inSettings) {
                    returnState = PreAskReturnState.RETURNED
                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            inSettings = false
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    if (returnState == PreAskReturnState.RETURNED) {
        returnState = PreAskReturnState.INITIAL
        when (NotificationManagerCompat.from(LocalContext.current).areNotificationsEnabled()) {
            true -> onPermissionGranted()
            else -> onPermissionDenied()
        }
    } else {
        val notificationsPermissionRequest =
            activityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = { result ->
                    when (result) {
                        true -> onPermissionGranted()
                        else -> onPermissionDenied()
                    }
                },
            )

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            shouldShowNotificationsNotEnabled = activity.shouldShowRequestPermissionRationale(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }

        if (shouldShowNotificationsNotEnabled) {
            NotificationsPermissionsContent(
                scaffoldState = scaffoldState,
                onEnableNotificationsButtonClicked = {
                    inSettings = true
                    ContextCompat.startActivity(context, intent, null)
                },
                onPermissionDelayedButtonClicked = onPermissionDelayed,
                descriptionText = stringResource(id = R.string.notifications_not_enabled_description),
                allowButtonText = stringResource(id = R.string.notifications_update_settings),
                delayButtonText = stringResource(id = R.string.notifications_pre_ask_maybe_later)
            )
        } else {
            NotificationsPermissionsContent(
                scaffoldState = scaffoldState,
                onEnableNotificationsButtonClicked = {
                    // We will only get here if we are API 33 or higher, but this check supresses a warning
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                        notificationsPermissionRequest.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                },
                onPermissionDelayedButtonClicked = onPermissionDelayed,
                descriptionText = stringResource(id = R.string.notifications_pre_ask_description),
                allowButtonText = stringResource(id = R.string.notifications_pre_ask_allow_notifications),
                delayButtonText = stringResource(id = R.string.notifications_pre_ask_maybe_later)
            )
        }
    }
}


/**
 * This composable wraps the new ActivityResults API and exposes a results launcher to be consumed
 * by other composables. For example:
 *
 * val permissionsLauncher = registerForActivityResult(
 *     contract = ActivityResultContracts.RequestPermission(),
 *     onResult = { granted ->
 *         // Process result, check rationale, etc.
 *         Toast.makeText(context, "Result: $granted", Toast.LENGTH_LONG).show()
 *     }
 * )
 *
 * permissionsLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
 *
 * @param I The input type
 * @param O The output type
 * @param contract The contract to be fulfilled
 * @param onResult Our callback for when a contract is completed
 * @return The launcher for executing the contract
 */
@Composable
fun <I, O> activityResult(
    contract: ActivityResultContract<I, O>,
    onResult: (O) -> Unit
) : ActivityResultLauncher<I> {

    val activityResultRegistry = (LocalContext.current as ActivityResultRegistryOwner).activityResultRegistry

    val currentOnResult = rememberUpdatedState(onResult)

    val key = rememberSaveable { UUID.randomUUID().toString() }

    // This usage of indirection allows us to immediately return an [ActivityResultLauncher] even
    // though we don't actually get an instance until calling the register() method
    val realLauncher = remember { mutableStateOf<ActivityResultLauncher<I>?>(null) }

    val returnedLauncher = remember {
        object : ActivityResultLauncher<I>() {
            override fun launch(input: I, options: ActivityOptionsCompat?) {
                realLauncher.value?.launch(input, options)
            }

            override fun unregister() {
                realLauncher.value?.unregister()
            }

            override fun getContract() = contract
        }
    }

    // This [DisposableEffect] uses our UUID to make sure that we only register this contract once
    DisposableEffect(activityResultRegistry, key, contract) {
        realLauncher.value = activityResultRegistry.register(key, contract) { result ->
            currentOnResult.value(result)
        }

        onDispose { realLauncher.value?.unregister() }
    }

    return returnedLauncher
}




@Composable
fun NotificationsPermissionsContent(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    onEnableNotificationsButtonClicked: () -> Unit,
    onPermissionDelayedButtonClicked: () -> Unit,
    descriptionText: String,
    allowButtonText: String,
    delayButtonText: String,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        scaffoldState = scaffoldState,
        snackbarHost = { CustomSnackbar(hostState = scaffoldState.snackbarHostState) }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopBar()
            Text(
                text = descriptionText,
                textAlign = TextAlign.Center
            )
            OutlinedButton(
                onClick = onEnableNotificationsButtonClicked,
                enabled = true,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = allowButtonText)
            }
            OutlinedButton(
                onClick = onPermissionDelayedButtonClicked,
                enabled = true,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = delayButtonText)
            }
        }
    }
}


enum class PreAskReturnState {
    INITIAL, RETURNED
}


@Preview(name = "Pre-Ask")
@Composable
internal fun PreviewNotificationsPreAskScreen() {
    NotificationsPermissionsContent(
        onEnableNotificationsButtonClicked = {},
        onPermissionDelayedButtonClicked = {},
        descriptionText = stringResource(id = R.string.notifications_not_enabled_description),
        allowButtonText = stringResource(id = R.string.notifications_update_settings),
        delayButtonText = stringResource(id = R.string.notifications_pre_ask_maybe_later)
    )
}

@Preview(name = "Not-Enabled")
@Composable
internal fun PreviewNotificationsNotEnabledScreen() {
    NotificationsPermissionsContent(
        onEnableNotificationsButtonClicked = {},
        onPermissionDelayedButtonClicked = {},
        descriptionText = stringResource(id = R.string.notifications_pre_ask_description),
        allowButtonText = stringResource(id = R.string.notifications_pre_ask_allow_notifications),
        delayButtonText = stringResource(id = R.string.notifications_pre_ask_maybe_later)
    )
}