package com.example.lab345unichnu.presentation

import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Phone
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import coil.compose.AsyncImage
import com.example.lab345unichnu.R
import com.example.lab345unichnu.data.local.models.Device
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewmodel: MainViewmodel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White),
            ) { innerPadding ->
                MyApp(modifier = Modifier.padding(innerPadding))
            }
        }
    }

    @Composable
    fun MyApp(modifier: Modifier) {
        val devices by mainViewmodel.uiStateFlow.collectAsState()
        val creatingDevices by mainViewmodel.uiCreateStateFlow.collectAsState()
        MyContent(modifier = modifier,
            phoneList = devices,
            creatingPhoneList = creatingDevices,
            onUpdate = { mainViewmodel.fetchAndStoreProducts() },
            onEdit = { device -> mainViewmodel.startEditingProduct(device) },
            onDelete = { device -> mainViewmodel.deleteProduct(device) },
            onRevert = { deviceUI -> mainViewmodel.stopEditingProduct(deviceUI) },
            onSave = { deviceUI -> mainViewmodel.saveEditingProduct(deviceUI) },
            onSaveCreating = {deviceUI -> mainViewmodel.saveCreatingProduct(deviceUI)},
            onRevertCreating = {deviceUI -> mainViewmodel.stopCreatingProduct(deviceUI)},
            onCreate = {mainViewmodel.createNewCellOfProduct()})
    }

    @Composable
    fun MyContent(
        modifier: Modifier = Modifier,
        phoneList: List<DeviceUI>,
        creatingPhoneList: List<DeviceUI?>,
        onUpdate: () -> Unit,
        onSave: (DeviceUI) -> Unit,
        onEdit: (DeviceUI) -> Unit,
        onDelete: (DeviceUI) -> Unit,
        onRevert: (DeviceUI?) -> Unit,
        onSaveCreating: (DeviceUI) -> Unit,
        onRevertCreating: (DeviceUI?) -> Unit,
        onCreate: () ->Unit
    ) {
        Column(modifier = modifier) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(creatingPhoneList){ creatingPhone ->
                    EditablePhoneColumn(
                        deviceUI = creatingPhone,
                        onSave =  onSaveCreating ,
                        onRevert = onRevertCreating
                    )

                }
                items(phoneList.reversed()) { phone ->
                    if (!phone.isEditing) {
                        NonEditablePhoneColumn(
                            deviceUI =  phone,
                            onEdit = onEdit,
                            onDelete = onDelete
                        )
                    } else {
                        EditablePhoneColumn(
                            deviceUI = phone,
                            onSave = onSave,
                            onRevert = onRevert
                        )
                    }
                }

            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(onClick = onUpdate, modifier = Modifier.weight(1f).padding(horizontal = 10.dp)) {
                    Text(text = "Refresh")
                }
                Button(onClick = onCreate, modifier = Modifier.weight(1f).padding(horizontal = 10.dp)) {
                    Text(text = "Add new Item")
                }
            }
        }
    }

    @Composable
    @Preview
    fun PreviewMyScreen() {
        MyContent(
            Modifier,
            listOf(DeviceUI(device = Device(1, "phoneFirst", "", "smartphone"), isEditing = true)),
            emptyList(),
            {}, {}, {}, {}, {},{},{},{})
    }

    @Composable
    @Preview
    fun PreviewMyEditableColumn() {
        EditablePhoneColumn(
            Modifier.padding(0.dp),
            DeviceUI(device = Device(1, "phoneFirst", "", "smartphone")),
            {},
            {})
    }
}