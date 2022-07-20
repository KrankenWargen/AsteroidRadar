package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        val application = requireNotNull(this.activity).application
        val database = AsteroidDatabase.getInstance(application).asteroidDatabaseDao
        val viewModelFactory = MainViewModelFactory(database, application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)


        val adapter = MainViewAdapter(MainViewAdapter.AsteroidListener {

            viewModel.onAsteroidClicked(it)
        })
        binding.lifecycleOwner = this

        binding.viewModel = viewModel


        binding.asteroidRecycler.adapter = adapter

        viewModel.asteroid.observe(viewLifecycleOwner, Observer {
            it?.let {

                adapter.submitList(it)
            }
        })
        viewModel.weekly.observe(viewLifecycleOwner, Observer {

            it?.let {

                adapter.submitList(it)
            }
        })
        viewModel.today.observe(viewLifecycleOwner, Observer {

            it?.let {

                adapter.submitList(it)
            }
        })
        viewModel.saved.observe(viewLifecycleOwner, Observer {

            it?.let {

                adapter.submitList(it)
            }
        })
        viewModel.navigateToDetailFragment.observe(viewLifecycleOwner, Observer {
            findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
        })




        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_weekly -> viewModel.showWeekly()
            R.id.show_today -> viewModel.showToday()
            R.id.show_saved -> viewModel.showSaved()
        }
        return true
    }
}
