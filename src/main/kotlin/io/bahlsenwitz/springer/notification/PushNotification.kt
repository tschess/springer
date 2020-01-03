package io.bahlsenwitz.springer.notification

import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.json.JSONArray
import org.json.JSONObject

class PushNotification {

    fun execute(id: String, message: String, title: String) {

        val pushNotification0 = JSONObject()
        pushNotification0.put("aps",JSONObject().put("alert",JSONObject().put("title",title).put("body",message)))

        val pushNotification1 = JSONObject()
        pushNotification1.put("interests", JSONArray().put(id))
        pushNotification1.put("apns", pushNotification0)

        val httpClient = HttpClients.createDefault()
        try {
            try {
                val postRequest =
                    HttpPost("https://33e7c056-ccd1-4bd1-ad69-0e2f0af28a69.pushnotifications.pusher.com/publish_api/v1/instances/33e7c056-ccd1-4bd1-ad69-0e2f0af28a69/publishes")
                postRequest.setHeader("Accept", "application/json")
                postRequest.setHeader("Content-type", "application/json")
                postRequest.setHeader(
                    "Authorization",
                    "Bearer 4DF05F290820E01105CF61F6AC0B202958511179E29761EAACA82A0CAF5739D9"
                )
                val entity = StringEntity(pushNotification1.toString())
                postRequest.entity = entity
                val response = httpClient.execute(postRequest)
                println("PUSH NOTIFICATION: " + response.statusLine.statusCode)
            } finally {
                httpClient.close()
            }
        } catch (e: Exception) {
            println("PUSH NOTIFICATION: " + e.localizedMessage)
        }

    }
}