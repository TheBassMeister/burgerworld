package com.bassmeister.burgerworld.config

import org.springdoc.core.SpringDocConfigProperties
import org.springdoc.core.SpringDocConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class SpringDocsConfiguration {

    @Bean @Primary
    fun springDocConfiguration(): SpringDocConfiguration {
        return SpringDocConfiguration()
    }

    @Bean @Primary
    fun springDocConfigProperties(): SpringDocConfigProperties {
        return SpringDocConfigProperties()
    }
}