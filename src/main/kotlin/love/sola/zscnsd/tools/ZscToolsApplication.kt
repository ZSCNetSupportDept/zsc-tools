package love.sola.zscnsd.tools

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ZscToolsApplication

fun main(args: Array<String>) {
    runApplication<ZscToolsApplication>(*args)
}
