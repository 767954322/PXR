package com.diting.pingxingren.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.diting.pingxingren.R;
import com.diting.pingxingren.app.MyApplication;
import com.diting.pingxingren.custom.MyURLSpan;
import com.diting.pingxingren.easypermissions.EasyPermissions;
import com.diting.pingxingren.entity.AppInfo;
import com.diting.pingxingren.entity.ChatUser;
import com.diting.pingxingren.entity.HomeAppItem;
import com.diting.pingxingren.entity.IndustryEntity;
import com.diting.pingxingren.entity.IndustryItemEntity;
import com.diting.pingxingren.entity.InvalidQuestion;
import com.diting.pingxingren.entity.Knowledge;
import com.diting.pingxingren.entity.Mail;
import com.diting.pingxingren.entity.Msg;
import com.diting.pingxingren.entity.RobotConcern;
import com.diting.pingxingren.model.ChildRobotModel;
import com.diting.pingxingren.model.CommonLanguageListModel;
import com.diting.voice.data.body.Contact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by asus on 2016/11/28.
 */
public class Utils {
    public static List<MultiItemEntity> sIndustryEntityList;
    public static List<HomeAppItem> sHomeAppItems;
    public static List<ChildRobotModel> sChildRobotModels;
    public static List<String> sCreateRobotNotes;

