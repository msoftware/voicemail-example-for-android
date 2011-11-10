/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */
package com.google.android.voicemail.example.receiver;

import com.google.android.voicemail.example.dependency.DependencyResolver;
import com.google.android.voicemail.example.dependency.DependencyResolverImpl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.android.voicemail.common.logging.Logger;

/**
 * A broadcast receiver which can receive OMTP format messages through an intent and handles it as
 * if a new OMTP message was received.
 * <p>
 * This intent can be generated by test scripts to send fake sync/mailbox-update messages to the
 * system. omtpsource/send_fake_omtp_msg.sh is a handy script which can be used for this purpose.
 */
public class FakeOmtpMessageReceiver extends BroadcastReceiver {
    public static final String ACTION_TEST_MESSAGE =
        "com.google.android.apps.vvm.action.FAKE_OMTP_MESSAGE";
    public static final String EXTRA_MESSAGE_BODY = "msg";

    private static final Logger logger = Logger.getLogger(FakeOmtpMessageReceiver.class);

    @Override
    public void onReceive(Context context, Intent intent) {
        DependencyResolver dependencyResolver = DependencyResolverImpl.getInstance();
        // Accept messages only in fake mode.
        if (!dependencyResolver.getUserSettings().isFakeModeEnabled()) {
            return;
        }
        if (ACTION_TEST_MESSAGE.equals(intent.getAction())) {
            String msgBody = intent.getStringExtra(EXTRA_MESSAGE_BODY);
            logger.d("Received msg: " + msgBody);
            if (msgBody != null) {
                dependencyResolver.createOmtpMessageHandler().process(msgBody);
            }
        }
    }
}
