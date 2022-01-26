package com.jeff.vrmz.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.jeff.vrmz.databinding.ActivityVerificationBinding

class Verification : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityVerificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityVerificationBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)




        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference


        if (auth.currentUser != null){
            auth.currentUser?.let {
                binding.tvUserEmail.text = it.email
            }
        }


        binding.btnLoginOut.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, Login::class.java))
        }

        binding.btnContinue.setOnClickListener {
            if (binding.edId.text!!.isEmpty()){
                Toast.makeText(this@Verification,"enter id number!",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else{
                checkData()
            }

        }
    }

    private fun checkData() {
        val idNo = binding.edId.text.toString()
        val database = FirebaseDatabase.getInstance().reference
        val idRef: DatabaseReference = database.child("IdNos").child(idNo)
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                if (dataSnapshot.exists() && dataSnapshot.child("ref").exists()){
                    Toast.makeText(this@Verification,"Already registered",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@Verification, Details::class.java)
                    intent.putExtra("idNo", idNo)
                    startActivity(intent)
                    finish()
                }else if (dataSnapshot.exists()){
                    val intent = Intent(this@Verification, Registration::class.java)
                    intent.putExtra("IdNo", idNo)
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(
                        this@Verification,
                        "not a registered citizen",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Toast.makeText(this@Verification,"error occurred",Toast.LENGTH_SHORT).show()
            }
        }
        idRef.addValueEventListener(postListener)
    }
}