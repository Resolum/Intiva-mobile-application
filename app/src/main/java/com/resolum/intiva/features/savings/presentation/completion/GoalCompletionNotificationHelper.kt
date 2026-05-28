package com.resolum.intiva.features.savings.presentation.completion

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.resolum.intiva.R
import com.resolum.intiva.features.savings.domain.models.SavingGoalStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Sends local notifications when a saving goal transitions to COMPLETED or UNCOMPLETED (US-022).
 *
 * Participants are notified on the device so they are aware of the goal outcome even when
 * the app is in the background.
 */
@Singleton
class GoalCompletionNotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

  private val notificationManager: NotificationManagerCompat =
      NotificationManagerCompat.from(context)

  init {
    createNotificationChannel()
  }

  /**
   * Shows a notification for the given [status] change.
   *
   * @param goalTitle Display name of the saving goal.
   * @param status    [SavingGoalStatus.COMPLETED] or [SavingGoalStatus.UNCOMPLETED].
   */
  fun notifyStatusChange(goalTitle: String, status: SavingGoalStatus) {
    when (status) {
      SavingGoalStatus.COMPLETED -> notifyGoalCompleted(goalTitle)
      SavingGoalStatus.UNCOMPLETED -> notifyGoalUncompleted(goalTitle)
      SavingGoalStatus.INPROGRESS -> Unit
    }
  }

  /** Notifies that the goal was successfully completed. */
  fun notifyGoalCompleted(goalTitle: String) {
    showNotification(
      notificationId = NOTIFICATION_ID_COMPLETED,
      title = "¡Meta cumplida!",
      message = "Has completado la meta \"$goalTitle\".",
      channelId = CHANNEL_ID_COMPLETED
    )
  }

  /** Notifies that the goal was not completed by the deadline. */
  fun notifyGoalUncompleted(goalTitle: String) {
    showNotification(
      notificationId = NOTIFICATION_ID_UNCOMPLETED,
      title = "Meta no cumplida",
      message = "La meta \"$goalTitle\" no alcanzó el monto objetivo a tiempo.",
      channelId = CHANNEL_ID_UNCOMPLETED
    )
  }

  private fun showNotification(
    notificationId: Int,
    title: String,
    message: String,
    channelId: String
  ) {
    val notification = NotificationCompat.Builder(context, channelId)
      .setSmallIcon(R.mipmap.ic_launcher)
      .setContentTitle(title)
      .setContentText(message)
      .setStyle(NotificationCompat.BigTextStyle().bigText(message))
      .setPriority(NotificationCompat.PRIORITY_DEFAULT)
      .setAutoCancel(true)
      .build()

    notificationManager.notify(notificationId, notification)
  }

  private fun createNotificationChannel() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val completedChannel = NotificationChannel(
      CHANNEL_ID_COMPLETED,
      "Metas cumplidas",
      NotificationManager.IMPORTANCE_DEFAULT
    ).apply {
      description = "Notificaciones cuando una meta de ahorro se cumple"
    }

    val uncompletedChannel = NotificationChannel(
      CHANNEL_ID_UNCOMPLETED,
      "Metas no cumplidas",
      NotificationManager.IMPORTANCE_DEFAULT
    ).apply {
      description = "Notificaciones cuando una meta de ahorro no se cumple"
    }

    manager.createNotificationChannel(completedChannel)
    manager.createNotificationChannel(uncompletedChannel)
  }

  companion object {
    private const val CHANNEL_ID_COMPLETED = "saving_goal_completed"
    private const val CHANNEL_ID_UNCOMPLETED = "saving_goal_uncompleted"
    private const val NOTIFICATION_ID_COMPLETED = 2001
    private const val NOTIFICATION_ID_UNCOMPLETED = 2002
  }
}
