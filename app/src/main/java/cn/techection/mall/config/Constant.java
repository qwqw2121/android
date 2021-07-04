package cn.techection.mall.config;

public class Constant {

    public static class API{
        //基地址
       // public static final String BASE_URL="http://106.55.44.114/actionmall/mgr/";
       public static final String BASE_URL="http://39.97.173.123:8080//actionmall/";
       // public static final String BASE_URL="http://10.21.142.92:8888/mall/";
        //产品类型参数地址
        public static final String CATEGORY_PARAM_URL=BASE_URL+"param/findallparams.do";
        //热销商品
        public static final String HOT_PRODUCT_URL=BASE_URL+"product/findhotproducts.do";
        //商品分页列表
        public static final String CATEGORY_PRODUCT_URL=BASE_URL+"product/findproducts.do";
        //商品详情
        public static final String PRODUCT_DETAIL_URL=BASE_URL+"product/getdetail.do";

        //购物车列表
        public static final String CART_LIST_URL=BASE_URL+"cart/findallcarts.do";
        //加入购物车
        public static final String CART_ADD_URL=BASE_URL+"cart/savecart.do";
        //更新购物中商品的数量
        public static final String CART_UPDATE_URL=BASE_URL+"cart/updatecarts.do";
        //删除购物车中商品
        public static final String CART_DEL_URL=BASE_URL+"cart/deletecarts.do";
        // 获取购物车中商品数量
        public static final String CART_COUNT_URL=BASE_URL+"cart/getcartcount.do ";
       // 清空购物车接口
       public static final String CART_CLEAR_URL=BASE_URL+"cart/clearcarts.do ";

        //登陆接口
        public static final String USER_LOGIN_URL=BASE_URL+"/user/do_login.do";
        //获取用户信息
        public static final String USER_INFO_URL=BASE_URL+"user/getuserinfo.do";

        //地址列表
        public static final String USER_ADDR_LIST_URL=BASE_URL+"addr/findaddrs.do";
        //删除地址
        public static final String USER_ADDR_DEL_URL=BASE_URL+"addr/deladdr.do";
        //设置默认地址
        public static final String USER_ADDR_DEFAULT_URL=BASE_URL+"addr/setdefault.do";
        //添加新地址
        public static final String USER_ADDR_ADD_URL=BASE_URL+"addr/saveaddr.do";


        //提交（创建）订单
        public static final String ORDER_CREATED_URL=BASE_URL+"prder/createorder.do";
        //确认收货接口
        public static final String OEDER_AFFIRM_URL=BASE_URL+"order/confirmreceipt.do";
        //订单详情---
        public static final String OEDER_DETAIL_URL=BASE_URL+"order/getdetail.do";
         //订单取消---
        public static final String ORDER_CANCLE_URL=BASE_URL+"order/cancelorder.do ";
        //订单列表---
        public static final String ORDER_LIST_URL=BASE_URL+"order/getList.do";
    }

    public static class ACTION{
        //加载购物车列表的
        public static final String LOAD_CART_ACTION="cn.techaction.mall.LOAD_CART_ACTION";
    }
}