    static {
        if (sIndustryEntityList == null) {
            sIndustryEntityList = new ArrayList<>();

            // IT/互联网.
            IndustryEntity itIndustryEntity = new IndustryEntity("IT/互联网");
            IndustryItemEntity itItemEntity1 = new IndustryItemEntity("互联网");
            itIndustryEntity.addSubItem(itItemEntity1);
            IndustryItemEntity itItemEntity2 = new IndustryItemEntity("电子商务");
            itIndustryEntity.addSubItem(itItemEntity2);
            IndustryItemEntity itItemEntity3 = new IndustryItemEntity("计算机软件");
            itIndustryEntity.addSubItem(itItemEntity3);
            IndustryItemEntity itItemEntity4 = new IndustryItemEntity("计算机硬件");
            itIndustryEntity.addSubItem(itItemEntity4);
            IndustryItemEntity itItemEntity5 = new IndustryItemEntity("系统/数据/维护");
            itIndustryEntity.addSubItem(itItemEntity5);
            IndustryItemEntity itItemEntity6 = new IndustryItemEntity("网络游戏");
            itIndustryEntity.addSubItem(itItemEntity6);
            sIndustryEntityList.add(itIndustryEntity);

            // 通信/电子.
            IndustryEntity ecIndustryEntity = new IndustryEntity("通信/电子");
            IndustryItemEntity ecItemEntity1 = new IndustryItemEntity("通信");
            ecIndustryEntity.addSubItem(ecItemEntity1);
            IndustryItemEntity ecItemEntity2 = new IndustryItemEntity("电信运营/增值业务");
            ecIndustryEntity.addSubItem(ecItemEntity2);
            IndustryItemEntity ecItemEntity3 = new IndustryItemEntity("电子技术");
            ecIndustryEntity.addSubItem(ecItemEntity3);
            IndustryItemEntity ecItemEntity4 = new IndustryItemEntity("半导体/集成电路");
            ecIndustryEntity.addSubItem(ecItemEntity4);
            IndustryItemEntity ecItemEntity5 = new IndustryItemEntity("网络设备");
            ecIndustryEntity.addSubItem(ecItemEntity5);
            IndustryItemEntity ecItemEntity6 = new IndustryItemEntity("仪器仪表");
            ecIndustryEntity.addSubItem(ecItemEntity6);
            IndustryItemEntity ecItemEntity7 = new IndustryItemEntity("工业自动化");
            ecIndustryEntity.addSubItem(ecItemEntity7);
            sIndustryEntityList.add(ecIndustryEntity);

            // 会计.
            IndustryEntity atIndustryEntity = new IndustryEntity("会计");
            IndustryItemEntity atItemEntity1 = new IndustryItemEntity("会计");
            atIndustryEntity.addSubItem(atItemEntity1);
            IndustryItemEntity atItemEntity2 = new IndustryItemEntity("审计");
            atIndustryEntity.addSubItem(atItemEntity2);
            sIndustryEntityList.add(atIndustryEntity);

            // 金融.
            IndustryEntity fcIndustryEntity = new IndustryEntity("金融");
            IndustryItemEntity fcItemEntity1 = new IndustryItemEntity("基金/证券");
            fcIndustryEntity.addSubItem(fcItemEntity1);
            IndustryItemEntity fcItemEntity2 = new IndustryItemEntity("期货/投资");
            fcIndustryEntity.addSubItem(fcItemEntity2);
            IndustryItemEntity fcItemEntity3 = new IndustryItemEntity("保险/理财");
            fcIndustryEntity.addSubItem(fcItemEntity3);
            IndustryItemEntity fcItemEntity4 = new IndustryItemEntity("银行业务");
            fcIndustryEntity.addSubItem(fcItemEntity4);
            IndustryItemEntity fcItemEntity5 = new IndustryItemEntity("信托/担保");
            fcIndustryEntity.addSubItem(fcItemEntity5);
            IndustryItemEntity fcItemEntity6 = new IndustryItemEntity("拍卖/典当");
            fcIndustryEntity.addSubItem(fcItemEntity6);
            sIndustryEntityList.add(fcIndustryEntity);

            // 建筑.
            IndustryEntity bdIndustryEntity = new IndustryEntity("建筑");
            IndustryItemEntity bdItemEntity1 = new IndustryItemEntity("建筑/建材/工程");
            bdIndustryEntity.addSubItem(bdItemEntity1);
            IndustryItemEntity bdItemEntity2 = new IndustryItemEntity("家居/室内设计/装饰装潢");
            bdIndustryEntity.addSubItem(bdItemEntity2);
            IndustryItemEntity bdItemEntity3 = new IndustryItemEntity("物业管理");
            bdIndustryEntity.addSubItem(bdItemEntity3);
            IndustryItemEntity bdItemEntity4 = new IndustryItemEntity("商业中心");
            bdIndustryEntity.addSubItem(bdItemEntity4);
            sIndustryEntityList.add(bdIndustryEntity);

            // 商业服务.
            IndustryEntity bsIndustryEntity = new IndustryEntity("商业服务");
            IndustryItemEntity bsItemEntity1 = new IndustryItemEntity("专业服务/咨询");
            bsIndustryEntity.addSubItem(bsItemEntity1);
            IndustryItemEntity bsItemEntity2 = new IndustryItemEntity("广告/会展/公关");
            bsIndustryEntity.addSubItem(bsItemEntity2);
            IndustryItemEntity bsItemEntity3 = new IndustryItemEntity("中介服务");
            bsIndustryEntity.addSubItem(bsItemEntity3);
            IndustryItemEntity bsItemEntity4 = new IndustryItemEntity("检验/检测/认证");
            bsIndustryEntity.addSubItem(bsItemEntity4);
            IndustryItemEntity bsItemEntity5 = new IndustryItemEntity("外包服务");
            bsIndustryEntity.addSubItem(bsItemEntity5);
            sIndustryEntityList.add(bsIndustryEntity);

            // 贸易/批发/零售.
            IndustryEntity twrIndustryEntity = new IndustryEntity("贸易/批发/零售");
            IndustryItemEntity twrItemEntity1 = new IndustryItemEntity("快速消费品");
            twrIndustryEntity.addSubItem(twrItemEntity1);
            IndustryItemEntity twrItemEntity2 = new IndustryItemEntity("耐用消费品");
            twrIndustryEntity.addSubItem(twrItemEntity2);
            IndustryItemEntity twrItemEntity3 = new IndustryItemEntity("进出口贸易");
            twrIndustryEntity.addSubItem(twrItemEntity3);
            IndustryItemEntity twrItemEntity4 = new IndustryItemEntity("零售/批发");
            twrIndustryEntity.addSubItem(twrItemEntity4);
            sIndustryEntityList.add(twrIndustryEntity);

            // 租赁服务.
            IndustryEntity rsIndustryEntity = new IndustryEntity("租赁服务");
            IndustryItemEntity rsItemEntity1 = new IndustryItemEntity("房屋租赁");
            rsIndustryEntity.addSubItem(rsItemEntity1);
            IndustryItemEntity rsItemEntity2 = new IndustryItemEntity("写字楼租赁");
            rsIndustryEntity.addSubItem(rsItemEntity2);
            IndustryItemEntity rsItemEntity3 = new IndustryItemEntity("厂房租赁");
            rsIndustryEntity.addSubItem(rsItemEntity3);
            IndustryItemEntity rsItemEntity4 = new IndustryItemEntity("场地租赁");
            rsIndustryEntity.addSubItem(rsItemEntity4);
            sIndustryEntityList.add(rsIndustryEntity);

            // 文体教育/工艺美术.
            IndustryEntity saIndustryEntity = new IndustryEntity("文体教育/工艺美术");
            IndustryItemEntity saItemEntity1 = new IndustryItemEntity("教育");
            saIndustryEntity.addSubItem(saItemEntity1);
            IndustryItemEntity saItemEntity2 = new IndustryItemEntity("培训");
            saIndustryEntity.addSubItem(saItemEntity2);
            IndustryItemEntity saItemEntity3 = new IndustryItemEntity("院校");
            saIndustryEntity.addSubItem(saItemEntity3);
            IndustryItemEntity saItemEntity4 = new IndustryItemEntity("礼品");
            saIndustryEntity.addSubItem(saItemEntity4);
            IndustryItemEntity saItemEntity5 = new IndustryItemEntity("玩具");
            saIndustryEntity.addSubItem(saItemEntity5);
            IndustryItemEntity saItemEntity6 = new IndustryItemEntity("工艺美术");
            saIndustryEntity.addSubItem(saItemEntity6);
            IndustryItemEntity saItemEntity7 = new IndustryItemEntity("收藏品");
            saIndustryEntity.addSubItem(saItemEntity7);
            IndustryItemEntity saItemEntity8 = new IndustryItemEntity("奢侈品");
            saIndustryEntity.addSubItem(saItemEntity8);
            sIndustryEntityList.add(saIndustryEntity);

            // 生产/加工/制造.
            IndustryEntity ppwIndustryEntity = new IndustryEntity("生产/加工/制造");
            IndustryItemEntity ppwItemEntity1 = new IndustryItemEntity("汽车");
            ppwIndustryEntity.addSubItem(ppwItemEntity1);
            IndustryItemEntity ppwItemEntity2 = new IndustryItemEntity("摩托车");
            ppwIndustryEntity.addSubItem(ppwItemEntity2);
            IndustryItemEntity ppwItemEntity3 = new IndustryItemEntity("大型设备/重工业");
            ppwIndustryEntity.addSubItem(ppwItemEntity3);
            IndustryItemEntity ppwItemEntity4 = new IndustryItemEntity("机电设备");
            ppwIndustryEntity.addSubItem(ppwItemEntity4);
            IndustryItemEntity ppwItemEntity5 = new IndustryItemEntity("加工制造");
            ppwIndustryEntity.addSubItem(ppwItemEntity5);
            IndustryItemEntity ppwItemEntity6 = new IndustryItemEntity("仪器仪表及工业自动化");
            ppwIndustryEntity.addSubItem(ppwItemEntity6);
            IndustryItemEntity ppwItemEntity7 = new IndustryItemEntity("印刷/包装");
            ppwIndustryEntity.addSubItem(ppwItemEntity7);
            IndustryItemEntity ppwItemEntity8 = new IndustryItemEntity("办公用品");
            ppwIndustryEntity.addSubItem(ppwItemEntity8);
            IndustryItemEntity ppwItemEntity9 = new IndustryItemEntity("生物工程");
            ppwIndustryEntity.addSubItem(ppwItemEntity9);
            IndustryItemEntity ppwItemEntity10 = new IndustryItemEntity("医疗设备");
            ppwIndustryEntity.addSubItem(ppwItemEntity10);
            IndustryItemEntity ppwItemEntity11 = new IndustryItemEntity("航空航天");
            ppwIndustryEntity.addSubItem(ppwItemEntity11);
            IndustryItemEntity ppwItemEntity12 = new IndustryItemEntity("材料加工");
            ppwIndustryEntity.addSubItem(ppwItemEntity12);
            sIndustryEntityList.add(ppwIndustryEntity);

            // 交通/运输.
            IndustryEntity tpIndustryEntity = new IndustryEntity("交通/运输");
            IndustryItemEntity tpItemEntity1 = new IndustryItemEntity("快递");
            tpIndustryEntity.addSubItem(tpItemEntity1);
            IndustryItemEntity tpItemEntity2 = new IndustryItemEntity("货运/包车");
            tpIndustryEntity.addSubItem(tpItemEntity2);
            IndustryItemEntity tpItemEntity3 = new IndustryItemEntity("商务接送");
            tpIndustryEntity.addSubItem(tpItemEntity3);
            sIndustryEntityList.add(tpIndustryEntity);

            // 物流/仓储.
            IndustryEntity lwIndustryEntity = new IndustryEntity("物流/仓储");
            IndustryItemEntity lwItemEntity1 = new IndustryItemEntity("物流/仓储");
            lwIndustryEntity.addSubItem(lwItemEntity1);
            sIndustryEntityList.add(lwIndustryEntity);

            // 服务业.
            IndustryEntity siIndustryEntity = new IndustryEntity("服务业");
            IndustryItemEntity siItemEntity1 = new IndustryItemEntity("医疗/护理/卫生服务");
            siIndustryEntity.addSubItem(siItemEntity1);
            IndustryItemEntity siItemEntity2 = new IndustryItemEntity("美容/保健");
            siIndustryEntity.addSubItem(siItemEntity2);
            IndustryItemEntity siItemEntity3 = new IndustryItemEntity("生活服务");
            siIndustryEntity.addSubItem(siItemEntity3);
            IndustryItemEntity siItemEntity4 = new IndustryItemEntity("餐饮");
            siIndustryEntity.addSubItem(siItemEntity4);
            IndustryItemEntity siItemEntity5 = new IndustryItemEntity("酒店");
            siIndustryEntity.addSubItem(siItemEntity5);
            IndustryItemEntity siItemEntity6 = new IndustryItemEntity("旅游");
            siIndustryEntity.addSubItem(siItemEntity6);
            sIndustryEntityList.add(siIndustryEntity);

            // 文化/传媒.
            IndustryEntity cmIndustryEntity = new IndustryEntity("文化/传媒");
            IndustryItemEntity cmItemEntity1 = new IndustryItemEntity("媒体/影视");
            cmIndustryEntity.addSubItem(cmItemEntity1);
            IndustryItemEntity cmItemEntity2 = new IndustryItemEntity("出版/文化传播");
            cmIndustryEntity.addSubItem(cmItemEntity2);
            sIndustryEntityList.add(cmIndustryEntity);

            // 娱乐/体育.
            IndustryEntity esIndustryEntity = new IndustryEntity("娱乐/体育");
            IndustryItemEntity esItemEntity1 = new IndustryItemEntity("休闲娱乐");
            esIndustryEntity.addSubItem(esItemEntity1);
            IndustryItemEntity esItemEntity2 = new IndustryItemEntity("体育");
            esIndustryEntity.addSubItem(esItemEntity2);
            sIndustryEntityList.add(esIndustryEntity);

            // 能源/矿产.
            IndustryEntity emIndustryEntity = new IndustryEntity("能源/矿产");
            IndustryItemEntity emItemEntity1 = new IndustryItemEntity("能源");
            emIndustryEntity.addSubItem(emItemEntity1);
            IndustryItemEntity emItemEntity2 = new IndustryItemEntity("新能源");
            emIndustryEntity.addSubItem(emItemEntity2);
            IndustryItemEntity emItemEntity3 = new IndustryItemEntity("矿产/冶炼");
            emIndustryEntity.addSubItem(emItemEntity3);
            IndustryItemEntity emItemEntity4 = new IndustryItemEntity("石油化工");
            emIndustryEntity.addSubItem(emItemEntity4);
            IndustryItemEntity emItemEntity5 = new IndustryItemEntity("电气/电力/水利");
            emIndustryEntity.addSubItem(emItemEntity5);
            sIndustryEntityList.add(emIndustryEntity);

            // 原材料.
            IndustryEntity rmIndustryEntity = new IndustryEntity("原材料");
            IndustryItemEntity rmItemEntity1 = new IndustryItemEntity("原材料");
            rmIndustryEntity.addSubItem(rmItemEntity1);
            sIndustryEntityList.add(rmIndustryEntity);

            // 环保.
            IndustryEntity epIndustryEntity = new IndustryEntity("环保");
            IndustryItemEntity epItemEntity1 = new IndustryItemEntity("环保");
            epIndustryEntity.addSubItem(epItemEntity1);
            sIndustryEntityList.add(epIndustryEntity);

            // 政府/非盈利机构.
            IndustryEntity gnIndustryEntity = new IndustryEntity("政府/非盈利机构");
            IndustryItemEntity gnItemEntity1 = new IndustryItemEntity("政府/政务");
            gnIndustryEntity.addSubItem(gnItemEntity1);
            IndustryItemEntity gnItemEntity2 = new IndustryItemEntity("公共事业/非盈利机构");
            gnIndustryEntity.addSubItem(gnItemEntity2);
            IndustryItemEntity gnItemEntity3 = new IndustryItemEntity("学术/科研");
            gnIndustryEntity.addSubItem(gnItemEntity3);
            IndustryItemEntity gnItemEntity4 = new IndustryItemEntity("多元化业务");
            gnIndustryEntity.addSubItem(gnItemEntity4);
            sIndustryEntityList.add(gnIndustryEntity);

            // 农林/渔牧.
            IndustryEntity afIndustryEntity = new IndustryEntity("农林/渔牧");
            IndustryItemEntity afItemEntity1 = new IndustryItemEntity("农业");
            afIndustryEntity.addSubItem(afItemEntity1);
            IndustryItemEntity afItemEntity2 = new IndustryItemEntity("林业");
            afIndustryEntity.addSubItem(afItemEntity2);
            IndustryItemEntity afItemEntity3 = new IndustryItemEntity("渔牧业");
            afIndustryEntity.addSubItem(afItemEntity3);
            sIndustryEntityList.add(afIndustryEntity);

            // 其他.
            IndustryEntity otIndustryEntity = new IndustryEntity("其他");
            IndustryItemEntity otItemEntity1 = new IndustryItemEntity("其他");
            otIndustryEntity.addSubItem(otItemEntity1);
            sIndustryEntityList.add(otIndustryEntity);
        }

        if (sHomeAppItems == null) {
            sHomeAppItems = new ArrayList<>();

            HomeAppItem homeAppItem1 = new HomeAppItem();
            homeAppItem1.setTitle("访客管理");
            homeAppItem1.setSubTitle("");
            homeAppItem1.setImageRes(R.drawable.ic_usermanage);
            sHomeAppItems.add(homeAppItem1);
            HomeAppItem homeAppItem2 = new HomeAppItem();
            homeAppItem2.setTitle("未知问题");
            homeAppItem2.setSubTitle("");
            homeAppItem2.setImageRes(R.drawable.ic_unknow);
            sHomeAppItems.add(homeAppItem2);
            HomeAppItem homeAppItem3 = new HomeAppItem();
            homeAppItem3.setTitle("抽奖有礼");
            homeAppItem3.setSubTitle("看新闻");
            homeAppItem3.setImageRes(R.drawable.ic_lucky);
            sHomeAppItems.add(homeAppItem3);
            HomeAppItem homeAppItem4 = new HomeAppItem();
            homeAppItem4.setTitle("语音记事");
            homeAppItem4.setSubTitle("我想翻译");
            homeAppItem4.setImageRes(R.drawable.ic_smarteditor);
            sHomeAppItems.add(homeAppItem4);
            HomeAppItem homeAppItem5 = new HomeAppItem();
            homeAppItem5.setTitle("智能小编");
            homeAppItem5.setSubTitle("附近有什么好吃");
            homeAppItem5.setImageRes(R.drawable.ic_xiaobian);
            sHomeAppItems.add(homeAppItem5);
            HomeAppItem homeAppItem6 = new HomeAppItem();
            homeAppItem6.setTitle("每日新闻");
            homeAppItem6.setSubTitle("我想学做菜");
            homeAppItem6.setImageRes(R.drawable.ic_cookbook);
            sHomeAppItems.add(homeAppItem6);
//            HomeAppItem homeAppItem7 = new HomeAppItem();
//            homeAppItem7.setTitle("生活助理");
//            homeAppItem7.setSubTitle("我想查天气");
//            homeAppItem7.setImageRes(R.drawable.ic_life_assistant);
//            sHomeAppItems.add(homeAppItem7);
//            HomeAppItem homeAppItem8 = new HomeAppItem();
//            homeAppItem8.setTitle("搞笑段子");
//            homeAppItem8.setSubTitle("讲个笑话");
//            homeAppItem8.setImageRes(R.drawable.ic_funny_piece);
//            sHomeAppItems.add(homeAppItem8);
//            HomeAppItem homeAppItem9 = new HomeAppItem();
//            homeAppItem9.setTitle("星座运势");
//            homeAppItem9.setSubTitle("我想知道我今天的星座运势");
//            homeAppItem9.setImageRes(R.drawable.ic_oroscope);
//            sHomeAppItems.add(homeAppItem9);
        }

        if (sCreateRobotNotes == null) {
            sCreateRobotNotes = new ArrayList<>();

            sCreateRobotNotes.add("第1个300元");
            sCreateRobotNotes.add("第2个280元");
            sCreateRobotNotes.add("第3个240元");
            sCreateRobotNotes.add("第4个180元");
            sCreateRobotNotes.add("第5个100元");
            sCreateRobotNotes.add("第6个60元");
            sCreateRobotNotes.add("第7个6元");
            sCreateRobotNotes.add("第8个1元");
        }
    }

