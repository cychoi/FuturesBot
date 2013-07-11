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

import java.net.URL;
import java.net.URLDecoder;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;

/**
 * XmppClient class has all the XMPP specific logic. This class uses smack
 * library to connect to windows live messenger service. For Windows Live Xmpp
 * documentation see <link>
 */
public class msn {

	public static final String Host = "xmpp.messenger.live.com";
	public static final int Port = 5222;
	public static final String Service = "messenger.live.com";

	private String accessToken;
	private XMPPConnection connection;

	/**
	 * This block initializes smack with SASL mechanism used by Windows Live.
	 */
	static {
		SASLAuthentication.registerSASLMechanism("X-MESSENGER-OAUTH2",
				XMessengerOAuth2.class);
		SASLAuthentication.supportSASLMechanism("X-MESSENGER-OAUTH2");
	}

	public static void main(String[] args) throws InterruptedException {
		/**
		 * 1. Set up a string with the path to the website.
		 * 2. Create a desktop variable.  The desktop class uses the computer's default browser to open the URL.
		 * 3. Load the URL.
		 * 4. Browse to the URL.
		 */

		//TODO put your own clientId here.
		//String clientId = "000000004807AE0A";
		//String scopes = "wl.messenger";
		//String signInUrl = "https://oauth.live.com/authorize?client_id=" + clientId + "&redirect_uri=https://oauth.live.com/desktop&response_type=token&scope=" + scopes;

		try {
			// launch a web browser to take the user through the OAuth 2.0 consent flow
			//BareBonesBrowserLaunch.browse(signInUrl);

			// pop a dialog that tells the developer to copy and paste the URL and put it into a text box in the dialog
			//String returnUrlString = (String)JOptionPane.showInputDialog("After completing the OAuth consent flow in the browser, copy and paste the return URL into this dialog box");
			String returnUrlString = "https://oauth.live.com/desktop#access_token=EwAoAq1DBAAUlbRWyAJjK5w968Ru3Cyt%2f6GvwXwAAcQc5qjgPRbE4InoTBM3bsPTd0eAZIcSZiRxdNSMn3K8jSLvqJjDBfhjf3fd71C%2fBlGBFqDAs%2fFZYb1WYDPUfbL239Y3sXKA8ZFZgMOKIWWb%2beib%2fyTpNIW6m4O4SUMhAu5GYe3DAiC7JsXAnLgu%2bUFeUpF%2fEMjmrdRGzVDUjNNuwfKgdqpgvBD5kWAU1A3NNGhwxJZZhwOpKcogIcWZ%2fR7eKdFsYjGN9riWjBh2gcmk%2bGiEMifAxI0PoqeECqq9otHAZ1fz9NxCUbFssIQuy5mxlzjPu%2fWl9wmwRvZnHkDY4EiuDrfUw6SHflWirQ74nQ2fjv7Ew7Dk4nl%2fvvTymxcDZgAACPsOZ%2fNUm4kh%2bABDsVcnU0eMbHeEikN9WrYIwYTrTQKy2WtThAVuuNwsJOiqgyP%2fInBCWZMudPy0qGOlFoJriKm16OiYrgf44t9aua6MO7Lq0hJchmmbBm9%2fEsSquOhKaLOrNgx%2fVPs%2bqycd6sBkqzPJMD69eIztb%2fPnllMbX%2f4qDz1cUU1ySkE%2f3%2bMlENfPDKiNARAvKxSpljsM6yjMgwPCTawChrUgExYjbeGxAR16vY1N7ibTmwcgIB9ewoyghGeNBI7GHBdpOMzBbp%2b6YZQYBDHwPc7SXYoxPq1s8TAlfdXrrpj2J2kEpxuwPGM6VnBJN1yOdFsolRENflmAkTCh4gAA&token_type=bearer&expires_in=3600&scope=wl.messenger";
			// take the string URL from the dialog and programmatically cram it into the access token parameter
			String accessToken = urlTokenizerHelper(returnUrlString);

			// log in using the access token
			msn client = new msn(accessToken);
			client.logIn();
			Roster r = client.getRoster();
			System.out.println(r.toString());
			// make sure the program hasn't already closed before the login has completed
			// in a real XMPP client, this would be replaced with waiting on UI events and Xmpp events 
			Thread.sleep(1000000);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * This function helps to extract the access_token query string parameter from the return URL.
	 */
	public static String urlTokenizerHelper(String urlString) {
		URL returnUrl = null;
		try{
			returnUrl = new URL(urlString);
		}catch(Exception e){}
		String queryParameters = returnUrl.getRef();
		queryParameters = queryParameters.substring(queryParameters.indexOf("access_token"));
		queryParameters = queryParameters.substring(queryParameters.indexOf("=")+1);

		String encodedAccessToken = queryParameters.substring(0, queryParameters.indexOf("&"));

		return URLDecoder.decode(encodedAccessToken);

	}
	/**
	 * Constructor
	 * 
	 * @param accessToken
	 *            The OAuth2.0 access token to be used for login.
	 */
	public msn(String accessToken) {
		this.accessToken = accessToken;
	}

	/**
	 * Get the Roster for this client instance.
	 * 
	 * @return The full Roster for the client.
	 */
	public Roster getRoster() {
		return this.connection.getRoster();
	}

	/**
	 * Get the Jid for this client instance.
	 */
	public String getLocalJid() {
		return StringUtils.parseBareAddress(this.connection.getUser());
	}

	/**
	 * Log in the client to the messenger service.
	 */
	public void logIn() {

		// Create a connection. We use service name in config and asmack will do
		// SRV look up locate the xmpp server.
		ConnectionConfiguration connConfig = new ConnectionConfiguration(
				msn.Service);
		connConfig.setRosterLoadedAtLogin(true);
		this.connection = new XMPPConnection(connConfig);

		try {
			this.connection.connect();

			// We do not need user name in this case.
			this.connection.login("", this.accessToken);
		} catch (XMPPException ex) {
			this.connection = null;
			return;
		}

		System.out.println(String.format("Logged in as %s",
				this.connection.getUser()));
		// set the message and presence handlers
		this.setPacketFilters();

		// Set the status to available
		Presence presence = new Presence(Presence.Type.available);
		this.connection.sendPacket(presence);
	}

	/**
	 * Send a text message to the buddy.
	 * 
	 * @param to
	 *            The Buddy Jid.
	 * @param text
	 *            The text message to be sent.
	 */
	public void sendMessage(String to, String text) {
		Message msg = new Message(to, Message.Type.chat);
		msg.setBody(text);
		this.connection.sendPacket(msg);
	}

	/**
	 * Set the packet filters for handling incoming stanzas.
	 */
	private void setPacketFilters() {
		if (this.connection != null) {
			PacketFilter presenceFilter = new PacketTypeFilter(Presence.class);
			this.connection.addPacketListener(new PacketListener() {
				public void processPacket(Packet packet) {
					Presence presence = (Presence) packet;
					handlePresenceReceived(presence);
				}
			}, presenceFilter);

			PacketFilter messageFilter = new MessageTypeFilter(
					Message.Type.chat);
			this.connection.addPacketListener(new PacketListener() {
				public void processPacket(Packet packet) {
					Message message = (Message) packet;
					if (message.getBody() != null) {
						handleMessageReceived(message);
					}
				}
			}, messageFilter);
		}
	}

	/**
	 * Handle the presence stanza received.
	 * 
	 * @param presence
	 *            The received presence stanza.
	 */
	private void handlePresenceReceived(Presence presence) {
		String from = StringUtils.parseBareAddress(presence.getFrom());
		System.out.println(String.format(
				"Presence received from Jid: %s, Name: %s", from,
				this.getContactName(from)));
	}

	/**
	 * Handle the message stanza received.
	 * 
	 * @param message
	 *            The received message stanza.
	 */
	private void handleMessageReceived(Message message) {
		String from = StringUtils.parseBareAddress(message.getFrom());
		System.out.println(String.format(
				"Message received from Jid: %s, Name: %s", from,
				this.getContactName(from)));
	}

	/**
	 * Get friendly name of a contact given the jid.
	 * 
	 * @param jid
	 *            Jid for the target contact.
	 * 
	 * @return The friendly name by looking up roster.
	 */
	private String getContactName(String jid) {
		Roster roster = this.connection.getRoster();
		RosterEntry entry = roster.getEntry(jid);
		return entry.getName();
	}
}
