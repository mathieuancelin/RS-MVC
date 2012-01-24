/*
 *  Copyright 2012 Mathieu ANCELIN.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package cx.ath.mancel01.restmvc;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author Mathieu ANCELIN
 */
public class Session {
    
    
    private static final String SESSION_ID = "webfwk-session-id";
    private static final String SESSION = "webfwk-session";
    private static final String REMEMBERME = "webfwk-rememberme";
    private static final String USERNAME = "webfwk-username";

    private static final Pattern sessionParser
            = Pattern.compile("\u0000([^:]*):([^\u0000]*)\u0000");
    static ThreadLocal<Session> current 
            = new ThreadLocal<Session>();
    private String sessionId;
    private Map<String, String> data = new HashMap<String, String>();
    
    private static String privatekey = "AZERTYUIOPQSDFGHJKLMWXCVBN";

    public static Session get() {
        return current.get();
    }
    
    public static void init() {
        try {
            Properties props = new Properties();
            props.load(Session.class.getResourceAsStream("conf.properties"));
            privatekey = props.getProperty("private.key", "AZERTYUIOPQSDFGHJKLMWXCVBN");
        } catch (IOException ex) {
            System.err.println("Error while reading configuration file.");
        }
    }

    public static Session restore() {
        HttpServletRequest req = FrameworkFilter.getRequest();
        HttpServletResponse res = FrameworkFilter.getResponse();
        Session session = new Session();
        try {
            Cookie[] cookies = req.getCookies();
            Cookie cookie = null;
            for (Cookie cook : cookies) {
                if (cook.getName().equals(SESSION)) {
                    cookie = cook;
                }
            }
            if (cookie != null) {
                String value = cookie.getValue();
                String sign = value.substring(0, value.indexOf("-"));
                String data = value.substring(value.indexOf("-") + 1);
                if (sign.equals(sign(data))) {
                    String sessionData = URLDecoder.decode(data, "utf-8");
                    Matcher matcher = sessionParser.matcher(sessionData);
                    while (matcher.find()) {
                        session.put(matcher.group(1), matcher.group(2));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Corrupted HTTP session from " + req.getRemoteAddr(), e);
        }
        boolean found = false;
        for (Cookie cook : req.getCookies()) {
            if (cook.getName().equals(SESSION_ID)) {
                session.sessionId = cook.getValue();
                found = true;
            }
        }
        if (!found) {
            session.sessionId = UUID.randomUUID().toString();
            Cookie cookie = new Cookie(SESSION_ID, session.sessionId);
            res.addCookie(cookie);
        }
        return session;
    }

    public void save() {
        try {
            StringBuilder session = new StringBuilder();
            for (String key : data.keySet()) {
                session.append("\u0000");
                session.append(key);
                session.append(":");
                session.append(data.get(key));
                session.append("\u0000");
            }
            String sessionData = URLEncoder.encode(session.toString(), "utf-8");
            String sign = sign(sessionData);
            Cookie cookie = new Cookie(SESSION, sign + "-" + sessionData);
            FrameworkFilter.getResponse().addCookie(cookie);
        } catch (Exception e) {
            throw new RuntimeException("Session serializationProblem", e);
        }
    }

    public Map<String, String> getData() {
        return data;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String put(String key, String value) {
        return data.put(key, value);
    }

    public void remove(String key) {
        data.remove(key);
    }

    public String get(String key) {
        return data.get(key);
    }

    public void clear() {
        data.clear();
    }

    public boolean contains(String key) {
        return data.containsKey(key);
    }
    
    private static final char[] HEX_CHARS =
        {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String sign(String message) {
        byte[] key = privatekey.getBytes();
        if (key.length == 0) {
            return message;
        }
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA1");
            mac.init(signingKey);
            byte[] messageBytes = message.getBytes("utf-8");
            byte[] result = mac.doFinal(messageBytes);
            int len = result.length;
            char[] hexChars = new char[len * 2];
            for (int charIndex = 0, startIndex = 0; charIndex < hexChars.length;) {
                int bite = result[startIndex++] & 0xff;
                hexChars[charIndex++] = HEX_CHARS[bite >> 4];
                hexChars[charIndex++] = HEX_CHARS[bite & 0xf];
            }
            return new String(hexChars);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
