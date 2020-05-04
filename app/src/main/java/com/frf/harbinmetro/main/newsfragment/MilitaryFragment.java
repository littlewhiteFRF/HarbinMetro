package com.frf.harbinmetro.main.newsfragment;

/**
 * @author 方荣福
 * @date 2019/6/4
 * 军事新闻页面Fragment
 */
public class MilitaryFragment extends TabFragment {
    @Override
    public void onGetNewType() {
        getDataFromServer("junshi");
    }
}
