package com.github.common.util;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.common.R;

/**
 * company 重庆庆云石油工程技术有限责任公司
 * FileName ImageLoader
 * Package com.kingyun.common.util
 * Description ${DESCRIPTION}
 * author jiexiaoqiang
 * create 2019-01-15 9:46
 * version V1.0
 */
public class ImageLoader {
    /**
     * @param string    文件路径，或 uri 或 url
     * @param imageView 图片加载目标
     */
    public static void loadImage(String string, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(string)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.hb_image_loading_ic)
                        .error(R.drawable.hb_image_error_ic))
                .into(imageView);
    }

    /**
     * @param string    文件路径，或 uri 或 url
     * @param imageView 图片加载目标
     * @param holder    占位图
     */
    public static void loadImage(String string, ImageView imageView, int holder) {
        Glide.with(imageView.getContext())
                .load(string)
                .apply(new RequestOptions()
                        .placeholder(holder)
                        .error(holder))
                .into(imageView);
    }

    /**
     * 加载圆形图片
     *
     * @param string    文件路径，或 uri 或 url
     * @param imageView 图片加载目标
     */
    public static void loadCircleImage(String string, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(string)
                .apply(new RequestOptions()
                        .circleCrop()
                        .placeholder(R.drawable.hb_image_loading_ic)
                        .error(R.drawable.hb_image_error_ic))
                .into(imageView);
    }

    public static void loadCircleImage(String string, ImageView imageView, int holder) {
        Glide.with(imageView.getContext())
                .load(string)
                .apply(new RequestOptions()
                        .circleCrop()
                        .placeholder(holder)
                        .error(holder))
                .into(imageView);
    }

    public static void loadCircleImage(String string, ImageView imageView, RequestOptions requestOptions) {
        Glide.with(imageView.getContext())
                .load(string)
                .apply(requestOptions)
                .into(imageView);
    }

    public static void loadFitCenterImage(String string, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(string)
                .apply(new RequestOptions()
                        .fitCenter())
                .into(imageView);
    }
}
