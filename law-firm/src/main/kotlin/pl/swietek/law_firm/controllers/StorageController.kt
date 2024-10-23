package pl.swietek.law_firm.controllers

import lombok.AllArgsConstructor
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.swietek.law_firm.storage.FileUploadRequest
import pl.swietek.law_firm.storage.Storage
import org.springframework.core.io.Resource;

@RestController
@RequestMapping("/api/v1/storage")
@AllArgsConstructor
class StorageController(
    @Qualifier("localStorage") private val storage: Storage
) {

    @PostMapping("/upload")
    fun uploadFile(@ModelAttribute request: FileUploadRequest): ResponseEntity<String> {
        val file = request.file
        val destinationPath = request.destinationPath

        if (file.isEmpty) {
            return ResponseEntity.badRequest().body("File is empty")
        }

        storage.store(file, destinationPath)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body("File uploaded successfully")
    }

    @GetMapping("/download")
    fun downloadFile(@RequestParam filePath: String?): ResponseEntity<Resource> {
        val resource: Resource? = storage.load(filePath)
        return ResponseEntity.ok()
            .header("Content-Disposition", ("attachment; filename=\"" + resource?.filename).toString() + "\"")
            .body<Resource>(resource)
    }

    @DeleteMapping("/delete")
    fun deleteFile(@RequestParam filePath: String?): ResponseEntity<String> {
        storage.delete(filePath)
        return ResponseEntity.ok("File deleted successfully")
    }

    @GetMapping("/list")
    fun listFiles(@RequestParam directoryPath: String?): ResponseEntity<List<String?>?> {
        val files = storage.listFiles(directoryPath)
        return ResponseEntity.ok(files)
    }
}