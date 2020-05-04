package com.frf.harbinmetro.main.newsfragment;

/**
 * @author 方荣福
 * @date 2019/6/4
 * 时尚新闻页面Fragment
 */
public class FashionFragment extends TabFragment {
    @Override
    public void onGetNewType() {
        getDataFromServer("shishang");
    }
}
