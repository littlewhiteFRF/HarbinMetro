package com.frf.harbinmetro.main.newsfragment;

/**
 * @author 方荣福
 * @date 2019/6/4
 * 科技新闻页面Fragment
 */
public class TechnologyFragment extends TabFragment {
    @Override
    public void onGetNewType() {
        getDataFromServer("keji");
    }
}
