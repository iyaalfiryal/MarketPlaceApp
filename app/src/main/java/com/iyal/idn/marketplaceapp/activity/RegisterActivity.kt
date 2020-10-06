package com.iyal.idn.marketplaceapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.iyal.idn.marketplaceapp.R
import com.iyal.idn.marketplaceapp.model.Users
import com.iyal.idn.marketplaceapp.utils.Constan
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.startActivity

class RegisterActivity : AppCompatActivity() {

    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        btn_signup.setOnClickListener {
            if (et_email_signup.text.isNotEmpty() &&
                    et_name_signup.text.isNotEmpty() &&
                    et_hp_signup.text.isNotEmpty() &&
                    et_password_signup.text.isNotEmpty() &&
                    et_confirm_pass_signup.text.isNotEmpty()
            ){
                authUserSignUp(
                    et_email_signup.text.toString(),
                    et_password_signup.text.toString()
                )
            }
        }
    }

    //proses authentication
    private fun authUserSignUp(email: String, pass: String): Boolean {

        auth = FirebaseAuth.getInstance()

        var status: Boolean? = null
        val tag = "tag"

        auth?.createUserWithEmailAndPassword(email, pass)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (insertUser(
                            et_name_signup.text.toString(),
                            et_email_signup.text.toString(),
                            et_hp_signup.text.toString(),
                            task.result?.user!!)){
                        startActivity<LoginActivity>()
                    }
                }else{
                    status = false
                }
            }
        return status!!
    }

    //proses menambahkan data user ke realtime database
    private fun insertUser(name: String, email: String,
                           hp: String, users: FirebaseUser): Boolean {

        var user = Users()
        user.uid = users.uid
        user.name = name
        user.email = email
        user.hp = hp

        val database = FirebaseDatabase.getInstance()
        val key = database.reference.push().key
        val myRef = database.getReference(Constan.tb_user)

        myRef.child(key!!).setValue(user)
        return true

    }
}