    public static void getChildRobots(List<ChildRobotModel> childRobotModels, String robotName) {
        if (childRobotModels != null) childRobotModels.clear();
        for (ChildRobotModel childRobotModel : sChildRobotModels) {
            if (!childRobotModel.getName().equals(robotName)) {
                childRobotModels.add(childRobotModel);
            }
        }
        int size = childRobotModels.size();
        if (size < 8) {
            childRobotModels.add(null);
        }
    }

    public static String getRobotUniqueIdByChildRobotList(String robotName) {
        for (ChildRobotModel childRobotModel : sChildRobotModels) {
            if (childRobotModel.getName().equals(robotName)) {
                return childRobotModel.getUniqueId();
            }
        }

        return null;
    }

    // 检查是否有相机权限.
    public static boolean hasCameraPermission(Context context) {
        return EasyPermissions.hasPermissions(context, Manifest.permission.CAMERA);
    }

    // 检查是否有录音权限.
    public static boolean hasRecordAudioPermission(Context context) {
        return EasyPermissions.hasPermissions(context, Manifest.permission.RECORD_AUDIO);
    }

    // 检查是否有录音和相机权限.
    public static boolean hasRecordAudioAndCameraPermission(Context context) {
        return EasyPermissions.hasPermissions(context, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA);
    }

