package jju.example.com.xgg.activity;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.longsh.optionframelibrary.OptionBottomDialog;
import com.longsh.optionframelibrary.OptionCenterDialog;
import com.longsh.optionframelibrary.OptionMaterialDialog;

import java.util.ArrayList;
import java.util.List;

import jju.example.com.xgg.R;

public class OptionFrameTestActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_frame_test);
    }


    public void QQDialog(View view) {
        List<String> stringList = new ArrayList<String>();
        stringList.add("拍照");
        stringList.add("从相册选择");
        final OptionBottomDialog optionBottomDialog = new OptionBottomDialog(this, stringList);
        optionBottomDialog.setItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                optionBottomDialog.dismiss();
            }
        });
    }

    public void WXCenterDialog(View view) {
        final ArrayList<String> list = new ArrayList<>();
        list.add("标为已读");
        list.add("置顶聊天");
        list.add("删除该聊天");
        final OptionCenterDialog optionCenterDialog = new OptionCenterDialog();
        optionCenterDialog.show(this, list);
        optionCenterDialog.setItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                optionCenterDialog.dismiss();
            }
        });
    }


    public void WXDialog(View view) {
        final OptionMaterialDialog mMaterialDialog = new OptionMaterialDialog(this);
        mMaterialDialog
//                .setTitle("这是标题")
//                .setTitleTextColor(R.color.colorPrimary)
//                .setTitleTextSize((float) 22.5)
                .setMessage("支持所有布局文字大小颜色等设置。")
//                .setMessageTextColor(R.color.colorPrimary)
//                .setMessageTextSize((float) 16.5)
                .setPositiveButtonTextColor(R.color.colorAccent)
//                .setNegativeButtonTextColor(R.color.colorPrimary)
//                .setPositiveButtonTextSize(15)
//                .setNegativeButtonTextSize(15)
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                })
                .setNegativeButton("取消",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMaterialDialog.dismiss();
                            }
                        })
                .setCanceledOnTouchOutside(true)
                .setOnDismissListener(
                        new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
//                                Toast.makeText(this, "onDismiss 取消", Toast.LENGTH_SHORT).show();
                            }
                        })
                .show();
    }
}