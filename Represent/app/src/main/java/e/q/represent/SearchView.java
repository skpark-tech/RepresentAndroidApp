package e.q.represent;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SearchView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);
        Intent intent = getIntent();

        //Extract the data…
        String source = intent.getStringExtra("source");
        EditText zipCodeInput = findViewById(R.id.zipcodeInput);

        if(source.equals("random")){
            String randomZipCode = intent.getStringExtra("zipcode");
            zipCodeInput.setText(randomZipCode);
        }else if(source.equals("current")){
            String currentZipCode = intent.getStringExtra("zipcode");
            zipCodeInput.setText(currentZipCode);
        }




    }
    public void search(View view) {
        EditText zipcodeInput = findViewById(R.id.zipcodeInput);
        String zipcode = zipcodeInput.getText().toString();
        String regex = "\\d{5}";
        if(!zipcode.matches(regex)){
            AlertDialog alertDialog = new AlertDialog.Builder(SearchView.this).create();
            alertDialog.setTitle("Incorrect ZipCode");
            alertDialog.setMessage("Please Insert a five digit ZipCode.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            return;
        }
        Intent intent = new Intent(this, CongressionalView.class);
        intent.putExtra("zipcode", zipcode);
        startActivity(intent);
    }

}
