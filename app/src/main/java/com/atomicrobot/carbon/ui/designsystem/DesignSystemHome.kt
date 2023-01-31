package com.atomicrobot.carbon.ui.designsystem

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.ui.theme.CarbonAndroidTheme
import org.koin.androidx.compose.getViewModel

internal const val designSystemHomeRoute: String = "design-system-home"

//region Composables
//region LazyColumn extension
@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.atoms(onAtomClicked: (String) -> Unit) {
    stickyHeader {
        DesignHomeListHeader(R.drawable.atom, stringResource(id = R.string.design_atoms))
    }
    items(atoms) { atom: DesignSystemScreens ->
        DesignScreenRow(stringResource(id = atom.title)) {
            onAtomClicked(atom.route)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.molecules(onMoleculeClicked: (String) -> Unit) {
    stickyHeader {
        DesignHomeListHeader(R.drawable.molecule, stringResource(id = R.string.design_molecule))
    }
    items(molecules) { molecule: DesignSystemScreens ->
        DesignScreenRow(stringResource(id = molecule.title)) {
            onMoleculeClicked(molecule.route)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.organisms(onOrganismClicked: (String) -> Unit) {
    stickyHeader {
        DesignHomeListHeader(R.drawable.organism, stringResource(id = R.string.design_organisms))
    }
    items(organisms) { organism: DesignSystemScreens ->
        DesignScreenRow(stringResource(id = organism.title)) {
            onOrganismClicked(organism.route)
        }
    }
}
//endregion

@Composable
fun DesignHomeListHeader(iconRes: Int, title: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painterResource(id = iconRes),
                "Header section icon",
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
        Divider()
    }
}

@Composable
fun DesignScreenRow(title: String, onItemClicked: (String) -> Unit = {}) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onItemClicked(title) }
    ) {
        Row(
            modifier = Modifier
                .padding(start = 16.dp, top = 8.dp, end = 24.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                title,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(Icons.Filled.ChevronRight, "View $title")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesignSystemHomeScreen(
    modifier: Modifier = Modifier,
    designSystemVM: DesignSystemViewModel = getViewModel(),
    onNavigate: (String) -> Unit = {},
    onNavIconClicked: () -> Unit,
) {
    val screenState: DesignSystemViewModel.ScreenState by designSystemVM.uiState.collectAsState()
    CarbonAndroidTheme(
        darkTheme = screenState.darkMode,
        fontScale = screenState.fontScale.scale,
    ) {
        Scaffold(
            topBar = {
                DesignScreenAppBar(
                    title = "Design Systems",
                    screenState.darkMode,
                    selectedFontScale = screenState.fontScale,
                    onBackPressed = onNavIconClicked,
                    onFontScaleChanged = {
                        designSystemVM.updateFontScale(it)
                    },
                    onDarkModeChanged = {
                        designSystemVM.enabledDarkMode(it)
                    }
                )
            },
            modifier = modifier
        ) {
            LazyColumn(
                modifier = modifier
                    .padding(it)
                    .padding(horizontal = 16.dp, vertical = 2.dp)
            ) {
                this.atoms(onNavigate)
                this.molecules(onNavigate)
                this.organisms(onNavigate)
            }
        }
    }
}
//endregion

//region Composable Previews
@Preview
@Composable
fun AtomsPreview() = CarbonAndroidTheme {
    LazyColumn {
        this.atoms {}
    }
}

@Preview
@Composable
fun MoleculesPreview() = CarbonAndroidTheme {
    LazyColumn {
        this.molecules {}
    }
}

@Preview
@Composable
fun OrganismsPreview() = CarbonAndroidTheme {
    LazyColumn {
        this.organisms {}
    }
}

@Preview("Atom Header")
@Composable
fun AtomsDesignHomeListHeaderPreview() = CarbonAndroidTheme {
    DesignHomeListHeader(R.drawable.atom, "Atoms")
}

@Preview("Molecule Header")
@Composable
fun MoleculesDesignHomeListHeaderPreview() = CarbonAndroidTheme {
    DesignHomeListHeader(R.drawable.molecule, "Molecules")
}

@Preview("Organisms Header")
@Composable
fun OrganismsDesignHomeListHeaderPreview() = CarbonAndroidTheme {
    DesignHomeListHeader(R.drawable.organism, "Organisms")
}
//endregion
