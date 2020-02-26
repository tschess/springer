package io.bahlsenwitz.springer.util

import java.io.*
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

class Zipper {

    fun from(zip: File): File {
        val destDir = File(".")
        val buffer = ByteArray(1024)
        val zis = ZipInputStream(FileInputStream(zip))
        val zipEntry: ZipEntry = zis.nextEntry

        val newFile: File = newFile(destDir, zipEntry)
        val fos = FileOutputStream(newFile)
        while (true) {
            val readBytes: Int = zis.read(buffer)
            if (readBytes == -1) {
                break
            }
            fos.write(buffer, 0, readBytes)
        }
        fos.close()
        zis.closeEntry()
        zis.close()
        return newFile
    }

    @Throws(IOException::class)
    fun newFile(destinationDir: File, zipEntry: ZipEntry): File {
        val destFile = File(destinationDir, zipEntry.name)

        val destDirPath = destinationDir.canonicalPath
        val destFilePath = destFile.canonicalPath

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw IOException("Entry is outside of the target dir: " + zipEntry.name)
        }

        return destFile
    }

    fun into(temp: File, name: String) {
        val zonedDateTime: Date = Date.from(ZonedDateTime.now(ZoneId.of("America/New_York")).toInstant())
        val date: String = SimpleDateFormat("dd-MM-yyy").format(zonedDateTime)
        val month: String = SimpleDateFormat("MM").format(zonedDateTime)
        val day: String = SimpleDateFormat("dd").format(zonedDateTime)

        val dir: File = File("..${File.separator}backup${File.separator + month + File.separator + day + File.separator}")
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