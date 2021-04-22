package com.example.twiliosms;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import okhttp3.*;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private EditText mTo;
    private EditText mBody;
    private EditText mName;
    private EditText mBir;
    private Button mSend;
    private Button mSend2;
    private OkHttpClient mClient = new OkHttpClient();
    private Context mContext;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private int rNum = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTo = (EditText) findViewById(R.id.txtNumber);
        mBody = (EditText) findViewById(R.id.txtMessage);
        mName = (EditText) findViewById(R.id.txtName);
        mBir = (EditText) findViewById(R.id.txtBir);
        mSend = (Button) findViewById(R.id.btnSend);
        mSend2 = (Button) findViewById(R.id.btnSend2);
        mContext = getApplicationContext();

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    rNum = randomRange(100000, 999999);
                    System.out.println(rNum);
                    post("http://b781b6ce185a.ngrok.io/sms", new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mTo.setText("");
                                    //mBody.setText("");
                                    Toast.makeText(getApplicationContext(), "SMS sent", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });

        mSend2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mBody.getText().toString().equals(Integer.toString(rNum))){
                    Toast.makeText(getApplicationContext(), "인증되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this,CreateQR.class);
                    intent.putExtra("name", mName.getText().toString());
                    intent.putExtra("bir", mBir.getText().toString());
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "인증실패하셨습니다.", Toast.LENGTH_SHORT).show();
                    System.out.println(rNum);
                }
            }
        });
    }

    Call post(String url, Callback callback) throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("To", mTo.getText().toString())
                .add("Body", Integer.toString(rNum))
                .build();
                //.add("Body", mBody.getText().toString())

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Call response = mClient.newCall(request);
        response.enqueue(callback);
        System.out.println(response);
        return  response;
    }
    public static int randomRange(int n1, int n2){
        return (int) (Math.random() * (n2-n1 +1));
    }
}