package com.sdgorinov.composeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sdgorinov.composeapp.ui.theme.*
import com.sgorinov.exilehelper.core.presentation.models.FilterOptionData
import com.sgorinov.exilehelper.currency_feature.presentation.CurrencyFeatureContent
import com.sgorinov.exilehelper.currency_feature.presentation.Groups
import org.koin.androidx.compose.getViewModel

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalAnimationApi::class, ExperimentalAnimationGraphicsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeAppTheme {
                TestContent()
            }
        }
    }

    @Composable
    fun MainContent(items: List<Screen>) {
        val navController = rememberNavController()
        Scaffold(
            bottomBar = {
                BottomNavigation {
                    val backStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = backStackEntry?.destination
                    items.forEach { screen ->
                        BottomNavigationItem(
                            selected = currentDestination?.hierarchy?.any {
                                it.route == screen.route
                            } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(imageVector = Icons.Filled.Favorite, contentDescription = null)
                            },
                            label = {
                                Text(text = screen.label)
                            }
                        )
                    }
                }
            }
        ) {
            NavHost(navController, startDestination = Screen.Screen1.route, Modifier.padding(it)) {
                composable(Screen.Screen1.route) { Groups() }
                composable(Screen.Screen2.route) { CurrencyFeatureContent() }
            }
        }
    }

    @ExperimentalAnimationGraphicsApi
    @ExperimentalAnimationApi
    @Composable
    fun TestContent() {
        val viewModel = getViewModel<MainViewModel>()
        viewModel.requestFilterData()
        val filtersData = viewModel.filterDataState.collectAsState()
        Scaffold(backgroundColor = DefaultDark) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                filtersData.value.forEach { data ->
                    item {
                        HeaderFilterItem(
                            expandedState = data.expanded,
                            checkedState = data.checked,
                            title = data.title,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                        Divider(
                            color = DefaultLight,
                            thickness = 1.dp,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    items(data.filters) {
                        InnerFilterItem(
                            id = it.id,
                            label = it.text,
                            options = it.options,
                            isMinMax = it.isMinMax,
                            minState = it.minState,
                            maxState = it.maxState,
                            isSockets = it.isSockets,
                            expandedState = data.expanded,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp)
                                .padding(bottom = 2.dp)
                        )
                    }
                }
            }
        }
    }

    @ExperimentalAnimationGraphicsApi
    @Composable
    fun HeaderFilterItem(
        expandedState: MutableState<Boolean>,
        checkedState: MutableState<Boolean>,
        title: String,
        modifier: Modifier = Modifier
    ) {
        var expanded by remember {
            expandedState
        }
        var checked by remember {
            checkedState
        }
        val resId = if (checked) {
            R.drawable.checkbox_check
        } else {
            R.drawable.checkbox_uncheck
        }

        val image = AnimatedImageVector.animatedVectorResource(id = resId)

        Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = rememberAnimatedVectorPainter(image, checked),
                contentDescription = null,
                modifier = Modifier.clickable {
                    checked = !checked
                }
            )
            Box(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable {
                        expanded = !expanded
                    },
                contentAlignment = Alignment.CenterStart
            ) {
                Text(text = title)
            }
        }
    }

    @Composable
    fun InnerFilterItem(
        id: String,
        label: String,
        options: List<FilterOptionData>?,
        isMinMax: Boolean,
        minState: MutableState<String>,
        maxState: MutableState<String>,
        isSockets: Boolean,
        expandedState: MutableState<Boolean>,
        modifier: Modifier
    ) {
        val expanded by remember {
            expandedState
        }

        AnimatedVisibility(visible = expanded) {
            Row(modifier = modifier) {
                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier
                        .background(Default)
                        .fillMaxHeight()
                        .fillMaxWidth(fraction = 0.4f)
                        .drawBehind {
                            drawLine(
                                DefaultLight,
                                Offset.Zero,
                                Offset(0f, size.height),
                                2f * density
                            )
                        }
                ) {
                    Text(
                        text = label,
                        modifier = Modifier.padding(start = 10.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (isMinMax) {
                    MinMaxFilter(
                        minState = minState,
                        maxState = maxState,
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .padding(start = 2.dp),
                        textFieldModifier = Modifier
                            .fillMaxHeight()
                            .padding(end = 1.dp)
                            .weight(1f)
                            .background(DefaultLight)
                    )
                }
            }
        }
    }

    @Composable
    fun MinMaxFilter(
        minState: MutableState<String>,
        maxState: MutableState<String>,
        modifier: Modifier = Modifier,
        textFieldModifier: Modifier = Modifier
    ) {
        Row(modifier = modifier) {
            MinMaxTextField(
                state = minState,
                placeholder = "MIN",
                modifier = textFieldModifier
            )
            MinMaxTextField(
                state = maxState,
                placeholder = "MAX",
                modifier = textFieldModifier
            )
        }
    }

    @Composable
    fun MinMaxTextField(
        state: MutableState<String>,
        placeholder: String,
        modifier: Modifier
    ) {
        var data by remember {
            state
        }
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            BasicTextField(
                value = data,
                onValueChange = {
                    data = it
                },
                singleLine = true,
                cursorBrush = SolidColor(Text),
                textStyle = DefaultFont.body1,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            if (data.isBlank()) {
                Text(text = placeholder, color = SecondaryLight)
            }
        }
    }

    sealed class Screen(val route: String, val label: String) {
        object Screen1 : Screen("screen_1", "Screen 1")
        object Screen2 : Screen("screen_2", "Screen 2")
    }
}