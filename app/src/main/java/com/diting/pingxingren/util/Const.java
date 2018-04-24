package com.diting.pingxingren.util;

;import android.os.Environment;

/**
 * Created by asus on 2016/11/25.
 */
public class Const {
    //public static final String URL_BASE = "http://l153369y82.iask.in";  //本地测试路径
    //public static final String URL_BASE = "http://101.201.210.144:3100";  //测试服务器路径
    //public static final String URL_BASE = "http://haitao.picp.net:22460";
    //public static final String URL_BASE = "http://192.168.3.243:3100";  //飞哥
    // public static final String URL_BASE = "http://192.168.3.164:3100/";  //海涛
    public static final String URL_BASE = "http://101.201.62.75:3100";     //正式路径
    public static final String URL_ANSWER = URL_BASE + "/api/chat/info";         //问答
    public static final String URL_REMOTE_ANSWER = URL_BASE + "/remote/chat/info";         //远程问答
    public static final String URL_RANK_BY_QUESTION = URL_BASE + "/api/company/yesterday/questions_rank";  //昨日排行
    public static final String URL_INVALID = URL_BASE + "/api/invalid-question/search?uuid=";     //根据设备查询无效问答
    public static final String URL_SEARCH_ROBOT = URL_BASE + "/api/company/search-for-keyword?keyword=";   //关键字搜索机器人
    public static final String URL_LOGIN = URL_BASE + "/api/login";   //登录
    public static final String URL_SEND_MESSAGE = URL_BASE + "/api/register/captchas/mobile/";   //注册发送验证码
    public static final String URL_REGISTER = URL_BASE + "/api/accounts/register/mobile";   //注册
    public static final String URL_ROBOT_SETTING = URL_BASE + "/api/robot/save";      //机器人信息设置
    public static final String URL_COMPANY_SETTING = URL_BASE + "/api/company/save";     //公司信息设置
    public static final String URL_SEND_RESET_MESSAGE = URL_BASE + "/api/reset/captchas/mobile/";   //修改密码发送验证码
    public static final String URL_RESET_PASSWORD = URL_BASE + "/api/password/reset";   //找回密码
    public static final String URL_CHANGE_PASSWORD = URL_BASE + "/api/password/change";   //修改密码
    public static final String URL_ADD_QUESTION = URL_BASE + "/api/knowledge/save";      //添加知识
    public static final String URL_DELETE_KNOWLEDGE = URL_BASE + "/api/knowledge/delete/";      //单条删除知识
    public static final String URL_DELETE_KNOWLEDGE_LIST = URL_BASE + "/api/knowledge/batchdelete";      //批量删除知识
    public static final String URL_SEARCH_KNOWLEDGE = URL_BASE + "/api/company/knowledge/search-page";      //查询知识
    public static final String URL_SEARCH_MAIL_SYSTEM = URL_BASE + "/mails/search_phone";      //系统站内信查询
    public static final String URL_SEARCH_MAIL_ROBOT = URL_BASE + "/tele_other/search";      //私信查询
    public static final String URL_EDIT_MAIL_SYSTEM = URL_BASE + "/mails/save_phone";      //站内信状态修改
    public static final String URL_EDIT_MAIL_ROBOT = URL_BASE + "/tele_other/update";      //私信状态修改
    public static final String URL_SEARCH_MAIL_COUNT = URL_BASE + "/api/find/unread_letter_num";      //查看站内信条数
    public static final String URL_SEARCH_CONCERN = URL_BASE + "/fans/search_concern_list";      //关注列表
    public static final String URL_DELETE_CONCERN = URL_BASE + "/fans/del_fans";      //取消关注
    public static final String URL_ADD_CONCERN = URL_BASE + "/fans/save_fans";      //添加关注
    public static final String URL_SEARCH_EXTERNALOPTIONSLIST = URL_BASE + "/api/apis/search-validity";      //查询应用列表
    public static final String URL_SET_EXTERNALOPTIONSLIST = URL_BASE + "/api/ex-applications/setting";      //应用设置
    public static final String URL_SEARCH_ENABLE_EXTERNALOPTION = URL_BASE + "/api/ex-applications/search-checked";      //开启应用查询
    public static final String URL_RANK_BY_VALUE = URL_BASE + "/api/company/all/questions_rank";      //按价值排名
    public static final String URL_UPLOAD = URL_BASE + "/upload/image";      //上传图片
    public static final String URL_UPLOAD_ANNEX = URL_BASE + "/upload/inputImage";      //上传附件
    public static final String URL_WECHAT_BIND = URL_BASE + "/wx/api/wechat_bind";      //绑定微信
    public static final String URL_WECHAT_LOGIN = URL_BASE + "/wx/api/remote/wechat_login";      //微信登录
    public static final String URL_ROBOT_INFO = URL_BASE + "/api/get-robot-info";      //获取机器人信息
    public static final String URL_COMPANY_INFO = URL_BASE + "/api/get-company-info";      //获取所有者信息
    public static final String URL_GET_INFO = URL_BASE + "/api/app/info/search";      //获取个人信息
    public static final String URL_FEEDBACK = URL_BASE + "/api/message/save";      //意见反馈
    public static final String URL_SEARCH_BALANCE = URL_BASE + "/balance_search";      //余额查询
    public static final String URL_RANK_BY_FANS = URL_BASE + "/api/company/top/fifty/questions_rank";      //粉丝排行
    public static final String URL_FANS_LIST = URL_BASE + "/api/fans/search_fans_list";      //粉丝列表
    public static final String URL_EDIT_QUESTION = URL_BASE + "/api/knowledge/update";      //修改知识
    public static final String URL_CAICAI = URL_BASE + "/temp/knowledge/save";      //拆拆
    public static final String URL_INVALID_QUESTION = URL_BASE + "/api/company/invalidquestion/searchpage";      //无效问题列表
    public static final String URL_DELETE_INVALID_QUESTION = URL_BASE + "/api/company/invalidquestion/batchdelete";      //删除无效问题
    public static final String URL_EDIT_INVALID_QUESTION = URL_BASE + "/api/company/invalidquestion/update";      //编辑无效问题
    public static final String URL_GET_CHAT_USER = URL_BASE + "/api/company/chatlog/search-group";  //api/company/chatlog/search-group    //用户管理列表
    public static final String URL_GET_CHAT_LOG = URL_BASE + "/api/company/chatlog/searchpage";      //聊天记录
    public static final String URL_GET_ROBOT_INFO = URL_BASE + "/api/app/robot_info/search/";      //机器人信息
    public static final String URL_ROBOT = "http://www.ditingai.com/robot-company/";      //机器人长域名前缀
    public static final String URL_PHONE_PERMISSION_SETTING = URL_BASE + "/api/update/telephone_switch/";      //机器人手机号权限设置
    public static final String URL_UPLOAD_CRASH_LOG = URL_BASE + "/app/upload";      //上传文件
    public static final String URL_GET_ROBOT_INFO_BY_USERNAME = URL_BASE + "/api/robot_info/";
    public static final String URL_UPLOAD_CONTACT = URL_BASE + "/api/accounts/cloud/address/book";   //本地通讯录上传
    public static final String URL_GET_CALL_LOG = URL_BASE + "/call/search";       //通话记录查询
    public static final String URL_UPDATE_REMARK = URL_BASE + "/fans/update";       //通话记录查询
    public static final String URL_DELETE_MAIL = URL_BASE + "/tele_other/delete/";       //通话记录查询
    public static final String URL_SAVE_GAME_INFO = URL_BASE + "/gaming/save";   //小游戏上传接口
    public static final String URL_SEARCH_CONCERN_INDUSTRY = URL_BASE + "/api/robot/industry/search-page";
    public static final String URL_UPDATE_LOCATION = URL_BASE + "/accounts/update/lng/lat";
    public static final String URL_SEARCH_NEARBY = URL_BASE + "/api/search/nearby";
    public static final String URL_SEARCH_COMMON_LANGUAGE = URL_BASE + "/chang/search"; // 查找常用语
    public static final String URL_SAVE_COMMON_LANGUAGE = URL_BASE + "/chang/save"; // 保存常用语
    public static final String URL_DELETE_COMMON_LANGUAGE = URL_BASE + "/chang/delete"; // 删除常用语
    public static final String UNIQEID_XIAODI = "7818c7f73c31468ca03c63b7375994b9";
    public static final String URL_FINDGUANZHU_NEW = URL_BASE + "/fans/findguanzhu_new";

