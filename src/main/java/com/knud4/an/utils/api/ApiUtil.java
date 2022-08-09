package com.knud4.an.utils.api;

public class ApiUtil {

    public static <T> ApiSuccessResult<T> success(T response) {
        return new ApiSuccessResult<>(response);
    }

    public static <T> ApiErrorResult<T> error(int code, T message) {
        return new ApiErrorResult<>(code, message);
    }

//    inner static class 는 static field 끼리 접근 가능
//    inner static class 의 private 생성자 -> static private 생성자 느낌
    public static class ApiSuccessResult<T> {
        private final T response;

        private ApiSuccessResult(T response) {
            this.response = response;
        }

        public T getResponse() {
            return response;
        }
    }

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
