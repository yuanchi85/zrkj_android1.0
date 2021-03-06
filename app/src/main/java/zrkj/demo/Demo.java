package zrkj.demo;

import java.io.IOException;
import java.net.URI;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.finddreams.baselib.R;
import com.finddreams.baselib.activity.CaptureActivity;
import com.finddreams.baselib.base.BaseActivity;
import com.finddreams.baselib.http.MyHttpUtils;
import com.finddreams.baselib.http.NetStateUtil;
import com.finddreams.baselib.http.URLUtils;
import com.finddreams.baselib.service.MQTTService;
import com.finddreams.baselib.utils.ActivityUtil;
import com.finddreams.baselib.utils.InputMethodUtil;
import com.finddreams.baselib.utils.SpinnerUtil;
import com.finddreams.baselib.utils.ToastManager;
import com.finddreams.baselib.view.ClearEditText;
import com.finddreams.baselib.view.CustomProgressDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;

import zrkj.project.util.Result;

@ContentView(R.layout.demo)
public class Demo extends BaseActivity {
	@ViewInject(R.id.userName)
	private ClearEditText userName;
	@ViewInject(R.id.password)
	private EditText password;
	@ViewInject(R.id.select)
	private Spinner select;
	@ViewInject(R.id.radio)
	private RadioGroup group;
	@ViewInject(R.id.cb01)
	private CheckBox cb01;
	@ViewInject(R.id.cb02)
	private CheckBox cb02;
	@ViewInject(R.id.cb03)
	private CheckBox cb03;
	@ViewInject(R.id.btn01)
	private Button btn01;
	@Override
	protected void requestCallBack(Result ajax, int posi) {

	}
	@Override
	protected void initData() {
	}

