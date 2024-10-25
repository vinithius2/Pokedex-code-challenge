package com.vinithius.poke10.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import com.vinithius.poke10.R
import com.vinithius.poke10.databinding.LayoutLoadingBinding


class PokemonLoading(context: Context, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {

    private val binding =
        LayoutLoadingBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        val loadingAlpha: Animation = AnimationUtils.loadAnimation(
            context,
            R.anim.loading_alpha
        )
        val loadingScale: Animation = AnimationUtils.loadAnimation(
            context,
            R.anim.loading_scale
        )
        binding.logo.startAnimation(loadingAlpha)
        binding.textLoading.startAnimation(loadingScale)
    }

}
