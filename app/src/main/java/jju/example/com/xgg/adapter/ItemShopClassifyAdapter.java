package jju.example.com.xgg.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cz.msebera.android.httpclient.client.cache.Resource;
import jju.example.com.xgg.R;
import jju.example.com.xgg.pojo.Product;
import jju.example.com.xgg.pojo.ProductClassify;

/**
 * Created by 112 on 2018/11/4.
 */

public class ItemShopClassifyAdapter extends RecyclerView.Adapter<ItemShopClassifyAdapter.ViewHolder> {

    private Context context;
    private List<ProductClassify> classifys;
    private OnItemClickListener mOnItemClickListener;
    private List<Boolean> flagList;

    public List<Boolean> getFlag() {
        return flagList;
    }

    public void setFlag(List<Boolean> flagList) {
        this.flagList = flagList;
    }

    public ItemShopClassifyAdapter(Context context, List<ProductClassify> classifys) {
        this.context = context;
        this.classifys = classifys;
    }
    /**
     * 设置监听
     */
    public interface OnItemClickListener{
        void onClick( int position);
        void onLongClick( int position);
    }

    public void setmOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
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
                R.layout.item_shop_classify, parent, false);

        return new ViewHolder(view);
    }
    /**
     * 配置行布局中控件的数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tvClassify.setText(classifys.get(position).getPc_name());

        if(flagList!=null){
            holder.llBg.setActivated(getFlag().get(position));
            holder.llBg.setActivated(getFlag().get(position));
        }


        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //holder.llBg.setBackgroundResource(R.color.shop_classify_background_selected);

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

    @Override
    public int getItemCount() {
        return classifys == null ? 0 : classifys.size();
    }

    /**
     * 定义行布局中的控件，并获取控件
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llBg;
        private TextView tvClassify;

        public ViewHolder(View itemView) {
            super(itemView);
            /*获取视图中的控件*/
            llBg = itemView.findViewById(R.id.isc_ll_bg);
            tvClassify = itemView.findViewById(R.id.isc_tv_classify);


        }
    }
}
