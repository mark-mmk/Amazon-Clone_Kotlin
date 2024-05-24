package com.example.amazon.HomeScreen

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.amazon.MainActivity
import com.example.amazon.databinding.FragmentPaymentPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PaymentPage : Fragment() {
    private var _binding: FragmentPaymentPageBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    @SuppressLint("NewApi")
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
                binding.emailPayment.setText(name)
                binding.usernamePayment.setText(user)
                binding.phonePayment.setText(phone)
            }
        }.addOnFailureListener {
            Log.w(ContentValues.TAG, "Error getting documents: ", it)
        }
        val channel = NotificationChannel(
            "channel_one",
            "Channel One",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            "Notifications"
        }
        val notificationManger =
            requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManger.createNotificationChannel(channel)
        val intent = Intent(requireActivity(), MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivities(requireActivity(), 20, arrayOf(intent), PendingIntent.FLAG_IMMUTABLE)
        var sendNotification = NotificationCompat.Builder(requireActivity(), "channel_one")
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle("Amazon@gmail.com")
            .setContentText("Our sear client ${binding.usernamePayment.text.toString()} successfully purchased by credit card ${binding.cardPayment.text.toString()}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .build()
        binding.buy.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    100
                )
            } else {
                NotificationManagerCompat.from(requireActivity()).notify(1, sendNotification)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPaymentPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}