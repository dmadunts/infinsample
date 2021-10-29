package com.dmadunts.samples.infinsample.screens.login

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import com.dmadunts.samples.infinsample.R
import com.dmadunts.samples.infinsample.databinding.FragmentLoginBinding
import com.dmadunts.samples.infinsample.persistence.getAccessToken
import com.dmadunts.samples.infinsample.persistence.setAccessToken
import com.dmadunts.samples.infinsample.remote.utils.NetworkManager
import com.dmadunts.samples.infinsample.screens.BaseFragment
import com.dmadunts.samples.infinsample.utils.Constants
import org.koin.android.ext.android.inject

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    private val redirectUri = "infinsample://callback"
    private val clientSecret = "82e39c906ce3ea34b553e237d8ad381bca9a217c"
    private val clientId = "78c3b3e17f734cf571af"
    private val viewModel: LoginViewModel by inject()

    override fun onCreateView() {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (requireContext().getAccessToken() == null) {
            binding.webView.webViewClient = object : WebViewClient() {
                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    super.onReceivedError(view, request, error)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (error?.description.toString() == "net::ERR_UNKNOWN_URL_SCHEME") {
                            view?.loadUrl("about:blank");
                        }
                    } else {
                        findNavController().navigate(
                            R.id.searchDest,
                            null,
                            getFragmentTransitionAnimation()
                        )
                    }
                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    try {
                        if (url?.startsWith(redirectUri) == true) {
                            url.split("code=")[1]?.let { code ->
                                view?.stopLoading()
                                viewModel.getAccessToken(clientId, clientSecret, code)
                            }
                        } else {
                            return
                        }
                    } catch (e: Exception) {

                    }
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)

                }
            }
            binding.webView.loadUrl("https://github.com/login/oauth/authorize?client_id=$clientId&scope=repo&redirect_uri=$redirectUri&allow_signup=false")
        } else {
            findNavController().navigate(R.id.searchDest, null, getFragmentTransitionAnimation())
        }

        viewModel.accessToken.observe(viewLifecycleOwner, {
            handleSingle(it, null) { accessToken ->
                NetworkManager.switchBaseUrl(Constants.API_BASE_URL)
                requireContext().setAccessToken(accessToken.accessToken)
                findNavController().navigate(
                    R.id.searchDest,
                    null,
                    getFragmentTransitionAnimation()
                )
                findNavController().popBackStack(R.id.searchDest, false)
            }
        })
    }
}