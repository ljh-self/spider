package com.example.spider.provider.cctv.dto;

import lombok.Data;

/**
 * @author ljh
 */
@Data
public class CctvNewsItem {
    private String id;
    private String url;
    private String title;
    private String keywords;
    private String brief;

    private String count;

    private String image;
    private String image2;
    private String image3;
}