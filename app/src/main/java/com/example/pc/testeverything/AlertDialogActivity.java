package com.example.pc.testeverything;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by PC on 2018/5/2.
 */

public class AlertDialogActivity extends Activity implements View.OnClickListener {

    private TextView text1, text2;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_dialog);
        mContext = this;
        initView();
        Intent startIntent = new Intent(this, MyService.class);
        startService(startIntent); // 启动服务

        String PACKAGE_NAME = "com.tiidian.seed";
        String SERVICE_NAME = "com.tiidian.seed.service.SeedService";//com.tiidian.seed.service.SeedService
        String ACTION_INSTALL_APK = "ACTION.INSTALL_APK";
        String INSTALL_APK_EXTRA_PATH = "apk_file";
        String INSTALL_APK_EXTRA_START = "start_app";

        Intent i = new Intent();
        i.setComponent(new ComponentName(PACKAGE_NAME, SERVICE_NAME));
        i.setAction(ACTION_INSTALL_APK);
        i.putExtra(INSTALL_APK_EXTRA_PATH, "");
        i.putExtra(INSTALL_APK_EXTRA_START, true);
        this.getApplicationContext().startService(i);

    }

    private void initView() {
        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);

        text1.setOnClickListener(this);
        text2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text1:
                dialogShow1();
                break;
            case R.id.text2:
                dialogShow2();
                break;

            default:
                break;
        }
    }

    private void dialogShow1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("温馨提示");
        builder.setIcon(R.drawable.ic_launcher);
        builder.setMessage("原理是基本");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(mContext, "no", Toast.LENGTH_LONG).show();
            }
        });
        builder.setPositiveButton("立即更新",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(mContext, "ok", Toast.LENGTH_LONG).show();
                    }
                });
        Dialog dialog = builder.create();
        dialog.show();
    }

    /**
     * 自定义布局
     * setView()只会覆盖AlertDialog的Title与Button之间的那部分，而setContentView()则会覆盖全部，
     * setContentView()必须放在show()的后面
     */
    private void dialogShow2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.update_manage_dialog, null);
        TextView content = (TextView) v.findViewById(R.id.dialog_content);
        Button btn_sure = (Button) v.findViewById(R.id.dialog_btn_sure);
        Button btn_cancel = (Button) v.findViewById(R.id.dialog_btn_cancel);
        //builer.setView(v);//这里如果使用builer.setView(v)，自定义布局只会覆盖title和button之间的那部分
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
        //dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        btn_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(mContext, "ok", Toast.LENGTH_LONG).show();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                Toast.makeText(mContext, "no", Toast.LENGTH_LONG).show();
            }
        });
    }

}
