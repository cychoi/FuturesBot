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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Futures implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double price;
	private int current;
	private int gapcount;
	private int HighVol = 0; //HighVol = 1
	private List<Double> history = new ArrayList<Double>();
	private List<Double> vol = new ArrayList<Double>();
	private int preSettle;

	public static void main(String args[]) {
		FileInputStream fis;
		try {
			fis = new FileInputStream("D:\\Dropbox\\Fu.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			Futures fu = (Futures) ois.readObject();
			ois.close();
			fu.addHistory(7741.24);
			fu.addvol((7787.89 / 7727.29) - 1);
			fu.setPreSettle(7735);
			FileOutputStream fos = new FileOutputStream("D:\\Dropbox\\Fu.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(fu);
			oos.close();
		} catch (Exception ex) {
			System.out.println("Exception thrown during writing Options: "
					+ ex.toString());
		}
	}

	public Futures(int current, double price) {
		this.current = current;
		this.price = price;
	}

	public Futures() {
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	public int getGapcount() {
		return gapcount;
	}

	public void setGapcount(int gapcount) {
		this.gapcount = gapcount;
	}

	public List<Double> getHistory() {
		return history;
	}

	public void setHistory(List<Double> history) {
		this.history = history;
	}

	public List<Double> getVol() {
		return vol;
	}

	public void setVol(List<Double> vol) {
		this.vol = vol;
	}
	
	public void addHistory(double input){
		if (history.size() >= 5)
			history.remove(0);
		history.add(input);
	}
	
	public void addvol(double input){
		if (vol.size() >= 5)
			vol.remove(0);
		vol.add(input);
	}
	
	public int getHistoryPro(){
		Object[] t = history.toArray();
		Arrays.sort(t);
		List<Object> list = Arrays.asList(t);
		double min = (Double) list.get(0);
		double max = (Double) list.get(history.size() - 1);
		int minpos = history.lastIndexOf(min);
		int maxpos = history.indexOf(max);
		int updown = 0;
		if ((minpos == 0) && (4 == maxpos)) {
			updown = -2;
			HighVol = 1;
		} else if ((minpos == 1) && (4 == maxpos)) {
			updown = -1;
		} else if ((maxpos == 0) && (4 == minpos)) {
			updown = 2;
			HighVol = 1;
		} else if ((maxpos == 1) && (4 == minpos)) {
			updown = 1;
		} else if ((minpos == 0) && (3 == maxpos)) {
			HighVol = 2;
		} else if ((maxpos == 0) && (3 == minpos)) {
			HighVol = 2;
		}
		return updown;
	}
	
	public double getVolPro(){	
		double avg = 0;
		for (double each: vol){
			avg = avg + each;
		}
		avg = avg / 5;
		int volindex = 0;
		if (avg >= 0.02)
			volindex = 2;
		else if (avg >= 0.01)
			volindex = 1;
		return (volindex + HighVol) * 0.25; 
	}

	public int getPreSettle() {
		return preSettle;
	}

	public void setPreSettle(int preSettle) {
		this.preSettle = preSettle;
	}
}
