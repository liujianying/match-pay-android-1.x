package com.fintech.match.pay.service.message;

import android.annotation.TargetApi;
import android.app.Notification;
import android.os.Build;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;

import com.fintech.match.pay.service.notification.ParsedNotification;

public abstract class NAbstractMessage extends IAbstractMessage {

    private ParsedNotification mContentNotification = null;

    private int mIconLevel = 0;

    private boolean mIsNotMessage = false;

    protected NAbstractMessage(int type) {
        super(type);
    }

    @TargetApi(18)
    public final NAbstractMessage buildMessage(StatusBarNotification statusBarNotification) {
        if ((statusBarNotification != null) && (statusBarNotification.getNotification() != null)) {

            //TODO flagsForbiddenMask
            if ((statusBarNotification.getNotification().flags & flagsForbiddenMask()) != 0) {
                setIsNotMessage();
            }
            if ((statusBarNotification.getNotification().flags & flagsAllowedMask()) != flagsAllowedMask()) {
                setIsNotMessage();
            }

            if (Build.VERSION.SDK_INT >= 21) {
                setKey(statusBarNotification.getKey());
            }

            long when = statusBarNotification.getNotification().when;
            long time = when;
            if (when <= 0L) {
                when = statusBarNotification.getPostTime();
                time = when;
                if (when <= 0L) {
                    time = System.currentTimeMillis();
                }
            }
            setTime(time);

            Notification notification = statusBarNotification.getNotification();

            ParsedNotification parsedNotification = new ParsedNotification(notification);
            setContentNotification(parsedNotification);
            //----------------------------------
            setId(statusBarNotification.getId());
            setPackageName(statusBarNotification.getPackageName());
            setTag(statusBarNotification.getTag());
            setIconLevel(notification.iconLevel);

            setTitle(parsedNotification.getmExtras().getString("android.title"));
            setContent(parsedNotification.getmTickerText());

            rebuild();
        }

        return this;
    }

    protected void copyFromMessage(IMessage iMessage) {
        super.copyFromMessage(iMessage);
        if ((iMessage instanceof NAbstractMessage)) {
            NAbstractMessage message = (NAbstractMessage) iMessage;
            setContentNotification(message.getContentNotification());
            setIconLevel(message.getIconLevel());
            if (message.isNotMessage()) {
                setIsNotMessage();
            }
        }
    }

    protected int flagsAllowedMask() {
        return 0;
    }

    protected int flagsForbiddenMask() {
        return 98;
    }

    public final ParsedNotification getContentNotification() {
        return this.mContentNotification;
    }

    private void setContentNotification(ParsedNotification paramParsedNotification) {
        this.mContentNotification = paramParsedNotification;
    }

    protected final int getIconLevel() {
        return this.mIconLevel;
    }

    private void setIconLevel(int paramInt) {
        this.mIconLevel = paramInt;
    }

    private void setIsNotMessage() {
        this.mIsNotMessage = true;
    }

    public final boolean isNotMessage() {
        return this.mIsNotMessage;
    }

    protected final boolean isSameMessage(IAbstractMessage kAbstractMessage) {
        return ((kAbstractMessage instanceof NAbstractMessage)) && (isSameMessage((NAbstractMessage) kAbstractMessage));
    }

    protected boolean isSameMessage(NAbstractMessage nAbstractMessage) {
        return getTitle().equals(nAbstractMessage.getTitle());
    }

    protected abstract void rebuild();

}
