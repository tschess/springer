package io.bahlsenwitz.springer

//import org.apache.http.client.methods.HttpGet
//import org.apache.http.impl.client.HttpClients
//import org.json.JSONObject
//import org.junit.Test
//import java.io.InputStream
//import java.nio.charset.Charset

class UnitTest {

//    @Test
//    fun xxxx() {
//
//        val xxx = execute("0x32652acbdf4483abAE879d054311cAC916e505dB")
//        println("\n\n\n\n\nxxx: ${xxx}")
//
//        val yyy = execute("0x3b66507597116b9bc4083a5D40Cc8a38D2a52F24")
//        println("\n\n\n\n\nyyy: ${yyy}")
//
//        val zzz = execute("0x171664F9Cc0226F023E02A351F97751D78ead3dc")
//        println("\n\n\n\n\nzzz: ${zzz}")
//
//    }
//
//    fun execute(address: String): Boolean {
//        val httpClient = HttpClients.createDefault()
//        val request =
//            HttpGet("https://api.etherscan.io/api?module=account&action=tokenbalance&contractaddress=0x13F74043eA61FE1BC62966A43f9cE9abbAD884E9&address=${address}&tag=latest&apikey=G2BQF152Q8VT726472MMKGE11P45WED1DJ")
//        try {
//            httpClient.use { client ->
//                val response = client.execute(request)
//                val inputStream: InputStream = response.entity.content!!
//                val content = inputStream.readBytes().toString(Charset.defaultCharset())
//                println(content)
//                val jsonObject = JSONObject(content)
//                val tschx = jsonObject["result"] ?: return false
//                if(tschx.toString().toInt() > 0){
//                    return true
//                }
//                return false
//            }
//        } catch (e: Exception) {
//            // TODO: handle error...
//        }
//        return false
//    }

}