    public static final String URL_SEARCH_HANGYE = URL_BASE + "/robots/searchHangye";//速配  里面查找行业
    public static final String TABLE_MESSAGE = "message";
    public static final String TABLE_INVALID = "invalid";
    public static final String DB_NAME = "diting";
    public static final int DB_VERSION = 4;

    //微信信息

    //秒答
//    public static final String APP_ID = "wx470e2169df89cf80";
//    public static final String WXAppSecret = "7e1c69929b614a1e9633cc752c481e40";

    //平行人
    public static final String APP_ID = "wxc6fbeded8b899a3b";
    public static final String WXAppSecret = "091b357ab5db270805c71464ae552994";


    public static final String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();

//    public static final String VERSION_URL = "http://60.205.59.176:8080/ParallelPerson/updateinfo.html";
//
//    public static final String SDCARD_PATH = FILE_PATH + "/ParallelPerson.apk";

    // public static final String VERSION_URL = "http://60.205.59.176:8080/pingxingren/updateinfo.html";
    public static final String VERSION_URL = "http://gufanfan.cc/public/DITing/pxr/checkTestVersion.html";

    public static final String SDCARD_PATH = FILE_PATH + "/pingxingren.apk";

    public static final String IMAGE_PATH = FILE_PATH + "/DCIM/diting";

