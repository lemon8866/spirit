package com.flower.spirit.utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.flower.spirit.config.Global;

public class DouUtil {
	
	private static Logger logger = LoggerFactory.getLogger(DouUtil.class);
	
	//  备用
	
	private static String ttwid ="https://ttwid.bytedance.com/ttwid/union/register/";  //ttwid申请 
	
	//private static JSONObject ttwidData =  JSONObject.parseObject("{\"region\":\"cn\",\"aid\":1768,\"needFid\":false,\"service\":\"www.ixigua.com\",\"migrate_info\":{\"ticket\":\"\",\"source\":\"node\"},\"cbUrlProtocol\":\"https\",\"union\":true}");  //需要配合ttwid 使用
	
	public static String ua="Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36";
	public static String  odin_tt ="324fb4ea4a89c0c05827e18a1ed9cf9bf8a17f7705fcc793fec935b637867e2a5a9b8168c885554d029919117a18ba69";
	public static String  passport_csrf_token ="2f142a9bb5db1f81f249d6fc997fe4a1";
	public static String referer =  "https://www.douyin.com/";
	public static  Map<String, String>  downVideo(String url) {
		Document document = null;
		Map<String, String>  data = new HashMap<String, String>();
		try {
			 document = Jsoup.connect(url).userAgent(ua).get();
			 String baseUri = document.baseUri();
			 String pattern_v1="(?<=/video/).*?(?=/)";
			 String pattern_v2 = "/video/(\\d+)";
		     Pattern compile_v1 = Pattern.compile(pattern_v1);
		     Pattern compile_v2 = Pattern.compile(pattern_v2);
		     Matcher matcher_v1 = compile_v1.matcher(baseUri);
		     Matcher matcher_v2 = compile_v2.matcher(baseUri);
		     if(matcher_v1.find()) {
		    	 String vedioId = matcher_v1.group(0);
		    	 logger.info("DouYin vedioId="+vedioId);
		    	 data  = DouUtil.getBogus(vedioId,"local");
		    	  if(data != null) {
		    		  logger.info("接口解析数据"+data);
		    		  return data;
		    	  }else {
		    		  //失败 使用htmlclient
		    		  return null;
//		    		  return DouUtil.htmlclient(url);
		    	  }
		     }
		     if(matcher_v2.find()) {
		    	 String vedioId = matcher_v2.group(0).replaceAll("video", "").replaceAll("/", "");
		    	 logger.info("DouYin vedioId="+vedioId);
		    	 data  = DouUtil.getBogus(vedioId,"local");
		    	  if(data != null) {
		    		  logger.info("接口解析数据"+data);
		    		  return data;
		    	  }else {
		    		  //失败 使用htmlclient
		    		  return null;
		    	  }
		     }
		     if(!matcher_v2.find() && !matcher_v1.find()) {
		    	 return null;
		     }
			
		} catch (IOException e1) {
			logger.info("解析异常"+e1.getMessage());
			return null;
			//return DouUtil.htmlclient(url);
		}
		return null;
	}
//	public static  Map<String, String> htmlclient(String url) {
//		 logger.info("WebClient客户端开始启动");
//		 Map<String, String> res = new HashMap<String, String>();
//		 WebClient webClient = ThreadConfig.getWebClient();
//	        HtmlPage page = null;
//	        try {
//	            page = webClient.getPage(url);
//		        webClient.waitForBackgroundJavaScript(300);
//		        String pageXml = page.asXml();
//		        Document parse = Jsoup.parse(pageXml);
//		        Element render_data = parse.getElementById("RENDER_DATA");
//		        String encode = URLDecoder.decode(render_data.html().substring("//<![CDATA[".length(), render_data.html().length() - "//]]>".length()).trim(), "UTF-8");
//		        JSONObject jsonObject = JSON.parseObject(encode);
////		        System.out.println(jsonObject);
//		        jsonObject.forEach((key, value) -> {
//		        	if(DouUtil.isJSONString(value.toString())) {
//		        		  JSONObject aweme = JSONObject.parseObject(value.toString()).getJSONObject("aweme");
//		        		  if(aweme != null) {
//		        			  JSONObject detail = aweme.getJSONObject("detail");
//		        			  String awemeId = detail.getString("awemeId");
//		        			  String desc = detail.getString("desc");
//		        			  JSONObject videoobj = detail.getJSONObject("video");
//		        	          String playApi = videoobj.getString("playApi");
//		        	          String cover = videoobj.getString("cover");
//		        	          res.put("cover", cover);
//		        	     	  res.put("awemeid", awemeId);
//		        			  res.put("videoplay", playApi);
//		        			  res.put("desc", desc);
//		        			  res.put("type", "client");
//		        		  }
//		        	}
//					
//				});
//	        } catch (Exception e) {
//	        	logger.info("获取不到");
//	        }finally {
//				//如果后续观察内存占用问题比较大 考虑取消此处注释
//				webClient.getCurrentWindow().getJobManager().removeAllJobs();
//				webClient.getCurrentWindow().getJobManager().shutdown();
//				webClient.close();
//				System.gc();
//	        }
//	        logger.info("下载流程结束");
//	        return res;
//	
//	}
	
