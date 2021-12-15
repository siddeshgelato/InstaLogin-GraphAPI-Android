package com.crazy.instalogin_graphapi.model


import com.fasterxml.jackson.annotation.JsonProperty

data class Cursors(
    @JsonProperty("after")
    var after: String?,
    @JsonProperty("before")
    var before: String?
)