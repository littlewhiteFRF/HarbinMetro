package com.frf.harbinmetro.main.newsfragment;

/**
 * @author 方荣福
 * @date 2019/6/4
 * 社会新闻页面Fragment
 */
public class SocietyFragment extends TabFragment {
    @Override
    public void onGetNewType() {
        getDataFromServer("shehui");
    }
}