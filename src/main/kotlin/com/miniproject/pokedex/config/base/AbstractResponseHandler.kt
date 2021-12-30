package com.miniproject.pokedex.config.base

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

abstract class AbstractResponseHandler {

    fun done(
        msg: String? = "OK",
        httpStatus: HttpStatus = HttpStatus.OK
    ): ResponseEntity<ResultResponse<Any>> {

        return when (val processResponse = data()) {
            is Exception -> onError(processResponse, httpStatus)
            else -> onSuccess(msg, processResponse, httpStatus)
        }
    }

    private fun onError(ex: Exception, httpStatus: HttpStatus): ResponseEntity<ResultResponse<Any>> {
        val debugInfo = ex.message

        val result = ResultResponse<Any>(
            code = httpStatus.value(),
            message = debugInfo.orEmpty()
        )
        return generateResponseEntity(result, httpStatus)
    }

    private fun onSuccess(msg: String?, any: Any, httpStatus: HttpStatus): ResponseEntity<ResultResponse<Any>> {
        val result = ResultResponse(
            code = httpStatus.value(),
            message = msg.orEmpty(),
            data = any
        )
        return generateResponseEntity(result, httpStatus)
    }

    private fun generateResponseEntity(result: ResultResponse<Any>, code: HttpStatus) =
        ResponseEntity(result, HttpHeaders().apply {
            set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        }, code)

    abstract fun data(): Any
}