package jju.example.com.xgg.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
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
import jju.example.com.xgg.activity.LoginActivity;
import jju.example.com.xgg.activity.SearchActivity;
import jju.example.com.xgg.activity.ShopDetailActivity;
import jju.example.com.xgg.pojo.Product;
import jju.example.com.xgg.util.SPUtil;

/**
 * Created by 112 on 2018/11/2.
 */

public class ItemProductListAdapter extends RecyclerView.Adapter<ItemProductListAdapter.ViewHolder> {

    private static final int SEARCH_REQUEST_CODE = 1;
    private Context context;
    private List<Product> products;

    private OnItemClickListener mOnItemClickListener;

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

    public ItemProductListAdapter(Context context, List<Product> products) {
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
                R.layout.item_product_more_list, parent, false);

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
                .load(products.get(position).getProd_head())
                .apply(options)
                .into(holder.ivProduct);

        DecimalFormat df = new DecimalFormat( "0.00 ");
        holder.tvProdPrice.setText(df.format(products.get(position).getProd_price()));
        holder.tvProdMonthConunt.setText(products.get(position).getProd_name()+position);
        holder.tvProdName.setText(products.get(position).getProd_name());

        holder.ivProduct.setOnClickListener(new View.OnClickListener() {        //点击头像监听
            @Override
            public void onClick(View v) {
                SPUtil.saveProduct(context,products.get(position));

                if (SPUtil.isLogin(context)){
                    Intent intent = new Intent(context,ShopDetailActivity.class );                 //跳转到店铺详情
                    intent.putExtra("classifyId",products.get(position).getProd_clas_id());  //传递分类id
                    intent.putExtra("productId",products.get(position).getProd_id());        //传递商品id
                    SearchActivity searchActivity = (SearchActivity) context;
                    searchActivity.startActivityForResult(intent,SEARCH_REQUEST_CODE);
                }else{
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.putExtra("isShow",true);
                    intent.putExtra("classifyId",products.get(position).getProd_clas_id());  //传递分类id
                    intent.putExtra("productId",products.get(position).getProd_id());        //传递商品id
                    SearchActivity searchActivity = (SearchActivity) context;
                    searchActivity.startActivityForResult(intent,SEARCH_REQUEST_CODE);
                }
            }
        });

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
     * 定义行布局中的控件，并获取控件
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivProduct;
        private TextView tvProdName,tvProdMonthConunt,tvProdPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            /*获取视图中的控件*/

            ivProduct = itemView.findViewById(R.id.ipml_iv_prod_head);
            tvProdName = itemView.findViewById(R.id.ipml_tv_prod_name);
            tvProdMonthConunt = itemView.findViewById(R.id.ipml_tv_prod_month_count);
            tvProdPrice = itemView.findViewById(R.id.ipml_tv_prod_price);


        }
    }


}
