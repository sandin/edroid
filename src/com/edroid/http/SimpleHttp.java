package com.edroid.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

import android.util.Log;

/**
 * HttpClient 
 * 
 * .GET
 * <code>
 * String html = SimpleHttp.doGet("http://www.google.com.hk");
 * </code>
 * 
 * .POST
 * <code>
 * List<NameValuePair> params = new ArrayList<NameValuePair>();
 * params.add(new BasicNameValuePair("keyword", "china"));
 * String res = SimpleHttp.doPost("http://www.google.com.hk", params);
 * </code>
 * 
 * .Get response InputStream
 * <code>
 * final SimpleHttp http = SimpleHttp.getInstance();
 * Response res = http.get("http://www.google.com.hk");
 * InputStream in = res.asStream();
 * </code>
 * 
 * .Upload Files 
 * <code>
 * URI uri = new URI("http://www.google.com");
 * 
 * List<NameValuePair> params = new ArrayList<NameValuePair>();
 * params.add(new BasicNameValuePair("keyword", "china"));
 * 
 * Map<String, File> files = new HashMap<String, File>();
 * files.put("myphoto", new File("/home/lds/some.jpg"));
 * 
 * http.post(uri, params, files) 
 * </code>
 * 
 * 
 * 
 * @author lds
 *
 */
public class SimpleHttp {
	private static final String TAG = SimpleHttp.class.getSimpleName();
	private static final boolean DEBUG = true;
	
	// Wait this many milliseconds max for the TCP connection to be established
	private static final int CONNECTION_TIMEOUT_MS = 60 * 1000;
	// Wait this many milliseconds max for the server to send us data once the connection has been established
	private static final int SOCKET_TIMEOUT_MS =  5 * 60 * 1000;

	private static final int RETRIEVE_LIMIT = 20;
	private static final int RETRIED_TIME = 3;
	private static final int MAX_TOTAL_CONNECTIONS = 10;
	
	private static SimpleHttp mInstance;
	
	private DefaultHttpClient mClient; 
	private BasicHttpContext localContext;
	
	public static SimpleHttp getInstance() {
		if (mInstance == null) {
			mInstance = new SimpleHttp();
		}
		return mInstance;
	}
	
	private SimpleHttp() {
		prepareHttpClient();
	}
	
	/**
	 * Shutdown
	 * 
	 * TODO: shutdown以后是否还可以再使用（重新启动）?
	 */
	public void shutdown() {
		if (mClient != null) {
			mClient.getConnectionManager().shutdown();
		}
	}
	

	/**
	 * Send HTTP GET request
	 * 
	 * @param uri if you need send url params, can use URIBuilder
	 * @return
	 * @throws SimpleHttpException
	 */
	public Response get(URI uri) throws SimpleHttpException {
		HttpGet httpGet = new HttpGet(uri);
		return sendRequest(httpGet);
	}
	
	/**
     * Send HTTP GET request
     * 
     * @param url if you need send url params, can use URIBuilder
     * @return
     * @throws SimpleHttpException
     */
	public Response get(String uri) throws SimpleHttpException {
		return get(createURI(uri));
	}
	
	public Response get(String url, List<NameValuePair> postParams, String encoding) throws SimpleHttpException {
	    String params = URLEncodedUtils.format(postParams, encoding);
	    HttpGet httpGet = new HttpGet(url + "?" + params);
        return sendRequest(httpGet);
	}
	
	public Response get(String url, List<NameValuePair> postParams) throws SimpleHttpException {
	    return get(url, postParams, "UTF-8");
	}
	
