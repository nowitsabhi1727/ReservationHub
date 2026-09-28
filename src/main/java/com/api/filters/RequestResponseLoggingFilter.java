package com.api.filters;

import io.qameta.allure.Allure;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class RequestResponseLoggingFilter implements Filter {

    @Override
    public Response filter(
            FilterableRequestSpecification requestSpec,
            FilterableResponseSpecification responseSpec,
            FilterContext context) {

        String requestDetails =
                "Method: " + requestSpec.getMethod() + "\n" +
                        "URI: " + requestSpec.getURI() + "\n" +
                        "Headers: " + maskSensitiveHeaders(
                        requestSpec.getHeaders().toString()) + "\n" +
                        "Body: " + requestSpec.getBody();

        System.out.println("========== REQUEST ==========");
        System.out.println(requestDetails);

        Allure.addAttachment(
                "API Request",
                "text/plain",
                requestDetails,
                ".txt"
        );

        Response response = context.next(requestSpec, responseSpec);

        String responseDetails =
                "Status Code: " + response.statusCode() + "\n" +
                        "Headers: " + response.getHeaders() + "\n" +
                        "Body: " + response.asPrettyString();

        System.out.println("========== RESPONSE ==========");
        System.out.println(responseDetails);
        System.out.println("==============================");

        Allure.addAttachment(
                "API Response",
                "text/plain",
                responseDetails,
                ".txt"
        );

        return response;
    }

    private String maskSensitiveHeaders(String headers) {
        return headers.replaceAll(
                "(?i)(Cookie=token=)[^,\\n]*",
                "$1********"
        );
    }
}