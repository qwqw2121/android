package cn.techection.mall;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import cn.techection.mall.config.Constant;
import okhttp3.Call;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        findViewById(R.id.btnOK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView img = (ImageView) findViewById(R.id.img);
                final TextView txt = (TextView) findViewById(R.id.showTxt);
                Glide.with(TestActivity.this).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1562210946796&di=92ad1a47343592d62fec7d492df0f14f&imgtype=0&src=http%3A%2F%2Fn1.itc.cn%2Fimg8%2Fwb%2Frecom%2F2016%2F07%2F10%2F146809292742058741.JPEG")
                        .into(img);
                OkHttpUtils.get().url(Constant.API.CATEGORY_PARAM_URL)
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        txt.setText(response);
                    }
                });
            }
        });
    }
}
