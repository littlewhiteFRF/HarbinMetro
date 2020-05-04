package com.frf.harbinmetro.main.newsfragment;

/**
 * @author 方荣福
 * @date 2019/6/4
 * 财经新闻页面Fragment
 */
public class FinanceFragment extends TabFragment {

    @Override
    public void onGetNewType() {
        getDataFromServer("caijing");
    }
}
