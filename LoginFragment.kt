package com.example.roomease

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_login, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val btnLogin = view.findViewById<Button>(R.id.btnLogin)
        val tvPasteCode = view.findViewById<TextView>(R.id.tvPasteCode)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            if (isValidEmail(email)) {
                findNavController().navigate(R.id.action_login_to_roomList)
            } else {
                etEmail.error = "Enter a valid email address"
            }
        }

        tvPasteCode.setOnClickListener {
            // Simulate pasting code logic
            Toast.makeText(requireContext(), "Room code pasted from clipboard!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_login_to_roomList)
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.contains(".")
    }
}