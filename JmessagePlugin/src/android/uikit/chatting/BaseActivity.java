package cn.jmessage.android.uikit.chatting;

import java.io.File;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import cn.jmessage.android.uikit.chatting.utils.DialogCreator;
import cn.jmessage.android.uikit.chatting.utils.FileHelper;
import cn.jmessage.android.uikit.chatting.utils.IdHelper;
import cn.jmessage.android.uikit.chatting.utils.SharePreferenceManager;
import cn.jmessage.android.uikit.utils.PhoneUtil;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.LoginStateChangeEvent;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by Ken on 2015/3/13.
 */
public class BaseActivity extends FragmentActivity {
    private static final String TAG = "BaseActivity";

    protected float mDensity;
    protected int mDensityDpi;
    protected int mAvatarSize;
    protected int mWidth;
    protected int mHeight;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        JMessageClient.init(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //订阅接收消息,子类只要重写onEvent就能收到
        JMessageClient.registerEventReceiver(this);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mDensity = dm.density;
        mDensityDpi = dm.densityDpi;
        mWidth = dm.widthPixels;
        mHeight = dm.heightPixels;
        mAvatarSize = (int) (50 * mDensity);
    }

    private Dialog dialog;

    private UserInfo myInfo;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog.dismiss();
//            Intent intent = new Intent();
//            intent.setClass(BaseActivity.this, DemoActivity.class);
//            startActivity(intent);
//            BaseActivity.this.finish();
        }
    };

    /**
     * 接收登录状态相关事件:登出事件,修改密码事件及被删除事件
     * @param event 登录状态相关事件
     */
    public void onEventMainThread(LoginStateChangeEvent event) {
        LoginStateChangeEvent.Reason reason = event.getReason();
        myInfo = event.getMyInfo();
        if (null != myInfo) {
            String path;
            File avatar = myInfo.getAvatarFile();
            if (avatar != null && avatar.exists()) {
                path = avatar.getAbsolutePath();
            } else {
                path = FileHelper.getUserAvatarPath(myInfo.getUserName());
            }
            Log.i(TAG, "userName " + myInfo.getUserName());
            SharePreferenceManager.setCachedUsername(myInfo.getUserName());
            SharePreferenceManager.setCachedAvatarPath(path);
            JMessageClient.logout();
        }
        switch (reason) {
            case user_password_change:
//                String title = IdHelper.getString(mContext, "change_password");
//                String msg = mContext.getString(R.string.change_password_message);
//                dialog = DialogCreator.createBaseCustomDialog(mContext, title, msg, onClickListener);
                break;
            case user_logout:
                String title = mContext.getString(IdHelper.getString(mContext,
                        "jmui_user_logout_dialog_title"));
                String msg = mContext.getString(IdHelper.getString(mContext, "jmui_user_logout_dialog_message"));
                dialog = DialogCreator.createBaseCustomDialog(mContext, title, msg, onClickListener);
                break;
            case user_deleted:
                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
//                        Intent intent = new Intent();
//                        intent.setClass(BaseActivity.this, DemoActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);
//                        BaseActivity.this.finish();
                    }
                };
                title = mContext.getString(IdHelper.getString(mContext, "jmui_user_logout_dialog_title"));
                msg = mContext.getString(IdHelper.getString(mContext, "jmui_user_delete_hint_message"));
                dialog = DialogCreator.createBaseCustomDialog(mContext, title, msg, listener);
                break;
        }
        dialog.getWindow().setLayout((int) (0.8 * mWidth), WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        JMessageClient.unRegisterEventReceiver(this);
        if (dialog != null){
            dialog.dismiss();
        }
        super.onDestroy();
    }




    /**
     * @directions 获取View
     * @param activity
     * @param id
     * @return T
     */
    public <T extends View> T getView(Activity activity, int id) {
        View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        View childView = rootView.findViewById(id);
        return (T) childView;
    }

    /**
     * @directions 获取View
     * @param view
     * @param id
     * @return T
     */
    public <T extends View> T getView(View view, int id) {
        View childView = view.findViewById(id);
        return (T) childView;
    }


    /**
     * 移除容器中所有视图
     */
    protected void removeAllViewFromContanier(View view, int resId) {
        FrameLayout searchContanier = getView(view, resId);
        searchContanier.removeAllViews();
    }

    @TargetApi(19)
    private boolean isFragmentAddable() {

        boolean addable = true;

        Activity activity = this;

        if (null == activity) {
            return false;
        }

        // fix bug: java.lang.IllegalStateException: Activity has been destroyed
        if (PhoneUtil.getSdkVersionInt() < 17) {
            if (activity.isFinishing()) {
                addable = false;
            }
        } else {
            //  || activity.isDestroyed()
            if (activity.isFinishing()) {
                addable = false;
            }
        }
        return addable;
    }

    /**
     * 添加Fragment
     *
     * @directions
     * @author superman
     */
    protected void addFragment(int container, Fragment fragment) {
        if (fragment != null && !fragment.isAdded()) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(container, fragment);
            fragmentTransaction.addToBackStack(fragment.getClass().getName());
            fragmentTransaction.commit();
        }
    }

    /**
     * 删除Fragment
     *
     * @directions
     * @author superman
     */
    protected void removeFragment(Fragment fgt) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(fgt);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * 替换Fragment
     *
     * @directions
     * @author superman
     */
    protected void replaceFragment(int container, Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
