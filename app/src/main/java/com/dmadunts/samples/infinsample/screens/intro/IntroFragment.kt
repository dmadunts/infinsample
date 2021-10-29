package com.dmadunts.samples.infinsample.screens.intro

import androidx.navigation.fragment.findNavController
import com.dmadunts.samples.infinsample.R
import com.dmadunts.samples.infinsample.databinding.FragmentIntroBinding
import com.dmadunts.samples.infinsample.persistence.getAccessToken
import com.dmadunts.samples.infinsample.screens.BaseFragment
import com.dmadunts.samples.infinsample.screens.login.LoginViewModel
import org.koin.android.ext.android.inject

class IntroFragment : BaseFragment<FragmentIntroBinding>(FragmentIntroBinding::inflate) {
    override fun onCreateView() {
        binding.loginButton.setButtonText(resources.getString(R.string.SignInWithGithub))
        binding.loginButton.setOnClickAction {
            if (requireContext().getAccessToken() == null) {
                findNavController().navigate(R.id.loginDest, null, getFragmentTransitionAnimation())
            } else {
                findNavController().navigate(
                    R.id.searchDest,
                    null,
                    getFragmentTransitionAnimation()
                )
            }
        }
    }
}