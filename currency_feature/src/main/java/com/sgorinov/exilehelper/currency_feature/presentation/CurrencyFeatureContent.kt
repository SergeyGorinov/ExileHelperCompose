package com.sgorinov.exilehelper.currency_feature.presentation

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.sgorinov.exilehelper.core.R
import org.koin.androidx.compose.getViewModel

@Composable
fun Groups() {
    val viewModel = getViewModel<CurrencyFeatureViewModel>()
    viewModel.requestStaticData()
    viewModel.requestFilterData()
    val data by viewModel.staticDataState.collectAsState()
    LazyColumn(
        modifier = Modifier
            .padding(top = 8.dp)
            .background(color = colorResource(R.color.primaryDarkColor))
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        items(
            items = data,
            itemContent = {
                GroupItem(it.entries.firstOrNull()?.imageUrl, it.label)
            }
        )
    }
}

@Composable
fun GroupItem(imageUrl: String?, text: String?) {
    Card(
        modifier = Modifier.padding(1.dp),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(
            width = 1.dp,
            color = colorResource(id = R.color.primaryLightColor)
        ),
        backgroundColor = colorResource(id = R.color.primaryColor)
    ) {
        Row(modifier = Modifier
            .padding(4.dp)
            .clickable {}
        ) {
            if (imageUrl != null) {
                NetworkImage(url = imageUrl)
            }
            Text(
                text = text ?: "",
                fontFamily = FontFamily(
                    Font(
                        R.font.fontinsmallcaps,
                        weight = FontWeight.Bold
                    )
                ),
                color = colorResource(id = R.color.primaryTextColor),
                modifier = Modifier
                    .padding(start = 8.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
fun NetworkImage(url: String) {
    val painter = rememberImagePainter(data = url)
    Box {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .align(Alignment.Center)
        )
        if (painter.state is ImagePainter.State.Loading) {
            CircularProgressIndicator(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
fun CurrencyFeatureContent() {
    val viewModel = getViewModel<CurrencyFeatureViewModel>()
    viewModel.requestStaticData()
    val data by viewModel.staticDataState.collectAsState()
    val itemSize = 48.dp
    val borderSize = 1.dp
    LazyGrid(
        modifier = Modifier
            .background(color = colorResource(id = R.color.primaryDarkColor))
            .fillMaxWidth()
            .padding(8.dp),
        horizontalSpacing = 8.dp,
        verticalSpacing = 8.dp,
        itemMinSize = itemSize,
        itemBorderSize = borderSize,
        data = data
    ) {
        val imageUrl = it.entries.firstOrNull()?.imageUrl
        Card(
            modifier = Modifier
                .border(
                    border = BorderStroke(
                        width = borderSize,
                        color = colorResource(id = R.color.primaryLightColor)
                    ),
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(1.dp)
                .size(itemSize),
            backgroundColor = colorResource(id = R.color.primaryColor)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                if (imageUrl != null) {
                    NetworkImage(url = imageUrl)
                } else {
                    Text(
                        text = "T",
                        color = colorResource(id = R.color.primaryTextColor),
                        fontFamily = FontFamily(Font(R.font.fontinsmallcaps)),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun <T> LazyGrid(
    modifier: Modifier,
    horizontalSpacing: Dp,
    verticalSpacing: Dp,
    itemMinSize: Dp,
    itemBorderSize: Dp = 0.dp,
    data: List<T>,
    content: @Composable (T) -> Unit
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val totalItemMinSize = itemMinSize + horizontalSpacing + (itemBorderSize * 2)
        val columns = maxOf((maxWidth / totalItemMinSize).toInt(), 1)
        val rows = (data.size + columns - 1) / columns
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(horizontalSpacing),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(rows) { rowIndex ->
                LazyRow(horizontalArrangement = Arrangement.spacedBy(verticalSpacing)) {
                    val startIndex = rowIndex * columns
                    val rowData = if (startIndex + columns < data.size) {
                        data.subList(startIndex, startIndex + columns)
                    } else {
                        data.subList(startIndex, startIndex + (data.size - startIndex))
                    }
                    items(rowData) {
                        content(it)
                    }
                }
            }
        }
    }
}