	/**
	 * Send HTTP POST request
	 * 
	 * @param uri
	 * @param postParams
	 * @return
	 * @throws SimpleHttpException
	 */
	public Response post(URI uri, List<NameValuePair> postParams) throws SimpleHttpException {
		UrlEncodedFormEntity entity = null;
		if (DEBUG) Log.d(TAG, "Params: " + postParams);
		try {
			entity = new UrlEncodedFormEntity(postParams, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new SimpleHttpException(e.getMessage(), e);
		}
		
		return post(uri, entity);
	}
	
	/**
     * Send HTTP POST request
     * 
     * @param url
     * @param postParams
     * @return
     * @throws SimpleHttpException
     */
	public Response post(String uri, List<NameValuePair> postParams) throws SimpleHttpException {
		return post(createURI(uri), postParams);
	}
	
	
	/**
	 * 上传文件
	 * 
	 * TODO: 要支持上传文件必须加上org.apache.http.entity.mime.MultipartEntity支持
	 * 
	 * @deprecated 取消该方法
	 * @param uri
	 * @param postParams
	 * @param files
	 * @return
	 * @throws SimpleHttpException
	 */
	public Response post(URI uri, List<NameValuePair> postParams, Map<String, File> files) throws SimpleHttpException {
		/*
		MultipartEntity entity = createMultipartEntity(postParams, files);
		return post(uri, entity);
		*/
		throw new IllegalAccessError("上传文件需格外Mime包支持");
	}
	
	/**
	 * 创建可带一个File的MultipartEntity
	 * 
	 * @param filename
	 *            文件名
	 * @param file
	 *            文件
	 * @param postParams
	 *            其他POST参数
	 * @return 带文件和其他参数的Entity
	 * @throws UnsupportedEncodingException
	private MultipartEntity createMultipartEntity(ArrayList<BasicNameValuePair> postParams,
			Map<String, File> files)
			throws UnsupportedEncodingException {
		MultipartEntity entity = new MultipartEntity();

		Iterator<String> it = files.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			entity.addPart(it.next(), new FileBody(files.get(key)));
		}
		for (BasicNameValuePair param : postParams) {
			entity.addPart(param.getName(), new StringBody(param.getValue()));
		}
		return entity;
	}
	 */
	
	/**
	 * Send HTTP POST request
	 * 
	 * @param uri
	 * @param entity
	 * @return
	 * @throws SimpleHttpException
	 */
	public Response post(URI uri, HttpEntity entity) throws SimpleHttpException {
		HttpPost httpPost = new HttpPost(uri);
		if (entity != null) {
			httpPost.setEntity(entity);
		}
		return sendRequest(httpPost);
	}
	
	// shortcut
	
	/**
	 * Send a POST request, Get Response as String
	 * 
	 * @param url
	 * @param postParams
	 * @return
	 * @throws IOException
	 * @throws SimpleHttpException
	 */
	public static String doPost(String url, List<NameValuePair> postParams) throws IOException, SimpleHttpException {
	    final SimpleHttp http = SimpleHttp.getInstance();
	    Response res = http.post(url, postParams);
	    if (res != null) {
	        return res.asString();
	    }
	    return null;
	}
	
	/**
	 * Send a GET Request, Get Response as String
	 * @deprecated use doGet()
	 * 
	 * @param uri
	 * @return
	 * @throws SimpleHttpException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static String getAsString(String uri) throws SimpleHttpException, IllegalStateException, IOException {
	    return doGet(uri);
	}
	
	/**
     * Send a Get Request, Get Response as String
     * 
     * @param uri
     * @return
     * @throws SimpleHttpException
     * @throws IllegalStateException
     * @throws IOException
     */
	public static String doGet(String url) throws SimpleHttpException, IOException {
	    Response res = getInstance().get(url);
        if (res != null) {
            return res.asString();
        }
        return null;
	}


	/**
	 * 执行HTTP请求，并返回结果
	 * 
	 * @param httpUriRequest
	 * @return
	 * @throws SimpleHttpException
	 */
	private Response sendRequest(HttpUriRequest httpUriRequest)
			throws SimpleHttpException {
		if (DEBUG) {
			Log.d(TAG, "URL: " + httpUriRequest.getURI());
			Log.d(TAG, "Method: " + httpUriRequest.getMethod());
		}
		
		HttpResponse response = null;
		try {
			response = mClient.execute(httpUriRequest, localContext);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			throw new SimpleHttpException(e.getMessage(), e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new SimpleHttpException(e.getMessage(), e);
		}

		if (response != null) {
			return new Response(response);
		}
		if (DEBUG) Log.d(TAG, "---------------");
		return null;
	}
	
	/**
	 * Setup DefaultHttpClient
	 * 
	 * Use ThreadSafeClientConnManager.
	 */
	private void prepareHttpClient() {
		if (DEBUG) Log.d(TAG, "Create default http client");
		// Create and initialize HTTP parameters
		HttpParams params = new BasicHttpParams();
		ConnManagerParams.setMaxTotalConnections(params, MAX_TOTAL_CONNECTIONS);
		HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT_MS);
		HttpConnectionParams.setSoTimeout(params, SOCKET_TIMEOUT_MS);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

		// Create and initialize scheme registry
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", SSLSocketFactory
				.getSocketFactory(), 443));

