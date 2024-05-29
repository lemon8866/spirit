package com.flower.spirit.config;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.flower.spirit.dao.CookiesConfigDao;
import com.flower.spirit.entity.BiliConfigEntity;
import com.flower.spirit.entity.ConfigEntity;
import com.flower.spirit.entity.TikTokConfigEntity;
import com.flower.spirit.entity.CookiesConfigEntity;
import com.flower.spirit.service.BiliConfigService;
import com.flower.spirit.service.ConfigService;
import com.flower.spirit.service.CookiesConfigService;
import com.flower.spirit.service.DownloaderService;
import com.flower.spirit.service.FfmpegQueueService;
import com.flower.spirit.service.TikTokConfigService;

/**
 * @author flower
 *程序初始化入口 加载常量信息到应用缓存
 */
@Configuration
public class AppConfig {
	
	private Logger logger = LoggerFactory.getLogger(AppConfig.class);
	
	@Autowired
	private DownloaderService downloaderService;
	
	@Autowired
	private ConfigService configService;
	

	@Autowired
	private BiliConfigService biliConfigService;
	
	@Autowired
	private TikTokConfigService  tikTokConfigService;
	
	@Autowired
	private FfmpegQueueService ffmpegQueueService;
	
	@Autowired
	private CookiesConfigService cookiesConfigService;
	
	
	@PostConstruct
	public void init() {
		downloaderService.renovate();
		ConfigEntity data = configService.getData();
		Global.apptoken =data.getApptoken();
		if(null != data.getOpenprocesshistory() && data.getOpenprocesshistory().equals("1")) {
			Global.openprocesshistory =true;
		}
		if(null != data.getWxboxpush() && !"".equals(data.getWxboxpush())) {
			Global.wxboxpush =data.getWxboxpush();
		}
		BiliConfigEntity bili = biliConfigService.getData();
		Global.bilicookies =bili.getBilicookies();
		if(null != bili.getBigmember() && bili.getBigmember().equals("是")) {
			Global.bilimember= true;
		}
		if(null != bili.getBitstream() && !"".equals(bili.getBitstream())) {
			Global.bilibitstream= bili.getBitstream();
		}
		TikTokConfigEntity tiktok = tikTokConfigService.getData();
		if(null !=tiktok.getCookies() && !"".equals(tiktok.getCookies())) {
			Global.tiktokCookie =tiktok.getCookies();
		}
		if(null != tiktok.getAnalysisserver() && !"".equals(tiktok.getAnalysisserver())) {
			Global.analysiSserver =tiktok.getAnalysisserver();
		}
		//新增cookies 配置信息
		CookiesConfigEntity cookies = cookiesConfigService.getData();
		if(cookies.getTwittercookies() != null && !cookies.getTwittercookies().equals("")) {
			Global.youtubecookies  = cookies.getYoutubecookies();
		}
		if(cookies.getYoutubecookies() != null && !cookies.getYoutubecookies().equals("")) {
			Global.twittercookies  = cookies.getTwittercookies();
		}
		//清空 ffmpeg 队列
		ffmpegQueueService.clearTask();
		logger.info("ffmpeg队列已清空");
		Global.initcomplete =true;
	}

}
