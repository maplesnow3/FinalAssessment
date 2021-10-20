package comp5216.finalAssessment;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class UserLogin extends AppCompatActivity {

    Button logIn, signUp;
    EditText userName, passWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userlogin);
        userName = findViewById(R.id.editTextuserName);
        passWord = findViewById(R.id.editTextuserPassword);
    }
}