package net.apptronic.core.commons.service

/**
 * This exception signals that [Service] thrown an exception during execution and this exception is not handled
 * inside of [Service.onError] method. If exception should no be handled by service itself but by service invoker
 * please override [Service.onError] and ignore exception here.
 */
class ServiceIsNotHandledErrorException internal constructor(message: String, cause: Exception) : RuntimeException(message, cause)