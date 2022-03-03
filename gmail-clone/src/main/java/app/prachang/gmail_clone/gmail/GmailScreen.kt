@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")

package app.prachang.gmail_clone.gmail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.prachang.common_compose_ui.animations.RotateIcon
import app.prachang.common_compose_ui.components.CircleImage
import app.prachang.common_compose_ui.utils.isScrollingUp
import app.prachang.dummy_data.instagram.kotlinIcon
import app.prachang.gmail_clone.GmailRoutes
import app.prachang.gmail_clone.home.BottomNavItems
import app.prachang.gmail_clone.home.HomeScreen
import app.prachang.gmail_clone.search.SearchScreen
import app.prachang.theme.CustomColors
import app.prachang.theme.materialyoutheme.GmailTheme
import app.prachang.theme.materialyoutheme.Material3Colors
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun GmailScreen() {
    GmailTheme {
        GmailContent()
    }
}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class
)
@Composable
private fun GmailContent() {
    val focusRequester = remember {
        FocusRequester()
    }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val emailScrollState = rememberLazyListState()

    val navController = rememberNavController()
    val navBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStack?.destination?.route

    val homeNavController = rememberNavController()
    val homeNavBackStack by homeNavController.currentBackStackEntryAsState()
    val homeCurrentRoute = homeNavBackStack?.destination?.route

    val gmailUtils = GmailUtils(
        emailScrollState = emailScrollState,
        searchValue = remember {
            mutableStateOf("")
        },
        focusRequester = FocusRequester(),
        navController = homeNavController,
        currentRoute = homeCurrentRoute,
    )

    val isScrollingUp = gmailUtils.emailScrollState.isScrollingUp()

    NavigationDrawer(
        drawerContent = {
            Text("Here is drawer content")
        },
        drawerState = drawerState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Material3Colors.background)
        ) {
            AnimatedVisibility(
                visible = isScrollingUp && homeCurrentRoute != BottomNavItems.Bookings.route,
            ) {
                TopContent(
                    focusRequester = gmailUtils.focusRequester,
                    searchValue = gmailUtils.searchValue,
                    isEnabled = currentRoute == GmailRoutes.SearchScreen,
                    onClick = {
                        navController.navigate(route = GmailRoutes.SearchScreen) {
                            gmailUtils.navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route = route)
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    },
                    onLeadingIconClick = {
                        if (currentRoute == GmailRoutes.SearchScreen) {
                            navController.popBackStack()
                        } else {
                            coroutineScope.launch {
                                drawerState.open()
                            }
                        }
                    },
                )
            }
            NavHost(
                navController = navController, startDestination = GmailRoutes.HomeScreen,
                builder = {
                    composable(GmailRoutes.HomeScreen) {
                        HomeScreen(
                            gmailUtils = gmailUtils,
                            isScrollingUp = isScrollingUp,
                        )
                    }
                    composable(GmailRoutes.SearchScreen) {
                        SearchScreen(
                            focusRequester = focusRequester
                        )
                    }
                },
            )
        }
    }
}


@Composable
private fun TopContent(
    modifier: Modifier = Modifier,
    searchValue: MutableState<String>,
    isEnabled: Boolean,
    onClick: () -> Unit,
    focusRequester: FocusRequester,
    onLeadingIconClick: () -> Unit = {},
    onProfileIconClick: () -> Unit = {},
) {
    val systemUiController = rememberSystemUiController()
    var icon by remember {
        mutableStateOf(Icons.Default.Menu)
    }
    LaunchedEffect(
        key1 = isEnabled,
        block = {
            delay(300)
            icon = if (isEnabled) Icons.Default.ArrowBack else Icons.Default.Menu
        },
    )
    val backgroundColor = animateColorAsState(
        targetValue = if (isEnabled) Material3Colors.surface else Material3Colors.primary
    )
    SideEffect {
        systemUiController.setSystemBarsColor(backgroundColor.value)
        systemUiController.setNavigationBarColor(CustomColors.VeryLightBlue)
    }

    Box(
        modifier = modifier
            .background(backgroundColor.value)
            .padding(
                horizontal = 16.dp, vertical = 8.dp
            )
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .clip(shape = CircleShape)
                .clickable { onClick() },
            placeholder = {
                Text(text = "Search in emails")
            },
            leadingIcon = {
                IconButton(onClick = onLeadingIconClick) {
                    RotateIcon(
                        state = isEnabled,
                        icon = icon,
                        angle = 360F,
                        duration = 600,
                    )
                }
            },
            trailingIcon = {
                IconButton(onClick = onProfileIconClick) {
                    CircleImage(
                        url = kotlinIcon,
                        modifier = Modifier.size(30.dp),
                    )
                }
            },
            value = searchValue.value,
            onValueChange = { searchValue.value = it },
            shape = CircleShape,
            singleLine = true,
            enabled = isEnabled,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = Color.DarkGray,
                backgroundColor = Material3Colors.surface
            ),
        )
    }
}
