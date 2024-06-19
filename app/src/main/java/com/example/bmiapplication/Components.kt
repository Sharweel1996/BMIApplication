package com.example.bmiapplication

import android.graphics.drawable.shapes.Shape
import android.icu.text.CaseMap.Title
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.layout.LazyLayoutPinnedItemList
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerDefaults.scrimColor
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableInferredTarget
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bmiapplication.ui.theme.CustomBlue
import com.example.bmiapplication.ui.theme.CustomGray
import com.example.bmiapplication.ui.theme.CustomGreen
import com.example.bmiapplication.ui.theme.CustomOrange
import com.example.bmiapplication.ui.theme.CustomRed
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Preview(showBackground = true)
@Composable
fun UnitItemPreview() {

}

@Composable
fun UnitItem(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = 22.sp
        )
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = "Select Icon"
        )
    }
}

@Composable
fun InputUnitValue(
    inputValue: String,
    inputUnit: String,
    inputColor: Color,
    onUnitValueClicked: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = inputValue,
            fontSize = 40.sp,
            color = inputColor,
            modifier = Modifier.clickable { onUnitValueClicked() },
        )
        Text(
            text = inputUnit,
            fontSize = 12.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun NumberKeyboard(
    modifier: Modifier = Modifier,
    onNumberClick: (String) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        val numberButtonList = listOf(
            "7", "8", "9", "4", "5", "6",
            "1", "2", "3", "", "0", "."
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(3)
        ) {
            items(numberButtonList) { item ->
                NumberButton(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f),
                    number = item,
                    onClick = onNumberClick
                )
            }
        }
    }
}

@Composable
fun NumberButton(
    modifier: Modifier = Modifier,
    number: String,
    onClick: (String) -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(10.dp)
            .clip(CircleShape)
            .clickable { onClick(number) }
    ) {
        Text(text = number, fontSize = 40.sp)
    }
}

@Composable
fun ColumnScope.SymbolButton(
    symbol: String,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(20.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(CustomGray)
            .clickable { onClick() }
            .padding(15.dp)
            .weight(1f)
            .aspectRatio(1f)
    ) {
        Text(text = symbol, fontSize = 20.sp, color = CustomOrange)
    }
}

@Composable
fun ColumnScope.SymbolButtonWithIcon(
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(20.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(CustomGray)
            .clickable { onClick() }
            .padding(15.dp)
            .weight(1f)
            .aspectRatio(1f)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_backspace_24),
            contentDescription = "Delete icon",
            tint = CustomOrange
        )
    }
}

@Composable
fun BottomSheetContent(
    sheetTitle: String,
    sheetItemList: List<String>,
    onItemClicked: (String) -> Unit,
    onCancelClicked: () -> Unit
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        text = sheetTitle,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )

    sheetItemList.forEach { item ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onItemClicked(item) }
        ) {
            Text(
                text = item,
                modifier = Modifier.padding(15.dp)
            )
        }
    }
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        onClick = onCancelClicked,
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.Black,
            containerColor = Color.LightGray
        )
    ) {
        Text(text = "Cancel")
    }
}

@Composable
fun BMIResultCard(
    bmi: Double,
    bmiStage: String,
    bmiStageColor: Color
) {
    val formattedBmi = String.format("%.2f", bmi)
    Column(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(15.dp)
            )
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = formattedBmi,
                fontSize = 70.sp,
                color = CustomOrange
            )

            Spacer(modifier = Modifier.width(15.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "BMI",
                    fontSize = 40.sp,
                    color = Color.Gray
                )
                Text(
                    text = bmiStage,
                    fontSize = 18.sp,
                    color = bmiStageColor
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Divider(
            modifier = Modifier
                .background(Color.Gray)
                .shadow(elevation = 5.dp),
            thickness = 5.dp
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Information",
            textAlign = TextAlign.Center,
            fontSize = 22.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.SpaceAround
        ) {
            Text(text = "Underweight", color = CustomBlue)
            Text(text = "Normal", color = CustomGreen)
            Text(text = "Overweight", color = CustomRed)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Divider(
                modifier = Modifier
                    .weight(1f)
                    .height(5.dp),
                thickness = 5.dp,
                color = CustomBlue
            )
            Divider(
                modifier = Modifier
                    .weight(1f)
                    .height(5.dp),
                thickness = 5.dp,
                color = CustomGreen
            )
            Divider(
                modifier = Modifier
                    .weight(1f)
                    .height(5.dp),
                thickness = 5.dp,
                color = CustomRed
            )

        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            Text(text = "16.0", fontSize = 18.sp, color = Color.Gray)
            Text(text = "18.5", fontSize = 18.sp, color = Color.Gray)
            Text(text = "25.0", fontSize = 18.sp, color = Color.Gray)
            Text(text = "40.0", fontSize = 18.sp, color = Color.Gray)
        }
    }
}

@Composable
fun ShareButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = CustomOrange,
            contentColor = Color.Black
        )
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(6.dp)
        )
    }
}

fun calculateBMI(weight: Double, height: Double, weightUnit: String, heightUnit: String): Double {
    val weightInKg = when (weightUnit) {
        "Pounds" -> weight * 0.453592
        else -> weight
    }
    val heightInMeters = when (heightUnit) {
        "Centimeter" -> height / 100
        "Feet" -> height * 0.3048
        "Inch" -> height * 0.0254
        else -> height
    }
    return weightInKg / (heightInMeters * heightInMeters)
}

fun getBMICategory(bmi: Double): Pair<String, Color> {
    return when {
        bmi < 18.5 -> "Underweight" to CustomBlue
        bmi < 25.0 -> "Normal" to CustomGreen
        bmi < 40.0 -> "Overweight" to CustomRed
        else -> "Obese" to Color.Red
    }
}