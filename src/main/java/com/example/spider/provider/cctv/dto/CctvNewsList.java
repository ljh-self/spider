package com.example.spider.provider.cctv.dto;

import lombok.Data;

import java.util.List;

/**
 * @author ljh
 */
@Data
public class CctvNewsList {
    private int total;
    private List<CctvNewsItem> list;
}