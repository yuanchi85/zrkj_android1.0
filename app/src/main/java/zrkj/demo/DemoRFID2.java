package zrkj.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

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
public class DemoRFID2 extends BaseActivity {

	@ViewInject(R.id.button_start)
	private Button button_start;
	@ViewInject(R.id.button_stop)
	private Button button_stop;
	@ViewInject(R.id.button_readclear)
	private Button button_readclear;
	@ViewInject(R.id.button_set)
	private Button button_set;

	@Override
	protected void onScan(String barcodeStr) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		button_stop.setEnabled(false);
	}

	// ????????????

	@Override
	protected void initData() {
		// ???????????????
		initDevice();
	}

	@Override
	protected void requestCallBack(Result ajax, int posi) {

	}

	private void initDevice() {
		Comm.repeatSound = true;
		Comm.app = new UHF1Application();
		Comm.spConfig = new SPconfig(this);
		// ===========================================================
		// ????????????
		setCS();
		// ===========================================================
		// ????????????
		Comm.checkDevice();
		Comm.powerUp();

		// ?????????????????????
		Comm.connecthandler = connectH;
		// 1
		// Comm.moduleType = Comm.Module.UHF008;
		// Comm.ConnectModule();
		// 2
		Comm.Connect();
		// 3
		// Comm.Connect(1);
		// Comm.Connect(2);
		// Comm.Connect(5);
		// Comm.Connect(6);
		// Comm.Connect(8);
		Log.e("cjj", "a");
		Awl = new AndroidWakeLock(
				(PowerManager) getSystemService(Context.POWER_SERVICE));
		Log.e("cjj", "b");
	}

	public Handler connectH = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Log.e("cjj", "???????????????????????????  " + msg.getData().get("Msg"));
			try {
				Bundle bd = msg.getData();
				String strMsg = bd.get("Msg").toString();
				if (!TextUtils.isEmpty(strMsg)) {
					showLongText(strMsg);
					// UHF008.mUHF8handler = uhfhandler;
					Comm.mInventoryHandler = uhfhandler;
					// Comm.SetInventoryTid(true);// ?????? TID ?????????????????????
					Comm.SetInventoryTid(false);
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
		@SuppressLint("SetTextI18n")
		@SuppressWarnings({ "unchecked", "unused" })
		@Override
		public void handleMessage(Message msg) {
			Log.e("cjj", "????????????  ");
		}
	};

	@OnTouch({ R.id.button_start, R.id.button_stop, R.id.button_readclear,
			R.id.button_set })
	private <T> boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			v.getBackground().setAlpha(20);
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			v.getBackground().setAlpha(255);// ??????????????????
			switch (v.getId()) {
			case R.id.button_start:
				try {
					Log.e("cjj", "????????????");
					button_readclear.performClick();
					ReadHandleUI();
					Awl.WakeLock();
					Comm.startScan();
					Log.e("cjj", "????????????");
				} catch (Exception ex) {
					StopHandleUI();
					ex.printStackTrace();
					Log.e("cjj", ex.getMessage());
				}
				break;
			case R.id.button_stop:
				Log.e("cjj", "????????????");
				Awl.ReleaseWakeLock();
				Comm.lv.clear();
				Comm.clean();
				Comm.stopScan();
				showlist();
				StopHandleUI();
				Log.e("cjj", "????????????");
				break;
			case R.id.button_readclear:
				Log.e("cjj", "????????????");
				Comm.lv.clear();
				Comm.clean();
				showlist();
				Log.e("cjj", "????????????");
				break;
			case R.id.button_set:
				// Comm.setParameters();// ????????????(???????????????)????????????????????? isQuick??????
				// ??????????????????????????????
				Log.e("cjj", "isQuick:" + isQuick);
				if (Comm.setParameters()) {
					if (isQuick) {// ?????????????????? true?????? false??????
						isQuick = false;
						showLongText("????????????????????????");
					} else {
						isQuick = true;
						showLongText("????????????????????????");
					}
				} else {
					showLongText("????????????????????????");
				}
				break;
			default:
				break;
			}
		}
		return true;
	}

	private void ReadHandleUI() {
		button_start.setEnabled(false);
		button_stop.setEnabled(true);
		button_readclear.setEnabled(false);
		button_set.setEnabled(false);
	}

	private void StopHandleUI() {
		button_start.setEnabled(true);
		button_stop.setEnabled(false);
		button_readclear.setEnabled(true);
		button_set.setEnabled(true);
	}

	// ????????????
	protected void onDestory() {
		Comm.stopScan();
		Comm.clean();
		Comm.powerDown();
		super.onDestroy();
	}

	String[] Coname = new String[] { "NO", "                    EPC ID ",
			"Count" };

	private void showlist() {
		// Log.v("cjj", "showlist");
		// try {
		// int index = 1;
		// int ReadCnt = 0;// ??????
		// List<Map<String, ?>> list = new ArrayList<Map<String, ?>>();
		// Map<String, String> h = new HashMap<String, String>();
		// for (int i = 0; i < Coname.length; i++)
		// h.put(Coname[i], Coname[i]);
		// list.add(h);
		// String epcstr = "";// epc
		//
		// int ListIndex = 0;
		// if (tagListSize > 0)
		// tv_tags.setText(String.valueOf(tagListSize));
		// if (isQuick && !Comm.isrun)
		// tv_state.setText(String.valueOf(R.string.please_wait));
		//
		// if (!isQuick || !Comm.isrun) {
		// while (tagListSize > 0) {
		// if (index < 100) {
		// if (Comm.is6BTag) {
		// epcstr = lsTagList6B.get(ListIndex).strUID;
		// if (epcstr != null && epcstr != "") {
		// epcstr = lsTagList6B.get(ListIndex).strUID
		// .replace(" ", "");
		// Map<String, String> m = new HashMap<String, String>();
		// m.put(Coname[0], String.valueOf(index));
		// m.put(Coname[1], epcstr);
		// ReadCnt = lsTagList6B.get(ListIndex).nTotal;
		// m.put(Coname[2], String.valueOf(ReadCnt));
		// // int
		// // mRSSI=Integer.parseInt(lsTagList.get(ListIndex).strRSSI);
		// index++;
		// list.add(m);
		// }
		// } else {
		// epcstr = lsTagList.get(ListIndex).strEPC;
		// if (Comm.dt == Comm.DeviceType.supoin_JT
		// && Comm.To433Index < index) {
		// Comm.mWirelessMg.writeTo433(epcstr + "\n");
		// Comm.To433Index++;
		// }
		//
		// if (epcstr != null && !epcstr.equals("")) {
		// epcstr = lsTagList.get(ListIndex).strEPC
		// .replace(" ", "");
		// if (epcstr.length() > 24) {
		// epcstr = epcstr.substring(0, 24) + "\r\n"
		// + epcstr.substring(24);
		// }
		//
		// Map<String, String> m = new HashMap<String, String>();
		// m.put(Coname[0], String.valueOf(index));
		// m.put(Coname[1], epcstr);
		// ReadCnt = lsTagList.get(ListIndex).nReadCount;
		// m.put(Coname[2], String.valueOf(ReadCnt));
		// // int
		// // mRSSI=Integer.parseInt(lsTagList.get(ListIndex).strRSSI);
		// index++;
		// list.add(m);
		// }
		// }
		// }
		// ListIndex++;
		// tagListSize--;
		// }
		//
		// }
		// if (!isQuick || !Comm.isrun) {
		// ListAdapter adapter = new MyAdapter(this, list,
		// R.layout.listitemview_inv, Coname, new int[] {
		// R.id.textView_readsort, R.id.textView_readepc,
		// R.id.textView_readcnt });
		//
		// // layout???listView??????????????????????????????TextView??????????????????????????????????????????
		// // ColumnNames???????????????????????????
		// // ?????????????????????int[]???????????????view?????????id???????????????ColumnNames????????????????????????view????????????TextView
		// listView.setAdapter(adapter);
		//
		// }
		// } catch (NumberFormatException e) {
		// e.printStackTrace();
		// }
		// if (!isrun)
		// tv_state.setText(R.string.wait_operate);
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
}
