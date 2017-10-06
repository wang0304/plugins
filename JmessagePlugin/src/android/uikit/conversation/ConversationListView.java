package cn.jmessage.android.uikit.conversation;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import cn.jmessage.android.uikit.chatting.utils.IdHelper;

public class ConversationListView {
	
	private View mConvListFragment;
	private ListView mConvListView = null;
	private TextView mTitle;
	private ImageButton mCreateGroup;
	private LinearLayout mHeader;
    private Context mContext;

	public ConversationListView(View view, Context context) {
		this.mConvListFragment = view;
        this.mContext = context;
	}

	public void initModule() {
		mTitle = (TextView) mConvListFragment.findViewById(IdHelper.getViewID(mContext, "main_title_bar_title"));
		mTitle.setText("会话");
		mConvListView = (ListView) mConvListFragment.findViewById(IdHelper.getViewID(mContext, "conv_list_view"));
		mCreateGroup = (ImageButton) mConvListFragment.findViewById(IdHelper.getViewID(mContext, "create_group_btn"));
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mHeader = (LinearLayout) inflater.inflate(IdHelper.getLayout(mContext, "jmui_conv_list_head_view"), mConvListView, false);
        mConvListView.addHeaderView(mHeader);
	}
	
	public void setConvListAdapter(ListAdapter adapter) {
		mConvListView.setAdapter(adapter);
	}
	
	public void setListener(OnClickListener onClickListener) {
		mCreateGroup.setOnClickListener(onClickListener);
	}
	
	public void setItemListeners(OnItemClickListener onClickListener) {
		mConvListView.setOnItemClickListener(onClickListener);
	}
	
	public void setLongClickListener(OnItemLongClickListener listener) {
		mConvListView.setOnItemLongClickListener(listener);
	}

    public void showHeaderView() {
        mHeader.findViewById(IdHelper.getViewID(mContext, "network_disconnected_iv")).setVisibility(View.VISIBLE);
        mHeader.findViewById(IdHelper.getViewID(mContext, "check_network_hit")).setVisibility(View.VISIBLE);
    }

    public void dismissHeaderView() {
		mHeader.findViewById(IdHelper.getViewID(mContext, "network_disconnected_iv")).setVisibility(View.GONE);
		mHeader.findViewById(IdHelper.getViewID(mContext, "check_network_hit")).setVisibility(View.GONE);
    }


}
