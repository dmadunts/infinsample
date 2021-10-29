package com.dmadunts.samples.infinsample.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.dmadunts.samples.infinsample.databinding.GithubButtonBinding
import com.dmadunts.samples.infinsample.extensions.gone
import com.dmadunts.samples.infinsample.extensions.visible

class GithubButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyle, defStyleRes) {

    val binding: GithubButtonBinding = GithubButtonBinding.inflate(LayoutInflater.from(context))

    init {
        enableButton(true)
        addView(binding.root)
    }

    fun setButtonText(text: String) {
        binding.button.text = text
        binding.button.invalidate()
    }

    fun setOnClickAction(runnable: Runnable) {
        binding.button.setOnClickListener {
            runnable.run()
        }
    }

    fun enableButton(state: Boolean) {
        binding.button.isEnabled = state
        if (state) {
            binding.button.setTextColor(Color.WHITE)
        } else {
            binding.button.setTextColor(Color.GRAY)
        }
    }

    fun showProgress(state: Boolean) {
        if (state) {
            binding.progressBar.visible()
        } else {
            binding.progressBar.gone()
        }
        binding.progressBar.invalidate()
    }
}