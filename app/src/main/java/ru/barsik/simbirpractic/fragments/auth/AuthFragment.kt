package ru.barsik.simbirpractic.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jakewharton.rxbinding.widget.RxTextView
import ru.barsik.simbirpractic.MainActivity
import ru.barsik.simbirpractic.databinding.FragmentAuthBinding
import ru.barsik.simbirpractic.fragments.CategoriesFragment
import rx.Observable
import rx.android.schedulers.AndroidSchedulers

class AuthFragment : Fragment() {

    private lateinit var binding: FragmentAuthBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(layoutInflater)
        binding.btnSubmit.isEnabled = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val observePass = RxTextView.textChanges(binding.etPassword)
        val observeEmail = RxTextView.textChanges(binding.etEmail)

        Observable.combineLatest(observeEmail, observePass)
        { x, y ->
            Pair(x.toString(), y.toString())
        }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                binding.btnSubmit.isEnabled = (it.first.length >= 6 && it.second.length >= 6)
            }

        binding.btnSubmit.setOnClickListener {
            (requireActivity() as MainActivity).switchFragment(CategoriesFragment(), addBackStack = false, showBottomNavigation = true)
        }
    }

}