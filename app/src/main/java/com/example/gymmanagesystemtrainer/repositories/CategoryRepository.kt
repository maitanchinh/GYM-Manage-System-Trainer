package com.example.gymmanagesystemtrainer.repositories

import com.example.gymmanagesystemtrainer.model.FilterRequestBody
import com.example.gymmanagesystemtrainer.network.CategoryApiService
import javax.inject.Inject

class CategoryRepository @Inject constructor(private val categoryApiService: CategoryApiService){
    suspend fun getCategories(filterRequestBody: FilterRequestBody) = categoryApiService.getCategories(filterRequestBody)
}