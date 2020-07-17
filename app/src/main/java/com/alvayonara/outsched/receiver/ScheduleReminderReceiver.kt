package com.alvayonara.outsched.receiver

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.alvayonara.outsched.R
import com.alvayonara.outsched.data.source.local.entity.ScheduleEntity
import com.alvayonara.outsched.ui.schedule.SelectScheduleActivity.Companion.EXTRA_REQUEST_CODE
import com.alvayonara.outsched.utils.ConvertUtils
import java.util.*

class ScheduleReminderReceiver : BroadcastReceiver() {

    companion object {
        const val EXTRA_TIME = "time"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val time = intent.getLongExtra(EXTRA_TIME, 0)
        val requestCode = intent.getIntExtra(EXTRA_REQUEST_CODE, 0)

        val scheduleReceiver = ScheduleEntity()
        scheduleReceiver.time = time
        scheduleReceiver.requestCode = requestCode

        // notification schedule reminder
        showNotificationSchedule(context, scheduleReceiver)
    }

    private fun showNotificationSchedule(context: Context, scheduleEntity: ScheduleEntity) {
        val channelId = "Channel_1"
        val channelName = "Schedule Reminder channel"

        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_jogging)
            .setContentTitle("Reminder Olahraga")
            .setContentText(
                "Hari ini anda memiliki jadwal olahraga pukul ${ConvertUtils.convertTimeToHour(
                    scheduleEntity.time
                )}. " +
                        "\nSelamat berolahraga!"
            )
            .setAutoCancel(true)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
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
            putExtra(EXTRA_TIME, time)
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

        val pendingIntent = PendingIntent.getBroadcast(context, scheduleEntity.requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT)
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