	@Override
	protected void initView() {
		// ***** ???????????? ????????????
		ViewUtils.inject(this);
		topTitle.mTvTitle.setText("????????????");
		topTitle.mTvTitle.setText("");
		topTitle.mTvBack.setText("");
		// ?????????????????????
		initSelect(new String[] { "0", "1", "2", "3" });
		// ????????????????????????
		SpinnerUtil.setSpinnerItemSelectedByValue(select, "3");
		InputMethodUtil.hiddenInputMethod(this, userName);
		InputMethodUtil.hiddenInputMethod(this, password);
		InputMethodUtil.hideBottomSoftInputMethod(this);

		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				// ??????????????????????????????ID
				int radioButtonId = arg0.getCheckedRadioButtonId();
				// ??????ID??????RadioButton?????????
				RadioButton rb = (RadioButton) Demo.this
						.findViewById(radioButtonId);
				// ???????????????????????????????????????
				showShortText("??????????????????" + rb.getText());
			}
		});

		initcheckbox();
	}

	private void initcheckbox() {

		/**
		 * ???Button????????????
		 */
		btn01.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String text = "???????????????";
				if (cb01.isChecked()) {
					text += cb01.getText().toString();
				}
				if (cb02.isChecked()) {
					text += cb02.getText().toString();
				}
				if (cb03.isChecked()) {
					text += cb03.getText().toString();
				}
				Toast.makeText(Demo.this, text, Toast.LENGTH_SHORT).show();
			}
		});

	}

	private void initSelect(String[] strs) {
		ArrayAdapter adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, strs);

		// ???????????????????????????
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// ???adapter ?????????spinner???
		select.setAdapter(adapter);
		// ????????????Spinner????????????
		select.setOnItemSelectedListener(new OnItemSelectedListener() {
			int count = 0;

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				showShortText(select.getSelectedItem().toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	@OnClick({ R.id.tz })
	private <T> void tz(View v) {
		// showShortText("????????????..." + userName.getText().toString()
		// + password.getText().toString());
		// ***** UI ??????????????????
		// ***** ??????????????????
		// tipDialog();
		// ***** ????????????(????????????) *****GSON
		// visitWeb();
		// *****????????????(????????????)
		tz();// ???????????????
		// tzCapture();//????????????????????? (?????????????????????)
		// tzWeb();//?????????????????????
	}

	@OnClick({ R.id.tzCapture })
	private <T> void tzCapture(View v) {
		tzCapture();// ????????????????????? (?????????????????????)
	}

	@OnClick({ R.id.tzPulltorefreshview })
	private <T> void tzPulltorefreshview(View v) {
		ActivityUtil.goToActivity(context, DemoPulltorefreshview.class);
	}

	@OnClick({ R.id.fragment_parent })
	private <T> void fragment_parent(View v) {
		ActivityUtil.goToActivity(context, DemoFragmentParnet.class);
	}

	@OnClick({ R.id.tzPictureUpload })
	private <T> void tzPictureUpload(View v) {
		ActivityUtil.goToActivity(context, DemoPictureUpload.class);
	}

	@OnClick({ R.id.tzWeb })
	private <T> void tzWeb(View v) {
		tzWeb();// ?????????????????????
	}

	@OnClick({ R.id.tzDemoScroll_Fragment })
	private <T> void tzDemoScroll_Fragment(View v) {
		ActivityUtil.goToActivity(context, DemoScroll_Fragment_list.class);
	}

	@OnClick({ R.id.DemoBlueBootPrint })
	private <T> void DemoBlueBootPrint(View v) {
		ActivityUtil.goToActivity(context, DemoBlueToothPrint.class);
	}

	// ======================================================================================
	private void tzWeb() {
		Bundle bundle = new Bundle();
		// bundle.putString("url", "http://www.baidu.com");
		bundle.putString(
				"url",
				"https://pro.modao.cc/app/i9o8CAI0Dty4qwlhOQqd2TGROaqCt6Q#screen=sC6C9F2343D1528127357289");
		ActivityUtil.goToActivity(context, DemoWebPrint.class, bundle);
	}

	private void tz() {
		Bundle bundle = new Bundle();
		bundle.putString("key", "value");
		ActivityUtil.goToActivity(context, Demo2.class, bundle);
	}

	private void tzCapture() {
		int nextcode = 0;
		Intent intent = new Intent();
		intent.setClass(this, CaptureActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivityForResult(intent, nextcode);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 0:
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getBundleExtra("bundle");
				showShortText("?????????????????????:" + bundle.getString("result"));
			}
			break;
		}
	}

	public void tipDialog() {
		Dialog dialog = new AlertDialog.Builder(this).setTitle("????????????!")
				.setIcon(android.R.drawable.ic_dialog_info)
				// .setView(view);
				.setPositiveButton("??????", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO
						dialog.cancel();// ???????????????
					}
				})
				.setNegativeButton("??????", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();// ???????????????
					}
				}).create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	private void visitWeb() {
		// *****GSON
		// System.out.println(new Gson().fromJson(new Gson().toJson(new User()),
		// User.class));
		// ????????????
		Log.v("cjj", "???????????????????????????0");
		// ??????????????????????????????
		RequestParams rp = new RequestParams();
		rp.addBodyParameter("userName", userName.getText().toString());
		rp.addBodyParameter("password", password.getText().toString());
		cpd.setTvMsgAndShow("????????????...");
		new MyHttpUtils(context).httpPost(URLUtils.getUrl("User_test02"), rp,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						Log.v("cjj", "2????????????onFailure");
						Log.v("cjj", "arg:" + arg1);
						cpd.dismiss();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						Log.v("cjj", "2????????????onSuccess");
						Log.v("cjj", "arg:" + arg0.result.toString());
						cpd.dismiss();
					}
				});

		// ????????????
		Log.v("cjj", "???????????????????????????1");
		com.loopj.android.http.RequestParams rp2 = new com.loopj.android.http.RequestParams();
		rp2.put("userName", userName.getText().toString());
		rp2.put("password", password.getText().toString());

		if (0 == NetStateUtil.isNetworkAvailable(context)) {
			ToastManager.showShortText(context,
					context.getString(R.string.no_net));
		} else {
			cpd.setTvMsgAndShow("????????????...");
			httpClient.post(URLUtils.getUrl("User_test01"), rp2,
					new AsyncHttpResponseHandler() {

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							Log.v("cjj", "1????????????onFailure");
							if (arg2 != null)// ????????????????????????
								Log.v("cjj", "arg:" + new String(arg2));
							cpd.dismiss();
						}

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							Log.v("cjj", "1????????????onSuccess");
							if (arg2 != null)// ????????????????????????
								Log.v("cjj", "arg:" + new String(arg2));
							cpd.dismiss();
						}
					});
		}
		Log.v("cjj", "???????????????????????????2");
		// ???????????? //?????????????????????
		com.loopj.android.http.RequestParams rp3 = new com.loopj.android.http.RequestParams();
		rp3.put("userName", userName.getText().toString());
		rp3.put("password", password.getText().toString());
		httpClient.post(context, URLUtils.getUrl("User_test03"), rp3,
				new ResponseHandlerInterface() {

					@Override
					public void setUseSynchronousMode(boolean arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void setRequestURI(URI arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void setRequestHeaders(Header[] arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void sendSuccessMessage(int arg0, Header[] arg1,
							byte[] arg2) {
						// TODO Auto-generated method stub

					}

					@Override
					public void sendStartMessage() {
						// TODO Auto-generated method stub

					}

					@Override
					public void sendRetryMessage(int arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void sendResponseMessage(HttpResponse arg0)
							throws IOException {
						// TODO Auto-generated method stub

					}

					@Override
					public void sendProgressMessage(int arg0, int arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void sendFinishMessage() {
						// TODO Auto-generated method stub

					}

					@Override
					public void sendFailureMessage(int arg0, Header[] arg1,
							byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method stub

					}

					@Override
					public void sendCancelMessage() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onPreProcessResponse(
							ResponseHandlerInterface arg0, HttpResponse arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onPostProcessResponse(
							ResponseHandlerInterface arg0, HttpResponse arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public boolean getUseSynchronousMode() {
						// TODO Auto-generated method stub
						return false;
					}

					@Override
					public URI getRequestURI() {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public Header[] getRequestHeaders() {
						// TODO Auto-generated method stub
						return null;
					}
				});
		Log.v("cjj", "???????????????????????????4");
		// ???????????? HttpUtil
	}

	@Override
	protected void onScan(String barcodeStr) {
		// TODO Auto-generated method stub

	}

}
