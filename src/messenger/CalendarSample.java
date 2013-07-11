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
/*
 * Copyright (c) 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package messenger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.FileCredentialStore;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar.Calendars.Get;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

/**
 * @author Yaniv Inbar
 */
public class CalendarSample {

  /** Global instance of the HTTP transport. */
  private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

  /** Global instance of the JSON factory. */
  private static final JsonFactory JSON_FACTORY = new JacksonFactory();

  private static com.google.api.services.calendar.Calendar client;
  
  private volatile static CalendarSample Calendarclient;
  private static Calendar calendar;
  //static String authToken; 

  /** Authorizes the installed application to access user's protected data. */
  private static Credential authorize() throws Exception {
    // load client secrets
    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
        JSON_FACTORY, new FileInputStream("client_secrets.json_futuresbot"));
    if (clientSecrets.getDetails().getClientId().startsWith("Enter")
        || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
      System.out.println(
          "Enter Client ID and Secret from https://code.google.com/apis/console/?api=calendar "
          + "into calendar-cmdline-sample/src/main/resources/client_secrets.json");
      System.exit(1);
    }
    // set up file credential store
    FileCredentialStore credentialStore = new FileCredentialStore(
        new File("calendar.json_futuresbot"), JSON_FACTORY);
    // set up authorization code flow
    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets,
        Collections.singleton(CalendarScopes.CALENDAR)).setCredentialStore(credentialStore).build();
    // authorize
    return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
  }
  
  private CalendarSample(){
	  try {
	      try {
	        // authorization
	        Credential credential = authorize();
	        // set up global Calendar instance
	        client = new com.google.api.services.calendar.Calendar.Builder(
	            HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(
	            "TradingBot Real-time SMS/1.0").build();
	        // run commands
	        calendar = getMyCalendar();
	      } catch (IOException e) {
	        System.err.println(e.getMessage());
	      }
	    } catch (Throwable t) {
	      t.printStackTrace();
	    }
  }
  public static CalendarSample getInstance(){
		if (Calendarclient == null) {
			synchronized (CalendarSample.class){
				if (Calendarclient == null) {
					Calendarclient = new CalendarSample();
				}
			}
		}
		return Calendarclient;
	}
  
  public static void main(String[] args) {
	  CalendarSample c = CalendarSample.getInstance();
      c.addEvent("TradingBot SMS 簡訊測試!!!");
  }
  
  private static Calendar getMyCalendar() throws IOException {
    Get get = client.calendars().get("YOUR_GOOGLE_CALENDAR_ID_SN");
    Calendar result = get.execute();
    return result;
  }

  public void addEvent(String input){
    //View.header("Add Event");
    Event event = new Event();
    event.setSummary(input);   
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTime(new Date());
    cal.add(GregorianCalendar.MINUTE, 0);
    DateTime start = new DateTime(cal.getTime(), TimeZone.getTimeZone("UTC"));
    event.setStart(new EventDateTime().setDateTime(start)); 
    event.setEnd(new EventDateTime().setDateTime(start));
    try {
		Event result = client.events().insert(calendar.getId(), event).execute();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
}
