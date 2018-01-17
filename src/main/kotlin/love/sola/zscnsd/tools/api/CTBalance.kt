package love.sola.zscnsd.tools.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

@RestController
class CTBalance {

    companion object {
        val URL = "http://gd.189.cn/webpay/chongzhi/czorderinput.jsp?busiType=AD&latnId=0760&bindType=5BA&isFromMall=true"
        val BALANCE_REGEX = "var balanceTotalJs = (.*);".toRegex()
    }

    val webClient = WebClient.builder()
        .baseUrl(URL)
        .defaultCookie("LATN_CODE_COOKIE", "0760")
        .build()

    @GetMapping("/api/ct-balance")
    fun getBalance(@RequestParam num: String): Mono<Response> {
        return webClient.get().uri { it.queryParam("num", num).build() }
            .retrieve()
            .bodyToMono<String>()
            .map {
                if (it.contains("未查询到该号码余额信息，请重试或选择非捆绑方式缴费！")) {
                    return@map Response(false, "未查询到该号码余额信息。")
                }
                val matchResult = BALANCE_REGEX.find(it)
                if (matchResult == null) {
                    Response(false, error = "未查询到该号码余额信息。")
                } else {
                    val balance = matchResult.groupValues[1].toFloat()
                    Response(true, balance = balance)
                }
            }
            .onErrorResume {
                Response(false, error = it.message).toMono()
            }
    }

    data class Response(val success: Boolean, val error: String? = null, val balance: Float = 0f)

}
