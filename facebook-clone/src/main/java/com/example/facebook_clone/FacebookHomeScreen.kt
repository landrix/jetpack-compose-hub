package com.example.facebook_clone

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@Composable
fun FacebookHomeScreen() {
    FacebookHomeContent()
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun FacebookHomeContent() {
    val tabs = BottomNavItems.values()
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState()

    Column {
        AnimatedVisibility(visible = pagerState.currentPage == BottomNavItems.Home.ordinal) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Facebook",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                )
            }
        }

        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                    color = Color.Blue,
                )
            },
            backgroundColor = Color.White,
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = pagerState.currentPage == index,
                    selectedContentColor = Color.Blue,
                    unselectedContentColor = Color.DarkGray,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.scrollToPage(index)
                        }
                    },
                    // text = { Text(title) },
                    icon = {
                        Icon(
                            modifier = Modifier.size(22.dp),
                            painter = painterResource(id = if (pagerState.currentPage == index) tab.selectedIcon else tab.unSelectedIcon),
                            contentDescription = null,
                        )
                    }
                )
            }
        }
        HorizontalPager(count = tabs.size, state = pagerState) { page: Int ->
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Current Page $page")
            }
        }
    }
}

