package zrkj.project.zrkj;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.finddreams.baselib.R;
import com.finddreams.baselib.base.BaseActivity;
import com.finddreams.baselib.utils.DeviceInfoUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnTouch;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import zrkj.project.entity.CmsAssembly;
import zrkj.project.entity.CmsCurrentProducts;
import zrkj.project.entity.CmsProduct;
import zrkj.project.toast.CommomDialog;
import zrkj.project.util.Constant;
import zrkj.project.util.JsonUtil;
import zrkj.project.util.Result;

@ContentView(R.layout.fenbao_activity)
public class FenTuoRkActivity extends BaseActivity {
    @ViewInject(R.id.saomiaotiaoma)
    private EditText saomiaotiaoma;
    @ViewInject(R.id.dangqianchanxian)
    private EditText dangqianchanxian;
    @ViewInject(R.id.dangqianpihao)
    private EditText dangqianpihao;
    @ViewInject(R.id.chanpinbianma)
    private EditText chanpinbianma;
    @ViewInject(R.id.chanpingmingcheng)
    private EditText chanpingmingcheng;
    @ViewInject(R.id.guigexinghao)
    private EditText guigexinghao;
    @ViewInject(R.id.jiliangdanwei)
    private EditText jiliangdanwei;
    @ViewInject(R.id.tuopantiaoma)
    private EditText tuopantiaoma;
    @ViewInject(R.id.shuliang)
    private EditText shuliang;

    @Override
    protected void initData() {

    }

    /**
     * @param barcodeStr
     */
    @Override
    protected void onScan(String barcodeStr) {
        if (barcodeStr.startsWith("T") && barcodeStr.endsWith("T")) {
            tuopantiaoma.setText(trimFirstAndLastChar(barcodeStr,"T"));
        } else {
            saomiaotiaoma.setText(barcodeStr);
            queryCmsProduct(barcodeStr);
        }
    }

    /**
     * 初始化所有控件
     */
    @Override
    protected void initView() {
        topTitle.mTvTitle.setText("分托入库");
        topTitle.mTvRight.setText("");
    }

    @OnTouch({R.id.login})
    private boolean login(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            switch (v.getId()) {
                case R.id.login:
                    //分包
                    if (beforeLogin()) {
                        CommomDialog commomDialog = new CommomDialog(FenTuoRkActivity.this, R.style.dialog, "是否确定提交？", new CommomDialog.OnCloseListener() {
                            @Override
                            public void onClick(Dialog dialog1, boolean confirm) {
                                if (confirm == false) {
                                    InertCmsAssembly();
                                }
                            }
                        });
                        commomDialog.setTitle("警告").show();
                    }
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    private void InertCmsAssembly() {
        CmsAssembly cmsAssembly = new CmsAssembly();
        cmsAssembly.setProductCode(chanpinbianma.getText().toString());
        cmsAssembly.setProductName(chanpingmingcheng.getText().toString());
        cmsAssembly.setSpecificationModel(guigexinghao.getText().toString());
        cmsAssembly.setUnit(jiliangdanwei.getText().toString());
        cmsAssembly.setBatch(dangqianpihao.getText().toString());
        cmsAssembly.setPalletCode(tuopantiaoma.getText().toString());
        cmsAssembly.setQuantity(new BigDecimal(shuliang.getText().toString()));
        cmsAssembly.setProductionLineCode(dangqianchanxian.getText().toString());
        cmsAssembly.setBarCode(saomiaotiaoma.getText().toString());
        cmsAssembly.setType(Constant.TYPE_POINTS);
        Gson gson = new Gson();
        Map<String, Object> ms = null;
        try {
            ms = JsonUtil.objectToMap(cmsAssembly);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        request(gson.toJson(ms), "cms/cmsAssembly/cmsAssembly/add", "正在设置...", 2);
    }


    private boolean beforeLogin() {
        if (dangqianchanxian.getText().toString().equals("")) {
            showCustomToast("请扫描有效产品条码");
            return false;
        }
        if (tuopantiaoma.getText().toString().equals("")) {
            showCustomToast("请扫描托盘条码");
            return false;
        }
        return true;
    }


    @Override
    protected void requestCallBack(Result ajax, int posi) {
        if (posi == 1) {
            Gson gson = new Gson();
            CmsProduct cmsProduct = gson.fromJson(ajax.getResult().toString(), CmsProduct.class);
            CmsCurrentProducts cmsCurrentProducts = gson.fromJson(ajax.getResult().toString(), CmsCurrentProducts.class);
            chanpinbianma.setText(cmsProduct.getProductCode());
            chanpingmingcheng.setText(cmsProduct.getProductName());
            guigexinghao.setText(cmsProduct.getSpecificationModel());
            jiliangdanwei.setText(cmsProduct.getUnit());
            dangqianchanxian.setText(cmsCurrentProducts.getProductionLineCode());
            dangqianpihao.setText(cmsCurrentProducts.getBatch());
            shuliang.setText(cmsProduct.getPalletsNumber().stripTrailingZeros().toPlainString());
            return;
        }
        if(posi==2){
            if(ajax.getMessage().contains("剩余可入库数量")){
                shuliang.setText(ajax.getResult().toString());
                CommomDialog commomDialog = new CommomDialog(FenTuoRkActivity.this, R.style.dialog, ajax.getMessage()+"是否继续？", new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog1, boolean confirm) {
                        if (confirm == false) {
                            InertCmsAssembly();
                        }
                    }
                });
                commomDialog.setTitle("警告").show();

            }

        }
    }

    private void queryCmsProduct(String barCode) {
        Gson gson = new Gson();
        Map<String, Object> ms = new HashMap<String, Object>();
        ms.put("barCode", barCode);
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        request(gson.toJson(ms), "cms/cmsProduct/cmsProduct/queryCmsProducts", "正在查询...", 1);
    }
}
