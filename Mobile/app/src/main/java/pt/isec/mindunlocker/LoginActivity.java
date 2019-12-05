package pt.isec.mindunlocker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import pt.isec.mindunlocker.MainActivity;
import pt.isec.mindunlocker.R;
import pt.isec.mindunlocker.Token;

public class LoginActivity extends AppCompatActivity {
    private URL url = null;
    private String response = null;
    private TextInputLayout eUsername, ePassword;

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

    /**
     * Validates the inputted username to match the project requirements
     * @return if the username is valid ? true : false
     */
    private boolean validateUsername(){
        String usernameInput = eUsername.getEditText().getText().toString().trim();

        if(usernameInput.isEmpty()){
            eUsername.setError("Username necessário");
            return false;
        }
        else {
            eUsername.setError(null);
            return true;
        }
    }

    /**
     * Validates the inputted password to match the project requirements
     * @return if the password is valid ? true : false
     */
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

    //TODO make javadoc
    public void login(View v){
        if(!validateUsername() | !validatePassword()) return;
        //TODO verificar do servidor/db se existe conta
        if(!onLogin()) {
            Toast.makeText(this, "Username ou password errados", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("result", "login");
        intent.putExtra("success", true);
        intent.putExtra("score", "1024");
        intent.putExtra("ranking", "#1");
        intent.putExtra("user", eUsername.getEditText().getText().toString());

        startActivity(intent);
        finish();
    }

    /**
     * When the Login button is pressed listener
     * @return if the login was made with success ? true : false
     */
    public boolean onLogin() {
        try {
            String username = eUsername.getEditText().getText().toString().trim();
            String password = ePassword.getEditText().getText().toString().trim();

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

            String line;
            //create your inputsream
            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            //read in the data from input stream, this can be done a variety of ways

            BufferedReader reader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }


            JSONObject jsonObject= new JSONObject(sb.toString());
            //get the string version of the response data
            //response = sb.toString();
            //Token.CONTENT = captureTokenFromResponse(response);

            Token.CONTENT= jsonObject.getString("access_token");
            // debug
            // pass the token to the main activity in a intent bundle?

            isr.close();
            reader.close();
        } catch (IOException e) {
            if (e instanceof FileNotFoundException) {
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Capture the token inside of json object that the backend api returns
     * @param response - response to the get request in json object format
     * @return String - token
     */
    private String captureTokenFromResponse(String response) {
        String[] arr = response.split(",");
        arr= arr[0].split(":");
        return arr[1];
    }

}
