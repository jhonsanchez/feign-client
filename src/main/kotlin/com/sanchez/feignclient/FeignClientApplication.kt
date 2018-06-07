package com.sanchez.feignclient

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.netflix.hystrix.EnableHystrix
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@EnableFeignClients
@EnableHystrix
@EnableDiscoveryClient
@SpringBootApplication(exclude = [RabbitAutoConfiguration::class])
class FeignClientApplication

fun main(args: Array<String>) {
    SpringApplicationBuilder()
            .sources(FeignClientApplication::class.java)
            .run(*args)
}

@RestController
class MyController(val feignClient: MyFeign, val feignClient2: MyFeign2) {
    @GetMapping("/")
    fun hola(): String = feignClient.hello("demo")
    @GetMapping("/test2")
    fun hola2(): String = feignClient2.hello("demo")

}

@FeignClient(name = "test" ,fallback = MyFallback::class)
interface MyFeign {
    @GetMapping("/hello")
    fun hello(@RequestParam(value = "name") name: String): String

}
@FeignClient(name = "producer")
interface MyFeign2 {
    @GetMapping("/hello")
    fun hello(@RequestParam(value = "name") name: String): String

}

@Component
class MyFallback : MyFeign {
    override fun hello(name: String): String {
        return "muy mal"
    }

}

