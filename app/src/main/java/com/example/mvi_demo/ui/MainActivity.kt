package com.example.mvi_demo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.mvi_demo.R
import com.example.mvi_demo.databinding.ActivityMainBinding
import com.example.mvi_demo.ui.state.FetchState
import com.example.mvi_demo.ui.state.UserIntent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupBtn()
        setupObservers()
    }

    //  I've not considered reduce() functionality, to merge it with old State.
    //  refer: https://www.raywenderlich.com/817602-mvi-architecture-for-android-tutorial-getting-started#toc-anchor-008
    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.stateFlow.collect {
                when (it.fetchState) {
                    is FetchState.NotFetched ->
                        setText(
                            text = getString(R.string.hello_world),
                            btnText = getString(R.string.click_me)
                        )

                    is FetchState.Fetching ->
                        setText(
                            text = "Loading...!",
                            btnText = "Pls, wait!"
                        )

                    is FetchState.Fetched -> {
                        println("yup size: ${it.userList?.size}")
                        val sBuilder = StringBuilder()
                        it.userList?.forEachIndexed { index, user ->
                            sBuilder.append(
                                "index: $index, userIs: $user"
                            )
                        }
                        setText(
                            text = sBuilder.toString(),
                            btnText = "All is well, click again!"
                        )
                    }

                    is FetchState.Error -> {
                        setText(
                            text = "Something went wrong! ${it.fetchState.msg}",
                            btnText = getString(R.string.click_me)
                        )
                    }
                }
            }
        }
    }

    private fun setupBtn() =
        binding.btnClickMe.setOnClickListener {
            lifecycleScope.launch {
                viewModel.intent.send(UserIntent.FetchUsers)
            }
        }

    private fun setText(text: String, btnText: String) =
        binding.run {
            tvDummy.text = text
            btnClickMe.text = btnText
        }
}
