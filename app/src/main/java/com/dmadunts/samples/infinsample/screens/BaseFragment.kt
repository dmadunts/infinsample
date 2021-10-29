package com.dmadunts.samples.infinsample.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.viewbinding.ViewBinding
import com.dmadunts.samples.infinsample.R
import com.dmadunts.samples.infinsample.extensions.gone
import com.dmadunts.samples.infinsample.extensions.visible
import com.dmadunts.samples.infinsample.remote.utils.Event
import com.dmadunts.samples.infinsample.remote.utils.Resource
import com.google.android.material.snackbar.Snackbar

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB : ViewBinding>(
    private val inflate: Inflate<VB>,
    private val isViewSaved: Boolean = false
) : Fragment() {

    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!isViewSaved || _binding == null) {
            _binding = inflate.invoke(inflater, container, false)
        }
        onCreateView()
        return binding.root
    }

    abstract fun onCreateView()

    fun <T : Any> handleSingle(
        event: Event<Resource<T>>,
        progressView: ProgressBar? = null,
        success: (T) -> Unit
    ) {
        event.getContentIfNotHandled()?.let { resource ->
            when (resource) {
                is Resource.Loading -> {
                    progressView?.visible()
                }
                is Resource.Success -> {
                    progressView?.gone()
                    success.invoke(resource.data)
                }
                is Resource.Error -> {
                    progressView?.gone()
                    showSnackbar(resource.exception.message)
                }
            }
        }
    }

    fun <T : Any> handle(
        resource: Resource<T>,
        progressView: ProgressBar?,
        successRunnable: Runnable
    ) {
        when (resource) {
            is Resource.Loading -> {
                progressView?.visible()
            }
            is Resource.Success -> {
                progressView?.gone()
                successRunnable.run()
            }
            is Resource.Error -> {
                progressView?.gone()
                showSnackbar(resource.exception.message)
            }
        }
    }

    fun showSnackbar(message: String?) =
        Snackbar.make(requireView(), message ?: "Error", Snackbar.LENGTH_SHORT).show()

    override fun onDestroyView() {
        super.onDestroyView()
        if (!isViewSaved)
            _binding = null
    }

    fun getFragmentTransitionAnimation(
        popUpTo: Int? = null,
        inclusive: Boolean = false
    ): NavOptions {
        val navOptions = if (popUpTo != null) {
            NavOptions.Builder().apply {
                setPopUpTo(popUpTo, inclusive)
                setEnterAnim(R.anim.slide_in_right)
                setExitAnim(R.anim.slide_out_left)
                setPopEnterAnim(R.anim.slide_in_left)
                setPopExitAnim(R.anim.slide_out_right)
            }
        } else {
            NavOptions.Builder().apply {
                setEnterAnim(R.anim.slide_in_right)
                setExitAnim(R.anim.slide_out_left)
                setPopEnterAnim(R.anim.slide_in_left)
                setPopExitAnim(R.anim.slide_out_right)
            }
        }
        return navOptions.build()
    }
}
