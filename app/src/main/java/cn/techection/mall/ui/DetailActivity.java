package cn.techection.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;

import cn.techection.mall.R;
import cn.techection.mall.config.Constant;
import cn.techection.mall.pojo.Product;
import cn.techection.mall.pojo.ResponeCode;
import cn.techection.mall.pojo.SverResponse;
import cn.techection.mall.utils.JSONutils;
import okhttp3.Call;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    private Product product;
    private ImageView icon_url;
    private TextView name;
    private TextView parts;
    private TextView price;
    private WebView product_detail;
    private EditText num;
    private TextView stock;
    private Toolbar toolbar;
    private TextView btn_jia;
    private TextView btn_jian;
    private TextView cart_btn;
    private TextView buy_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        icon_url = findViewById(R.id.icon_url);
        name = findViewById(R.id.name);
        parts = findViewById(R.id.parts);
        price = findViewById(R.id.price);
        product_detail = findViewById(R.id.product_detail);
        stock = findViewById(R.id.stock);
        num = findViewById(R.id.num);
        toolbar = findViewById(R.id.toolbar);

        initView();
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        System.out.println("ID:" + id);
        if (!TextUtils.isEmpty(id)) {
            loadProductById(id);
        }

    }

    private void initView() {

        toolbar.setTitle("配件详情");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_jia = findViewById(R.id.btn_jia);
        btn_jia.setOnClickListener((View.OnClickListener) this);

        btn_jian = findViewById(R.id.btn_jian);
        btn_jian.setOnClickListener(this);

        buy_btn = findViewById(R.id.buy_btn);
        buy_btn.setOnClickListener(this);

        cart_btn = findViewById(R.id.cart_btn);
        cart_btn.setOnClickListener(this);
        num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String stock = editable.toString();
                if (!TextUtils.isEmpty(stock)) {
                    int inputNum = Integer.valueOf(stock);
                    {
                        if (inputNum > product.getStock()) {
                            num.setText(product.getStock() + "");
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        int inputNum = Integer.valueOf(num.getText().toString());
        switch (view.getId()) {
            case R.id.cart_btn:
                addProduct2cart();
                break;
            case R.id.btn_jia:
                if (inputNum + 1 <= product.getStock()) {
                    num.setText((inputNum + 1) + "");
                }
                break;
            case R.id.btn_jian:
                if (inputNum - 1 >= 1) {
                    num.setText((inputNum - 1) + "");
                }
                break;
        }
    }

    /**
     * 通过id加载商品信息
     *
     * @param id productId
     */
    private void loadProductById(String id) {
        OkHttpUtils.post().url(Constant.API.PRODUCT_DETAIL_URL).addParams("productId", id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Type type = new TypeToken<SverResponse>() {
                        }.getType();
                        System.out.println(response);
//                        SverResponse<Product> result = JSONUtils.fromJson(response, type);
                        SverResponse<Product> result = JSON.parseObject(response, new TypeReference<SverResponse<Product>>() {
                        });

                        if (result.getStatus() == ResponeCode.SUCESS.getCode()) {
                            if (result.getData() == null)
                                return;

                            product = result.getData();
                            Glide.with(DetailActivity.this).load(Constant.API.BASE_URL + product.getIconUrl())
                                    .into(icon_url);
                            name.setText(product.getName());
                            parts.setText("配件类型" + product.getPartsId());
                            price.setText("$" + product.getPrice());
                            stock.setText("库存" + product.getStock());
                            num.setText("1");
                            product_detail.loadDataWithBaseURL(Constant.API.BASE_URL, product.getDetail(), "text/html",
                                    "utf_8", null);
                        } else {
                            DetailActivity.this.finish();
                        }
                    }
                });
    }

    private void addProduct2cart() {
        System.out.println(product.toString());
        if (product != null) {
            OkHttpUtils.post().url(Constant.API.CART_ADD_URL).addParams("productId", product.getId() + "")
                    .addParams("count", num.getText().toString()).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    Toast.makeText(DetailActivity.this, "加入购物车异常", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onResponse(String response, int id) {
                    SverResponse result = new JSONutils().fromJson(response, SverResponse.class);
                    if (result.getStatus() == ResponeCode.SUCESS.getCode()) {
                        Toast.makeText(DetailActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DetailActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}