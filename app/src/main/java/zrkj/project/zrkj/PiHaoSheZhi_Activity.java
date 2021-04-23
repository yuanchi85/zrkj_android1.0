package zrkj.project.zrkj;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.service.autofill.AutofillService;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStructure;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.finddreams.baselib.R;
import com.finddreams.baselib.base.BaseActivity;
import com.finddreams.baselib.base.MyBaseAdapter;
import com.finddreams.baselib.utils.ActivityUtil;
import com.finddreams.baselib.utils.DeviceInfoUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnTouch;
import com.nhaarman.listviewanimations.ArrayAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zrkj.project.entity.CmsCurrentProducts;
import zrkj.project.entity.CmsCurrentProductsHis;
import zrkj.project.entity.CmsProduct;
import zrkj.project.entity.CmszdEntity;
import zrkj.project.util.JsonUtil;
import zrkj.project.util.Result;

@ContentView(R.layout.pihaoshezi_activity)
public class PiHaoSheZhi_Activity extends BaseActivity {
    @ViewInject(R.id.spinner1)
    private Spinner m_spiModel;
    @ViewInject(R.id.chanpinbianma)
    private TextView chanpinbianma;
    @ViewInject(R.id.chanpingmingcheng)
    private TextView chanpingmingcheng;
    @ViewInject(R.id.guigexinghao)
    private TextView guigexinghao;
    @ViewInject(R.id.jiliangdanwei)
    private TextView jiliangdanwei;
    @ViewInject(R.id.dangqianpihao)
    private EditText dangqianpihao;
    @Override
    protected void initData() {

    }

    @Override
    protected void onScan(String barcodeStr) {
        queryCmsProduct(barcodeStr);
    }

    /**
     * 初始化所有控件
     */
    @Override
    protected void initView() {
        topTitle.mTvTitle.setText("批号设置");
        topTitle.mTvRight.setText("");
        queryAllProductionLine();
    }

    @Override
    protected void requestCallBack(Result ajax, int posi) {
        Gson gson = new Gson();
        if (posi == 1) {
            List<Map<String, Object>> list = gson.fromJson(ajax.getResult().toString(), List.class);
            m_spiModel.setAdapter(new MyBaseAdapter<Map<String, Object>, ListView>(this.context, list) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    ViewGroup layout = null;
                    if (convertView == null) {
                        layout = (ViewGroup)LayoutInflater.from(context).inflate(R.layout.zdyspinner, parent, false);
                    } else {
                        layout = (ViewGroup)convertView;
                    }
                    Map<String, Object> mbean = this.list.get(position);
                    //int[] colors = { Color.parseColor("#79B9B1"),Color.parseColor("#90D9FF") };// RGB颜色
                    ((TextView)layout.findViewById(R.id.textView1)).setText(mbean.get("text").toString()+"_"+mbean.get("value").toString());
//                    ((TextView)layout.findViewById(R.id.textView2)).setText(mbean.get("value").toString());
//                    layout.setBackgroundColor(colors[position % 2]);// 每隔item之间颜色不同
                    if (releativeOptionIndex == position) layout.setBackgroundColor(Color.YELLOW);
                    return layout;
                }
            });
            return;
        }
        if(posi==2){
            CmsProduct cmsProduct = gson.fromJson(ajax.getResult().toString(), CmsProduct.class);
            chanpinbianma.setText(cmsProduct.getProductCode());
            chanpingmingcheng.setText(cmsProduct.getProductName());
            guigexinghao.setText(cmsProduct.getSpecificationModel());
            jiliangdanwei.setText(cmsProduct.getUnit());
            return;
        }
    }

   private boolean beforeLogin() {
        if (chanpinbianma.getText().toString().equals("")) {
            showCustomToast("请扫描有效产品条码");
            return false;
        }
        if (dangqianpihao.getText().toString().equals("")) {
            showCustomToast("请输入批号");
            return false;
        }
        return true;
    }

    @OnTouch({R.id.login})
    private boolean login(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.login:
                //分包
                if (beforeLogin()){
                    CmsCurrentProducts cmszdEntity = new CmsCurrentProducts();
                    Map<String, Object> map =   (Map<String, Object>)m_spiModel.getSelectedItem();
                    cmszdEntity.setProductionLineCode(map.get("value").toString());
                    cmszdEntity.setProductCode(chanpinbianma.getText().toString());
                    cmszdEntity.setBatch(dangqianpihao.getText().toString());
                    InertCmszd(cmszdEntity);
                }
                break;
            default:
                break;
        }
        return true;
    }

    private void InertCmszd(CmsCurrentProducts cmszdEntity) {
        Gson gson = new Gson();
        Map<String, Object> ms = null;
        try {
            ms = JsonUtil.objectToMap(cmszdEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        request(gson.toJson(ms), "cms/cmsCurrentProducts/cmsCurrentProducts/add", "正在设置...", 3);
    }

    private void queryAllProductionLine() {
        Gson gson = new Gson();
        Map<String, Object> ms = new HashMap<String, Object>();
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        request(gson.toJson(ms), "cms/cmsProductionLine/cmsProductionLine/queryAllProductionLine", "正在查询...", 1);
    }

    private void queryCmsProduct(String barCode) {
        Gson gson = new Gson();
        Map<String, Object> ms = new HashMap<String, Object>();
        ms.put("barCode", barCode);
        ms.put("device", DeviceInfoUtil.getOnlyID(this));
        request(gson.toJson(ms), "cms/cmsProduct/cmsProduct/queryCmsProduct", "正在查询...", 2);
    }
}
