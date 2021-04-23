package zrkj.project;

import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;

import com.finddreams.baselib.R;
import com.finddreams.baselib.base.BaseActivity;
import com.finddreams.baselib.utils.ActivityUtil;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.event.OnTouch;

import zrkj.project.util.Result;
import zrkj.project.zrkj.DiaoBo_Activity;
import zrkj.project.zrkj.FenTuoRkActivity;
import zrkj.project.zrkj.PiHaoSheZhi_Activity;
import zrkj.project.zrkj.PoDaiTiaoZheng_Activity;
import zrkj.project.zrkj.RuKuFuHe_Activity;
import zrkj.project.zrkj.RuKuZhiJian_Activity;
import zrkj.project.zrkj.TuiHuo_Activity;
import zrkj.project.zrkj.XiaoHuo_Activity;

@ContentView(R.layout.project_mainmenu)
public class MainMenu extends BaseActivity {

	@Override
	protected void onScan(String barcodeStr) {

	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initView() {
		topTitle.mTvTitle.setText("菜单");
		topTitle.mTvBack.setText("退出登录");
		/*Gson gson = new Gson();
		functionLogin = gson.fromJson(SharePrefUtil.getString(context, "functions", ""), List.class);
		if (functionLogin.contains("curzd")) {
			this.findViewById(R.id.curzd).setVisibility(View.VISIBLE);
		}*/
	}

	@Override
	protected void initData() {

	}

	@OnTouch({R.id.relativeLayout_pymian1,
			R.id.relativeLayout_pymian2,
			R.id.relativeLayout_pymian3,
			R.id.relativeLayout_pymian4,
			R.id.relativeLayout_pymian5,
			R.id.relativeLayout_pymian6,
			R.id.relativeLayout_pymian7,
			R.id.relativeLayout_pymian8})
	private boolean menu(final View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if(v!=null&&v.getBackground()!=null){
				v.getBackground().setAlpha(20);
				TimerTask task = new TimerTask() {
					@Override
					public void run() {
						new Handler(Looper.getMainLooper()).post(new Runnable() {
							@Override
							public void run() {
								v.getBackground().setAlpha(255);// 设置的透明度
							}
						});
					}
				};
				Timer timer = new Timer();
				timer.schedule(task, 1000);
			}
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			if(v!=null&&v.getBackground()!=null) v.getBackground().setAlpha(255);// 设置的透明度
			switch (v.getId()) {
				case R.id.relativeLayout_pymian1:
					//分包
					ActivityUtil.goToActivity(context, FenTuoRkActivity.class);
					break;
				case R.id.relativeLayout_pymian2:
					//入库复核
					ActivityUtil.goToActivity(context, RuKuFuHe_Activity.class);
					break;
				case R.id.relativeLayout_pymian3:
					//入库质检
					ActivityUtil.goToActivity(context, RuKuZhiJian_Activity.class);
					break;
				case R.id.relativeLayout_pymian4:
					//破袋调整
					ActivityUtil.goToActivity(context, PoDaiTiaoZheng_Activity.class);
					break;
				case R.id.relativeLayout_pymian5:
					//调拨
					ActivityUtil.goToActivity(context, DiaoBo_Activity.class);
					break;
				case R.id.relativeLayout_pymian6:
					//销货
					ActivityUtil.goToActivity(context, XiaoHuo_Activity.class);
					break;
				case R.id.relativeLayout_pymian7:
					//退货
					ActivityUtil.goToActivity(context, TuiHuo_Activity.class);
					break;
				case R.id.relativeLayout_pymian8:
					//退货
					ActivityUtil.goToActivity(context, PiHaoSheZhi_Activity.class);
					break;
				default:
					break;
			}
		}
		return true;
	}

	@Override
	protected void requestCallBack(Result ajax, int posi) {

	}
}
