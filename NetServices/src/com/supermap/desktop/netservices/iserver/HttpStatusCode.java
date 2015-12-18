package com.supermap.desktop.netservices.iserver;

public class HttpStatusCode {
	// 摘要:
	// Equivalent to HTTP status 100.System.Net.HttpStatusCode.Continue indicates
	// that the client can continue with its request.
	public static final int CONTINUE = 100;
	//
	// 摘要:
	// Equivalent to HTTP status 101.System.Net.HttpStatusCode.SwitchingProtocols
	// indicates that the protocol version or protocol is being changed.
	public static final int SWITCHING_PROTOCOLS = 101;
	//
	// 摘要:
	// Equivalent to HTTP status 200.System.Net.HttpStatusCode.OK indicates that
	// the request succeeded and that the requested information is in the response.这是最常接收的状态代码。
	public static final int OK = 200;
	//
	// 摘要:
	// Equivalent to HTTP status 201.System.Net.HttpStatusCode.Created indicates
	// that the request resulted in a new resource created before the response was
	// sent.
	public static final int CREATED = 201;
	//
	// 摘要:
	// Equivalent to HTTP status 202.System.Net.HttpStatusCode.Accepted indicates
	// that the request has been accepted for further processing.
	public static final int ACCEPTED = 202;
	//
	// 摘要:
	// Equivalent to HTTP status 203.System.Net.HttpStatusCode.NonAuthoritativeInformation
	// indicates that the returned metainformation is from a cached copy instead
	// of the origin server and therefore may be incorrect.
	public static final int NON_AUTHORITATIVE_INFORMATION = 203;
	//
	// 摘要:
	// Equivalent to HTTP status 204.System.Net.HttpStatusCode.NoContent indicates
	// that the request has been successfully processed and that the response is
	// intentionally blank.
	public static final int NO_CONTENT = 204;
	//
	// 摘要:
	// Equivalent to HTTP status 205.System.Net.HttpStatusCode.ResetContent indicates
	// that the client should reset (not reload) the current resource.
	public static final int RESET_CONTENT = 205;
	//
	// 摘要:
	// Equivalent to HTTP status 206.System.Net.HttpStatusCode.PartialContent indicates
	// that the response is a partial response as requested by a GET request that
	// includes a byte range.
	public static final int PARTIAL_CONTENT = 206;
	//
	// 摘要:
	// Equivalent to HTTP status 300.System.Net.HttpStatusCode.MultipleChoices indicates
	// that the requested information has multiple representations.默认操作是将此状态视为重定向，并遵循与此响应关联的
	// Location 头的内容。
	public static final int MULTIPLE_CHOICES = 300;
	//
	// 摘要:
	// Equivalent to HTTP status 300.System.Net.HttpStatusCode.Ambiguous indicates
	// that the requested information has multiple representations.默认操作是将此状态视为重定向，并遵循与此响应关联的
	// Location 头的内容。
	public static final int AMBIGUOUS = 300;
	//
	// 摘要:
	// Equivalent to HTTP status 301.System.Net.HttpStatusCode.MovedPermanently
	// indicates that the requested information has been moved to the URI specified
	// in the Location header.接收到此状态时的默认操作为遵循与响应关联的 Location 头。
	public static final int MOVED_PERMANENTLY = 301;
	//
	// 摘要:
	// Equivalent to HTTP status 301.System.Net.HttpStatusCode.Moved indicates that
	// the requested information has been moved to the URI specified in the Location
	// header.接收到此状态时的默认操作为遵循与响应关联的 Location 头。原始请求方法为 POST 时，重定向的请求将使用 GET 方法。
	public static final int MOVED = 301;
	//
	// 摘要:
	// Equivalent to HTTP status 302.System.Net.HttpStatusCode.Found indicates that
	// the requested information is located at the URI specified in the Location
	// header.接收到此状态时的默认操作为遵循与响应关联的 Location 头。原始请求方法为 POST 时，重定向的请求将使用 GET 方法。
	public static final int FOUND = 302;
	//
	// 摘要:
	// Equivalent to HTTP status 302.System.Net.HttpStatusCode.Redirect indicates
	// that the requested information is located at the URI specified in the Location
	// header.接收到此状态时的默认操作为遵循与响应关联的 Location 头。原始请求方法为 POST 时，重定向的请求将使用 GET 方法。
	public static final int REDIRECT = 302;
	//
	// 摘要:
	// Equivalent to HTTP status 303.System.Net.HttpStatusCode.SeeOther automatically
	// redirects the client to the URI specified in the Location header as the result
	// of a POST.用 GET 生成对 Location 头所指定的资源的请求。
	public static final int SEE_OTHER = 303;
	//
	// 摘要:
	// Equivalent to HTTP status 303.System.Net.HttpStatusCode.RedirectMethod automatically
	// redirects the client to the URI specified in the Location header as the result
	// of a POST.用 GET 生成对 Location 头所指定的资源的请求。
	public static final int REDIRECT_METHOD = 303;
	//
	// 摘要:
	// Equivalent to HTTP status 304.System.Net.HttpStatusCode.NotModified indicates
	// that the client's cached copy is up to date.未传输此资源的内容。
	public static final int NOT_MODIFIED = 304;
	//
	// 摘要:
	// Equivalent to HTTP status 305.System.Net.HttpStatusCode.UseProxy indicates
	// that the request should use the proxy server at the URI specified in the
	// Location header.
	public static final int USE_PROXY = 305;
	//
	// 摘要:
	// Equivalent to HTTP status 306.System.Net.HttpStatusCode.Unused is a proposed
	// extension to the HTTP/1.1 specification that is not fully specified.
	public static final int UNUSED = 306;
	//
	// 摘要:
	// Equivalent to HTTP status 307.System.Net.HttpStatusCode.TemporaryRedirect
	// indicates that the request information is located at the URI specified in
	// the Location header.接收到此状态时的默认操作为遵循与响应关联的 Location 头。原始请求方法为 POST 时，重定向的请求还将使用
	// POST 方法。
	public static final int TEMPORARY_REDIRECT = 307;
	//
	// 摘要:
	// Equivalent to HTTP status 307.System.Net.HttpStatusCode.RedirectKeepVerb
	// indicates that the request information is located at the URI specified in
	// the Location header.接收到此状态时的默认操作为遵循与响应关联的 Location 头。原始请求方法为 POST 时，重定向的请求还将使用
	// POST 方法。
	public static final int REDIRECT_KEEP_VERB = 307;
	//
	// 摘要:
	// Equivalent to HTTP status 400.System.Net.HttpStatusCode.BadRequest indicates
	// that the request could not be understood by the server.System.Net.HttpStatusCode.BadRequest
	// is sent when no other error is applicable; or if the exact error is unknown
	// or does not have its own error code.
	public static final int BAD_REQUEST = 400;
	//
	// 摘要:
	// Equivalent to HTTP status 401.System.Net.HttpStatusCode.Unauthorized indicates
	// that the requested resource requires authentication.WWW-Authenticate 头包含如何执行身份验证的详细信息。
	public static final int UNAUTHORIZED = 401;
	//
	// 摘要:
	// Equivalent to HTTP status 402.System.Net.HttpStatusCode.PaymentRequired is
	// reserved for future use.
	public static final int PAYMENT_REQUIRED = 402;
	//
	// 摘要:
	// Equivalent to HTTP status 403.System.Net.HttpStatusCode.Forbidden indicates
	// that the server refuses to fulfill the request.
	public static final int FORBIDDEN = 403;
	//
	// 摘要:
	// Equivalent to HTTP status 404.System.Net.HttpStatusCode.NotFound indicates
	// that the requested resource does not exist on the server.
	public static final int NOT_FOUND = 404;
	//
	// 摘要:
	// Equivalent to HTTP status 405.System.Net.HttpStatusCode.MethodNotAllowed
	// indicates that the request method (POST or GET) is not allowed on the requested
	// resource.
	public static final int METHOD_NOT_ALLOWED = 405;
	//
	// 摘要:
	// Equivalent to HTTP status 406.System.Net.HttpStatusCode.NotAcceptable indicates
	// that the client has indicated with Accept headers that it will not accept
	// any of the available representations of the resource.
	public static final int NOT_ACCEPTABLE = 406;
	//
	// 摘要:
	// Equivalent to HTTP status 407.System.Net.HttpStatusCode.ProxyAuthenticationRequired
	// indicates that the requested proxy requires authentication.Proxy-authenticate
	// 头包含如何执行身份验证的详细信息。
	public static final int PROXY_AUTHENTICATION_REQUIRED = 407;
	//
	// 摘要:
	// Equivalent to HTTP status 408.System.Net.HttpStatusCode.RequestTimeout indicates
	// that the client did not send a request within the time the server was expecting
	// the request.
	public static final int REQUEST_TIMEOUT = 408;
	//
	// 摘要:
	// Equivalent to HTTP status 409.System.Net.HttpStatusCode.Conflict indicates
	// that the request could not be carried out because of a conflict on the server.
	public static final int CONFLICT = 409;
	//
	// 摘要:
	// Equivalent to HTTP status 410.System.Net.HttpStatusCode.Gone indicates that
	// the requested resource is no longer available.
	public static final int GONE = 410;
	//
	// 摘要:
	// Equivalent to HTTP status 411.System.Net.HttpStatusCode.LengthRequired indicates
	// that the required Content-length header is missing.
	public static final int LENGTH_REQUIRED = 411;
	//
	// 摘要:
	// Equivalent to HTTP status 412.System.Net.HttpStatusCode.PreconditionFailed
	// indicates that a condition set for this request failed; and the request cannot
	// be carried out.条件是用条件请求标头（如 If-Match、If-None-Match 或 If-Unmodified-Since）设置的。
	public static final int PRECONDITION_FAILED = 412;
	//
	// 摘要:
	// Equivalent to HTTP status 413.System.Net.HttpStatusCode.RequestEntityTooLarge
	// indicates that the request is too large for the server to process.
	public static final int REQUEST_ENTITY_TOO_LARGE = 413;
	//
	// 摘要:
	// Equivalent to HTTP status 414.System.Net.HttpStatusCode.RequestUriTooLong
	// indicates that the URI is too long.
	public static final int REQUEST_URI_TOO_LONG = 414;
	//
	// 摘要:
	// Equivalent to HTTP status 415.System.Net.HttpStatusCode.UnsupportedMediaType
	// indicates that the request is an unsupported type.
	public static final int UNSUPPORTED_MEDIA_TYPE = 415;
	//
	// 摘要:
	// Equivalent to HTTP status 416.System.Net.HttpStatusCode.RequestedRangeNotSatisfiable
	// indicates that the range of data requested from the resource cannot be returned;
	// either because the beginning of the range is before the beginning of the
	// resource; or the end of the range is after the end of the resource.
	public static final int REQUESTED_RANGE_NOT_SATISFIABLE = 416;
	//
	// 摘要:
	// Equivalent to HTTP status 417.System.Net.HttpStatusCode.ExpectationFailed
	// indicates that an expectation given in an Expect header could not be met
	// by the server.
	public static final int EXPECTATION_FAILED = 417;
	//
	// 摘要:
	// Equivalent to HTTP status 500.System.Net.HttpStatusCode.InternalServerError
	// indicates that a generic error has occurred on the server.
	public static final int INTERNAL_SERVER_ERROR = 500;
	//
	// 摘要:
	// Equivalent to HTTP status 501.System.Net.HttpStatusCode.NotImplemented indicates
	// that the server does not support the requested function.
	public static final int NOT_IMPLEMENTED = 501;
	//
	// 摘要:
	// Equivalent to HTTP status 502.System.Net.HttpStatusCode.BadGateway indicates
	// that an intermediate proxy server received a bad response from another proxy
	// or the origin server.
	public static final int BAD_GATEWAY = 502;
	//
	// 摘要:
	// Equivalent to HTTP status 503.System.Net.HttpStatusCode.ServiceUnavailable
	// indicates that the server is temporarily unavailable; usually due to high
	// load or maintenance.
	public static final int SERVICE_UNAVAILABLE = 503;
	//
	// 摘要:
	// Equivalent to HTTP status 504.System.Net.HttpStatusCode.GatewayTimeout indicates
	// that an intermediate proxy server timed out while waiting for a response
	// from another proxy or the origin server.
	public static final int GATEWAY_TIMEOUT = 504;
	//
	// 摘要:
	// Equivalent to HTTP status 505.System.Net.HttpStatusCode.HttpVersionNotSupported
	// indicates that the requested HTTP version is not supported by the server.
	public static final int HTTP_VERSION_NOT_SUPPORTED = 505;
}
