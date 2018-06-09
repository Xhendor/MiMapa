package com.uabc.edu.mapa

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.support.design.widget.CoordinatorLayout.Behavior.setTag
import kotlinx.coroutines.experimental.Job




class MyFirebaseMessagingService : FirebaseMessagingService() {


    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("SERVICIO", "From: " + remoteMessage!!.from!!)

        // Check if message contains a data payload.
        if (remoteMessage.data.size > 0) {
            Log.d("SERVICIO", "Message data payload: " + remoteMessage.data)

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
               // scheduleJob()
            } else {
                // Handle message within 10 seconds
               // handleNow()
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.d("SERVICIO", "Message Notification Body: " + remoteMessage.notification!!.body!!)
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }




}
