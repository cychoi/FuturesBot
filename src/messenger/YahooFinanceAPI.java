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
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

public class YahooFinanceAPI {
	static CookieStore cookiestore;
	static CookieStore cookiestore1;
	// static LogFile txt1;
	static boolean use_proxy = false;
	static String PROXY_NAME = "proxy.hinet.net";
	static int PROXY_PORT = 80;
	static String url = "http://finance.yahoo.com/d/quotes.csv?s=";
	static String format = "&f=nl1vhgd1pt1c6";

	public static void main(String[] args) {
		YahooFinanceAPI api = new YahooFinanceAPI();
		double percent = api.getKSPercent();
		System.out.println(percent);
	}
	
	private Date transDate(String YYMMDD){
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(dateFormat.parse(YYMMDD));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return cal.getTime();
	}
	
	private Date getToday(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Calendar cal = Calendar.getInstance();
		try {
			Date today = new Date();
			cal.setTime(dateFormat.parse(dateFormat.format(today)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return cal.getTime();
	}
	
	public Double getKSPercent(){
		String contents = httpget(url + "%5EKS11" + format);
		//System.out.println(contents);
		String[] content = contents.split(",");
		
		Date Kdate = transDate(content[5].replaceAll("\"", ""));
		Date today = getToday();
		int i = today.compareTo(Kdate);
		if (i == 0) {
			double price = Double.parseDouble(content[1]);
			double vol = Double.parseDouble(content[8].replace("\"", "").replace("+", ""));
			return vol/price;
		} else {
			return null;
		}
		
	}
	
	public Double getKSPrice(){
		String contents = httpget(url + "%5EKS11" + format);
		//System.out.println(contents);
		String[] content = contents.split(",");
		return Double.parseDouble(content[1]);
	}
	

	public static String httppost(String url, List<NameValuePair> header,
			String refer, boolean cookie) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		if (cookie)
			httpclient.setCookieStore(cookiestore);
		else if (cookiestore1 != null)
			httpclient.setCookieStore(cookiestore1);
		if (use_proxy) {
			HttpHost proxy = new HttpHost(PROXY_NAME, PROXY_PORT);
			httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					proxy);
		}

		HttpPost httpPost = new HttpPost(url);
		httpPost.getParams().setParameter(ClientPNames.COOKIE_POLICY,
				CookiePolicy.BROWSER_COMPATIBILITY);

		Header[] headers = {
				new BasicHeader(
						"Accept",
						"application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5"),
				new BasicHeader("Content-Type",
						"application/x-www-form-urlencoded"),
				new BasicHeader("Origin", "http://aomp.judicial.gov.tw"),
				new BasicHeader("Referer", refer),
				new BasicHeader(
						"User-Agent",
						"Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/534.3 (KHTML, like Gecko) Chrome/6.0.472.55 Safari/534.3") };

		httpPost.setHeaders(headers);

		try {
			httpPost.setEntity(new UrlEncodedFormEntity(header, "Big5"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		HttpResponse response = null;
		String responseString = null;
		try {
			response = httpclient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				if (cookie)
					cookiestore = httpclient.getCookieStore();
				else
					cookiestore1 = httpclient.getCookieStore();
				responseString = EntityUtils.toString(response.getEntity());
				// 如果回傳是 200 OK 的話才輸出
				// System.out.println(responseString);
				//
			} else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY) {
				Header[] urlh = response.getAllHeaders();
				System.out.println(urlh.toString());
			} else {
				System.out.println(response.getStatusLine());
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

	public static String httpget(String url) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		if (use_proxy) {
			HttpHost proxy = new HttpHost(PROXY_NAME, PROXY_PORT);
			httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					proxy);
		}

		HttpGet httpget = new HttpGet(url);
		// Override the default policy for this request

		httpget.getParams().setParameter(ClientPNames.COOKIE_POLICY,
				CookiePolicy.BROWSER_COMPATIBILITY);
		
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
