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
import java.util.concurrent.TimeUnit;

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
    public ArrayList<String> getIPs(int number, boolean attacked, boolean global) {
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<String> temporary = new ArrayList<String>();
        int globali = 0;
        if (global) {
            globali = 1;
        }
        if (number > 10) {
            for (int i = 10; i <= number + 9; i = i + 10) {
                JSONObject json = Utils.JSONRequest("user::::pass::::global", username + "::::" + password + "::::" + Integer.toString(globali), "vh_network.php");
                JSONArray jSONArray = json.getJSONArray("data");
                for (int j = 0; j <= jSONArray.length() - 1; j++) {
                    JSONObject ip = jSONArray.getJSONObject(j);
                    temporary.add(ip.getString("ip"));
                }

            }
            for (int k = 0; k <= number; k++) {
                result.add(temporary.get(k));
            }
        } else {
            JSONObject json = Utils.JSONRequest("user::::pass::::global", username + "::::" + password + "::::" + Integer.toString(globali), "vh_network.php");
            JSONArray jSONArray = json.getJSONArray("data");
            for (int k = 0; k < number; k++) {
                result.add(jSONArray.getJSONObject(k).getString("ip"));
            }
        }
        return result;
    }

    public ScannedNode scanIP(String ip) throws IOException {
        InputStream is = new URL(Utils.generateURL("user::::pass::::target", username + "::::" + password + "::::" + ip, "vh_scan.php")).openStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String[] temp = ReadBigStringIn(rd);
        return new ScannedNode(temp);
    }

    public List<ScannedNode> scanIPs(List<String> ips) {
        InputStream is;
        BufferedReader rd;
        List<ScannedNode> array = new ArrayList<>();
        ScannedNode result;
        for (String ip : ips) {
            try {
                result = scanIP(ip);
                result.setIP(ip);
                array.add(result);
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return array;
    }

    public TransferResult trTransferIP(ScannedNode node) throws JSONException {
        JSONObject json = Utils.JSONRequest("user::::pass::::target", username + "::::" + password + "::::" + node.getIP(), "vh_trTransfer.php");
        return new TransferResult(json, node.getIP());
    }


    public ArrayList<TransferResult> trTransferIPs(ArrayList<String> ips) {
        ArrayList<TransferResult> array = new ArrayList<TransferResult>();
        TransferResult result;
        for (String ip : ips) {
            JSONObject json = Utils.JSONRequest("user::::pass::::target", username + "::::" + password + "::::" + ip, "vh_trTransfer.php");
            result = new TransferResult(json, ip);
            array.add(result);
        }
        return array;
    }

    private String[] ReadBigStringIn(BufferedReader buffIn) throws IOException {
        List<String> string = new ArrayList<>(14);
        String line;
        while ((line = buffIn.readLine()) != null) {
            string.add(line);
        }
        return string.toArray(new String[0]);
    }


}
