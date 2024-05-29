package com.flower.spirit.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.alibaba.fastjson.JSONObject;
import com.flower.spirit.config.Global;

public class WeChatUtil {
	
	private static ExecutorService wechat = Executors.newFixedThreadPool(1);
	
	public static void sendMessage(String title,String p,int type) {
		if(null !=Global.wxboxpush && !"".equals(Global.wxboxpush)) {
			wechat.execute(() ->{
				String message ="";
				if(type == 1) {
					message =message+"单视频下载完成通知 \n";
				}
				if(type == 2) {
					message =message+"收藏夹下载完成通知 \n";
				}
				message=message+"平台:"+p+" \n";
				message=message+"标题:"+title+" \n";
				message=message+"完成时间:"+DateUtils.getDateTime()+" \n";
				message=message+"备注:如使用aria2下载器,本通知发送成功不代表视频下载完成";
				JSONObject parms =  new JSONObject();
				parms.put("msgtype", "text");
				JSONObject content = new JSONObject();
				content.put("content", message);
				parms.put("text", content);
				HttpUtil.doPostNew(Global.wxboxpush, parms);
			});

		}

	}

}
