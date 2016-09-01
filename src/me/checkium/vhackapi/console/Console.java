package me.checkium.vhackapi.console;

import me.checkium.vhackapi.Utils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Console {

    protected String password;
    protected String username;

    public Console(String user, String pass) {
        username = user;
        password = pass;
        //return this;
    }

    /**
     * Get IPs from console
     *
     * @param number   How many IPs?
     * @param global   Use global?
     * @param attacked Get already attacked IPs?
     */
    public ArrayList<String> getIPs(int number, boolean attacked, boolean global) throws JSONException {
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<String> temporary = new ArrayList<String>();
        int globali = 0;
        if (global) {
            globali = 1;
        }
        if (number > 10) {
            for (int i = 10; i < number + 10; i += 10) {
                JSONObject json = Utils.JSONRequest("user::::pass::::global", username + "::::" + password + "::::" + Integer.toString(globali), "vh_network.php");
                JSONArray arr = json.getJSONArray("data");
                for (Object obj : arr) {
                    JSONObject ip = ((JSONObject) obj);
                    temporary.add(ip.getString("ip"));
                }
            }
            result.addAll(temporary);
        } else {
            JSONObject json = Utils.JSONRequest("user::::pass::::global", username + "::::" + password + "::::" + Integer.toString(globali), "vh_network.php");
            JSONArray jSONArray = json.getJSONArray("data");
            for (int k = 0; k < number; k++) {
                result.add(jSONArray.getJSONObject(k).getString("ip"));
            }
        }
        return result;
    }

    public List<ScannedNode> scanIPs(List<String> ips) {
        InputStream is;
        BufferedReader rd;
        List<ScannedNode> array = new ArrayList<>();
        ScannedNode result;
        try {
            for (String ip : ips) {
                is = new URL(Utils.generateURL("user::::pass::::target", username + "::::" + password + "::::" + ip, "vh_scan.php")).openStream();
                rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String[] temp = ReadBigStringIn(rd);
                result = new ScannedNode(temp);
                result.setIP(ip);
                array.add(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return array;
    }

    public TransferResult transferTrojanTo(ScannedNode node) throws JSONException {
        JSONObject json = Utils.JSONRequest("user::::pass::::target", username + "::::" + password + "::::" + node.getIP(), "vh_trTransfer.php");
        return new TransferResult(json, node.getIP());
    }

    public List<TransferResult> trTransferIPs(List<ScannedNode> nodes) throws JSONException {
        ArrayList<TransferResult> array = new ArrayList<TransferResult>();
        TransferResult result;
        for (ScannedNode node : nodes) {
            JSONObject json = Utils.JSONRequest("user::::pass::::target", username + "::::" + password + "::::" + node.getIP(), "vh_trTransfer.php");
            result = new TransferResult(json, node.getIP());
            array.add(result);
        }
        return array;
    }

    public String[] ReadBigStringIn(BufferedReader buffIn) throws IOException {
        String[] string = new String[14];
        int i = 0;
        String line;
        while ((line = buffIn.readLine()) != null) {
            string[i] = line;
            i++;
        }
        return string;
    }
}
