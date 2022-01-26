package com.jeff.vrmz.ui

import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.DatePicker
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jeff.vrmz.models.UserRegData
import com.jeff.vrmz.R
import com.jeff.vrmz.databinding.ActivityRegistrationBinding
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class Registration : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var database: DatabaseReference
    private lateinit var imageSaved: Bitmap

    private val REQUEST_IMAGE_CAPTURE = 1

    var cal = Calendar.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.tvWeb.setOnClickListener {
            startActivity(Intent(this, Web::class.java))
        }

        val intent = intent
        val idNo = intent.getStringExtra("IdNo").toString()

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker,year: Int,monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }

        binding.selectDate.setOnClickListener {
            DatePickerDialog(this,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        database = FirebaseDatabase.getInstance().getReference("IdNos")

        database.child(idNo).get().addOnSuccessListener {
            if (it.exists()){
                val firstname = it.child("firstname").value
                val lastname = it.child("lastname").value
                binding.NameEd.text = "$firstname $lastname"
                binding.idEd.text = idNo
            }else{
                Toast.makeText(this@Registration,"user doesn't exist!",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@Registration, Verification::class.java))
                finish()
            }
        }



        binding.camPicker.setOnClickListener {
            imageGet()
        }
        binding.submitBtn.setOnClickListener {
            submitForm(idNo)
        }

    }


    fun submitForm(idNo: String) {
        val gender = when(binding.radioGroup.checkedRadioButtonId){
            R.id.genderMale -> "male"
            else -> "female"
        }
        val databaseReg = FirebaseDatabase.getInstance().getReference("IdNos")



        val data = mapOf<String, String>(
            "ref" to "1",
            "date" to binding.dateEt.text.toString(),
            "age" to binding.ageEt.text.toString(),
            "gender" to gender,
            "county" to binding.countyDd.text.toString(),
            "subCounty" to binding.subcountyDd.text.toString(),
            "ward" to binding.wardDd.text.toString(),
            "location" to binding.locationDd.text.toString(),
            "votingCenter" to binding.votingCenterDd.text.toString()
        )

        databaseReg.child(idNo).updateChildren(data).addOnSuccessListener {
            Toast.makeText(this,"added successfully",Toast.LENGTH_SHORT).show()
            val intent = Intent(this@Registration, Details::class.java)
            intent.putExtra("idNo", idNo)
            startActivity(intent)
            finish()
        }.addOnFailureListener{
            Toast.makeText(this,"not saved",Toast.LENGTH_SHORT).show()
        }

    }

    private fun imageGet() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode,resultCode,data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageSaved = data!!.extras!!.get("data") as Bitmap
            binding.camPicker.setImageBitmap(imageSaved)
        }
    }

    private fun updateDateInView() {
        val myFormat = "MM-dd-yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.dateEt.text = sdf.format(cal.getTime())
    }
}