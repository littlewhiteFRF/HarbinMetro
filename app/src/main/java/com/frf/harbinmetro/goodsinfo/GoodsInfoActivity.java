package com.frf.harbinmetro.goodsinfo;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.frf.harbinmetro.R;
import com.frf.harbinmetro.goods.model.Goods;
import com.frf.harbinmetro.pay.PayActivity;
import com.frf.harbinmetro.searchline.model.Line;
import com.frf.harbinmetro.searchmoney.SearchMoneyActivity;
import com.frf.harbinmetro.util.MyApplication;
import com.frf.harbinmetro.util.PayPwdEditText;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GoodsInfoActivity extends AppCompatActivity {

    private Toolbar goods_info_activity_toolbar;//顶部导航栏

    private TextView lineid;
    private TextView startstation;
    private TextView endstation;
    private TextView ticketprice;
    private TextView paystyle;
    private TextView datetime;
    private TextView goodsid;
    private TextView state;

    private ImageView qrcode;//二维码

    private Button btn_use;//使用按钮
    private Button btn_quit;//退票按钮

    private TextView textView;

    Goods goodsInfo;

    Dialog walletDialog;

    String inputpassword;//输入支付密码字符串

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_info);

        goods_info_activity_toolbar = findViewById(R.id.goods_info_activity_toolbar);
        goods_info_activity_toolbar.setTitle("订单详情");
        goods_info_activity_toolbar.setLogo(R.drawable.ic_action_goods_white);
        setSupportActionBar(goods_info_activity_toolbar);
        ActionBar actionBar = getSupportActionBar();//得到ActionBar的实例
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//通过该方法让导航按钮显示出来
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);//通过该方法设置一个导航按钮图标
        }

        lineid = findViewById(R.id.goods_info_activity_textview_lineid);
        startstation = findViewById(R.id.goods_info_activity_textview_start);
        endstation = findViewById(R.id.goods_info_activity_textview_finish);
        ticketprice = findViewById(R.id.goods_info_activity_textview_ticketprice);
        paystyle = findViewById(R.id.goods_info_activity_textview_paystyle);
        datetime = findViewById(R.id.goods_info_activity_textview_datetime);
        goodsid = findViewById(R.id.goods_info_activity_goodsid);
        state = findViewById(R.id.goods_info_activity_state);

        //获得可用订单信息
        goodsInfo = new Goods();
        goodsInfo = (Goods) getIntent().getSerializableExtra("goodsinfo");

        lineid.setText(goodsInfo.getLineid());
        startstation.setText(goodsInfo.getStartstation());
        endstation.setText(goodsInfo.getFinishstation());
        ticketprice.setText("¥"+goodsInfo.getTicketprice()+".00");
        paystyle.setText("支付宝");
        datetime.setText(goodsInfo.getDatetime());
        goodsid.setText(goodsInfo.getGoodsid());
        state.setText(goodsInfo.getState());

        btn_use = findViewById(R.id.goods_info_activity_button_use);
        btn_quit = findViewById(R.id.goods_info_activity_button_quit);
        textView = findViewById(R.id.goods_info_activity_textview8);

        if(goodsInfo.getState().equals("yes")){//可用订单

//            btn_use.setVisibility(View.VISIBLE);
//            btn_quit.setVisibility(View.VISIBLE);
//            textView.setVisibility(View.VISIBLE);

            //生成二维码
            qrcode = findViewById(R.id.goods_info_activity_qrcode);
            Bitmap qrBitmap = generateBitmap(goodsInfo.getGoodsid(),400, 400);
            Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.harbin_metro_logo_round);
            Bitmap bitmap = addLogo(qrBitmap, logoBitmap);
            qrcode.setImageBitmap(bitmap);

            btn_use.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(GoodsInfoActivity.this,"你点击了使用按钮",Toast.LENGTH_SHORT).show();
                    //向服务器发送使用请求，此时修改订单的状态从yes改变为no（实际情况应该是通过扫码来进行使用，这里只是模拟这样一个使用过程）
                    useTicketRequest(goodsInfo.getGoodsid());
                }
            });

            btn_quit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(GoodsInfoActivity.this,"你点击了退票按钮",Toast.LENGTH_SHORT).show();
                    //弹出支付密码输入框
                    showEditPayPwdDialog();
                }
            });
        }else{//历史订单（不可用订单）
            btn_use.setVisibility(View.GONE);
            btn_quit.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        }

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
                    //向服务器发送退票请求，删除该订单信息，并且退款
                    quitTicketRequest(MyApplication.getPassenger().getAlipayid(),goodsInfo.getTicketprice(),inputpassword,goodsInfo.getGoodsid());

                }else{//支付密码不正确
                    Toast.makeText(GoodsInfoActivity.this,"支付密码不正确",Toast.LENGTH_SHORT).show();
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
     * 生成二维码
     * @param content
     * @param width
     * @param height
     * @return
     */
    private Bitmap generateBitmap(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 给二维码中间添加图片logo
     * @param qrBitmap
     * @param logoBitmap
     * @return
     */
    private Bitmap addLogo(Bitmap qrBitmap, Bitmap logoBitmap) {
        int qrBitmapWidth = qrBitmap.getWidth();
        int qrBitmapHeight = qrBitmap.getHeight();
        int logoBitmapWidth = logoBitmap.getWidth();
        int logoBitmapHeight = logoBitmap.getHeight();
        Bitmap blankBitmap = Bitmap.createBitmap(qrBitmapWidth, qrBitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(blankBitmap);
        canvas.drawBitmap(qrBitmap, 0, 0, null);
        //Canvas.ALL_SAVE_FLAG
        canvas.save();
        float scaleSize = 1.0f;
        while ((logoBitmapWidth / scaleSize) > (qrBitmapWidth / 5) || (logoBitmapHeight / scaleSize) > (qrBitmapHeight / 5)) {
            scaleSize *= 2;
        }
        float sx = 1.0f / scaleSize;
        canvas.scale(sx, sx, qrBitmapWidth / 2, qrBitmapHeight / 2);
        canvas.drawBitmap(logoBitmap, (qrBitmapWidth - logoBitmapWidth) / 2, (qrBitmapHeight - logoBitmapHeight) / 2, null);
        canvas.restore();
        return blankBitmap;
    }

    /**
     * 向服务器发送使用地铁票请求
     * @param goodsid
     */
    public void useTicketRequest(final String goodsid) {
        //请求地址
        String url = "http://HaerbinStation.free.idcfengye.com/AppHarbinMetro/UseTicketServlet";
        String tag = "UseTicket";

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
                            if (result.equals("success")) {
                                //做自己的使用成功操作，如页面跳转
                                Toast.makeText(GoodsInfoActivity.this, "使用成功！", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                //做自己的使用失败操作，如Toast提示
                                Toast.makeText(GoodsInfoActivity.this, "使用失败！", Toast.LENGTH_SHORT).show();
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
                params.put("goodsid", goodsid);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

    /**
     * 向服务器发送退票请求
     * @param goodsid
     */
    public void quitTicketRequest(final String alipayid,final String ticketprice, final String paypassword, final String goodsid) {
        //请求地址
        String url = "http://HaerbinStation.free.idcfengye.com/AppHarbinMetro/QuitTicketServlet";
        String tag = "QuitTicket";

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
                            if (result.equals("success")) {
                                //做自己的退票成功操作，如页面跳转
                                Toast.makeText(GoodsInfoActivity.this, "退票成功！", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                //做自己的退票失败操作，如Toast提示
                                Toast.makeText(GoodsInfoActivity.this, "退票失败！", Toast.LENGTH_SHORT).show();
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
                params.put("alipayid",alipayid);
                params.put("ticketprice",ticketprice);
                params.put("paypassword",paypassword);
                params.put("goodsid", goodsid);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

}
