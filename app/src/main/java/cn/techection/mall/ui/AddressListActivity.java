package cn.techection.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.techection.mall.R;
import cn.techection.mall.adapter.AddressAdapter;
import cn.techection.mall.config.Constant;
import cn.techection.mall.listener.OnItemClickListener;
import cn.techection.mall.pojo.Address;
import cn.techection.mall.pojo.ResponeCode;
import cn.techection.mall.pojo.SverResponse;
import cn.techection.mall.utils.JSONutils;
import okhttp3.Call;

public class AddressListActivity extends AppCompatActivity {

    private List<Address> mData;
    private AddressAdapter addressAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        initView();
        loadAddressList();
    }

    private void initView(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.address_rv);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("收货地址列表");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mData = new ArrayList<>();
        addressAdapter = new AddressAdapter(this,mData);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(addressAdapter);

        addressAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                Address address = mData.get(pos);
                Intent intent = new Intent();
                intent.putExtra("address",address);
                setResult(RESULT_OK,intent);

                //销毁自己
                finish();

            }
        });
        addressAdapter.setOnAddrOptListener(new AddressAdapter.OnAddrOptListener() {
            @Override
            public void deleteItem(View v, int pos) {
                String id = mData.get(pos).getId()+"";
                deleteAddress(id);

            }
        });
    }

    /**
     * 加载地址列表
     */
    private void loadAddressList(){
        OkHttpUtils.get()
                .url(Constant.API.USER_ADDR_LIST_URL)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Type type = new TypeToken<SverResponse<List<Address>>>(){}.getType();
                        SverResponse<List<Address>> result = JSONutils.fromJson(response,type);
                        if(result.getStatus()== ResponeCode.SUCESS.getCode()){
                            mData.clear();
                            mData.addAll(result.getData());
                            addressAdapter.notifyDataSetChanged();
                        }

                    }
                });
    }

    /**
     * 根据编号删除地址
     * @param id
     */
    private void deleteAddress(String id){
        OkHttpUtils.get()
                .url(Constant.API.USER_ADDR_DEL_URL)
                .addParams("id",id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        SverResponse result = JSONutils.fromJson(response, SverResponse.class);
                        if(result.getStatus()== ResponeCode.SUCESS.getCode()){
                            //重新加载数据
                            loadAddressList();
                        }else{
                            Toast.makeText(AddressListActivity.this,result.getMsg(), Toast.LENGTH_LONG).show();
                        }

                    }
                });

    }

}
