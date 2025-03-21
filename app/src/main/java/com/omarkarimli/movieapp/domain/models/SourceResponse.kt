package com.omarkarimli.movieapp.domain.models

import com.google.gson.annotations.SerializedName

data class SourceResponse(
    @SerializedName("sources")
    val sources: List<SourceX>?,
    @SerializedName("status")
    val status: String?
)