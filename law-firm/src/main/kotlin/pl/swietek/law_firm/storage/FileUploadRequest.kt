package pl.swietek.law_firm.storage

import org.springframework.web.multipart.MultipartFile

data class FileUploadRequest(
    val file: MultipartFile,
    val destinationPath: String,
)
