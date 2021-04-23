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
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnTouch;

import java.util.HashMap;
import java.util.Map;

import zrkj.project.entity.CmsAssembly;
import zrkj.project.toast.CommomDialog;
import zrkj.project.util.Result;

@ContentView(R.layout.podaitiaozheng_activity)
public class PoDaiTiaoZheng_Activity extends BaseActivity {
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
	@ViewInject(R.id.danqianshuliang)
	private EditText dangqianshuliang;
	@ViewInject(R.id.podaishuliang)
	private EditText podaishuliang;
	@ViewInject(R.id.beizhu)
	private EditText beizhu;
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

	@OnTouch({R.id.login})
	private boolean login(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			switch (v.getId()) {
				case R.id.login:
					//分包
					if (beforeLogin()) {
						CommomDialog commomDialog = new CommomDialog(PoDaiTiaoZheng_Activity.this, R.style.dialog, "是否确定提交？", new CommomDialog.OnCloseListener() {
							@Override
							public void onClick(Dialog dialog1, boolean confirm) {
								if (confirm == false) {
									InsetCmsTornBags();
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
		if (podaishuliang.getText().toString().equals("")) {
			showCustomToast("请输入破袋数量");
			return false;
		}
		if (Integer.parseInt(podaishuliang.getText().toString())>Integer.parseInt(dangqianshuliang.getText().toString())) {
			showCustomToast("破袋数量不能大于当前数量");
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
			chanpinbianma.setText(cmsAssembly.getProductCode());
			chanpingmingcheng.setText(cmsAssembly.getProductName());
			guigexinhao.setText(cmsAssembly.getSpecificationModel());
			jiliangdanwei.setText(cmsAssembly.getUnit());
			dangqianshuliang.setText(cmsAssembly.getQuantity().stripTrailingZeros().toPlainString());
		}
	}

	private void queryAllProductionLine(String barCode,String palletCode) {
		Gson gson = new Gson();
		Map<String, Object> ms = new HashMap<String, Object>();
		ms.put("barCode", barCode);
		ms.put("palletCode",palletCode);
		ms.put("type","");
		ms.put("device", DeviceInfoUtil.getOnlyID(this));
		request(gson.toJson(ms), "cms/cmsAssembly/cmsAssembly/queryCmsAssembly", "正在查询...", 1);
	}


	private void InsetCmsTornBags() {
		Gson gson = new Gson();
		Map<String, Object> ms = new HashMap<String, Object>();
		ms.put("assemblyId", cmsAssembly.getId());
		ms.put("tornBagsQuantity",podaishuliang.getText().toString());
		ms.put("remarks",beizhu.getText().toString());
		ms.put("device", DeviceInfoUtil.getOnlyID(this));
		request(gson.toJson(ms), "cms/cmsTornBags/cmsTornBags/add", "正在保存...", 2);
	}
}
