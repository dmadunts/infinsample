package com.dmadunts.samples.infinsample.remote.models

data class Repository(
    val incomplete_results: Boolean,
    val items: List<Item>,
    val total_count: Int
)