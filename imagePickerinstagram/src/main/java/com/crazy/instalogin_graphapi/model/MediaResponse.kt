package com.crazy.instalogin_graphapi.model


import com.fasterxml.jackson.annotation.JsonProperty

data class MediaResponse(
    @JsonProperty("data")
    var `data`: List<MediaData>?,
    @JsonProperty("paging")
    var paging: Paging?
)