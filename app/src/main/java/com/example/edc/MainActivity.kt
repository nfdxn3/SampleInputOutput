package com.example.edc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.edc.ui.theme.EDCTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EDCTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EDCLayout()
                }
            }
        }
    }
}

@Composable
fun EDCLayout() {
    var loadPowerInput by remember { mutableStateOf("") }
    var sourceVoltageInput by remember { mutableStateOf("") }
    var threePhase by remember { mutableStateOf(false) }

    val loadP = loadPowerInput.toDoubleOrNull() ?: 0.0
    val sourceV = sourceVoltageInput.toDoubleOrNull() ?: 0.0
    val loadC = calculateLoad(loadP, sourceV, threePhase)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(40.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Image(
            painter = painterResource(R.drawable._x4_var2_col3_circ),
            contentDescription = null,
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
                .padding(50.dp)
        )
        Text(
            text = stringResource(R.string.calculate_load),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(alignment = Alignment.Start)
        )
        EditNumberField(
            label = R.string.load_power,

            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            valueEDC = loadPowerInput,
            onValueChanged = {loadPowerInput = it},
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        EditNumberField(
            label = R.string.source_voltage,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            valueEDC = sourceVoltageInput,
            onValueChanged = {sourceVoltageInput = it},
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        PhaseSelection(
            threePhase = threePhase,
            onthreePhaseChange = {threePhase = it},
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        Text(
            text = stringResource(R.string.load_current, loadC),
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.height(150.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNumberField(
    @StringRes label: Int,
    keyboardOptions: KeyboardOptions,

    valueEDC: String, // Apparent Power
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        label = { Text(stringResource(label))},
        leadingIcon = { Icon(imageVector = Icons.Default.Build, null)},
        colors = TextFieldDefaults.textFieldColors(
            unfocusedLabelColor = Color.Black,
            containerColor = Color.Green.copy(alpha = 0.25f,)
        ),
        keyboardOptions = keyboardOptions,
        singleLine = true,

        value = valueEDC,
        onValueChange = onValueChanged,
        modifier = modifier
    )
}

@Composable
fun PhaseSelection(
    threePhase: Boolean,
    onthreePhaseChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .size(48.dp)
    ) {
        Text(text = stringResource(R.string.three_phase))
        Switch(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End),
            checked = threePhase,
            onCheckedChange = onthreePhaseChange
        )
    }
}

private fun calculateLoad(
    loadP: Double = 0.0,
    sourceV: Double = 230.0,
    threePhase: Boolean,
    ): String {
    var loadC = loadP / (sourceV)
    if (threePhase) {
        loadC = loadP / (sourceV * 1.732050808)
    }

    return NumberFormat.getNumberInstance().format(loadC)
}

@Preview(showBackground = true)
@Composable
fun EDCPreview() {
    EDCTheme {
        EDCLayout()
    }
}