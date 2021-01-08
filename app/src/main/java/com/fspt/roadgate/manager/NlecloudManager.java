package com.fspt.roadgate.manager;

import android.content.Context;
import android.util.Log;

import com.fspt.roadgate.global.Constants;

import cn.com.newland.nle_sdk.requestEntity.SignIn;
import cn.com.newland.nle_sdk.responseEntity.User;
import cn.com.newland.nle_sdk.responseEntity.base.BaseResponseEntity;
import cn.com.newland.nle_sdk.util.NCallBack;
import cn.com.newland.nle_sdk.util.NetWorkBusiness;
import retrofit2.Call;


/**
 * 云平台操作类 设计为双检锁单例模式
 */
public class NlecloudManager {
    private String baseUrl = "https://api.nlecloud.com/";
    public String token = "";
    //在构造方法中调用登陆云平台的方法
    private static NlecloudManager mInstance;
    private Context mCtx;

    public static NlecloudManager getInstance(Context ctx) {
        if (mInstance == null) {
            synchronized (NlecloudManager.class) {
                if (mInstance == null)
                    mInstance = new NlecloudManager(ctx);
            }
        }
        return mInstance;
    }

    private NlecloudManager(Context ctx) {
        mCtx = ctx;
//        loginNLECloud(ctx);
    }


    //登陆云平台
    private void loginNLECloud(Context ctx) {
        final NetWorkBusiness netWorkBusiness = new NetWorkBusiness("", baseUrl);
        netWorkBusiness.signIn(new SignIn("xxxxxxxxxxx", "xxxxxxxxxxxxxx"), new NCallBack<BaseResponseEntity<User>>(ctx) {
            @Override
            protected void onResponse(BaseResponseEntity<User> response) {
                String accessToken = response.getResultObj().getAccessToken();
                token = accessToken;
                Constants.token = accessToken;
                Log.d("HHHH", " Login  onResponse: " + accessToken);
            }


            @Override
            public void onFailure(Call<BaseResponseEntity<User>> call, Throwable t) {
                Log.d("HHHH", "onFailure : ");
                super.onFailure(call, t);
            }
        });
    }


    /**
     * 开关LED灯
     *
     * @param isOn
     */

    public void onOffLight(final boolean isOn) {
        // 执行网络上报
        NetWorkBusiness netWorkBusiness = new NetWorkBusiness(Constants.token, Constants.CLOUD_URL);
        netWorkBusiness.control(Constants.DEVICE_ID, Constants.API_ALARM_LIGHT_ID, isOn, new NCallBack<BaseResponseEntity>(mCtx) {
            @Override
            protected void onResponse(BaseResponseEntity response) {
                if (response.getStatusCode() == 0) {
                    //调取网络开关 方法 ,再调用动画
                    // 成功
                    Log.d("HHHH", "OnOffLight Success " + isOn);
                }
            }
        });


    }


    //退出登录 ,无此方法
    public void logOut() {

    }
}
