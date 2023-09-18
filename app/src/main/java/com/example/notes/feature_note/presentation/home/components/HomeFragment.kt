package com.example.notes.feature_note.presentation.home.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.R
import com.example.notes.databinding.FragmentHomeBinding
import com.example.notes.feature_note.presentation.add_edit_note.AddEditViewModel
import com.example.notes.feature_note.presentation.home.components.adapter.NoteAdapter
import com.example.notes.feature_note.presentation.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.homeFragment = this@HomeFragment

        recyclerView = binding.recyclerView

        val adapter = NoteAdapter {
            AddEditViewModel.selectedNote = it
            val action = HomeFragmentDirections
                .actionHomeFragmentToAddEditFragment(id = it.id)
            findNavController().navigate(action)
        }

        recyclerView.adapter = adapter

        viewModel.notes.observe(this.viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
            }
        }

        viewModel.layoutState.observe(this.viewLifecycleOwner) {
            viewModel.isLinearLayoutManager = it
            selectLayout()
            activity?.invalidateOptionsMenu()
        }

    }

    private fun selectLayout() {
        if (viewModel.isLinearLayoutManager) {
            recyclerView.layoutManager = LinearLayoutManager(context)
        } else {
            recyclerView.layoutManager = GridLayoutManager(context, 2)
        }
    }

    private fun setIcon(menuItem: MenuItem?) {
        menuItem?.let {
            menuItem.icon =
                if (viewModel.isLinearLayoutManager)
                    ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_grid_layout)
                else
                    ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_linear_layout)
        }

    }

    fun onAddButton() {
        val action = HomeFragmentDirections
            .actionHomeFragmentToAddEditFragment(-1)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "super.onCreateOptionsMenu(menu, inflater)",
            "androidx.fragment.app.Fragment"
        )
    )

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)

        val layoutButton = menu.findItem(R.id.layoutOption)
        setIcon(layoutButton)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.layoutOption -> {
                viewModel.isLinearLayoutManager = !viewModel.isLinearLayoutManager
                selectLayout()
                setIcon(item)
                viewModel.saveLayout(requireContext())
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


}