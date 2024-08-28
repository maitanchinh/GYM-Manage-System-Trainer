package com.example.gymmanagesystemtrainer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymmanagesystemtrainer.model.Equipment
import com.example.gymmanagesystemtrainer.model.FilterRequestBody
import com.example.gymmanagesystemtrainer.model.Response
import com.example.gymmanagesystemtrainer.model.SlotEquipment
import com.example.gymmanagesystemtrainer.network.BorrowRequestBody
import com.example.gymmanagesystemtrainer.repositories.EquipmentRepository
import com.example.gymmanagesystemtrainer.utils.DataState
import com.example.gymmanagesystemtrainer.utils.ErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class EquipmentViewModel @Inject constructor(private val equipmentRepository: EquipmentRepository) : ViewModel() {
    private val _equipmens = MutableStateFlow<DataState<Response<Equipment>>>(DataState.Idle)
    val equipments : StateFlow<DataState<Response<Equipment>>> = _equipmens
    private val _equipmentsRequested = MutableStateFlow<DataState<List<SlotEquipment>>>(DataState.Idle)
    val equipmentsRequested : StateFlow<DataState<List<SlotEquipment>>> = _equipmentsRequested
    private val _slotEquipments = MutableStateFlow<DataState<Response<SlotEquipment>>>(DataState.Idle)
    val slotEquipments : StateFlow<DataState<Response<SlotEquipment>>> = _slotEquipments
    private val _slotEquipment = MutableStateFlow<DataState<SlotEquipment>>(DataState.Idle)
    val slotEquipment : StateFlow<DataState<SlotEquipment>> = _slotEquipment

    fun fetchEquipments(filterRequestBody: FilterRequestBody) {
        viewModelScope.launch {
            _equipmens.value = DataState.Loading
            try {
                val response = equipmentRepository.getEquipments(filterRequestBody = filterRequestBody)
                _equipmens.value = DataState.Success(response)
            } catch (e: HttpException) {
                _equipmens.value = DataState.Error(ErrorHandler(e.code()))
            }
        }
    }

    fun borrowRequestList(borrowRequestBody: BorrowRequestBody) {
        viewModelScope.launch {
            _equipmentsRequested.value = DataState.Loading
            try {
                val response = equipmentRepository.borrowRequestList(borrowRequestBody = borrowRequestBody)
                _equipmentsRequested.value = DataState.Success(response)
            } catch (e: HttpException) {
                _equipmentsRequested.value = DataState.Error(ErrorHandler(e.code()))
            }
        }
    }

    fun fetchSlotEquipments(filterRequestBody: FilterRequestBody) {
        viewModelScope.launch {
            _slotEquipments.value = DataState.Loading
            try {
                val response = equipmentRepository.getSlotEquipments(filterRequestBody = filterRequestBody)
                _slotEquipments.value = DataState.Success(response)
            } catch (e: HttpException) {
                _slotEquipments.value = DataState.Error(ErrorHandler(e.code()))
            }
        }
    }

    fun deleteSlotEquipment(id: String) {
        viewModelScope.launch {
            _slotEquipment.value = DataState.Loading
            try {
                val response = equipmentRepository.deleteSlotEquipment(id = id)
                _slotEquipment.value = DataState.Success(response)
            } catch (e: HttpException) {
                _slotEquipment.value = DataState.Error(ErrorHandler(e.code()))
            }
        }
    }
}