package cn.techection.mall.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.techection.mall.R;
import cn.techection.mall.adapter.CategoryLeftAdapter;
import cn.techection.mall.adapter.CategoryRightAdapter;
import cn.techection.mall.config.Constant;
import cn.techection.mall.listener.OnItemClickListener;
import cn.techection.mall.pojo.PageBean;
import cn.techection.mall.pojo.Param;
import cn.techection.mall.pojo.Product;
import cn.techection.mall.pojo.ResponeCode;
import cn.techection.mall.pojo.SverResponse;
import cn.techection.mall.ui.DetailActivity;
import cn.techection.mall.utils.JSONutils;
import cn.techection.mall.utils.SpaceItemDecoration;
import cn.techection.mall.utils.Utils;
import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {
    private RecyclerView leftRecyclerView ;         //左侧列表组件
    private List<Param> leftCategoryData;           //左侧分类参数

    private CategoryLeftAdapter categoryLeftAdapter; //分类适配器

    private RecyclerView rightRecyclerView ;
    private List<Product> rightProductData ;
    private CategoryRightAdapter categoryRightAdapter;



    private MaterialRefreshLayout refreshLayout;
    private SverResponse<PageBean<Product>> result ;
    private String typeId;


    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_category, container, false);
        initView(view);
        loadParams();
        bindRefreshLinstener();
        return view;
    }

    private void initView(View view){
        //初始化
        leftRecyclerView =(RecyclerView)view.findViewById(R.id.category_rv) ;
        rightRecyclerView = (RecyclerView)view.findViewById(R.id.product_rv);
        refreshLayout = (MaterialRefreshLayout)view.findViewById(R.id.refresh_layout);

        leftCategoryData = new ArrayList<>();
        categoryLeftAdapter = new CategoryLeftAdapter(getActivity(),leftCategoryData);
        rightProductData = new ArrayList<>();
        categoryRightAdapter = new CategoryRightAdapter(getActivity(),rightProductData);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        leftRecyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器
        leftRecyclerView.setAdapter(categoryLeftAdapter);
        categoryLeftAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                typeId = leftCategoryData.get(pos).getId()+"";
                findProductByParam(typeId,1,10,true);
            }
        });

        categoryRightAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                //跳转到详情界面
                String id = rightProductData.get(pos).getId() + "";
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        //网格布局管理器
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        rightRecyclerView.addItemDecoration(new SpaceItemDecoration(Utils.dp2px(getActivity(),10),Utils.dp2px(getActivity(),5)));
        rightRecyclerView.setLayoutManager(gridLayoutManager);
        rightRecyclerView.setAdapter(categoryRightAdapter);

    }

    private void bindRefreshLinstener(){
        refreshLayout.setLoadMore(true);
        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                //下拉刷新
                refreshLayout.finishRefresh();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                //super.onRefreshLoadMore(materialRefreshLayout);
                if(result!=null && result.getStatus()== ResponeCode.SUCESS.getCode()){
                    PageBean pageBean = result.getData();
                    if(pageBean.getPageNum()!=pageBean.getNextPage()){
                        findProductByParam(typeId,pageBean.getNextPage(),pageBean.getPageSize(),false);
                    }
                }else {
                    materialRefreshLayout.finishRefreshLoadMore();
                }
            }
        });
    }
    private void loadParams(){
        //加载产品分类参数
        OkHttpUtils.get()
                .url(Constant.API.CATEGORY_PARAM_URL)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }
                    @Override
                    public void onResponse(String response, int id) {
                        final Type type = new TypeToken<SverResponse<List<Param>>>(){}.getType();
                        SverResponse<List<Param>> result = JSONutils.fromJson(response,type);
                        if(result.getStatus()== ResponeCode.SUCESS.getCode()){
                            if(result.getData()==null)
                                return;
                            leftCategoryData.addAll(result.getData());

                            typeId = leftCategoryData.get(0).getId()+"";
                            leftCategoryData.get(0).setPressed(true);
                            findProductByParam(typeId,1,10,true);

                            categoryLeftAdapter.notifyDataSetChanged();

                        }
                    }
                });
    }

    private void findProductByParam(String productTypeId, int pageNum, int pageSize, final boolean flag){
        OkHttpUtils.post()
                .url(Constant.API.CATEGORY_PRODUCT_URL)
                .addParams("productTypeId",productTypeId)
                .addParams("partsId",0+"")
                .addParams("pageNum",pageNum+"")
                .addParams("pageSize",pageSize+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        final Type type = new TypeToken<SverResponse<PageBean<Product>>>(){}.getType();
                        result = JSONutils.fromJson(response,type);
                        if(result.getStatus()== ResponeCode.SUCESS.getCode()){
                            if(result.getData()!=null){
                                if(flag){
                                    rightProductData.clear();
                                }
                                rightProductData.addAll(result.getData().getData());
                                categoryRightAdapter.notifyDataSetChanged();
                            }
                            if (!flag){
                                refreshLayout.finishRefreshLoadMore();
                            }
                        }
                    }
                });
    }

}
