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

import java.text.DecimalFormat;
import java.util.List;

import jju.example.com.xgg.R;
import jju.example.com.xgg.pojo.Product;

/**
 * Created by 112 on 2018/10/5.
 */

public class ProductListGridAdapter extends RecyclerView.Adapter<ProductListGridAdapter.ViewHolder> {

    private Context context;
    private List<Product> products;
    private OnItemClickListener mOnItemClickListener;

    public ProductListGridAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
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
                R.layout.item_product_list_grid, parent, false);

        return new ViewHolder(view);
    }
    /**
     * 配置行布局中控件的数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        RequestOptions options = new RequestOptions()
                //.centerCrop()
                .placeholder(R.mipmap.item_fruit2);
        Glide.with(context)
                .load(R.mipmap.item_fruit)
                .apply(options)
                .into(holder.listIvProduct);
        DecimalFormat df = new DecimalFormat( "0.00 ");
        holder.tvProdPrice.setText(df.format(products.get(position).getProd_price()));
        holder.tvProdName.setText(products.get(position).getProd_name());

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
    /**
     * 配置列表行数
     */
    @Override
    public int getItemCount() {
        return products == null ? 0 : products.size();
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
     * 定义行布局中的控件，并获取控件
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView listIvProduct;
        private TextView tvProdName,tvProdPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            /*获取视图中的控件*/

            listIvProduct = itemView.findViewById(R.id.iv_title_item_product);
            tvProdName = itemView.findViewById(R.id.tv_product_name);
            tvProdPrice = itemView.findViewById(R.id.tv_product_price);


        }
    }
}
