package com.example.lab345unichnu.presentation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.lab345unichnu.R
import com.example.lab345unichnu.data.local.models.Device
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun EditablePhoneColumn(
    modifier: Modifier = Modifier.padding(20.dp),
    deviceUI: DeviceUI?,
    onSave: (DeviceUI) -> Unit,
    onRevert: (DeviceUI?) -> Unit
) {

        var name by remember { mutableStateOf(deviceUI?.device?.name ?: "") }
        var imageStringResource by remember { mutableStateOf(deviceUI?.device?.image ?: "") }
        var typeOfDevice by remember { mutableStateOf(deviceUI?.device?.type ?: "") }
        OutlinedCard(modifier = modifier) {
            Column() {
                Box(
                    modifier = Modifier
                        .padding(20.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                        .fillMaxWidth()
                ) {
                    TextField(
                        value = name,
                        onValueChange = { value -> name = value },
                        modifier = Modifier
                            .align(alignment = Alignment.Center)
                            .size(150.dp, 50.dp),
                        textStyle = TextStyle(
                            fontSize = 14.sp,
                            fontStyle = FontStyle.Italic
                        )
                    )
                }
                OverlayEditableImage(imageStringResource) { newImagePath ->
                    imageStringResource = newImagePath
                }
                Box(
                    modifier = Modifier
                        .padding(20.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                        .fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            onSave(DeviceUI(device = Device(uid = deviceUI?.device?.uid?: 0, name = name, image = imageStringResource, type = typeOfDevice)))
                        },
                        elevation = ButtonDefaults.elevatedButtonElevation(
                            10.dp,
                            5.dp,
                            5.dp,
                            5.dp,
                            0.dp
                        ),
                        contentPadding = PaddingValues(),
                        colors = ButtonColors(Color.Green, Color.Green, Color.Green, Color.Green),
                        modifier = Modifier
                            .align(alignment = Alignment.CenterStart)
                            .size(40.dp, 40.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.baseline_approve_24),
                            "approve"
                        )
                    }
                    TextField(
                        value = typeOfDevice,
                        onValueChange = { value -> typeOfDevice = value },
                        modifier = Modifier
                            .padding(20.dp)
                            .size(150.dp, 50.dp)
                            .align(alignment = Alignment.Center),
                        textStyle = TextStyle(
                            fontSize = 14.sp,
                            fontStyle = FontStyle.Italic,
                            color = Color.Green,
                        )
                    )
                    Button(
                        onClick = {
                            onRevert(deviceUI)
                        },
                        elevation = ButtonDefaults.elevatedButtonElevation(
                            10.dp,
                            5.dp,
                            5.dp,
                            5.dp,
                            0.dp
                        ),
                        contentPadding = PaddingValues(),
                        colors = ButtonColors(Color.Blue, Color.Blue, Color.Blue, Color.Blue),
                        modifier = Modifier
                            .align(alignment = Alignment.CenterEnd)
                            .size(40.dp, 40.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.baseline_arrow_back_24),
                            "delete",
                        )
                    }
                }
            }
        }

}
@Composable
fun OverlayEditableImage(imagePath: String, changingImagePath: (String) -> Unit) {

    val singleLauncherPhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {uri -> changingImagePath(uri.toString()) }
    )

    Box(
        modifier = Modifier.fillMaxWidth().clickable {
                singleLauncherPhotoPicker.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }
    ) {
        // Основное изображение
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imagePath)
                .size(800)
                .diskCachePolicy(CachePolicy.ENABLED) // Включаем кеширование на диск
                .memoryCachePolicy(CachePolicy.ENABLED) // Включаем кеш в RAM
                .build(),
            contentDescription = "Loaded Image",
            modifier = Modifier.fillMaxWidth(),

            contentScale = ContentScale.Fit,
        )

        // Полупрозрачное изображение сверху
        Image(
            painter = painterResource(R.drawable.modify_icon), // Статичное изображение
            contentDescription = "Overlay Icon",
            modifier = Modifier
                .align(Alignment.Center) // Размещение в углу
                .size(50.dp) // Размер иконки
                .alpha(0.6f), // Полупрозрачность
        )
    }
}