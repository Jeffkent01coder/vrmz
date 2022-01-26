package com.jeff.vrmz.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jeff.vrmz.databinding.ActivityDetailsBinding

class Details : AppCompatActivity() {
    lateinit var binding: ActivityDetailsBinding
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val intent = intent
        val idNo = intent.getStringExtra("idNo").toString()
        database = FirebaseDatabase.getInstance().getReference("IdNos")




        binding.idNoTv.text = idNo

        database.child(idNo).get().addOnSuccessListener {
            if (it.exists()){
                binding.firstNameTv.text = it.child("firstname").value.toString()
                binding.lastNameTv.text = it.child("lastname").value.toString()
                binding.ageTv.text = it.child("age").value.toString()
                binding.genderTv.text = it.child("gender").value.toString()
                binding.countyTv.text = it.child("county").value.toString()
                binding.subCountyTv.text = it.child("subCounty").value.toString()
                binding.wardTv.text = it.child("ward").value.toString()
                binding.locationTv.text = it.child("location").value.toString()
                binding.votingCenterTv.text = it.child("votingCenter").value.toString()
            }else{
                Toast.makeText(this@Details,"user doesn't exist!",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@Details, Verification::class.java))
                finish()
            }
        }

    }

    fun byteArrayToBitmap(data: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(data, 0, data.size)
    }
}