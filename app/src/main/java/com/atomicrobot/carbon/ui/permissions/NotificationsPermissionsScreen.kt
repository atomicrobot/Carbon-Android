package com.atomicrobot.carbon.ui.permissions

import android.Manifest
import android.content.Intent
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.ui.components.CustomSnackbar
import com.atomicrobot.carbon.ui.components.TopBar
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import java.util.*


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Sample() {
    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.POST_NOTIFICATIONS
        )
    )
    if (locationPermissionsState.allPermissionsGranted) {
        Text("Thanks! I can access your exact location :D")
    } else {
        Column {
            val allPermissionsRevoked =
                locationPermissionsState.permissions.size ==
                        locationPermissionsState.revokedPermissions.size

            val textToShow = if (!allPermissionsRevoked) {
                // If not all the permissions are revoked, it's because the user accepted the COARSE
                // location permission, but not the FINE one.
                "Yay! Thanks for letting me access your approximate location. " +
                        "But you know what would be great? If you allow me to know where you " +
                        "exactly are. Thank you!"
            } else if (locationPermissionsState.shouldShowRationale) {
                // Both location permissions have been denied
                "Getting your exact location is important for this app. " +
                        "Please grant us fine location. Thank you :D"
            } else {
                // First time the user sees this feature or the user doesn't want to be asked again
                "This feature requires location permission"
            }

            val buttonText = if (!allPermissionsRevoked) {
                "Allow precise location"
            } else {
                "Request permissions"
            }

            Text(text = textToShow)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { locationPermissionsState.launchMultiplePermissionRequest() }) {
                Text(buttonText)
            }
        }
    }
}

@Composable
fun NotificationsPermissionsScreen(
    notificationsPermissionsViewModel: NotificationsPermissionsViewModel,
    activity: AppCompatActivity,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
    onPermissionDelayed: () -> Unit,
) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    var inSettings: Boolean by remember { mutableStateOf(false) }
    var returnState: PreAskReturnState by remember { mutableStateOf(PreAskReturnState.INITIAL)}
//    val activity = LocalContext.current as AppCompatActivity

//    if ( notificationsPermissionsViewModel.uiState.value == NotificationsPermissionsViewModel.UiState.Unknown) {
//        var shouldShowNotificationsNotEnabled = true
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
//            shouldShowNotificationsNotEnabled = activity.shouldShowRequestPermissionRationale(
//                Manifest.permission.POST_NOTIFICATIONS
//            )
//        }
//
//        if (shouldShowNotificationsNotEnabled) {
//            notificationsPermissionsViewModel.setState(NotificationsPermissionsViewModel.UiState.Denied)
//        } else {
//            notificationsPermissionsViewModel.setState(NotificationsPermissionsViewModel.UiState.Initial)
//        }
//    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if ( inSettings ) {
                    returnState = PreAskReturnState.RETURNED
                    inSettings = false
                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            notificationsPermissionsViewModel.setState(NotificationsPermissionsViewModel.UiState.Unknown)
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    when ( returnState ) {
        PreAskReturnState.RETURNED -> {
            returnState = PreAskReturnState.INITIAL
            when (NotificationManagerCompat.from(LocalContext.current).areNotificationsEnabled()) {
                true -> onPermissionGranted()
                else -> onPermissionDenied()
            }
        }
        else -> {
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

            val onEnableNotificationsButtonClicked = {
                if (notificationsPermissionsViewModel.uiState.value == NotificationsPermissionsViewModel.UiState.Denied) {
                    val intent = Intent()
                    intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, activity.packageName)
                    inSettings = true

                    ContextCompat.startActivity(activity, intent, null)
                } else {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                        notificationsPermissionRequest.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
            }

            NotificationsPermissionsContent(
                scaffoldState = scaffoldState,
                onEnableNotificationsButtonClicked = onEnableNotificationsButtonClicked,
                onPermissionDelayedButtonClicked = onPermissionDelayed,
                descriptionText = notificationsPermissionsViewModel.getDescription(),
                allowButtonText = notificationsPermissionsViewModel.getButtonLabel(),
                delayButtonText = notificationsPermissionsViewModel.getLaterButtonLabel()
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
    onEnableNotificationsButtonClicked: () -> Unit = {},
    onPermissionDelayedButtonClicked: () -> Unit = {},
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


@Preview(name = "Pre-Ask-Denied")
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

@Preview(name = "Pre-Ask-Not-Enabled")
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