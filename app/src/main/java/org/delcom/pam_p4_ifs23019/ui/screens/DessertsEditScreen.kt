package org.delcom.pam_p4_ifs23019.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import okhttp3.MultipartBody
import org.delcom.pam_p4_ifs23019.R
import org.delcom.pam_p4_ifs23019.helper.AlertHelper
import org.delcom.pam_p4_ifs23019.helper.AlertState
import org.delcom.pam_p4_ifs23019.helper.AlertType
import org.delcom.pam_p4_ifs23019.helper.ConstHelper
import org.delcom.pam_p4_ifs23019.helper.RouteHelper
import org.delcom.pam_p4_ifs23019.helper.SuspendHelper
import org.delcom.pam_p4_ifs23019.helper.SuspendHelper.SnackBarType
import org.delcom.pam_p4_ifs23019.helper.ToolsHelper
import org.delcom.pam_p4_ifs23019.helper.ToolsHelper.toRequestBodyText
import org.delcom.pam_p4_ifs23019.helper.ToolsHelper.uriToMultipart
import org.delcom.pam_p4_ifs23019.network.Desserts.data.ResponseDessertData
import org.delcom.pam_p4_ifs23019.ui.components.BottomNavComponent
import org.delcom.pam_p4_ifs23019.ui.components.LoadingUI
import org.delcom.pam_p4_ifs23019.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23019.ui.viewmodels.DessertActionUIState
import org.delcom.pam_p4_ifs23019.ui.viewmodels.DessertUIState
import org.delcom.pam_p4_ifs23019.ui.viewmodels.DessertViewModel

@Composable
fun DessertsEditScreen(
    navController: NavHostController,
    snackbarHost: SnackbarHostState,
    DessertViewModel: DessertViewModel,
    DessertId: String
) {
    // Ambil data dari viewmodel
    val uiStateDessert by DessertViewModel.uiState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }

    // Muat data
    var Dessert by remember { mutableStateOf<ResponseDessertData?>(null) }

    // Dapatkan tumbuhan berdasarkan ID
    LaunchedEffect(Unit) {
        isLoading = true
        // Reset status Dessert action
        uiStateDessert.DessertAction = DessertActionUIState.Loading
        uiStateDessert.Dessert = DessertUIState.Loading
        DessertViewModel.getDessertById(DessertId)
    }

    // Picu ulang ketika data tumbuhan berubah
    LaunchedEffect(uiStateDessert.Dessert) {
        if (uiStateDessert.Dessert !is DessertUIState.Loading) {
            if (uiStateDessert.Dessert is DessertUIState.Success) {
                Dessert = (uiStateDessert.Dessert as DessertUIState.Success).data
                isLoading = false
            } else {
                RouteHelper.back(navController)
                isLoading = false
            }
        }
    }

    // Simpan perubahan data
    fun onSave(
        context: Context,
        nama: String,
        deskripsi: String,
        bahanUtama: String,
        tingkatKemanisan: String,
        harga: String,
        file: Uri? = null
    ) {
        isLoading = true

        val namaBody = nama.toRequestBodyText()
        val deskripsiBody = deskripsi.toRequestBodyText()
        val bahanUtamaBody = bahanUtama.toRequestBodyText()
        val tingkatKemanisanBody = tingkatKemanisan.toRequestBodyText()
        val hargaBody = harga.toRequestBodyText()

        var filePart: MultipartBody.Part? = null
        if (file != null) {
            filePart = uriToMultipart(context, file, "file")
        }

        DessertViewModel.putDessert(
            DessertId = DessertId,
            nama = namaBody,
            deskripsi = deskripsiBody,
            bahanUtama = bahanUtamaBody,
            tingkatKemanisan = tingkatKemanisanBody,
            harga = hargaBody,
            file = filePart,
        )
    }

    LaunchedEffect(uiStateDessert.DessertAction) {
        when (val state = uiStateDessert.DessertAction) {
            is DessertActionUIState.Success -> {
                SuspendHelper.showSnackBar(
                    snackbarHost = snackbarHost,
                    type = SnackBarType.SUCCESS,
                    message = state.message
                )
                RouteHelper.to(
                    navController = navController,
                    destination = ConstHelper.RouteNames.DessertsDetail.path
                        .replace("{DessertId}", DessertId),
                    popUpTo = ConstHelper.RouteNames.DessertsDetail.path
                        .replace("{DessertId}", DessertId),
                    removeBackStack = true
                )
                isLoading = false
            }

            is DessertActionUIState.Error -> {
                SuspendHelper.showSnackBar(
                    snackbarHost = snackbarHost,
                    type = SnackBarType.ERROR,
                    message = state.message
                )
                isLoading = false
            }

            else -> {}
        }
    }

    // Tampilkan halaman loading
    if (isLoading || Dessert == null) {
        LoadingUI()
        return
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top App Bar
        TopAppBarComponent(
            navController = navController,
            title = "Ubah Data",
            showBackButton = true,
        )
        // Content
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            DessertsEditUI(
                Dessert = Dessert!!,
                onSave = ::onSave
            )
        }
        // Bottom Nav
        BottomNavComponent(navController = navController)
    }
}

