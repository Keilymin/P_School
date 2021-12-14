package com.burlakov.week1application.activities


import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.burlakov.week1application.MyApplication
import com.burlakov.week1application.R
import com.burlakov.week1application.util.UserUtil
import com.burlakov.week1application.viewmodels.LogInViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LogInFragment : Fragment() {

    lateinit var singInButton: Button
    lateinit var usernameEditText: EditText
    private val logInViewModel: LogInViewModel by viewModel()
    lateinit var actv : MenuActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_log_in, container, false)
        actv = activity as MenuActivity

        singInButton = root.findViewById(R.id.singIn)
        usernameEditText = root.findViewById(R.id.username)

        logInViewModel.logInResult.observe(this, {
            if (it == true) {
                actv.unlockDrawerAndSetName()
                UserUtil.saveUser(requireContext(), MyApplication.curUser!!)
                findNavController().navigate(R.id.to_menu)
            }
        })

        singInButton.setOnClickListener {
            if (usernameEditText.text.toString().trim().isNotEmpty()) {
                logInViewModel.singIn(usernameEditText.text.toString())
            }
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        actv.lockDrawer()
    }
}
