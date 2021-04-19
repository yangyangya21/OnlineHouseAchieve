<!-- 前台用户预约时间列表信息页面 -->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <#include "../common/head_url.ftl"/>
</head>
<body>
<#include "../common/top_menu.ftl"/>
<div id="order_time_list">
    <div class="content">
        <div class="width1190">
            <div class="vip-left">
                <div class="vipNav">
                    <h3 class="vipTitle">
                        会员中心
                        <span v-if="user.roleId == 1">[普通用户]</span>
                        <span v-if="user.roleId == 2">[房屋中介]</span>
                        <span v-if="user.roleId == 3">[管理员]</span>
                    </h3>
                    <dl>
                        <dt class="vipIcon3">账户设置</dt>
                        <dd>
                            <a href="/home/user/index">我的资料</a>
                        </dd>
                        <dt class="vipIcon1">我的邻居大妈</dt>
                        <dd>
                            <a href="/home/order_time/user_order_list" id="user-order-time" class="vipNavCur">预约房源</a>
                            <a href="/home/user/apply_agent_index" id="user-apply-agent">申请成为中介</a>
                        </dd>
                    </dl>
                </div><!--vipNav/-->
            </div><!--vip-left/-->
            <div class="vip-right">
                <h3 class="vipright-title">关注房源</h3>
                <ul class="guanzhueq">
                    <li class="guanzhueqcur"><a href="javascript:void(0);">租房</a></li>
                    <div class="clearfix"></div>
                </ul><!--guanzhueq/-->
                <div class="guanzhulist">
                    <dl v-for="orderTime in orderTimeList">
                        <dt><a :href="'/home/house/info?id='+orderTime.house.id"><img :src="'/common/photo/view?filename='+orderTime.house.coverPhoto" style="height:150px;width:190px;" /></a></dt>
                        <dd>
                            <h3><a :href="'/home/house/info?id='+orderTime.house.id">{{orderTime.house.info | limitInfoStr}}</a></h3>
                            <div class="guantext" v-text="orderTime.house.location"></div>
                            <div class="guantext">中介回复：<span v-text="orderTime.agentReply"></span></div>
                            <div class="guantext">{{orderTime.house.houseType}} | {{orderTime.house.area}}㎡ | {{orderTime.house.orientation}} |
                                <span v-if="orderTime.house.category == 1">租房</span>
                                <span v-if="orderTime.house.category == 2">购房</span>
                            </div>
                            <div class="guantext2">预约时间：<span v-text="orderTime.orderTime"></span>&nbsp;&nbsp;&nbsp;&nbsp;
                                预约状态：<span v-if="orderTime.state == 1" style="color:lawngreen">中介同意</span>
                                <span v-if="orderTime.state == 2" style="color:red">中介拒绝</span>
                                <span v-if="orderTime.state == 3" style="color:orange">待回复</span>&nbsp;&nbsp;&nbsp;&nbsp;
                                <a href="javascript:void(0);" @click='cancelOrderTime(orderTime.id)' class="qxgz">取消/删除</a>
                            </div>
                        </dd>
                        <div class="price">¥ <strong v-text="orderTime.house.money"></strong>
                            <span class="font12" v-if="orderTime.house.category == 1">元/月</span>
                            <span class="font12" v-if="orderTime.house.category == 2">元</span>
                        </div>
                        <div class="clearfix"></div>
                    </dl>
                </div><!--guanzhulist/-->
                <div class="pages_Collect">
                    <a v-if="nowPage == 1" href="javascript:void(0);" style="margin-left: -50px">&lt;&lt;</a>
                    <a v-if="nowPage != 1" href="javascript:void(0);" @click='skipPage(nowPage-1)' style="margin-left: -50px">&lt;&lt;</a>
                    <span v-for="i in totalPage">
                        <span v-if="i == (nowPage-2)">
                            <a href="javascript:void(0);" @click='skipPage(i)' v-text="i"></a>
                        </span>
                        <span v-if="i == (nowPage-1)">
                            <a href="javascript:void(0);" @click='skipPage(i)' v-text="i"></a>
                        </span>
                        <span v-if="i == nowPage">
                            <a href="javascript:void(0);" class="on" @click='skipPage(i)' v-text="i"></a>
                        </span>
                         <span v-if="i == (nowPage+1)">
                            <a href="javascript:void(0);" @click='skipPage(i)' v-text="i"></a>
                        </span>
                        <span v-if="i == (nowPage+2)">
                            <a href="javascript:void(0);" @click='skipPage(i)' v-text="i"></a>
                        </span>
                    </span>
                    <a v-if="nowPage == totalPage" href="javascript:void(0);" >&gt;&gt;</a>
                    <a v-if="nowPage != totalPage" href="javascript:void(0);" @click='skipPage(nowPage+1)' >&gt;&gt;</a>
                </div>
            </div><!--vip-right/-->
            <div class="clearfix"></div>
        </div><!--width1190/-->
    </div><!--content/-->
