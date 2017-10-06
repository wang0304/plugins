package cn.jmessage.android.uikit.conversation;

import android.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.jmessage.android.uikit.chatting.ChatActivity;
import cn.jmessage.android.uikit.chatting.utils.DialogCreator;
import cn.jmessage.android.uikit.chatting.utils.HandleResponseCode;
import cn.jmessage.android.uikit.chatting.utils.IdHelper;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.CreateGroupCallback;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by Ken on 2015/1/26.
 */
public class MenuItemController implements View.OnClickListener {

    private MenuItemView mMenuItemView;
    private ConversationListFragment mContext;
    private Context mCon;
    private ConversationListController mController;
    private Dialog mLoadingDialog;
    private Dialog mAddFriendDialog;
    private int mWidth;

    public MenuItemController(MenuItemView view, ConversationListFragment context,
                              ConversationListController controller, int width) {
        this.mMenuItemView = view;
        this.mContext = context;
        this.mCon = mContext.getActivity().getApplicationContext();
        this.mController = controller;
        mWidth = width;
    }


    @Override
    public void onClick(View v) {
        final int create_group_ll = IdHelper.getViewID(mCon, "create_group_ll");
        final int add_friend_ll = IdHelper.getViewID(mCon, "add_friend_ll");
        if( v.getId() == create_group_ll){
            mContext.dismissPopWindow();
//                mContext.StartCreateGroupActivity();
            mLoadingDialog = DialogCreator.createLoadingDialog(mContext.getActivity(),
                    mContext.getString(IdHelper.getString(mCon, "creating_hint"))); 
            mLoadingDialog.show();
            JMessageClient.createGroup("", "", new CreateGroupCallback() {

                @Override
                public void gotResult(final int status, String msg, final long groupId) {
                    mLoadingDialog.dismiss();
                    if (status == 0) {
                        Conversation conv = Conversation.createGroupConversation(groupId);
                        mController.getAdapter().setToTop(conv);
                        Intent intent = new Intent();
                        //设置跳转标志
                        intent.putExtra("fromGroup", true);
                        intent.putExtra(JChatDemoApplication.MEMBERS_COUNT, 1);
                        intent.putExtra(JChatDemoApplication.GROUP_ID, groupId);
                        intent.setClass(mContext.getActivity(), ChatActivity.class);
                        mContext.startActivity(intent);
                    } else {
                        HandleResponseCode.onHandle(mContext.getActivity(), status, false);
                        Log.i("CreateGroupController", "status : " + status);
                    }
                }
            });
        }
        else if(v.getId() == add_friend_ll){
            mContext.dismissPopWindow(); 
            mAddFriendDialog = new Dialog(mContext.getActivity(), IdHelper.getStyle(mCon, "jmui_default_dialog_style"));
            final View view = LayoutInflater.from(mContext.getActivity())
                    .inflate(IdHelper.getLayout(mCon, "jmui_dialog_add_friend_to_conv_list"), null); 
            mAddFriendDialog.setContentView(view);
            mAddFriendDialog.getWindow().setLayout((int) (0.8 * mWidth), WindowManager.LayoutParams.WRAP_CONTENT);
            mAddFriendDialog.show(); 
            final EditText userNameEt = (EditText) view.findViewById(IdHelper.getViewID(mCon, "user_name_et"));
            final Button cancel = (Button) view.findViewById(IdHelper.getViewID(mCon, "jmui_cancel_btn")); 
            final Button commit = (Button) view.findViewById(IdHelper.getViewID(mCon, "jmui_commit_btn")); 
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                	int jmui_cancel_btn = IdHelper.getViewID(mCon, "jmui_cancel_btn");
                	int jmui_commit_btn = IdHelper.getViewID(mCon, "jmui_commit_btn");
                    if (view.getId() == jmui_cancel_btn) {
                        mAddFriendDialog.cancel();
                    } else if(view.getId() == jmui_commit_btn) {
                        String targetId = userNameEt.getText().toString().trim();
                        Log.i("MenuItemController", "targetID " + targetId);
                        if (TextUtils.isEmpty(targetId)) {
                            HandleResponseCode.onHandle(mContext.getActivity().getApplicationContext(), 801001, true);
                        } else if (targetId.equals(JMessageClient.getMyInfo().getUserName())
                                || targetId.equals(JMessageClient.getMyInfo().getNickname())) {
                            Toast.makeText(mContext.getActivity(), 
                                    mContext.getString(IdHelper.getString(mCon, "user_add_self_toast")),
                                    Toast.LENGTH_SHORT).show();
                            return;
                        } else if (isExistConv(targetId)) {
                            Toast.makeText(mContext.getActivity(), 
                                    mContext.getString(IdHelper.getString(mCon, "jmui_user_already_exist_toast")),
                                    Toast.LENGTH_SHORT).show();
                            userNameEt.setText("");
                        } else {
                            mLoadingDialog = DialogCreator.createLoadingDialog(mContext.getActivity(),
                                    mContext.getString(IdHelper.getString(mCon, "adding_hint"))); 
                            mLoadingDialog.show();
                            dismissSoftInput();
                            getUserInfo(targetId);
                        }
                    }
                }
            };
            cancel.setOnClickListener(listener);
            commit.setOnClickListener(listener);
        }
    }

    private void getUserInfo(final String targetId){
        JMessageClient.getUserInfo(targetId, new GetUserInfoCallback() {
            @Override
            public void gotResult(final int status, String desc, final UserInfo userInfo) {
                if(null != mLoadingDialog) mLoadingDialog.dismiss();
                if (status == 0) {
                    Conversation conv = Conversation.createSingleConversation(targetId);
                    if (!TextUtils.isEmpty(userInfo.getAvatar())) {
                        userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                            @Override
                            public void gotResult(int status, String desc, Bitmap bitmap) {
                                if (status == 0) {
                                    mController.getAdapter().notifyDataSetChanged();
                                } else {
                                    HandleResponseCode.onHandle(mContext.getActivity(), status, false);
                                }
                            }
                        });
                    }
                    mController.getAdapter().setToTop(conv);
                    if(null != mAddFriendDialog) mAddFriendDialog.cancel();
                } else {
                    HandleResponseCode.onHandle(mContext.getActivity(), status, true);
                }
            }
        });
    }

    public void dismissSoftInput() {
        InputMethodManager imm = ((InputMethodManager) mContext.getActivity()
                .getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (mContext.getActivity().getWindow().getAttributes().softInputMode
                != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (mContext.getActivity().getCurrentFocus() != null)
                imm.hideSoftInputFromWindow(mContext.getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private boolean isExistConv(String targetId) {
        Conversation conv = JMessageClient.getSingleConversation(targetId);
        return conv != null;
    }


    // =========================== 自定义 ========================
    /**
     * 添加好友
     * @param targetId
     */
    public void addNewMyFriend(String targetId){
        Log.i("MenuItemController", "targetID " + targetId);
        if (TextUtils.isEmpty(targetId)) { // 用户名不能为空
            HandleResponseCode.onHandle(mContext.getActivity().getApplicationContext(), 801001, true);
        } else if (targetId.equals(JMessageClient.getMyInfo().getUserName())
                || targetId.equals(JMessageClient.getMyInfo().getNickname())) { // 用户不能添加自己
            Toast.makeText(mContext.getActivity(),
                    mContext.getString(IdHelper.getString(mCon, "user_add_self_toast")), 
                    Toast.LENGTH_SHORT).show();
            return;
        } else if (isExistConv(targetId)) { // 用户已存在
            Toast.makeText(mContext.getActivity(),
                    mContext.getString(IdHelper.getString(mCon, "jmui_user_already_exist_toast")), 
                    Toast.LENGTH_SHORT).show();
            return;
        } else {
            getFriendInfo(targetId);
        }
    }

    /**
     * 获取好友信息
     * @param targetId
     */
    private void getFriendInfo(final String targetId){
        JMessageClient.getUserInfo(targetId, new GetUserInfoCallback() {
            @Override
            public void gotResult(final int status, String desc, final UserInfo userInfo) {
                if (status == 0) {
                    Conversation conv = Conversation.createSingleConversation(targetId);
                    if (!TextUtils.isEmpty(userInfo.getAvatar())) {
                        userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                            @Override
                            public void gotResult(int status, String desc, Bitmap bitmap) {
                                if (status == 0) {
                                    //mController.getAdapter().notifyDataSetChanged();
                                } else {
                                    //HandleResponseCode.onHandle(mContext.getActivity(), status, false);
                                }
                            }
                        });
                    }
                    //mController.getAdapter().setToTop(conv);
                } else {
                    //HandleResponseCode.onHandle(mContext.getActivity(), status, true);
                }
            }
        });
    }
}
