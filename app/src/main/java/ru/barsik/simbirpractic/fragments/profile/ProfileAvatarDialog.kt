package ru.barsik.simbirpractic.fragments.profile

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
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