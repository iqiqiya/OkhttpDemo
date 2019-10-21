package iqiqiya.lanlana.okhttpdemo;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.http2.Header;

import android.content.ReceiverCallNotAllowedException;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.HeaderViewListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private final OkHttpClient mClient = new OkHttpClient();
    private TextView mTextView;
    private String POST_URL = "https://api.github.com/markdown/raw";

    private MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.tv_text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuGet:
                get();
                break;
            case R.id.menuResponse:
                response();
                break;
            case R.id.menu_clear:
                clear();
                break;
            case R.id.menuPost:
                post();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void post(){
        Request.Builder builder = new Request.Builder();
        builder.url(POST_URL);
        builder.post(RequestBody.create(MEDIA_TYPE_MARKDOWN,"Hello world github/linguist#1 **cool**, and #1!"));
        Request request = builder.build();
        Call call = mClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()){
                    final String content = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTextView.setText(content);
                        }
                    });
                }
            }
        });
    }

    private void clear(){
        mTextView.setText("");
        Toast.makeText(MainActivity.this,"已经清空",Toast.LENGTH_SHORT).show();
    }

    private void response(){
        Request.Builder builder = new Request.Builder();
        builder.url("https://github.com/square/okhttp/blob/master/README.md");
        Request request = builder.build();
        Call call = mClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d(TAG, "onFailure: call = [" + call + "], e = " + e + "]");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.d(TAG, "onResponse: call = [" + call + "], reponse = " + response + "]");

                int code = response.code();
                Headers headers = response.headers();
                String content = response.body().string();
                final StringBuilder buf = new StringBuilder();
                buf.append("code："+code);
                buf.append("\nHeaders：" + headers);
                buf.append("\nbody："+ content);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTextView.setText(buf.toString());
                    }
                });
            }
        });
    }

    private void get(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                Request.Builder builder = new Request.Builder();
                builder.url("https://github.com/square/okhttp/blob/master/README.md");
                Request request = builder.build();

                Call call = mClient.newCall(request);
                try {
                    Response response = call.execute();
                    if (response.isSuccessful()){
                        final String string = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTextView.setText(string);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
