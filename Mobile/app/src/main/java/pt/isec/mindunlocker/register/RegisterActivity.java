package pt.isec.mindunlocker.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

import pt.isec.mindunlocker.MainActivity;
import pt.isec.mindunlocker.R;

public class RegisterActivity extends AppCompatActivity {

    private URL url = null;
    private String response = null;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" + //pelo menos um numero
                    "(?=.*[a-z])" + //pelo menos um caracter minusculo
                    "(?=.*[A-Z])" + //pelo menus um caracter maiusculo
                    "(?=.*[@#$%^&+=])" + //pelo menos um caracter especial
                    "(?=\\S+$)" + //sem espaços
                    ".{6,}" + //pelo menos seis caracteres
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

    private boolean validateUsername(){
        String usernameInput = username.getEditText().getText().toString().trim();

        if(usernameInput.isEmpty()){
            username.setError("Username necessário");
            return false;
        }
        else if(usernameInput.length() > 15){
            username.setError("Username tem mais de 15 caracteres");
            return false;
        }
        else {
            username.setError(null);
            return true;
        }
    }

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
            password.setError("Password Inválida");
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

    private boolean validateEmail(){
        String emailInput = email.getEditText().getText().toString().trim();

        if(emailInput.isEmpty()){
            email.setError("Email necessário");
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            email.setError("Email inválido sugestão (xxx@xxx.com)");
            return false;
        }
        else{
            email.setError(null);
            return true;
        }
    }

    public void register(View v){
        if(!validateUsername() | !validatePassword() | !validateEmail()) return;
        //TODO verificar do servidor/db se existe conta
        if(!onRegister()){
            Toast.makeText(this, "Username ja existe", Toast.LENGTH_SHORT).show();
            return;
        }


        Intent intent = new Intent(this, MainActivity.class);

        intent.putExtra("result", "login");
        intent.putExtra("success", true); // apenas para teste, mudar para verificação do servidor/db
        intent.putExtra("score", "1024"); // mudar para valores reais
        intent.putExtra("ranking", "#1"); // mudar para valores reais
        intent.putExtra("user", username.getEditText().getText().toString());

        startActivity(intent);
    }

    public boolean onRegister() {
        String tEmail = email.getEditText().getText().toString().trim();
        String tUser = username.getEditText().getText().toString().trim();
        String pw = password.getEditText().getText().toString().trim();
        String rePw = repeat_password.getEditText().getText().toString().trim();

        try {
            Registration registration = new Registration(tEmail, pw, rePw, tUser);
            String parameters = new Gson().toJson(registration);

            url = new URL("https://mindunlocker20191126085502.azurewebsites.net/api/Account/Register");

            //create the connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

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
            InputStreamReader isr = new InputStreamReader(connection.getInputStream());

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
            return false;
        }

        return true;
    }
}
