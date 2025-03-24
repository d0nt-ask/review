package io.whatap.library.shared.web.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.whatap.library.shared.message.ErrorResponse;
import org.springframework.core.log.LogFormatUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.UnknownHttpStatusCodeException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ExtendedDefaultResponseErrorHandler extends DefaultResponseErrorHandler {
    private final ObjectMapper objectMapper;

    public ExtendedDefaultResponseErrorHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {

        return super.hasError(response);
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        HttpStatus statusCode = HttpStatus.resolve(response.getRawStatusCode());
        if (statusCode == null) {
            byte[] body = this.getResponseBody(response);
            String message = getErrorMessage(response.getRawStatusCode(), response.getStatusText(), body, this.getCharset(response));
            throw new UnknownHttpStatusCodeException(message, response.getRawStatusCode(), response.getStatusText(), response.getHeaders(), body, this.getCharset(response));
        } else {
            String statusText = response.getStatusText();
            HttpHeaders headers = response.getHeaders();
            byte[] body = this.getResponseBody(response);
            Charset charset = this.getCharset(response);
            ErrorResponse message = this.getErrorResponse(statusCode.value(), statusText, body, charset);
            if (message != null) {
                switch (statusCode.series()) {
                    case CLIENT_ERROR:
                        throw ExtendedHttpClientErrorException.create(message.getErrorCode(), message.getErrorMessage(), statusCode, statusText, headers, body, charset);
                    case SERVER_ERROR:
                        throw ExtendedHttpClientErrorException.create(message.getErrorCode(), message.getErrorMessage(), statusCode, statusText, headers, body, charset);
                    default:
                        throw new UnknownHttpStatusCodeException(message.getErrorMessage(), statusCode.value(), statusText, headers, body, charset);
                }
            } else {
                String preface = statusCode.value() + " " + statusText + ": ";

                switch (statusCode.series()) {
                    case CLIENT_ERROR:
                        throw HttpClientErrorException.create(preface + "[no body]", statusCode, statusText, headers, body, charset);
                    case SERVER_ERROR:
                        throw HttpServerErrorException.create(preface + "[no body]", statusCode, statusText, headers, body, charset);
                    default:
                        throw new UnknownHttpStatusCodeException(preface + "[no body]", statusCode.value(), statusText, headers, body, charset);
                }
            }
        }
    }

    private String getErrorMessage(int rawStatusCode, String statusText, @Nullable byte[] responseBody, @Nullable Charset charset) {
        String preface = rawStatusCode + " " + statusText + ": ";
        if (ObjectUtils.isEmpty(responseBody)) {
            return preface + "[no body]";
        } else {
            charset = charset != null ? charset : StandardCharsets.UTF_8;
            String bodyText = new String(responseBody, charset);
            bodyText = LogFormatUtils.formatValue(bodyText, -1, true);
            return preface + bodyText;
        }
    }

    private ErrorResponse getErrorResponse(int rawStatusCode, String statusText, @Nullable byte[] responseBody, @Nullable Charset charset) {
        String preface = rawStatusCode + " " + statusText + ": ";
        if (ObjectUtils.isEmpty(responseBody)) {
            return null;
        } else {
            charset = charset != null ? charset : StandardCharsets.UTF_8;
            String bodyText = new String(responseBody, charset);
            try {
                return objectMapper.readValue(bodyText, ErrorResponse.class);
            } catch (JsonProcessingException e) {
                return null;
            }
        }
    }
}
