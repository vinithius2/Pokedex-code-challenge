package com.vinithius.poke10.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import com.vinithius.poke10.R
import com.vinithius.poke10.databinding.PikachuSadBinding

class PikachuComponent(context: Context, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {

    private val binding =
        PikachuSadBinding.inflate(LayoutInflater.from(context), this, true)


    init {
        val loadingScale: Animation = AnimationUtils.loadAnimation(
            context,
            R.anim.pikachu_error
        )
        binding.imageErrorPikachu.startAnimation(loadingScale)
    }

}
