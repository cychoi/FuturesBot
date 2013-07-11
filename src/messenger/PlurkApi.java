/*
 * TradingBot - A Java Trading system..
 * 
 * Copyright (C) 2013 Philipz (philipzheng@gmail.com)
 * http://www.tradingbot.com.tw/
 * http://www.facebook.com/tradingbot
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Apache License, Version 2.0 授權中文說明
 * http://www.openfoundry.org/licenses/29
 * 利用 Apache-2.0 程式所應遵守的義務規定
 * http://www.openfoundry.org/tw/legal-column-list/8950-obligations-of-apache-20
 */
package messenger;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class PlurkApi {

	private static final String API_KEY = "PLURK_API_KEY";
	static boolean use_proxy = false;
	static String PROXY_NAME = "proxy.hinet.net";
	static int PROXY_PORT = 80;
	static CookieStore cookiestore;
	static final String username= "PLURK_ID";
	static final String password= "PLURK_PASSWD";
	private volatile static PlurkApi plurk;
	
	private PlurkApi(){}
	
	public static PlurkApi getInstance(){
		if (plurk == null) {
			synchronized (PlurkApi.class){
				if (plurk == null) {
					plurk = new PlurkApi();
				}
			}
		}
		return plurk;
	}

    public static String getApiUri(String uri) {
        return "http://www.plurk.com/API" + uri;
    }

    public final static void main(String[] args) {
    	PlurkApi p = PlurkApi.getInstance();
    	p.plurkAdd("API測試111111！");
    	p.logout();
    }
    
    public String login() {
    	DefaultHttpClient httpclient = new DefaultHttpClient();
        if (use_proxy) {
			HttpHost proxy = new HttpHost(PROXY_NAME, PROXY_PORT);
			httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					proxy);
		}
        
        HttpGet httpget = new HttpGet(getApiUri("/Users/login?"+
                            "api_key=" + API_KEY + "&" +
                            "username=" + username + "&" +
                            "password=" + password
                          ));
        HttpResponse response = null;
		String responseString = null;
		try {
			response = httpclient.execute(httpget);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				cookiestore = httpclient.getCookieStore();
				responseString = EntityUtils.toString(response.getEntity());
				// 如果回傳是 200 OK 的話才輸出
				// System.out.println(responseString);
				//
			} else {
				System.out.println(response.getStatusLine());
				responseString = EntityUtils.toString(response.getEntity());
				System.out.println(responseString);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		httpclient.getConnectionManager().shutdown();
		return responseString;
    }
    
    public String plurkAdd(String url) {
    	PlurkApi p = PlurkApi.getInstance();
    	if (cookiestore == null)
    		p.login();
    	DefaultHttpClient httpclient = new DefaultHttpClient();
        if (use_proxy) {
			HttpHost proxy = new HttpHost(PROXY_NAME, PROXY_PORT);
			httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					proxy);
		}
        httpclient.setCookieStore(cookiestore);
       
        HttpResponse response = null;
		String responseString = null;
		try {
			String content = URLEncoder.encode(url, "UTF-8");
			HttpGet httpget = new HttpGet(getApiUri("/Timeline/plurkAdd?"+
	                "api_key=" + API_KEY + "&" +
	                "content=" + content + "&" +
	                "qualifier=" + "says" + "&" +
	                "lang=tr_ch"));
			response = httpclient.execute(httpget);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				cookiestore = httpclient.getCookieStore();
				responseString = EntityUtils.toString(response.getEntity());
				// 如果回傳是 200 OK 的話才輸出
				// System.out.println(responseString);
				//
			} else {
				System.out.println(response.getStatusLine());
				responseString = EntityUtils.toString(response.getEntity());
				System.out.println(responseString);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		httpclient.getConnectionManager().shutdown();
		return responseString;
    }
    
    /*
     * Private Plurk add limited_to=[0]
     */
    /*public String plurkAdd(String url) {
    	PlurkApi p = PlurkApi.getInstance();
    	if (cookiestore == null)
    		p.login();
    	DefaultHttpClient httpclient = new DefaultHttpClient();
        if (use_proxy) {
			HttpHost proxy = new HttpHost(PROXY_NAME, PROXY_PORT);
			httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					proxy);
		}
        httpclient.setCookieStore(cookiestore);
       
        HttpResponse response = null;
		String responseString = null;
		try {
			String content = URLEncoder.encode(url, "UTF-8");
			HttpGet httpget = new HttpGet(getApiUri("/Timeline/plurkAdd?"+
	                "api_key=" + API_KEY + "&" +
	                "content=" + content + "&" +
	                "qualifier=" + "says" + "&" +
	                "lang=tr_ch&limited_to=[0]"));
			response = httpclient.execute(httpget);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				cookiestore = httpclient.getCookieStore();
				responseString = EntityUtils.toString(response.getEntity());
				// 如果回傳是 200 OK 的話才輸出
				// System.out.println(responseString);
				//
			} else {
				System.out.println(response.getStatusLine());
				responseString = EntityUtils.toString(response.getEntity());
				System.out.println(responseString);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		httpclient.getConnectionManager().shutdown();
		return responseString;
    }*/

    public String logout() {
    	PlurkApi p = PlurkApi.getInstance();
    	if (cookiestore == null)
    		p.login();
    	DefaultHttpClient httpclient = new DefaultHttpClient();
        if (use_proxy) {
			HttpHost proxy = new HttpHost(PROXY_NAME, PROXY_PORT);
			httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					proxy);
		}
        httpclient.setCookieStore(cookiestore);
       
        HttpResponse response = null;
		String responseString = null;
		try {
			 HttpGet httpget = new HttpGet(getApiUri("/Users/logout?"+
                     "api_key=" + API_KEY));
			response = httpclient.execute(httpget);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				cookiestore = httpclient.getCookieStore();
				responseString = EntityUtils.toString(response.getEntity());
				// 如果回傳是 200 OK 的話才輸出
				// System.out.println(responseString);
				//
			} else {
				System.out.println(response.getStatusLine());
				responseString = EntityUtils.toString(response.getEntity());
				System.out.println(responseString);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		httpclient.getConnectionManager().shutdown();
		return responseString;
    }
    
}
