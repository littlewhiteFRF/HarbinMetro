package com.frf.harbinmetro.payinfo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import com.frf.harbinmetro.goodsinfo.GoodsInfoActivity;
import com.frf.harbinmetro.login.LoginActivity;
import com.frf.harbinmetro.login.model.Passenger;
import com.frf.harbinmetro.main.MainActivity;
import com.frf.harbinmetro.util.MyApplication;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PayInfoActivity extends AppCompatActivity {

    private Toolbar pay_info_activity_toolbar;//顶部导航栏

    private TextView money;

    private CardView goodsInfo;

    private ImageView qrcode;//二维码

    private String paymoney;
    private String goodsid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_info);

        pay_info_activity_toolbar = findViewById(R.id.pay_info_activity_toolbar);
        pay_info_activity_toolbar.setTitle("付款结果");
        pay_info_activity_toolbar.setLogo(R.drawable.ic_action_pay);
        setSupportActionBar(pay_info_activity_toolbar);
        ActionBar actionBar = getSupportActionBar();//得到ActionBar的实例
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//通过该方法让导航按钮显示出来
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);//通过该方法设置一个导航按钮图标
        }

        paymoney = getIntent().getStringExtra("paymoney");
        goodsid = getIntent().getStringExtra("goodsid");

        money = findViewById(R.id.pay_info_activity_money);
        money.setText(paymoney+".00元");

        goodsInfo = findViewById(R.id.pay_info_activity_cardview2);
        goodsInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PayInfoActivity.this,"你点击了账单详情",Toast.LENGTH_SHORT).show();
                getOneGoodsRequest(goodsid);
            }
        });

        qrcode = findViewById(R.id.pay_info_activity_qrcode);
        Bitmap qrBitmap = generateBitmap(goodsid,400, 400);
        Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.harbin_metro_logo_round);
        Bitmap bitmap = addLogo(qrBitmap, logoBitmap);
        qrcode.setImageBitmap(bitmap);
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

    public void getOneGoodsRequest(final String goodsid) {
        //请求地址
        String url = "http://HaerbinStation.free.idcfengye.com/AppHarbinMetro/GetOneGoodsServlet";
        String tag = "GetOneGoods";

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
                                //登陆验证成功后，继续解析jsonObject对象，获得该用户的所有信息
                                Goods goods = new Goods();
                                goods.setGoodsid(jsonObject.getString("goodsid"));
                                goods.setOfuserid(jsonObject.getString("ofuserid"));
                                goods.setLineid(jsonObject.getString("lineid"));
                                goods.setDatetime(jsonObject.getString("datetime"));
                                goods.setState(jsonObject.getString("state"));
                                goods.setStartstation(jsonObject.getString("startStation"));
                                goods.setFinishstation(jsonObject.getString("endStation"));
                                goods.setTicketprice(jsonObject.getString("ticketprice"));

                                //做自己的查询成功操作，如页面跳转
                                Toast.makeText(PayInfoActivity.this, "获得订单详情成功！", Toast.LENGTH_SHORT).show();
                                //查询成功后，结束支付详情页面
                                finish();
                                //查询成功后，跳转到订单详情
                                Intent intent = new Intent(PayInfoActivity.this, GoodsInfoActivity.class);
                                intent.putExtra("goodsinfo",goods);
                                startActivity(intent);
                            } else {
                                //做自己的查询失败操作，如Toast提示
                                Toast.makeText(PayInfoActivity.this, "获得订单详情失败！", Toast.LENGTH_SHORT).show();
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
}
