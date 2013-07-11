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
import java.util.HashMap;
import javax.security.auth.callback.CallbackHandler;
import javax.security.sasl.Sasl;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.sasl.SASLMechanism;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.Base64;
 
public class MySASLDigestMD5Mechanism extends SASLMechanism
{
 
    public MySASLDigestMD5Mechanism(SASLAuthentication saslAuthentication)
    {
        super(saslAuthentication);
    }
 
    protected void authenticate()
        throws IOException, XMPPException
    {
        String mechanisms[] = {
            getName()
        };
        java.util.Map props = new HashMap();
        sc = Sasl.createSaslClient(mechanisms, null, "xmpp", hostname, props, this);
        super.authenticate();
    }
 
    public void authenticate(String username, String host, String password)
        throws IOException, XMPPException
    {
        authenticationId = username;
        this.password = password;
        hostname = host;
        String mechanisms[] = {
            getName()
        };
        java.util.Map props = new HashMap();
        sc = Sasl.createSaslClient(mechanisms, null, "xmpp", host, props, this);
        super.authenticate();
    }
 
    public void authenticate(String username, String host, CallbackHandler cbh)
        throws IOException, XMPPException
    {
        String mechanisms[] = {
            getName()
        };
        java.util.Map props = new HashMap();
        sc = Sasl.createSaslClient(mechanisms, null, "xmpp", host, props, cbh);
        super.authenticate();
    }
 
    protected String getName()
    {
        return "DIGEST-MD5";
    }
 
    public void challengeReceived(String challenge)
        throws IOException
    {
        //StringBuilder stanza = new StringBuilder();
        byte response[];
        if(challenge != null)
            response = sc.evaluateChallenge(Base64.decode(challenge));
        else
            //response = sc.evaluateChallenge(null);
            response = sc.evaluateChallenge(new byte[0]);
        //String authenticationText = "";
        Packet responseStanza;
        //if(response != null)
        //{
            //authenticationText = Base64.encodeBytes(response, 8);
            //if(authenticationText.equals(""))
                //authenticationText = "=";
           
            if (response == null){
                responseStanza = new Response();
            } else {
                responseStanza = new Response(Base64.encodeBytes(response,Base64.DONT_BREAK_LINES));   
            }
        //}
        //stanza.append("<response xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\">");
        //stanza.append(authenticationText);
        //stanza.append("</response>");
        //getSASLAuthentication().send(stanza.toString());
        getSASLAuthentication().send(responseStanza);
    }
}
