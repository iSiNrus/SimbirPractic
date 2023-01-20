package ru.barsik.simbirpractic.fragments

import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.decodeBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResultListener
import ru.barsik.simbirpractic.MainActivity
import ru.barsik.simbirpractic.R
import ru.barsik.simbirpractic.databinding.FragmentProfileBinding
import java.io.InputStream


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val TAG = "ProfileFragment"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener("photo") { requestKey, bundle ->
            parentFragmentManager.commit {
                replace(R.id.fragment_container, ProfileFragment())
                (requireActivity() as MainActivity).showNavigation()
//                addToBackStack(null)
            }
            if(!bundle.isEmpty) {
                val path = bundle.getString(CameraFragment.BUNDLE_PATH)
                Toast.makeText(
                    requireContext(),
                    "Get Photo From Camera Fragment $path",
                    Toast.LENGTH_SHORT
                ).show()
                val myUri = Uri.parse("content:/$path")
                Log.d(TAG, "uri= $myUri")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val bmp = ImageDecoder.createSource(requireActivity().contentResolver, myUri)
                    val bitmap = ImageDecoder.decodeBitmap(bmp)
                    binding.ivAvatar.setImageBitmap(bitmap)
                }

            }
            else
                Toast.makeText(requireContext(), "Empty", Toast.LENGTH_SHORT)
                    .show()

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
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
}