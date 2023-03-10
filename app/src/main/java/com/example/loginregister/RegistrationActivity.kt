package com.example.loginregister

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_reg.*

class RegistrationActivity : AppCompatActivity() {

    private var userList = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg)

        val shared: SharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
        val edit = shared.edit()
        val gson = Gson()
        val convert = object : TypeToken<List<User>>() {}.type

        val intent = Intent(this, LoginActivity::class.java)

        signInReg.setOnClickListener {
            startActivity(intent)
        }

        signUpReg.setOnClickListener {
            var users = shared.getString("users", "")
            if (users == "") {
                userList = mutableListOf()
            } else {
                userList = gson.fromJson(users, convert)
            }

            if (validate()) {
                userList.add(User(nameInputReg.text.toString(), passwordInputReg.text.toString()))

                val str = gson.toJson(userList)
                edit.putString("users", str).apply()

                Toast.makeText(this,"Successfully registered",Toast.LENGTH_SHORT).show()

                startActivity(intent)
            }
        }

    }

    private fun validate(): Boolean {
        if (nameInputReg.text.toString().isEmpty() || passwordInputReg.text.toString().isEmpty()) {
            Toast.makeText(this, "Fill the form fully", Toast.LENGTH_SHORT).show()
            return false
        }
        for (i in userList.indices) {
            if (nameInputReg.text.toString() == userList[i].name) {
                Toast.makeText(this, "User with this username already registered", Toast.LENGTH_SHORT).show()
                return false
            }
        }

        return true
    }
}