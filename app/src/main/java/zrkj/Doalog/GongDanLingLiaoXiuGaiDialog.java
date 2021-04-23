package zrkj.Doalog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.finddreams.baselib.R;

import java.math.BigDecimal;

import zrkj.project.entity.CmsAssembly;
import zrkj.project.zrkj.XiaoHuo_Activity;


/**
 * 时间：2019/7/19
 * 项目名：zrkj_tm_pda
 * 包名：com.zrkj.Base
 * 类名：Activity
 * 作者：杨亚明
 **/

public class GongDanLingLiaoXiuGaiDialog extends Dialog implements View.OnClickListener {
    private TextView contentTxt;
    private TextView titleTxt;
    private TextView submitTxt;
    private TextView cancelTxt;

    private TextView pinghao, mingcheng, xunlingshuliang, kelingshuliang;
    private EditText shuliang;
    private Context mContext;
    private String content;
    private OnCloseListener listener;
    private String positiveName;
    private String negativeName;
    private String title;
    private ImageView view;
    private String shishishuliang;

    private BigDecimal bigmal = new BigDecimal("0");


    public GongDanLingLiaoXiuGaiDialog(Context context) {
        super(context);
        this.mContext = context;
    }


    public GongDanLingLiaoXiuGaiDialog(Context context, int themeResId, String content) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
    }


    public GongDanLingLiaoXiuGaiDialog(Context context, int themeResId, String content, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
        this.listener = listener;


    }


    protected GongDanLingLiaoXiuGaiDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }


    public GongDanLingLiaoXiuGaiDialog setTitle(String title) {
        this.title = title;
        return this;
    }


    public GongDanLingLiaoXiuGaiDialog setPositiveButton(String name) {
        this.positiveName = name;
        return this;
    }


    public GongDanLingLiaoXiuGaiDialog setNegativeButton(String name) {
        this.negativeName = name;
        return this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gongdanlingliaoxiugaishuliang_common_layout);
        setCanceledOnTouchOutside(false);
        initView();
        viewClick();
    }


    public void viewClick() {
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shuliang.setText("");
            }
        });
    }


    /**
     *
     */
    private void initView() {
        titleTxt = (TextView) findViewById(R.id.title);
        submitTxt = (TextView) findViewById(R.id.submit);
        submitTxt.setOnClickListener(this);
        cancelTxt = (TextView) findViewById(R.id.cancel);
        cancelTxt.setOnClickListener(this);
        view = (ImageView) findViewById(R.id.ImageView1);

        pinghao = (TextView) findViewById(R.id.wuliaobianhao);
        mingcheng = (TextView) findViewById(R.id.wuliaomingcheng);
        xunlingshuliang = (TextView) findViewById(R.id.xunlingshuliang);
        kelingshuliang = (TextView) findViewById(R.id.kelingshuliang);
        shuliang = (EditText) findViewById(R.id.chanpintiaoma);


        if (XiaoHuo_Activity.cmsAssembly != null) {
            CmsAssembly cmsAssemblys = XiaoHuo_Activity.cmsAssembly;
            pinghao.setText(cmsAssemblys.getProductCode());
            mingcheng.setText(cmsAssemblys.getProductName());
            xunlingshuliang.setText(cmsAssemblys.getZongshu().stripTrailingZeros().toPlainString());
            kelingshuliang.setText(cmsAssemblys.getQualifiedQuantity().stripTrailingZeros().toPlainString());
            shuliang.setText(cmsAssemblys.getQualifiedQuantity().stripTrailingZeros().toPlainString());
            bigmal = cmsAssemblys.getQualifiedQuantity();
            if (cmsAssemblys.getZongshu().intValue() < cmsAssemblys.getQualifiedQuantity().intValue()) {
                shuliang.setText(cmsAssemblys.getZongshu().stripTrailingZeros().toPlainString());
                bigmal = cmsAssemblys.getZongshu();
            }
        }


    }

    public String getshuliang() {
        return shuliang.getText().toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                BigDecimal big = new BigDecimal(shuliang.getText().toString());
                if (big.intValue() > bigmal.intValue()) {
                    Toast.makeText(mContext, "数量超出", Toast.LENGTH_LONG).show();
                } else {
                    if (listener != null) {
                        listener.onClick(this, false);
                    }
                    this.dismiss();
                }

                break;
            case R.id.submit:
                if (listener != null) {
                    listener.onClick(this, true);
                }
                this.dismiss();
                break;
        }
    }


    public interface OnCloseListener {
        void onClick(Dialog dialog1, boolean confirm);

    }
}
