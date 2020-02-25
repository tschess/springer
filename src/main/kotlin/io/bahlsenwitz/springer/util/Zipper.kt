package io.bahlsenwitz.springer.util

import java.io.*
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class Zipper {

    fun into(temp: File, name: String) {
        val zonedDateTime: Date = Date.from(ZonedDateTime.now(ZoneId.of("America/New_York")).toInstant())
        val date: String = SimpleDateFormat("dd-MM-yyy").format(zonedDateTime)
        val month: String = SimpleDateFormat("MM").format(zonedDateTime)
        val day: String = SimpleDateFormat("dd").format(zonedDateTime)

        val dir = File("..${File.separator}backup${File.separator + month + File.separator + day + File.separator}")
        dir.mkdirs()
        val zip: File = File(dir, "${name}.zip")
        zip.createNewFile()

        val fileOutputStream: FileOutputStream = FileOutputStream(zip, false)
        ZipOutputStream(BufferedOutputStream(fileOutputStream)).use { out ->
            val data = ByteArray(1024)
            FileInputStream(temp).use { fi ->
                BufferedInputStream(fi).use { origin ->
                    val entry = ZipEntry("${date}_${name}.csv")
                    out.putNextEntry(entry)
                    while (true) {
                        val readBytes = origin.read(data)
                        if (readBytes == -1) {
                            break
                        }
                        out.write(data, 0, readBytes)
                    }
                }
            }
        }
    }
}