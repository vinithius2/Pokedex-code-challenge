package com.vinithius.pokedexcodechallenge.extension

import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.view.animation.Transformation
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.palette.graphics.Palette
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.vinithius.pokedexcodechallenge.R
import com.vinithius.pokedexcodechallenge.datasource.response.Pokemon
import kotlin.math.max

private const val urlSprite =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"
private const val urlArtwork =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"
private const val fileType = ".png"


/**
 * Open View.
 */
fun View.expand() {
    val matchParentMeasureSpec =
        View.MeasureSpec.makeMeasureSpec((parent as View).width, View.MeasureSpec.EXACTLY)
    val wrapContentMeasureSpec =
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    measure(matchParentMeasureSpec, wrapContentMeasureSpec)

    val targetHeight: Int = measuredHeight
    val duration = (targetHeight / context.resources.displayMetrics.density * 2).toLong()
    layoutParams.height = 1
    isVisible = true
    val animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            layoutParams.height = if (interpolatedTime == 1f)
                LinearLayout.LayoutParams.WRAP_CONTENT
            else
                max(1, (targetHeight * interpolatedTime).toInt())
            requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }
    animation.duration = duration
    startAnimation(animation)
}

/**
 * Close View.
 */
fun View.collapse() {
    val initialHeight: Int = measuredHeight
    val duration = (initialHeight / context.resources.displayMetrics.density * 2).toLong()
    val animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            if (interpolatedTime == 1f) {
                isVisible = false
            } else {
                layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }
    animation.duration = duration
    startAnimation(animation)
}

/**
 * Rotation View bottom to top.
 */
fun View.rotationFromBottomToTop() {
    val animation = RotateAnimation(
        -180f,
        0f,
        Animation.RELATIVE_TO_SELF,
        0.5f,
        Animation.RELATIVE_TO_SELF,
        0.5f
    )
    animation.duration = 500
    startAnimation(animation)
    rotation = 0f
}

/**
 * Rotation View top to bottom.
 */
fun View.rotationFromTopToBottom() {
    val animation = RotateAnimation(
        180f,
        0f,
        Animation.RELATIVE_TO_SELF,
        0.5f,
        Animation.RELATIVE_TO_SELF,
        0.5f
    )
    animation.duration = 500
    startAnimation(animation)
    rotation = 180f
}

/**
 * Open and Close View.
 */
fun View.getCollapseAndExpand(expand: Boolean, arrow: View): Boolean {
    if (expand) {
        arrow.rotationFromBottomToTop()
        this.collapse()
    } else {
        arrow.rotationFromTopToBottom()
        this.expand()
    }
    return !expand
}


/**
 * Set pokemon image in imageView.
 */
fun ImageView.setPokemonImage(pokemon: Pokemon, invert: Boolean = false) {
    val pokemonId = pokemon.id ?: pokemon.url?.getIdIntoUrl()
    val urlSprite = "$urlSprite${pokemonId}$fileType"
    val urlArtwork = "$urlArtwork${pokemonId}$fileType"
    Picasso.get()
        .load(if (invert) urlArtwork else urlSprite)
        .into(this, object : Callback {

            override fun onSuccess() {
                // Nothing
            }

            override fun onError(e: Exception?) {
                Picasso.get()
                    .load(if (invert) urlSprite else urlArtwork)
                    .error(R.drawable.ic_error_image)
                    .into(this@setPokemonImage)
            }

        })
}

/**
 * Set color in background is GradientDrawable View.
 */
fun View.setColorBackgroundGradientDrawable(color: Palette.Swatch?) {
    val background = this.background
    if (background is GradientDrawable) {
        color?.let {
            background.setColor(it.rgb)
        }
    }
}
