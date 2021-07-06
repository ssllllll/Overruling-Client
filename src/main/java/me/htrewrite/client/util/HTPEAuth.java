package me.htrewrite.client.util;

import me.htrewrite.client.HTRewrite;
import me.htrewrite.client.Wrapper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.swing.*;
import java.security.MessageDigest;

public class HTPEAuth {
    public static ConfigUtils configUtils = new ConfigUtils("auth", "");

    private String[] showInvalidGUI(JFrame jFrame) {
        JOptionPane.showMessageDialog(jFrame, "Invalid login, please login now!", "HT+Auth System " + AVERSION + "(client=" + VERSION + ")", JOptionPane.ERROR_MESSAGE);
        String user = JOptionPane.showInputDialog(jFrame, "User: ", "HT+Auth System " + AVERSION + "(client=" + VERSION + ")", JOptionPane.QUESTION_MESSAGE);
        String pass = JOptionPane.showInputDialog(jFrame, "Password: ", "HT+Auth System " + AVERSION + "(client=" + VERSION + ")", JOptionPane.QUESTION_MESSAGE);
        configUtils.set("u", user);
        configUtils.set("p", pass);
        configUtils.save();

        return new String[] {user, pass};
    }
    private String obtainHWID() {
        String h = "HWID!!";

        try {
            String fullHWID = System.getenv("COMPUTERNAME") + System.getProperty("user.name") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_LEVEL");
            StringBuffer buffer = new StringBuffer();

            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(fullHWID.getBytes());

            for (byte md5Byte : md5.digest()) {
                String hex = Integer.toHexString(0xff & md5Byte);
                buffer.append(hex.length() == 1 ? '0' : hex);
            } h+=buffer.toString();
        } catch (Exception exception) { exception.printStackTrace(); }

        return h.equalsIgnoreCase("HWID!!")?"NOHWID":h;
    }
    public boolean auth_ok(String username, String password) {
        String user = StringEscapeUtils.escapeHtml4(username);
        String pass = StringEscapeUtils.escapeHtml4(password);
        String hwid = StringEscapeUtils.escapeHtml4(obtainHWID());

        String response = PostRequest.urlEncodedPostRequest("https://aurahardware.eu/api/user/auth.php", "" +
                "user=" + user + "&" +
                "pass=" + pass + "&" +
                "hwid=" + hwid);
        return response.contains("1");
    }

    public String VERSION = HTRewrite.VERSION;
    public String AVERSION = "a2.2";
    public HTPEAuth() {
        JOptionPane optionPane = new JOptionPane();
        JFrame jFrame = new JFrame();
        jFrame.setAlwaysOnTop(true);

        if(!VERSION.contentEquals(AVERSION))
            JOptionPane.showMessageDialog(jFrame, "It seems your client is outdated!\nThe client will continue running but please download the latest update from the discord!", "HT+Auth System " + AVERSION + "(client=" + VERSION + ")", JOptionPane.ERROR_MESSAGE);

        Object[] objects = {
                configUtils.get("u"),
                configUtils.get("p")
        };
        boolean yes = false;
        for (Object object : objects)
            if (object == null)
                yes = true;
        if (yes) {
            configUtils.set("u", "INVALID");
            configUtils.set("p", "INVALID");
            configUtils.save();

            showInvalidGUI(jFrame);
        }

        try { PostRequest.read(PostRequest.genGetCon("https://aurahardware.eu/ht/api/connectivity/connect.php?user=" + Wrapper.getMC().session.getUsername())); } catch (Exception exception) { }
        if (!auth_ok((String) configUtils.get("u"), (String) configUtils.get("p"))) {
            String[] details = showInvalidGUI(jFrame);
            if (!auth_ok(details[0], details[1])) {
                details = showInvalidGUI(jFrame);
                if (!auth_ok(details[0], details[1])) {
                    FMLCommonHandler.instance().exitJava(-1, true);
                    return;
                }
            }
        }

        JOptionPane.showMessageDialog(jFrame, "Login was OK!", "HT+Auth System " + AVERSION + "(client=" + VERSION + ")", JOptionPane.INFORMATION_MESSAGE);
    }
}