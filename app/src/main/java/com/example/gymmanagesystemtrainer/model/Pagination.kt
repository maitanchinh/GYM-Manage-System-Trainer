package com.example.gymmanagesystemtrainer.model

import com.google.gson.annotations.SerializedName

data class Pagination(

    @SerializedName("pageNumber") var pageNumber: Int? = null,
    @SerializedName("pageSize") var pageSize: Int = 1000,
    @SerializedName("totalRow") var totalRow: Int? = null

)