    public static final String APP_NAME = "pingxingren";

    public static final String[] questionList = {"今日新闻", "今天有什么新闻", "我要看新闻", "讲笑话", "我要听笑话", "来个笑话", "给我讲个段子", "现在几点了", "今天星期几", "今天几号", "后天几号", "昨天礼拜几", "现在是什么时间", "金牛座运势", "狮子座幸运色", "处女座健康", "白羊座工作", "双子座爱情", "巨蟹座财运", "天秤座幸运数字", "天蝎座速配星座", "射手座今天怎么样", "摩羯座今天要注意什么", "7和9谁大", "5乘以6得多少", "北京天气", "哈尔滨明天下雪吗", "你快乐吗", "今年你有对象了吗", "你刷牙了没有", "你愿不愿意嫁给我", "相过亲吗", "床前明月光", "天空为什么是蓝色的", "你是美女吗", "你饿不饿", "你是男生女生", "你会做什么", "彩虹是什么颜色的", "我要礼物", "怎么减肥", "今天过得开心吗", "谁发明了电话", "我国第一部字典", "我国第一部写在纸上的书是", "京杭大运河开凿于什么年代", "地球到月球的距离为", "哪根手指的指甲长得最快", "长颈鹿怎么打架", "女排的球网有多高", "千里送鹅毛", "红色食品是指什么", "哪个沙漠位于海边", "老鼠为什么要啃家具", "冰淇淋最早出现在哪个国家", "鳄鱼为什么会流泪", "颜筋柳骨", "怎么使鲜花插得久一些", "紫菜长在哪里", "第一位华人航天员是谁", "京剧中红色脸谱代表", "我困了", "你叫什么名字", "你爸爸是谁", "你奶奶的儿子是谁", "你快乐吗", "你会做什么", "出来聊天吧", "我心情特别好", "你是机器人吗", "你有朋友吗", "天王盖地虎", "逗逗你", "你口味重吗", "抢票神器", "好无聊啊", "我叫什么名字", "你在干嘛呢", "长得像孙红雷的狗", "比利牛斯山犬", "斑点狗", "拉布拉多寻回犬", "曾经沧海难为水", "白居易的暮江吟", "两个黄鹂鸣翠柳", "何当共剪西窗烛", "李白乘舟将欲行", "鹅鹅鹅", "结婚对象怎么选", "两只老虎两只老虎", "剪刀石头布", "狼和哈士奇有什么区别", "怎样才能脱单", "在爱情里什么最重要", "商鞅是谁", "单恋会成功吗", "你有休息时间吗", "你对相亲怎么看", "你最讨厌的人是谁呀", "怎样追到男神"};

