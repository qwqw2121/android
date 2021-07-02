package cn.techection.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.techection.mall.R;
import cn.techection.mall.adapter.ConfirmOrderProductAdapter;
import cn.techection.mall.config.Constant;
import cn.techection.mall.pojo.Address;
import cn.techection.mall.pojo.Cart;
import cn.techection.mall.pojo.CartItem;
import cn.techection.mall.pojo.Order;
import cn.techection.mall.pojo.ResponeCode;
import cn.techection.mall.pojo.SverResponse;
import cn.techection.mall.utils.JSONutils;
import okhttp3.Call;
public class ConfirmOrderActivity extends AppCompatActivity {

    private TextView name;
    private TextView mobile;
    private TextView addr_detail;

    private BigDecimal totals;
    private RecyclerView recyclerView;
    private ConfirmOrderProductAdapter confirmOrderProductAdapter;
    private List<CartItem> cartItems;
    private TextView total;

    private Address defaultAddr;
    private Toolbar toolbar;

    private static final int REQ_ADDR_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        initView();
        //初始化默认地址
        initDefaultAddr();
        //初始化购物车信息
        initCartProducts();

    }

    private void initView() {
        name = (TextView) findViewById(R.id.name);
        mobile = (TextView) findViewById(R.id.mobile);
        addr_detail = (TextView) findViewById(R.id.addr_detail);
        recyclerView = (RecyclerView) findViewById(R.id.cart_rv);
        total = (TextView) findViewById(R.id.total);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("确认订单信息");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cartItems = new ArrayList<>();
        confirmOrderProductAdapter = new ConfirmOrderProductAdapter(this, cartItems);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(confirmOrderProductAdapter);

        //提交订单
        findViewById(R.id.buy_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitOrder();
                Intent intent = new Intent();
                intent.setClass(ConfirmOrderActivity.this, PaySuccessActivity.class);
                startActivity(intent);
            }
        });
        //选择地址
        findViewById(R.id.address_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConfirmOrderActivity.this, AddressListActivity.class);
                startActivityForResult(intent,REQ_ADDR_CODE);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQ_ADDR_CODE) {
            if (resultCode == RESULT_OK) {
                defaultAddr = (Address) data.getSerializableExtra("address");
                displayInfo();
            }
        }

    }

    /**
     * 显示地址信息
     */
    private void displayInfo() {
        name.setText(defaultAddr.getName());
        mobile.setText(defaultAddr.getMobile());
        addr_detail.setText(
                defaultAddr.getProvince() + ""
                        + defaultAddr.getCity() + ""
                        + defaultAddr.getDistrict() + ""
                        + defaultAddr.getAddr()
        );
    }

    /**
     * 加载默认地址
     */
    private void initDefaultAddr() {
        OkHttpUtils.get()
                .url(Constant.API.USER_ADDR_DEFAULT_URL)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
//                        SverResponse<List<Address>> result = JSON.parseObject(response,new TypeReference<SverResponse<List<Address>>>(){},null);
                        Type type = new TypeToken<SverResponse<List<Address>>>(){}.getType();
                        SverResponse<List<Address>> result = JSONutils.fromJson(response,type);
                        if (result.getStatus() == ResponeCode.SUCESS.getCode()) {

                            if (result.getData() != null) {
                                //查找默认
                                for (Address adr : result.getData()) {
                                    if (adr.getDefaultAddr()==1) {
                                        defaultAddr = adr;
                                        System.out.println("GET");
                                        System.out.println(adr);
                                        defaultAddr = result.getData().get(0);
                                        break;

                                    }
                                    else
                                    {
                                        System.out.println("NULL");
                                        System.out.println(adr);
                                    }
                                }

                                if (defaultAddr == null) {

                                    defaultAddr = result.getData().get(0);
                                }
                                displayInfo();
                            } else {
                                name.setText("");
                                addr_detail.setText("");
                                mobile.setText("请选择收件地址");
                            }
                        } else {
                            name.setText("");
                            addr_detail.setText("");
                            mobile.setText("请选择收件地址");
                        }

                    }
                });
    }

    /**
     * 加载购物车数据
     */
    private void initCartProducts() {
        OkHttpUtils.get()
                .url(Constant.API.CART_LIST_URL)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Type type = new TypeToken<SverResponse<Cart>>() {
                        }.getType();
                        SverResponse<Cart> result = JSONutils.fromJson(response, type);
                        if (result.getStatus() == ResponeCode.SUCESS.getCode()) {
                            if (result.getData().getLists() != null) {
                                cartItems.clear();
                                ;
                                cartItems.addAll(result.getData().getLists());
                                confirmOrderProductAdapter.notifyDataSetChanged();
                            }
                        }
                        totals = BigDecimal.valueOf(0);
                        for(int i=0;i<cartItems.size();i++){


                            BigDecimal m = cartItems.get(i).getPrice().multiply(BigDecimal.valueOf(cartItems.get(i).getQuantity()));

                            totals = totals.add(m);
                        }
                        total.setText("合计：￥" + totals);
                    }
                });
    }

    private void submitOrder() {

        if (defaultAddr == null) {
            Toast.makeText(this, "请选择收货地址!", Toast.LENGTH_LONG).show();
            return;
        }



        OkHttpUtils.post()
                .url(Constant.API.ORDER_CREATED_URL)
                .addParams("addrId", defaultAddr.getId() + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Type type = new TypeToken<SverResponse<Order>>() {
                        }.getType();
                        SverResponse<Order> result = JSONutils.fromJson(response, type);
                        if (result.getStatus() == ResponeCode.SUCESS.getCode()) {
                            // 跳转到订单详情

                        } else {
                            Toast.makeText(ConfirmOrderActivity.this, result.getStatus(), Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }
}
