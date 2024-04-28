package com.threegroup.tobedated.composeables.searching

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.threegroup.tobedated.R
import com.threegroup.tobedated._dating.DatingViewModel
import com.threegroup.tobedated.composeables.composables.GenericBodyText
import com.threegroup.tobedated.composeables.composables.GenericTitleText
import com.threegroup.tobedated.composeables.composables.SimpleBox
import com.threegroup.tobedated.composeables.composables.TopBarText
import com.threegroup.tobedated.shareclasses.models.AgeRange
import com.threegroup.tobedated.shareclasses.models.UserModel
import com.threegroup.tobedated.theme.AppTheme
import kotlin.math.roundToInt


@Composable
fun SimpleButton(
    modifier: Modifier,
    onClick: () -> Unit,
    colorButton:Color,
    colorText:Color,
    text:String,
    enabled:Boolean = true,
){
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonColors(
            containerColor = colorButton,
            contentColor = colorText,
            disabledContentColor = Color.Gray,
            disabledContainerColor = AppTheme.colorScheme.primary,
        ),
        enabled = enabled
    ) {
        Text(text = text)
    }
}
@Composable
fun SimpleOutLinedButton(
    modifier: Modifier,
    onClick: () -> Unit,
    colorText:Color,
    text:String,
    enabled:Boolean = true,
){
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonColors(
            containerColor = Color.Transparent,
            contentColor = colorText,
            disabledContentColor = Color.Gray,
            disabledContainerColor = AppTheme.colorScheme.primary,
        ),
        contentPadding = PaddingValues(),
        enabled = enabled
    ) {
        Text(text = text)
    }
}
@Composable
fun SearchingButtons(
    onClickLike: () -> Unit,
    onClickPass: () -> Unit,
    onClickReport: () -> Unit,
    onClickSuggest: () -> Unit,
    ){
    Column {
        Row {
            SimpleButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                onClick = onClickPass,
                colorButton = Color.Gray,
                colorText = AppTheme.colorScheme.primary,
                text = "Pass",
            )
            Spacer(modifier = Modifier.width(6.dp))
            SimpleButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                onClick = onClickLike,
                colorButton = AppTheme.colorScheme.primary,
                colorText = AppTheme.colorScheme.onPrimary,
                text = "Like",
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row {
            SimpleOutLinedButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .height(24.dp)
                    .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                onClick = onClickReport,
                colorText = Color.Red,
                text = "Report",
            )
            Spacer(modifier = Modifier.width(6.dp))
            SimpleOutLinedButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .height(24.dp)
                    .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                onClick = onClickSuggest,
                colorText = Color.Green,
                text = "Suggestion",
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsideSearchSettings(
    nav: NavHostController,
    searchSettings: @Composable () -> Unit = {},
) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = if (isSystemInDarkTheme()) Color(0xFF181618) else Color(0xFFCDC2D0),
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    modifier = Modifier.height(46.dp),
                    colors = TopAppBarColors(
                        containerColor = AppTheme.colorScheme.onTertiary,
                        navigationIconContentColor = AppTheme.colorScheme.primary,
                        titleContentColor = AppTheme.colorScheme.secondary,
                        actionIconContentColor = AppTheme.colorScheme.primary,
                        scrolledContainerColor = AppTheme.colorScheme.background
                    ),
                    title = { TopBarText(title= "Search Preferences", isPhoto = false) },
                    navigationIcon = {
                        IconButton(onClick = { nav.popBackStack() }) { //Showing in stuff like messages, editing profile and stuff
                            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.arrow_back), contentDescription = "Go back")
                        }
                    },
                    actions = {}
                )
            },
        ) {
                paddingValues ->
            val state = rememberScrollState()
            LaunchedEffect(Unit) { state.animateScrollTo(0) }
            Column(
                Modifier
                    .padding(paddingValues)
                    .verticalScroll(state)
                    .fillMaxSize()
            ){
                Spacer(modifier = Modifier.height(24.dp))
                searchSettings()
            }
        }
    }
}
@Composable
fun AgeSlider(
    preferredMin: Int,
    preferredMax: Int,
    vmDating: DatingViewModel,
    currentUser: UserModel,
) {
    var sliderPosition by remember { mutableStateOf(preferredMin.toFloat()..preferredMax.toFloat()) }

    // Extracting min and max values from the slider position
    val min = (sliderPosition.start.roundToInt())
    val max = (sliderPosition.endInclusive.roundToInt())

    SimpleBox(
        whatsInsideTheBox = {
            Column(modifier = Modifier.padding(15.dp, 15.dp, 15.dp, 0.dp)) {
                // Displaying the age range
                GenericTitleText(text = "Age Range: $min - $max")
                Spacer(modifier = Modifier.height(4.dp))

                // Range slider for selecting age range
                RangeSlider(
                    value = sliderPosition,
                    steps = 82,
                    onValueChange = { range -> sliderPosition = range },
                    valueRange = 18f..80f,
                    onValueChangeFinished = {
                        currentUser.userPref.ageRange = AgeRange(min, max)
                        vmDating.updateUser(currentUser)
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = AppTheme.colorScheme.primary,
                        activeTrackColor = AppTheme.colorScheme.primary,
                        activeTickColor = Color.Transparent,
                        inactiveTickColor = Color.Transparent,
                        inactiveTrackColor = Color.White,
                    )
                )
            }
        }
    )
}

