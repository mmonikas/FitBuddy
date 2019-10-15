package com.monika.SignInAndRegister

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.monika.Enums.FirebaseRequestResult
import com.monika.HomeScreen.MainActivity
import com.monika.Model.User.User
import com.monika.R
import kotlinx.android.synthetic.main.fragment_register.*


class RegisterFragment : Fragment() {

    private val presenter = RegisterFragmentPresenter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRegisterButtonListener()
    }

    private fun setRegisterButtonListener() {
        registerFragment_nextButton.setOnClickListener {
            val userData = getInputData()
            val isInputDataValid = presenter.isDataValid(userData, registerFragment_passwordRepeatEditText.text.toString())
            if (isInputDataValid == true) {
                presenter.registerUserWithUserdata(activity as MainActivity, userData) {
                    result ->
                    if (result == FirebaseRequestResult.SUCCESS) {
                        Toast.makeText(context, R.string.registerSuccess, Toast.LENGTH_LONG).show()
                    }
                    else if (result == FirebaseRequestResult.FAILURE) {
                        Toast.makeText(context, R.string.registerFailure, Toast.LENGTH_LONG).show()
                    }
                }
            }
            else {
                Toast.makeText(context, R.string.registerErrorData, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getInputData(): User {
        var user = User()
        user.name = registerFragment_nameEditText.text.toString()
        user.email = registerFragment_emailEditText.text.toString()
        user.password = registerFragment_passwordEditText.text.toString()
        return user
    }


}