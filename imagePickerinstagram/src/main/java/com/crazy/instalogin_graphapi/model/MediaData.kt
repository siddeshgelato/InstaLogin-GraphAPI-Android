package com.crazy.instalogin_graphapi.model


import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

data class MediaData(
    @SerializedName("id")
    @JsonProperty("id")
    var id: String?,
    @SerializedName("media_type")
    @JsonProperty("media_type")
    var mediaType: String?,
    @SerializedName("media_url")
    @JsonProperty("media_url")
    var mediaUrl: String?
) {
    var selected: Boolean?=false
}