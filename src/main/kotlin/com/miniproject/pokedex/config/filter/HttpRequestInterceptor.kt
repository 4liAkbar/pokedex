package com.miniproject.pokedex.config.filter

import com.miniproject.pokedex.config.base.MetaResponse
import com.miniproject.pokedex.config.base.ResultResponse
import com.miniproject.pokedex.config.exception.DataNotFoundException
import com.miniproject.pokedex.config.extension.toJson
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Service
class HttpRequestInterceptor @Autowired constructor(
) : HandlerInterceptorAdapter() {

    private var logger = LogFactory.getLog(this.javaClass)

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        when {
            handler is ResourceHttpRequestHandler -> {
                printException(
                    DataNotFoundException("Resource not found"),
                    HttpStatus.NOT_FOUND.value(),
                    response
                )
                return false
            }
        }

        return true
    }

    private fun printException(ex: Throwable, httpStatus: Int, response: HttpServletResponse) {
        val message = ex.localizedMessage
        val meta = MetaResponse(
            code = httpStatus,
            message = message,
            debugInfo = null
        )
        val result = ResultResponse<Any>(
            status = "ERROR",
            meta = meta
        )

        logger.error(ex)
        response.apply {
            status = httpStatus
            contentType = MediaType.APPLICATION_JSON_VALUE
            writer.write(result.toJson())
        }
    }

}
