package jju.example.com.xgg.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import jju.example.com.xgg.R;
import jju.example.com.xgg.pojo.ShoppingCart;

/**
 * Created by 112 on 2018/10/11.
 */

public class ItemShoppingCartAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<ShoppingCart> shoppingCarts;
    private ItemActivityAdapter.OnItemClickListener mOnItemClickListener;
    private static final int VIEWTYPE_EMPTY = 0;
    private static final int VIEWTYPE_FULL = 1;

    public ItemShoppingCartAdapter(Context context, List<ShoppingCart> shoppingCarts) {
        this.context = context;
        this.shoppingCarts = shoppingCarts;
    }

    /**
     * 设置监听
     */
    public interface OnItemClickListener{
        void onClick(int position);
        void onLongClick(int position);
    }
    public void setmOnItemClickListener(ItemActivityAdapter.OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }
    public class emptyHolder extends RecyclerView.ViewHolder{

        public emptyHolder(View itemView) {
            super(itemView);
        }
    }
    public class fullHolder extends RecyclerView.ViewHolder{

        public fullHolder(View itemView) {
            super(itemView);
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建行视图
        View view;
        switch (viewType){
            case VIEWTYPE_EMPTY:

                view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_shopping_cart_empty_cart, parent, false);
                return new emptyHolder(view);
            case VIEWTYPE_FULL:
                view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_shopping_cart_list, parent, false);
                return new fullHolder(view);
        }
        view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_shopping_cart_empty_cart, parent, false);
        return new emptyHolder(view);

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {



    }

    @Override
    public int getItemCount() {
        return (shoppingCarts == null || shoppingCarts.size()==0) ? 1 : shoppingCarts.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(shoppingCarts==null) return super.getItemViewType(position);
        switch (shoppingCarts.size()){
            case 0:
                return VIEWTYPE_EMPTY;
            default:
                return VIEWTYPE_FULL;

        }


    }
}
