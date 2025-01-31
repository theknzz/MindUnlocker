package pt.isec.mindunlocker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

import pt.isec.mindunlocker.api.register.Registration;

public class RegisterActivity extends AppCompatActivity {

    private URL url = null;
    private String response = null;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" + //pelo menos um numero
                    "(?=.*[A-Z])" + //pelo menos um caracter maiusculo
                    "(?=\\S+$)" + //sem espaços
                    ".{8,12}" + //pelo menos seis caracteres
                    "$"); //TODO alterar para o padrao definido
    private TextInputLayout username, password, repeat_password, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        repeat_password = findViewById(R.id.re_password);
        email = findViewById(R.id.email);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    /**
     * Validates the username to correspond to the project requirements
     * @return username is valid ? true : false
     */
    private boolean validateUsername() {
        String usernameInput = username.getEditText().getText().toString().trim();

        if (usernameInput.isEmpty()) {
            username.setError("Invalid username");
            return false;
        } else if (usernameInput.length() > 15) {
            username.setError("Username cant have more than 15 characters");
            return false;
        } else {
            username.setError(null);
            return true;
        }
    }


    /**
     * Validates the password to correspond to the project requirements
     * @return if the password is valid ? true : false
     */
    private boolean validatePassword(){
        String passwordInput = password.getEditText().getText().toString().trim();
        String repeated = repeat_password.getEditText().getText().toString().trim();

        if(passwordInput.isEmpty()){
            password.setError("Password necessária");
            return false;
        }
        else if(repeated.isEmpty()){
            repeat_password.setError("Password necessária");
            return false;
        }
        else if(!PASSWORD_PATTERN.matcher(passwordInput).matches()){
            password.setError("Password fraca, sugestão(Aa1aaaaa)");
            return false;
        }
        else if(!passwordInput.equals(repeated)){
            repeat_password.setError("Password não corresponde");
            return false;
        }
        else{
            password.setError(null);
            return true;
        }
    }

    /**
     * Validates the inputted email to correspond to the project requirements
     * @return if the email is valid ? true : false
     */
    private boolean validateEmail() {
        String emailInput = email.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            email.setError("Email necessário");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            email.setError("Email inválido sugestão (xxx@xxx.com)");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    //TODO javadoc
    public void register(View v){
        if(!validateUsername() | !validatePassword() | !validateEmail()) return;

        if(!onRegister()){
            Toast.makeText(this, "Erro ao registar, tente outra vez", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, LoginActivity.class);

        startActivity(intent);
    }

    /**
     * When the Register button is pressed listener
     * @return if the register has success ? true : false
     */
    public boolean onRegister() {
        try {
            email.setError(null);
            username.setError(null);
            password.setError(null);
            repeat_password.setError(null);

            String tEmail = email.getEditText().getText().toString().trim();
            String tUser = username.getEditText().getText().toString().trim();
            String pw = password.getEditText().getText().toString().trim();
            String rePw = repeat_password.getEditText().getText().toString().trim();

            Registration registration = new Registration(tEmail, pw, rePw, tUser);

            String parameters = new Gson().toJson(registration);
            url = new URL("https://mindunlocker20191126085502.azurewebsites.net/api/Account/Register");

            //create the connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type",
                    "application/json");

            connection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
            connection.setRequestProperty("Accept", "*/*");

            //set the request method to POST
            connection.setRequestMethod("POST");

            //get the output stream from the connection you created
            OutputStreamWriter request = new OutputStreamWriter(connection.getOutputStream());

            //write your data to the ouputstream
            request.write(parameters);
            request.flush();
            request.close();
            String line;

            //create your inputsream
            InputStreamReader isr;
            int status = connection.getResponseCode();

            if (status != HttpURLConnection.HTTP_OK) {
                isr = new InputStreamReader(connection.getErrorStream());
            } else {
                isr = new InputStreamReader(connection.getInputStream());
            }

            //read in the data from input stream, this can be done a variety of ways
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }

            //get the string version of the response data
            response = sb.toString();

            // if the http result code is 400, something went wrong
            if (status == 400) {
                String[] arr = response.split(",");
                response = arr[2].replace("\"", "")
                        .replace("{", "")
                        .replace("}", "")
                        .replace("]", "");
                throw new Exception(response);
            }
            isr.close();
            reader.close();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        return true;
    }
}
