package com.fangzuo.assist.Activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fangzuo.assist.Adapter.BTAdapter;
import com.fangzuo.assist.Beans.BlueToothBean;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.MathUtil;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.orhanobut.hawk.Hawk;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zpSDK.zpSDK.zpBluetoothPrinter;

public class PrintOutTestActivity extends AppCompatActivity {
    @BindView(R.id.tv_bluetooth)
    TextView tvBluetooth;
    @BindView(R.id.ed_print_num)
    EditText edPriintNum;
    @BindView(R.id.ry_bluetooth)
    EasyRecyclerView ryBl;
    @BindView(R.id.buttonPrint)
    Button btnPrint;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.btn_back)
    RelativeLayout btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    private BroadcastReceiver broadcastReceiver = null;
    private IntentFilter intentFilter = null;
    PrintOutTestActivity mActivity;
    private static String TAG = "SearchBTActivity";
//    zpBluetoothPrinter zpSDK;
    private BTAdapter btAdapter;
    //    boolean isOk = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_out_test);
        ButterKnife.bind(this);

        mActivity = this;
        ryBl.setAdapter(btAdapter = new BTAdapter(this));
        ryBl.setLayoutManager(new LinearLayoutManager(this));
//        zpSDK = new zpBluetoothPrinter(this);
        edPriintNum.setText(Hawk.get(Config.PrintNum,1)+"");
        tvTitle.setText("蓝牙配置");
        btAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Hawk.put(Config.OBJ_BLUETOOTH, btAdapter.getAllData().get(position));
                Print1(btAdapter.getAllData().get(position).getAddress());
//                es.submit(new TaskOpen(mBt, btAdapter.getAllData().get(position).getAddress(), mActivity));
                Toast.makeText(mActivity, "正在配置" + btAdapter.getAllData().get(position).getAddress(), Toast.LENGTH_SHORT).show();
            }
        });

        ryBl.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                btAdapter.removeAll();
                //检测是否开启蓝牙
                BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
                if (null == adapter) {
                    finish();
                    ryBl.setRefreshing(false);
                    return;
                }
                if (!adapter.isEnabled()) {
                    if (adapter.enable()) {
                        while (!adapter.isEnabled())
                            ;
                        Log.v(TAG, "Enable BluetoothAdapter");
                    } else {
                        finish();
                        ryBl.setRefreshing(false);
                    }
                }

                adapter.cancelDiscovery();
                adapter.startDiscovery();
                ryBl.setRefreshing(false);
//                initBroadcast();
            }
        });

//        /* 启动WIFI */
//        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        switch (wifiManager.getWifiState()) {
//            case WifiManager.WIFI_STATE_DISABLED:
//                wifiManager.setWifiEnabled(true);
//                break;
//            default:
//                break;
//        }

//        /* 启动蓝牙 */
//        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
//        if (null != adapter) {
//            if (!adapter.isEnabled()) {
//                if (!adapter.enable()) {
//                    finish();
//                    return;
//                }
//            }
//        }
//        if (!adapter.isEnabled()) {
//            if (adapter.enable()) {
//                while (!adapter.isEnabled())
//                    ;
//                Log.v(TAG, "Enable BluetoothAdapter");
//            } else {
//                finish();
//            }
//        }
//
//        adapter.cancelDiscovery();
//        adapter.startDiscovery();
        initBroadcast();

        BlueToothBean bean = Hawk.get(Config.OBJ_BLUETOOTH, new BlueToothBean("", ""));
//        if (bean.address.equals("")){
//            tvRight.setText("连接打印机错误");
//            tvRight.setTextColor(Color.RED);
//        }else{
//            if(!zpSDK.connect(bean.address))
//            {
//                tvRight.setText("连接打印机错误");
//                tvRight.setTextColor(Color.RED);
//                return;
//            }else{
//                tvRight.setText("打印机就绪");
//                tvRight.setTextColor(Color.BLACK);
//            }
//        }
        tvBluetooth.setText("蓝牙名称："+"\n" +getString(R.string.name) + bean.getName() + "\n"+"蓝牙地址：" + bean.getAddress());

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initBroadcast() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    if (device == null) {
                        return;
                    }

                    final String address = device.getAddress();
                    Log.e("获取address：", address);
                    String name = device.getName();
                    if (name == null) {
                        name = "BT";
                    } else if (name.equals(address)) {
                        name = "BT";
                    }
                    btAdapter.add(new BlueToothBean(name, address));