</div>
<div class="copy">Copyright@ 2015 邻居大妈 版权所有 沪ICP备1234567号-0&nbsp;&nbsp;&nbsp;&nbsp;技术支持：<a target="_blank" href="https://space.bilibili.com/384182241">杨杨吖</a> </div>
<div class="bg100"></div>
</body>
<script type="text/javascript">

    const order_time_list = new Vue({
        el:"#order_time_list",  //element 用来给Vue实例定义一个作用范围
        data:{      //用来给Vue实例定义一些相关数据
            orderTimeList:[],
            totalPage:1,
            nowPage:1,
            user:{}
        },
        created(){
            $("#user-order-time").addClass("vipNavCur");
            $("#user-apply-agent").removeClass("vipNavCur");
            axios.post("get_user_order",{}).then((res)=>{
                if(res.data.code == 0){
                    this.orderTimeList = res.data.data.data;
                    this.totalPage = res.data.data.totalPage;
                }else{
                    layer.alert(res.data.msg, {icon: 5});
                }
            }).catch(()=>{
                layer.alert("网络错误，获取用户预约房源数据失败！", {icon: 5});
            });
            axios.post("/home/user/get_user_info").then((res)=>{
                if(res.data.code == 0){
                    this.user = res.data.data;
                }else{
                    layer.alert(res.data.msg, {icon: 5});
                }
            }).catch(()=>{
                layer.alert("网络错误，获取当前用户数据失败！", {icon: 5});
            });
        },
        methods:{
            skipPage(nowPage){
                axios.post("get_user_order", {page:nowPage}).then((res)=>{
                    if(res.data.code == 0){
                        this.orderTimeList = res.data.data.data;
                        this.totalPage = res.data.data.totalPage;
                        this.nowPage = res.data.data.page;
                    }else{
                        layer.alert(res.data.msg, {icon: 5});
                    }
                }).catch(()=>{
                    layer.alert("网络错误，获取用户预约房源数据失败！", {icon: 5});
                });
            },
            //取消或删除预约
            cancelOrderTime(id) {
                layer.confirm("是否确定要取消/删除预约呢？", {btn: ['确定', '取消'], title: "提示"}, function () {
                    axios.post("cancel_order_time", {id: id}).then((res) => {
                        if (res.data.code == 0) {
                            layer.alert(res.data.msg, {icon: 6}, function () {
                                window.location.reload();
                            });
                        } else {
                            layer.alert(res.data.msg, {icon: 5});
                        }
                    }).catch(() => {
                        layer.alert("网络错误，取消/删除预约失败！", {icon: 5});
                    });
                });
            }
        },
        filters:{
            //限制字符串长度
            limitInfoStr(str){
                if(str.length > 16){
                    str = str.substring(0,16);
                    str = str + "...";
                }
                return str;
            }
        }
    });


</script>
</html>
