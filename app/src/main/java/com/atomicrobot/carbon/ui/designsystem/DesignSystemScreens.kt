package com.atomicrobot.carbon.ui.designsystem

import androidx.annotation.StringRes
import com.atomicrobot.carbon.R

sealed class DesignSystemScreens(@StringRes val title: Int, val route: String) {
    object Home: DesignSystemScreens(R.string.design_home ,"design-home")
    object Colors: DesignSystemScreens(R.string.design_colors ,"design-colors")
    object Typography: DesignSystemScreens(R.string.design_typography ,"design-typography")
    object Buttons: DesignSystemScreens(R.string.design_buttons ,"design-buttons")
    object Checkboxes: DesignSystemScreens(R.string.design_checkboxes ,"design-checkboxes")
    object Radios: DesignSystemScreens(R.string.design_radios ,"design-radios")
    object Sliders: DesignSystemScreens(R.string.design_sliders ,"design-sliders")
    object Switches: DesignSystemScreens(R.string.design_switches ,"design-switches")

    companion object {
        fun values(): List<DesignSystemScreens> =
            DesignSystemScreens::class.sealedSubclasses.map {
                it.objectInstance as DesignSystemScreens
            }
    }
}

val designAtomScreens: List<DesignSystemScreens> = listOf(
    DesignSystemScreens.Colors,
    DesignSystemScreens.Typography,
)

val designMoleculeScreens: List<DesignSystemScreens> = listOf(
    DesignSystemScreens.Buttons,
    DesignSystemScreens.Checkboxes,
    DesignSystemScreens.Radios,
    DesignSystemScreens.Sliders,
    DesignSystemScreens.Switches,
)

val designOrganismsScreens: List<DesignSystemScreens> = emptyList()