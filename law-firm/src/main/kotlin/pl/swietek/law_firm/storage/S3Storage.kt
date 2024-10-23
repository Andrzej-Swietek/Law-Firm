package pl.swietek.law_firm.storage

import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class S3Storage : Storage {
    override fun store(file: MultipartFile?, destinationPath: String?) {
        TODO("Not yet implemented")
    }

    override fun load(filePath: String?): Resource? {
        TODO("Not yet implemented")
    }

    override fun delete(filePath: String?) {
        TODO("Not yet implemented")
    }

    override fun exists(filePath: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun move(sourcePath: String?, destinationPath: String?) {
        TODO("Not yet implemented")
    }

    override fun rename(currentPath: String?, newName: String?) {
        TODO("Not yet implemented")
    }

    override fun copy(sourcePath: String?, destinationPath: String?) {
        TODO("Not yet implemented")
    }

    override fun generateUniqueFileName(originalFileName: String?): String? {
        TODO("Not yet implemented")
    }

    override fun listFiles(directoryPath: String?): List<String?>? {
        TODO("Not yet implemented")
    }

    override fun deleteDirectory(directoryPath: String?) {
        TODO("Not yet implemented")
    }
}