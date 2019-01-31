package com.learner.androidnotification;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String BASE_URL = "https://androidnotificationfcm.firebaseapp.com/api/";
    public static final String USER = "com.learner.androidnotification.USER";
    private EditText titleEditText;
    private EditText bodyEditText;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView userEmailTextView = findViewById(R.id.user_email_text_view);
        titleEditText = findViewById(R.id.title_edit_text);
        bodyEditText = findViewById(R.id.body_edit_text);

        Button sendBtn = findViewById(R.id.send_btn);
        sendBtn.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            user = (User) bundle.getSerializable(USER);
            assert user != null;
            userEmailTextView.setText(String.format("Sending to: %s", user.getEmail()));
        }
    }

    @Override
    public void onClick(View v) {
        if (titleEditText.getText().toString().isEmpty()) {
            titleEditText.setError("Title required");
            titleEditText.requestFocus();
            return;
        }
        if (bodyEditText.getText().toString().isEmpty()) {
            bodyEditText.setError("Body required");
            bodyEditText.requestFocus();
            return;
        }
        sendNotification();
    }

    private void sendNotification() {
        String title = titleEditText.getText().toString();
        String body = bodyEditText.getText().toString();
        String token = user.getToken();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        Call<ResponseBody> call = api.sendNotification(token, title, body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    assert response.body() != null;
                    Toast.makeText(NotificationActivity.this, response.body().string(), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(NotificationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
