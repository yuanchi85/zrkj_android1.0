package zrkj.project.zrkj;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
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

@ContentView(R.layout.rukufuhe_activity)
public class RuKuFuHe_Activity extends BaseActivity {
	@ViewInject(R.id.saomiaotiaoma)
	private EditText saomiaotiaoma;
	@ViewInject(R.id.chanxian)
	private EditText chanxian;
	@ViewInject(R.id.pihao)
	private EditText pihao;
	@ViewInject(R.id.fuhepihao)
	private EditText fuhepihao;
	@ViewInject(R.id.chanpinbianhao)
	private EditText chanpinbianhao;
	@ViewInject(R.id.chanpinmingcheng)
	private EditText chanpinmingcheng;
	@ViewInject(R.id.guigexinghao)
	private EditText guigexinghao;
	@ViewInject(R.id.jiliangdanwei)
	private EditText jiliangdanwei;
	@ViewInject(R.id.tuopantiaoma)
	private EditText tuopantiaoma;
	@ViewInject(R.id.rukushuliang)
	private EditText rukushuliang;
	@ViewInject(R.id.fuheshuliang)
	private EditText fuheshuliang;
	CmsAssembly cmsAssembly =  new CmsAssembly();
	@Override
	protected void initData() {

	}

	@Override
	protected void onScan(String barcodeStr) {
		if(barcodeStr.startsWith("T") && barcodeStr.endsWith("T")){
			tuopantiaoma.setText(trimFirstAndLastChar(barcodeStr,"T"));
			if(!StringUtil.isNotEmpty(saomiaotiaoma.getText().toString())){
				showMsg("请扫描产品条码");
				return;
			}
			queryAllProductionLine(saomiaotiaoma.getText().toString(),tuopantiaoma.getText().toString());
		}else{
			saomiaotiaoma.setText(barcodeStr);
			if(!StringUtil.isNotEmpty(tuopantiaoma.getText().toString())){
				showMsg("请扫描托盘条码");
				return;
			}
			queryAllProductionLine(saomiaotiaoma.getText().toString(),tuopantiaoma.getText().toString());
		}
	}

	/**
	 * 初始化所有控件
	 */
	@Override
	protected void initView() {
		topTitle.mTvTitle.setText("入库复核");
		topTitle.mTvRight.setText("");
	}

	@OnTouch({R.id.cancel})
	private boolean login(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			switch (v.getId()) {
				case R.id.cancel:
					//分包
					if (beforeLogin()) {
						CommomDialog commomDialog = new CommomDialog(RuKuFuHe_Activity.this, R.style.dialog, "是否确定提交？", new CommomDialog.OnCloseListener() {
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

	private boolean beforeLogin() {
		if (saomiaotiaoma.getText().toString().equals("")) {
			showCustomToast("请扫描有效产品条码");
			return false;
		}
		if (tuopantiaoma.getText().toString().equals("")) {
			showCustomToast("请扫描托盘条码");
			return false;
		}
		if (fuhepihao.getText().toString().equals("")) {
			showCustomToast("请输入复核批号");
			return false;
		}
		if (fuheshuliang.getText().toString().equals("")) {
			showCustomToast("请输入复核数量");
			return false;
		}

		return true;
	}

	@Override
	protected void requestCallBack(Result ajax, int posi) {
		Gson gson = new Gson();
		if(posi==1){
			cmsAssembly = gson.fromJson(ajax.getResult().toString(), CmsAssembly.class);
			chanxian.setText(cmsAssembly.getProductionLineCode());
			pihao.setText(cmsAssembly.getBatch());
			chanpinbianhao.setText(cmsAssembly.getProductCode());
			chanpinmingcheng.setText(cmsAssembly.getProductName());
			guigexinghao.setText(cmsAssembly.getSpecificationModel());
			jiliangdanwei.setText(cmsAssembly.getUnit());
			rukushuliang.setText(cmsAssembly.getQuantity().stripTrailingZeros().toPlainString());
		}
	}

	private void queryAllProductionLine(String barCode,String palletCode) {
		Gson gson = new Gson();
		Map<String, Object> ms = new HashMap<String, Object>();
		ms.put("barCode", barCode);
		ms.put("palletCode",palletCode);
		ms.put("type","复核");
		ms.put("device", DeviceInfoUtil.getOnlyID(this));
		request(gson.toJson(ms), "cms/cmsAssembly/cmsAssembly/queryCmsAssembly", "正在查询...", 1);
	}

	private void InertCmsAssembly() {
		cmsAssembly.setReviewQuantity(new BigDecimal(fuheshuliang.getText().toString()));
		cmsAssembly.setReviewerBatch(fuhepihao.getText().toString());
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		Map<String, Object> ms = null;
		try {
			ms = JsonUtil.objectToMap(cmsAssembly);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ms.put("device", DeviceInfoUtil.getOnlyID(this));
		request(gson.toJson(ms), "cms/cmsAssembly/cmsAssembly/fHAddUpdate", "正在设置...", 2);
	}

}
