package com.alvayonara.outsched.receiver

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.AsyncTask
import android.os.Build
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.alvayonara.outsched.R
import com.alvayonara.outsched.data.source.local.entity.ScheduleEntity
import com.alvayonara.outsched.data.source.local.room.ScheduleRoomDatabase
import com.alvayonara.outsched.ui.dashboard.DashboardActivity
import com.alvayonara.outsched.utils.ConvertUtils
import java.util.*
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
        val summary = intent.getStringExtra(EXTRA_SUMMARY)
        val icon = intent.getStringExtra(EXTRA_ICON)
        val temperature = intent.getDoubleExtra(EXTRA_TEMPERATURE, 0.0)
        val address = intent.getStringExtra(EXTRA_ADDRESS)
        val latitude = intent.getStringExtra(EXTRA_LATITUDE)
        val longitude = intent.getStringExtra(EXTRA_LONGITUDE)
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

        AsyncTask.execute {
            run {
                val scheduleRoomDatabase = ScheduleRoomDatabase.getInstance(context)
                val scheduleDao = scheduleRoomDatabase.scheduleDao()

                scheduleDao.deleteScheduleByRequestCode(requestCode)
                scheduleDao.insertSchedule(scheduleReminded)
            }
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
            "You have exercise schedule today at ${ConvertUtils.convertTimeToHour(
                scheduleEntity.time
            )}. " +
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

    fun setScheduleReminder(context: Context, scheduleEntity: ScheduleEntity) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val date = ConvertUtils.convertTimeToDateFormat(scheduleEntity.time)
        val time = ConvertUtils.convertTimeToHour(scheduleEntity.time)

        val intent = Intent(context, ScheduleReminderReceiver::class.java).apply {
            putExtra(EXTRA_TIME, scheduleEntity.time)
            putExtra(EXTRA_SUMMARY, scheduleEntity.summary)
            putExtra(EXTRA_ICON, scheduleEntity.icon)
            putExtra(EXTRA_TEMPERATURE, scheduleEntity.temperature)
            putExtra(EXTRA_ADDRESS, scheduleEntity.address)
            putExtra(EXTRA_LATITUDE, scheduleEntity.latitude)
            putExtra(EXTRA_LONGITUDE, scheduleEntity.longitude)
            putExtra(EXTRA_REQUEST_CODE, scheduleEntity.requestCode)
        }

        val dateArray = date!!.split("-").toTypedArray()
        val timeArray = time!!.split(":").toTypedArray()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, Integer.parseInt(dateArray[0]))
        calendar.set(Calendar.MONTH, Integer.parseInt(dateArray[1]) - 1)
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[2]))
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            scheduleEntity.requestCode,
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
