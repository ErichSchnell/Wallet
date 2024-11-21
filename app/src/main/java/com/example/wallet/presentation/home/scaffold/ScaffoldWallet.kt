package com.example.wallet.presentation.home.scaffold

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wallet.R
import com.example.wallet.presentation.home.Screens
import com.example.wallet.presentation.home.profile.DialogAddCategory
import com.example.wallet.presentation.model.CategoryUi
import com.example.wallet.presentation.model.EventItem
import com.example.wallet.presentation.model.ProfileModelUi
import com.example.wallet.presentation.model.TabItem
import com.example.wallet.presentation.model.TransactionModelUI
import com.example.wallet.presentation.model.TypeOfTransaction
import com.example.wallet.presentation.util.ex.millisToDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldWallet(
    screen: Screens,

    onClickNavigationIcon: () -> Unit = {},

    transactionSelected: TransactionModelUI?,
    onClickSelectTransaction: (TransactionModelUI?) -> Unit,
    transactionList: List<TransactionModelUI>,
    transactionBookmark: List<TransactionModelUI>,

    profiles: List<ProfileModelUi> = emptyList(),
    profileSelected: ProfileModelUi? = null,
    onProfileChanged: (ProfileModelUi?) -> Unit = {},

    showSettingIcon: Boolean = true,
    onClickSetting: () -> Unit = {},

    showBottomBar: Boolean = true,
    onClickBottomNavigation: (Int) -> Unit = {},
    onClickEventItem: (TransactionModelUI, EventItem) -> Unit,
    onClickAddCategory: (CategoryUi) -> Unit,

    content: @Composable (Dp, Dp) -> Unit
) {
    var eventItem by remember { mutableStateOf<EventItem>(EventItem.ADD) }

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(initialValue = SheetValue.Expanded)
    )
    val sheetPeekHeight = if (showBottomBar) 100.dp else 0.dp

    var showAddCategory by remember { mutableStateOf(false) }

    LaunchedEffect(showBottomBar) {
        if (!showBottomBar) {
            scaffoldState.bottomSheetState.partialExpand()
        }
    }

    BoxWithConstraints(Modifier.fillMaxSize()) {

        val maxBottomScaffoldBardHeight = maxHeight * 0.4f
        val maxModalBottomBardHeight = maxHeight * 0.5f

        BottomSheetScaffold(
            scaffoldState = scaffoldState,// BottomSheetScaffoldState = rememberBottomSheetScaffoldState(),

            sheetPeekHeight = sheetPeekHeight, //Dp = BottomSheetDefaults.SheetPeekHeight,
            sheetContent = {
                BottomBar(
                    screen = screen,
                    maxBottomBardHeight = maxBottomScaffoldBardHeight,
                    scaffoldState = scaffoldState.bottomSheetState.currentValue,
                    transactionList = transactionList,
                    transactionBookmark = transactionBookmark,
                    onClickNavigation = onClickBottomNavigation,
                    onClickEventItem = { item, event ->
                        if (event == EventItem.BOOKMARK) {
                            onClickEventItem(item, event)
                        } else {
                            onClickSelectTransaction(item)
                            eventItem = event
                        }
                    },
                )
            },

            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            sheetContainerColor = Color.Black,
            sheetContentColor = MaterialTheme.colorScheme.onBackground,

            sheetDragHandle = { SheetDragHandle() }
        ) {

            val topPadding = TopAppBarDefaults.windowInsets.asPaddingValues().calculateTopPadding() + 56.dp

            Box(Modifier.fillMaxSize()) {
                content(topPadding, maxBottomScaffoldBardHeight)
                TopBar(
                    title = screen.title,

                    navigationIcon = screen.iconNavigation,
                    onClicknavigationIcon = onClickNavigationIcon,

                    profiles = profiles,
                    profileSelected = profileSelected,
                    onProfileChanged = onProfileChanged,

                    showSettingIcon = showSettingIcon,
                    onClickSetting = onClickSetting,
                )

                AnimatedVisibility(
                    visible = scaffoldState.bottomSheetState.currentValue == SheetValue.Expanded && showBottomBar,
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                    FloatingActionButton(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(bottom = maxBottomScaffoldBardHeight + 34.dp, end = 16.dp),
                        containerColor = Color.White,
                        contentColor = Color.Black,
                        onClick = {
                            onClickSelectTransaction(TransactionModelUI())
                            eventItem = EventItem.ADD
                        },
                        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 18.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "")
                    }
                }
                AnimatedVisibility(
                    visible = scaffoldState.bottomSheetState.currentValue == SheetValue.PartiallyExpanded && showBottomBar,
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                    FloatingActionButton(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(bottom = sheetPeekHeight + 16.dp, end = 16.dp),
                        containerColor = Color.White,
                        contentColor = Color.Black,
                        onClick = {
                            onClickSelectTransaction(TransactionModelUI())
                            eventItem = EventItem.ADD
                        }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "")
                    }
                }

                if (transactionSelected != null) {
                    when (eventItem) {
                        EventItem.ADD,
                        EventItem.EDIT -> {
                            ModalBottomSheetWallet(
                                onDismissRequest = {
                                    onClickSelectTransaction(null)
                                },
                                eventItem = eventItem,
                                maxBottomBardHeight = maxModalBottomBardHeight,
                                isLoading = false,
                                categories = profileSelected?.categories ?: emptyList(),
                                item = transactionSelected,
                                onClickEvent = { item ->
                                    onClickEventItem(item, eventItem)
                                },
                                onClickAddCategory = {
                                    showAddCategory = true
                                }
                            )
                        }

                        EventItem.DELETE -> {
                            DialogDeleteTransaction(
                                onDismissRequest = { onClickSelectTransaction(null) },
                                onClickAccept = {
                                    onClickEventItem(transactionSelected!!, EventItem.DELETE)
                                }
                            )
                        }

                        else -> {}
                    }
                }

            }
        }
    }

    DialogAddCategory(
        showAddCategory = showAddCategory,
        onDismissRequest = { showAddCategory = false },
        onClickAddCategory = { category ->
            showAddCategory = false
            onClickAddCategory(category)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: Int? = null,
    navigationIcon: Int? = null,
    profiles: List<ProfileModelUi> = emptyList(),
    profileSelected: ProfileModelUi? = null,
    onClicknavigationIcon: () -> Unit = {},
    onProfileChanged: (ProfileModelUi?) -> Unit = {},
    showSettingIcon: Boolean = false,
    onClickSetting: () -> Unit = {},
) {
    TopAppBar(
        title = {
            if (title != null) {
                Text(
                    text = stringResource(title),
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        navigationIcon = {
            if (navigationIcon != null) {
                Icon(
                    modifier = Modifier
                        .clickable(onClick = onClicknavigationIcon)
                        .padding(12.dp)
                        .size(32.dp),
                    painter = painterResource(navigationIcon),
                    contentDescription = ""
                )
            }
        },
        actions = {
            if (showSettingIcon) {
                IconSetting(onClick = onClickSetting)
            }
            ProfileSelected(
                profileSelected = profileSelected,
                profiles = profiles,
                onValueChanged = onProfileChanged
            )
        },
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = Color.Transparent,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground,
        )
    )
}

@Composable
fun IconSetting(onClick: () -> Unit) {
    IconButton(onClick = { onClick() }) {
        Icon(imageVector = Icons.Default.Settings, contentDescription = "")
    }
}

@Composable
fun ProfileSelected(
    profiles: List<ProfileModelUi> = emptyList(),
    profileSelected: ProfileModelUi?,
    onValueChanged: (ProfileModelUi?) -> Unit
) {
    if (profileSelected == null) return


    var isExpanded by remember { mutableStateOf(false) }
    OutlinedButton(
        modifier = Modifier.padding(end = 16.dp),
        onClick = {
            if (profiles.isNotEmpty()) {
                isExpanded = true
            }
        }
    ) {
        Text(
            text = profileSelected.name,
            fontWeight = FontWeight.ExtraLight,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
    DropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
        profiles.forEach { profile ->
            if (profile != profileSelected) {
                DropdownMenuItem(
                    text = { Text(profile.name) },
                    onClick = {
                        isExpanded = false
                        onValueChanged(profile)
                    }
                )
            }
        }
        if (profiles.size < 3) {
            DropdownMenuItem(
                text = { Text("agregar perfil") },
                onClick = {
                    isExpanded = false
                    onValueChanged(null)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBar(
    screen: Screens,
    maxBottomBardHeight: Dp,
    scaffoldState: SheetValue,
    transactionList: List<TransactionModelUI>,
    transactionBookmark: List<TransactionModelUI>,
    onClickNavigation: (Int) -> Unit,
    onClickEventItem: (TransactionModelUI, EventItem) -> Unit,
) {

    var itemSelected by remember { mutableStateOf<TransactionModelUI?>(null) }
    val tabItems = listOf(
        TabItem(
            title = stringResource(R.string.history),
            unselectedIcon = R.drawable.ic_clipboard_text_outline,
            selectedIcon = R.drawable.ic_clipboard_text,
        ),
        TabItem(
            title = stringResource(R.string.reuse),
            unselectedIcon = R.drawable.ic_bookmark_outline,
            selectedIcon = R.drawable.ic_bookmark,
        )
    )
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(0) { tabItems.size }

    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
        itemSelected = null
    }
    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            selectedTabIndex = pagerState.currentPage
            itemSelected = null
        }
    }

    Column(
        Modifier
            .fillMaxWidth()
            .height(maxBottomBardHeight)
            .padding(
                bottom = BottomAppBarDefaults.windowInsets
                    .asPaddingValues()
                    .calculateBottomPadding()
            )
    ) {
        AnimatedVisibility(scaffoldState == SheetValue.Expanded) {
            Column {
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = Color.Black,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ) {
                    tabItems.forEachIndexed { index, item ->
                        Tab(
                            selected = index == selectedTabIndex,
                            onClick = { selectedTabIndex = index },
                            icon = {
                                Icon(
                                    painter = if (index == selectedTabIndex) painterResource(item.selectedIcon)
                                    else painterResource(item.unselectedIcon),
                                    contentDescription = ""
                                )
                            }
                        )
                    }

                }
                HorizontalPager(state = pagerState) { currentPage ->
                    when (currentPage) {
                        0 -> {
                            Transacciones(
                                transactions = transactionList,
                                items = { trans ->
                                    TransaccionesItem(
                                        trans = trans,
                                        isSelected = (itemSelected == trans),
                                        onLongClick = { itemSelected = it },
                                        onClickEventItem = onClickEventItem
                                    )
                                },
                            )
                        }

                        1 -> {
                            Transacciones(
                                transactions = transactionBookmark,
                                items = { trans ->
                                    TransaccionesBookmark(
                                        trans = trans,
                                        isSelected = (itemSelected == trans),
                                        onLongClick = { itemSelected = it },
                                        onClickEventItem = onClickEventItem
                                    )
                                },
                            )
                        }
                    }

                }
            }
        }
        AnimatedVisibility(scaffoldState == SheetValue.PartiallyExpanded) {
            Row(Modifier.fillMaxWidth()) {
                BottomNavigationWallet(
                    modifier = Modifier.weight(1f),
                    title = Screens.INCOME.title,
                    iconUnfocused = Screens.INCOME.iconUnFocused,
                    iconFocused = Screens.INCOME.iconFocused,
                    selected = screen.id == Screens.INCOME.id,
                    background = Screens.INCOME.colors.container
                ) { onClickNavigation(Screens.INCOME.id) }

                BottomNavigationWallet(
                    modifier = Modifier.weight(1f),
                    title = Screens.OVERVIEW.title,
                    iconUnfocused = Screens.OVERVIEW.iconUnFocused,
                    iconFocused = Screens.OVERVIEW.iconFocused,
                    selected = screen.id == Screens.OVERVIEW.id,
                    background = Screens.OVERVIEW.colors.container
                ) { onClickNavigation(Screens.OVERVIEW.id) }

                BottomNavigationWallet(
                    modifier = Modifier.weight(1f),
                    title = Screens.EXPENSES.title,
                    iconUnfocused = Screens.EXPENSES.iconUnFocused,
                    iconFocused = Screens.EXPENSES.iconFocused,
                    selected = screen.id == Screens.EXPENSES.id,
                    background = Screens.EXPENSES.colors.container
                ) { onClickNavigation(Screens.EXPENSES.id) }
            }
        }
    }
}

@Composable
fun BottomNavigationWallet(
    modifier: Modifier,
    title: Int,
    iconUnfocused: Int,
    iconFocused: Int,
    selected: Boolean,
    background: Color,
    onClick: () -> Unit,
) {
    val icon = if (!selected) iconUnfocused else iconFocused

    Column(
        modifier = modifier.clickable { onClick() },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.Center) {

            if (selected) Box(
                Modifier
                    .background(
                        background,
                        RoundedCornerShape(
                            topStart = 20.dp,
                            bottomEnd = 20.dp,
                            topEnd = 20.dp,
                            bottomStart = 20.dp,
                        )
                    )
                    .height(28.dp)
                    .width(54.dp)
            )
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(icon),
                contentDescription = ""
            )
        }

        Text(
            text = stringResource(title),
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }

}

@Composable
fun Transacciones(
    transactions: List<TransactionModelUI>,
    items: @Composable (TransactionModelUI) -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        Spacer(Modifier.height(8.dp))
        LazyColumn {
            items(transactions) { trans ->
                items(trans)
                HorizontalDivider()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransaccionesItem(
    trans: TransactionModelUI,
    isSelected: Boolean,
    onLongClick: (TransactionModelUI?) -> Unit,
    onClickEventItem: (TransactionModelUI, EventItem) -> Unit
) {
    AnimatedVisibility(isSelected) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 16.dp)
                .height(76.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .clickable { onClickEventItem(trans, EventItem.EDIT) }
                    .weight(1f)
                    .padding(vertical = 20.dp),
                painter = painterResource(R.drawable.ic_pencil_outline), contentDescription = ""
            )
            Icon(
                modifier = Modifier
                    .clickable {
                        onClickEventItem(
                            trans.copy(
                                id = "",
                                isBookmark = false,
                                date = null
                            ), EventItem.ADD
                        )
                    }
                    .weight(1f)
                    .padding(vertical = 20.dp),
                imageVector = Icons.Default.ContentCopy, contentDescription = ""
            )
            Icon(
                modifier = Modifier
                    .clickable {
                        onClickEventItem(
                            trans.copy(isBookmark = !trans.isBookmark),
                            EventItem.BOOKMARK
                        )
                    }
                    .weight(1f)
                    .padding(vertical = 20.dp),
                painter = if (trans.isBookmark) painterResource(R.drawable.ic_bookmark)
                else painterResource(R.drawable.ic_bookmark_outline), contentDescription = ""
            )
            Icon(
                modifier = Modifier
                    .clickable { onClickEventItem(trans, EventItem.DELETE) }
                    .weight(1f)
                    .padding(vertical = 20.dp),
                painter = painterResource(R.drawable.ic_delete_outline), contentDescription = ""
            )
            Icon(
                modifier = Modifier
                    .clickable { onLongClick(null) }
                    .weight(1f)
                    .padding(vertical = 20.dp),
                imageVector = Icons.Default.Close, contentDescription = ""
            )
        }
    }
    AnimatedVisibility(!isSelected) {
        Row(
            modifier = Modifier
                .combinedClickable(onLongClick = { onLongClick(trans) }, onClick = {})
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 16.dp)
                .height(76.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .size(44.dp)
                    .background(trans.colorBudget.onContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(trans.icon),
                    contentDescription = ""
                )
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Row {
                    Text(
                        text = "(${trans.category.name})",
                        fontSize = 12.sp,
                        color = trans.category.color,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(Modifier.weight(1f))

                    if (trans.budget == TypeOfTransaction.INCOME) {
                        Text(
                            text = if (trans.amount.toString().length >= 8) "${trans.amount}$" else String.format(
                                "+%.2f$",
                                trans.amount
                            ),
                            fontSize = 16.sp,
                            color = trans.colorBudget.onContainer,
                        )
                    } else {
                        Text(
                            text = if (trans.amount.toString().length >= 8) "${trans.amount}$" else String.format(
                                "%.2f$",
                                trans.amount
                            ),
                            fontSize = 16.sp,
                            color = trans.colorBudget.onContainer,
                        )
                    }
                }

                Text(
                    text = trans.description,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = trans.date.millisToDate(),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,

                    )
            }
            Spacer(Modifier.weight(1f))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransaccionesBookmark(
    trans: TransactionModelUI,
    isSelected: Boolean,
    onLongClick: (TransactionModelUI?) -> Unit,
    onClickEventItem: (TransactionModelUI, EventItem) -> Unit,
) {
    AnimatedVisibility(isSelected) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 16.dp)
                .height(76.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .clickable {
                        onClickEventItem(
                            trans.copy(
                                id = "",
                                isBookmark = false,
                                date = null
                            ), EventItem.ADD
                        )
                    }
                    .weight(1f)
                    .padding(vertical = 20.dp),
                imageVector = Icons.Default.ContentCopy, contentDescription = ""
            )
            Icon(
                modifier = Modifier
                    .clickable {
                        onClickEventItem(
                            trans.copy(isBookmark = !trans.isBookmark),
                            EventItem.BOOKMARK
                        )
                    }
                    .weight(1f)
                    .padding(vertical = 20.dp),
                painter = if (trans.isBookmark) painterResource(R.drawable.ic_bookmark)
                else painterResource(R.drawable.ic_bookmark_outline), contentDescription = ""
            )
            Icon(
                modifier = Modifier
                    .clickable { onLongClick(null) }
                    .weight(1f)
                    .padding(vertical = 20.dp),
                imageVector = Icons.Default.Close, contentDescription = ""
            )
        }
    }
    AnimatedVisibility(!isSelected) {
        Row(
            modifier = Modifier
                .combinedClickable(onLongClick = { onLongClick(trans) }, onClick = {})
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 16.dp)
                .height(76.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .size(44.dp)
                    .background(trans.colorBudget.onContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(trans.icon),
                    contentDescription = ""
                )
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Row {
                    Text(
                        text = "(${trans.category.name})",
                        fontSize = 12.sp,
                        color = trans.colorBudget.onContainer,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(Modifier.weight(1f))

                    if (trans.budget == TypeOfTransaction.INCOME) {
                        Text(
                            text = if (trans.amount.toString().length >= 8) "${trans.amount}$" else String.format(
                                "+%.2f$",
                                trans.amount
                            ),
                            fontSize = 16.sp,
                            color = trans.colorBudget.onContainer,
                        )
                    } else {
                        Text(
                            text = if (trans.amount.toString().length >= 8) "${trans.amount}$" else String.format(
                                "%.2f$",
                                trans.amount
                            ),
                            fontSize = 16.sp,
                            color = trans.colorBudget.onContainer,
                        )
                    }
                }

                Text(
                    text = trans.description,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Spacer(Modifier.weight(1f))
        }
    }

}

@Composable
fun SheetDragHandle() {
    HorizontalDivider(
        modifier = Modifier
            .width(42.dp)
            .padding(top = 16.dp, bottom = 8.dp)
            .clip(CircleShape),
        thickness = 3.dp,
    )
}