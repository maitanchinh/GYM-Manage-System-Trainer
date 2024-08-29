package com.example.gymmanagesystemtrainer.network

import com.example.gymmanagesystemtrainer.model.Equipment
import com.example.gymmanagesystemtrainer.model.FilterRequestBody
import com.example.gymmanagesystemtrainer.model.Response
import com.example.gymmanagesystemtrainer.model.SlotEquipment
import com.example.gymmanagesystemtrainer.utils.RequiresAuth
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface EquipmentApiService {
    @POST("admin/equipments/filter")
    suspend fun getEquipments(@Body filterRequestBody: FilterRequestBody) : Response<Equipment>

    @RequiresAuth
    @POST("slot-equipments/borrow-request-list")
    suspend fun borrowRequestList(@Body borrowRequestBody: BorrowRequestBody) : List<SlotEquipment>

    @POST("slot-equipments/filter")
    suspend fun getSlotEquipments(@Body filterRequestBody: FilterRequestBody) : Response<SlotEquipment>

    @DELETE("slot-equipments/{id}")
    suspend fun deleteSlotEquipment(@Path("id") id: String) : SlotEquipment

    @RequiresAuth
    @PUT("slot-equipments/repay-request")
    suspend fun repayRequest(@Query("id") id: String) : SlotEquipment
}

data class BorrowRequestBody(
    val slotId: String,
    val equipmentIds: List<String>
)