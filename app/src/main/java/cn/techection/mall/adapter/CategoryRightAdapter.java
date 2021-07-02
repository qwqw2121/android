package cn.techection.mall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.techection.mall.R;
import cn.techection.mall.config.Constant;
import cn.techection.mall.listener.OnItemClickListener;
import cn.techection.mall.pojo.Product;

/**
 * Created by Gjl on 2019/4/2.
 */

public class CategoryRightAdapter
        extends RecyclerView.Adapter<CategoryRightAdapter.ProductViewHolder>
        implements View.OnClickListener{
    private Context context;
    private List<Product> mData;
    private OnItemClickListener onItemClickListener;

    public CategoryRightAdapter(Context context, List<Product> mData) {
        this.context = context;
        this.mData = mData;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_category_right_list_item,null,false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = mData.get(position);
        holder.name.setText(product.getName());
        holder.price.setText(product.getPrice()+"");
        Glide.with(context).load(Constant.API.BASE_URL+product.getIconUrl()).into(holder.icon_url);
        //
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(onItemClickListener!=null){
            onItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder{
        public View itemView;
        public TextView name;
        public TextView price;
        public ImageView icon_url;
        public ProductViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            name = (TextView)itemView.findViewById(R.id.name);
            price =(TextView)itemView.findViewById(R.id.price);
            icon_url=(ImageView)itemView.findViewById(R.id.icon_url);
        }
    }

}
