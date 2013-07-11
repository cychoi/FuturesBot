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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UpdownNew implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double direction;
	private List<Integer> sgxvol = new ArrayList<Integer>();
	private List<Date> sgxdate = new ArrayList<Date>();

	public static void main(String args[]) {
		try {
			FileInputStream fis = new FileInputStream("Updown.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			UpdownNew up = (UpdownNew) ois.readObject();
			ois.close();

			FileOutputStream fos = new FileOutputStream("Updown.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(up);
			oos.close();
		} catch (Exception ex) {
			System.out.println("Exception thrown during writing Options: "
					+ ex.toString());
		}
	}

	public UpdownNew(int direction) {
		this.direction = direction;
	}

	public UpdownNew() {
	}

	public double getDirection() {
		return direction;
	}

	public void setDirection(double direction) {
		this.direction = direction;
	}
	
	public void addsgxvol(Date date, int input){
		if (sgxvol.size() >= 5) {
			sgxvol.remove(0);
			sgxdate.remove(0);
		}
		sgxvol.add(input);
		sgxdate.add(date);
	}
	
	public List<String> getsgxRec(){
		List<String> list = new ArrayList<String>();
		int i =0;
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMdd");
		for(int vol:sgxvol){
			list.add(dateFormat.format(sgxdate.get(i)) + ":" + vol);
			i++;
		}
		return list;
	}
	
	public List<Integer> getsgxvol() {
		return sgxvol;
	}
	
	public List<Date> getsgxdate() {
		return sgxdate;
	}
}
