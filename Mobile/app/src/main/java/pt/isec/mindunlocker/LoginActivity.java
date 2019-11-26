package pt.isec.mindunlocker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout username, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
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

        if(passwordInput.isEmpty()){
            password.setError("Password necessária");
            return false;
        }
        else {
            password.setError(null);
            return true;
        }
    }

    public void login(View v){
        if(!validateUsername() | !validatePassword()) return;
        //TODO verificar do servidor/db se existe conta


        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("result", "login");
        intent.putExtra("success", true); // apenas para teste, mudar para verificação do servidor/db
        intent.putExtra("score", "1024"); // mudar para valores reais
        intent.putExtra("ranking", "#1"); // mudar para valores reais
        intent.putExtra("user", username.getEditText().getText().toString());

        startActivity(intent);
        finish();
    }
}
