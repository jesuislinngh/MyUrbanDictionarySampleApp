package com.fireguard.android.myurbandictionarysampleapp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DefinitionList(
    val list: List<DefinitionItem>
) : Parcelable

@Parcelize
data class DefinitionItem(
    val definition: String,
    val permalink: String,
    val thumbs_up: Int,
    val sound_urls: List<String>,
    val author: String,
    val word: String,
    val defid: Int,
    val current_vote: String,
    val written_on: String,
    val example: String,
    val thumbs_down: Int
) : Parcelable