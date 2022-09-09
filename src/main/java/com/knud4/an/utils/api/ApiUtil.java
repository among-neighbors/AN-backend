package com.knud4.an.utils.api;

/**
 * REST API 의 HTTPResponse 생성 보조 클래스
 */
public class ApiUtil {

    /**
     * REST API 호출 성공시 반환형을 생성하는 메소드
     * @param response response body
     * @return @{@link ApiSuccessResult}
     */
    public static <T> ApiSuccessResult<T> success(T response) {
        return new ApiSuccessResult<>(response);
    }

    /**
     * REST API 호출 실패시 반환형을 생성하는 메소드
     * @param code server failure code
     * @param message response message
     * @return @{@link ApiErrorResult}
     */
    public static <T> ApiErrorResult<T> error(int code, T message) {
        return new ApiErrorResult<>(code, message);
    }

    /**
     * REST API 호출 성공시 반환형
     */
    public static class ApiSuccessResult<T> {
        private final T response;

        private ApiSuccessResult(T response) {
            this.response = response;
        }

        public T getResponse() {
            return response;
        }
    }

    /**
     * REST API 호출 실패시 반환형
     */
    public static class ApiErrorResult<T> {
        private final int statusCode;
        private final T message;

        private ApiErrorResult(int statusCode, T message) {
            this.statusCode = statusCode;
            this.message = message;
        }

        public int getStatus() {
            return statusCode;
        }

        public T getMessage() {
            return message;
        }
    }

}
