package me.htrewrite.client.util;

import org.apache.commons.io.IOUtils;

import javax.swing.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ClientAuthenticator {
    public static ConfigUtils configUtils = new ConfigUtils("auth", "");

    public static void auth() {
        JOptionPane optionPane = new JOptionPane();
        JFrame jFrame = new JFrame();
        jFrame.setAlwaysOnTop(true);

        Object[] objects = {
                configUtils.get("u"),
                configUtils.get("p")
        };
        boolean yes = false;
        for(Object object : objects)
            if(object == null)
                yes = true;
        if(yes) {
            configUtils.set("u", "INVALID");
            configUtils.set("p", "INVALID");
            configUtils.save();

            JOptionPane.showMessageDialog(jFrame, "Invalid login, please login now!");
            String user = JOptionPane.showInputDialog(jFrame, "User: ");
            String pass = JOptionPane.showInputDialog(jFrame, "Password: ");
            configUtils.set("u", user);
            configUtils.set("p", pass);
            configUtils.save();
        }
        try {
            URL url = new URL("https://aurahardware.eu/api/auth.php");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            String data = "user=" + configUtils.get("u") + "&pass=" + configUtils.get("p");

            byte[] out = data.getBytes(StandardCharsets.UTF_8);
            OutputStream stream = http.getOutputStream();
            stream.write(out);

            InputStream inputStream = http.getInputStream();
            String response = IOUtils.toString(inputStream);
            inputStream.close();
            http.disconnect();

            if(response.contains("111") || response.startsWith("111") || response.endsWith("111")) return; else {
                optionPane.createDialog("Invalid login, please try again!").setAlwaysOnTop(true);
                String user = JOptionPane.showInputDialog(jFrame, "User: ");
                String pass = JOptionPane.showInputDialog(jFrame, "Password: ");
                configUtils.set("u", user);
                configUtils.set("p", pass);
                configUtils.save();
            }

            URL url2 = new URL("https://aurahardware.eu/api/auth.php");
            HttpURLConnection http2 = (HttpURLConnection)url.openConnection();
            http2.setRequestMethod("POST");
            http2.setDoOutput(true);
            http2.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http2.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            String data2 = "user=" + configUtils.get("u") + "&pass=" + configUtils.get("p");

            byte[] out2 = data2.getBytes(StandardCharsets.UTF_8);
            OutputStream stream2 = http2.getOutputStream();
            stream2.write(out2);
            String response2 = http2.getResponseMessage();
            http.disconnect();

            if(response2.contains("111") || response2.startsWith("111") || response2.endsWith("111"))
                return;
        } catch (Exception e) { e.printStackTrace(); }

        System.exit(0);
        return;
    }
}