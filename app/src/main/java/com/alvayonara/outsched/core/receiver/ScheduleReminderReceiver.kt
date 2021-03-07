package com.alvayonara.outsched.core.receiver

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.alvayonara.outsched.R
import com.alvayonara.outsched.core.data.source.local.entity.ScheduleEntity
import com.alvayonara.outsched.core.data.source.local.room.ScheduleRoomDatabase
import com.alvayonara.outsched.core.domain.model.Schedule
import com.alvayonara.outsched.core.utils.ConvertUtils.dateTimeConvert
import com.alvayonara.outsched.core.utils.DateFormat.Companion.FORMAT_DATE_WITH_DASH
import com.alvayonara.outsched.core.utils.DateFormat.Companion.FORMAT_ONLY_TIME
import com.alvayonara.outsched.ui.dashboard.DashboardActivity
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class ScheduleReminderReceiver : BroadcastReceiver() {

    companion object {
        const val EXTRA_TIME = "time"
        const val EXTRA_SUMMARY = "summary"
        const val EXTRA_ICON = "icon"
        const val EXTRA_TEMPERATURE = "temperature"
        const val EXTRA_ADDRESS = "address"
        const val EXTRA_LATITUDE = "latitude"
        const val EXTRA_LONGITUDE = "longitude"
        const val EXTRA_REQUEST_CODE = "request_code"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val time = intent.getLongExtra(EXTRA_TIME, 0)
        val summary = intent.getStringExtra(EXTRA_SUMMARY).orEmpty()
        val icon = intent.getStringExtra(EXTRA_ICON).orEmpty()
        val temperature = intent.getDoubleExtra(EXTRA_TEMPERATURE, 0.0)
        val address = intent.getStringExtra(EXTRA_ADDRESS).orEmpty()
        val latitude = intent.getStringExtra(EXTRA_LATITUDE).orEmpty()
        val longitude = intent.getStringExtra(EXTRA_LONGITUDE).orEmpty()
        val requestCode = intent.getIntExtra(EXTRA_REQUEST_CODE, 0)

        val scheduleReminded = ScheduleEntity(
            0,
            time,
            summary,
            icon,
            temperature,
            address,
            latitude,
            longitude,
            true,
            requestCode
        )

        Executors.newSingleThreadExecutor().execute {
            val scheduleRoomDatabase = ScheduleRoomDatabase.getInstance(context)
            val scheduleDao = scheduleRoomDatabase.scheduleDao()

            scheduleDao.updateSchedule(reminded = true, requestCode = requestCode)
        }

        // Notification schedule reminder
        showNotificationSchedule(context, scheduleReminded)

        // Wakelock
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            if (!(context.getSystemService(Context.POWER_SERVICE) as PowerManager).isInteractive) {
                val wl =
                    (context.getSystemService(Context.POWER_SERVICE) as PowerManager).newWakeLock(
                        PowerManager.ACQUIRE_CAUSES_WAKEUP,
                        context.packageName
                    )
                wl.acquire(10)
                try {
                    TimeUnit.MILLISECONDS.sleep(10)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                wl.release()
            }
        }
    }

    private fun showNotificationSchedule(context: Context, scheduleEntity: ScheduleEntity) {
        val channelId = "Channel_1"
        val channelName = "Schedule Reminder channel"

        val message =
            "You have exercise schedule today at ${
                scheduleEntity.time.dateTimeConvert(FORMAT_ONLY_TIME)
            }. " +
                    "\nEnjoy!"

        // Set big text notification
        val bigText =
            NotificationCompat.BigTextStyle()
        bigText.bigText(message)

        // Notification intent
        val pendingIntentToApp =
            PendingIntent.getActivity(context, 0, Intent(context, DashboardActivity::class.java), 0)

        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_jogging)
            .setContentIntent(pendingIntentToApp)
            .setContentTitle("Exercise Reminder")
            .setContentText(message)
            .setAutoCancel(true)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)
            .setStyle(bigText)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(channelId)
            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(scheduleEntity.requestCode, notification)
    }

    fun setScheduleReminder(context: Context, schedule: Schedule) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val date = schedule.time.dateTimeConvert(FORMAT_DATE_WITH_DASH)
        val time = schedule.time.dateTimeConvert(FORMAT_ONLY_TIME)

        val intent = Intent(context, ScheduleReminderReceiver::class.java).apply {
            putExtra(EXTRA_TIME, schedule.time)
            putExtra(EXTRA_SUMMARY, schedule.summary)
            putExtra(EXTRA_ICON, schedule.icon)
            putExtra(EXTRA_TEMPERATURE, schedule.temperature)
            putExtra(EXTRA_ADDRESS, schedule.address)
            putExtra(EXTRA_LATITUDE, schedule.latitude)
            putExtra(EXTRA_LONGITUDE, schedule.longitude)
            putExtra(EXTRA_REQUEST_CODE, schedule.requestCode)
        }

        val dateArray = date.split("-").toTypedArray()
        val timeArray = time.split(":").toTypedArray()

        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, Integer.parseInt(dateArray[0]))
            set(Calendar.MONTH, Integer.parseInt(dateArray[1]) - 1)
            set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[2]))
            set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
            set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
            set(Calendar.SECOND, 0)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            schedule.requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    fun cancelAlarm(context: Context, requestCode: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ScheduleReminderReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
    }
}
