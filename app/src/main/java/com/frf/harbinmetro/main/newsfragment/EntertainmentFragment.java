package com.frf.harbinmetro.main.newsfragment;

/**
 * @author 方荣福
 * @date 2019/6/4
 * 娱乐新闻页面Fragment
 */

public class EntertainmentFragment extends TabFragment {
    @Override
    public void onGetNewType() {
        getDataFromServer("yule");
    }
}
