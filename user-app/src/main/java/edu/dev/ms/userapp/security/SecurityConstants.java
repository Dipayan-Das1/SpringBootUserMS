package edu.dev.ms.userapp.security;

public class SecurityConstants {

	public static final long EXPIRATION_TIME = 2 * 60 * 60 * 1000;
	public static final long EMAIL_EXPIRATION_TIME = 3 * 60 * 60 * 1000;
	public static final long PWD_RESET_EXPIRATION_TIME = 24 * 60 * 60 * 1000;
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/user/signup";
	public static final String VERIFICATION_URL = "/user/verify";
	public static final String PASSWORD_RESET_URL = "/user/password-reset/*";
	public static final String PASSWORD_RESET_URL_POST = "/user/password-reset";
	public static final String TOKEN_SECRET = "jhdoas8337e8981ejkkhkj";
}
