package org.delcom.pam_p4_ifs23019.ui.viewmodels

import androidx.annotation.Keep
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.delcom.pam_p4_ifs23019.network.Desserts.data.ResponseDessertData
import org.delcom.pam_p4_ifs23019.network.Desserts.data.ResponseProfile
import org.delcom.pam_p4_ifs23019.network.Desserts.service.IDessertRepository
import javax.inject.Inject

sealed interface ProfileUIState {
    data class Success(val data: ResponseProfile) : ProfileUIState
    data class Error(val message: String) : ProfileUIState
    object Loading : ProfileUIState
}

sealed interface DessertsUIState {
    data class Success(val data: List<ResponseDessertData>) : DessertsUIState
    data class Error(val message: String) : DessertsUIState
    object Loading : DessertsUIState
}

sealed interface DessertUIState {
    data class Success(val data: ResponseDessertData) : DessertUIState
    data class Error(val message: String) : DessertUIState
    object Loading : DessertUIState
}

sealed interface DessertActionUIState {
    data class Success(val message: String) : DessertActionUIState
    data class Error(val message: String) : DessertActionUIState
    object Loading : DessertActionUIState
}

data class UIStateDessert(
    val profile: ProfileUIState = ProfileUIState.Loading,
    val Desserts: DessertsUIState = DessertsUIState.Loading,
    var Dessert: DessertUIState = DessertUIState.Loading,
    var DessertAction: DessertActionUIState = DessertActionUIState.Loading
)

@HiltViewModel
@Keep
class DessertViewModel @Inject constructor(
    private val repository: IDessertRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UIStateDessert())
    val uiState = _uiState.asStateFlow()

    fun getProfile() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    profile = ProfileUIState.Loading
                )
            }
            _uiState.update { it ->
                val tmpState = runCatching {
                    repository.getProfile()
                }.fold(
                    onSuccess = {
                        if (it.status == "success") {
                            ProfileUIState.Success(it.data!!)
                        } else {
                            ProfileUIState.Error(it.message)
                        }
                    },
                    onFailure = {
                        ProfileUIState.Error(it.message ?: "Unknown error")
                    }
                )

                it.copy(
                    profile = tmpState
                )
            }
        }
    }

    fun getAllDesserts(search: String? = null) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    Desserts = DessertsUIState.Loading
                )
            }
            _uiState.update { it ->
                val tmpState = runCatching {
                    repository.getAllDesserts(search)
                }.fold(
                    onSuccess = {
                        if (it.status == "success") {
                            DessertsUIState.Success(it.data!!.Desserts)
                        } else {
                            DessertsUIState.Error(it.message)
                        }
                    },
                    onFailure = {
                        DessertsUIState.Error(it.message ?: "Unknown error")
                    }
                )

                it.copy(
                    Desserts = tmpState
                )
            }
        }
    }

    fun postDessert(
        nama: RequestBody,
        deskripsi: RequestBody,
        bahanUtama: RequestBody,
        tingkatKemanisan: RequestBody,
        harga: RequestBody,
        file: MultipartBody.Part
    ) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    DessertAction = DessertActionUIState.Loading
                )
            }
            _uiState.update { it ->
                val tmpState = runCatching {
                    repository.postDessert(
                        nama = nama,
                        deskripsi = deskripsi,
                        bahanUtama = bahanUtama,
                        tingkatKemanisan = tingkatKemanisan,
                        harga = harga,
                        file = file
                    )
                }.fold(
                    onSuccess = {
                        if (it.status == "success") {
                            DessertActionUIState.Success(it.data!!.DessertId)
                        } else {
                            DessertActionUIState.Error(it.message)
                        }
                    },
                    onFailure = {
                        DessertActionUIState.Error(it.message ?: "Unknown error")
                    }
                )

                it.copy(
                    DessertAction = tmpState
                )
            }
        }
    }

    fun getDessertById(DessertId: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    Dessert = DessertUIState.Loading
                )
            }
            _uiState.update { it ->
                val tmpState = runCatching {
                    repository.getDessertById(DessertId)
                }.fold(
                    onSuccess = {
                        if (it.status == "success") {
                            DessertUIState.Success(it.data!!.Dessert)
                        } else {
                            DessertUIState.Error(it.message)
                        }
                    },
                    onFailure = {
                        DessertUIState.Error(it.message ?: "Unknown error")
                    }
                )

                it.copy(
                    Dessert = tmpState
                )
            }
        }
    }

    fun putDessert(
        DessertId: String,
        nama: RequestBody,
        deskripsi: RequestBody,
        bahanUtama: RequestBody,
        tingkatKemanisan: RequestBody,
        harga: RequestBody,
        file: MultipartBody.Part?
    ) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    DessertAction = DessertActionUIState.Loading
                )
            }
            _uiState.update { it ->
                val tmpState = runCatching {
                    repository.putDessert(
                        DessertId = DessertId,
                        nama = nama,
                        deskripsi = deskripsi,
                        bahanUtama = bahanUtama,
                        tingkatKemanisan = tingkatKemanisan,
                        harga = harga,
                        file = file
                    )
                }.fold(
                    onSuccess = {
                        if (it.status == "success") {
                            DessertActionUIState.Success(it.message)
                        } else {
                            DessertActionUIState.Error(it.message)
                        }
                    },
                    onFailure = {
                        DessertActionUIState.Error(it.message ?: "Unknown error")
                    }
                )

                it.copy(
                    DessertAction = tmpState
                )
            }
        }
    }

    fun deleteDessert(DessertId: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    DessertAction = DessertActionUIState.Loading
                )
            }
            _uiState.update { it ->
                val tmpState = runCatching {
                    repository.deleteDessert(
                        DessertId = DessertId
                    )
                }.fold(
                    onSuccess = {
                        if (it.status == "success") {
                            DessertActionUIState.Success(it.message)
                        } else {
                            DessertActionUIState.Error(it.message)
                        }
                    },
                    onFailure = {
                        DessertActionUIState.Error(it.message ?: "Unknown error")
                    }
                )

                it.copy(
                    DessertAction = tmpState
                )
            }
        }
    }
}