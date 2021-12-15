package com.crazy.instalogin_graphapi.model


import com.fasterxml.jackson.annotation.JsonProperty

data class Paging(
    @JsonProperty("cursors")
    var cursors: Cursors?
)