package com.miniproject.pokedex.config.exception

import com.miniproject.pokedex.config.base.AbstractResponseHandler
import com.miniproject.pokedex.config.base.ResultResponse
import com.miniproject.pokedex.config.property.AppMessages
import com.miniproject.pokedex.config.property.GlobalConstants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.validation.ConstraintViolationException

@ControllerAdvice
class AppExceptionHandler @Autowired constructor(
    val appMessages: AppMessages
) : ResponseEntityExceptionHandler() {

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val message = ex.bindingResult.fieldError?.defaultMessage
        return basicThrowException(ex, status, message)
    }

    override fun handleNoHandlerFoundException(
        ex: NoHandlerFoundException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val message = ex.message
        return basicThrowException(ex, status, message)
    }

    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ) = basicThrowException(ex, status)

    override fun handleHttpRequestMethodNotSupported(
        ex: HttpRequestMethodNotSupportedException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ) = basicThrowException(ex, status)

    override fun handleMissingServletRequestParameter(
        ex: MissingServletRequestParameterException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ) = basicThrowException(ex, status)

    @ExceptionHandler(DataNotFoundException::class)
    fun dataNotFound(ex: DataNotFoundException) = throwException(ex, HttpStatus.NOT_FOUND)

    @ExceptionHandler(AppException::class)
    fun appException(ex: AppException) = throwException(ex, HttpStatus.BAD_REQUEST)

    @ExceptionHandler(ConstraintViolationException::class)
    fun constraintViolation(ex: ConstraintViolationException) = throwException(ex, HttpStatus.BAD_REQUEST)

    @ExceptionHandler(Exception::class)
    fun exception(ex: Exception) = throwException(ex, HttpStatus.BAD_REQUEST, "error.app")

    /**
     * =============================================================
     * Private Functions
     * =============================================================
     */

    private fun throwException(
        throwable: Throwable,
        httpStatus: HttpStatus,
        messageId: String? = null
    ): ResponseEntity<ResultResponse<Any>> {
        val message = parseMessage(messageId ?: throwable.localizedMessage)

        logger.error(message, throwable)
        return object : AbstractResponseHandler() {
            override fun data(): Any = throwable
        }.done(msg = message, httpStatus = httpStatus)
    }

    private fun basicThrowException(ex: Exception, status: HttpStatus, message: String? = null): ResponseEntity<Any> {
        val result = ResultResponse<Any>(
            code = status.value(),
            message = parseMessage(message ?: GlobalConstants.EMPTY_STRING),
        )

        logger.error(ex)
        return ResponseEntity(result, status)
    }

    private fun parseMessage(message: String): String {
        val pieceOfMessage = message.split(GlobalConstants.MESSAGE_SEPARATOR).toMutableList()
        val messageSize = pieceOfMessage.size
        val firstMessage = pieceOfMessage.first()
        pieceOfMessage.removeAt(0)

        return try {
            when (messageSize) {
                1 -> appMessages.call(firstMessage)
                else -> appMessages.call(firstMessage, *pieceOfMessage.toTypedArray())
            }
        } catch (ex: Exception) {
            pieceOfMessage.first()
        }
    }
}