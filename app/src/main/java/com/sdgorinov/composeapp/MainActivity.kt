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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sdgorinov.composeapp.ui.theme.*
import com.sgorinov.exilehelper.core.presentation.models.FilterOptionData
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel by viewModel<MainViewModel>()

    @OptIn(
        ExperimentalAnimationApi::class,
        ExperimentalAnimationGraphicsApi::class,
        ExperimentalMaterialApi::class
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeAppTheme {
                TestContent()
                FloatingActionButton(onClick = { viewModel.onEvent(Event.SearchItems) }) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                }
            }
        }
    }

//    @Composable
//    fun MainContent(items: List<Screen>) {
//        val navController = rememberNavController()
//        Scaffold(
//            bottomBar = {
//                BottomNavigation {
//                    val backStackEntry by navController.currentBackStackEntryAsState()
//                    val currentDestination = backStackEntry?.destination
//                    items.forEach { screen ->
//                        BottomNavigationItem(
//                            selected = currentDestination?.hierarchy?.any {
//                                it.route == screen.route
//                            } == true,
//                            onClick = {
//                                navController.navigate(screen.route) {
//                                    popUpTo(navController.graph.findStartDestination().id) {
//                                        saveState = true
//                                    }
//                                    launchSingleTop = true
//                                    restoreState = true
//                                }
//                            },
//                            icon = {
//                                Icon(imageVector = Icons.Filled.Favorite, contentDescription = null)
//                            },
//                            label = {
//                                Text(text = screen.label)
//                            }
//                        )
//                    }
//                }
//            }
//        ) {
//            NavHost(navController, startDestination = Screen.Screen1.route, Modifier.padding(it)) {
//                composable(Screen.Screen1.route) { Groups() }
//                composable(Screen.Screen2.route) { CurrencyFeatureContent() }
//            }
//        }
//    }

    @ExperimentalMaterialApi
    @ExperimentalAnimationGraphicsApi
    @Composable
    fun TestContent() {
        val viewModel = getViewModel<MainViewModel>()
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
                        Divider(
                            color = DefaultLight,
                            thickness = 1.dp,
                            modifier = Modifier.padding(start = 4.dp, top = 4.dp, bottom = 4.dp)
                        )
                    }
                    items(data.filters) { innerFilterData ->
                        InnerFilterItem(
                            label = innerFilterData.title,
                            options = innerFilterData.options,
                            isSockets = innerFilterData.isSockets,
                            expandedState = data.expanded,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp)
                                .padding(bottom = 2.dp),
                            inputStates = innerFilterData.inputStates,
                            selectedOption = innerFilterData.selectedOption
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

    @ExperimentalMaterialApi
    @Composable
    fun InnerFilterItem(
        label: String,
        options: List<FilterOptionData>?,
        isSockets: Boolean,
        expandedState: MutableState<Boolean>,
        modifier: Modifier = Modifier,
        inputStates: Map<String, MutableState<String>>? = null,
        selectedOption: MutableState<FilterOptionData?> = mutableStateOf(null),
    ) {
        val expanded by remember {
            expandedState
        }

        AnimatedVisibility(visible = expanded) {
            Row(modifier = modifier) {
                FilterHeader(
                    text = label,
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
                )
                if (inputStates != null) {
                    FlexibleTextInputFilter(
                        inputStates = inputStates,
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(start = 2.dp)
                            .weight(1f)
                            .background(DefaultLight),
                        isSockets = isSockets
                    )
                }
                if (options != null) {
                    ExposedAutoCompleteTextField(
                        options = options,
                        selectedOptionState = selectedOption,
                        modifier = Modifier
                            .padding(start = 2.dp)
                            .clip(shape = Shapes.large)
                            .background(Default)
                    )
                }
            }
        }
    }

    @Composable
    fun FilterHeader(
        text: String,
        modifier: Modifier = Modifier
    ) {
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = modifier
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(start = 10.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }

    @Composable
    fun FlexibleTextInputFilter(
        inputStates: Map<String, MutableState<String>>,
        modifier: Modifier = Modifier,
        isSockets: Boolean = false
    ) {
        inputStates.forEach { (placeholder, inputState) ->
            PlaceholderTextInput(
                inputState = inputState,
                placeholder = placeholder,
                modifier = modifier.padding(end = 2.dp),
                isSockets = isSockets
            )
        }
    }

    @Composable
    fun PlaceholderTextInput(
        inputState: MutableState<String>,
        placeholder: String,
        modifier: Modifier = Modifier,
        isSockets: Boolean = false
    ) {
        var data by remember {
            inputState
        }
        Box(
            modifier = modifier.drawBehind {
                if (isSockets) {
                    val color = when (placeholder.lowercase()) {
                        "r" -> Color.Red
                        "g" -> Color.Green
                        "b" -> Color.Blue
                        "w" -> Color.White
                        else -> return@drawBehind
                    }
                    val strokeWidth = 1f
                    drawLine(
                        color = color,
                        start = Offset(0f, size.height - strokeWidth),
                        end = Offset(size.width, size.height - strokeWidth),
                        strokeWidth = strokeWidth * density
                    )
                }
            },
            contentAlignment = Alignment.Center
        ) {
            BasicTextField(
                value = data,
                onValueChange = { data = it },
                singleLine = true,
                cursorBrush = SolidColor(Text),
                textStyle = DefaultFont.body1.copy(textAlign = TextAlign.Center),
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            if (data.isBlank()) {
                Text(text = placeholder.uppercase(), color = SecondaryLight)
            }
        }
    }

    @ExperimentalMaterialApi
    @Composable
    fun ExposedAutoCompleteTextField(
        options: List<FilterOptionData>,
        selectedOptionState: MutableState<FilterOptionData?>,
        modifier: Modifier = Modifier
    ) {
        var dropdownExpanded by remember {
            mutableStateOf(false)
        }
        var selectedOption by remember {
            mutableStateOf(options.firstOrNull { it.id == null })
        }
        var selectedText by remember {
            mutableStateOf(
                TextFieldValue(
                    text = selectedOption?.text ?: "",
                    selection = TextRange(selectedOption?.text?.length ?: 0)
                )
            )
        }

        ExposedDropdownMenuBox(
            expanded = dropdownExpanded,
            onExpandedChange = { dropdownExpanded = !dropdownExpanded },
            modifier = modifier
        ) {
            val icon = if (dropdownExpanded) {
                Icons.Filled.ArrowDropUp
            } else {
                Icons.Filled.ArrowDropDown
            }
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Default)
            ) {
                BasicTextField(
                    value = selectedText,
                    onValueChange = { selectedText = it },
                    singleLine = true,
                    cursorBrush = SolidColor(Text),
                    textStyle = DefaultFont.body1.copy(textAlign = TextAlign.Center),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 2.dp)
                        .padding(start = 4.dp, end = 4.dp + icon.defaultWidth)
                        .align(Alignment.Center)
                        .onFocusChanged { focusState ->
                            if (focusState.isFocused && !dropdownExpanded) {
                                dropdownExpanded = true
                            }
                        }
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Text,
                    modifier = Modifier.align(
                        Alignment.CenterEnd
                    )
                )
            }

            val filteredOptions = options.filter { option ->
                option.text.contains(selectedText.text, ignoreCase = true)
            }

            if (filteredOptions.isNotEmpty()) {
                ExposedDropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { dropdownExpanded = false },
                    modifier = Modifier.background(Default)
                ) {
                    filteredOptions.forEach { filteredOption ->
                        DropdownMenuItem(
                            modifier = Modifier.background(Default),
                            onClick = {
                                selectedOption = filteredOption
                                selectedOptionState.value = filteredOption
                                selectedText = TextFieldValue(
                                    text = filteredOption.text,
                                    selection = TextRange(filteredOption.text.length)
                                )
                                dropdownExpanded = false
                            }
                        ) {
                            Text(
                                text = filteredOption.text,
                                style = DefaultFont.body1
                            )
                        }
                    }
                }
            }
        }
    }

    sealed class Event {
        object SearchItems : Event()
    }

//    sealed class Screen(val route: String, val label: String) {
//        object Screen1 : Screen("screen_1", "Screen 1")
//        object Screen2 : Screen("screen_2", "Screen 2")
//    }
}