package me.checkium.vhackapi.console;

import me.checkium.vhackapi.Utils;
import me.checkium.vhackapi.vHackAPI;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

public class AdwareManager {

    protected String password;
    protected String username;

    public AdwareManager(String pass, String user) {
        password = pass;
        username = user;
    }

    public AdwareManager(vHackAPI api) {
        this(api.getPassword(), api.getUsername());
    }

    public boolean uploadAdware(String target) {

        URLConnection in;
        try {
            in = new URL(Utils.generateURL("user::::pass::::target", username + "::::" + password + "::::" + target, "vh_adwareUpload.php")).openConnection();

            BufferedReader br = new BufferedReader(new InputStreamReader((in.getInputStream())));
            String line = br.readLine();
            return line.contains("11");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean removeAdware(String target) {
        @SuppressWarnings("unused")
        URLConnection in;
        try {
            in = new URL(Utils.generateURL("user::::pass::::target", username + "::::" + password + "::::" + target, "vh_removeAdwareRemote.php")).openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;

    }


    public HashMap<String, Integer> getAdware() throws JSONException {
        HashMap<String, Integer> map = new HashMap<>();
        JSONObject json = Utils.JSONRequest("user::::pass", username + "::::" + password, "vh_adwareInfo.php");
        if (json.getInt("remote") <= 0) {
            return null;
        }
        JSONArray array = json.getJSONArray("data");
        for (int i = 0; i <= array.length() - 1; i++) {
            String IP = array.getJSONObject(i).getString("ip");
            Integer earn = array.getJSONObject(i).getInt("earn");
            map.put(IP, earn);
        }

        return map;
    }
}
