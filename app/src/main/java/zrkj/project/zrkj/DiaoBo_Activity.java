package zrkj.project.zrkj;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.finddreams.baselib.R;
import com.finddreams.baselib.base.BaseActivity;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

import zrkj.project.util.Result;

@ContentView(R.layout.diaobo_activity)
public class DiaoBo_Activity extends BaseActivity {
	@ViewInject(R.id.lv_list)
	ListView mList;
	DemoAdapter adapter;
	@Override
	protected void initData() {

	}

	@Override
	protected void onScan(String barcodeStr) {
	}

	/**
	 * 初始化所有控件
	 */
	@Override
	protected void initView() {
		topTitle.mTvTitle.setText("调拨");
		topTitle.mTvRight.setText("");
		adapter = new DiaoBo_Activity.DemoAdapter();
		mList.setAdapter(adapter);
	}

	/**
	 * 启动构造器，显示列表，初始化列表，响应事件
	 */
	public class DemoAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return 10;
		}

		@Override
		public Object getItem(int i) {
			return null;
		}

		@Override
		public long getItemId(int i) {
			return i;
		}


		@Override
		public View getView(final int i, View view, ViewGroup viewGroup) {
			final DiaoBo_Activity.DemoAdapter.ViewHolder holder;

			if (view == null) {
				view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.diaobo_tbaerl, null);
				holder = new DiaoBo_Activity.DemoAdapter.ViewHolder();
				holder.tv1 = view.findViewById(R.id.bianhao);
				holder.tv2 = view.findViewById(R.id.mingcheng);
				holder.tv3 = view.findViewById(R.id.xinghao);
				holder.tv4 = view.findViewById(R.id.danwei);
				holder.tv5 = view.findViewById(R.id.pihao);
				holder.tv6 = view.findViewById(R.id.shuliang);
				holder.tv7 = view.findViewById(R.id.yuancangkubianhao);
				holder.tv8 = view.findViewById(R.id.yuantuopantiaoma);
				holder.tv9 = view.findViewById(R.id.xingcangkubianhao);
				holder.tv10 = view.findViewById(R.id.xingtuopantiaoma);
				holder.tv11 = view.findViewById(R.id.beizhu);

				holder.okTable = view.findViewById(R.id.biaoge);
				view.setTag(holder);

			} else {
				holder = (DiaoBo_Activity.DemoAdapter.ViewHolder) view.getTag();
			}





			return view;
		}

		class ViewHolder {
			TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv10,tv11;
			LinearLayout okTable;
		}
	}
	@Override
	protected void requestCallBack(Result ajax, int posi) {

	}


}
