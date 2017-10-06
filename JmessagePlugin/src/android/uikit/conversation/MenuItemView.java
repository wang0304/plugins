package cn.jmessage.android.uikit.conversation;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import cn.jmessage.android.uikit.chatting.utils.IdHelper;

/**
 * Created by Ken on 2015/1/26.
 */
public class MenuItemView {

    private Context context;
    private View mView;
    private LinearLayout mCreateGroupLl;
    private LinearLayout mAddFriendLl;

    public MenuItemView(Context context, View view) {
        this.context = context;
        this.mView = view;
    }

    public void initModule() {
        mCreateGroupLl = (LinearLayout) mView.findViewById(IdHelper.getViewID(context, "create_group_ll"));
        mAddFriendLl = (LinearLayout) mView.findViewById(IdHelper.getViewID(context, "add_friend_ll"));
    }

    public void setListeners(View.OnClickListener listener) {
        mCreateGroupLl.setOnClickListener(listener);
        mAddFriendLl.setOnClickListener(listener);
    }
}
