package com.example.bmiapplication

import android.widget.Toast
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bmiapplication.ui.theme.CustomGreen
import com.example.bmiapplication.ui.theme.CustomOrange
import com.example.bmiapplication.ui.theme.GrayBackground

@Preview
@Composable
fun BMIScreenPreview() {
    BMIScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BMIScreen() {

    val modalBottomSheetState = rememberModalBottomSheetState()

    val listOfInputWeight = rememberSaveable { mutableStateOf(listOf("Kilogram", "Pounds")) }
    val setSelectedWeight = rememberSaveable { mutableStateOf(listOfInputWeight.value.first()) }
    val listOfInputHeight =
        rememberSaveable { mutableStateOf(listOf("Centimeter", "Feet", "Meter", "Inch")) }
    val setSelectedHeight = rememberSaveable { mutableStateOf(listOfInputHeight.value.first()) }

    val openBottomDialogForWeight = rememberSaveable { mutableStateOf(false) }
    val bottomSheetType = rememberSaveable { mutableStateOf("0") } // 0= wight,1= Height

    val weightInputValue = remember { mutableStateOf("0") }
    val heightInputValue = remember { mutableStateOf("0") }
    val openWeight = rememberSaveable { mutableStateOf(false) }
    val openBMICardShow = rememberSaveable { mutableStateOf(false) }

    val bmiValue = rememberSaveable { mutableDoubleStateOf(0.0) }
    val bmiCategory = rememberSaveable { mutableStateOf("Normal") }
    val bmiCategoryColor = remember { mutableStateOf(CustomGreen) }
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Crossfade(targetState = openBMICardShow.value, label = "") { isBMICardVisible ->
            if (isBMICardVisible) {
                Column(modifier = Modifier.padding(20.dp)) {
                    BMIResultCard(
                        bmi = bmiValue.doubleValue,
                        bmiStage = bmiCategory.value,
                        bmiStageColor = bmiCategoryColor.value
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    ShareButton(
                        text = "Share", modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Toast.makeText(context, "Shared Successfully..!!", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                ScreenContent(
                    onWeightTextClicked = {
                        bottomSheetType.value = "0"
                        openBottomDialogForWeight.value = true
                    },
                    onHeightTextClicked = {
                        bottomSheetType.value = "1"
                        openBottomDialogForWeight.value = true
                    },
                    weightInput = setSelectedWeight.value,
                    weightValue = weightInputValue.value,
                    heightInput = setSelectedHeight.value,
                    heightValue = heightInputValue.value,
                    onNumberClickValueChanged = { newValue ->
//                if (openWeight.value) { weightInputValue.value = weightInputValue.value + newValue }
                        if (openWeight.value) {
                            weightInputValue.value =
                                if (weightInputValue.value == "0") newValue else weightInputValue.value + newValue
                        } else {
                            heightInputValue.value =
                                if (heightInputValue.value == "0") newValue else heightInputValue.value + newValue
                        }
                    },
                    onWeightInputValueOnClick = {
                        openWeight.value = true
                        openBMICardShow.value = false
                    },
                    onHeightInputValueOnClick = {
                        openWeight.value = false
                        openBMICardShow.value = false
                    },
                    allClearButtonClicked = {
                        weightInputValue.value = "0"
                        heightInputValue.value = "0"
                    },
                    deleteButtonClicked = {
                        if (openWeight.value) {
                            if (weightInputValue.value.isNotEmpty()) {
                                weightInputValue.value = weightInputValue.value.dropLast(1)
                                if (weightInputValue.value.isEmpty()) {
                                    weightInputValue.value = "0"
                                }
                            }
                        } else {
                            if (heightInputValue.value.isNotEmpty()) {
                                heightInputValue.value = heightInputValue.value.dropLast(1)
                                if (heightInputValue.value.isEmpty()) {
                                    heightInputValue.value = "0"
                                }
                            }
                        }
                    },
                    goButtonClicked = {
                        val weight = weightInputValue.value.toDoubleOrNull() ?: 0.0
                        val height = heightInputValue.value.toDoubleOrNull() ?: 0.0
                        val bmi =
                            calculateBMI(
                                weight,
                                height,
                                setSelectedWeight.value,
                                setSelectedHeight.value
                            )
                        val formattedBmi = String.format("%.2f", bmi).toDouble()
                        val (category, color) = getBMICategory(formattedBmi)
                        bmiValue.doubleValue = formattedBmi
                        bmiCategory.value = category
                        bmiCategoryColor.value = color
                        openBMICardShow.value = true
                    }
                )
            }
        }

        if (openBottomDialogForWeight.value) {
            ModalBottomSheet(sheetState = modalBottomSheetState, content = {
                BottomSheetContent(sheetTitle = if (bottomSheetType.value == "0") "Width" else "Height",
                    sheetItemList = if (bottomSheetType.value == "0") listOfInputWeight.value else listOfInputHeight.value,
                    onItemClicked = {
                        if (bottomSheetType.value == "0") {
                            setSelectedWeight.value = it
                        } else {
                            setSelectedHeight.value = it
                        }
                    },
                    onCancelClicked = {
                        openBottomDialogForWeight.value = false
                    })
            }, onDismissRequest = { openBottomDialogForWeight.value = false })
        }
    }
}

@Composable
fun ScreenContent(
    onWeightTextClicked: () -> Unit,
    onHeightTextClicked: () -> Unit,
    weightInput: String,
    weightValue: String,
    heightInput: String,
    heightValue: String,
    onNumberClickValueChanged: (String) -> Unit,
    onWeightInputValueOnClick: () -> Unit,
    onHeightInputValueOnClick: () -> Unit,
    allClearButtonClicked: () -> Unit,
    deleteButtonClicked: () -> Unit,
    goButtonClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GrayBackground)
            .padding(15.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "BMI Application",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 28.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(40.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                UnitItem(text = "Weight", onClick = onWeightTextClicked)
                InputUnitValue(inputValue = weightValue,
                    inputUnit = weightInput,
                    inputColor = CustomOrange,
                    onUnitValueClicked = {
                        onWeightInputValueOnClick()
                    })
            }

            Spacer(modifier = Modifier.height(40.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                UnitItem(text = "Height", onClick = onHeightTextClicked)
                InputUnitValue(inputValue = heightValue,
                    inputUnit = heightInput,
                    inputColor = CustomOrange,
                    onUnitValueClicked = {
                        onHeightInputValueOnClick()
                    })
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            Divider()
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                NumberKeyboard(
                    modifier = Modifier.weight(7f),
                    onNumberClick = { number ->
                        onNumberClickValueChanged(number)
                    }
                )
                Column(
                    modifier = Modifier.weight(3f),
                ) {
                    SymbolButton(symbol = "AC", onClick = { allClearButtonClicked() })
                    SymbolButtonWithIcon(onClick = { deleteButtonClicked() })
                    SymbolButton(symbol = "GO", onClick = { goButtonClicked() })
                }
            }
        }
    }
}
