package cn.techection.mall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;

import cn.techection.mall.R;

public class HomeActAdapter extends DelegateAdapter.Adapter<HomeActAdapter.ActViewHolder>{
    private Context context;
    private LayoutHelper layoutHelper;

    public HomeActAdapter(Context context, LayoutHelper layoutHelper) {
        this.context = context;
        this.layoutHelper = layoutHelper;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return this.layoutHelper;
    }

    @NonNull
    @Override
    public ActViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ActViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_home_act_list_item,null,false));

    }

    @Override
    public void onBindViewHolder(@NonNull ActViewHolder actViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    protected  static class ActViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        public ActViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.image_act);
        }
    }
}
