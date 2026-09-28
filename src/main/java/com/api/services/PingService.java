package com.api.services;

import com.api.base.BaseService;
import com.api.constants.Endpoints;
import io.restassured.response.Response;

public class PingService extends BaseService {

    public Response healthCheck() {
        return getRequest(Endpoints.PING);
    }

}
