package com.onopry.domain.utils

// todo #2 Exceptions
//I think will be better to create typed Exceptions for Error and Exception type.
// Like LocationException, ForecastResponceException, BackEndException and etc...

sealed interface ApiResult<T : Any>

class ApiSuccess<T : Any>(val data: T) : ApiResult<T>
class ApiError<T : Any>(val code: Int, val message: String) : ApiResult<T>
class ApiException<T : Any>(val message: String) : ApiResult<T>
