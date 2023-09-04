package Source;

import Utils.Reporter;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

public class ApiValidation {
    Reporter log;

    public ApiValidation(Reporter log) {
        this.log = log;
    }

    public Response getResponse(String requestUrl, String method, Map<String, String> headers, String jsonBody) {
        RestAssured.baseURI = requestUrl;
        RequestSpecification request = RestAssured.given();
        log.info("api request to %s", requestUrl);

        if (headers != null) {
            for (var entry : headers.entrySet()) {
                request.given().header(entry.getKey(), entry.getValue());
                log.info("added header - %s: '%s'", entry.getKey(), entry.getValue());
            }
        }

        if (jsonBody != null) {
            request.contentType(ContentType.JSON);
            request.body(jsonBody);
            log.info("json body provided:\n%s", jsonBody);
        }

        Response response = request.when().request(method);
        log.info("response code - %s - '%s'", response.getStatusCode(), response.getStatusLine());

        return response;
    }
}
