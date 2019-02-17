package jju.example.com.xgg.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import jju.example.com.xgg.R;
import jju.example.com.xgg.activity.ShopDetailActivity;
import jju.example.com.xgg.pojo.Product;
import jju.example.com.xgg.util.SPUtil;

/**
 * Created by 112 on 2018/11/2.
 */

public class ItemShopListAdapter extends RecyclerView.Adapter<ItemShopListAdapter.ViewHolder>  {

    private Context context;
    private List<List<Product>> list;
    //private List<Product> productList = new ArrayList <>();
    private OnItemClickListener mOnItemClickListener;


    public ItemShopListAdapter(Context context, List<List<Product>> list) {
        this.context = context;
        this.list = list;
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
                R.layout.item_search_product_list, parent, false);

        return new ViewHolder(view);
    }

    /**
     * 配置行布局中控件的数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final List<Product> productList = new ArrayList <>();

        RequestOptions options = new RequestOptions()
                //.centerCrop()
                .placeholder(R.mipmap.item_fruit2);
        Glide.with(context)
                .load(list.get(position).get(0).getShop_head())
                .apply(options)
                .into(holder.ivShopHead);
        holder.tvShopName.setText(list.get(position).get(0).getShop_name());


        switch (list.get(position).size()){
            default:   //当商品数量大于2时

            case 2:
                productList.clear();
                productList.add(list.get(position).get(0));
                productList.add(list.get(position).get(1));
                break;
            case 1:
                productList.clear();
                productList.add(list.get(position).get(0));
                break;

        }
        if (list.get(position).size()>2){
            int productCount = list.get(position).size()-2;
            holder.llMore.setVisibility(View.VISIBLE);
            holder.tvMoreProducts.setText(Integer.toString(productCount));
        }
        LinearLayoutManager layoutManager =  new LinearLayoutManager(context);

        holder.rvProduct.setLayoutManager(layoutManager);

        ItemProductListAdapter itemProductListAdapter = new ItemProductListAdapter(context,productList);

        holder.rvProduct.setAdapter(itemProductListAdapter);

        if( mOnItemClickListener!= null){                       //点击监听
            holder.ivShopHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(position, v);
                }
            });
            holder.itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(position, v);
                }
            });
            holder.itemView.setOnLongClickListener( new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onLongClick(position);
                    //return false;
                    return true;
                }
            });
        }

        final ItemProductListAdapter finalItemProductListAdapter = itemProductListAdapter;
        holder.llLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                productList.clear();
                productList.add(list.get(position).get(0));
                productList.add(list.get(position).get(1));
                finalItemProductListAdapter.notifyDataSetChanged(); // 通知适配器更新列表
                holder.llMore.setVisibility(View.VISIBLE);
                holder.llLess.setVisibility(View.GONE);
            }
        });
        holder.llMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productList.clear();
                productList.addAll(list.get(position));
                finalItemProductListAdapter.notifyDataSetChanged(); // 通知适配器更新列表
                holder.llLess.setVisibility(View.VISIBLE);
                holder.llMore.setVisibility(View.GONE);

            }
        });

    }
    /**
     * 配置列表行数
     */
    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }



    /**
     * 设置监听
     */
    public interface OnItemClickListener{
        void onClick( int position, View v);
        void onLongClick( int position);
    }

    public void setmOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }
    /**
     * 定义行布局中的控件，并获取控件
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivShopHead;
        private TextView tvShopName;
        private LinearLayout llLess,llMore;
        private RecyclerView rvProduct;
        private TextView tvMoreProducts;


        public ViewHolder(View itemView) {
            super(itemView);
            /*获取视图中的控件*/

            rvProduct = itemView.findViewById(R.id.ispl_rv_product);
            ivShopHead = itemView.findViewById(R.id.ispl_iv_shop_head);
            tvShopName = itemView.findViewById(R.id.ispl_tv_shop_name);
            llLess = itemView.findViewById(R.id.ispl_ll_less);
            llMore = itemView.findViewById(R.id.ispl_ll_more);
            tvMoreProducts = itemView.findViewById(R.id.ispl_tv_more_product_number);


        }
    }
}
