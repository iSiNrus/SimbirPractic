package ru.barsik.simbirpractic.fragments.profile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResultListener
import ru.barsik.simbirpractic.MainActivity
import ru.barsik.simbirpractic.R
import ru.barsik.simbirpractic.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener("photo") { _, bundle ->
            parentFragmentManager.commit {
                replace(R.id.fragment_container, ProfileFragment())
                (requireActivity() as MainActivity).showNavigation()
            }
            if (!bundle.isEmpty) {
                val path = bundle.getString(CameraFragment.BUNDLE_PATH)
                Toast.makeText(
                    requireContext(),
                    "Get Photo From Camera Fragment $path",
                    Toast.LENGTH_SHORT
                ).show()

                val bitmap = BitmapFactory.decodeFile(path)
                AVATAR_BITMAP = bitmap
            } else
                Toast.makeText(requireContext(), "Empty", Toast.LENGTH_SHORT).show()

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        if(AVATAR_BITMAP ==null) AVATAR_BITMAP =  BitmapFactory.decodeResource(resources, R.drawable.image_man)
        binding.ivAvatar.setImageBitmap(AVATAR_BITMAP)
        binding.ivAvatar.setOnClickListener {
            val dialog = ProfileAvatarDialog { _, which ->
                when (which) {
                    0 -> Toast.makeText(requireContext(), "Выбрать фото...", Toast.LENGTH_SHORT)
                        .show()
                    1 -> parentFragmentManager.commit {
                        replace(R.id.fragment_container, CameraFragment())
                        (requireActivity() as MainActivity).hideNavigation()
                        addToBackStack(null)
                    }
                    2 -> Toast.makeText(requireContext(), "Удаление...", Toast.LENGTH_SHORT).show()
                }
            }
            dialog.show(parentFragmentManager, "avatar")
        }
        return binding.root
    }

    companion object {
        private var AVATAR_BITMAP : Bitmap? = null
    }
}