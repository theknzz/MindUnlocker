package pt.isec.mindunlocker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +
                    "(?=.*[a-z])" +
                    "(?=.*[A-Z])" +
                    "(?=.*[@#$%^&+=])" +
                    "(?=\\S+$)" +
                    ".{6,}" +
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


        Intent intent = new Intent(this, MainActivity.class);

        intent.putExtra("result", "login");
        intent.putExtra("success", true); // apenas para teste, mudar para verificação do servidor/db
        intent.putExtra("score", "1024"); // mudar para valores reais
        intent.putExtra("ranking", "#1"); // mudar para valores reais
        intent.putExtra("user", username.getEditText().getText().toString());

        startActivity(intent);
    }
}