    // 检查是否有定位权限.
    public static boolean hasLocationPermission(Context context) {
        return EasyPermissions.hasPermissions(context, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    // 检查是否有存储权限.
    public static boolean hasStoragePermission(Context context) {
        return EasyPermissions.hasPermissions(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    // 检查是否有日历权限.
    public static boolean hasCalendarPermission(Context context) {
        return EasyPermissions.hasPermissions(context, Manifest.permission.WRITE_CALENDAR);
    }

    // 检查是否有相机和存储权限.
    public static boolean hasCameraAndStoragePermission(Context context) {
        return EasyPermissions.hasPermissions(context, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    // 获取手机型号.
    public static String getPhoneBrand() {
        return android.os.Build.MANUFACTURER;
    }

    public static String getUuid(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String macAddress = wm.getConnectionInfo().getMacAddress();
        return hashKeyForDisk(macAddress);
    }

    public static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 判断对象是否为空.
     *
     * @param o 需要判空的对象.
     * @return True: 非空. False: 空.
     */
    public static boolean isNotEmpty(Object o) {
        if (o instanceof String) {
            return !isEmpty((String) o);
        } else {
            return o != null;
        }
    }

    /**
     * 判断集合是否为空.
     *
     * @param objects 需要判空的集合.
     * @return True: 非空. False: 空.
     */
    public static boolean isNotEmpty(List<?> objects) {
        return objects != null && objects.size() > 0;
    }

    public static boolean isEmpty(String s) {
        return TextUtils.isEmpty(s) || s.equals("null");
    }

    public static boolean isEmpty(String[] strings) {
        return (strings == null || strings.length == 0);
    }

    public static boolean isMobile(String mobile) {
        return RegexUtil.isMobileExact(mobile);
    }

    public static boolean isPassword(String password) {
        String regex = "^\\w{6,16}$";
        return password.matches(regex);
    }

    public static boolean isCompany(String company) {
        String regex = "^[\\u4E00-\\u9FA5\\(\\)]{2,16}$";
        return company.matches(regex);
    }

    public static boolean isRobot(String robot) {
        String regex1 = "^[\u4E00-\u9FA5]{1,5}$";
        String regex2 = "^[a-zA-Z]{4,12}$";
        String regex3 = "^[\u4E00-\u9FA5a-zA-Z]{2,5}$";
        return (robot.matches(regex1) || robot.matches(regex2) || robot.matches(regex3));
    }

    //系统消息解析
    public static List<Mail> parseMailSystem(JSONObject jsonObject, boolean isRead) {
        JSONArray jsonArray = null;
        try {
            jsonArray = jsonObject.getJSONArray("items");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        List<Mail> mailList = new ArrayList<Mail>();
        if (jsonArray == null || jsonArray.length() == 0) {
            return null;
        }
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject object = jsonArray.getJSONObject(i);

                Mail mail = new Mail();
                mail.setTitle(object.getString("biaoti"));
                mail.setContent(object.getString("zhengwen"));
                mail.setMailId(object.getInt("id"));
                mail.setTime(new Date(object.getLong("createdTime")));
                if (object.getJSONArray("mail_phone").length() == 0) {
                    mail.setRead(false);
                } else {
                    mail.setRead(object.getJSONArray("mail_phone").getJSONObject(0).getString("flg").equals("666"));
                }
                mailList.add(mail);
            } catch (JSONException e) {
                return null;
            }
        }
        return mailList;
    }

    //私信解析
    public static List<Mail> parseMailRobot(JSONObject jsonObject, boolean isUnRead) {
        JSONArray jsonArray = null;
        try {
            jsonArray = jsonObject.getJSONArray("items");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        List<Mail> mailList = new ArrayList<Mail>();
        if (jsonArray == null || jsonArray.length() == 0) {
            return null;
        }
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject object = jsonArray.getJSONObject(i);

                Mail mail = new Mail();
                mail.setUniqueId(object.getString("forword_unique_id"));
                mail.setTitle(object.getString("forword_name"));
                mail.setContent(object.getString("message"));
                mail.setMailId(object.getInt("id"));
                mail.setTime(new Date(object.getLong("createdTime")));
                boolean isRead = object.getString("flag").equals("1");
                mail.setRead(isRead);
                if (!isUnRead || !isRead) {
                    mailList.add(mail);
                }
            } catch (JSONException e) {
                return null;
            }
        }
        return mailList;
    }

    //关注列表解析
    public static List<RobotConcern> parseConcern(JSONArray jsonArray) {
        List<RobotConcern> robotConcernList = new ArrayList<RobotConcern>();
        if (jsonArray == null || jsonArray.length() == 0) {
            return null;
        }
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject object = jsonArray.getJSONObject(i);
                RobotConcern robotConcern = new RobotConcern();
                robotConcern.setHeadPortrait(object.getString("headImgUrl"));
                robotConcern.setUsername(object.getString("userName"));
                robotConcern.setRobotName(object.getString("robot_name"));
                robotConcern.setCompanyName(object.getString("company_name"));
                robotConcern.setWelcome(object.getString("welcome"));
                robotConcern.setFansCount(object.getInt("ownerType"));
                robotConcern.setPhone(object.getString("own_phone"));
                robotConcern.setUniqueId(object.getString("uniqueId"));
                robotConcern.setRemark(object.getString("remarks"));

                if (object.has("robotValue")) {
                    if (object.getString("robotValue").equals("null")) {
                        robotConcern.setRobotValue(50.0);
                    } else {
                        robotConcern.setRobotValue(object.getDouble("robotValue"));
                    }
                }

//                robotConcern.setRobotValue(50.0);
                robotConcern.setConcerned(true);
                robotConcernList.add(robotConcern);
            } catch (JSONException e) {
                return null;
            }
        }
        return robotConcernList;
    }

    //粉丝列表解析
    public static List<RobotConcern> parseFans(JSONObject jsonObject) {
        List<RobotConcern> robotConcernList = new ArrayList<RobotConcern>();
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            if (jsonArray.length() == 0) {
                return null;
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                RobotConcern robotConcern = new RobotConcern();
                robotConcern.setWelcome(json.getString("welcome"));
                robotConcern.setUsername(json.getString("userName"));
                robotConcern.setRobotName(json.getString("robot_name"));
                robotConcern.setCompanyName(json.getString("company_name"));
                if (json.getString("robotValue").equals("null")) {
                    robotConcern.setRobotValue(50.0);
                } else {
                    robotConcern.setRobotValue(json.getDouble("robotValue"));
                }
                robotConcern.setFansCount(json.getInt("fansNum"));
                robotConcern.setHeadPortrait(json.getString("headImgUrl"));
                robotConcern.setPhone(json.getString("oth_phone"));
                robotConcern.setConcerned(json.getString("attentionState").equals("true"));
                robotConcern.setUniqueId(json.getString("uniqueId"));   //目前写死 部署后改为object.getString("uniqueId")
                robotConcernList.add(robotConcern);
            }
            return robotConcernList;
        } catch (JSONException e) {
            return null;
        }
    }

    //排行列表解析
    public static List<RobotConcern> parseRank(JSONArray jsonArray) {
        List<RobotConcern> robotConcernList = new ArrayList<>();
        if (jsonArray == null || jsonArray.length() == 0) {
            return null;
        }
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject object = jsonArray.getJSONObject(i);
                RobotConcern robotConcern = new RobotConcern();
                if (object.has("welcomes")) {
                    robotConcern.setWelcome(object.getString("welcomes"));
                } else if (object.has("welcome")) {
                    robotConcern.setWelcome(object.getString("welcome"));
                }

                robotConcern.setUsername(object.getString("username"));
                if (object.has("robotName")) {
                    robotConcern.setRobotName(object.getString("robotName"));
                } else if (object.has("name")) {
                    robotConcern.setRobotName(object.getString("name"));
                }

                robotConcern.setCompanyName(object.getString("companyName"));

                if (object.has("robotValue")) {
                    if (object.getString("robotValue").equals("null")) {
                        robotConcern.setRobotValue(50.0);
                    } else {
                        robotConcern.setRobotValue(object.getDouble("robotValue"));
                    }
                } else if (object.has("robotVal")) {
                    if (object.getString("robotVal").equals("null")) {
                        robotConcern.setRobotValue(50.0);
                    } else {
                        robotConcern.setRobotValue(object.getDouble("robotVal"));
                    }
                }

                if (object.has("num")) {
                    robotConcern.setFansCount(object.getInt("num"));
                }

//                robotConcern.setFansCount(object.getInt("num"));
                if (object.has("headImgUrl")) {
//                    robotConcern.setFansCount(object.getInt("num"));
                    robotConcern.setHeadPortrait(object.getString("headImgUrl"));
                }
//                robotConcern.setHeadPortrait(object.getString("headImgUrl"));

                if (object.has("userId")) {
                    robotConcern.setPhone(object.getString("userId"));
                }

                if (object.has("attentionState")) {
                    if (Utils.isEmpty(object.getString("attentionState"))) {
                        robotConcern.setConcerned(false);
                    } else {
                        robotConcern.setConcerned(object.getString("attentionState").equals("true"));
                    }
                }

                if (object.has("app_key")) {
                    robotConcern.setUniqueId(object.getString("app_key"));
                } else {
                    robotConcern.setUniqueId(object.getString("uniqueId"));
                }
                robotConcernList.add(robotConcern);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return robotConcernList;
    }


    //知识库列表解析
    public static List<Knowledge> parseKnowledge(JSONObject jsonObject) {
        List<Knowledge> knowledgeList = new ArrayList<Knowledge>();
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            if (jsonArray.length() == 0) {
                return null;
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                Knowledge knowledge = new Knowledge();
                knowledge.setQuestion(Utils.fromHtml(json.getString("question")));
                knowledge.setAnswer(Utils.fromHtml(json.getString("answer")));
                knowledge.setKnowledgeId(json.getInt("id"));
                knowledge.setPicture(json.getString("img_url"));
                knowledge.setVideo(json.getString("video_url"));
                knowledge.setAudio(json.getString("audio_url"));
                knowledge.setFile(json.getString("file_url"));
                knowledge.setHyperlink(json.getString("hyperlink_url"));
                knowledge.setScene(Utils.fromHtml(json.getString("scene")));
                knowledgeList.add(knowledge);
            }
            return knowledgeList;
        } catch (JSONException e) {
            return null;
        }
    }

