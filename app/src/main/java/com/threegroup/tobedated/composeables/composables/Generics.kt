package com.threegroup.tobedated.composeables.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarColors
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.threegroup.tobedated.MyApp
import com.threegroup.tobedated.R
import com.threegroup.tobedated.composeables.searching.SeekingUserInfo
import com.threegroup.tobedated.shareclasses.api.ApiViewModel
import com.threegroup.tobedated.shareclasses.models.MatchedUserModel
import com.threegroup.tobedated.shareclasses.models.getStarSymbol
import com.threegroup.tobedated.theme.AppTheme
import com.threegroup.tobedated.theme.JoseFinSans
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
fun getAddShadow(style: TextStyle, type: String): TextStyle {
    val displace = when (type){
        "med" -> 8f
        "body" -> 4f
        "label" -> 4f
        else -> 8f
    }
    return style.copy(
        shadow = Shadow(
            color = AppTheme.colorScheme.primary.copy(alpha = 0.75f),
            offset = Offset(displace, displace),
            blurRadius = displace
        )
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun getTopColors(): TopAppBarColors {
    return TopAppBarColors(
        containerColor = AppTheme.colorScheme.onTertiary,
        navigationIconContentColor = AppTheme.colorScheme.primary,
        titleContentColor = AppTheme.colorScheme.secondary,
        actionIconContentColor = AppTheme.colorScheme.primary,
        scrolledContainerColor = AppTheme.colorScheme.background
    )
}
@Composable
fun getBottomColors(): NavigationBarItemColors {
    return NavigationBarItemColors(
        selectedIconColor = AppTheme.colorScheme.primary,
        selectedTextColor = AppTheme.colorScheme.primary,
        selectedIndicatorColor = Color.Transparent,
        unselectedIconColor = AppTheme.colorScheme.onBackground,
        unselectedTextColor = AppTheme.colorScheme.onBackground,
        disabledIconColor = Color.Black,
        disabledTextColor = Color.Black
    )
}
@Composable
fun TopBarText(
    title:String,
    size: TextStyle = AppTheme.typography.titleLarge,
    isPhoto:Boolean,
    activity:String = "dating"
){
    if(isPhoto){
        val photo = if (isSystemInDarkTheme()) painterResource(
            id = when(activity){
                "dating" -> R.drawable.tbd_dark
                "friends" -> R.drawable.tbf_dark
                "causal" -> R.drawable.tbc_dark
                else -> R.drawable.tbd_dark
            }
        ) else painterResource(
            id = when(activity){
                "dating" -> R.drawable.tbd_light
                "friends" -> R.drawable.tbf_light
                "causal" -> R.drawable.tbc_light
                else -> R.drawable.tbd_light
            }
        )
        Box(modifier = Modifier
            .height(30.dp)
            .offset(y = (8).dp)){
            Image(painter = photo, contentDescription = "Logo")
        }
    }else{
        Text(
            modifier = Modifier.offset(y = (16).dp),
            text = title,
            style = size,
            color = AppTheme.colorScheme.onBackground,
        )
    }
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
fun OutLinedButton(
    onClick: () -> Unit,
    textColor:Color = AppTheme.colorScheme.onBackground,
    outLineColor:Color = AppTheme.colorScheme.primary,
    text:String,
){
    OutlinedButton(
        onClick = onClick,
        border = BorderStroke(2.dp , outLineColor)
    ) {
        GenericTitleText(text = text, color = textColor)
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
fun AlertDialogBoxWTextField(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    number:String,
    numberChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
) {
    AlertDialog(
        containerColor = AppTheme.colorScheme.surface,
        title = {
            Text(text = dialogTitle, style = AppTheme.typography.titleLarge, color = AppTheme.colorScheme.onSurface)
        },
        text = {
            Column {
                Text(text = dialogText, style = AppTheme.typography.body, color = AppTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(value = number,
                    onValueChange = numberChange,
                    singleLine = true,
                    keyboardOptions = keyboardOptions

                )
            }

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
    textStyle: TextStyle = getAddShadow(style = AppTheme.typography.titleMedium, "med"),
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
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerIndicator(
    pagerState: PagerState,
    pageCount: Int,
    modifier: Modifier = Modifier,
    pageIndexMapping: (Int) -> Int = { it },
    activeColor: Color = AppTheme.colorScheme.primary,
    inactiveColor: Color = AppTheme.colorScheme.onBackground,
    indicatorWidth: Dp = 8.dp,
    indicatorHeight: Dp = indicatorWidth,
    spacing: Dp = indicatorWidth,
    indicatorShape: Shape = CircleShape,
) {

//    val indicatorWidthPx = with(LocalDensity.current) { indicatorWidth.roundToPx() }
//    val spacingPx = with(LocalDensity.current) { spacing.roundToPx() }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(pageCount) { index ->
                val isActive = index == pagerState.currentPage
                val color = if (isActive) activeColor else inactiveColor
                Box(
                    modifier = Modifier
                        .size(width = indicatorWidth, height = indicatorHeight)
                        .clip(indicatorShape)
                        .background(color = color)
                )
            }
        }
    }
}
@Composable
fun Comeback(
    text: String
){
    SeekingUserInfo(
        user = MatchedUserModel(
            name = text,
            birthday = "01/01/2024",
            seeMe = false,
            pronoun = "pronoun",
            gender = "loading",
            height = "6'9",
            ethnicity = "ethnicity",
            star = "Cancer",
            sexOrientation = "Orientation",
            sex = "other",
            testResultsMbti = "INTP",
            children = "children",
            family = "family",
            education = "education",
            religion = "religion",
            politics = "politics",
            relationship = "relationship",
            intentions = "intentions",
            drink = "drink",
            smoke = "smoke",
            weed = "weed",
            meetUp = "meetUp",
            promptQ1 = "promptQ1",
            promptA1 = "promptA1",
            promptQ2 = "promptQ2",
            promptA2 = "promptA2",
            promptQ3 = "promptQ3",
            promptA3 = "promptA3",
            bio = "bio",
            location = "",
            image1 = "",
            image2 = "",
            image3 = "",
            image4 = "",
            number = "",
            verified = true,

        ),//usersArray[currentProfileIndex]
        location = "Not Here",
    )
}
@Composable
fun NavDraw(
    colorDating:Color = AppTheme.colorScheme.onBackground,
    colorFriends:Color = AppTheme.colorScheme.onBackground,
    colorCausal:Color = AppTheme.colorScheme.onBackground,
    datingClickable: () -> Unit = {},
    causalClickable: () -> Unit = {},
    friendsClickable: () -> Unit = {},
    star:String
    
){

    Column(
        modifier = Modifier.padding(25.dp)
    ) {

        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable { datingClickable() }
        ){
            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.datingsec), contentDescription = "dating", tint = colorDating, modifier = Modifier
                .offset(y = (-4).dp))
            Spacer(modifier = Modifier.width(8.dp))
            GenericTitleText(text = "Dating", style = AppTheme.typography.titleLarge)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable { friendsClickable() }
        ){
            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.friendsce), contentDescription = "friends", tint = colorFriends, modifier = Modifier
                .offset(y = (-4).dp))
            Spacer(modifier = Modifier.width(8.dp))
            GenericTitleText(text = "Friends", style = AppTheme.typography.titleLarge)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable {
                causalClickable()
            }
        ){
            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.causalsce), contentDescription = "causal", tint = colorCausal, modifier = Modifier
                .offset(y = (-4).dp))
            Spacer(modifier = Modifier.width(8.dp))
            GenericTitleText(text = "Causal", style = AppTheme.typography.titleLarge)
        }
        val vmApi = viewModel { ApiViewModel(MyApp.x) }
        vmApi.fetchWordOfTheDay()
        vmApi.fetchHoroscope(star)
        var description by remember { mutableStateOf("") }
        var luckyTime by remember { mutableStateOf("") }
        var luckyNumber by remember { mutableStateOf("") }
        var mood by remember { mutableStateOf("") }
        description = vmApi.getDescription()
        luckyTime = vmApi.getTime(star)
        luckyNumber = vmApi.getLuckyNumber(star)
        mood = vmApi.getMood(star)

        Spacer(modifier = Modifier.height(24.dp))
        Column {
            Column {
                GenericTitleText(text = "Word of the Day", style = getAddShadow(AppTheme.typography.titleMedium, "med"))
                Spacer(modifier = Modifier.height(2.dp))
                HorizontalDivider(modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    GenericTitleText(text = vmApi.getWord(), style = AppTheme.typography.titleLarge)
                }
                Spacer(modifier = Modifier.height(3.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically){
                        GenericBodyText(text = vmApi.getPartOfSpeech())
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        GenericBodyText(text = vmApi.getSource())
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                GenericBodyText(text = vmApi.getDef())
            }

            Spacer(modifier = Modifier.height(16.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 20.dp)){
                    GenericTitleText(text = "Horoscope", style = getAddShadow(AppTheme.typography.titleMedium, "med"))
                    Icon(imageVector = getStarSymbol(star), contentDescription = star, tint = AppTheme.colorScheme.onBackground)
                }

                Spacer(modifier = Modifier.height(2.dp))
                HorizontalDivider(modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp, 0.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically){
                        GenericLabelText(text = "Date: ")
                        GenericBodyText(text = vmApi.getDate())
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        GenericLabelText(text = "Lucky Time: ")
                        GenericBodyText(text = luckyTime)
                    }
                }
                Spacer(modifier = Modifier.height(3.dp))
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp, 0.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically){
                        GenericLabelText(text = "Lucky Number: ")
                        GenericBodyText(text = luckyNumber)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically){
                        GenericLabelText(text = "Mood: ")
                        GenericBodyText(text = mood)
                    }
                }
//                Spacer(modifier = Modifier.height(3.dp))
//                Row(modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(4.dp, 0.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.Start
//                ) {
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        GenericLabelText(text = "Compatibility: ")
//                        GenericBodyText(text = vmApi.getComp())
//                    }
//                }
                Spacer(modifier = Modifier.height(5.dp))
                GenericBodyText(text = description)
            }
        }
    }
}

