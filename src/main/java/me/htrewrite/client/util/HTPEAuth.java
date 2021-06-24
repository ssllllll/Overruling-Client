package me.htrewrite.client.util;

import me.htrewrite.client.Wrapper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.swing.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class HTPEAuth {
    public static ConfigUtils configUtils = new ConfigUtils("auth", "");

    public HTPEAuth() {
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

        String h = "NOHWID";

        try {
            String fullHWID = System.getenv("COMPUTERNAME") + System.getProperty("user.name") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_LEVEL");
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(fullHWID.getBytes());

            StringBuffer buffer = new StringBuffer();

            byte[] md5Bytes = md5.digest();
            for (byte md5Byte : md5Bytes) {
                String hex = Integer.toHexString(0xff & md5Byte);
                buffer.append(hex.length() == 1 ? '0' : hex);
            }
            h = "HWID!!" + buffer.toString();
        } catch (Exception exception) {}

        try {
            PostRequest.read(PostRequest.genGetCon("https://aurahardware.eu/ht/api/connectivity/connect.php?user=" + Wrapper.getMC().session.getUsername()));

            URL url = new URL("https://aurahardware.eu/api/user/auth.php");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            String data = "user=" + StringEscapeUtils.escapeHtml4((String)configUtils.get("u")) + "&pass=" + StringEscapeUtils.escapeHtml4((String)configUtils.get("p")) + "&hwid=" + StringEscapeUtils.escapeHtml4(h);

            byte[] out = data.getBytes(StandardCharsets.UTF_8);
            OutputStream stream = http.getOutputStream();
            stream.write(out);

            InputStream inputStream = http.getInputStream();
            String response = IOUtils.toString(inputStream);
            inputStream.close();
            http.disconnect();

            if(response.contains("1")) return; else {
                optionPane.createDialog("Invalid login, please try again!").setAlwaysOnTop(true);
                String user = JOptionPane.showInputDialog(jFrame, "User: ");
                String pass = JOptionPane.showInputDialog(jFrame, "Password: ");
                configUtils.set("u", user);
                configUtils.set("p", pass);
                configUtils.save();
            }

            URL url2 = new URL("https://aurahardware.eu/api/user/auth.php");
            HttpURLConnection http2 = (HttpURLConnection)url.openConnection();
            http2.setRequestMethod("POST");
            http2.setDoOutput(true);
            http2.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http2.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            String data2 = "user=" + StringEscapeUtils.escapeHtml4((String)configUtils.get("u")) + "&pass=" + StringEscapeUtils.escapeHtml4((String)configUtils.get("p")) + "&hwid=" + StringEscapeUtils.escapeHtml4(h);

            byte[] out2 = data2.getBytes(StandardCharsets.UTF_8);
            OutputStream stream2 = http2.getOutputStream();
            stream2.write(out2);
            String response2 = http2.getResponseMessage();
            http.disconnect();

            if(response2.contains("1"))
                return;
        } catch (Exception e) { e.printStackTrace(); }

        FMLCommonHandler.instance().exitJava(-1, true);
        return;
    }
}