package com.frf.harbinmetro.myinfo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.frf.harbinmetro.R;
import com.frf.harbinmetro.login.model.Passenger;
import com.frf.harbinmetro.myinfo.model.MyInfoSelect;
import com.frf.harbinmetro.pay.PayActivity;
import com.frf.harbinmetro.pay.model.PaySelect;
import com.frf.harbinmetro.searchstation.SearchStationActivity;
import com.frf.harbinmetro.stationinfo.StationInfoActivity;
import com.frf.harbinmetro.util.MyApplication;
import com.frf.harbinmetro.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyInfoActivity extends AppCompatActivity {

    private Toolbar myinfo_activity_toolbar;//顶部导航栏

    private ListView myInfoListView;//个人信息列表

    private ArrayList<MyInfoSelect> objects;

    private String sexSelect = "男";//性别选择

    private final static int UPDATE_SEX=1;//修改性别

    private BaseAdapter myInfoAdapter = new BaseAdapter(){

        @Override
        public int getCount() {
            return objects.size();
        }

        @Override
        public Object getItem(int position) {
            return objects.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = View.inflate(MyInfoActivity.this, R.layout.activity_my_info_listview_items, null);
            } else {
                view = convertView;
            }

            TextView myinfokey = view.findViewById(R.id.my_info_activity_listview_textview1);
            TextView myinfovalue = view.findViewById(R.id.my_info_activity_listview_textview2);

            myinfokey.setText(objects.get(position).getMyinfokey());
            myinfovalue.setText(objects.get(position).getMyinfovalue());

            return view;
        }
    };

    //异步消息处理机制,个人消息如果进行修改，则界面进行也要进行修改
    private Handler myInfoHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_SEX://修改性别
                    myInfoListView.setAdapter(myInfoAdapter);
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);

        myinfo_activity_toolbar = findViewById(R.id.myinfo_activity_toolbar);
        myinfo_activity_toolbar.setTitle("个人信息");
        myinfo_activity_toolbar.setLogo(R.drawable.ic_action_myinfo_white);
        setSupportActionBar(myinfo_activity_toolbar);
        ActionBar actionBar = getSupportActionBar();//得到ActionBar的实例
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//通过该方法让导航按钮显示出来
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);//通过该方法设置一个导航按钮图标
        }

        objects = new ArrayList<MyInfoSelect>();

        MyInfoSelect info1 =new MyInfoSelect("用户名",MyApplication.getPassenger().getUsername());
        MyInfoSelect info2 =new MyInfoSelect("UID",MyApplication.getPassenger().getId());
        MyInfoSelect info3 =new MyInfoSelect("姓名",MyApplication.getPassenger().getRealname());
        MyInfoSelect info4 =new MyInfoSelect("身份证号码",MyApplication.getPassenger().getIdcard());
        MyInfoSelect info5 =new MyInfoSelect("性别",MyApplication.getPassenger().getSex());
        MyInfoSelect info6 =new MyInfoSelect("生日",MyApplication.getPassenger().getBirthday());
        MyInfoSelect info7 =new MyInfoSelect("手机",MyApplication.getPassenger().getPhonenumber());
        MyInfoSelect info8 =new MyInfoSelect("邮箱",MyApplication.getPassenger().getEmail());

        objects.add(info1);
        objects.add(info2);
        objects.add(info3);
        objects.add(info4);
        objects.add(info5);
        objects.add(info6);
        objects.add(info7);
        objects.add(info8);

        myInfoListView = findViewById(R.id.my_info_activity_select_listview);
        myInfoListView.setAdapter(myInfoAdapter);
        myInfoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch(position){
                    case 0://用户名
                        Toast.makeText(MyInfoActivity.this,"你选择了"+objects.get(position).getMyinfokey().toString()+"选项",Toast.LENGTH_SHORT).show();
                        Intent intent0 = new Intent(MyInfoActivity.this,UpdateUserNameActivity.class);
                        startActivity(intent0);
                        break;
                    case 1://UID
                        Toast.makeText(MyInfoActivity.this,"UID不可修改",Toast.LENGTH_SHORT).show();
                        break;
                    case 2://姓名
                        Toast.makeText(MyInfoActivity.this,"姓名不可修改",Toast.LENGTH_SHORT).show();
                        break;
                    case 3://身份证号码
                        Toast.makeText(MyInfoActivity.this,"身份证号码不可修改",Toast.LENGTH_SHORT).show();
                        break;
                    case 4://性别
                        Toast.makeText(MyInfoActivity.this,"你选择了"+objects.get(position).getMyinfokey().toString()+"选项",Toast.LENGTH_SHORT).show();
                        //性别选择框
                        SexSelectDialog();
                        break;
                    case 5://生日
                        Toast.makeText(MyInfoActivity.this,"你选择了"+objects.get(position).getMyinfokey().toString()+"选项",Toast.LENGTH_SHORT).show();

                        break;
                    case 6://手机
                        Toast.makeText(MyInfoActivity.this,"你选择了"+objects.get(position).getMyinfokey().toString()+"选项",Toast.LENGTH_SHORT).show();
                        //手机号码修改框
                        PhoneDialog();
                        break;
                    case 7://邮箱
                        Toast.makeText(MyInfoActivity.this,"你选择了"+objects.get(position).getMyinfokey().toString()+"选项",Toast.LENGTH_SHORT).show();

                        break;
                        default:
                            break;
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home://ToolBar最左侧的按钮叫做HomeAsUp按钮，而且id永远是android.R.id.home
                finish();//结束这个这个活动
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 性别选择框
     */
    public void SexSelectDialog(){
        sexSelect = "男" ;
        final String items[] = {"男", "女"};
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_action_myinfo)
                //设置标题的图片
                .setTitle("性别选择")
                //设置对话框的标题
                .setSingleChoiceItems(items, 1, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MyInfoActivity.this, "你暂时选择了："+items[which], Toast.LENGTH_SHORT).show();
                        sexSelect = items[which];
                    }
                }) .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }) .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        //向服务器发送性别修改请求
                        UpdateSexRequest(MyApplication.getPassenger().getId(), sexSelect);
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    /**
     * 向服务器发送修改性别请求
     * @param userid
     * @param update_sex
     */
    public void UpdateSexRequest(final String userid, final String update_sex) {
        //请求地址
        String url = "http://HaerbinStation.free.idcfengye.com/AppHarbinMetro/UpdateSexServlet";
        String tag = "UpdateSex";

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //防止重复请求，所以先取消tag标识的请求队列
        requestQueue.cancelAll(tag);

        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //解析从response中获得的json格式数据
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");
                            String result = jsonObject.getString("Result");
                            if (result.equals("success")) {//修改成功
                                //修改成功后，继续解析jsonObject对象，对原来的乘客信息进行修改
                                MyApplication.getPassenger().setSex(update_sex);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Message message = new Message();
                                        message.what = UPDATE_SEX;
                                        myInfoHandler.sendMessage(message);
                                    }
                                }).start();
                                //做自己的修改成功操作，如Toast提示和页面跳转
                                Toast.makeText(MyInfoActivity.this, "修改性别成功！", Toast.LENGTH_SHORT).show();
                            }
                            if(result.equals("failed")){//修改失败
                                //做自己的修改失败操作，如Toast提示
                                Toast.makeText(MyInfoActivity.this, "修改性别失败！", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.e("TAG", error.getMessage(), error);
                    }
                })
                //是StringRequest匿名类的重写方法，设置请求参数
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", userid);
                params.put("updatesex", update_sex);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

    /**
     * 手机号码选择框
     */
    public void PhoneDialog(){
        View view = getLayoutInflater().inflate(R.layout.dialog_phone_update, null);
        final EditText editText = (EditText) view.findViewById(R.id.dialog_phone_update_edittext);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_action_myinfo)//设置标题的图片
                .setTitle("修改手机号码")//设置对话框的标题
                .setView(view) .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        String content = editText.getText().toString();
                        if(content.equals(MyApplication.getPassenger().getPhonenumber())){//输入的手机号码和之前相同
                            Toast.makeText(MyInfoActivity.this, "输入的手机号码与之前相同", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(content.equals("")){//输入的手机号码不能为空
                            Toast.makeText(MyInfoActivity.this, "输入的手机号码不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(content.length()!=11){//输入的手机号码没有11位数
                            Toast.makeText(MyInfoActivity.this, "输入的手机号码没有11位数", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        //向服务器发送修改手机号码请求
                        UpdatePhoneRequest(MyApplication.getPassenger().getId(), content);
                        //Toast.makeText(MyInfoActivity.this, content, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    public void UpdatePhoneRequest(final String userid, final String update_phone) {
        //请求地址
        String url = "http://HaerbinStation.free.idcfengye.com/AppHarbinMetro/UpdatePhoneServlet";
        String tag = "UpdatePhone";

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //防止重复请求，所以先取消tag标识的请求队列
        requestQueue.cancelAll(tag);

        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //解析从response中获得的json格式数据
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");
                            String result = jsonObject.getString("Result");
                            if (result.equals("success")) {//修改成功
                                //修改成功后，继续解析jsonObject对象，对原来的乘客信息进行修改
                                MyApplication.getPassenger().setPhonenumber(update_phone);
                                //做自己的修改成功操作，如Toast提示和页面跳转
                                Toast.makeText(MyInfoActivity.this, "修改手机号码成功！", Toast.LENGTH_SHORT).show();
                            }
                            if(result.equals("failed")){//修改失败
                                //做自己的修改失败操作，如Toast提示
                                Toast.makeText(MyInfoActivity.this, "修改手机号码失败！", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.e("TAG", error.getMessage(), error);
                    }
                })
                //是StringRequest匿名类的重写方法，设置请求参数
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", userid);
                params.put("updatephone", update_phone);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

    @Override
    public void onResume() {//要善于利用Activity和Fragment的生命周期实现UI的变化，利用Android系统提供的Application类实现共享数据，活灵活用
        super.onResume();

        //MyApplication myApplication = (MyApplication) MyInfoActivity.this.getApplication();

        //重新加载乘客的信息
        objects = new ArrayList<MyInfoSelect>();

        MyInfoSelect info1 =new MyInfoSelect("用户名",MyApplication.getPassenger().getUsername());
        MyInfoSelect info2 =new MyInfoSelect("UID",MyApplication.getPassenger().getId());
        MyInfoSelect info3 =new MyInfoSelect("姓名",MyApplication.getPassenger().getRealname());
        MyInfoSelect info4 =new MyInfoSelect("身份证号码",MyApplication.getPassenger().getIdcard());
        MyInfoSelect info5 =new MyInfoSelect("性别",MyApplication.getPassenger().getSex());
        MyInfoSelect info6 =new MyInfoSelect("生日",MyApplication.getPassenger().getBirthday());
        MyInfoSelect info7 =new MyInfoSelect("手机",MyApplication.getPassenger().getPhonenumber());
        MyInfoSelect info8 =new MyInfoSelect("邮箱",MyApplication.getPassenger().getEmail());

        objects.add(info1);
        objects.add(info2);
        objects.add(info3);
        objects.add(info4);
        objects.add(info5);
        objects.add(info6);
        objects.add(info7);
        objects.add(info8);

    }
}