		// Create an HttpClient with the ThreadSafeClientConnManager.
		ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(
				params, schemeRegistry);
		mClient = new DefaultHttpClient(cm, params);

		mClient.addResponseInterceptor(gzipResponseIntercepter); // Support GZIP
		mClient.setHttpRequestRetryHandler(requestRetryHandler);
		mClient.addRequestInterceptor(requestInterceptor);

		// TODO: need to release this connection in httpRequest()
		// cm.releaseConnection(conn, validDuration, timeUnit);
		// httpclient.getConnectionManager().shutdown();
		
		localContext = new BasicHttpContext();
	}
	
	private URI createURI(String uri) throws SimpleHttpException {
		try {
			return new URI(uri);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			throw new SimpleHttpException(e.getMessage(), e);
		}
	}
	
	// 
	
	private static HttpRequestInterceptor requestInterceptor = new HttpRequestInterceptor() {
		
		@Override
		public void process(HttpRequest request, HttpContext context)
				throws HttpException, IOException {
			request.addHeader("Accept-Encoding", "gzip, deflate");
			request.addHeader("Accept-Charset", "UTF-8,*;q=0.5");
		}
	};
	
	private static HttpResponseInterceptor gzipResponseIntercepter = new HttpResponseInterceptor() {
		public void process(HttpResponse response, HttpContext context)
				throws org.apache.http.HttpException, IOException {
			HttpEntity entity = response.getEntity();
			Header ceheader = entity.getContentEncoding();
			if (ceheader != null) {
				HeaderElement[] codecs = ceheader.getElements();
				for (int i = 0; i < codecs.length; i++) {
					if (codecs[i].getName().equalsIgnoreCase("gzip")) {
						response.setEntity(new GzipDecompressingEntity(response
								.getEntity()));
						return;
					}
				}
			}

		}
	};

	/**
	 * GZip Support
	 */
	static class GzipDecompressingEntity extends HttpEntityWrapper {

		public GzipDecompressingEntity(final HttpEntity entity) {
			super(entity);
		}

		@Override
		public InputStream getContent() throws IOException,
				IllegalStateException {

			// the wrapped entity's getContent() decides about repeatability
			InputStream wrappedin = wrappedEntity.getContent();
			return new GZIPInputStream(wrappedin);
		}

		@Override
		public long getContentLength() {
			// length of ungzipped content is not known
			return -1;
		}

	}
	
	/**
	 * 异常自动恢复处理, 使用HttpRequestRetryHandler接口实现请求的异常恢复
	 */
	private static HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {
		// 自定义的恢复策略
		public boolean retryRequest(IOException exception, int executionCount,
				HttpContext context) {
			// 设置恢复策略，在发生异常时候将自动重试N次
			if (executionCount >= RETRIED_TIME) {
				// Do not retry if over max retry count
				return false;
			}
			if (exception instanceof NoHttpResponseException) {
				// Retry if the server dropped connection on us
				return true;
			}
			if (exception instanceof SSLHandshakeException) {
				// Do not retry on SSL handshake exception
				return false;
			}
			HttpRequest request = (HttpRequest) context
					.getAttribute(ExecutionContext.HTTP_REQUEST);
			boolean idempotent = (request instanceof HttpEntityEnclosingRequest);
			if (!idempotent) {
				// Retry if the request is considered idempotent
				return true;
			}
			return false;
		}
	};

}
