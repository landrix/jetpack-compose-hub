package app.prachang.composehub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.prachang.common_compose_ui.animations.loading.ProgressAnimation
import app.prachang.common_compose_ui.animations.loading.SpinKitLoading
import app.prachang.common_compose_ui.animations.loading.WavesAnimation
import app.prachang.common_compose_ui.extensions.Width
import app.prachang.theme.ComposeHubTheme
import app.prachang.theme.Typography

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeHubTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Gray//color = MaterialTheme.colors.background
                ) {
                    // HomeScreen()
                    // ProfileScreen()
                    // MainScreen()

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentWidth(align = Alignment.CenterHorizontally),
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        SpinKitLoading()
                        ProgressAnimation(modifier = Modifier.align(Alignment.CenterHorizontally))
                        WavesAnimation(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }
                }
            }
        }
    }
}

@Composable
internal fun MainScreen() {
    Scaffold(
        backgroundColor = Color.LightGray,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Compose Sample")
                },
            )
        },
        content = {
            val samples = SampleAppData.SampleAppType.values()
            LazyColumn(
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                content = {
                    items(samples) { sample ->
                        SampleItem(sample)
                    }
                },
            )
        },
    )
}

@Composable
private fun SampleItem(sample: SampleAppData.SampleAppType) {
    val hasSubList = sample.samples.isNotEmpty()
    var expanded by remember {
        mutableStateOf(false)
    }
    val paddingHorizontal: Dp by animateDpAsState(targetValue = if (expanded) 0.dp else 22.dp)
    val corner: Dp by animateDpAsState(targetValue = if (expanded) 0.dp else 8.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = paddingHorizontal)
            .clip(shape = RoundedCornerShape(corner))
            .background(Color.White),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .clickable(
                    enabled = hasSubList,
                    indication = LocalIndication.current,
                    interactionSource = MutableInteractionSource()
                ) {
                    expanded = !expanded
                }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = sample.icon),
                contentDescription = null,
            )
            Width(width = 12.dp)
            Text(
                text = sample.title,
                style = Typography.h5,
            )
        }
        if (hasSubList) {
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically(),
            ) {
                SubItem(sample)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SubItem(
    sample: SampleAppData.SampleAppType,
) {
    Column {
        sample.samples.forEach { sample ->
            ListItem(
                modifier = Modifier.padding(horizontal = 30.dp),
                secondaryText = {
                    Text(
                        text = sample.description.orEmpty(),
                        style = Typography.body2,
                    )
                },
                icon = {
                    sample.icon?.let { icon ->
                        Image(
                            modifier = Modifier.size(30.dp),
                            painter = painterResource(id = icon),
                            contentDescription = null
                        )
                    }
                },
                text = {
                    Text(
                        text = sample.label,
                        style = Typography.subtitle1.copy(color = Color.Gray),
                    )
                },
            )
        }
    }
}