package com.dmadunts.samples.infinsample.remote.models

import com.google.gson.annotations.SerializedName

data class Codes(
    @SerializedName("device_code") val deviceCode: String,
    @SerializedName("user_code") val userCode: String,
    @SerializedName("verification_uri") val verificationUri: String,
    @SerializedName("expires_in") val expiresIn: Int,
    val interval: Int
)