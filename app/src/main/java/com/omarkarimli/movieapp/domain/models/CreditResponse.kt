package com.omarkarimli.movieapp.domain.models


import com.google.gson.annotations.SerializedName

data class CreditResponse(
    @SerializedName("cast")
    val cast: List<Cast?>?,
    @SerializedName("crew")
    val crew: List<Crew?>?,
    @SerializedName("id")
    val id: Int?
)