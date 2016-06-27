package com.school.util;

public interface StatusCode {

	int OK = 200;
	int CREATED = 201;
	int FORBIDDEN = 403;
	int NOT_FOUND = 404;
	int CONFLICT = 409;
	int LOCKED_OUT = 420;
	int NOT_SIGNED_UP = 443;
	int DUPLICATE = 444;
	int INTERNAL_ERROR = 500;
	int DBEERROR = 555;
}
