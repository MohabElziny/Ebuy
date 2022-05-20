package com.iti.android.team1.ebuy.credit_screen.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.credit_screen.CreditViewModel
import com.iti.android.team1.ebuy.databinding.FragmentCreditBinding

class CreditFragment : Fragment() {

    companion object {
        fun newInstance() = CreditFragment()
    }

    private var _binding: FragmentCreditBinding? = null
    private lateinit var viewModel: CreditViewModel
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textInputEditExpireNumber.addTextChangedListener(object : TextWatcher {
            val textInput = binding.textInputEditExpireNumber
            override fun beforeTextChanged(
                p0: CharSequence?,
                start: Int,
                added: Int,
                removed: Int
            ) {

            }

            fun addSlashMark(p0: CharSequence?, start: Int, removed: Int, added: Int) {
                when (start + added) {
                    2 -> {
                        if (start == 1) {
                            textInput.apply {
                                setText(p0.toString().plus("/"))
                                setSelection(3)
                            }
                        }
                    }
                    3 -> {
                        if (start == 2) {
                            textInput.apply {
                                setText(
                                    p0?.get(0).toString().plus(p0?.get(1).toString()).plus("/")
                                        .plus(p0?.get(2).toString())
                                )
                                setSelection(4)
                            }
                        }
                    }
                }
            }

            fun validateInsertDate(p0: CharSequence?) {
                val string = p0.toString()
                if (p0?.length == 5) {
                    if (string.split("/")[0].length == 3) {

                        textInput.apply {
                            setText(p0?.drop(1).toString())
                            setSelection(4)
                        }
                    } else if (string.split("/")[1].length == 3) {
                        print(string)
                        textInput.apply {
                            setText(p0?.dropLast(1).toString())
                            setSelection(1)

                        }
                    }
                } else if (p0?.length == 4) {
                    if (string.split("/")[0].isEmpty()) {
                        textInput.apply {
                            setText(p0?.dropLast(1).toString())
                            setSelection(0)
                        }
                    }

                }
            }

            fun removeSlashMark(p0: CharSequence?, start: Int, removed: Int, added: Int) {
                if (start == 3 && start - removed == 2) {
                    textInput.apply {
                        setText(p0.toString().replace("/", ""))
                        setSelection(2)
                    }
                }
            }

            override fun onTextChanged(
                p0: CharSequence?,
                start: Int,
                removed: Int,
                added: Int
            ) {
                if (p0?.contains('/') == false) {
                    addSlashMark(p0, start, removed, added)
                } else if (p0?.contains('/') == true && p0.length == 3) {
                    removeSlashMark(p0, start, removed, added)
                } else if (p0?.contains('/') == true) {
                    validateInsertDate(p0)
                }
            }


            override fun afterTextChanged(p0: Editable?) {
            }
        })

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CreditViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}