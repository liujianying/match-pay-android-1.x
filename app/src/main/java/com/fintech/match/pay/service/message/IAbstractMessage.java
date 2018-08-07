package com.fintech.match.pay.service.message;

public abstract class IAbstractMessage implements IMessage {

    float mAmount;

    private String mContent;

    private int mId;

    private String mKey;

    private String mPackageName;

    private String mTag;

    private long mTime;

    private String mTitle;

    private int mType;

    public IAbstractMessage(int type) {
        setType(type);
        clear();
    }

    private void clear() {
        setPackageName(null);
        setTime(0L);
        setTitle(null);
        setContent(null);
        setId(0);
        setTag(null);
    }

    protected void copyFromMessage(IMessage iMessage) {
        setType(iMessage.getType());
        setPackageName(iMessage.getPackageName());
        setTime(iMessage.getTime());
        setTitle(iMessage.getTitle());
        setContent(iMessage.getContent());
        setId(iMessage.getId());
        setTag(iMessage.getTag());
    }

    public void setAmount(float amount) {
        this.mAmount = amount;
    }

    public float getAmount() {
        return this.mAmount;
    }

    public void setKey(String mKey) {
        this.mKey = mKey;
    }

    public String getKey() {
        return this.mKey;
    }

    public void setPackageName(String packageName) {
        this.mPackageName = packageName;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public void setTag(String tag) {
        this.mTag = tag;
    }

    public String getTag() {
        return this.mTag;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public void setType(int type) {
        this.mType = type;
    }

    public int getType() {
        return this.mType;
    }

    public int getId() {
        return this.mId;
    }

    protected void setId(int mId) {
        this.mId = mId;
    }

    protected final void setContent(String content) {
        this.mContent = content;
    }

    public final String getContent() {
        return this.mContent;
    }

    protected final void setTime(long time) {
        long l = time;
        if (time < 0L) {
            l = System.currentTimeMillis();
        }
        this.mTime = l;
    }

    public long getTime() {
        return this.mTime;
    }

    @Override
    public String toString() {
        return "IAbstractMessage{" +
                "mAmount=" + mAmount +
                ", mContent='" + mContent + '\'' +
                ", mId=" + mId +
                ", mKey='" + mKey + '\'' +
                ", mPackageName='" + mPackageName + '\'' +
                ", mTag='" + mTag + '\'' +
                ", mTime=" + mTime +
                ", mTitle='" + mTitle + '\'' +
                ", mType=" + mType +
                '}';
    }
}

