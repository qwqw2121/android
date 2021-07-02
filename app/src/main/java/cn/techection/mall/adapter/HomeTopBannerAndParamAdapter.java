package cn.techection.mall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;

import java.util.List;

import cn.techection.mall.R;
import cn.techection.mall.pojo.Param;

public class HomeTopBannerAndParamAdapter extends DelegateAdapter.Adapter<HomeTopBannerAndParamAdapter.BannerAndParamViewHolder>{
    public static final int TYPE_HEADER=0;
    public static final int TYPE_NORMAL=1;

    private View mHeadView;//头部的Banner
    private List<Param> mData;
    private Context context;
    private LayoutHelper layoutHelper;

    /**
     * 构造函数
     * @param mData
     * @param context
     * @param layoutHelper
     */
    public HomeTopBannerAndParamAdapter(List<Param> mData, Context context, LayoutHelper layoutHelper) {
        this.mData = mData;
        this.context = context;
        this.layoutHelper = layoutHelper;
    }

    public void setHeadeView(View headeView){
        this.mHeadView = headeView;
    }
    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return this.layoutHelper;
    }

    /**
     * 根据位置返回不同的视图类型
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if(this.mHeadView==null)
            return TYPE_NORMAL;
        if(position==0){
            return TYPE_HEADER;
        }
        return TYPE_NORMAL;
    }

    @NonNull
    @Override
    public HomeTopBannerAndParamAdapter.BannerAndParamViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if(mHeadView!=null && viewType ==TYPE_HEADER)
            return new BannerAndParamViewHolder(mHeadView);
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_home_params_list_item,null,false);
        return new BannerAndParamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeTopBannerAndParamAdapter.BannerAndParamViewHolder holder, int position) {
        if(getItemViewType(position)==TYPE_HEADER){
            return;
        }
        final int pos = getRealPosition(holder);
        holder.tv.setText(mData.get(pos).getName());
    }

    /**
     * 读取数据的位置
     * @param holder
     * @return
     */
    private  int getRealPosition(RecyclerView.ViewHolder holder){
        int pos = holder.getLayoutPosition();
        return mHeadView == null?pos:pos-1;
    }

    /**
     * 返回一共有多少数据
     * @return
     */
    @Override
    public int getItemCount() {
        return mHeadView==null?mData.size():mData.size()+1;
    }

    public static  class  BannerAndParamViewHolder extends RecyclerView.ViewHolder{
        public TextView tv;
        public BannerAndParamViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = (TextView)itemView.findViewById(R.id.item_tv);
        }
    }
}
