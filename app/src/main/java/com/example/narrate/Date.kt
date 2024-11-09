package com.example.narrate

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CenteredDatePicker(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val days = (1..31).toList()
    val months = (1..12).toList()
    val years = (1900..2030).toList()

    var selectedDay by remember { mutableIntStateOf(selectedDate.dayOfMonth) }
    var selectedMonth by remember { mutableIntStateOf(selectedDate.monthValue) }
    var selectedYear by remember { mutableIntStateOf(selectedDate.year) }

    // Container for the date picker
    Box(
        modifier = Modifier
            .size(500.dp, 200.dp)
            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Display current date centered
            Text(
                text = selectedDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                // Days Column
                ScrollableColumn(
                    items = days,
                    selectedItem = selectedDay,
                    onItemSelected = {
                        selectedDay = it
                        onDateSelected(LocalDate.of(selectedYear, selectedMonth, it))
                    },
                    labelFormatter = { String.format("%02d", it) },
                    title = "Days",
                    modifier = Modifier.width(90.dp)
                )

                // Months Column
                ScrollableColumn(
                    items = months,
                    selectedItem = selectedMonth,
                    onItemSelected = {
                        selectedMonth = it
                        onDateSelected(LocalDate.of(selectedYear, it, selectedDay))
                    },
                    labelFormatter = { month -> Month.of(month).name.take(3) }, // Show first three letters of month
                    title = "Months",
                    modifier = Modifier.width(90.dp)
                )

                // Years Column
                ScrollableColumn(
                    items = years,
                    selectedItem = selectedYear,
                    onItemSelected = {
                        selectedYear = it
                        onDateSelected(LocalDate.of(it, selectedMonth, selectedDay))
                    },
                    labelFormatter = { it.toString() },
                    title = "Years",
                    modifier = Modifier.width(90.dp)
                )
            }
        }
    }
}

@Composable
fun ScrollableColumn(
    items: List<Int>,
    selectedItem: Int,
    onItemSelected: (Int) -> Unit,
    labelFormatter: (Int) -> String,
    title: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text= title, style= MaterialTheme.typography.headlineLarge)

        LazyColumn(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            contentPadding = PaddingValues(vertical= 10.dp)
        ) {
            items(items) { item ->
                val isSelected= item == selectedItem
                val textStyle= if (isSelected) {
                    TextStyle(fontSize= 24.sp, fontWeight= FontWeight.Bold)
                } else {
                    TextStyle(fontSize= 16.sp)
                }

                Text(
                    text= labelFormatter(item),
                    style= textStyle,
                    modifier= Modifier.fillMaxWidth().padding(4.dp).clickable {
                        onItemSelected(item)
                    }.graphicsLayer {
                        if (isSelected) {
                            scaleX= 1.2f
                            scaleY= 1.2f
                        }
                    }
                )
            }
        }
    }
}

