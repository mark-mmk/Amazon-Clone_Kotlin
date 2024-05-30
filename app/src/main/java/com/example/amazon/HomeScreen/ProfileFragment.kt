package com.example.amazon.HomeScreen

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.amazon.LoginScreen.LoginPageScreen
import com.example.amazon.R
import com.example.amazon.databinding.FragmentProfileBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.io.File

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val userId = firebaseAuth.currentUser!!.uid
        val ref = db.collection("users").document(userId).get()
        ref.addOnSuccessListener {
            if (it != null) {
                val name = it.data?.get("Email").toString()
                val user = it.data?.get("User Name").toString()
                val phone = it.data?.get("Phone").toString()
                binding.textView3.text = "Email : " + name
                binding.textView4.text = "User Name : " + user
                binding.textView5.text = "Phone : " + phone
            }
        }.addOnFailureListener {
            Log.w(ContentValues.TAG, "Error getting documents: ", it)
        }

        binding.camera.setOnClickListener {

            val dialogView = layoutInflater.inflate(R.layout.custom_dialoge, null)
            val builder = AlertDialog.Builder(requireActivity())
            builder.setView(dialogView)
            val Dialog = builder.create()
            Dialog.setCancelable(false)
            val take = dialogView.findViewById<TextView>(R.id.take_photo)
            val choose = dialogView.findViewById<TextView>(R.id.gallery)
            val negativeButton = dialogView.findViewById<Button>(R.id.back)
            take.setOnClickListener {
                if (ActivityCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.CAMERA),
                        1
                    )
                } else {
                    val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(i, 1)
                }
            }
            choose.setOnClickListener {
                val i = Intent(Intent.ACTION_PICK)
                i.type = "image/*"
                startActivityForResult(i, 100)
            }
            negativeButton.setOnClickListener {
                Dialog.dismiss()
//                Toast.makeText(requireActivity(), "Back", Toast.LENGTH_SHORT).show()
            }
            Dialog.show()
        }
        binding.logout.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(
                requireActivity(),
                LoginPageScreen::class.java
            )
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == 1) {
                val imageBitmap = data?.extras?.get("data") as Bitmap
                binding.profileImage.setImageBitmap(imageBitmap)
                val userId = FirebaseAuth.getInstance().currentUser!!.uid
                val storage = Firebase.storage
                val storageRef = storage.reference
                val mountainsRef = storageRef.child("profile/$userId")
                val baos = ByteArrayOutputStream()
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                val uploadTask = mountainsRef.putBytes(data)
                uploadTask.addOnSuccessListener { taskSnapshot ->
                    val downloadUrl = taskSnapshot.storage.downloadUrl
                    downloadUrl.addOnSuccessListener { url ->

                        Toast.makeText(requireActivity(), "Done", Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(requireActivity(), "Error", Toast.LENGTH_LONG).show()
                }
            }
            if (requestCode == 100) {
                binding.profileImage.setImageURI(data?.data)
                val userId = FirebaseAuth.getInstance().currentUser!!.uid
                val storage = Firebase.storage
                val storageRef = storage.reference
                val mountainsRef = storageRef.child("profile/$userId")
                val baos = ByteArrayOutputStream()
                val data = baos.toByteArray()
                val uploadTask = mountainsRef.putBytes(data)
                uploadTask.addOnSuccessListener { taskSnapshot ->
                    val downloadUrl = taskSnapshot.storage.downloadUrl
                    downloadUrl.addOnSuccessListener { url ->
                        Toast.makeText(requireActivity(), "Done", Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(requireActivity(), "Error", Toast.LENGTH_LONG).show()
                }
            }
        }
        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == 0) {
                Toast.makeText(requireActivity(), "Failed", Toast.LENGTH_LONG).show()
            }

        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child("profile/$userId")
        mountainsRef.getBytes(Long.MAX_VALUE).addOnSuccessListener { bytes ->
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            binding.profileImage.setImageBitmap(bitmap)
        }.addOnFailureListener { exception ->
            Toast.makeText(requireActivity(), "Error", Toast.LENGTH_LONG).show()
        }
    }
}