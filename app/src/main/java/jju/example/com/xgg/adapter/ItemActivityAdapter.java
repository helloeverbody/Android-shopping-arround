package jju.example.com.xgg.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import jju.example.com.xgg.R;
import jju.example.com.xgg.pojo.ShopActivity;

/**
 * Created by 112 on 2018/10/10.
 */

public class ItemActivityAdapter extends RecyclerView.Adapter<ItemActivityAdapter.ViewHolder> {

    private Context context;
    private List<ShopActivity> shopActivities;
    private OnItemClickListener mOnItemClickListener;

    public ItemActivityAdapter(Context context, List<ShopActivity> shopActivities) {
        this.context = context;
        this.shopActivities = shopActivities;
    }

    /**
     * 设置监听
     */
    public interface OnItemClickListener{
        void onClick(int position);
        void onLongClick(int position);
    }
    public void setmOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }
    /**
     * 定义行布局中的控件，并获取控件
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private ImageView ivTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            /*获取视图中的控件*/
            tvName = itemView.findViewById(R.id.tv_title_find);
            ivTitle = itemView.findViewById(R.id.iv_title_find);
        }
    }



    /**
     * 创建视图，并绑定ViewHolder
     * @param parent   :
     * @param viewType :
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建行视图
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_activity_list, parent, false);
        return new ItemActivityAdapter.ViewHolder(view);
    }




    /**
     * 配置行布局中控件的数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ItemActivityAdapter.ViewHolder holder, final int position) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.item_fruit2);
        Glide.with(context)
                .load(R.mipmap.find_img1)
                .apply(options)
                .into(holder.ivTitle);

        holder.tvName.setText(shopActivities.get(position).getActi_name());
        if( mOnItemClickListener!= null){
            holder.itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(position);

                }
            });
            holder. itemView.setOnLongClickListener( new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onLongClick(position);
                    //return false;
                    return true;
                }
            });
        }

    }


    @Override
    public int getItemCount() {
        return shopActivities == null ? 0 : shopActivities.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
