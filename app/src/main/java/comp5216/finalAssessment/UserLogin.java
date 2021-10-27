package comp5216.finalAssessment;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Method;

public class UserLogin extends AppCompatActivity {

    private Button LogIn, SignUp;
    private EditText UserNameField, PassWordField;
    private String UserName, PassWord, token;
    private TextView tv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userlogin);
        UserNameField = findViewById(R.id.editTextuserName);
        PassWordField = findViewById(R.id.editTextuserPassword);
        LogIn = findViewById(R.id.login);
        SignUp = findViewById(R.id.signup);
        tv = findViewById(R.id.textView);

        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserName = UserNameField.getText().toString().trim();
                PassWord = PassWordField.getText().toString().trim();
                if (TextUtils.isEmpty(UserName)||TextUtils.isEmpty(PassWord)){

                    Toast.makeText(UserLogin.this, "Username or Password is empty, please retry", Toast.LENGTH_SHORT).show();
                }
                else if(UserName.equals("admin") && PassWord.equals("123")){
                    Toast.makeText(UserLogin.this, "Login as adminstraor success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UserLogin.this, MapPage.class);
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

            StringBuilder sb = new StringBuilder(UserName + " " + PassWord);

            OkHttpClient okHttpClient = new OkHttpClient();

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, String.valueOf(sb));
            Request request = new Request.Builder()
                    .url("http://81.68.198.152:7429/api/auth/register")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();
            Response response = client.newCall(request).execute();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "onFailure: " + e.getMessage());
                    Toast.makeText(UserLogin.this, "Failed", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d(TAG, response.protocol() + " " +response.code() + " " + response.message());
                    Headers headers = response.headers();
                    for (int i = 0; i < headers.size(); i++) {
                        Log.d(TAG, headers.name(i) + ":" + headers.value(i));
                    }
                    Log.d(TAG, "onResponse: " + response.body().string());
                    Toast.makeText(UserLogin.this, "Success", Toast.LENGTH_SHORT).show();
                }
            });

        }
    private void LoginFunc() throws IOException {

        try {
            Method forName = Class.class.getDeclaredMethod("forName", String.class);
            Method getDeclaredMethod = Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);
            Class<?> vmRuntimeClass = (Class<?>) forName.invoke(null, "dalvik.system.VMRuntime");
            Method getRuntime = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "getRuntime", null);
            Method setHiddenApiExemptions = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "setHiddenApiExemptions", new Class[]{String[].class});
            Object sVmRuntime = getRuntime.invoke(null);
            setHiddenApiExemptions.invoke(sVmRuntime, new Object[]{new String[]{"L"}});
        } catch (Throwable e) {
            Log.e("[error]", "reflect bootstrap failed:", e);
        }

        StringBuilder sb1 = new StringBuilder(UserName + " " + PassWord);

        OkHttpClient okHttpClient = new OkHttpClient();

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, String.valueOf(sb1));
        Request request = new Request.Builder()
                .url("http://81.68.198.152:7429/api/auth/register")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Toast.makeText(UserLogin.this, "Failed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        String jsonString = response.body().string();
                        try {
                            JSONObject json = new JSONObject(jsonString);
                            token = json.getString("data");
                            tv.setText(token);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //Intent intent = new Intent(UserLogin.this, MapPage.class);
                       // startActivity(intent);
                    }
                });
            }
        });


    }
}
