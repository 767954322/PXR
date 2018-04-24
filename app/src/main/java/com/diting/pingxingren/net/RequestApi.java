package com.diting.pingxingren.net;

/**
 * Created by Gu FanFan.
 * Date: 2017/4/5, 17:37.
 * 请求Api接口.
 */

public interface RequestApi {

    long TIME_OUT = 30; // 超时时间.

    String BASE_HOST = "http://www.ditingai.com/"; // 正式服务器.
    String BASE_HOST_1 = "http://47.95.172.222/"; // 正式服务器.
    String BASE_HOST_2 = "http://dtrobo.ditingai.com/"; // 正式服务器.
    String BASE_CHECK_VERSION_HOST = "http://gufanfan.cc/"; // 检查更新服务器.
    String BASE_WX_HOST = "https://api.mch.weixin.qq.com/"; // 微信统一下单.

    String BASE_HOST_TEST = "http://101.201.210.144:3100/"; // 测试服务器.
    String BASE_HOST_TEST_NIU = "http://192.168.1.237:3100/"; // 牛智鑫服务器
    String BASE_HOST_TEST_TAO = "http://192.168.3.166:3100/"; // 海涛服务器.
    String BASE_HOST_TEST_FEI = "http://liufei19881202.ticp.net/"; // 刘飞服务器.
    String BASE_HOST_TEST_SHENKUN = "http://192.168.3.113:3100/";//申昆服务器

    String HEADERS_JSON = "Content-Type: application/json"; // Json请求头.
    String HEADERS_XML = "Content-Type: application/xml"; // Xml请求头.

    String CHECK_VERSION = BASE_CHECK_VERSION_HOST + "public/DITing/pxr/newVersion.html"; // 检查更新.
    String CHECK_TEST_VERSION = BASE_CHECK_VERSION_HOST + "public/DITing/pxr/newTestVersion.html"; // 检查更新.
    String WX_UNIFIED_ORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder"; // 微信统一下单.
    String WX_RESULT_NOTIFY = ""; // 微信回调服务器接口地址.

    String LOGIN = "api/login"; // 登录.
    String REGISTER = "api/accounts/register/mobile"; // 注册.
    String REGISTER_CODE = "api/register/captchas/mobile/"; // 注册获取验证码.
    String RESET_PASSWORD = "api/password/reset"; // 重置密码.
    String CHANGE_PASSWORD = "api/password/change"; // 修改密码.
    String RESET_AND_CHANGE_PASSWORD_CODE = "api/reset/captchas/mobile/"; // 注册获取验证码.
    String CHILD_ROBOT_LIST = "api/account-robots"; // 子机器人列表.
    String CHOOSE_CHILD_ROBOT = "robot-setting/"; // 选择子机器人.
    String CREATE_ROBOT = "api/app/robot/create"; // 创建机器人.
    String ROBOT_NAME_IS_EXISTS = "robot/checkNameExists";//判断机器人名称是否存在