    //无效问题解析
    public static List<InvalidQuestion> parseInvalidQuestion(JSONObject jsonObject) {
        List<InvalidQuestion> invalidQuestionList = new ArrayList<InvalidQuestion>();
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            if (jsonArray.length() == 0) {
                return null;
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                InvalidQuestion invalidQuestion = new InvalidQuestion();
                invalidQuestion.setQuestion(json.getString("question"));
                invalidQuestion.setFrequency(json.getInt("num"));
                invalidQuestion.setId(json.getInt("id"));
                invalidQuestionList.add(invalidQuestion);
            }
            return invalidQuestionList;
        } catch (JSONException e) {
            return null;
        }
    }

    //聊天用户解析
    public static List<ChatUser> parseChatUser(JSONObject jsonObject) {
        List<ChatUser> chatUserList = new ArrayList<ChatUser>();
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            if (jsonArray.length() == 0) {
                return null;
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                ChatUser chatUser = new ChatUser();
                chatUser.setCount((int) json.getDouble("count"));
                chatUser.setCreatedTime(json.getLong("createdTime"));
                chatUser.setExtra(Integer.parseInt(json.getString("extra4")));
                chatUser.setHeadPortrait(json.getString("headImgUrl"));
                chatUser.setRobotName(json.getString("robotName"));
                chatUser.setWelcome(json.getString("welcome"));
                chatUser.setUuid(json.getString("uuid"));
                chatUser.setUniqueId(json.getString("app_key"));
                if (json.has("loginUsername")) {
                    chatUser.setUserName(json.getString("loginUsername"));
                }
                chatUserList.add(chatUser);
            }
            return chatUserList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    //聊天记录解析
    public static List<Msg> parseChatLog(JSONObject jsonObject) {
        List<Msg> msgList = new ArrayList<Msg>();
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            if (jsonArray.length() == 0) {
                return null;
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                Msg msgQuestion = new Msg();
                msgQuestion.setContent(json.getString("question"));
                msgQuestion.setType(Msg.TYPE_RECEIVED_TEXT);
                msgQuestion.setTime(json.getLong("createdTime"));
                msgList.add(msgQuestion);
                Msg msgAnswer = new Msg();
                msgAnswer.setContent(json.getString("answer"));
                msgAnswer.setTime(json.getLong("createdTime"));
                msgAnswer.setHeadPortrait(MySharedPreferences.getHeadPortrait());
                msgAnswer.setType(Msg.TYPE_SENT);
                msgList.add(msgAnswer);
            }
            return msgList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    //根据Uri获取真实路径
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    public static void hideSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showSoftInput(Context context, View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.findFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 获取当前版本号
     */
    public static String getCurrentVersion(Context context) {
        try {

            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getRandomString(String[] strings) {
        Random random = new Random();
        return strings[random.nextInt(strings.length)];
    }

    public static String getRandomQuestion() {
        return getRandomString(Const.questionList);
    }

    public static String getRandomWelcome() {
        return getRandomString(Const.welcomeLists);
    }

    public static List<Contact> readContacts(Context context) {
        List<Contact> list = new ArrayList<Contact>();
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                Contact contact = new Contact(displayName, number, 0);
                list.add(contact);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return list;
    }

    // 获得所有启动Activity的信息，类似于Launch界面
    public static List<AppInfo> queryAppInfo(Context context) {
        List<AppInfo> mListAppInfo = new ArrayList<AppInfo>();
        PackageManager pm = context.getPackageManager(); // 获得PackageManager对象
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        // 通过查询，获得所有ResolveInfo对象.
        List<ResolveInfo> resolveInfos = pm
                .queryIntentActivities(mainIntent, PackageManager.MATCH_DEFAULT_ONLY);
        // 调用系统排序 ， 根据name排序
        // 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序
        Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(pm));
        for (ResolveInfo reInfo : resolveInfos) {
            String activityName = reInfo.activityInfo.name; // 获得该应用程序的启动Activity的name
            String pkgName = reInfo.activityInfo.packageName; // 获得应用程序的包名
            String appLabel = (String) reInfo.loadLabel(pm); // 获得应用程序的Label
            Drawable icon = reInfo.loadIcon(pm); // 获得应用程序图标
            // 为应用程序的启动Activity 准备Intent
            Intent launchIntent = new Intent();
            launchIntent.setComponent(new ComponentName(pkgName,
                    activityName));
            // 创建一个AppInfo对象，并赋值
            AppInfo appInfo = new AppInfo();
            appInfo.setAppLabel(appLabel);
            appInfo.setPkgName(pkgName);
            appInfo.setAppIcon(icon);
            appInfo.setIntent(launchIntent);
            mListAppInfo.add(appInfo); // 添加至列表中
        }
        return mListAppInfo;
    }

    public static String parseIatResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);

            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // 转写结果词，默认使用第一个结果
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
//				如果需要多候选结果，解析数组其他字段
//				for(int j = 0; j < items.length(); j++)
//				{
//					JSONObject obj = items.getJSONObject(j);
//					ret.append(obj.getString("w"));
//				}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }

    /**
     * 提示用户的 dialog
     */
    public static void showMissingPermissionDialog(final Activity activity, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("提示");
        builder.setMessage("当前应用缺少" + message + "权限。\n\n请点击\"设置\"-\"权限\"-打开所需权限。");
        // 拒绝, 退出应用
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Log.i("info", "8--权限被拒绝,此时不会再回调onRequestPermissionsResult方法");
                    }
                });
        builder.setPositiveButton("设置",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("info", "4,需要用户手动设置，开启当前app设置界面");
                        startAppSettings(activity);
                    }
                });
        builder.setCancelable(false);
        builder.show();
    }

    /**
     * 打开     App设置界面
     */
    public static void startAppSettings(Activity activity) {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        activity.startActivity(intent);
    }

    /**
     * 将paramValue中的汉字提取出来
     *
     * @param paramValue
     * @return
     */
    public static String getChinese(String paramValue) {
        String str = "";
        String regex = "([\u4e00-\u9fa5]+)";
        Matcher matcher = Pattern.compile(regex).matcher(paramValue);
        while (matcher.find()) {
            str += matcher.group(0);
        }
        return str;
    }

    /**
     * 将url中的汉字编码
     *
     * @param
     * @return
     */
    public static String decodeUrl(String url) {
        String gradeChineseStr = getChinese(url);
        String[] arr = new String[gradeChineseStr.length()];
        String decodeSrc = url;
        for (int i = 0; i < gradeChineseStr.length(); i++) {
            arr[i] = gradeChineseStr.substring(i, i + 1);
            String gradeStr = URLEncoder.encode(arr[i]);
            decodeSrc = decodeSrc.replace(arr[i], gradeStr);
        }
        return decodeSrc;
    }

    public static void addUrl(Context context, TextView textView) {
        CharSequence text = textView.getText();
        if (text instanceof Spannable) {
            int end = text.length();
            Spannable sp = (Spannable) text;
            URLSpan urls[] = sp.getSpans(0, end, URLSpan.class);
            SpannableStringBuilder style = new SpannableStringBuilder(text);
            style.clearSpans();
            for (URLSpan urlSpan : urls) {
                MyURLSpan myURLSpan = new MyURLSpan(urlSpan.getURL(), context);
                style.setSpan(myURLSpan, sp.getSpanStart(urlSpan),
                        sp.getSpanEnd(urlSpan),
                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

            }
            textView.setText(style);
        }
    }

    public static String fromHtml(String text) {
        return Html.fromHtml(text).toString();
    }

    /**
     * 检查手机是否是miui
     *
     * @return
     * @ref http://dev.xiaomi.com/doc/p=254/index.html
     */
    public static boolean isMIUI() {
        String device = Build.MANUFACTURER;
        System.out.println("Build.MANUFACTURER = " + device);
        if (device.equals("Xiaomi")) {
            return true;
        } else {
            return false;
        }
    }

    //判断是否有未读的消息
    public static boolean hasUnreadMail() {
        return MyApplication.unreadLetterNum + MyApplication.unreadMessageNum > 0;
    }

    //判断用户分享的次数
    public static boolean hasLuckyCount() {
        return MyApplication.shareTimes - MyApplication.sharerStatusNum >= 0;
    }

    public static boolean canCall(String phoneNumber) {
        String regex = "^\\d+$";
        return phoneNumber.matches(regex);
    }

    /**
     * 对数据进行排序
     *
     * @param list 要进行排序的数据源
     */
    public static void sortData(List<Contact> list) {
        if (list == null || list.size() == 0) return;
        for (int i = 0; i < list.size(); i++) {
            Contact bean = list.get(i);
            String tag = Pinyin4jUtil.converterToSpell(bean.getName().substring(0, 1)).substring(0, 1).toUpperCase();
            if (tag.matches("[A-Z]")) {
                bean.setIndexTag(tag);
            } else {
                bean.setIndexTag("#");
            }
        }
        Collections.sort(list, new Comparator<Contact>() {
            @Override
            public int compare(Contact o1, Contact o2) {
                if ("#".equals(o1.getIndexTag())) {
                    return 1;
                } else if ("#".equals(o2.getIndexTag())) {
                    return -1;
                } else {
                    return o1.getIndexTag().compareTo(o2.getIndexTag());
                }
            }
        });
    }

    /**
     * @param beans 数据源
     * @return tags 返回一个包含所有Tag字母在内的字符串
     */
    public static String getTags(List<Contact> beans) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < beans.size(); i++) {
            if (!builder.toString().contains(beans.get(i).getIndexTag())) {
                builder.append(beans.get(i).getIndexTag());
            }
        }
        return builder.toString();
    }

    public static boolean hasKitkat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean hasMr1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean hasM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean hasN() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    public static void setBackGround(View view, Drawable background) {
        if (hasJellyBean()) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }

    public static void setImageView(ImageView view, Drawable background) {
        if (hasJellyBean()) {


            view.setImageDrawable(background);
        } else {
            view.setImageDrawable(background);

        }
    }

    public static String getFileExtensionFromUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            int fragment = url.lastIndexOf('#');
            if (fragment > 0) {
                url = url.substring(0, fragment);
            }

            int query = url.lastIndexOf('?');
            if (query > 0) {
                url = url.substring(0, query);
            }

            int filenamePos = url.lastIndexOf('/');
            String filename =
                    0 <= filenamePos ? url.substring(filenamePos + 1) : url;

            // if the filename contains special characters, we don't
            // consider it valid for our matching purposes:
            if (!filename.isEmpty() &&
                    Pattern.matches("[a-zA-Z_0-9\\.\\-\\(\\)\\%]+", filename)) {
                int dotPos = filename.lastIndexOf('.');
                if (0 <= dotPos) {
                    return filename.substring(dotPos + 1);
                }
            }
        }

        return "";
    }

    public static boolean isInTheLanguages(String question, int questionId) {
        int size = MyApplication.sCommonLanguages.size();
        if (size > 0) {
            for (CommonLanguageListModel commonLanguageModel : MyApplication.sCommonLanguages) {
                if (question.equals(commonLanguageModel.getQuestion())
                        && String.valueOf(questionId).equals(commonLanguageModel.getId())) {
                    return true;
                }
            }
        }

        return false;
    }

    public static double getCreateRobotPrice() {
        int size = sChildRobotModels.size();
        boolean isVip = MySharedPreferences.getVip();
        int price;
        switch (size - 1) {
            case 0:
                price = 300;
                break;
            case 1:
                price = 280;
                break;
            case 2:
                price = 240;
                break;
            case 3:
                price = 180;
                break;
            case 4:
                price = 100;
                break;
            case 5:
                price = 60;
                break;
            case 6:
                price = 6;
                break;
            case 7:
                price = 1;
                break;
            default:
                price = 0;
                break;
        }

        return isVip ? price * 0.88 : price;
    }

    /**
     * 获取随机字符串 a-z,A-Z,0-9
     *
     * @param length 字符串长度
     */
    public static String getRandomString(int length) {
        char[] chr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
                'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
                'W', 'X', 'Y', 'Z'};
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(chr[random.nextInt(62)]);
        }
        return builder.toString();
    }

    public static String generateToken(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}