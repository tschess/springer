package io.bahlsenwitz.springer.push

import java.security.KeyFactory
import java.security.PrivateKey
import java.security.Signature
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*

class Pusher() {

    private val development: String = "api.sandbox.push.apple.com:443"
    private val production: String = "api.push.apple.com:443"

    private val key: Key = Key()

    //fun ios(player: Player) {
    fun test() {

        val path: String = "/3/device/"

        val header00: MutableMap<String, String> = mutableMapOf()
        header00["alg"] = "ES256"
        header00["kid"] = key.idKey

        val millis: Long = System.currentTimeMillis()
        val time: Long = millis / 1000

        val claims: MutableMap<String, String> = mutableMapOf()
        claims["iss"] = key.idTeam
        claims["iat"] = "$time"

        val jwt00: String = "$header00.$claims"
        // Remove the "BEGIN" and "END" lines, as well as any whitespace
        var pkcs8Pem: String = key.value
        pkcs8Pem = pkcs8Pem.replace("-----BEGIN PRIVATE KEY-----", "")
        pkcs8Pem = pkcs8Pem.replace("-----END PRIVATE KEY-----", "")
        pkcs8Pem = pkcs8Pem.replace("\\s+".toRegex(), "")
        // Base64 decode the result
        val pkcs8EncodedBytes: ByteArray = Base64.getDecoder().decode(pkcs8Pem)
        // extract the private key
        val keySpec = PKCS8EncodedKeySpec(pkcs8EncodedBytes)
        val kf: KeyFactory = KeyFactory.getInstance("RSA")
        val privKey: PrivateKey = kf.generatePrivate(keySpec)
        val signer: Signature = Signature.getInstance("SHA256withRSA")
        signer.initSign(privKey) // PKCS#8 is preferred
        signer.update(jwt00.toByteArray())
        val signature: ByteArray = signer.sign()
        val jwt02: String = Base64.getEncoder()
            .encodeToString(signature)!!
            .replace("+/", "-_")
            .replace("=", "")
        val jwt: String = "$header00.$claims.$jwt02"

        val header01: MutableMap<String, String> = mutableMapOf()
        header01["authorization"] = "bearer $jwt"
        header01["apns-topic"] = key.idBundle

        val payload: String = "{\"aps\":{\"alert\":\"your move...999\",\"badge\":\"1\",\"content-available\":1}}"

        val token: String = "9c06a3a757faf8ee173fb8f6b631160db1a2dd8c43f3f4a083d3dbb88034a76a"
        try {
            //khttp.post(headers = header01, url = "$development$path${player.note_key}", data = payload)

            khttp.post(headers = header01, url = "$development$path${token}", data = payload)


        } catch (e: Exception) {
            print(e.localizedMessage)
        }
    }


}