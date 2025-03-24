package io.whatap.library.shared.web.client;

import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.charset.Charset;


@Getter
public class ExtendedHttpClientErrorException extends HttpClientErrorException {
    private String errorCode;

    public ExtendedHttpClientErrorException(String errorCode, String message, HttpStatus statusCode, String statusText, HttpHeaders headers, byte[] body, Charset responseCharset) {
        super(message, statusCode, statusText, headers, body, responseCharset);
        this.errorCode = errorCode;
    }


    public static HttpClientErrorException create(String errorCode, @Nullable String message, HttpStatus statusCode, String statusText, HttpHeaders headers, byte[] body, @Nullable Charset charset) {
        switch (statusCode) {
            case BAD_REQUEST:
                return message != null ? new BadRequest(errorCode, message, statusText, headers, body, charset) : new BadRequest(errorCode, null, statusText, headers, body, charset);
            case UNAUTHORIZED:
                return message != null ? new Unauthorized(errorCode, message, statusText, headers, body, charset) : new Unauthorized(errorCode, null, statusText, headers, body, charset);
            case FORBIDDEN:
                return message != null ? new Forbidden(errorCode, message, statusText, headers, body, charset) : new Forbidden(errorCode, null, statusText, headers, body, charset);
            case NOT_FOUND:
                return message != null ? new NotFound(errorCode, message, statusText, headers, body, charset) : new NotFound(errorCode, null, statusText, headers, body, charset);
            case METHOD_NOT_ALLOWED:
                return message != null ? new MethodNotAllowed(errorCode, message, statusText, headers, body, charset) : new MethodNotAllowed(errorCode, null, statusText, headers, body, charset);
            case NOT_ACCEPTABLE:
                return message != null ? new NotAcceptable(errorCode, message, statusText, headers, body, charset) : new NotAcceptable(errorCode, null, statusText, headers, body, charset);
            case CONFLICT:
                return message != null ? new Conflict(errorCode, message, statusText, headers, body, charset) : new Conflict(errorCode, null, statusText, headers, body, charset);
            case GONE:
                return message != null ? new Gone(errorCode, message, statusText, headers, body, charset) : new Gone(errorCode, null, statusText, headers, body, charset);
            case UNSUPPORTED_MEDIA_TYPE:
                return message != null ? new UnsupportedMediaType(errorCode, message, statusText, headers, body, charset) : new UnsupportedMediaType(errorCode, null, statusText, headers, body, charset);
            case TOO_MANY_REQUESTS:
                return message != null ? new TooManyRequests(errorCode, message, statusText, headers, body, charset) : new TooManyRequests(errorCode, null, statusText, headers, body, charset);
            case UNPROCESSABLE_ENTITY:
                return message != null ? new UnprocessableEntity(errorCode, message, statusText, headers, body, charset) : new UnprocessableEntity(errorCode, null, statusText, headers, body, charset);
            default:
                return message != null ? new ExtendedHttpClientErrorException(errorCode, message, statusCode, statusText, headers, body, charset) : new HttpClientErrorException(statusCode, statusText, headers, body, charset);
        }
    }

    public static final class BadRequest extends ExtendedHttpClientErrorException {
        private BadRequest(String errorCode, String message, String statusText, HttpHeaders headers, byte[] body, @Nullable Charset charset) {
            super(errorCode, message, HttpStatus.BAD_REQUEST, statusText, headers, body, charset);
        }
    }

    public static final class Unauthorized extends ExtendedHttpClientErrorException {
        private Unauthorized(String errorCode, String message, String statusText, HttpHeaders headers, byte[] body, @Nullable Charset charset) {
            super(errorCode, message, HttpStatus.UNAUTHORIZED, statusText, headers, body, charset);
        }
    }

    public static final class Forbidden extends ExtendedHttpClientErrorException {
        private Forbidden(String errorCode, String message, String statusText, HttpHeaders headers, byte[] body, @Nullable Charset charset) {
            super(errorCode, message, HttpStatus.FORBIDDEN, statusText, headers, body, charset);
        }
    }

    public static final class NotFound extends ExtendedHttpClientErrorException {
        private NotFound(String errorCode, String message, String statusText, HttpHeaders headers, byte[] body, @Nullable Charset charset) {
            super(errorCode, message, HttpStatus.NOT_FOUND, statusText, headers, body, charset);
        }
    }

    public static final class MethodNotAllowed extends ExtendedHttpClientErrorException {
        private MethodNotAllowed(String errorCode, String message, String statusText, HttpHeaders headers, byte[] body, @Nullable Charset charset) {
            super(errorCode, message, HttpStatus.METHOD_NOT_ALLOWED, statusText, headers, body, charset);
        }
    }

    public static final class NotAcceptable extends ExtendedHttpClientErrorException {
        private NotAcceptable(String errorCode, String message, String statusText, HttpHeaders headers, byte[] body, @Nullable Charset charset) {
            super(errorCode, message, HttpStatus.NOT_ACCEPTABLE, statusText, headers, body, charset);
        }
    }

    public static final class Conflict extends ExtendedHttpClientErrorException {
        private Conflict(String errorCode, String message, String statusText, HttpHeaders headers, byte[] body, @Nullable Charset charset) {
            super(errorCode, message, HttpStatus.CONFLICT, statusText, headers, body, charset);
        }
    }

    public static final class Gone extends ExtendedHttpClientErrorException {
        private Gone(String errorCode, String message, String statusText, HttpHeaders headers, byte[] body, @Nullable Charset charset) {
            super(errorCode, message, HttpStatus.GONE, statusText, headers, body, charset);
        }
    }

    public static final class UnsupportedMediaType extends ExtendedHttpClientErrorException {
        private UnsupportedMediaType(String errorCode, String message, String statusText, HttpHeaders headers, byte[] body, @Nullable Charset charset) {
            super(errorCode, message, HttpStatus.UNSUPPORTED_MEDIA_TYPE, statusText, headers, body, charset);
        }
    }

    public static final class UnprocessableEntity extends ExtendedHttpClientErrorException {
        private UnprocessableEntity(String errorCode, String message, String statusText, HttpHeaders headers, byte[] body, @Nullable Charset charset) {
            super(errorCode, message, HttpStatus.UNPROCESSABLE_ENTITY, statusText, headers, body, charset);
        }
    }

    public static final class TooManyRequests extends ExtendedHttpClientErrorException {
        private TooManyRequests(String errorCode, String message, String statusText, HttpHeaders headers, byte[] body, @Nullable Charset charset) {
            super(errorCode, message, HttpStatus.TOO_MANY_REQUESTS, statusText, headers, body, charset);
        }
    }
}
