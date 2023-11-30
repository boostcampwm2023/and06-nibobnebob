package com.avengers.nibobnebob.presentation.ui.main.home.model

data class UiFilterData(
    val name: String,
    var isSelected: Boolean,
    val onItemClicked: (String) -> Unit
)
