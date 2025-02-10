package fr.lfavreli.ranking.exception;

/**
 * This exception means that the requested functionality is not implemented yet.
 * HTTP status 501 Not Implemented will be replied when this exception is thrown and not caught.
 * A 501 status page could be configured by registering a custom [io.ktor.plugins.StatusPages] handler.
 */
class NotImplementedException(message: String, cause: Throwable? = null) : Exception(message, cause)
