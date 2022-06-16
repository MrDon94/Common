package com.github.common.widget;

import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择列表文字的对话框
 */
public class SelectItemDialog<T extends BaseSelectTextInfo> {

    private final MaterialDialog mDialog;
    private final List<T> data;

    public SelectItemDialog(Context context, List<T> data) {
        this(context,data,false);
    }

    /**
     *
     * @param context
     * @param data
     * @param multiChoice 是否多选
     */
    public SelectItemDialog(Context context, List<T> data, boolean multiChoice){
        if (data == null){
            data = new ArrayList<>();
        }
        this.data = data;
        List<String> textItems = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            T t = data.get(i);
            textItems.add(t.getText());
        }
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context)
                .items(textItems);
        if (multiChoice){
            builder.itemsCallbackMultiChoice(null, (dialog, which, text) -> {
                if (mMultiChoiceListener != null){
                    List<T> result = new ArrayList<>();
                    for (Integer i : which) {
                        if (i < this.data.size()){
                            T item = this.data.get(i);
                            result.add(item);
                        }
                    }
                    mMultiChoiceListener.onListCallbackMultiChoice(result);
                }
                return true;
            }).positiveText("确定");
        }else {
            builder.itemsCallback((dialog, itemView, position, text) -> {
                if (mOnItemClickListener != null && position < this.data.size()){
                    T item = this.data.get(position);
                    if (item != null){
                        mOnItemClickListener.onClick(item);
                    }
                }
            });
        }
        mDialog = builder.build();
    }

    public void show(){
        if (mDialog != null){
            mDialog.show();
        }
    }

    public void dismiss(){
        if (mDialog != null){
            mDialog.dismiss();
        }
    }

    private OnItemClickListener<T> mOnItemClickListener;

    public SelectItemDialog<T> setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
        return this;
    }

    /**
     * 单选监听回调接口
     * @param <T>
     */
    public interface OnItemClickListener<T extends BaseSelectTextInfo> {
        void onClick(@NonNull T item);
    }

    private OnCallbackMultiChoiceListener<T> mMultiChoiceListener;

    public SelectItemDialog<T> setMultiChoiceListener(OnCallbackMultiChoiceListener<T> multiChoiceListener) {
        mMultiChoiceListener = multiChoiceListener;
        return this;
    }

    /**
     * 多选监听回调接口
     * @param <T>
     */
    public interface OnCallbackMultiChoiceListener<T extends BaseSelectTextInfo> {
        void onListCallbackMultiChoice(@NonNull List<T> data);
    }

}
