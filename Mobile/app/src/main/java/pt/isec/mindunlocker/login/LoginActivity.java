package pt.isec.mindunlocker.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;

import com.google.android.material.textfield.TextInputLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import pt.isec.mindunlocker.MainActivity;
import pt.isec.mindunlocker.R;

public class LoginActivity extends AppCompatActivity {
    private URL url = null;
    private String response = null;
    private TextInputLayout eUsername, ePassword;
    private String token=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        eUsername = findViewById(R.id.username);
        ePassword = findViewById(R.id.password);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    private boolean validateUsername(){
        String usernameInput = eUsername.getEditText().getText().toString().trim();

        if(usernameInput.isEmpty()){
            eUsername.setError("Username necessário");
            return false;
        }
        else if(usernameInput.length() > 15){
            eUsername.setError("Username tem mais de 15 caracteres");
            return false;
        }
        else {
            eUsername.setError(null);
            return true;
        }
    }

    private boolean validatePassword(){
        String passwordInput = ePassword.getEditText().getText().toString().trim();

        if(passwordInput.isEmpty()){
            ePassword.setError("Password necessária");
            return false;
        }
        else {
            ePassword.setError(null);
            return true;
        }
    }

    public void login(View v){
        if(!validateUsername() | !validatePassword()) return;
        //TODO verificar do servidor/db se existe conta
        onLogin();


        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("result", "login");
        intent.putExtra("success", true); // apenas para teste, mudar para verificação do servidor/db
        intent.putExtra("score", "1024"); // mudar para valores reais
        intent.putExtra("ranking", "#1"); // mudar para valores reais
        intent.putExtra("user", eUsername.getEditText().getText().toString());

        startActivity(intent);
        finish();
    }


    public void onLogin() {
        String username = eUsername.getEditText().getText().toString().trim();
        String password = ePassword.getEditText().getText().toString().trim();

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
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

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
                InputStreamReader isr = new InputStreamReader(connection.getInputStream());
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
                // pass the token to the main activity in a intent bundle?

                isr.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String captureTokenFromResponse(String response) {
        String[] arr = response.split(",");
        arr= arr[0].split(":");
        return arr[1];
    }

}
