package com.jsolutionssp.ring.service;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.jsolutionssp.ring.RingActivity;
import com.jsolutionssp.ring.RingLogics;
import com.jsolutionssp.ring.widget.WidgetProvider;

public class SetAlarms extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences settings = context.getSharedPreferences(RingActivity.PREFS_NAME, 0);
		int diary = settings.getInt("diaryAlarm", -1);
		int cycle = settings.getInt("cycleAlarm", -1);
		int startCycleDayofYear = settings.getInt("startCycleDayofYear", -1);
		int startCycleYear = settings.getInt("startCycleYear", -1);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		if (diary == 1 && startCycleDayofYear != -1 && startCycleYear != -1) {
			long putRingAlarm = RingLogics.putRingAlarm(settings);
			if (putRingAlarm != -1) {
				Intent alarmIntent = new Intent(context, PutRingAlarmTriggered.class);
				PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
				alarmManager.set(AlarmManager.RTC_WAKEUP, putRingAlarm,pendingIntent);
			}
			long removeRingAlarm = RingLogics.removeRingAlarm(settings);
			if (removeRingAlarm != -1) {
				Intent alarmIntent = new Intent(context, RemoveRingAlarmTriggered.class);
				PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
				alarmManager.set(AlarmManager.RTC_WAKEUP, removeRingAlarm,pendingIntent);
			}
		}
		if (cycle == 1 && startCycleDayofYear != -1 && startCycleYear != -1) {
			long cycleAlarm = RingLogics.cycleAlarm(settings);
			if (cycleAlarm != -1) {
				Intent alarmIntent = new Intent(context, CycleAlarmTriggered.class);
				PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
				alarmManager.set(AlarmManager.RTC_WAKEUP, cycleAlarm,pendingIntent);
			}
		}
		GregorianCalendar date = new GregorianCalendar();
		//Update widget info
		WidgetProvider.infoUpdate(RingLogics.isRingDay(settings, 
				date.get(Calendar.DAY_OF_YEAR), date.get(Calendar.YEAR)));
		WidgetProvider.updateWidgetContent(context,
			    AppWidgetManager.getInstance(context));
		//Set next alarm if not changed before
		date.set(Calendar.DAY_OF_YEAR, (date.get(Calendar.DAY_OF_YEAR) + 1));
		date.set(Calendar.HOUR_OF_DAY, 0);
		date.set(Calendar.MINUTE, 0);
		long alarm = date.getTimeInMillis();
		Intent alarmIntent = new Intent(context, SetAlarms.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.set(AlarmManager.RTC_WAKEUP, alarm, pendingIntent);
	}
}