    String CHAT_INFO = "api/chat/info"; // 对话.
    String PUBLIC_CHAT_INFO = "remote/chat/info"; // 开放对话.
    String USER_INFO = "api/app/info/search"; // 获取用户信息.
    String ROBOT_INFO = "api/get-robot-info"; // 获取机器人信息.
    String ROBOT_INFO_BY_UNIQUE_ID = "api/app/robot_info/search/"; // 获取用户信息.
    String SAVE_KNOWLEDGE = "api/knowledge/save"; // 添加知识.
    String EDIT_KNOWLEDGE = "api/knowledge/update"; // 编辑知识.
    String DELETE_KNOWLEDGE = "api/knowledge/delete/"; // 删除知识.
    String USER_COMMON_LANGUAGE = "chang/search"; // 获取常用语.
    String SAVE_COMMON_LANGUAGE = "chang/save"; // 保存常用语.
    String DELETE_COMMON_LANGUAGE = "chang/delete"; // 删除常用语.
    String UPDATE_COMMON_LANGUAGE = "chang/update"; // 修改常用语.
    String SAVE_COMPANY_INFO = "api/company/save"; // 保存公司信息.
//    String SAVE_ROBOT_INFO = "api/robot/save"; // 保存机器人信息.
    String SAVE_ROBOT_INFO = "api/robot/update"; // 保存机器人信息.
    String SEARCH_KNOWLEDGE = "api/company/knowledge/search-page"; // 查询知识库.
    String SEARCH_COMMON_FEATURES = "api/apis/search-validity"; // 查询通用功能信息.
    String SEARCH_OPEN_COMMON_FEATURES = "api/ex-applications/search-checked"; // 查询开启通用功能信息.
    String SAVE_COMMON_FEATURES = "api/ex-applications/setting"; // 保存通用功能信息.
    String UNREAD_MAIL_COUNT = "api/find/unread_letter_num"; // 获取未读消息.
    String UPLOAD_LOCATION = "accounts/update/lng/lat"; // 上传位置.
    String UPLOAD_IMAGE = "upload/image"; // 上传图片.
    String UPLOAD_ANNEX = "upload/inputImage"; // 上传附件.
    String FOLLOW_LIST = "fans/findguanzhu_new"; // 关注列表.
    String FANS_LIST = "fans/findfans_new"; // 粉丝列表.
    String ADD_FOLLOW = "fans/save_fans_new"; // 添加关注.
    String CANCEL_FOLLOW = "fans/del_fans_new"; // 取消关注.
    String UPDATE_REMARK = "fans/updatenew"; // 修改备注.
    String RANK_LIST = "fans/search_jiazhinew"; // 价值排行列表.
    String CHAT_USER__MANAGE = "api/company/chatlog/search-group";  // 用户管理列表.
    String GET_ROBOT_INFO_BY_USERNAME = "api/robot_info/"; // 根据手机号获取机器人信息.
    String GET_CHAT_RECORD = "api/company/chatlog/searchpage"; // 聊天记录  访客管理点击进入.
    String SYSTEM_MESSAGE = "mails/search_phone"; // 系统站内信查询.
    String SYSTEM_MAIL_IS_READ = "mails/save_phone"; // 系统消息信状态修改.
    String PERSONAL_MAIL = "tele_other/search"; // 私信查询.
    String PERSONAL_MAIL_IS_READ = "tele_other/update"; // 私信状态修改.
    String PERSONAL_MAIL_DELETE = "tele_other/delete/"; // 私信消息删除.
    String CALL_RECORD_LIST = "call/search";       // 通话记录查询.
    String SUBMIT_FEEDBACK = "api/message/save"; // 意见反馈.
    String INVALID_QUESTION = "api/company/invalidquestion/searchpage"; // 无效问题列表.
    String INVALID_QUESTION_DELETE = "app/company/invalidquestion/batchdelete"; // 删除无效问题.
    String INVALID_QUESTION_EDIT = "api/company/invalidquestion/update"; // 编辑无效问题.
    String PHONE_PERMISSION_SETTING = "api/update/telephone_switch/"; // 机器人手机号权限设置.
    String ROBOT_SPEED_LIST = "robots/searchHangyenew"; // 速配列表  里面查找行业.
    String CHECK_IS_ADD_FOLLOW = "fans/find_fans_new";//查看是否添加了关注

    String GET_COMMON_LANGUAGE_LIST = "api/high-frequency/problem/";//获取常用语  与pc端  同步
    String ADD_OR_DELETE_COMMON_LANGUAGE = "api/knowledge/update/frequency";//添加常用语
    String GET_LUCKY_LIST = "api/activity";//我的活动列表
    String LUCKY_SHATE = "api/lottery/share";//我的活动分享成功后请求
    String ADD_TO_COLLECTION = "collection/save";//添加到收藏
    String DELETE_COLLECTION = "collection/del";//我的收藏的删除
    String GET_COLLECTION_LIST = "collection/search";//我的收藏列表
    String GET_ASK_LIST = "ask/search";//得到会问的列表
    String SAVE_CHANGE_ASK = "ask/saveaskrobot";//会问知识添加后
    String URL_NEWS_LIST = "news/get";//新闻列表
    String GET_ASK_LIST_GET = "ask/search-page";//会问列表  有分页
    String ASK_COOUNT = "ask/searchcount";//获取会问的数字
    String GET_LUCKY_COUNT = "api/lottery/find/sharer/status/";//获取活动的次数
    //第三方登录
    String THIRD_BIND_CODE = "/api/lottery/captchas/mobile/";// 第三方绑定发送验证码
    //微博
    String ISBIND_WEIBO = "weiBo/isregister";//判断是否绑定了微博
    String BIND_WEIBO = "weiBo/isbind";//微博登录绑定
    //QQ
    String ISBIND_QQ = "qq/isregister";//判断是否绑定了QQ
    String BIND_QQ = "qq/isbind";//QQ登录绑定
    //微信
    String ISBIND_WEIXIN="weixin/isregister";
    String BIND_WEIXIN="weixin/isbind";
    String CHECK_THIRD_BIND="qq/whobind"; //判断绑定三方
}

