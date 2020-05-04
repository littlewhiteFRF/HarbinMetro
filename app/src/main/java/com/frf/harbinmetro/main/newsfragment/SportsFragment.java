package com.frf.harbinmetro.main.newsfragment;

/**
 * @author 方荣福
 * @date 2019/6/4
 * 体育新闻页面Fragment
 */
public class SportsFragment extends TabFragment {
    @Override
    public void onGetNewType() {
        getDataFromServer("tiyu");
    }
}
