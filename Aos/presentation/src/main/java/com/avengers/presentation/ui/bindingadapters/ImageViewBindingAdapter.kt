package com.avengers.nibobnebob.presentation.ui.bindingadapters

import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.avengers.presentation.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("filterRateBackground")
fun bindFilterBackground(iv: ImageView, rate: Int) = with(iv) {
    when (rate) {
        0 -> {
            setBackgroundResource(R.drawable.oval_primary1fill_nostroke)
        }

        1 -> {
            setBackgroundResource(R.drawable.oval_primary2fill_nostroke)
        }

        2 -> {
            setBackgroundResource(R.drawable.oval_primary3fill_nostroke)
        }

        3 -> {
            setBackgroundResource(R.drawable.oval_primary4fill_nostroke)
        }

        4 -> {
            setBackgroundResource(R.drawable.oval_primary5fill_nostroke)
        }

        else -> {
            setBackgroundResource(R.drawable.oval_primary1fill_nostroke)
        }
    }
}


@BindingAdapter("wishStatus")
fun bindWishStatus(iv: ImageView, isWish: Boolean) {
    iv.setBackgroundResource(
        if (isWish) R.drawable.ic_star_full else R.drawable.ic_star_border
    )
}

@BindingAdapter("profileImageUrl")
fun bindLoadImage(view: ImageView, imageUrl: String) {
    if (imageUrl.isNotBlank()) {
        Glide.with(view.context)
            .load(imageUrl)
            .apply(RequestOptions.circleCropTransform())
            .into(view)
    }
}

@BindingAdapter("reviewImageUrl")
fun bindReviewImageUrl(iv: ImageView, imageUrl: String) {
    if (imageUrl.isNotBlank()) {
        iv.visibility = View.VISIBLE
        Glide.with(iv.context)
            .load(imageUrl)
            .into(iv)
    } else {
        iv.visibility = View.GONE
    }
}

@BindingAdapter("checkEmptyImgUri")
fun bindCheckEmptyIngUri(iv: ImageView, uri: String) {
    if (uri.isNotBlank()) {
        iv.setImageURI(uri.toUri())
    }
}