@Composable
fun DistanceSlider(
    preferredMax: Int,
    vmDating: DatingViewModel,
    currentUser: UserModel,
) {
    var sliderPosition by remember { mutableFloatStateOf(preferredMax.toFloat()) }

    SimpleBox(
        whatsInsideTheBox = {
            Column(modifier = Modifier.padding(15.dp, 15.dp, 15.dp, 0.dp)) {
                val max = sliderPosition.roundToInt()

                // Displaying the maximum distance
                GenericTitleText(text = "Maximum Distance: $max")
                Spacer(modifier = Modifier.height(4.dp))

                // Slider for selecting maximum distance
                Slider(
                    value = sliderPosition,
                    onValueChange = { sliderPosition = it },
                    valueRange = 1f..100f,
                    onValueChangeFinished = {
                        // Update user preferences with the selected maximum distance
                        currentUser.userPref.maxDistance = max
                        // Update user preferences via the view model
                        vmDating.updateUser(currentUser)
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = AppTheme.colorScheme.primary,
                        activeTrackColor = AppTheme.colorScheme.primary,
                        inactiveTrackColor = Color.White,
                        disabledThumbColor = Color.Gray,
                        disabledActiveTrackColor = Color.Gray
                    )
                )
            }
        }
    )
}


@Composable
fun SeekingBox(
    desiredSex:String,
    navController: NavController
    ) {
    val lookingFor by remember { mutableStateOf(desiredSex) }
    SimpleBox(
        whatsInsideTheBox = {
            Column(
                modifier = Modifier
                    .padding(15.dp, 15.dp, 15.dp, 0.dp)
                    .clickable { navController.navigate("ChangePreference/Searching For/69420") }
                    .fillMaxWidth()) {
                GenericTitleText(text = "Looking for:")
                Spacer(modifier = Modifier.height(4.dp))
                GenericBodyText(text = lookingFor)
            }
        }
    )
}



@Composable
fun OtherPreferences(
    title: String,
    navController: NavController,
    searchPref: List<String>,
    clickable: Boolean = false,
    index:Int
) {

    val modifier = if (clickable) {
        Modifier.clickable { navController.navigate("ChangePreference/$title/$index") }
    } else {
        Modifier
    }

    SimpleBox(
        whatsInsideTheBox = {
            Column(
                modifier = Modifier
                    .padding(15.dp, 15.dp, 15.dp, 0.dp)
                    .fillMaxWidth()
                    .then(modifier)
            ) {
                GenericTitleText(text = "$title:")
                Spacer(modifier = Modifier.height(4.dp))
                GenericBodyText(text = searchPref.joinToString(", "))
            }
        }
    )
}

