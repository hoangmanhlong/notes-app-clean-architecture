package com.example.notes.feature_note.presentation.add_edit_note.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notes.R
import com.example.notes.databinding.FragmentAddEditBinding
import com.example.notes.feature_note.domain.model.Note
import com.example.notes.feature_note.presentation.add_edit_note.AddEditViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class AddEditFragment : Fragment() {

    private val viewModel: AddEditViewModel by viewModels()

    private var _binding: FragmentAddEditBinding? = null

    private val binding get() = _binding!!


    private val navigationArgs: AddEditFragmentArgs by navArgs()

    private var id: Int? = null

    private lateinit var deleteButton: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        id = navigationArgs.id

        when (id) {
            -1 -> {
                binding.time.text = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"))
            }

            else -> {

                viewModel.getNote(id!!)
                viewModel.note.observe(this.viewLifecycleOwner) {
                    it?.let { note ->
                        bind(note)
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_edit, menu)
        val confirm = menu.findItem(R.id.confirm)
        deleteButton = menu.findItem(R.id.delete)
        setIcon(confirm)
    }

    private fun setIcon(menuItem: MenuItem?) {
        when (id) {
            -1 -> deleteButton.isVisible = false
            else -> deleteButton.isVisible = true
        }

        menuItem?.let {
            menuItem.isVisible = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.confirm -> {
                when (id) {
                    -1 -> addNote()
                    else -> updateNote()
                }
                true
            }

            R.id.delete -> {
                val note = AddEditViewModel.selectedNote
                note?.let {
                    deleteNote(note)
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun bind(note: Note) {
        binding.apply {
            title.setText(note.title)
            content.setText(note.content)
            time.text = note.getFormattedTime()
        }
    }

    private fun updateNote() {
        id?.let {
            viewModel.update(
                id = it,
                title = binding.title.text.toString(),
                content = binding.content.text.toString()
            )
        }
        findNavController().navigate(R.id.action_addEditFragment_to_homeFragment)
    }

    private fun addNote() {
        viewModel.add(
            title = binding.title.text.toString(),
            content = binding.content.text.toString()
        )
        findNavController().navigate(R.id.action_addEditFragment_to_homeFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun deleteNote(note: Note) {
        viewModel.deleteNote(note)
        findNavController().navigate(R.id.action_addEditFragment_to_homeFragment)
    }
}