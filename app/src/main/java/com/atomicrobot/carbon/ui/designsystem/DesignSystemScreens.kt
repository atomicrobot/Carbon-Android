package com.atomicrobot.carbon.ui.designsystem

import androidx.annotation.StringRes
import com.atomicrobot.carbon.R

enum class DesignSystemType {
    Atom,
    Molecule,
    @Suppress("unused")
    Organism,
}

sealed class DesignSystemScreens(
    @StringRes val title: Int,
    val route: String,
    val type: DesignSystemType
) {
    object Colors : DesignSystemScreens(
        R.string.design_colors,
        "design-system-colors",
        DesignSystemType.Atom
    )
    object Typography : DesignSystemScreens(
        R.string.design_typography,
        "design-system-typography",
        DesignSystemType.Atom
    )
    object Buttons : DesignSystemScreens(
        R.string.design_buttons,
        "design-system-buttons",
        DesignSystemType.Molecule
    )
    object Checkboxes : DesignSystemScreens(
        R.string.design_checkboxes,
        "design-system-checkboxes",
        DesignSystemType.Molecule
    )
    object Pickers : DesignSystemScreens(
        R.string.design_pickers,
        "design-system-pickers",
        DesignSystemType.Molecule)
    object Radios : DesignSystemScreens(
        R.string.design_radios,
        "design-system-radios",
        DesignSystemType.Molecule
    )
    object Sliders : DesignSystemScreens(
        R.string.design_sliders,
        "design-system-sliders",
        DesignSystemType.Molecule
    )
    object Switches : DesignSystemScreens(
        R.string.design_switches,
        "design-system-switches",
        DesignSystemType.Molecule
    )
    object TextFields : DesignSystemScreens(
        R.string.design_textfields,
        "design-system-textfields",
        DesignSystemType.Molecule
    )

    companion object {
        fun values(): List<DesignSystemScreens> =
            DesignSystemScreens::class.sealedSubclasses.map {
                it.objectInstance as DesignSystemScreens
            }

        fun values(type: DesignSystemType): List<DesignSystemScreens> =
            DesignSystemScreens::class.sealedSubclasses.map {
                it.objectInstance as DesignSystemScreens
            }.filter {  it.type == type }
    }
}

val atoms: List<DesignSystemScreens> = DesignSystemScreens.values(DesignSystemType.Atom)

val molecules: List<DesignSystemScreens> = DesignSystemScreens.values(DesignSystemType.Molecule)

val organisms: List<DesignSystemScreens> = DesignSystemScreens.values(DesignSystemType.Organism)
