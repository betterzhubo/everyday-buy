package com.seasoning.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

//@Component
@Slf4j
public class HttpUtil {

	/**
	 * 返回json字符
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException
	 */
	public static String post(String url, Map<String, String> params)
			throws IOException {
		HttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> nvps = new LinkedList<NameValuePair>();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
		HttpResponse response = httpclient.execute(httpPost);
		HttpEntity entity = response.getEntity();
		return EntityUtils.toString(entity);
	}

	/**
	 * 返回json字符
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String get(String url) throws IOException {
		HttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response = httpclient.execute(httpGet);
		HttpEntity entity = response.getEntity();
		return EntityUtils.toString(entity);
	}

	public static String doGet(String url, Map<String, String> param) {

		// 创建Httpclient对象
		CloseableHttpClient httpclient = HttpClients.createDefault();

		String resultString = "";
		CloseableHttpResponse response = null;
		try {
			// 创建uri
			URIBuilder builder = new URIBuilder(url);
			if (param != null) {
				for (String key : param.keySet()) {
					builder.addParameter(key, param.get(key));
				}
			}
			java.net.URI uri = builder.build();

			// 创建http GET请求
			HttpGet httpGet = new HttpGet(uri);

			// 执行请求
			response = httpclient.execute(httpGet);
			// 判断返回状态是否为200
			if (response.getStatusLine().getStatusCode() == 200) {
				resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
			}
		} catch (Exception e) {
			log.error("send get request fail", e);
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				httpclient.close();
			} catch (IOException e) {
				log.error("close resource fail", e);
			}
		}
		log.info("request content: "+ resultString);
		return resultString;
	}

	public static String postXML(String url,String xml){
		CloseableHttpClient client = null;
		CloseableHttpResponse resp = null;
		try{
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Content-Type", "text/xml; charset=UTF-8");
			client = HttpClients.createDefault();
			StringEntity entityParams = new StringEntity(xml,"utf-8");
			httpPost.setEntity(entityParams);
			client = HttpClients.createDefault();
			resp = client.execute(httpPost);
			String resultMsg = EntityUtils.toString(resp.getEntity(),"utf-8");
			return resultMsg;
		}catch (Exception e){
			log.error("", e);
		}finally {
			try {
				if(client!=null){
					client.close();
				}
				if(resp != null){
					resp.close();
				}
			} catch (IOException e) {
				log.error("", e);
			}
		}
		return null;
	}


	public static void main(String[] args) throws IOException{
		String result = HttpUtil.get("https://baidu.com");
		System.out.println(result);
	}
}
