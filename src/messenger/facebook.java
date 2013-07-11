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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

public class facebook implements MessageListener {
	XMPPConnection connection;
	private volatile static facebook fclient;
	static String password = "FB_PASSWD";
	static List<String> sendlist;
	
	private facebook(){}
	
	public static facebook getInstance(){
		if (fclient == null) {
			synchronized (facebook.class){
				if (fclient == null) {
					fclient = new facebook();
				}
			}
		}
		return fclient;
	}

	public void login(String userName, String password) throws XMPPException {
		SASLAuthentication.registerSASLMechanism("DIGEST-MD5", MySASLDigestMD5Mechanism.class);
		ConnectionConfiguration config = new ConnectionConfiguration(
				"chat.facebook.com", 5222, "chat.facebook.com");
		config.setCompressionEnabled(true);
		config.setSASLAuthenticationEnabled(true);
		connection = new XMPPConnection(config);

		connection.connect();
		connection.login(userName, password);
	}
	
	public void sendMessage(String message, String to) throws XMPPException {
		Chat chat = connection.getChatManager().createChat(to, this);
		chat.sendMessage(message);
	}

	public void displayBuddyList() {
		Roster roster = connection.getRoster();
		roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);
		Collection<RosterEntry> entries = roster.getEntries();

		System.out.println("\n\n" + entries.size() + " buddy(ies):");
		for (RosterEntry r : entries) {
			System.out.println(r.getName() + ":" + r.getUser());
		}
	}
	
	public void getBuddyList() {
		Roster roster = connection.getRoster();
		roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);
		Collection<RosterEntry> entries = roster.getEntries();
		sendlist = new ArrayList<String>();
		for (RosterEntry r : entries) {
			sendlist.add(r.getUser());
		}
	}
	
	public void addRoster(String bot, String email, String input){
		facebook c = facebook.getInstance();
		try {
			addRoster(bot,input);
			c.sendMessage(input, email);
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void addRoster(String bot,String input){
		facebook c = facebook.getInstance();
		try {
			c.login("FB_ID_SN", password);
			Roster roster = connection.getRoster();
			roster.createEntry(input, null, null);
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.disconnect();
	}
	
	public void disconnect() {
		connection.disconnect();
	}

	public void processMessage(Chat chat, Message message) {
		if (message.getType() == Message.Type.chat)
			System.out.println(chat.getParticipant() + " says: "
					+ message.getBody());
	}

	public static void main(String args[]) throws XMPPException, IOException {
		// declare variables
		facebook c = facebook.getInstance();
		/*BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String msg;*/

		// turn on the enhanced debugger
		XMPPConnection.DEBUG_ENABLED = false;

		// provide your login information here
		c.login("FB_ID_SN", password);

		System.out.println("-----");
		c.displayBuddyList();
		System.out.println("-----");

		/*while (!(msg = br.readLine()).equals("bye")) {
			// your buddy's gmail address goes here
			c.sendMessage(msg, "-517003764@chat.facebook.com");
		}*/

		c.disconnect();
		System.exit(0);
	}

	public void alert(String bot,String email, String msg) {
		facebook c = facebook.getInstance();

		// turn on the enhanced debugger
		XMPPConnection.DEBUG_ENABLED = false;

		// provide your login information here
		try {
			c.login("FB_ID_SN", password);
			c.sendMessage(msg, email);
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.disconnect();
	}
	
	public void alert(String bot, String msg) {
		facebook c = facebook.getInstance();

		// turn on the enhanced debugger
		XMPPConnection.DEBUG_ENABLED = false;

		// provide your login information here
		try {
			c.login("futures" + bot, password);
			fclient.getBuddyList();
			for (String list: sendlist){
				c.sendMessage(msg, list);
			}
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.disconnect();
	}
}