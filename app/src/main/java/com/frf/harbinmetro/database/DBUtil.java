package com.frf.harbinmetro.database;

import android.text.TextUtils;

import com.frf.harbinmetro.contact.model.QuestionInfo;
import com.frf.harbinmetro.lostfind.model.LostFindInfo;
import com.frf.harbinmetro.notification.model.NotificationInfo;

import java.util.List;

/**
 * 解析和处理从服务器所获得的数据
 */
public class DBUtil {
    /**
     * 将从服务器获得数据存储到SQLite的lostfindtable中
     * @param harbinMetroDB
     * @param lostFindInfo
     * @return
     */
    public synchronized static boolean handleLostFindResponse(HarbinMetroDB harbinMetroDB, List<LostFindInfo> lostFindInfo) {
        if(lostFindInfo != null) {
            for(int i=0;i<lostFindInfo.size();i++){
                LostFindInfo lostFind = lostFindInfo.get(i);
                //将解析出来的数据存储到lostfindtable
                harbinMetroDB.saveLostFindInfo(lostFind);
            }
            return true;
        }
        return false;
    }

    /**
     * 将从服务器获得数据存储到SQLite的notificationtable中
     * @param harbinMetroDB
     * @param notificationInfo
     * @return
     */
    public synchronized static boolean handleNotificationResponse(HarbinMetroDB harbinMetroDB, List<NotificationInfo> notificationInfo) {
        if(notificationInfo != null) {
            for(int i=0;i<notificationInfo.size();i++){
                NotificationInfo notification = notificationInfo.get(i);
                //将解析出来的数据存储到notificationtable
                harbinMetroDB.saveNotificationInfo(notification);
            }
            return true;
        }
        return false;
    }

    /**
     * 将从服务器获得数据存储到SQLite的questiontable中
     * @param harbinMetroDB
     * @param questionInfo
     * @return
     */
    public synchronized static boolean handleQuestionResponse(HarbinMetroDB harbinMetroDB, List<QuestionInfo> questionInfo) {
        if(questionInfo != null) {
            for(int i=0;i<questionInfo.size();i++){
                QuestionInfo question = questionInfo.get(i);
                //将解析出来的数据存储到questiontable
                harbinMetroDB.saveQuestionInfo(question);
            }
            return true;
        }
        return false;
    }
}
