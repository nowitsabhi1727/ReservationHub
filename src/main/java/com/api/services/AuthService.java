package com.api.services;

import com.api.base.BaseService;
import com.api.constants.Endpoints;
import com.api.models.request.AuthRequest;
import io.restassured.response.Response;

public class AuthService extends BaseService {

   public Response login(AuthRequest payload){
      return postRequest(payload, Endpoints.AUTH);
   }
}
