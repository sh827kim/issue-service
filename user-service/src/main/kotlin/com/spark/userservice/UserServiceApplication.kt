package com.spark.userservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing


@SpringBootApplication
@ConfigurationPropertiesScan
@EnableR2dbcAuditing
class UserServiceApplication
fun main(args: Array<String>) {
    runApplication<UserServiceApplication>(*args)
}