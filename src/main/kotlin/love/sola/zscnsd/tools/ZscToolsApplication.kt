package love.sola.zscnsd.tools

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import java.net.URI

@SpringBootApplication
class ZscToolsApplication {

    @Bean
    fun route() = router {
        GET("/cet") {
            ServerResponse.permanentRedirect(URI.create("http://www.chsi.com.cn/cet/")).build()
        }
    }

}

fun main(args: Array<String>) {
    runApplication<ZscToolsApplication>(*args)
}
