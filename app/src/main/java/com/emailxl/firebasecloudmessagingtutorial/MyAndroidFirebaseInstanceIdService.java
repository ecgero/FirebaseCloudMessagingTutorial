package com.emailxl.firebasecloudmessagingtutorial;

import android.provider.Settings;
import android.util.Log;

import com.emailxl.firebasecloudmessagingtutorial.utils.AppConstants;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.emailxl.firebasecloudmessagingtutorial.utils.Utils.readStream;

public class MyAndroidFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = "MyAndroidFCMIIDService";
    private static final boolean LOG = false;

    @Override
    public void onTokenRefresh() {
        //Get hold of the registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Log the token
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        //Send token to external server
        sendRegistrationTokenToServer(refreshedToken);
    }

    private int sendRegistrationTokenToServer(String token) {
        //Implement this method if you want to store the token on your server
        String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        InputStream is = null;
        int output = 0;

        try {
            URL url = new URL(AppConstants.SERVER_DIR);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String input = loadJson(token, android_id);

            OutputStream os = conn.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");

            osw.write(input);
            osw.flush();
            osw.close();

            if (conn.getResponseCode() == 200) {

                is = conn.getInputStream();
                output = Integer.parseInt(readStream(is));
            }

        } catch (Exception e) {
            if (LOG) Log.e(TAG, e.getMessage());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    if (LOG) Log.e(TAG, e.getMessage());
                }
            }
        }

        return output;

    }

    private static String loadJson(String token, String android_id) throws Exception {

        JSONObject json = new JSONObject();

        json.put("fcm_regid", token);
        json.put("android_id", android_id);

        return json.toString();
    }
}
