package org.delcom.pam_p4_ifs23019.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import org.delcom.pam_p4_ifs23019.R
import org.delcom.pam_p4_ifs23019.helper.ConstHelper
import org.delcom.pam_p4_ifs23019.helper.RouteHelper
import org.delcom.pam_p4_ifs23019.helper.SuspendHelper
import org.delcom.pam_p4_ifs23019.helper.SuspendHelper.SnackBarType
import org.delcom.pam_p4_ifs23019.helper.ToolsHelper
import org.delcom.pam_p4_ifs23019.network.Desserts.data.ResponseDessertData
import org.delcom.pam_p4_ifs23019.ui.components.BottomDialog
import org.delcom.pam_p4_ifs23019.ui.components.BottomDialogType
import org.delcom.pam_p4_ifs23019.ui.components.BottomNavComponent
import org.delcom.pam_p4_ifs23019.ui.components.LoadingUI
import org.delcom.pam_p4_ifs23019.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23019.ui.components.TopAppBarMenuItem
import org.delcom.pam_p4_ifs23019.ui.theme.DelcomTheme
import org.delcom.pam_p4_ifs23019.ui.viewmodels.DessertActionUIState
import org.delcom.pam_p4_ifs23019.ui.viewmodels.DessertUIState
import org.delcom.pam_p4_ifs23019.ui.viewmodels.DessertViewModel

@Composable
fun DessertsDetailScreen(
    navController: NavHostController,
    snackbarHost: SnackbarHostState,
    DessertViewModel: DessertViewModel,
    DessertId: String
) {
    // Ambil data dari viewmodel
    val uiStateDessert by DessertViewModel.uiState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var isConfirmDelete by remember { mutableStateOf(false) }

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
        if(uiStateDessert.Dessert !is DessertUIState.Loading){
            if(uiStateDessert.Dessert is DessertUIState.Success){
                Dessert = (uiStateDessert.Dessert as DessertUIState.Success).data
                isLoading = false
            } else {
                RouteHelper.back(navController)
            }
        }
    }

    fun onDelete(){
        isLoading = true
        DessertViewModel.deleteDessert(DessertId)
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
                    navController,
                    ConstHelper.RouteNames.Desserts.path,
                    true
                )
                uiStateDessert.Dessert = DessertUIState.Loading
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
    if(isLoading || Dessert == null){
        LoadingUI()
        return
    }

    // Menu item details
    val detailMenuItems = listOf(
        TopAppBarMenuItem(
            text = "Ubah Data",
            icon = Icons.Filled.Edit,
            route = null,
            onClick = {
                RouteHelper.to(
                    navController,
                    ConstHelper.RouteNames.DessertsEdit.path
                        .replace("{DessertId}", Dessert!!.id),
                )
            }
        ),
        TopAppBarMenuItem(
            text = "Hapus Data",
            icon = Icons.Filled.Delete,
            route = null,
            onClick = {
                isConfirmDelete = true
            }
        ),
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    )
    {
        // Top App Bar
        TopAppBarComponent(
            navController = navController,
            title = Dessert!!.nama,
            showBackButton = true,
            customMenuItems = detailMenuItems
        )
        // Content
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            // Content UI
            DessertsDetailUI(
                Dessert = Dessert!!
            )
            // Bottom Dialog to Confirmation Delete
            BottomDialog(
                type = BottomDialogType.ERROR,
                show = isConfirmDelete,
                onDismiss = { isConfirmDelete = false },
                title = "Konfirmasi Hapus Data",
                message = "Apakah Anda yakin ingin menghapus data ini?",
                confirmText = "Ya, Hapus",
                onConfirm = {
                    onDelete()
                },
                cancelText = "Batal",
                destructiveAction = true
            )
        }
        // Bottom Nav
        BottomNavComponent(navController = navController)
    }
}

@Composable
fun DessertsDetailUI(
    Dessert: ResponseDessertData
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    )
    {
        // Gambar
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp)
        )
        {
            AsyncImage(
                model = ToolsHelper.getDessertImageUrl(Dessert.id),
                contentDescription = Dessert.nama,
                placeholder = painterResource(R.drawable.img_placeholder),
                error = painterResource(R.drawable.img_placeholder),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text = Dessert.nama,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Deskripsi
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Deskripsi",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )
                Text(
                    text = Dessert.deskripsi,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

        }

        // Bahan Utama
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Bahan Utama",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )
                Text(
                    text = Dessert.bahanUtama,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

        }

        // Tingkat Kemanisan
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Tingkat Kemanisan",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )
                Text(
                    text = Dessert.tingkatKemanisan,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

        }

        // Harga
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Harga",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )
                Text(
                    text = "Rp. ${Dessert.harga}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun PreviewDessertsDetailUI() {
    DelcomTheme {
//        DessertsDetailUI(
//            Dessert = DummyData.getDessertsData()[0]
//        )
    }
}
