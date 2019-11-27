package pt.isec.mindunlocker.register;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import pt.isec.mindunlocker.R;
import pt.isec.mindunlocker.exceptions.InvalidEmailFormatException;

public class RegisterActivity extends AppCompatActivity {

    private URL url = null;
    private String response = null;
    private EditText etEmail, etUsername, etPassword, etConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        etEmail = findViewById(R.id.email);
        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);
        etConfirmPassword = findViewById(R.id.re_password);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    public void onRegister(View view) {
        String email = etEmail.getText().toString();
        String user = etUsername.getText().toString();
        String pw = etPassword.getText().toString();
        String rePw = etConfirmPassword.getText().toString();

//        if (!isValidateRegistration(email, user,pw,rePw)) return;

        email = "test@test.pt";
        user = "test";
        pw = "Qwerty123!";
        rePw = "Qwerty123!";

        try {
            Registration registration = new Registration(email, pw, rePw, user);
            String parameters = new Gson().toJson(registration);
            url = new URL("https://mindunlocker20191126085502.azurewebsites.net/api/Account/Register");
            //create the connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type",
                    "application/json");
            //set the request method to POST
            connection.setRequestMethod("POST");
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

            isr.close();
            reader.close();
        } catch (IOException e) {
            Log.e("HTTP POST:", e.toString());
        }
    }

    private boolean isValidateRegistration(String email, String user, String pw, String rePw) {
        if (!email.contains("@") || !email.contains(".")) {
            System.err.println("email format invalid");
            return false;
        } else if (user.length()<4||user.length()>12) {
            System.err.println("invalid user length");
            return false;
        } else if (!pw.equals(rePw)) {
            System.err.println("passwords dont match");
            return false;
        }
        return true;
    }
}
