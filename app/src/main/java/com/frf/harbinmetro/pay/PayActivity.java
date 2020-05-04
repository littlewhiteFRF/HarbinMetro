package com.frf.harbinmetro.pay;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
import com.frf.harbinmetro.addmoney.AddMoneyActivity;
import com.frf.harbinmetro.goods.model.Goods;
import com.frf.harbinmetro.lineinfo.LineInfoActivity;
import com.frf.harbinmetro.pay.model.PaySelect;
import com.frf.harbinmetro.payinfo.PayInfoActivity;
import com.frf.harbinmetro.searchline.SearchLineActivity;
import com.frf.harbinmetro.searchline.model.Line;
import com.frf.harbinmetro.util.MyApplication;
import com.frf.harbinmetro.util.PayPwdEditText;
import com.frf.harbinmetro.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PayActivity extends AppCompatActivity {

    private Toolbar pay_activity_toolbar;//顶部导航栏

    private ListView paySelectListView;//支付方式列表

    private ArrayList<PaySelect> objects;

    private Dialog walletDialog;//支付密码框
    private String inputpassword;//输入的支付密码

    private TextView startstation;//订单信息的起点
    private TextView finishstation;//订单信息的终点
    private TextView ticketprice;//订单信息的票价

    private Goods goodsInfo;//订单信息对象

    private Line lineInfo;//线路信息对象

    private BaseAdapter paySelectAdapter = new BaseAdapter() {//支付方式选择列表适配器
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
                view = View.inflate(PayActivity.this, R.layout.pay_activity_select_listview_items, null);
            } else {
                view = convertView;
            }

            ImageView payImageView = view.findViewById(R.id.pay_activity_pay_listview_imageview);
            TextView payTextView = view.findViewById(R.id.pay_activity_pay_listview_textview);

            payImageView.setImageDrawable(getResources().getDrawable(objects.get(position).getResId()));
            payTextView.setText(objects.get(position).getPayName());

            return view;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        pay_activity_toolbar = findViewById(R.id.pay_activity_toolbar);
        pay_activity_toolbar.setTitle("付款确认");
        pay_activity_toolbar.setLogo(R.drawable.ic_action_pay);
        setSupportActionBar(pay_activity_toolbar);
        ActionBar actionBar = getSupportActionBar();//得到ActionBar的实例
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//通过该方法让导航按钮显示出来
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);//通过该方法设置一个导航按钮图标
        }

        objects = new ArrayList<PaySelect>();

        PaySelect paySelect1 = new PaySelect();
        paySelect1.setResId(R.drawable.ali_pay_logo);
        paySelect1.setPayName("支付宝");
        objects.add(paySelect1);
        PaySelect paySelect2 = new PaySelect();
        paySelect2.setResId(R.drawable.wechat_pay_logo);
        paySelect2.setPayName("微信支付");
        objects.add(paySelect2);
        PaySelect paySelect3 = new PaySelect();
        paySelect3.setResId(R.drawable.union_pay_logo);
        paySelect3.setPayName("银联闪付");
        objects.add(paySelect3);

        paySelectListView = findViewById(R.id.pay_activity_select_listview);
        paySelectListView.setAdapter(paySelectAdapter);

        paySelectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(PayActivity.this,"你选择了"+objects.get(position).getPayName().toString(),Toast.LENGTH_SHORT).show();
                switch(position){
                    case 0:
                        Toast.makeText(PayActivity.this, "你选择了支付宝付款", Toast.LENGTH_SHORT).show();
                        //弹出支付密码支付框
                        showEditPayPwdDialog();
                        break;
                    case 1:
                        Toast.makeText(PayActivity.this,"该支付方式未开放",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(PayActivity.this,"该支付方式未开放",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });

        final View decorView = getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //1、获取main在窗体的可视区域
                decorView.getWindowVisibleDisplayFrame(rect);
                //2、获取main在窗体的不可视区域高度，在键盘没有弹起时，main.getRootView().getHeight()调节度应该和rect.bottom高度一样
                int mainInvisibleHeight = decorView.getRootView().getHeight() - rect.bottom;
                int screenHeight = decorView.getRootView().getHeight();//屏幕高度
                //3、不可见区域大于屏幕本身高度的1/4：说明键盘弹起了
                if (mainInvisibleHeight > screenHeight / 4) {   //软键盘显示
                    //LogUtils.d(TAG, "show------------" + rect.bottom + "----" + decorView.getRootView().getHeight());
                    //Toast.makeText(AddMoneyActivity.this,"软键盘显示",Toast.LENGTH_SHORT).show();
                } else {                      //软键盘隐藏
                    if (walletDialog!=null){        //在软键盘隐藏时，关闭Dialog。
                        walletDialog.dismiss();
                    }
                }
            }
        });

        //获得线路信息
        lineInfo = new Line();
        lineInfo = (Line) getIntent().getSerializableExtra("lineinfo");

        startstation = findViewById(R.id.pay_activity_start);
        finishstation = findViewById(R.id.pay_activity_finish);
        ticketprice = findViewById(R.id.pay_activity_ticket_price);

        startstation.setText(lineInfo.getStartstation());
        finishstation.setText(lineInfo.getFinishstation());
        ticketprice.setText("¥"+lineInfo.getTicketprice()+".00");

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
     * 支付密码输入框
     */
    //将此方法放在按钮的点击事件中即可弹出输入支付密码页面
    private void showEditPayPwdDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_et_paypwd, null);
        walletDialog = new Dialog(this, R.style.walletFrameWindowStyle);
        walletDialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        final Window window = walletDialog.getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        //紧贴软键盘弹出
        wl.gravity = Gravity.BOTTOM;
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        walletDialog.onWindowAttributesChanged(wl);
        walletDialog.setCanceledOnTouchOutside(false);
        walletDialog.show();

        final PayPwdEditText ppet = (PayPwdEditText) view.findViewById(R.id.dialog_ppet);
        //调用initStyle方法创建你需要设置的样式
        ppet.initStyle(R.drawable.edit_num_bg, 6, 0.33f, R.color.yellow, R.color.yellow, 30);
        ppet.setOnTextFinishListener(new PayPwdEditText.OnTextFinishListener() {
            @Override
            public void onFinish(String str) {//密码输入完后的回调
                //手动收起软键盘
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(ppet.getWindowToken(), 0);

                //可在此执行下一步操作
                //获得支付密码
                inputpassword = ppet.getPwdText();
                if(inputpassword.equals(MyApplication.getPassenger().getAlipaypassword())){//支付密码正确
                    //支付密码输入框消失
                    walletDialog.dismiss();
                    //向服务器发送支付请求
                    PayRequest(MyApplication.getPassenger().getId(), MyApplication.getPassenger().getAlipayid(), inputpassword, lineInfo.getStartstation(), lineInfo.getFinishstation(), lineInfo.getTicketprice());

                }else{//支付密码不正确
                    Toast.makeText(PayActivity.this,"支付密码不正确",Toast.LENGTH_SHORT).show();
                }

            }
        });
        //延迟弹起软键盘，使PayPwdEditText获取焦点
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ppet.setFocus();
            }
        }, 100);
    }

    /**
     * 向服务器发送支付请求
     * @param alipayid
     * @param inputpassword
     * @param startstation
     * @param finishstation
     * @param ticketprice
     */
    public void PayRequest(final String userid, final String alipayid, final String inputpassword, final String startstation, final String finishstation, final String ticketprice) {
        //请求地址
        String url = "http://HaerbinStation.free.idcfengye.com/AppHarbinMetro/PayServlet";
        String tag = "Pay";

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
                            if (result.equals("paysuccess")) {//支付成功
                                finish();
                                //做自己的支付成功操作，如页面跳转
                                Toast.makeText(PayActivity.this, "支付成功！", Toast.LENGTH_SHORT).show();
                                //支付成功后，跳转到站点支付成功页面
                                Intent intent = new Intent(PayActivity.this, PayInfoActivity.class);
                                intent.putExtra("paymoney",ticketprice);
                                intent.putExtra("goodsid",jsonObject.getString("goodsid"));
                                startActivity(intent);
                            }
                            if(result.equals("goodsfailed")){//生成订单错误
                                Toast.makeText(PayActivity.this, "生成订单错误,支付失败！", Toast.LENGTH_SHORT).show();
                            }
                            if(result.equals("payfailed")){//支付失败
                                //做自己的支付失败操作，如Toast提示
                                Toast.makeText(PayActivity.this, "支付失败！请重新支付", Toast.LENGTH_SHORT).show();
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
                params.put("userid",userid);
                params.put("alipayid", alipayid);
                params.put("inputpassword", inputpassword);
                params.put("startstation",startstation);
                params.put("finishstation",finishstation);
                params.put("ticketprice",ticketprice);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }
}
