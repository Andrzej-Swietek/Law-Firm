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
        try {
            val resource = resourceLoader!!.getResource(basePath + destinationPath)
            val destinationFile = resource.file
            Files.createDirectories(destinationFile.parentFile.toPath())
            Files.copy(file!!.inputStream, destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        } catch (e: IOException) {
            throw java.lang.RuntimeException("Failed to store file: " + e.message)
        }
    }

    override fun load(filePath: String?): Resource {
        try {
            val resource: Resource = FileSystemResource(
                ResourceUtils.getFile(
                    filePath!!
                )
            )
            if (resource.exists() && resource.isReadable()) {
                return resource
            } else {
                throw FileNotFoundException("Resource not found or not readable.")
            }
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
            val fileToCheck: File = ResourceUtils.getFile((basePath + File.separator).toString() + filePath)
            return fileToCheck.exists() && fileToCheck.isFile()
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
            val currentFile = Paths.get(basePath!!, currentPath)
            val renamedFile = currentFile.resolveSibling(newName!!)
            Files.move(currentFile, renamedFile, StandardCopyOption.REPLACE_EXISTING)
        } catch (e: IOException) {
            throw RuntimeException("Failed to rename file: " + e.message)
        }
    }

    override fun copy(sourcePath: String?, destinationPath: String?) {
        try {
            val source = basePath?.let { Paths.get(it, sourcePath) }
            val destination = basePath?.let { Paths.get(it, destinationPath) }
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