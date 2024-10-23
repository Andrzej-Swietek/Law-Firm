package pl.swietek.law_firm.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import pl.swietek.law_firm.storage.FileSystemStorage
import pl.swietek.law_firm.storage.S3Storage
import pl.swietek.law_firm.storage.Storage


@Configuration
class StorageConfig {


    @Bean
    @Profile("local")
    fun localStorage(): Storage {
        return FileSystemStorage()
    }

    @Bean
    @Profile("s3")
    fun s3Storage(): Storage {
        return S3Storage() // TODO: Implement S3Storage class
    }

}