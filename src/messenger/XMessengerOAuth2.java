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

import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.sasl.SASLMechanism;
/**
 * Implementation of the X-MESSENGER-OAUTH2 mechanism
 *	This mechanism is used for SASL authentication with Windows Live Messenger
 *	service. The OAuth2.0 access token is passed as is for authentication.
 *	For more information on Windows Live OAuth2.0 see <link>
 * 
 */
public class XMessengerOAuth2 extends SASLMechanism {

	public XMessengerOAuth2(SASLAuthentication saslAuthentication) {
		super(saslAuthentication);
	}

	protected String getName() {
		return "X-MESSENGER-OAUTH2";
	}

	protected void authenticate() throws IOException, XMPPException {
		try {
			// Just return the oauth access token
			String authenticationText = this.password;
			getSASLAuthentication().send(
					new AuthMechanism(getName(), authenticationText));
		} catch (Exception e) {
			throw new XMPPException("SASL authentication failed", e);
		}
	}
}