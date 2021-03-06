package zrkj.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.finddreams.baselib.R;
import com.finddreams.baselib.base.BaseActivity;
import com.finddreams.baselib.view.ClearEditText;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnTouch;
import com.uhf.uhf.Common.Comm;
import com.uhf.uhf.UHF1.UHF001;
import com.uhf.uhf.UHF1.UHF1Application;
import com.uhf.uhf.UHF1.UHF1Function.AndroidWakeLock;
import com.uhf.uhf.UHF1.UHF1Function.SPconfig;
import com.uhf.uhf.UHF8.UHF008;

import zrkj.project.util.Result;

import static com.uhf.uhf.Common.Comm.Awl;
import static com.uhf.uhf.Common.Comm.Connect;
import static com.uhf.uhf.Common.Comm.ConnectModule;
import static com.uhf.uhf.Common.Comm.checkDevice;
import static com.uhf.uhf.Common.Comm.context;
import static com.uhf.uhf.Common.Comm.isQuick;
import static com.uhf.uhf.Common.Comm.isrun;
import static com.uhf.uhf.Common.Comm.lsTagList;
import static com.uhf.uhf.Common.Comm.lsTagList6B;
import static com.uhf.uhf.Common.Comm.soundPool;
import static com.uhf.uhf.Common.Comm.tagListSize;

@ContentView(R.layout.demorfid)
public class DemoRFID3 extends BaseActivity {

	@Override
	protected void onScan(String barcodeStr) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initView() {
	}

	// ????????????

	@Override
	protected void initData() {
		// ???????????????
		initDevice();
	}

	private void initDevice() {

		Comm.repeatSound = true;
		// getApplication()?????????????????????????????????
		Comm.app = new UHF1Application();
		Comm.spConfig = new SPconfig(this);
		checkDevice();
		Comm.powerUp();
		Comm.initWireless(Comm.app);
		Comm.connecthandler = connectH;
		Comm.uhf6.UHF6handler=uhfhandler;
		Comm.uhf8.mUHF8handler=uhfhandler;
		Comm.mInventoryHandler=uhfhandler;
		Comm.mOtherHandler=uhfhandler;
		Comm.mRWLHandler=uhfhandler;
		Comm.moduleType = Comm.Module.UHF008;
		if (Comm.moduleType != null) {
			Log.e("cjj", "1");
			ConnectModule();
		} else {
			Log.e("cjj", "2");
			Connect();
		}
		// // ?????????????????????
		// Comm.connecthandler = connectH;
		// // 1
		// // Comm.moduleType = Comm.Module.UHF008;
		// // Comm.ConnectModule();
		// // 2
		// Comm.Connect();
		// // 3
		// // Comm.Connect(1);
		// // Comm.Connect(2);
		// // Comm.Connect(5);
		// // Comm.Connect(6);
		// //Comm.Connect(8);
		Timer timer = new Timer(true);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Log.e("cjj", "????????????RFID??????");
				if (lsTagList6B.size() > 0) {
					Log.e("cjj", lsTagList6B.size() + "");
				}
				if (lsTagList.size() > 0) {
					Log.e("cjj", lsTagList.size() + "");
				}
			}
		}, 0 * 1000, 1 * 1000); // set timer duration
	}

	public Handler connectH = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			try {
				Bundle bd = msg.getData();
				String strMsg = bd.get("Msg").toString();
				if (!TextUtils.isEmpty(strMsg)) {
					showLongText(strMsg);
					// UHF008.mUHF8handler = uhfhandler;
					// ??????RDIF?????????
					Comm.mInventoryHandler = uhfhandler;
					// Comm.SetInventoryTid(true);// ?????? TID ?????????????????????
					Comm.SetInventoryTid(false);
					Awl.WakeLock();
					Comm.startScan();
					Log.e("cjj", "?????????????????????  " + strMsg);
				} else {
					showLongText("?????????????????????");
					Log.e("cjj", "?????????????????????");
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("cjj", e.getMessage());
			}
		}
	};
	public Handler uhfhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Log.e("cjj", "????????????  ");
		}
	};

	// ????????????
	protected void onDestory() {
		Awl.ReleaseWakeLock();
		Comm.stopScan();
		Comm.clean();
		Comm.powerDown();
		super.onDestroy();
	}

	private void setCS() {
		// ??????????????????(?????????????????????)???
		// ?????????????????????????????????
		// Comm.repeatSound=true;
		// ????????????????????????????????????
		// Comm.repeatSound=false;
		// try {
		// // ??????
		// Comm.opeT = Comm.operateType.setAntCheck;
		// Comm.setANTCheck(1);// 0????????? 1??????
		// } catch (Exception e) {
		// showLongText("?????????????????? ??????");
		// }
		try {
			// ????????????
			Comm.opeT = Comm.operateType.setPower;
			int ant1pow = 33;// 5-33
			int ant2pow = 5;// 5
			int ant3pow = 5;// 5
			int ant4pow = 5;// 5
			Comm.setAntPower(ant1pow, ant2pow, ant3pow, ant4pow);
		} catch (Exception e) {
			showLongText("?????????????????? ??????");
		}
		try {
			// ??????
			Comm.opeT = Comm.operateType.setReg;
			Comm.setAntReg(1); // CHN(China), FCC(America),ETSI(Europe),OTHER
		} catch (Exception e) {
			showLongText("???????????? ??????");
		}
		// try {
		// // ?????? ????????????
		// Comm.opeT = Comm.operateType.setReg;
		// Comm.setAntReg(1); // CHN(China), FCC(America),ETSI(Europe),OTHER
		// } catch (Exception e) {
		// showLongText("???????????? ??????");
		// }
	}

	@Override
	protected void requestCallBack(Result ajax, int posi) {

	}
}
