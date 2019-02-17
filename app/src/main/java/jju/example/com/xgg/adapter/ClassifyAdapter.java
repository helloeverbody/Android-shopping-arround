package jju.example.com.xgg.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import jju.example.com.xgg.R;

/**
 * Created by 112 on 2018/9/24.
 */

public class ClassifyAdapter extends BaseAdapter {
    // 分类导航区的图标ID
    private int[] imgs= new int[]{
            R.mipmap.item_classify_fruit,
            R.mipmap.item_classify_vegetable,
            R.mipmap.item_classify_juice,
            R.mipmap.item_classify_wine
    };
    //分类导航区圆角效果
    private int[] radius= new int[]{
            R.drawable.shape_corner_topleft,
            R.drawable.shape_corner_topright,
            R.drawable.shape_corner_bottomleft,
            R.drawable.shape_corner_bottomright
    };
    // 分类导航区文字
    private String[] titles = new String[]{
            "新鲜水果\n果香四溢",
            "有机蔬菜\n健康美味",
            "多种饮料\n任你挑选",
            "精品好酒\n品味臻品"
    };
    private OnItemClickListener mOnItemClickListener ;
    public ClassifyAdapter(){
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
    @Override
    public int getCount() {
        return imgs.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.gv_item_classify,parent,false);
        LinearLayout linearLayout = convertView.findViewById(R.id.ll_gv_home);
        ImageView iv = convertView.findViewById(R.id.iv_gv_item);
        TextView tv = convertView.findViewById(R.id.tv_gv_item);
        linearLayout.setBackgroundResource(radius[position]);
        iv.setImageResource(imgs[position]);
        tv.setText(titles[position]);

        if( mOnItemClickListener!= null){
            convertView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(position);

                }
            });
            convertView. setOnLongClickListener( new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onLongClick(position);
                    //return false;
                    return true;
                }
            });
        }

        return convertView;
    }
}
