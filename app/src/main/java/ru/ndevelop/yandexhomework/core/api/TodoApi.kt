package ru.ndevelop.tinkoffproject.core.api

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import ru.ndevelop.yandexhomework.data.models.network.AddItemRequestModel
import ru.ndevelop.yandexhomework.data.models.network.AddItemResponseModel
import ru.ndevelop.yandexhomework.data.models.network.ListOfItemsResponseModel
import ru.ndevelop.yandexhomework.data.models.network.SynchronizeDataRequestModel


interface TodoApi {
    @GET("list")
    suspend fun getListOfItems(): ListOfItemsResponseModel

    @POST("list")
    suspend fun addItem(
        @Body elementRequest: AddItemRequestModel,
        @Header("X-Last-Known-Revision") revision: Int
    ): AddItemResponseModel

    @DELETE("list/{id}")
    suspend fun deleteItem(
        @Path("id") id: String,
        @Header("X-Last-Known-Revision") revision: Int
    ): AddItemResponseModel

    @PUT("list/{id}")
    suspend fun updateItem(
        @Path("id") id: String,
        @Body elementRequest: AddItemRequestModel,
        @Header("X-Last-Known-Revision") revision: Int
    ): AddItemResponseModel

    @PATCH("list")
    suspend fun synchronizeData(
        @Body elementsRequest: SynchronizeDataRequestModel,
        @Header("X-Last-Known-Revision") revision: Int
    ): ListOfItemsResponseModel
}