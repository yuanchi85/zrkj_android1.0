package zrkj.project.zrkj;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.finddreams.baselib.R;
import com.finddreams.baselib.base.BaseActivity;
import com.finddreams.baselib.utils.DeviceInfoUtil;
import com.finddreams.baselib.utils.StringUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnTouch;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import zrkj.project.entity.CmsAssembly;
import zrkj.project.toast.CommomDialog;
import zrkj.project.util.JsonUtil;
import zrkj.project.util.Result;

@ContentView(R.layout.rukuzhijian_activity)
public class RuKuZhiJian_Activity extends BaseActivity {
    @ViewInject(R.id.saomiaotiaoma)
    private EditText saomiaotiaoma;
    @ViewInject(R.id.chanxian)
    private EditText chanxian;
    @ViewInject(R.id.pihao)
    private EditText pihao;
    @ViewInject(R.id.chanpinbianma)
    private EditText chanpinbianma;
    @ViewInject(R.id.chanpingmingcheng)
    private EditText chanpingmingcheng;
    @ViewInject(R.id.guigexinhao)
    private EditText guigexinhao;
    @ViewInject(R.id.jiliangdanwei)
    private EditText jiliangdanwei;
    @ViewInject(R.id.tuopantiaoma)
    private EditText tuopantiaoma;
    @ViewInject(R.id.rukushuliang)
    private EditText rukushuliang;
    @ViewInject(R.id.fuheshuliang)
    private EditText fuheshuliang;
    /*@ViewInject(R.id.hegeshuliang)
    private EditText hegeshuliang;
    @ViewInject(R.id.buliangshuliang)
    private EditText buliangshuliang;*/
    @ViewInject(R.id.beizhu)
    private EditText beizhu;

    CmsAssembly cmsAssembly = new CmsAssembly();

    @Override
    protected void initData() {

    }


    /**
     * 初始化所有控件
     */
    @Override
    protected void initView() {
        topTitle.mTvTitle.setText("入库质检");
        topTitle.mTvRight.setText("");
    }



    @OnTouch({R.id.login})
    private boolean login(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            switch (v.getId()) {
                case R.id.login:
                    //分包
                    if (beforeLogin()) {
                        CommomDialog commomDialog = new CommomDialog(RuKuZhiJian_Activity.this, R.style.dialog, "是否确定提交？", new CommomDialog.OnCloseListener() {
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

    @Override
    protected void onScan(String barcodeStr) {
            queryAllProductionLine(saomiaotiaoma.getText().toString());
    }

    private boolean beforeLogin() {
        if (saomiaotiaoma.getText().toString().equals("")) {
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
        Gson gson = new Gson();
        if (posi == 1) {

            cmsAssembly = gson.fromJson(ajax.getResult().toString(), CmsAssembly.class);
            chanxian.setText(cmsAssembly.getProductionLineCode());
            pihao.setText(cmsAssembly.getBatch());
            chanpinbianma.setText(cmsAssembly.getProductCode());
            chanpingmingcheng.setText(cmsAssembly.getProductName());
            guigexinhao.setText(cmsAssembly.getSpecificationModel());
            jiliangdanwei.setText(cmsAssembly.getUnit());
            rukushuliang.setText(cmsAssembly.getQuantity().stripTrailingZeros().toPlainString());
            fuheshuliang.setText(cmsAssembly.getReviewQuantity().stripTrailingZeros().toPlainString());
        }
    }

    private void queryAllProductionLine(String barCode) {
        Gson gson = new Gson();
        Map<String, Object> ms = new HashMap<String, Object>();
        ms.put("barCode", barCode);
        ms.put("type", "质检");
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        request(gson.toJson(ms), "cms/cmsAssembly/cmsAssembly/queryCmsAssembly", "正在查询...", 1);
    }

    private void InertCmsAssembly() {
        /*cmsAssembly.setQualifiedQuantity(new BigDecimal(hegeshuliang.getText().toString()));
        cmsAssembly.setUnqualifiedQuantity(new BigDecimal(buliangshuliang.getText().toString()));*/
        cmsAssembly.setRemarks(beizhu.getText().toString());
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        Map<String, Object> ms = null;
        try {
            ms = JsonUtil.objectToMap(cmsAssembly);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        request(gson.toJson(ms), "cms/cmsAssembly/cmsAssembly/addpdate", "正在设置...", 2);
    }

}
