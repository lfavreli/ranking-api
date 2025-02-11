package fr.lfavreli.ranking.exception

/**
 * This exception indicates that an internal server error occurred.
 * HTTP status 500 Internal Server Error will be replied when this exception is thrown and not caught.
 */
class InternalServerErrorException(message: String, cause: Throwable? = null) : Exception(message, cause)