	/**
	 * 获取xBogus 并获取视频数据
	 * @param aweme_id
	 * @param type
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public static  Map<String, String> getBogus(String aweme_id,String type) throws HttpException, IOException {
		 Map<String, String> res = new HashMap<String, String>();
		 if(null !=Global.tiktokCookie && !"".equals(Global.tiktokCookie) ) {
//			 String cookie = simplifycookie(Global.tiktokCookie);
			 String cookie = Global.tiktokCookie;
			 Map<String, String> generatetoken = generatetoken(aweme_id);
			 String httpget = DouUtil.httpget(generatetoken.get("url").trim(),cookie);
			 JSONObject data = JSONObject.parseObject(httpget);
			 JSONObject aweme_detail = data.getJSONObject("aweme_detail");
			 String coveruri = "";
			 JSONArray cover = aweme_detail.getJSONObject("video").getJSONObject("cover").getJSONArray("url_list");
			 if(cover.size() >=2) {
				 coveruri = cover.getString(cover.size()-1);
			 }else {
				 coveruri = cover.getString(0);
			 }
			 JSONArray jsonArray = aweme_detail.getJSONObject("video").getJSONObject("play_addr").getJSONArray("url_list");
			 String videoplay = "";
			 if(jsonArray.size() >=2) {
				 videoplay = jsonArray.getString(jsonArray.size()-1);
			 }else {
				 videoplay = jsonArray.getString(0);
			 }
			 String desc = aweme_detail.getString("desc");
			 res.put("awemeid", aweme_id);
			 res.put("videoplay", videoplay);
			 res.put("desc", desc);
			 res.put("cover", coveruri);
			 res.put("cookie", cookie.trim());
			 res.put("type", "api");
			 return res;
		 }
		 return null;

	}
	
	/**
	 * 
	 * 已废弃接口  目前需要填写用户cookie 所以暂时这个接口不用了
	 * @param aweme_id
	 * @param type
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	@Deprecated
	public static  Map<String, String> getBogusDiscard(String aweme_id,String type) throws HttpException, IOException {
		Map<String, String> res = new HashMap<String, String>();
		 String url ="";
		 String cookie ="";
		 String code = "";
		// 2023.07.10 优先本地模式  懒得改了 先这样写
		if(type.equals("local")) {
			Map<String, String> generatetoken = generatetoken(aweme_id);
			try {
				if(generatetoken != null) {
					logger.info("使用本地生成xBogus");
					code="200";
					url = generatetoken.get("url");
					cookie = generatetoken.get("cookie");
				}else {
					 logger.info("本地生成异常(空信息)--正在使用remote模式");
					 return getBogus(aweme_id, "remote");
				}
			} catch (Exception e) {
				 logger.info("本地生成异常--正在使用remote模式");
				 return getBogus(aweme_id, "remote");
			}
		}else {
			logger.info("使用远程生成xBogus");
			JSONObject token = HttpUtil.doPostNew(Global.analysiSserver+"/spirit-token", DouUtil.generateparameters(aweme_id));
			code = token.getString("code");
			if(code.equals("200")) {
				code="200";
				url = token.getJSONObject("data").getString("url");
				cookie = token.getJSONObject("data").getString("cookie"); 
			}
		}
		// 2023.07.10 优先本地模式  懒得改了 先这样写
		if(code.equals("200")) {
			 String httpget = DouUtil.httpget(url.trim(), cookie.trim());
			 JSONObject data = JSONObject.parseObject(httpget);
			 if(null == data) {
				 return getBogus(aweme_id, "remote");
			 }
			 JSONObject aweme_detail = data.getJSONObject("aweme_detail");
			 if(null == aweme_detail && type.equals("local")) {
				 return getBogus(aweme_id, "remote");
			 }
			 String coveruri = "";
			 JSONArray cover = aweme_detail.getJSONObject("video").getJSONObject("cover").getJSONArray("url_list");
			 if(cover.size() >=2) {
				 coveruri = cover.getString(cover.size()-1);
			 }else {
				 coveruri = cover.getString(0);
			 }
			 JSONArray jsonArray = aweme_detail.getJSONObject("video").getJSONObject("play_addr").getJSONArray("url_list");
			 String videoplay = "";
			 if(jsonArray.size() >=2) {
				 videoplay = jsonArray.getString(jsonArray.size()-1);
			 }else {
				 videoplay = jsonArray.getString(0);
			 }
			 String desc = aweme_detail.getString("desc");
			 res.put("awemeid", aweme_id);
			 res.put("videoplay", videoplay);
			 res.put("desc", desc);
			 res.put("cover", coveruri);
			 res.put("cookie", cookie.trim());
			 res.put("type", "api");
			 return res;
		}
		return null;

		
	}
	/**
	 * get 请求 接口数据
	 * @param addr
	 * @param ck
	 * @return
	 * @throws IOException
	 */
	public static String httpget(String addr,String ck) throws IOException {
		String cookie = ck;
        String urlString = addr;    
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("user-agent", ua);
        conn.setRequestProperty("referer", "https://www.douyin.com/");
        conn.setRequestProperty("cookie", cookie);
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
	}
	
	

	
	/**
	 * 构建请求数据
	 * @param aweme_id
	 * @return
	 */
	public static JSONObject generateparameters(String aweme_id) {
		JSONObject data =  new JSONObject();
		data.put("awemeid", aweme_id);
		data.put("ua", ua);
		return data;
	}

	
	/**
	 * 判断是否为json
	 * @param str
	 * @return
	 */
	@SuppressWarnings("unused")
	public static boolean isJSONString(String str) {
	    boolean result = false;
	    try {
	    	JSONObject obj=JSONObject.parseObject(str);
	        result = true;
	    } catch (Exception e) {
	        result=false;
	    }
	    return result;
	}
	
	
	/**
	 * 生成Xbogus并返回 请求URL
	 * @param aid
	 * @return
	 */
	public static Map<String, String> generatetoken(String aid) {
		String url ="https://www.douyin.com/aweme/v1/web/aweme/detail/?aweme_id=#awemeid#&aid=1128&version_name=23.5.0&device_platform=android&os_version=2333&X-Bogus=#bogus#";
		Map<String, String> res = new HashMap<String, String>();
//	    String urlPath = "aweme_id="+aid+"&aid=1128&cookie_enabled=true&platform=android&downlink=10";
	    String urlPath = "aweme_id="+aid+"&aid=1128&version_name=23.5.0&device_platform=android&os_version=2333";
		try {
			String xbogusToken = XbogusUtil.getXBogus(urlPath);
			String queryurl = url.replace("#awemeid#",aid).replace("#bogus#",xbogusToken);
			res.put("xbogus", xbogusToken);
			res.put("url", queryurl);
			return res;
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
	
	/**
	 * 
	 * 获取Ttwid 其实可以加缓存 复用ttwid  暂时没有加缓存
	 * 后续添加
	 * @return
	 */
	public static String getTtwid() {
        try {
            String data = "{\"region\":\"cn\",\"aid\":1768,\"needFid\":false,\"service\":\"www.ixigua.com\",\"migrate_info\":{\"ticket\":\"\",\"source\":\"node\"},\"cbUrlProtocol\":\"https\",\"union\":true}";
            URL url = new URL(ttwid);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("user-agent", ua);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data.getBytes());
            outputStream.flush();
            outputStream.close();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                String setCookie = connection.getHeaderField("Set-Cookie");
                if (setCookie != null) {
                	  String[] parts = setCookie.split(";");
                      for (String part : parts) {
                          if (part.trim().startsWith("ttwid=")) {
                              String[] keyValue = part.split("=", 2);
                              if (keyValue.length == 2) {
                                  return keyValue[1].trim();
                              }
                          }
                      }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
	/**
	 * 简化cookie
	 * @param cookie
	 * @return
	 */
	public static String simplifycookie(String cookie) {
		Map<String, String> parseCookieString = parseCookieString(cookie);
		String ck ="odin_tt="+parseCookieString.get("odin_tt")+";sessionid_ss="+parseCookieString.get("sessionid_ss")+";ttwid="+parseCookieString.get("ttwid")+";passport_csrf_token="+parseCookieString.get("passport_csrf_token")+";msToken="+parseCookieString.get("msToken")+";";
		return ck;
		
	}
	
	
	/**
	 * cookie 转map
	 * @param cookieString
	 * @return
	 */
	public static Map<String, String> parseCookieString(String cookieString) {
        Map<String, String> cookieMap = new HashMap<>();

        if (cookieString != null && !cookieString.isEmpty()) {
            String[] cookiePairs = cookieString.split("; ");
            for (String cookiePair : cookiePairs) {
                String[] parts = cookiePair.split("=");
                if (parts.length == 2) {
                    String name = parts[0];
                    String value = parts[1];
                    cookieMap.put(name, value);
                }
            }
        }

        return cookieMap;
    }
	
	
	 public static String getFp() {
		 String e = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	        int t = e.length();
	        long milliseconds = System.currentTimeMillis();
	        StringBuilder base36 = new StringBuilder();

	        while (milliseconds > 0) {
	            int remainder = (int) (milliseconds % 36);
	            if (remainder < 10) {
	                base36.insert(0, remainder);
	            } else {
	                base36.insert(0, (char) (Character.valueOf('a') + remainder - 10));
	            }
	            milliseconds = milliseconds / 36;
	        }
	        String r = base36.toString();
	        char[] o = new char[36];
	        o[8] = o[13] = o[18] = o[23] = '_';
	        o[14] = '4';

	        Random random = new Random();
	        for (int i = 0; i < 36; i++) {
	            if (o[i] == 0) {
	                int n = random.nextInt(t);
	                if (i == 19) {
	                    n = 3 & n | 8;
	                }
	                o[i] = e.charAt(n);
	            }
	        }
	        StringBuilder ret = new StringBuilder("verify_" + r + "_");
	        ret.append(o);
	        return ret.toString();
	    }

	public static void main(String[] args) {
		Map<String, String> generatetoken = DouUtil.generatetoken("7221047525594139944");
		System.out.println(generatetoken);
		
	}

	
}
