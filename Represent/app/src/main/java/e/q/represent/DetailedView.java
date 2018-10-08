package e.q.represent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DetailedView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);

        Intent intent = getIntent();
        String state = intent.getStringExtra("state");
        String district = intent.getStringExtra("district");
        String chamber = intent.getStringExtra("chamber");
        String id = intent.getStringExtra("id");

        char c = id.charAt(0);
        String firstLetter = Character.toString(c);

        String imageUrl= "http://bioguide.congress.gov/bioguide/photo/" + firstLetter + "/" + id + ".jpg";


        WebView myWebView = (WebView) findViewById(R.id.webView);
        myWebView.loadUrl(imageUrl);

        final String[] billsText = {""};
        final String[] rolesText = {""};
        String membersText = "";
        final TextView billsView = (TextView) findViewById(R.id.bills);
        final TextView membersView = (TextView) findViewById(R.id.membership);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://api.propublica.org/congress/v1/members/"+ id +"/bills/introduced.json";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        String jsonString = response;
                        JSONObject jsonResult = null;
                        try {
                            jsonResult = new JSONObject(jsonString);
                            JSONArray data = jsonResult.getJSONArray("results");
                            JSONArray bills = data.getJSONObject(0).getJSONArray("bills");
                            for(int i = 0;  i < 3&& i <bills.length(); i++){
                                billsText[0] += "-";
                                billsText[0] += bills.getJSONObject(i).getString("short_title");
                                billsText[0] += "\n";
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    billsView.setText(billsText[0]);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                billsView.setText("That didn't work!");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("X-API-KEY", "Znwec1af6EG6tS9vKpzOiVCuppyXVfy8wBankiSg");
                return params;
            }
        };
        queue.add(stringRequest);

        url = "https://api.propublica.org/congress/v1/members/" + id + ".json";

        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        String jsonString = response;
                        JSONObject jsonResult = null;
                        try {
                            jsonResult = new JSONObject(jsonString);
                            JSONArray data = jsonResult.getJSONArray("results");
                            JSONArray roles = data.getJSONObject(0).getJSONArray("roles");
                            JSONArray committees = roles.getJSONObject(0).getJSONArray("committees");
                            for(int i = 0;  i < 3 && i <committees.length(); i++){
                                rolesText[0] += "-";
                                rolesText[0] += committees.getJSONObject(i).getString("name");
                                rolesText[0] += "\n";
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        membersView.setText(rolesText[0]);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                billsView.setText("That didn't work!");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("X-API-KEY", "Znwec1af6EG6tS9vKpzOiVCuppyXVfy8wBankiSg");
                return params;
            }
        };

// Add the request to the RequestQueue.
        queue.add(stringRequest2);

    }

}
