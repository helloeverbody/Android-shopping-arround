package jju.example.com.xgg.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.longsh.optionframelibrary.OptionBottomDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.sunfusheng.glideimageview.GlideImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import jju.example.com.xgg.R;
import jju.example.com.xgg.config.Constant;
import jju.example.com.xgg.pojo.Base;
import jju.example.com.xgg.pojo.BasePojo;
import jju.example.com.xgg.pojo.Customer;
import jju.example.com.xgg.util.ImageUtil;
import jju.example.com.xgg.util.JsonUtil;
import jju.example.com.xgg.util.SPUtil;
import jju.example.com.xgg.util.VerifyPermission;
import jju.example.com.xgg.widget.ChoseSexDialog;
import jju.example.com.xgg.widget.MyEditDialog;

public class PersonInfoActivity extends FullTranStatActivity implements View.OnClickListener {

    private static final int STORAGE_REQUSET_CODE = 1;
    private static final int CAMERA_REQUSET_CODE = 2;
    private static final int RESULT_SUCCESS = 0;     // 成功的结果码
    private static final int RESULT_FAIL = -1;       // 失败的结果码
    private static final String TAG = "PersonInfoActivity";


    private LinearLayout llHead;        //头像行
    private LinearLayout llName;        //用户名行
    private LinearLayout llSex;         //性别行
    private LinearLayout llBirthday;  //出生日期行
    private LinearLayout llLogout;    //退出登录
    private ImageButton ibBack;       //返回键

    private GlideImageView givHead; //头像
    private TextView tvName;        //用户名
    private TextView tvSex;         //性别
    private TextView tvBirthday;    //出生日期

    private static final int REQUEST_PHOTO_CAMERA = 0;    // 拍照请求
    private static final int REQUEST_PHOTO_ALBUM = 1;   // 读取本地相册请求
    private static final int RESULT_PHONE_CROP = 2;   // 返回裁图结果

    private File tempFile;    // 拍照的照片临时存放路径
    private Uri imageUri;   // 拍照的照片返回数据格式
    private Bitmap bitmap;
    private InputStream imgIs;

    File cutfile;           //截图文件
    private Uri mCutUri;




    private String cutcameraNew;    //截图生成的文件uri

    private int mYear;
    private int mMonth;
    private int mDay;

