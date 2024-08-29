package com.example.gymmanagesystemtrainer.repositories

import com.example.gymmanagesystemtrainer.model.FilterRequestBody
import com.example.gymmanagesystemtrainer.network.BorrowRequestBody
import com.example.gymmanagesystemtrainer.network.EquipmentApiService
import javax.inject.Inject

class EquipmentRepository @Inject constructor(private val equipmentApiService: EquipmentApiService) {
    suspend fun getEquipments(filterRequestBody: FilterRequestBody) = equipmentApiService.getEquipments(filterRequestBody)
    suspend fun borrowRequestList(borrowRequestBody: BorrowRequestBody) = equipmentApiService.borrowRequestList(borrowRequestBody)
    suspend fun getSlotEquipments(filterRequestBody: FilterRequestBody) = equipmentApiService.getSlotEquipments(filterRequestBody)
    suspend fun deleteSlotEquipment(id: String) = equipmentApiService.deleteSlotEquipment(id)
    suspend fun repayRequest(id: String) = equipmentApiService.repayRequest(id)
}