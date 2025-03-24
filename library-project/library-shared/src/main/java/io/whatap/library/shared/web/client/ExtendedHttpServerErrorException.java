package io.whatap.library.shared.web.client;

import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpServerErrorException;

import java.nio.charset.Charset;

@Getter
public class ExtendedHttpServerErrorException extends HttpServerErrorException {
    private String errorCode;

    public ExtendedHttpServerErrorException(String errorCode, String message, HttpStatus statusCode, String statusText, HttpHeaders headers, byte[] body, Charset charset) {
        super(message, statusCode, statusText, headers, body, charset);
        this.errorCode = errorCode;
    }

    public static ExtendedHttpServerErrorException create(String errorCode, @Nullable String message, HttpStatus statusCode, String statusText, HttpHeaders headers, byte[] body, @Nullable Charset charset) {
        switch (statusCode) {
            case INTERNAL_SERVER_ERROR:
                return message != null ? new InternalServerError(errorCode, message, statusText, headers, body, charset) : new InternalServerError(errorCode, null, statusText, headers, body, charset);
            case NOT_IMPLEMENTED:
                return message != null ? new NotImplemented(errorCode, message, statusText, headers, body, charset) : new NotImplemented(errorCode, null, statusText, headers, body, charset);
            case BAD_GATEWAY:
                return message != null ? new BadGateway(errorCode, message, statusText, headers, body, charset) : new BadGateway(errorCode, null, statusText, headers, body, charset);
            case SERVICE_UNAVAILABLE:
                return message != null ? new ServiceUnavailable(errorCode, message, statusText, headers, body, charset) : new ServiceUnavailable(errorCode, null, statusText, headers, body, charset);
            case GATEWAY_TIMEOUT:
                return message != null ? new GatewayTimeout(errorCode, message, statusText, headers, body, charset) : new GatewayTimeout(errorCode, null, statusText, headers, body, charset);
            default:
                return message != null ? new ExtendedHttpServerErrorException(errorCode, message, statusCode, statusText, headers, body, charset) : new ExtendedHttpServerErrorException(errorCode, null, statusCode, statusText, headers, body, charset);
        }
    }

    public static final class InternalServerError extends ExtendedHttpServerErrorException {
        private InternalServerError(String errorCode, String message, String statusText, HttpHeaders headers, byte[] body, @Nullable Charset charset) {
            super(errorCode, message, HttpStatus.INTERNAL_SERVER_ERROR, statusText, headers, body, charset);
        }
    }

    public static final class NotImplemented extends ExtendedHttpServerErrorException {
        private NotImplemented(String errorCode, String message, String statusText, HttpHeaders headers, byte[] body, @Nullable Charset charset) {
            super(errorCode, message, HttpStatus.NOT_IMPLEMENTED, statusText, headers, body, charset);
        }
    }

    public static final class BadGateway extends ExtendedHttpServerErrorException {
        private BadGateway(String errorCode, String message, String statusText, HttpHeaders headers, byte[] body, @Nullable Charset charset) {
            super(errorCode, message, HttpStatus.BAD_GATEWAY, statusText, headers, body, charset);
        }
    }

    public static final class ServiceUnavailable extends ExtendedHttpServerErrorException {
        private ServiceUnavailable(String errorCode, String message, String statusText, HttpHeaders headers, byte[] body, @Nullable Charset charset) {
            super(errorCode, message, HttpStatus.SERVICE_UNAVAILABLE, statusText, headers, body, charset);
        }
    }

    public static final class GatewayTimeout extends ExtendedHttpServerErrorException {
        private GatewayTimeout(String errorCode, String message, String statusText, HttpHeaders headers, byte[] body, @Nullable Charset charset) {
            super(errorCode, message, HttpStatus.GATEWAY_TIMEOUT, statusText, headers, body, charset);
        }
    }
}
