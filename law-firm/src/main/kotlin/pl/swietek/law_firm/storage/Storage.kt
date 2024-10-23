package pl.swietek.law_firm.storage

import org.springframework.web.multipart.MultipartFile
import org.springframework.core.io.Resource;


interface Storage {

    fun store(file: MultipartFile?, destinationPath: String?)
    fun load(filePath: String?): Resource?
    fun delete(filePath: String?)

    fun exists(filePath: String?): Boolean
    fun move(sourcePath: String?, destinationPath: String?)
    fun rename(currentPath: String?, newName: String?)
    fun copy(sourcePath: String?, destinationPath: String?)

    fun generateUniqueFileName(originalFileName: String?): String?
    fun listFiles(directoryPath: String?): List<String?>?
    fun deleteDirectory(directoryPath: String?)

}