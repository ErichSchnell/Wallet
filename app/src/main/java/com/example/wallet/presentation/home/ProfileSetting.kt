package com.example.wallet.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wallet.R
import com.example.wallet.presentation.home.profile.DialogAddCategory
import com.example.wallet.presentation.home.profile.DialogDeleteCategory
import com.example.wallet.presentation.model.CategoryUi
import com.example.wallet.presentation.model.ProfileModelUi
import com.example.wallet.presentation.util.BackgroundScreen

@Composable
fun ProfileSetting(
    profileSelected: ProfileModelUi? = null,
    topPadding: Dp,
    onClickEditCategory: (CategoryUi) -> Unit,
    onClickDeleteCategory: (CategoryUi) -> Unit,
) {
    var categorySelected by remember { mutableStateOf<CategoryUi?>(null) }
    var showDialogEdit by remember { mutableStateOf(false) }
    var showDialogDelete by remember { mutableStateOf(false) }

    BackgroundScreen(Screens.SETTING.bg)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = topPadding)
            .padding(16.dp)
    ) {
        Spacer(Modifier.height(24.dp))

        TitleSetting(modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(Modifier.height(32.dp))

        Row (modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TitleCategories()
            Spacer(Modifier.weight(1f))
            if (categorySelected != null) {
                IconButton(onClick = { showDialogEdit = true}) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "")
                }

                IconButton(onClick = { showDialogDelete = true }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "")
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Categories(profileSelected?.categories ?: emptyList(), categorySelected) { categorySelected = it }
    }

    DialogAddCategory(
        category = categorySelected,
        showAddCategory = showDialogEdit,
        onDismissRequest = { showDialogEdit = false},
        onClickAddCategory = {
            showDialogEdit = false
            onClickEditCategory(it)
                             },
    )

    DialogDeleteCategory(
        category = categorySelected,
        showDialog = showDialogDelete,
        onDismissRequest = { showDialogDelete = false},
        onClickAccept = {
            showDialogDelete = false
            onClickDeleteCategory(categorySelected ?: CategoryUi())
                        },
    )


}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Categories(
    categories: List<CategoryUi>,
    categorySelected: CategoryUi?,
    onClickCategory: (CategoryUi?) -> Unit
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        categories.let {
            it.forEach { category ->
                ItemWallet(
                    selected = categorySelected == category,
                    containerColor = category.color,
                    onClick = {
                        if (categorySelected == category) onClickCategory(null)
                        else onClickCategory(category)

                    }
                ) {
                    Text(
                        text = category.name,
                        color = if (categorySelected == category) Color.Black
                        else MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

@Composable
fun TitleSetting(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = stringResource(R.string.title_setting),
        fontSize = 34.sp
    )
    Spacer(Modifier.height(12.dp))
    HorizontalDivider(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp)
    )
}

@Composable
fun TitleCategories(modifier: Modifier = Modifier) {
    Column {
        Text(
            modifier = modifier,
            text = stringResource(R.string.title_categories),
            fontSize = 22.sp
        )

    }

}


@Composable
fun ItemWallet(
    selected: Boolean,
    containerColor: Color,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .border(1.dp, containerColor, CircleShape)
            .background(if (selected) containerColor else Color.Transparent)
            .clickable { onClick() }
            .padding(8.dp)
            .wrapContentHeight()
            .wrapContentWidth(),
        contentAlignment = Alignment.BottomEnd
    ) { content() }
}