package com.gamer.gamerapp.config;

public final class Constants {


    private Constants() {
        throw new UnsupportedOperationException("Esta es una clase de constantes y no debe ser instanciada");
    }


    public static final String LOGIN_URL = "/api/auth/login";
    public static final String HEADER_AUTHORIZACION_KEY = "Authorization";
    public static final String TOKEN_BEARER_PREFIX = "Bearer ";
    public static final String ISSUER_INFO = "https://www.gamerapp.cl/";
}