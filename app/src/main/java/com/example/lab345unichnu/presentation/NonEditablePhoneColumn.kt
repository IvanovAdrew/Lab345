package com.example.lab345unichnu.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.lab345unichnu.R
import java.io.File

@Composable
fun NonEditablePhoneColumn(
    modifier: Modifier = Modifier.padding(20.dp),
    deviceUI: DeviceUI,
    onEdit: (DeviceUI) -> Unit,
    onDelete: (DeviceUI) -> Unit
) {
    OutlinedCard(modifier = modifier) {
        Column() {
            Box(
                modifier = Modifier
                    .padding(20.dp)
                    .align(alignment = Alignment.CenterHorizontally)
                    .fillMaxWidth()
            ) {
                val buttonColor = 0xFF0191B9
                Button(
                    onClick = {
                        onEdit(deviceUI)
                    },
                    elevation = ButtonDefaults.elevatedButtonElevation(
                        10.dp,
                        5.dp,
                        5.dp,
                        5.dp,
                        0.dp
                    ),
                    contentPadding = PaddingValues(),
                    colors = ButtonColors(
                        Color(buttonColor),
                        Color(buttonColor),
                        Color(buttonColor),
                        Color(buttonColor)
                    ),
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .size(40.dp, 40.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.baseline_edit_24),
                        "edit",
                    )
                }
                Text(
                    modifier = Modifier.align(alignment = Alignment.Center),
                    text = deviceUI.device.name,
                    fontSize = 14.sp,
                    fontStyle = FontStyle.Italic
                )
                Button(
                    onClick = {
                        onDelete(deviceUI)
                        deviceUI.isEditing = true
                    },
                    elevation = ButtonDefaults.elevatedButtonElevation(
                        10.dp,
                        5.dp,
                        5.dp,
                        5.dp,
                        0.dp
                    ),
                    contentPadding = PaddingValues(),
                    colors = ButtonColors(Color.Red, Color.Red, Color.Red, Color.Red),
                    modifier = Modifier
                        .align(alignment = Alignment.TopEnd)
                        .size(40.dp, 40.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.baseline_delete_outline_24),
                        "delete",
                    )
                }
            }

            AsyncImage(
                model = deviceUI.device.image.toString(),
                contentDescription = "Loaded Image",
                modifier = Modifier
                    .fillMaxWidth().padding(horizontal = 30.dp),
                contentScale = ContentScale.Fit,
            )
            Text(
                text = deviceUI.device.type.toString(),
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(20.dp)
                    .align(alignment = Alignment.CenterHorizontally),
                color = Color.Green,
                fontStyle = FontStyle.Italic
            )
        }
    }
}