package com.frf.harbinmetro.main.newsfragment;

/**
 * @author 方荣福
 * @date 2019/6/4
 * 头条新闻页面Fragment
 */
public class TopFragment extends TabFragment {

    @Override
    public void onGetNewType() {
        getDataFromServer("top");
    }
}
