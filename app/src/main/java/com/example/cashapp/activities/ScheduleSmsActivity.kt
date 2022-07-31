package com.example.cashapp.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TimePicker
import com.example.cashapp.R
import com.example.cashapp.utils.SmsScheduler
import com.example.cashapp.utils.Toaster
import java.util.*

class ScheduleSmsActivity : AppCompatActivity() {

    private lateinit var timePicker: TimePicker
    private lateinit var buttonSetSmsHour: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_sms)

        timePicker = findViewById(R.id.timePickerSmsHour)
        buttonSetSmsHour = findViewById(R.id.buttonSetSmsHour)

        buttonSetSmsHour.setOnClickListener() {
            val calendar = Calendar.getInstance()

            calendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                timePicker.hour,
                timePicker.minute,
                0
            )

            setAlarm(calendar.timeInMillis)
        }

    }

    private fun setAlarm(timeInMillis: Long) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, SmsScheduler::class.java)

        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

        alarmManager.setRepeating(AlarmManager.RTC, timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)

        Toaster.toast("Sms schedule is set", this)
    }
}