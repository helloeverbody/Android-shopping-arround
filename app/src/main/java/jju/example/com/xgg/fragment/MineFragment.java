package jju.example.com.xgg.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunfusheng.glideimageview.GlideImageView;

import jju.example.com.xgg.R;
import jju.example.com.xgg.activity.LoginActivity;
import jju.example.com.xgg.activity.PersonInfoActivity;
import jju.example.com.xgg.config.Constant;
import jju.example.com.xgg.pojo.Customer;
import jju.example.com.xgg.util.MessageEvent;
import jju.example.com.xgg.util.SPUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {

    private static final int REQUSET_LOGIN = 1;           // 跳到登录页面请求码
    private static final int REQUSET_PERSON_INFO = 2;     // 个人信息请求码
    private static final int RESULT_SUCCESS = 0;     // 成功的结果码
    private static final int RESULT_FAIL = -1;       // 失败的结果码
    private static final String TAG = "我的页面" ;

    private GlideImageView givHead;         //头像
    private TextView tvToLogin;             //立即登录
    private TextView tvCustName;            //用户名
    private LinearLayout llPersonInfo;      //个人信息
    private TextView tvLoginOut;

    public MineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        givHead = view.findViewById(R.id.giv_mine_head);        //头像
        givHead.setOnClickListener(this);                       //头像点击监听

        tvCustName = view.findViewById(R.id.mine_tv_cust_name);    //用户名
        tvToLogin = view.findViewById(R.id.mine_tv_to_login);      //立即登录
        llPersonInfo = view.findViewById(R.id.mine_ll_person_info); //个人信息
        tvLoginOut = view.findViewById(R.id.tv_login_out);

        tvLoginOut.setOnClickListener(this);                    //退出登录
        tvToLogin.setOnClickListener(this);                        //点击立即登录
        llPersonInfo.setOnClickListener(this);                     //点击个人信息

        showHead();                                             //显示头像



    }

    /**
     * 根据用户登录状态显示头像，用户名，登录提示
     */
    private void showHead() {
        if(!SPUtil.isLogin(getContext())){      //判断是否登录，未登录则不做操作
            //本地头像
            givHead.loadLocalCircleImage(R.drawable.ic_mine_head,R.drawable.ic_mine_head);
            tvCustName.setText("未登录");
            tvToLogin.setVisibility(View.VISIBLE);  //显示立即登录
            llPersonInfo.setVisibility(View.GONE);  //隐藏个人信息跳转
            return;
        }

        Customer customer = new Customer();
        customer = SPUtil.getCustomer(getContext());
        String headImgUrl = customer.getCust_head();    //获取头像地址
        tvCustName.setText(customer.getCust_name());
        tvToLogin.setVisibility(View.GONE);             //隐藏立即登录
        llPersonInfo.setVisibility(View.VISIBLE);       //显示个人信息跳转
        givHead.loadCircleImage(headImgUrl,R.drawable.ic_mine_head);
}

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mine_ll_person_info: //点击个人信息跳转
            case R.id.mine_tv_to_login:    //点击登录跳转
            case R.id.giv_mine_head:       //点击头像
                Log.i("MineFragment", "onClick: 点击头像"+SPUtil.isLogin(getContext()));

                if(!SPUtil.isLogin(getContext())){          //若未登录则跳转到登录页面
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    intent.putExtra("isShow",true);
                    startActivityForResult(intent,REQUSET_LOGIN);
                }else{
                    Intent intent = new Intent(getContext(), PersonInfoActivity.class);
                    startActivityForResult(intent,REQUSET_PERSON_INFO);
                }
                break;
            case R.id.tv_login_out:
                if (SPUtil.isLogin(getContext())){      //登录状态
                    SPUtil.customerLogout(getContext());
                    showHead();
                }
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_SUCCESS){      //返回成功结果码

            switch (requestCode){
                case REQUSET_LOGIN:
                case REQUSET_PERSON_INFO:
                    showHead();         //更新头像及其他个人信息
                    break;
            }
        }

    }

    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);
        switch (event.getMsg()){
            case "MineFragment":
                Log.i(TAG, "onMessageEvent: 更新我的页面");
                showHead();         //更新头像及其他个人信息
                break;
        }
    }
}
