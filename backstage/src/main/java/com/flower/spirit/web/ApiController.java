package com.flower.spirit.web;


import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.flower.spirit.common.AjaxEntity;
import com.flower.spirit.config.Global;
import com.flower.spirit.entity.VideoDataEntity;
import com.flower.spirit.service.AnalysisService;
import com.flower.spirit.service.VideoDataService;

/**
 * api 调用控制器 此处控制器不拦截  仅通过token 校验
 * @author flower
 *
 */
@RestController
@RequestMapping("/api")
public class ApiController {
	
	@Autowired
	private AnalysisService analysisService;
	
//	private ExecutorService exec = Executors.newFixedThreadPool(1);
	
	@Autowired
	private VideoDataService videoDataService;
	
	
	/**
	 * 接受 视频平台的分享链接
	 * @param token
	 * @param video
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/processingVideos")
	@CrossOrigin
	public AjaxEntity processingVideos(String token,String video) throws Exception {
//		 analysisService.processingVideos(token,video);
		if(!Global.initcomplete) {
			return new AjaxEntity(Global.ajax_uri_error, "程序正在初始化", "");
		}
		analysisService.processingVideos(token,video);
		return new AjaxEntity(Global.ajax_success, "已提交,等待系统处理", "");
	
	}

	
	/**
	 * app 或者小程序 分页获取视频列表功能 接口
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping("/findVideos")
	public AjaxEntity findVideos(HttpServletRequest req,VideoDataEntity res) {
		String token = req.getParameter("token");
		if(null == token || !token.equals(Global.apptoken)) {
			return new AjaxEntity(Global.ajax_uri_error, "app token 错误", null);
		}
		return videoDataService.findPage(res);
	}
}
