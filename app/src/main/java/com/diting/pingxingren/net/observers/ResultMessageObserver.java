package com.diting.pingxingren.net.observers;

import com.diting.pingxingren.model.ResultMessageModel;
import com.diting.pingxingren.net.ResultCallBack;
import com.diting.pingxingren.util.Utils;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 08, 15:58.
 * Description: .
 */

public class ResultMessageObserver extends DefaultObserver<ResultMessageModel> {

    public ResultMessageObserver(ResultCallBack resultCallBack) {
        super(resultCallBack);
    }

    @Override
    public void onNext(ResultMessageModel resultMessageModel) {
        super.onNext(resultMessageModel);
        String resultMessage = resultMessageModel.getMessage();
        String result = resultMessageModel.getResult();
        if (Utils.isNotEmpty(result)) {
            if (result.equals("添加成功")) {
                mResultCallBack.onResult("内容添加成功.");
            } else {
                mResultCallBack.onError(result);
            }
        } else {
            if (resultMessage.contains("成功")) {
                if (resultMessage.equals("注册登录成功！")) {
                    mResultCallBack.onResult("注册成功.");
                } else if (resultMessage.equals("验证码获取成功!")) {
                    mResultCallBack.onResult("验证码已发送到手机.");
                } else if (resultMessage.equals("密码找回成功！")) {
                    mResultCallBack.onResult("找回成功, 请重新登录.");
                } else if (resultMessage.equals("密码修改成功！")) {
                    mResultCallBack.onResult("密码已修改.");
                } else if (resultMessage.equals("知识修改成功！")) {
                    mResultCallBack.onResult("知识修改成功.");
                } else if (resultMessage.equals("删除成功！")) {
                    mResultCallBack.onResult("知识删除成功.");
                } else {
                    mResultCallBack.onResult(resultMessage);
                }
            } else if (resultMessage.equals("success")) {
                mResultCallBack.onResult("子机器人创建成功.");
            } else {
                mResultCallBack.onError(resultMessage);
            }
        }
    }
}
