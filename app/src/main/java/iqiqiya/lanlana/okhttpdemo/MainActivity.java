package iqiqiya.lanlana.okhttpdemo;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private OkHttpClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mClient = new OkHttpClient();
    }
}
