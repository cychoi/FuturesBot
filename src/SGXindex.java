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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;


public class SGXindex implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private List<Double> SGXpercent = new ArrayList<Double>();
	private List<Double> SGXHistory = new ArrayList<Double>();
	private List<Double> SGXGap = new ArrayList<Double>();
	private double openindex;
	
	public static void main(String args[]) {
		try {
		FileInputStream fis = new FileInputStream("D:\\Dropbox\\SGX.ser");
		ObjectInputStream ois = new ObjectInputStream(fis);
		SGXindex sgx = (SGXindex) ois.readObject();
		ois.close();
		
		sgx.setOpenindex(277.7);
		double d1 = (277.7/273.8 - 1);
		double d2 = (7735.0/7640.0 - 1);
		sgx.addGap(d1 - d2);
		
		FileOutputStream fos = new FileOutputStream("D:\\Dropbox\\SGX.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(sgx);
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public SGXindex (){
	}
	
	private void addPercent(double input){
		if (SGXpercent.size() >= 10) {
			SGXpercent.remove(0);
		}
		SGXpercent.add(input);
	}
	
	private void addHistory(double input){
		if (SGXHistory.size() >= 10) {
			SGXHistory.remove(0);
		}
		SGXHistory.add(input);
	}
	
	public List<Double> getAllPercent(){
		return SGXpercent;
	}
	
	public Double getLastPercent() {
		if (SGXpercent.size() > 0)
			return SGXpercent.get(SGXpercent.size() - 1);
		else
			return 0.0d;
	}

	public double getOpenindex() {
		return openindex;
	}

	public void setOpenindex(double input) {
		addHistory(input);
		addPercent((input / this.openindex) - 1);
		this.openindex = input;
	}
	
	public void setPreSettle(double input) {
		this.openindex = input;
	}
	
	public void addGap(double input){
		if (SGXGap.size() >= 10) {
			SGXGap.remove(0);
		}
		SGXGap.add(input);
	}
	
	public double getlastSGX() {
		if (SGXGap.size() > 0)
			return SGXGap.get(SGXGap.size() - 1);
		else
			return 0;
	}
}
