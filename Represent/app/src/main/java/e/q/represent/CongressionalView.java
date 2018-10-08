package e.q.represent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CongressionalView extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congressional_view);

        Intent intent = getIntent();

        //Extract the data…
        String zipcode = intent.getStringExtra("zipcode");

        final List<ContactInfo> contactList = new ArrayList();
        final List<String> names = new ArrayList();

        final TextView mTextView = (TextView) findViewById(R.id.testText);
        mTextView.setText(zipcode);

// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://api.geocod.io/v1.3/geocode?postal_code=" + zipcode + "&fields=cd&api_key=ae285432ff53b5b4a8b848bb2af523bfb683558";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        String jsonString = response;
                        mTextView.setText(response);
                        JSONObject jsonResult = null;
                        try {
                            jsonResult = new JSONObject(jsonString);
                            JSONArray data = jsonResult.getJSONArray("results");
                            for(int j = 0 ; j < data.length() ; j++) {
                                JSONObject fields = data.getJSONObject(j).getJSONObject("fields");
                                JSONArray congressional_districts = fields.getJSONArray("congressional_districts");
                                for (int k = 0; k < congressional_districts.length(); k++){
                                    JSONArray current_legislators = congressional_districts.getJSONObject(k).getJSONArray("current_legislators");
                                    for (int i = 0; i < current_legislators.length(); i++) {
                                        JSONObject legislator = current_legislators.getJSONObject(i);

                                        JSONObject bio = legislator.getJSONObject("bio");
                                        if(!names.contains(bio.getString("first_name") + " " + bio.getString("last_name"))){
                                            ContactInfo ci = new ContactInfo();
                                            ci.name = bio.getString("first_name") + " " + bio.getString("last_name");
                                            ci.party = bio.getString("party");

                                            ci.district = congressional_districts.getJSONObject(0).getString("district_number");
                                            ci.contactForm = legislator.getJSONObject("contact").getString("url");
                                            ci.type = legislator.getString("type");
                                            ci.state = data.getJSONObject(0).getJSONObject("address_components").getString("state");
                                            ci.id = legislator.getJSONObject("references").getString("bioguide_id");
                                            names.add(ci.name);
                                            contactList.add(ci);
                                        }

                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mTextView.setText("That didn't work!");
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
        queue.add(stringRequest);


        RecyclerView recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        ContactAdapter ca = new ContactAdapter(contactList);
        recList.setAdapter(ca);
        mTextView.setText(Integer.toString(contactList.size()));
    }


}
