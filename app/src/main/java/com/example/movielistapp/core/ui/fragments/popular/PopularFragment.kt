package com.example.movielistapp.core.ui.fragments.popular

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movielistapp.R
import com.example.movielistapp.core.ui.adapters.movie.MovieAdapter
import com.example.movielistapp.core.ui.adapters.movie.SetOnClickMovie
import com.example.movielistapp.core.ui.fragments.BindingFragment
import com.example.movielistapp.core.ui.fragments.SharedViewModel
import com.example.movielistapp.core.ui.uiState.MovieState
import com.example.movielistapp.databinding.FragmentPopularBinding
import com.example.movielistapp.domain.models.Movie
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PopularFragment : BindingFragment<FragmentPopularBinding>(FragmentPopularBinding::inflate),
    SetOnClickMovie {

    private lateinit var movieAdapter: MovieAdapter
    private val viewModel: PopularViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieAdapter = MovieAdapter()
        movieAdapter.injectSetOnClickMovie(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        useBinding {
            it.apply {
                lifecycleScope.launch {
                    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                        viewModel._mutableMovieState.collect { movieState ->
                            when (movieState) {
                                is MovieState.Loading -> {
                                    if (movieState.isLoading) {
                                        Log.d("dapgoooo", "Laoding")

                                        loadingBar.visibility = View.VISIBLE
                                    } else {
                                        Log.d("dapgoooo", "finish Laoding")

                                        loadingBar.visibility = View.GONE

                                    }
                                }

                                is MovieState.Empty -> {
                                    Log.d("dapgoooo", "Empty")

                                    if (movieState.isEmpty) {
                                        makeToastFromStr("No Data")
                                    }
                                }

                                is MovieState.Success -> {
                                    Log.d("dapgoooo", "Success")

                                    val gridLayoutManager = GridLayoutManager(context, 2)
                                    movieAdapter.injectMoviesList(movieState.moviesList)
                                    rvPopularMovies.layoutManager = gridLayoutManager
                                    rvPopularMovies.adapter = movieAdapter
                                }

                                is MovieState.Error -> {
                                    makeToastFromStr(movieState.errMsg)
                                }

                                is MovieState.ResError -> {
                                    makeToastFromRes(movieState.errMsg)
                                }
                            }
                        }


                    }

                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("dapgoooo", "onResume")
        viewModel.getPopularMovies()

    }

    override fun onStart() {
        super.onStart()
        Log.d("dapgoooo", "Started")

    }

    override fun onClick(movie: Movie) {
        sharedViewModel.setMovie(movie)
        Log.d("dapgoooooooo", "onClick: ${sharedViewModel.getMovie()}")

        findNavController().navigate(R.id.action_mainFragment_to_movieDetailsFragment2)
    }

}