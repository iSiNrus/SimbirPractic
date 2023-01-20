package ru.barsik.simbirpractic.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.database.DataSetObserver
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import ru.barsik.simbirpractic.R

class ProfileAvatarDialog(private val listener: (DialogInterface, Int) -> Unit) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            val items = arrayListOf(
                Pair("Выбрать фото", R.drawable.ic_upload),
                Pair("Сделать снимок", R.drawable.ic_photo_camera),
                Pair("Удалить", R.drawable.ic_delete)
            )
            builder.setAdapter(
                AlarmDialogAdapter(
                    requireContext(),
                    R.layout.alarm_dialog_item,
                    items
                ),
                listener
            )
//                    _, which ->
//                when (which) {
//                    0 -> Toast.makeText(requireContext(), "Выбрать фото...", Toast.LENGTH_SHORT).show()
//                    1 -> {}
////                    Toast.makeText(requireContext(), "Камера...", Toast.LENGTH_SHORT).show()
//                    2 -> Toast.makeText(requireContext(), "Удаление...", Toast.LENGTH_SHORT).show()
//                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}