@Composable
fun DessertsEditUI(
    Dessert: ResponseDessertData,
    onSave: (
        Context,
        String,
        String,
        String,
        String,
        String,
        Uri?
    ) -> Unit
) {
    val alertState = remember { mutableStateOf(AlertState()) }

    var dataFile by remember { mutableStateOf<Uri?>(null) }
    var dataNama by remember { mutableStateOf(Dessert.nama) }
    var dataDeskripsi by remember { mutableStateOf(Dessert.deskripsi) }
    var dataBahanUtama by remember { mutableStateOf(Dessert.bahanUtama) }
    var dataTingkatKemanisan by remember { mutableStateOf(Dessert.tingkatKemanisan) }
    var dataHarga by remember { mutableStateOf(Dessert.harga) }
    val context = LocalContext.current

    // Focus manager
    val focusManager = LocalFocusManager.current

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        dataFile = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // File Gambar
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .clickable {
                        imagePicker.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                if (dataFile != null) {
                    AsyncImage(
                        model = dataFile,
                        placeholder = painterResource(R.drawable.img_placeholder),
                        error = painterResource(R.drawable.img_placeholder),
                        contentDescription = "Pratinjau Gambar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    AsyncImage(
                        model = ToolsHelper.getDessertImageUrl(Dessert.id),
                        placeholder = painterResource(R.drawable.img_placeholder),
                        error = painterResource(R.drawable.img_placeholder),
                        contentDescription = "Pratinjau Gambar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tap untuk mengganti gambar",
                style = MaterialTheme.typography.bodySmall
            )
        }

        // Nama
        OutlinedTextField(
            value = dataNama,
            onValueChange = { dataNama = it },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                focusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                cursorColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
            label = {
                Text(
                    text = "Nama",
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
        )

        // Deskripsi
        OutlinedTextField(
            value = dataDeskripsi,
            onValueChange = { dataDeskripsi = it },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                focusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                cursorColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
            label = {
                Text(
                    text = "Deskripsi",
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            maxLines = 5,
            minLines = 3
        )

        // Bahan Utama
        OutlinedTextField(
            value = dataBahanUtama,
            onValueChange = { dataBahanUtama = it },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                focusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                cursorColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
            label = {
                Text(
                    text = "Bahan Utama",
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
        )

        // Tingkat Kemanisan
        OutlinedTextField(
            value = dataTingkatKemanisan,
            onValueChange = { dataTingkatKemanisan = it },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                focusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                cursorColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
            label = {
                Text(
                    text = "Tingkat Kemanisan",
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
        )

        // Harga
        OutlinedTextField(
            value = dataHarga,
            onValueChange = { dataHarga = it },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                focusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                cursorColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
            label = {
                Text(
                    text = "Harga",
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
        )

        Spacer(modifier = Modifier.height(64.dp))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Floating Action Button
        FloatingActionButton(
            onClick = {
                if (dataNama.isEmpty()) {
                    AlertHelper.show(
                        alertState,
                        AlertType.ERROR,
                        "Nama tidak boleh kosong!"
                    )
                    return@FloatingActionButton
                }

                if (dataDeskripsi.isEmpty()) {
                    AlertHelper.show(
                        alertState,
                        AlertType.ERROR,
                        "Deskripsi tidak boleh kosong!"
                    )
                    return@FloatingActionButton
                }

                if (dataBahanUtama.isEmpty()) {
                    AlertHelper.show(
                        alertState,
                        AlertType.ERROR,
                        "Bahan utama tidak boleh kosong!"
                    )
                    return@FloatingActionButton
                }

                if (dataTingkatKemanisan.isEmpty()) {
                    AlertHelper.show(
                        alertState,
                        AlertType.ERROR,
                        "Tingkat kemanisan tidak boleh kosong!"
                    )
                    return@FloatingActionButton
                }

                if (dataHarga.isEmpty()) {
                    AlertHelper.show(
                        alertState,
                        AlertType.ERROR,
                        "Harga tidak boleh kosong!"
                    )
                    return@FloatingActionButton
                }

                onSave(
                    context,
                    dataNama,
                    dataDeskripsi,
                    dataBahanUtama,
                    dataTingkatKemanisan,
                    dataHarga,
                    dataFile
                )
            },
            modifier = Modifier
                .align(Alignment.BottomEnd) // pojok kanan bawah
                .padding(16.dp) // jarak dari tepi
            ,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(
                imageVector = Icons.Default.Save,
                contentDescription = "Simpan Data"
            )
        }
    }

    if (alertState.value.isVisible) {
        AlertDialog(
            onDismissRequest = {
                AlertHelper.dismiss(alertState)
            },
            title = {
                Text(alertState.value.type.title)
            },
            text = {
                Text(alertState.value.message)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        AlertHelper.dismiss(alertState)
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun PreviewDessertsEditUI() {
//    DelcomTheme {
//        DessertsEditUI(
//            Desserts = DummyData.getDessertsEditData(),
//            onOpen = {}
//        )
//    }
}
