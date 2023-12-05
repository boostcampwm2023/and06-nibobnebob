package com.avengers.nibobnebob.presentation.ui.bindingadapters

import android.widget.ImageButton
import androidx.databinding.BindingAdapter
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.presentation.ui.main.home.TrackingState

@BindingAdapter("trackingBtnDrawable")
fun bindTrackingBtnDrawable(btn: ImageButton, state: TrackingState) {
    when (state) {
        is TrackingState.On -> btn.setImageResource(R.drawable.ic_location_on)
        is TrackingState.Off -> btn.setImageResource(R.drawable.ic_location_off)
        else -> {}
    }
}
