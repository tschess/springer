package io.bahlsenwitz.springer.ethereum

import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.json.JSONObject
import java.io.InputStream
import java.nio.charset.Charset

class EthPollingAgent {

    fun hasToken(address: String): Boolean {
        val httpClient = HttpClients.createDefault()
        val request =
            HttpGet("https://api.etherscan.io/api?module=account&action=tokenbalance&contractaddress=0x13F74043eA61FE1BC62966A43f9cE9abbAD884E9&address=${address}&tag=latest&apikey=G2BQF152Q8VT726472MMKGE11P45WED1DJ")
        try {
            httpClient.use { client ->
                val response = client.execute(request)
                val inputStream: InputStream = response.entity.content!!
                val content = inputStream.readBytes().toString(Charset.defaultCharset())
                println(content)
                val jsonObject = JSONObject(content)
                val tschx = jsonObject["result"] ?: return false
                if(tschx.toString().toInt() > 0){
                    return true
                }
                return false
            }
        } catch (e: Exception) {
            // TODO: handle error...
        }
        return false
    }
}