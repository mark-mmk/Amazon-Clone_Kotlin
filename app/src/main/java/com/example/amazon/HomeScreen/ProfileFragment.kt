package com.example.amazon.HomeScreen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.amazon.LoginScreen.LoginPageScreen
import com.example.amazon.R
import com.example.amazon.databinding.FragmentProfileBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.ByteArrayOutputStream


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set a click listener for the Logout button
        binding.ProfileLogOutBtn.setOnClickListener {
            startActivity(Intent(requireContext(),
                LoginPageScreen::class.java))
        }
        // Set a click listener for the Setting button to Edite profile
        binding.ProfileSettingBtn.setOnClickListener {
            val dialog = BottomSheetDialog(requireContext())
            val view = layoutInflater.inflate(R.layout.edit_profile_bottom_sheet, null)
            dialog.setCancelable(false)

            dialog.setContentView(view)
            dialog.show()
            val btnClose = view.findViewById<Button>(R.id.EProfileBottomSheetBtnDismiss)

            btnClose.setOnClickListener {
                dialog.dismiss()
            }
        }

        // Set a click listener for the ImageView to change it
        binding.ProfileImageIV.setOnClickListener {
            checkPermission()
        }
    }


private fun checkPermission() {

    if (ActivityCompat.checkSelfPermission(requireContext(),
            Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED){
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CAMERA),
            1)
    } else {
        openCamera()
    }
}

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode==AppCompatActivity.RESULT_OK){
            if (requestCode==1){
                val imageBitmap = data?.extras?.get("data") as Bitmap
                binding.ProfileImageIV.setImageBitmap(imageBitmap)
                val sharedPreferences = requireActivity().getSharedPreferences("MySharedPref", AppCompatActivity.MODE_PRIVATE)
                val myEdit = sharedPreferences.edit()
                val bitmap = binding.ProfileImageIV.drawable.toBitmap()
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val byteArray = stream.toByteArray()
                myEdit.putString("image", Base64.encodeToString(byteArray, Base64.DEFAULT))
                myEdit.apply()
                Toast.makeText(requireActivity(), "Done", Toast.LENGTH_LONG).show()

            }else  if (requestCode == 0) {
                Toast.makeText(requireActivity(), "Failed", Toast.LENGTH_LONG).show()
            }
        }
    }


    override fun onResume() {
        super.onResume()
        val shared = requireActivity().getSharedPreferences("MySharedPref", AppCompatActivity.MODE_PRIVATE)
        val imageString = shared.getString("image", "")
        if (imageString!!.isNotEmpty()) {
            val byteArray = Base64.decode(imageString, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            binding.ProfileImageIV.setImageBitmap(bitmap)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}