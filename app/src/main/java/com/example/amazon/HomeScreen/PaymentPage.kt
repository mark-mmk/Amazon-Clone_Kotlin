
package com.example.amazon.HomeScreen

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.fragment.navArgs
import com.example.amazon.MainActivity
import com.example.amazon.ProductsDescriptionArgs
import com.example.amazon.databinding.FragmentPaymentPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.paypal.android.sdk.payments.PayPalConfiguration
import com.paypal.android.sdk.payments.PayPalPayment
import com.paypal.android.sdk.payments.PayPalService
import com.paypal.android.sdk.payments.PaymentActivity
import com.paypal.android.sdk.payments.PaymentConfirmation

import org.json.JSONException
import java.math.BigDecimal

class PaymentPage : Fragment() {
    private var _binding: FragmentPaymentPageBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    val args : PaymentPageArgs by navArgs()
    private val config = PayPalConfiguration()
        .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
        .clientId("AW1TdvpSGbIM5iP4HJNI5TyTmwpY9Gv9dYw8_8yW5lYIbCqf326vrkrp0ce9TAqjEGMHiV3OqJM_aRT0")

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
        val price =args.price
        binding.pricePayment.setText(price)

        binding.buy.setOnClickListener {
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
                PendingIntent.getActivities(
                    requireActivity(),
                    20,
                    arrayOf(intent),
                    PendingIntent.FLAG_IMMUTABLE
                )
            var sendNotification = NotificationCompat.Builder(requireActivity(), "channel_one")
                .setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setContentTitle("Amazon@gmail.com")
                .setContentText("Our dear client ${binding.usernamePayment.text.toString()} successfully purchased by credit card Number ${binding.cardPayment.text.toString()}")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .build()
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    100
                )
            } else {
                NotificationManagerCompat.from(requireContext()).notify(1, sendNotification)
            }
        }
        binding.paybal.setOnClickListener {
            val payment = PayPalPayment(
                BigDecimal("100.12"),
                "USD",
                "The payment transaction description.",
                PayPalPayment.PAYMENT_INTENT_SALE
            )

            val intent = Intent(requireActivity(), PaymentActivity::class.java)
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config)
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment)
            startActivityForResult(intent, 0)
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