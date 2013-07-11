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
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class GetWednesday {
	
	static Map<String, String> map = new HashMap<String, String>();
	static Map<String, String> SGXmap = new HashMap<String, String>();
	
	public static void main(String[] args) {
		System.out.println(getSGXClose("20120204"));
		System.out.println(getSGXClose("20120227"));
		System.out.println(getSGXClose("20100225"));
		System.out.println(getSGXClose("20100226"));
		System.out.println(getSGXClose("20110225"));
		System.out.println(getSGXClose("20110224"));
		System.out.println(getSGXClose("20110428"));
		System.out.println(getSGXClose("20110530"));
		System.out.println(compareWed1("20100519"));
		System.out.println(compareWed1("20100520"));
		System.out.println(compareWed2("20100519"));
		System.out.println(compareWed2("20100520"));
		System.out.println(getSymbol("20100519"));
		System.out.println(getSymbol("20100520"));
		System.out.println(getOpweek());
		if (GetWednesday.isSGXClose()) {
			System.out.println(GetWednesday.getNextSGXSymbol());
		}
	}
	
	public static int getOpweek(String yyyymmdd){
		Date d = null;   
        SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");   
        Date c = null;
		try {
			d=df.parse(yyyymmdd);
			c = df.parse(compareWed2(yyyymmdd));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double day = (c.getTime() - d.getTime())/ (24 * 60 * 60 *1000);
        double week = day / 7;
        week = Math.ceil(week);
        return (int)week;
	}
	
	public static int getOpday(String yyyymmdd){
		Date d = null;   
        SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");   
        Date c = null;
		try {
			d=df.parse(yyyymmdd);
			c = df.parse(compareWed2(yyyymmdd));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double day = (c.getTime() - d.getTime())/ (24 * 60 * 60 *1000);
        return (int)day;
	}
	
	public static int getOpweek(){
		Date d = new Date();   
        SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");   
        Date c = null;
		try {
			d = df.parse(df.format(d));
			c = df.parse(compareWed2(df.format(d)));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double day = (c.getTime() - d.getTime())/ (24 * 60 * 60 *1000);
        double week = day / 7;
        week = Math.ceil(week);
        return (int)week;
	}
	
	public static int getSGXday(String yyyymmdd){
		Date d = null;   
        SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");   
        Date c = null;
		try {
			d=df.parse(yyyymmdd);
			c = df.parse(getSGXClose(yyyymmdd));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double day = (c.getTime() - d.getTime())/ (24 * 60 * 60 *1000);
        return (int)day;
	}
	
	private static String getWed(String yyyymm) {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			in = new BufferedReader(new FileReader("D:\\Dropbox\\closeday.txt"));
			String s1;
			while ((s1 = in.readLine()) != null) {
				String[] tmp = s1.split(" ");
				map.put(tmp[0], tmp[1]);
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String result = map.get(yyyymm);
		if (result == null) {
			// int weekdays[] = new int[12];
			String curDate = yyyymm + "01";

			GregorianCalendar cal = new GregorianCalendar();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

			try {
				cal.setTime(dateFormat.parse(curDate));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			cal.set(GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.WEDNESDAY);
			java.util.Date today = null, close = null;
			try {
				today = dateFormat.parse(curDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			close = cal.getTime();
			int i = 1;
			if (today.after(close))
				i = 0;
			/*
			 * if (cal.get(GregorianCalendar.YEAR) == startYear)
			 * weekdays[cal.get(GregorianCalendar.MONTH)]++;
			 */
			while (true) {
				// System.out.println("Wednesday " + i + "=" +
				// dateFormat.format(cal.getTime()));
				if (i == 3)
					break;
				i++;
				cal.add(GregorianCalendar.DATE, 7);
			}
			result = dateFormat.format(cal.getTime());
		}
		return result;
	}
	
	public static String compareWed(String yyyymmdd){
		GregorianCalendar cal = basicWed(yyyymmdd);
		SimpleDateFormat sdf1=new SimpleDateFormat("yyyyMM");
		return sdf1.format(cal.getTime());
	}
	
	public static String compareWed1(String yyyymmdd){
		GregorianCalendar cal = basicWed(yyyymmdd);
		SimpleDateFormat sdf1=new SimpleDateFormat("yyyy/MM");
		return sdf1.format(cal.getTime());
	}
	
	public static String compareWed2(String yyyymmdd){
		return getWed(compareWed(yyyymmdd));
	}
	
	private static GregorianCalendar basicWed(String yyyymmdd){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		String yyyymm = yyyymmdd.substring(0, 6);
		java.util.Date today = null, close = null;
		GregorianCalendar cal = new GregorianCalendar();
		try {
			today=sdf.parse(yyyymmdd);
			close=sdf.parse(getWed(yyyymm));
			cal.setTime(sdf.parse(yyyymmdd));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (today.after(close)){
			cal.add(GregorianCalendar.MONTH, 1);
		}
		return cal;
	}
	
	public static String compareWed(Date yyyymmdd){
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String time=df.format(yyyymmdd);
		return compareWed(time);
	}
	
	public static String compareWed(){
		Date yyyymmdd = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String time=df.format(yyyymmdd);
		return compareWed(time);
	}
	
	public static String compareWed1(){
		Date yyyymmdd = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String time=df.format(yyyymmdd);
		return compareWed1(time);
	}
	
	public static String gettoday(){
		Date yyyymmdd = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String time=df.format(yyyymmdd);
		return time;
	}
	
	public static String getSymbol(){
		String yyyymm = compareWed();
		String symbol = "TXF";  //MXF,MTX
		int num = Integer.parseInt(yyyymm.substring(4, 6));
		String year = yyyymm.substring(3, 4);
		num = num + 64;
		char c = (char)num;
		symbol = symbol + c + year;
		return symbol;
	}
	
	public static String getOPSymbol(String range, boolean buy){
		String yyyymm = compareWed();
		String symbol = "TXO";  //MXF,MTX
		int num = Integer.parseInt(yyyymm.substring(4, 6));
		String year = yyyymm.substring(3, 4);
		num = num + 64;
		if (!buy)
			num = num + 12;
		if (range.length() < 5){
			for(int i = 5;i > range.length() ;i--)
				range = "0" + range;
		}
		char c = (char)num;
		symbol = symbol + range + c + year;
		return symbol;
	}
	
	public static String getSymbol(String yyyymmdd){
		String yyyymm = compareWed(yyyymmdd);
		String symbol = "TXF";  //MXF,MTX
		int num = Integer.parseInt(yyyymm.substring(4, 6));
		String year = yyyymm.substring(3, 4);
		num = num + 64;
		char c = (char)num;
		symbol = symbol + c + year;
		return symbol;
	}
	
	public static String getSGXSymbol(String yyyymmdd){
		String yyyymm = getSGXClose(yyyymmdd);
		String symbol = "TAX";  //MXF,MTX
		String month = yyyymm.substring(4, 6);
		symbol = symbol + month;
		return symbol;
	}
	
	public static String getSGXSymbolJson(String yyyymmdd){
		String yyyymm = getSGXClose(yyyymmdd);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-M");
		Date today = null;
		try {
			today = dateFormat.parse(yyyymm);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dateFormat1.format(today);
		
	}
	
	public static String getSGXSymbol(){
		return getSGXSymbol(gettoday());
	}
	
	public static String getSGXSymbolJson(){
		return getSGXSymbolJson(gettoday());
	}
	
	public static String getNextSGXSymbol(){
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		cal.add(GregorianCalendar.DATE, 1);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		return getSGXSymbol(dateFormat.format(cal.getTime()));
	}
	
	public static String getNextSGXSymbolJson(){
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		cal.add(GregorianCalendar.DATE, 1);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		return getSGXSymbolJson(dateFormat.format(cal.getTime()));
	}
	
	public static String getSGXQuote(){
		String yyyymm = getSGXClose(gettoday());
		String yy = yyyymm.substring(2,4);
		String mm = yyyymm.substring(4, 6);
		if (mm.substring(0,1).endsWith("0"))
			mm = yyyymm.substring(5, 6);
		return yy + "," + mm;
	}
	
	public static String getSGXClose(String yyyymmdd) {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			in = new BufferedReader(new FileReader("D:\\Dropbox\\SGXcloseday.txt"));
			String s1;
			while ((s1 = in.readLine()) != null) {
				String[] tmp = s1.split(" ");
				SGXmap.put(tmp[0], tmp[1]);
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String yyyymm = yyyymmdd.substring(0, 6);
		String result = SGXmap.get(yyyymm);
		int dd = Integer.parseInt(yyyymmdd.substring(6, 8));
		int newdd = 0;
		boolean overflag = false;
		if (result != null) {
			newdd = Integer.parseInt(result.substring(6, 8));
			if (dd > newdd)
				overflag = true;
		}
		if (result == null || overflag) {
			// int weekdays[] = new int[12];
			String curDate;
			if (overflag) {
				String mm = yyyymmdd.substring(4, 6);
				int month = Integer.parseInt(mm) + 1;
				if (month > 9)
					curDate = yyyymmdd.substring(0, 4) + month + "01";
				else
					curDate = yyyymmdd.substring(0, 4) + "0" + month + "01";
			} else {
				curDate = yyyymmdd.substring(0, 6) + "01";
			}

			GregorianCalendar cal = new GregorianCalendar();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

			try {
				cal.setTime(dateFormat.parse(curDate));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			cal.add(GregorianCalendar.MONTH, 1);
			cal.add(GregorianCalendar.DATE, -1);
			
			if (cal.get(Calendar.MONTH) == Calendar.FEBRUARY)
				if ((cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) || (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY))
					cal.add(GregorianCalendar.DATE, -1);
			if ((cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) || (cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY))
				cal.add(GregorianCalendar.DATE, -3);
			else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
				cal.add(GregorianCalendar.DATE, -2);
			else
				cal.add(GregorianCalendar.DATE, -1);
			
			java.util.Date today = null, close = null;
			close = cal.getTime();
			try {
				today = dateFormat.parse(yyyymmdd);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String tmp = dateFormat.format(cal.getTime());
			if (today.after(close)){
				cal.add(GregorianCalendar.MONTH, 1);
				cal.set(GregorianCalendar.DATE, 1);
				tmp = dateFormat.format(cal.getTime());
				tmp = getSGXClose(tmp);
			}
			result = tmp;
		} 
		return result;
	}
	
	public static boolean isSGXClose() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Date today = new Date();
		String today_s = dateFormat.format(today);
		String close = getSGXClose(today_s);
		if (today_s.equals(close))
			return true;
		else
			return false;
	}
	
	public static boolean isSGXClose(String today_s) {
		String close = getSGXClose(today_s);
		if (today_s.equals(close))
			return true;
		else
			return false;
	}
	
	public static Date getYesterday(Date input) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(input);
		cal.add(GregorianCalendar.DATE, -1);
		
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
			cal.add(GregorianCalendar.DATE, -2);
		else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
			cal.add(GregorianCalendar.DATE, -1);
		Date close = null;
		close = cal.getTime();
		return close;
	}
	
	public static Calendar getYesterday(Calendar cal) {
		cal.add(GregorianCalendar.DATE, -1);
		
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
			cal.add(GregorianCalendar.DATE, -2);
		else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
			cal.add(GregorianCalendar.DATE, -1);
		return cal;
	}
}
