package com.baimei.jmessage;

import android.app.Activity;
import android.os.Bundle;

import org.apache.cordova.*;

import cn.jmessage.android.uikit.chatting.utils.IdHelper;
import cn.jmessage.android.uikit.conversation.ConversationListFragment;
import cn.jmessage.android.uikit.chatting.BaseActivity;

public class ConvListMainActivity extends BaseActivity {

    private ConversationListFragment converListFgt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(IdHelper.getLayout(this, "jpush_conv_list_main"));

        converListFgt = new ConversationListFragment();
        addFragment(IdHelper.getViewID(this, "converPanel"), converListFgt);
    }
}
