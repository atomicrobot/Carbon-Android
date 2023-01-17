package com.atomicrobot.carbon.ui.about

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.ui.theme.Orange

@Preview(showBackground = true)
@Composable
fun AboutScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        aboutBody()
    }
}

@Composable
fun aboutBody() {
    val context = LocalContext.current
    LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
        item {
            Text(text = stringResource(R.string.about), style = MaterialTheme.typography.headlineMedium)
            Text(text = stringResource(R.string.about_desc), style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.feature_about),
                    contentDescription = "placeholder image"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.about_flexible_solutions),
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = stringResource(R.string.about_flexible_solutions_sub_header),
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = stringResource(R.string.about_flexible_solutions_desc),
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        shape = RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 5.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 5.dp
                        )
                    )
                    .height(IntrinsicSize.Max)
            ) {

                Box {
                    Column(
                        modifier = Modifier.width(50.dp)
                    ) {
                        StripedLineShape()
                    }

                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {

                        Text(
                            text = stringResource(R.string.about_ten_plus),
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Text(
                            text = stringResource(R.string.about_years_in_business),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.about_forty_plus),
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Text(
                            text = stringResource(R.string.about_employees),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.about_one_hundred_fifty_plus),
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Text(
                            text = stringResource(R.string.about_apps_shipped),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.about_what_makes_us_different),
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.about_what_makes_us_different_craft).toUpperCase(
                    Locale.current
                ),
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = stringResource(R.string.about_what_makes_us_different_craft_desc),
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.about_what_makes_us_different_collaboration).toUpperCase(
                    Locale.current
                ),
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = stringResource(R.string.about_what_makes_us_different_collaboration_desc),
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.about_what_makes_us_different_curiosity).toUpperCase(
                    Locale.current
                ),
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = stringResource(R.string.about_what_makes_us_different_curiosity_desc),
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.about_what_makes_us_different_impact).toUpperCase(
                    Locale.current
                ),
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = stringResource(R.string.about_what_makes_us_different_impact_desc),
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.about_what_makes_us_different_partnership).toUpperCase(
                    Locale.current
                ),
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = stringResource(R.string.about_what_makes_us_different_partnership_desc),
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.about_what_makes_us_different_transparency).toUpperCase(
                    Locale.current
                ),
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = stringResource(R.string.about_what_makes_us_different_transparency_desc),
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .height(IntrinsicSize.Max)
                    .padding(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.about_our_success),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )

                FilledTonalButton(
                    onClick = {
                        val uri = Uri.parse("https://atomicrobot.com/careers/")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        context.startActivity(intent)
                    }
                ) {
                    Text(
                        text = stringResource(R.string.about_join_our_team),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.about_awards_and_recognition),
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = stringResource(R.string.about_awards_and_recognition_apps_created_for_impact),
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = stringResource(R.string.about_awards_and_recognition_apps_created_for_impact_desc),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

class StripedLine : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width, 0f)
            lineTo(0f, size.height / 7f)

            moveTo(size.width, (size.height / 7f) * 2f)
            lineTo(0f, (size.height / 7f) * 3f)
            lineTo(0f, (size.height / 7f) * 5f)
            lineTo(size.width, (size.height / 7f) * 4f)

            moveTo(size.width, (size.height / 7f) * 6f)
            lineTo(0f, size.height)
            lineTo(size.width, size.height)

            close()
        }
        return Outline.Generic(path)
    }
}

@Preview
@Composable
fun StripedLineShape(modifier: Modifier = Modifier) {
    val shape = StripedLine()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(shape)
            .background(Orange)
    )
}
