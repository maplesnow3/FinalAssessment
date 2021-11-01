package comp5216.finalAssessment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserLogin extends AppCompatActivity {

    private Button LogIn, SignUp;
    private EditText UserNameField, PassWordField;
    private String UserName, PassWord, token;
    private Thread newThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userlogin);
        UserNameField = findViewById(R.id.editTextuserName);
        PassWordField = findViewById(R.id.editTextuserPassword);
        LogIn = findViewById(R.id.login);
        SignUp = findViewById(R.id.signup);


        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserName = UserNameField.getText().toString().trim();
                PassWord = PassWordField.getText().toString().trim();
                if (TextUtils.isEmpty(UserName)||TextUtils.isEmpty(PassWord)){

                    Toast.makeText(UserLogin.this, "Username or Password is empty, please retry", Toast.LENGTH_SHORT).show();
                }
                else if(UserName.equals("admin") && PassWord.equals("123")){
                    Toast.makeText(UserLogin.this, "Login as administrator success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UserLogin.this, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    try {
                        LoginFunc();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserName = UserNameField.getText().toString().trim();
                PassWord = PassWordField.getText().toString().trim();
                if (TextUtils.isEmpty(UserName) || TextUtils.isEmpty(PassWord)) {

                    Toast.makeText(UserLogin.this, "Username or Password is empty, please retry", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        signUpFunc();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }
    private void signUpFunc() throws IOException {


        String credentials = "password="+PassWord + "&username=" + UserName;

        newThread= new Thread(new Runnable() {
            @Override
            public void run() {
                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                Request request = new Request.Builder()
                        .url("http://81.68.198.152:7429/api/auth/register")
                        .post(RequestBody.create(mediaType, credentials))
                        .build();
                OkHttpClient okHttpClient = new OkHttpClient();
                try {
                    Response response;
                    response = okHttpClient.newCall(request).execute();
                    Looper.prepare();
                    Handler  mHandler = new Handler(Looper.myLooper()) {
                        public void handleMessage(Message msg) {
                            // process incoming messages here
                        }
                    };
                    if(response.code() == 500){
                        Toast.makeText(UserLogin.this, "Sign up failed, password needs contain uppercase and lowercase letter with number", Toast.LENGTH_LONG).show();
                    }else if(response.code() == 501) {
                        Toast.makeText(UserLogin.this, "Username is used", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(UserLogin.this, "Sign up successful, please log in with your username and password", Toast.LENGTH_LONG).show();
                    }
                    Looper.loop();

                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        });
        newThread.start();


    }

    private void LoginFunc() throws IOException {

        String credential = "username=" + UserName + "&password=" + PassWord;

        newThread = new Thread(new Runnable() {
            @Override
            public void run() {
                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                Request request = new Request.Builder()
                        .url("http://81.68.198.152:7429/api/auth/login")
                        .post(RequestBody.create(mediaType, credential))
                        .build();
                OkHttpClient okHttpClient = new OkHttpClient();
                try {
                    Response response;
                    response = okHttpClient.newCall(request).execute();
                    String jsonString = response.body().string();
                    Looper.prepare();
                    Handler mHandler = new Handler(Looper.myLooper()) {
                        public void handleMessage(Message msg) {
                            // process incoming messages here
                        }
                    };
                    if (response.code() == 500) {
                        Toast.makeText(UserLogin.this, "Log in failed, password needs contain uppercase " +
                                "and lowercase letter with number", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    } else {

                        JSONObject json = new JSONObject(jsonString); //create json object from json respond by the server
                        token = json.getString("data"); //extract token from json, key is "data"
                        Intent intent = new Intent(UserLogin.this, MapPage.class); //create intent
                        intent.putExtra("token", token);
                        startActivity(intent);  //pass the token to mappage
                    }


                } catch (JSONException | IOException e) {

                    e.printStackTrace();
                }
            }
        });
        newThread.start();
    }
}


