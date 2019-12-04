package pt.isec.mindunlocker.login;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import pt.isec.mindunlocker.R;

public class LoginActivity extends AppCompatActivity {
    private URL url = null;
    private String response = null;
    private EditText etUsername, etPassword;
    private TextView tvError;
    private String token=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }


    public void onLogin(View view) {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        if (username.length()!=0 && password.length()!=0) {
            try {
//                debug:
//                username="henrymfdays@gmail.com";
//                password="Qwerty123!";
                String parameters = "grant_type=password&username="+username+"&password="+password;
                url = new URL("https://mindunlocker20191126085502.azurewebsites.net/token");
                //create the connection
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                //set the request method to GET
                connection.setRequestMethod("GET");
                //get the output stream from the connection you created
                OutputStreamWriter request = new OutputStreamWriter(connection.getOutputStream());
                //write your data to the ouputstream
                request.write(parameters);
                request.flush();
                request.close();
                String line = "";
                //create your inputsream
                InputStreamReader isr = new InputStreamReader(
                        connection.getInputStream());
                //read in the data from input stream, this can be done a variety of ways
                BufferedReader reader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                //get the string version of the response data
                response = sb.toString();
                token = captureTokenFromResponse(response);

                // debug
                tvError.setText("Login done properly!");
                tvError.setVisibility(View.VISIBLE);
                tvError.setTextColor(Color.GREEN);
                // pass the token to the main activity in a intent bundle?

                isr.close();
                reader.close();
            } catch (IOException e) {
                if (e instanceof FileNotFoundException) {
                    tvError.setVisibility(View.VISIBLE);
                    etUsername.setText("");
                    etPassword.setText("");
                }
            }
        }
    }

    private String captureTokenFromResponse(String response) {
        String[] arr = response.split(",");
        arr= arr[0].split(":");
        return arr[1];
    }

    public void onEditText(View view) {
        tvError.setVisibility(View.INVISIBLE);
    }
}