    public static final String[] questions = {"我是岛主", "价格查询", "学狗叫", "你是狗吗", "退出"};

    public static final String VOICE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/diting/";//录音存放位

    public static final String[] phraseGroup = {
            "今天有什么新闻",
            "北京天气",
            "你会做什么",
            "来个笑话",
            "你叫什么名字",
            "变身唐僧",
            "告诉猪八戒我要听故事",
            "查找讲故事机器人",
            "打电话给小谛"
    };

    public static final String diting_profile = "我是谛听机器人科技有限公司开\n" +
            "发的移动端产品，是您的自动应\n" +
            "答机器人。任何人都可以建立属\n" +
            "于自己的机器人，让它用特定的\n" +
            "语言风格说话，回答问题。可以\n" +
            "是客服机器人，回答各类客服问\n" +
            "题；可以是专家机器人，提供各\n" +
            "类专业知识；可以是娱乐机器人\n" +
            "休闲玩耍；可以是聊天机器人，\n" +
            "与你轻松陪伴...…";

    public static final String[] welcomeLists = {
            "欢迎您使用谛听机器人，我们支持深度个性化机器人定制，零开发基础也能拥有属于自己的机器人。",
            "不知道做些什么？跟我说讲故事、讲笑话、放首歌试试看。",
            "试试跟我说，想查看的地区名字+天气预报。",
            "不知道做些什么？跟我说猜灯谜试试看。",
            "不知道做些什么？跟我说看新闻试试看。",
            "我能歌善舞能说会道，欢迎随时来测试！",
            "欢迎您使用谛听机器人，我可以查看时间、天气预报、新闻、火车票，还可以猜灯谜、讲笑话、讲故事、放音乐，计算多组数字也很拿手的！",
            "谛听机器人（www.ditingai.com），支持知识库个性化定制，可以自己上传知识库，可用于陪伴机器人、咨询机器人、营销机器人、实体机器人的大脑。可轻松绑定网页和微信公众账号，直接调用。",
            "嗨！我是北京谛听机器人科技有限公司陪伴机器人。我教您怎样建立像我这样的机器人。您可以在我们公司网站了解详情。www.ditingai.com。",
            "欢迎使用北京谛听机器人，登陆www.ditingai.com可以了解更多，可以下载平行人APP，或使用接入方式中的各种途径使用机器人。无门槛、零基础随心定制。"
    };

    public static String commonProfile = "迷之人物，什么线索都没留下";
    public static String commonWelcome = "这家伙很懒什么都没留下";
    public static String defaultFoods = "沙县小吃 兰州拉面 黄焖鸡米饭 成都小吃 烤鸭 烤鱼 烤肉 烤全羊 意大利面 牛排 西餐 煎饼 鸡排 外卖 小龙虾 皮皮虾 海鲜 烧烤 卤煮 香河肉饼 米线 麻辣烫 排骨 红烧肉 KFC 麦当劳 汉堡王 羊蝎子 快餐 自助餐";
}
