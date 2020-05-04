package com.frf.harbinmetro.main.searchfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.frf.harbinmetro.R;
import com.frf.harbinmetro.contact.ContactActivity;
import com.frf.harbinmetro.lostfind.LostFindActivity;
import com.frf.harbinmetro.metromap.MetroMapActivity;
import com.frf.harbinmetro.metroticket.MetroTicketActivity;
import com.frf.harbinmetro.metrotime.MetroTimeActivity;
import com.frf.harbinmetro.notification.NotificationActivity;
import com.frf.harbinmetro.saleticket.SaleTicketActivity;
import com.frf.harbinmetro.searchline.SearchLineActivity;
import com.frf.harbinmetro.searchstation.SearchStationActivity;
import com.frf.harbinmetro.web.WebActivity;

public class SearchFragment extends Fragment {
    
    private CardView cardView_search_line;//线路查询卡片
    private CardView cardView_search_station;//站点查询卡片
    private CardView cardView_news_notification;//地铁公告卡片
    private CardView cardView_lost_find;//失物招领卡片
    private CardView cardView_contact_assistant;//热心帮助卡片
    private CardView cardView_map;//地铁线网卡片
    private CardView cardView_ticket;//地铁票价卡片
    private CardView cardView_time;//首末班车卡片
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        cardView_search_line = getActivity().findViewById(R.id.search_fragment_card_view_search_line);//线路查询卡片
        cardView_search_station = getActivity().findViewById(R.id.search_fragment_card_view_search_station);//站点查询卡片
        cardView_news_notification = getActivity().findViewById(R.id.search_fragment_card_view_notification);//地铁公告卡片
        cardView_lost_find = getActivity().findViewById(R.id.search_fragment_card_view_lost_find);//失物招领卡片
        cardView_contact_assistant = getActivity().findViewById(R.id.search_fragment_card_view_contact_assistant);//热心帮助卡片
        cardView_map = getActivity().findViewById(R.id.search_fragment_card_view_map);//地铁线网卡片
        cardView_ticket = getActivity().findViewById(R.id.search_fragment_card_view_ticket);//地铁票价卡片
        cardView_time = getActivity().findViewById(R.id.search_fragment_card_view_time);//首末班车卡片
        
        cardView_search_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "你点击了线路查询功能", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(getActivity(), SearchLineActivity.class);
                startActivity(intent1);
            }
        });
        cardView_search_station.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "你点击了站点查询功能", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(getActivity(), SearchStationActivity.class);
                startActivity(intent2);
            }
        });
        cardView_news_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "你点击了地铁公告功能", Toast.LENGTH_SHORT).show();
                Intent intent3 = new Intent(getActivity(), NotificationActivity.class);
                startActivity(intent3);
            }
        });
        cardView_lost_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "你点击了失物招领功能", Toast.LENGTH_SHORT).show();
                Intent intent4 = new Intent(getActivity(), LostFindActivity.class);
                startActivity(intent4);
            }
        });
        cardView_contact_assistant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "你点击了热心连线功能", Toast.LENGTH_SHORT).show();
                Intent intent5 = new Intent(getActivity(), ContactActivity.class);
                startActivity(intent5);
            }
        });
        cardView_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "你点击了地铁线网功能", Toast.LENGTH_SHORT).show();
                Intent intent6 = new Intent(getActivity(), MetroMapActivity.class);
                //https://m.huoche.net/haerbindtskb/100/
                intent6.putExtra("url", "https://www.huoche.net/images/ditie/20180629/23.jpg");
                startActivity(intent6);
                //https://www.huoche.net/images/ditie/20180629/23.jpg
            }
        });
        cardView_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "你点击了地铁票价功能", Toast.LENGTH_SHORT).show();
                Intent intent7 = new Intent(getActivity(), MetroTicketActivity.class);
                startActivity(intent7);
            }
        });
        cardView_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "你点击了首末班车功能", Toast.LENGTH_SHORT).show();
                Intent intent8 = new Intent(getActivity(), MetroTimeActivity.class);
                startActivity(intent8);
            }
        });


    }
}
