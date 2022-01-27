package com.udacity.asteroidradar.presentation.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.data.AsteroidsRepository
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.presentation.MainViewModel

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this,
            MainViewModel.Factory(AsteroidsRepository.getInstance(requireContext())!!)
            ).get(MainViewModel::class.java)
    }
    private val asteroidAdapter = AsteroidAdapter { asteroid ->
        viewModel.currentAsteroid = asteroid
        findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        //start init view
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.refreshLayout.apply {
            setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            setOnRefreshListener {
                viewModel.downloadData()
            }
        }
        binding.asteroidRecycler.adapter = asteroidAdapter
        //end initview


        //start INITOBSERVER
        viewModel._imageOfDayEventLiveData.observe(viewLifecycleOwner, {
            Log.d("response " , "_imageOfDayEventLiveData" + it.toString())
            binding.refreshLayout.isRefreshing = false

            binding.picture = it
            it?.url?.also { imageUrl->
                Picasso.with(requireContext())
                    .load(it?.url?:"")
                    .into(binding.activityMainImageOfTheDay)
            }

        })

        viewModel._feedLiveData.observe(viewLifecycleOwner, {
            binding.refreshLayout.isRefreshing = false
            Log.d("response" ,"_feedLiveData" + it.toString())
            asteroidAdapter.submitList(it)
        })
         //end INITOBSERVER



        viewModel.downloadData()
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.onChangeFilter(
            when (item.itemId) {
                R.id.show_rent_menu -> {
                    AsteroidsRepository.FilterAsteroid.TODAY
                }
                R.id.show_all_menu -> {
                    AsteroidsRepository.FilterAsteroid.WEEK
                }
                else -> {
                    AsteroidsRepository.FilterAsteroid.ALL
                }
            }
        )
        return true
    }
}
