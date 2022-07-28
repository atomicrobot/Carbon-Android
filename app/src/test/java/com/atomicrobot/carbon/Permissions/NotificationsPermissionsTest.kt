package com.atomicrobot.carbon.Permissions

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.atomicrobot.carbon.ui.main.MainViewModel
import com.atomicrobot.carbon.ui.permissions.NotificationsPermissionsViewModel
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.atomicrobot.carbon.R

@RunWith(AndroidJUnit4::class)
class NotificationsPermissionsTest {

    private lateinit var viewModel: NotificationsPermissionsViewModel
    private lateinit var app: Application

    @Before
    fun setup() {
        app = ApplicationProvider.getApplicationContext<Application>()
        viewModel = NotificationsPermissionsViewModel(app)
    }

    @Test
    fun testNotificationNotEnabled() {
        viewModel.setState(NotificationsPermissionsViewModel.UiState.Initial)
        assertTrue(viewModel.getDescription().equals(app.getString(R.string.notifications_pre_ask_description)))
        assertTrue(viewModel.getButtonLabel().equals(app.getString(R.string.notifications_pre_ask_allow_notifications)))
        assertTrue(viewModel.getLaterButtonLabel().equals(app.getString(R.string.notifications_pre_ask_maybe_later)))
    }

    @Test
    fun testNotificationDenied() {
        viewModel.setState(NotificationsPermissionsViewModel.UiState.Denied)
        assertTrue(viewModel.getDescription().equals(app.getString(R.string.notifications_not_enabled_description)))
        assertTrue(viewModel.getButtonLabel().equals(app.getString(R.string.notifications_update_settings)))
        assertTrue(viewModel.getLaterButtonLabel().equals(app.getString(R.string.notifications_pre_ask_maybe_later)))
    }
}