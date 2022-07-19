package com.atomicrobot.carbon.ui.permissions

import android.app.Application
import android.content.Intent
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityOptionsCompat
import com.atomicrobot.carbon.app.LoadingDelayMs
import com.atomicrobot.carbon.data.api.github.GitHubInteractor
import com.atomicrobot.carbon.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NotificationsPermissionsViewModel @Inject constructor(
    private val app: Application,
) : BaseViewModel(app) {

    var inSettings: Boolean = false
    var returnState: PreAskReturnState = PreAskReturnState.INITIAL

    override fun setupViewModel() {
        TODO("Not yet implemented")
    }

    @Composable
    fun EnablePermissionButtonClicked() {

        val context = LocalContext.current
        val intent = Intent()

        intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
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
fun <I, O> activityResultA(
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
