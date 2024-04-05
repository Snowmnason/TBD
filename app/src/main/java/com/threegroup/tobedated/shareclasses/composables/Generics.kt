package com.threegroup.tobedated.shareclasses.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.threegroup.tobedated.shareclasses.theme.AppTheme
import com.threegroup.tobedated.shareclasses.theme.JoseFinSans
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
fun baseAppTextTheme(): TextStyle {
    return TextStyle(
        color = AppTheme.colorScheme.onBackground,
        fontFamily = JoseFinSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
}
@Composable
fun GenericTitleText(
    text:String,
    modifier: Modifier = Modifier,
    color: Color = AppTheme.colorScheme.onBackground,
    style: TextStyle = AppTheme.typography.titleSmall
){
    Text(modifier = modifier,
        text = text,
        style = style,
        color = color,
    )
}
@Composable
fun GenericBodyText(
    text:String,
    modifier: Modifier = Modifier,
    color: Color = AppTheme.colorScheme.onBackground,
    style: TextStyle = AppTheme.typography.body
){
    Text(
        modifier= modifier,
        text = text,
        style = AppTheme.typography.body,
        color = color)
}
@Composable
fun GenericLabelText(
    text:String,
    modifier: Modifier = Modifier,
    color: Color = AppTheme.colorScheme.onBackground,
    style:TextStyle = AppTheme.typography.labelSmall
){
    Text(
        modifier = modifier,
        text = text,
        style = style,
        color = color)
}
@Composable
fun PlainTextButton(
    onClick : () -> Unit,
    question:String,
    enabled:Boolean = true
){
    Button(
        shape = RoundedCornerShape(4.dp),
        onClick = onClick ,
        contentPadding = PaddingValues(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AppTheme.colorScheme.surface,
            contentColor = AppTheme.colorScheme.onSurface,
            disabledContainerColor = Color.Gray
        ),
        enabled = enabled
    ){
        Text(text = question, style = AppTheme.typography.body)
    }
}

@Composable
fun GenTextOnlyButton(
    color:Color,
    text: String,
    onClick: () -> Unit
){
    Button(
        onClick = onClick,
        colors = ButtonColors(
            contentColor = color,
            disabledContentColor = color,
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        )
    ) {
       Text(text = text,
           style = AppTheme.typography.titleLarge)
    }

}

@Composable
fun AlertDialogBox(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
) {
    AlertDialog(
        containerColor = AppTheme.colorScheme.surface,
        title = {
            Text(text = dialogTitle, style = AppTheme.typography.titleLarge, color = AppTheme.colorScheme.onSurface)
        },
        text = {
            Text(text = dialogText, style = AppTheme.typography.body, color = AppTheme.colorScheme.onSurface)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(

                onClick = {
                    onConfirmation()
                }
            ) {
                Text(text = "Confirm", color = AppTheme.colorScheme.secondary)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(text = "Dismiss", color = AppTheme.colorScheme.secondary)
            }
        }
    )
}

@Composable
fun DialogWithImage(
    onDismissRequest: () -> Unit,
    painter: Painter,
    imageDescription: String,
    body:String
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        // Draw a rectangle shape with rounded corners inside the dialog
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(525.dp)
                .background(AppTheme.colorScheme.surface)
                .padding(4.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Surface(
                color = AppTheme.colorScheme.surface,
                shape = RoundedCornerShape(10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        painter = painter,
                        contentDescription = imageDescription,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .height(240.dp)
                    )
                    Text(
                        text = body,
                        style = AppTheme.typography.bodySmall,
                        color = AppTheme.colorScheme.secondary,
                        modifier = Modifier.padding(8.dp),
                        textAlign = TextAlign.Center
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        TextButton(
                            onClick = { onDismissRequest() },
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Text(text = "Confirm", color = AppTheme.colorScheme.secondary)
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun ProgressBar(
    questionIndex:Int,
    totalQuestionCount:Int,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val progress by animateFloatAsState(
            targetValue = (questionIndex) / totalQuestionCount.toFloat(), label = ""
        )
        LinearProgressIndicator(
            progress = {
                progress
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
        )
    }
}

@Composable
fun RadioButtonGroup(
    options: List<String>,
    selectedIndex: Int,
    onSelectionChange: (Int) -> Unit,
    style: TextStyle,
) {
    Column {
        options.forEachIndexed { index, text ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectionChange(index) }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = index == selectedIndex,
                    onClick = null, // onClick handled by Row
                    colors = RadioButtonDefaults.colors(
                        selectedColor = AppTheme.colorScheme.primary,
                        unselectedColor = AppTheme.colorScheme.onBackground
                    )
                )
                Text(
                    text = text,
                    style = style,
                    color = AppTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}
@Composable
fun BasicPicker(
    values:List<String>,// (1..99).map { it.toString() } }
    valuesPickerState: PickerState,
    style: TextStyle = AppTheme.typography.titleSmall,
    start:Int = 0,
    visible:Int = 3,
) {
    Surface(modifier = Modifier.fillMaxSize(), color = Color.Transparent) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Picker(
                    state = valuesPickerState,
                    items = values,
                    visibleItemsCount = visible,
                    modifier = Modifier.weight(0.3f),
                    startIndex = start,
                    textModifier = Modifier.padding(8.dp),
                    textStyle = style
                )
            }

        }
    }
}
@Composable
fun rememberPickerState() = remember { PickerState() }

class PickerState {
    var selectedItem by mutableStateOf("")
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Picker(
    modifier: Modifier = Modifier,
    items: List<String>,
    state: PickerState = rememberPickerState(),
    startIndex: Int = 0,
    visibleItemsCount: Int = 3,
    textModifier: Modifier = Modifier,
    textStyle: TextStyle = AppTheme.typography.titleMedium,
    dividerColor: Color = AppTheme.colorScheme.primary,
    textColor:Color = AppTheme.colorScheme.onBackground
) {

    val visibleItemsMiddle = visibleItemsCount / 2
    val listScrollCount = items.size+2
    val listScrollMiddle = listScrollCount / 2
    val listStartIndex = listScrollMiddle - listScrollMiddle % items.size - visibleItemsMiddle + startIndex

    fun getItem(index: Int) = items[index % items.size]

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = listStartIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    val itemHeightPixels = remember { mutableIntStateOf(0) }
    val itemHeightDp = pixelsToDp(itemHeightPixels.intValue)

    val fadingEdgeGradient = remember {
        Brush.verticalGradient(
            0f to Color.Transparent,
            0.5f to Color.Black,
            1f to Color.Transparent
        )
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .map { index -> getItem(index + visibleItemsMiddle) }
            .distinctUntilChanged()
            .collect { item -> state.selectedItem = item }
    }

    Box(modifier = modifier) {

        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeightDp * visibleItemsCount)
                .fadingEdge(fadingEdgeGradient)
        ) {
            items(listScrollCount) { index ->
                Text(
                    text = getItem(index),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = textStyle,
                    color = textColor,
                    modifier = Modifier
                        .onSizeChanged { size -> itemHeightPixels.intValue = size.height }
                        .then(textModifier)
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.offset(y = itemHeightDp * visibleItemsMiddle),
            color = dividerColor
        )

        HorizontalDivider(
            modifier = Modifier.offset(y = itemHeightDp * (visibleItemsMiddle + 1)),
            color = dividerColor
        )

    }

}

private fun Modifier.fadingEdge(brush: Brush) = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(brush = brush, blendMode = BlendMode.DstIn)
    }

@Composable
private fun pixelsToDp(pixels: Int) = with(LocalDensity.current) { pixels.toDp() }