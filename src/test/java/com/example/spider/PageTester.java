package com.example.spider;

import cn.hutool.core.util.ReUtil;
import com.example.spider.provider.cctv.dto.CctvNewsItem;
import com.example.spider.provider.cctv.dto.CctvNewsList;
import com.example.spider.util.JsonUtils;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

public class PageTester {

    private static RestTemplate rest = getInstance(StandardCharsets.UTF_8);

    public static void main(String[] args) {
        testCctvDetail();
    }

    private static void testCctvList(){
        String url = "http://news.cctv.com/2019/07/gaiban/cmsdatainterface/page/news_1.jsonp?cb=t&cb=news";
        String result = rest.getForObject(url, String.class);
        String prefix = "news({\"data\":";
        String suffix = "})";
        String json = result.substring(prefix.length(), result.length() - suffix.length());
        CctvNewsList newsList = JsonUtils.toObject(json, CctvNewsList.class);
        System.out.println(result);
    }

    private static void testCctvDetail(){
        String url = "http://news.cctv.com/2020/05/30/ARTIZlxguep2EzQJ2AWcaGWT200530.shtml";
        String html =  rest.getForObject(url, String.class);
//        String html = new String(bytes, StandardCharsets.ISO_8859_1);
        String suffix = "<!--repaste.body.end-->";
        Pattern pattern = Pattern.compile("<!--repaste.body.begin-->(.*?)<!--repaste.body.end-->", Pattern.DOTALL);
        List<String> reResult = ReUtil.findAll(pattern, html, 1);
        String context = reResult.get(0);
        //String context = (String) XmlUtil.getByXPath(contentXpathId, html, XPathConstants.STRING);
        System.out.println(context);
    }

    public static RestTemplate getInstance(Charset charset) {
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> list = restTemplate.getMessageConverters();
        for (HttpMessageConverter<?> httpMessageConverter : list) {
            if(httpMessageConverter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) httpMessageConverter).setDefaultCharset(charset);
                break;
            }
        }
        return restTemplate;
    }

}
