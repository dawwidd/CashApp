package com.example.cashapp.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class SmsScheduler : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Toaster.toast("Tutaj powinien być wysłany sms", context!!)
        Log.e("SMS", "Tutaj powinien być wysłany sms")
    }
}