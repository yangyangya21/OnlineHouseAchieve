<!-- 前台系统房屋详情页面 -->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <#include "../common/head_url.ftl"/>
    <link rel="stylesheet" type="text/css" href="/admin/easyui/easyui/1.3.4/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="/admin/easyui/css/wu.css" />
    <link rel="stylesheet" type="text/css" href="/admin/easyui/css/icon.css" />
    <script type="text/javascript" src="/admin/easyui/js/jquery-1.8.0.min.js"></script>
    <script type="text/javascript" src="/admin/easyui/easyui/1.3.4/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="/admin/easyui/easyui/1.3.4/locale/easyui-lang-zh_CN.js"></script>
</head>
<style type="text/css"> .dialog-button { padding: 5px; text-align: center; plain: true} </style>
<body>
<#include "../common/top_menu.ftl"/>
<div id="info">
    <!-- 填写预约时间窗口 -->
    <div id="order-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:450px; padding:10px;">
        <form id="order-form">
            <table>
                <tr>
                    <td width="100" align="right">预约时间:</td>
                    <td><input class="easyui-datetimebox" id="time" name="orderTime" data-options="required:true, showSeconds:true, editable:false, missingMessage:'请选择预约看房时间！'" style="width:150px" />
                        <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-cancel" title="清空时间" @click="resetTime('time');" plain="true"></a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div class="content">
        <div class="width1190" style="width:1000px;">
            <div class="proImg fl">
                <span v-if="house.coverPhoto" ><img :src="'/common/photo/view?filename='+house.coverPhoto"/></span>
            </div><!--proImg/-->
            <div class="proText fr">
                <h3 class="proTitle"><span v-text="house.info"></span></h3>
                <div class="proText1">
                    方式：<span v-if="house.category == 1">租房</span><span v-if="house.category == 2">购房</span><br />
                    售价：<span v-if="house.category == 1">{{house.money}}元/月</span><span v-if="house.category == 2">{{house.money}}元</span><br />
                    户型：<span v-text="house.houseType"></span><br />
                    面积：{{house.area}}㎡<br />
                    朝向：<span v-text="house.orientation"></span><br />
                    楼层：<span v-text="house.floor"></span><br />
                    装修：<span v-text="house.renovation"></span><br />
                    位置：<span v-text="house.location"></span><br />
                    发布人：<span v-if="house.user"><span v-text="house.user.username"></span></span>
                </div>
                <div class="xun-car">
                    <a href="javascript:void(0);" @click="openOrderDialog();" class="projrgwc">预约看房</a>
                    <a href="javascript:void(0);" @click="chatWithAgent();" class="xwjg">找中介聊聊</a>
                </div><!--xun-car/-->
            </div><!--proText/-->
            <div class="clears"></div>
        </div><!--width1190/-->
        <div class="proBox" style="width:1000px;margin:10px auto;">
            <div class="proEq">
                <ul class="fl">
                    <li class="proEqCur">房源详情</li>
                </ul>
                <div class="lxkf fr"><a href="http://wpa.qq.com/msgrd?v=3&uin=1072631488&site=qq&menu=yes" target="_blank"></a></div>
                <div class="clears"></div>
            </div><!--proEq/-->
            <div class="proList">
                {{house.details}}
            </div><!--proList/-->
        </div><!--proBox/-->
    </div><!--content/-->

    <div class="copy">Copyright@ 2015 邻居大妈 版权所有 沪ICP备1234567号-0&nbsp;&nbsp;&nbsp;&nbsp;技术支持：<a target="_blank" href="https://space.bilibili.com/384182241">杨杨吖</a> </div>
    <div class="bg100"></div>
</div>
</body>
<script type="text/javascript">

    const info = new Vue({
        el:"#info",  //element 用来给Vue实例定义一个作用范围
        data:{      //用来给Vue实例定义一些相关数据
            house:{}
        },
        created(){
            var id = this.$options.methods.getUrlParam('id');
            axios.post("get_info_data",{id:id}).then((res)=>{
                if(res.data.code == 0){
                    this.house = res.data.data;
                }else{
                    layer.alert(res.data.msg, {icon: 5});
                }
            }).catch(()=>{
                layer.alert("网络错误，获取房屋详情数据失败！", {icon: 5});
            });
        },
        methods:{
            getUrlParam(name) {
                var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
                var r = window.location.search.substr(1).match(reg); //匹配目标参数
                if (r != null) return unescape(r[2]);
                return null;
            },
            resetTime(id){
                $('#'+id).datetimebox('setValue', '');
            },
            openOrderDialog(){
                $('#order-dialog').dialog({
                    closed: false,
                    modal:true,
                    title: "预约看房时间",
                    buttons: [{
                        text: '确定',
                        iconCls: 'icon-ok',
                        handler:  orderTime
                    }, {
                        text: '取消',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $('#order-dialog').dialog('close');
                        }
                    }],
                    onBeforeOpen:function(){
                        //$("#add-form input").val('');
                    }
                });
            },
            chatWithAgent(){
                axios.post("chat_with_agent",{userId:this.house.userId}).then((res)=>{
                    if(res.data.code == 0){
                        window.location.href = "/home/chat/index";
                    }else{
                        layer.alert(res.data.msg, {icon: 5});
                    }
                }).catch(()=>{
                    layer.alert("网络错误，进入与中介交流的页面失败！", {icon: 5});
                });
            }
        }
    });
    function orderTime(){
        var data = {
            orderTime : $("#time").datetimebox('getValue'),
            houseId : info.house.id
        };
        axios.post("/home/order_time/order", data).then((res)=>{
            if(res.data.code == 0){
                layer.alert(res.data.msg, {icon: 6});
                $('#order-dialog').dialog('close');
            }else{
                layer.alert(res.data.msg, {icon: 5});
            }
        }).catch(()=>{
            layer.alert("网络错误，预约看房时间失败！", {icon: 5});
        });
    }

</script>
</html>