//                    Hawk.put(Config.S_BLUETOOTH, address);
//                    if ("dc:0d:30:0e:3e:0f".equals(address) || "DC:0D:30:0E:3E:0F".equals(address)) {
//                        Log.e("蓝牙：", "查到打印机");
//                        Toast.makeText(mActivity, "Connecting...", Toast.LENGTH_SHORT).show();
//                        btnDisconnect.setEnabled(false);
//                        btnPrint.setEnabled(false);
////                        es.submit(new TaskOpen(mBt,address, mActivity));
//                        es.submit(new TaskOpen(mBt, address, mActivity));
//                        //es.submit(new TaskTest(mPos, mBt, address, mActivity));
//                    }
                    ryBl.setRefreshing(true);
                } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED
                        .equals(action)) {
                    Lg.e("开始扫描...");
                    ryBl.setRefreshing(true);
                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
                        .equals(action)) {
                    Lg.e("扫描结束...");
                    ryBl.setRefreshing(false);
                }

            }

        };
        intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void uninitBroadcast() {
        if (broadcastReceiver != null)
            unregisterReceiver(broadcastReceiver);
    }

    @OnClick({R.id.buttonPrint})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.buttonPrint:
                Lg.e("蓝牙：", Hawk.get(Config.OBJ_BLUETOOTH, new BlueToothBean("", "")));
                Print1((Hawk.get(Config.OBJ_BLUETOOTH, new BlueToothBean("", ""))).getAddress());
                break;
        }
    }


    public void Print1(String BDAddress) {
        zpBluetoothPrinter zpSDK = new zpBluetoothPrinter(mActivity);

//        if (!isOk) {
        if (!zpSDK.connect(BDAddress)) {
            Toast.makeText(this, "连接失败------", Toast.LENGTH_LONG).show();
            return;
        } else {
            BlueToothBean bean = Hawk.get(Config.OBJ_BLUETOOTH, new BlueToothBean("", ""));
            tvBluetooth.setText("已配置的打印机\n" + "名称：" + bean.getName() + "\n地址：" + bean.getAddress());
            Toast.makeText(this, "连接成功------", Toast.LENGTH_LONG).show();
        }
//        }


//        Resources res = getResources();
//        @SuppressLint("ResourceType") InputStream is = res.openRawResource(R.mipmap.logo);
//        BitmapDrawable bmpDraw = new BitmapDrawable(is);
//        Bitmap bmp = bmpDraw.getBitmap();

//        for (int i = 0; i < 6; i++) {
        zpSDK.pageSetup(850, 420);
        zpSDK.drawText(0, 100, "测试-------------111222333344-----55666777881234555555444444888888", 2, 0, 0, false, false);
        zpSDK.drawText(0, 400, "测试-------------111222333344-----55666777881234", 2, 0, 0, false, false);
//        }
        zpSDK.drawBarCode(200, 200, "12345678901234567", 128, false, 3, 100);
//              zpSDK.drawGraphic(90, 70, 0, 0, bmp);
//        zpSDK.drawQrCode(350, 48, "111111111", 0, 6, 0);
        zpSDK.drawQrCode(100, 100, "PO1912001", 0, 6, 0);
        zpSDK.print(0, 1);
        zpSDK.disconnect();
//            zpSDK.drawText(90, 48+100, "400-8800-", 2
//                    , 0, 0, false, false);
//            zpSDK.drawText(100, 48+100+56, "株洲      张贺", 4, 0, 0, false, false);
//            zpSDK.drawText(250, 48+100+56+56, "经由  株洲", 2, 0, 0, false, false);

//            zpSDK.drawText(100, 48+100+56+56+80, "2015110101079-01-01   广州", 2, 0, 0, false, false);
//            zpSDK.drawText(100, 48+100+56+56+80+80, "2015-11-01  23:00    卡班", 2, 0, 0, false, false);

//            zpSDK.drawBarCode(124,48+100+56+56+80+80+80 , "12345678901234567", 128, false, 3, 60);
//            zpSDK.print(0, 0);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Hawk.put(Config.PrintNum, MathUtil.toInt(edPriintNum.getText().toString()));
//        try{
//            zpSDK.disconnect();
//        }catch (Exception e){}
        uninitBroadcast();
    }
}
