package cn.jmessage.android.uikit.conversation;

import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import cn.jmessage.android.uikit.chatting.ChatActivity;
import cn.jmessage.android.uikit.chatting.utils.DialogCreator;
import cn.jmessage.android.uikit.chatting.utils.IdHelper;
import cn.jmessage.android.uikit.utils.StringUtil;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.UserInfo;

public class ConversationListController implements OnClickListener,
        OnItemClickListener, OnItemLongClickListener {

    private final String TAG = "jmessage";
    private ConversationListView mConvListView;
    private ConversationListFragment mContext;
    private List<Conversation> mDatas = new ArrayList<Conversation>();
    private ConversationListAdapter mListAdapter;
    private int mWidth;
    private Dialog mDialog;
    private Conversation info;

    public ConversationListController(ConversationListView listView, ConversationListFragment context,
                                      int width) {
        this.mConvListView = listView;
        this.mContext = context;
        this.mWidth = width;
        initConvListAdapter();
    }

    // 得到会话列表
    private void initConvListAdapter() {
        mDatas = JMessageClient.getConversationList();
        if(null != mDatas){
            //对会话列表进行时间排序
            if (null != mDatas && mDatas.size() > 1) {
                SortConvList sortList = new SortConvList();
                Collections.sort(mDatas, sortList);
            }

            mListAdapter = new ConversationListAdapter(mContext.getActivity(), mDatas);
            mConvListView.setConvListAdapter(mListAdapter);
        }

        Log.i(TAG, TAG + "_会话列表mDatas1：" + (mDatas != null ? mDatas.toString() : ""));
        List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
        List<String> newStrList = new ArrayList<String>();

        try {
            for(Conversation info: mDatas){
                String jsonStr = StringUtil.format2JSONStr(info.toString(), "Conversation");
                newStrList.add(jsonStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i(TAG, TAG + "_会话列表List<Map>：" + newStrList.toString());
    }


    @Override
    public void onClick(View v) {
        final int create_group_btn = IdHelper.getViewID(mContext.getActivity().getApplicationContext(), "create_group_btn");
        if(v.getId() == create_group_btn){
            mContext.showMenuPopWindow();
        }
    }

    // 点击会话列表
    @Override
    public void onItemClick(AdapterView<?> viewAdapter, View view, int position, long id) {
        // TODO Auto-generated method stub
        final Intent intent = new Intent();
        if (position > 0) {
            Conversation conv = mDatas.get(position - 1);
            if (null != conv) {
                // 当前点击的会话是否为群组
                if (conv.getType() == ConversationType.group) {
                    long groupId = ((GroupInfo) conv.getTargetInfo()).getGroupID();
                    intent.putExtra(JChatDemoApplication.GROUP_ID, groupId);
                    intent.putExtra(JChatDemoApplication.DRAFT, getAdapter().getDraft(conv.getId()));
                    intent.setClass(mContext.getActivity(), ChatActivity.class);
                    mContext.getActivity().startActivity(intent);
                    return;
                } else {
                    String targetId = ((UserInfo) conv.getTargetInfo()).getUserName();
                    intent.putExtra(JChatDemoApplication.TARGET_ID, targetId);
                    intent.putExtra(JChatDemoApplication.TARGET_APP_KEY, conv.getTargetAppKey());
                    Log.d("ConversationList", "Target app key from conversation: " + conv.getTargetAppKey());
                    intent.putExtra(JChatDemoApplication.DRAFT, getAdapter().getDraft(conv.getId()));
                }
                intent.setClass(mContext.getActivity(), ChatActivity.class);
                mContext.getActivity().startActivity(intent);
            }
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> viewAdapter, View view, final int position, long id) {
        if (position > 0) {
            final Conversation conv = mDatas.get(position - 1);
            if (conv != null) {
                OnClickListener listener = new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (conv.getType() == ConversationType.group) {
                            JMessageClient.deleteGroupConversation(((GroupInfo) conv.getTargetInfo())
                                    .getGroupID());
                        } else {
                            //使用带AppKey的接口,可以删除跨/非跨应用的会话(如果不是跨应用,conv拿到的AppKey则是默认的)
                            JMessageClient.deleteSingleConversation(((UserInfo) conv.getTargetInfo())
                                    .getUserName(), conv.getTargetAppKey());
                        }
                        mDatas.remove(position - 1);
                        mListAdapter.notifyDataSetChanged();
                        mDialog.dismiss();
                    }
                };
                mDialog = DialogCreator.createDelConversationDialog(mContext.getActivity(), conv.getTitle(),
                        listener);
                mDialog.show();
                mDialog.getWindow().setLayout((int) (0.8 * mWidth), WindowManager.LayoutParams.WRAP_CONTENT);
            }
        }
        return true;
    }

    public ConversationListAdapter getAdapter() {
        return mListAdapter;
    }

}
