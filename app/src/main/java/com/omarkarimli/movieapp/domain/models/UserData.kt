package com.omarkarimli.movieapp.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserData(
    val name: String? = null,
    val surname: String? = null,
    val bio: String? = null,
    val website: String? = null
): Parcelable
