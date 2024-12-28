package pl.swietek.law_firm.storage

import lombok.AllArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import org.springframework.util.ResourceUtils
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.text.SimpleDateFormat
import java.util.*

@Service
@AllArgsConstructor
class FileSystemStorage : Storage {

    @Value("\${storage.local.base-path}")
    private val basePath: String? = null

    @Autowired
    private val resourceLoader: ResourceLoader? = null

    @Async
    override fun store(file: MultipartFile?, destinationPath: String?) {
        if (file == null || destinationPath == null) {
            throw IllegalArgumentException("File or destination path must not be null")
        }

        try {
            println(basePath)
            val destination = basePath?.let { Paths.get(it, destinationPath) }
            if (destination != null) {
                Files.createDirectories(destination.parent)
                Files.copy(file.inputStream, destination, StandardCopyOption.REPLACE_EXISTING)
            }
        } catch (e: IOException) {
            throw RuntimeException("Failed to store file: ${e.message}", e)
        }
    }

    override fun load(filePath: String?): Resource {
        try {
            if (filePath.isNullOrBlank()) {
                throw IllegalArgumentException("File path must not be null or blank")
            }

            val path = Paths.get(basePath, filePath)
            if (!Files.exists(path) || !Files.isReadable(path)) {
                throw RuntimeException("File not found or not readable: $filePath")
            }

            return FileSystemResource(path)
        } catch (e: Exception) {
            throw RuntimeException("Error loading resource: " + e.message)
        }
    }

    override fun delete(filePath: String?) {
        try {
            val fileToDelete: File = ResourceUtils.getFile((basePath + File.separator).toString() + filePath)

            if (fileToDelete.exists() && fileToDelete.isFile()) {
                if (!fileToDelete.delete()) {
                    throw RuntimeException("Failed to delete file.")
                }
            } else {
                throw FileNotFoundException("File not found or is not a file.")
            }
        } catch (e: IOException) {
            throw RuntimeException("Failed to delete file: " + e.message)
        }
    }

    override fun exists(filePath: String?): Boolean {
        try {
            if (filePath.isNullOrBlank()) return false

            val path = basePath?.let { Paths.get(it, filePath) }
            return Files.exists(path!!) && Files.isRegularFile(path)
        } catch (e: IOException) {
            return false
        }
    }


    override fun move(sourcePath: String?, destinationPath: String?) {
        try {
            val source = Paths.get(basePath!!, sourcePath)
            val destination = Paths.get(basePath, destinationPath)

            Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING)
        } catch (e: IOException) {
            throw RuntimeException("Failed to move file or directory: " + e.message)
        }
    }

    override fun rename(currentPath: String?, newName: String?) {
        try {
            if (currentPath.isNullOrBlank() || newName.isNullOrBlank()) {
                throw IllegalArgumentException("Current path and new name must not be null or blank")
            }

            val currentFile = Paths.get(basePath!!, currentPath)
            val renamedFile = currentFile.resolveSibling(newName)
            Files.move(currentFile, renamedFile, StandardCopyOption.REPLACE_EXISTING)
        } catch (e: IOException) {
            throw RuntimeException("Failed to rename file: " + e.message)
        }
    }

    override fun copy(sourcePath: String?, destinationPath: String?) {
        try {
            if (sourcePath.isNullOrBlank() || destinationPath.isNullOrBlank()) {
                throw IllegalArgumentException("Source and destination paths must not be null or blank")
            }

            val source = Paths.get(basePath, sourcePath)
            val destination = Paths.get(basePath, destinationPath)
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING)

        } catch (e: IOException) {
            throw RuntimeException("Failed to copy file or directory: " + e.message)
        }
    }

    override fun generateUniqueFileName(originalFileName: String?): String? {
        val dateFormat = SimpleDateFormat("yyyyMMddHHmmss")
        val timestamp = dateFormat.format(Date())
        val uniqueId = UUID.randomUUID().toString()
        val fileExtension = originalFileName?.let { getFileExtension(it) }

        return timestamp + "_" + uniqueId + "." + fileExtension
    }

    override fun listFiles(directoryPath: String?): List<String?>? {
        try {
            val directory: File = ResourceUtils.getFile((basePath + File.separator).toString() + directoryPath)

            if (directory.exists() && directory.isDirectory()) {
                val files: Array<out File>? = directory.listFiles()
                val fileNames: MutableList<String> = ArrayList()

                if (files != null) {
                    for (file in files) {
                        if (file.isFile()) {
                            fileNames.add(file.getName())
                        }
                    }
                }

                return fileNames
            } else {
                throw FileNotFoundException("Directory not found or is not a directory.")
            }
        } catch (e: IOException) {
            throw RuntimeException("Failed to list files: " + e.message)
        }
    }

    override fun deleteDirectory(directoryPath: String?) {
        try {
            val directoryToDelete: File = ResourceUtils.getFile((basePath + File.separator).toString() + directoryPath)

            if (directoryToDelete.exists() && directoryToDelete.isDirectory()) {
                if (!FileSystemUtils.deleteRecursively(directoryToDelete)) {
                    throw RuntimeException("Failed to delete directory.")
                }
            } else {
                throw FileNotFoundException("Directory not found or is not a directory.")
            }
        } catch (e: IOException) {
            throw RuntimeException("Failed to delete directory: " + e.message)
        }
    }

    private fun getFileExtension(originalFileName: String): String {
        val lastDotIndex = originalFileName.lastIndexOf(".")
        if (lastDotIndex != -1) {
            return originalFileName.substring(lastDotIndex + 1)
        }
        return ""
    }

}