    private Customer customer ; //用户信息
    private String key;         //七牛云删除图片的key
    private AsyncHttpClient httpClient = new AsyncHttpClient();  //异步网络框架 接收selevet请求


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);

        setStatusBarFullTransparent(true);      //状态栏透明
        setFitSystemWindow(true);               //添加状态栏高度
        initPhotoError();   // 解决android7.0系统拍照时，报uri错误问题

        init();
    }


    private void init() {

        llHead = findViewById(R.id.ll_person_info_head);        //头像行
        llName = findViewById(R.id.ll_person_info_name);        //用户名行
        llSex = findViewById(R.id.ll_person_info_sex);          //性别行
        llBirthday = findViewById(R.id.ll_person_info_birthday);//出生日期行
        llLogout = findViewById(R.id.person_info_logout);       //退出登录行

        ibBack = findViewById(R.id.ib_person_info_back);  //返回键

        givHead = findViewById(R.id.giv_person_info_head);       //头像
        tvName = findViewById(R.id.tv_person_info_name);         //用户名
        tvSex = findViewById(R.id.tv_person_info_sex);           //性别
        tvBirthday = findViewById(R.id.tv_person_info_birthday); //出生日期

        llHead.setOnClickListener(this);            //头像行点击监听
        givHead.setOnClickListener(this);           //头像点击监听
        llName.setOnClickListener(this);            //用户名行点击监听
        llSex.setOnClickListener(this);             //性别行点击监听
        llBirthday.setOnClickListener(this);        //出生日期行点击监听
        ibBack.setOnClickListener(this);            //返回键点击监听
        llLogout.setOnClickListener(this);          //退出登录点击监听



        showInfo();    //显示用户信息



    }

    private void showInfo() {           //测试
        customer = new Customer();
        customer = SPUtil.getCustomer(PersonInfoActivity.this);
        String head = customer.getCust_head();
        givHead.loadCircleImage(head,R.drawable.ic_mine_head);

        tvName.setText(customer.getCust_name());
        String sex = null;

        switch (customer.getCust_sex()) {  //性别输出格式化
            case 0 :
                sex = "未填写";
                break;
            case 1 :
                sex = "男";
                break;
            case 2 :
                sex = "女";
                break;
        }
        tvSex.setText(sex);

        String birthday = customer.getCust_birthday();
        if (birthday==null){
            String[] birthdayArray = new String[]{"1987","3","10"};
        }else {
            String[] birthdayArray = birthday.split("-");
            birthday = birthdayArray[0]+"年"+birthdayArray[1]+"月"+birthdayArray[2]+"日";
        }
        tvBirthday.setText(birthday);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_person_info_head:          //点击头像行
            case R.id.giv_person_info_head:
                ChoseHeadDialog(v);                 //选择头像
                break;
            case R.id.ll_person_info_name:          //点击用户名行
                setName();                          //设置用户名
                break;
            case R.id.ll_person_info_sex:           //点击性别行
                chooseSex();                        //设置性别
                break;
            case R.id.ll_person_info_birthday:      //点击出生日期行
                chooseBirthday();                   //设置出生日期
                break;
            case R.id.ib_person_info_back:          //点击返回键

                setResult(RESULT_SUCCESS);            //返回上个页面
                finish();
                break;
            case R.id.person_info_logout:          //点击退出登录

                SPUtil.customerLogout(this);       //退出登录

                setResult(RESULT_SUCCESS);            //返回上个页面
                finish();
                break;
        }
    }





    /**
     * 选择上传头像方式弹窗
     * @param view
     */
    public void ChoseHeadDialog(View view) {
        List<String> stringList = new ArrayList<String>();
        stringList.add("拍照");
        stringList.add("从相册选择");
        VerifyPermission.verifyCameraPermission(this);      // 申请相机权限
        Log.i("PersonInfoActivity", "ChoseHeadDialog: 申请相机权限");
        final OptionBottomDialog optionBottomDialog = new OptionBottomDialog(this, stringList);
        optionBottomDialog.setItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                optionBottomDialog.dismiss();

                switch (position){
                    case 0:         //拍照
                        takePhoto();    // 选择相机
                        break;
                    case 1:         //从相册中选择
                        choosePhoto();  // 选择相册
                        break;
                }

            }


        });
    }

    /**
     * 设置用户名
     */
    private void setName() {
        MyEditDialog myEditDialog = new MyEditDialog(this);
        myEditDialog.setTitle("请设置您的用户名！！！");
        myEditDialog.setmOnMedClickListener(new MyEditDialog.onMedClickListener() {
            @Override
            public void onBtnSureClick(String content) {
                Log.i(TAG, "onBtnSureClick: 点击确认按钮");
                Log.i(TAG, "onBtnSureClick: conent:"+content);
                tvName.setText(content);
                customer.setCust_name(content);
                updateCustomer();       //修改用户信息
            }

            @Override
            public void onBtnCancelClick() {
                Log.i(TAG, "onBtnCancelClick: 点击取消按钮");
            }
        });
        myEditDialog.show();
    }



    /**
     * 设置性别
     */
    private void chooseSex() {
        ChoseSexDialog choseSexDialog= new ChoseSexDialog(this);
        choseSexDialog.setmOnCsdClickListener(new ChoseSexDialog.onCsdClickListener() {
            @Override
            public void onManSureClick(int man) {
                Log.i(TAG, "onManSureClick: 选择为："+man);
                tvSex.setText("男");
                customer.setCust_sex(man);
                updateCustomer();       //修改用户信息
            }

            @Override
            public void onWomanCancelClick(int woman) {
                Log.i(TAG, "onManSureClick: 选择为："+woman);
                tvSex.setText("女");
                customer.setCust_sex(woman);
                updateCustomer();       //修改用户信息
            }
        });
        choseSexDialog.show();
    }

    /**
     * 设置出生日期
     */
    private void chooseBirthday() {
        Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
        //TODO 调用时间选择器
        new DatePickerDialog(PersonInfoActivity.this, R.style.MyDatePickerDialogTheme,onDateSetListener, mYear, mMonth, mDay).show();
    }


    /**
     * 日期选择器对话框监听
     */
    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            String days;
            if (mMonth + 1 < 10) {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("年").append("0").
                            append(mMonth + 1).append("月").append("0").append(mDay).append("日").toString();
                } else {
                    days = new StringBuffer().append(mYear).append("年").append("0").
                            append(mMonth + 1).append("月").append(mDay).append("日").toString();
                }

            } else {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("年").
                            append(mMonth + 1).append("月").append("0").append(mDay).append("日").toString();
                } else {
                    days = new StringBuffer().append(mYear).append("年").
                            append(mMonth + 1).append("月").append(mDay).append("日").toString();
                }

            }
            tvBirthday.setText(days);               //选择出生日期后显示
            days = days.replace("年","-");
            days = days.replace("月","-");
            days = days.replace("日","");
            customer.setCust_birthday(days);
            updateCustomer();       //修改用户信息
        }
    };
    /**
     * 选择相机
     * 创建存储拍照照片的文件夹，并调起相机
     */
    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 调起相机
        // 判断存储卡是否可以用，可用进行存储
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            String filename = "cutcamera"+ImageUtil.getTempFileName() + ".png";
            tempFile = new File(Environment.getExternalStorageDirectory(),
                    filename);     // 指定图片文件名
            imageUri = Uri.fromFile(tempFile);   // 从文件中创建uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); // 指定拍照返回的uri



        }
        startActivityForResult(intent, REQUEST_PHOTO_CAMERA);

    }
    /**
     * 选择相册
     */
    private void choosePhoto() {
        /* 打开选择图片的界面*/
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent, REQUEST_PHOTO_ALBUM);
    }


    /**
     *解决android7.0系统拍照时，报uri错误问题
     */
    private void initPhotoError() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
    }



    /**
     * 拍照或者在相册中选择图片后，图片以URI格式返回
     **/
    @Override
    protected void onActivityResult(int req, int res, Intent data) {
        if (res == RESULT_OK){

            switch (req){
                case REQUEST_PHOTO_CAMERA:      //拍照请求回调

                    Log.i("PersonInfoActivity", "onActivityResult: 拍照返回之后1");
                    startActivityForResult(CutForPhoto(imageUri),RESULT_PHONE_CROP);

                    break;
                case REQUEST_PHOTO_ALBUM:       //相册请求回调

                    Uri uri = data.getData();    // 该uri是上一个Activity返回的
                    Log.i("PersonInfoActivity", "onActivityResult: 从相册选择图片");
                    startActivityForResult(CutForPhoto(uri),RESULT_PHONE_CROP);  //图片裁剪

                    break;
                case RESULT_PHONE_CROP:         //截图请求回调

                    photoDel(tempFile);   //删除拍照的临时文件


                    try {
                        Log.i("PersonInfoActivity", "onActivityResult: 返回成功后的图片");
                        Uri qiniuUri = mCutUri;
                        InputStream qiniuImgIs = null;
                        qiniuImgIs = getContentResolver().openInputStream(qiniuUri);
                        Bitmap qiniuBitmap = BitmapFactory.decodeStream(qiniuImgIs);
                        key = customer.getCust_head().replace(Constant.QINIU_BASE_URL,"");
                        headUpload(qiniuBitmap,cutcameraNew); //上传到七牛云
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Log.i("PersonInfoActivity", "onActivityResult: 截取失败");
                    }

                    break;
            }

        }
    }




    /**
     * 图片裁剪
     * @param uri
     * @return
     */
    private Intent CutForPhoto(Uri uri) {
        try {
            //直接裁剪
            Intent intent = new Intent("com.android.camera.action.CROP");
            //设置裁剪之后的图片路径文件

            cutcameraNew = "image_"+ ImageUtil.getTempFileName()+".png";  //文件名
            cutfile = new File(Environment.getExternalStorageDirectory().getPath(),
                    cutcameraNew); //生成的截图文件

            photoDel(cutfile);   //若存在本地图片，则删除

            cutfile.createNewFile();
            //初始化 uri
            Uri imageUris = uri; //返回来的 uri
            Uri outputUri = null; //真实的 uri
            Log.d("PersonInfoActivity", "CutForPhoto: "+cutfile);
            outputUri = Uri.fromFile(cutfile);
            mCutUri = outputUri;
            Log.d("PersonInfoActivity", "mCameraUri: "+mCutUri);
            // crop为true是设置在开启的intent中设置显示的view可以剪裁
            intent.putExtra("crop",true);
            // aspectX,aspectY 是宽高的比例，这里设置正方形
            intent.putExtra("aspectX",1);
            intent.putExtra("aspectY",1);
            //设置要裁剪的宽高
            intent.putExtra("outputX", dip2px(200)); //200dp
            intent.putExtra("outputY", dip2px(200));
            intent.putExtra("scale",true);
            //如果图片过大，会导致oom，这里设置为false
            intent.putExtra("return-data",false);
            if (imageUris != null) {
                intent.setDataAndType(imageUris, "image/*");
            }
            if (outputUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
            }
            intent.putExtra("noFaceDetection", true);
            //压缩图片
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            return intent;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }




    public  float dip2px(float dipValue)
    {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return  (dipValue * scale + 0.5f);
    }



    /*权限回调*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQUSET_CODE:
                switch (permissions[0]){
                    case Manifest.permission.READ_CONTACTS://权限1
                        if (grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                            Log.i("PersonInfoActivity", "onRequestPermissionsResult: 已获得相机权限");
                        }else {
                            Log.i("PersonInfoActivity", "onRequestPermissionsResult: 未获得相机权限");
                        }
                        break;

                }

                VerifyPermission.verifyStoragePermission(PersonInfoActivity.this);
                break;
            case STORAGE_REQUSET_CODE:
                switch (permissions[0]){
                    case Manifest.permission.READ_CONTACTS://权限1
                        if (grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                            Log.i("PersonInfoActivity", "onRequestPermissionsResult: 已获得存储权限");
                        }else {
                            Log.i("PersonInfoActivity", "onRequestPermissionsResult: 未获得存储权限");
                        }
                        break;

                }
                break;
            default:
        }
    }

    /**
     * 删除本地生成的临时图片
     */
    private void photoDel(File tempFile) {
        if( tempFile == null || !tempFile.exists() ){return;} //文件不存在，直接返回

        if (tempFile.delete()) {       //删除临时照片
            Log.i("PersonInfoActivity", "takePhoto: 照片已删除");
            Log.i(TAG, "photoDel: imageUri："+imageUri);
        }else{
            Log.i("PersonInfoActivity", "takePhoto: 照片未删除");
            Log.i(TAG, "photoDel: imageUri："+imageUri);
        }

    }

    /**
     * 上传图片到七牛云
     * @return
     */
    private void headUpload(final Bitmap qiniuBitmap, final String fileName){

        httpClient.post(Constant.QINIU_UPTOKEN,  new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i(TAG, "Success: 连接服务器成功！");
                if(statusCode==200){        //响应成功

                    String jsonString = new String(responseBody);
                    Map<String, String> map = new Gson().fromJson(jsonString,new TypeToken<Map<String, String>>() {}.getType());
                    String upToken = map.get("uptoken");

                    if(upToken != null){        //不为空，已获取upToken

                        // 将Bitmap转换为OutputStream对象
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        qiniuBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        UploadManager uploadManager = new UploadManager();
                        // 参数1 ： 上传的图片，byte[]格式
                        // 参数2 ：自定义图片名，必须唯一，可以使用时间戳
                        // 参数3 ： 上传的凭证，从后台获取
                        // 参数4 ：上传后的回调方法，如果上传成功七牛会将图片的完整路径返回
                        uploadManager.put(baos.toByteArray(), fileName, upToken, new UpCompletionHandler() {
                            @Override
                            public void complete(String key, ResponseInfo info, JSONObject response) {
                                if (info.statusCode == 200) {
                                    String urlKey = "";
                                    try {
                                        urlKey = response.getString("key");
                                        // 图片上传成功后，将用户修改的信息封装在Customer对象，发送到服务端执行个人信息修改操作
                                        customer.setCust_head(Constant.QINIU_BASE_URL + urlKey);
                                        updateCustomer();       //修改用户信息
                                        Log.i("上传成功提示", "上传成功");

                                        photoDel(cutfile);    //删除截图所生成的文件

                                        givHead.loadCircleImage(Constant.QINIU_BASE_URL + urlKey,R.drawable.ic_mine_head);
                                        //uploadUserInfo();   更新用户信息
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Log.i("上传错误提示", "complete: "+info.error);

                                }
                            }
                        }, null);
                    }


                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i(TAG, "onFailure: 连接服务器失败！");
                Log.i(TAG, "statusCode: "+statusCode);
                Log.i(TAG, "headers: "+headers);
                Log.i(TAG, "responseBody: "+responseBody);
                Log.i(TAG, "error: "+error);
            }
        });
        
    }

    /**
     * 用户信息修改
     */
    public void updateCustomer(){

        String paras = new Gson().toJson(customer);
        Log.i(TAG, "转变为json paras"+paras);

        RequestParams requestParams = new RequestParams();
        requestParams.put("paras",paras);
        if (key != null){   //key不为null时，用户修改头像，同时删除七牛云原头像
            requestParams.put("key",key);
            key = null;     //重新将key赋值为null,避免不更改图像时也传参
        }
        httpClient.post(Constant.CUSTOMER_UPDATE, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.i(TAG, "Success: 连接服务器成功！");
                if(statusCode==200){        //响应成功

                    String jsonString = new String(responseBody);
                    BasePojo basePojo = new Gson().fromJson(jsonString, new TypeToken<BasePojo<Customer>>(){}.getType());

                    if (basePojo.isSuccess()){
                        Log.i(TAG, "onSuccess: 用户信息修改成功");
                        customer = (Customer) basePojo.getList().get(0);
                        SPUtil.saveCustomer(PersonInfoActivity.this,customer);      //保存用户信息
                        Log.i(TAG, "onSuccess: customer"+customer);

                    }else{
                        Log.i(TAG, "onSuccess: 用户信息修改失败");
                    }



                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i(TAG, "onFailure: 连接服务器失败！");
                Log.i(TAG, "statusCode: "+statusCode);
                Log.i(TAG, "headers: "+headers);
                Log.i(TAG, "responseBody: "+responseBody);
                Log.i(TAG, "error: "+error);
            }
        });


    }
}
