<!-- 前台系统首页页面 -->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <#include "../common/head_url.ftl"/>
</head>

<body>
<#include "../common/top_menu.ftl"/>
<div id="system_index">
    <div class="content">
        <div class="width1190">
            <h2 class="title">租房 <a href="/home/house/achieve_list">更多&gt;&gt;</a></h2>
            <div class="index-fang-list">
                <span v-for="(house,index) in rentingHouseList">
                    <span v-if="index == 3">
                        <dl style="margin-right: 0px;">
                            <dt><a :href="'/home/house/info?id='+house.id"><img :src="'/common/photo/view?filename='+house.coverPhoto" width="286" height="188" /></a></dt>
                            <dd>
                                <h3><a :href="'/home/house/info?id='+house.id">{{house.info | limitInfoStr}}</a></h3>
                                <div class="hui">{{house.houseType}} | {{house.area}}m² | {{house.renovation}}</div>
                            </dd>
                        </dl>
                    </span>
                    <span v-if="index < 3">
                         <dl>
                            <dt><a :href="'/home/house/info?id='+house.id"><img :src="'/common/photo/view?filename='+house.coverPhoto" width="286" height="188" /></a></dt>
                            <dd>
                                <h3><a :href="'/home/house/info?id='+house.id">{{house.info | limitInfoStr}}</a></h3>
                                <div class="hui">{{house.houseType}} | {{house.area}}m² | {{house.renovation}}</div>
                            </dd>
                        </dl>
                    </span>
                </span>
                <div class="clears"></div>
            </div><!--index-fang-list/-->

            <h2 class="title">购房 <a href="/home/house/achieve_list">更多&gt;&gt;</a></h2>
            <div class="index-fang-list">
                <span v-for="(house,index) in purchaseHouseList">
                    <span v-if="index == 3">
                        <dl style="margin-right: 0px;">
                            <dt><a :href="'/home/house/info?id='+house.id"><img :src="'/common/photo/view?filename='+house.coverPhoto" width="286" height="188" /></a></dt>
                            <dd>
                                <h3><a :href="'/home/house/info?id='+house.id">{{house.info | limitInfoStr}}</a></h3>
                                <div class="hui">{{house.houseType}} | {{house.area}}m² | {{house.renovation}}</div>
                            </dd>
                        </dl>
                    </span>
                    <span v-if="index < 3">
                        <dl>
                            <dt><a :href="'/home/house/info?id='+house.id"><img :src="'/common/photo/view?filename='+house.coverPhoto" width="286" height="188" /></a></dt>
                            <dd>
                                <h3><a :href="'/home/house/info?id='+house.id">{{house.info | limitInfoStr}}</a></h3>
                                <div class="hui">{{house.houseType}} | {{house.area}}m² | {{house.renovation}}</div>
                            </dd>
                        </dl>
                    </span>
                </span>
                <div class="clears"></div>
            </div><!--index-fang-list/-->

        </div><!--width1190/-->
    </div><!--content/-->
</div>
<div class="xinren">
    <div class="width1190">
        <dl style="background:url(/home/house/images/icon1.jpg) left center no-repeat;">
            <dt>承诺</dt>
            <dd>真实可信100%真房源<br />链家承诺，假一赔百</dd>
        </dl>
        <dl style="background:url(/home/house/images/icon2.jpg) left center no-repeat;">
            <dt>权威</dt>
            <dd>独家房源 覆盖全城<br />房源信息最权威覆盖最广</dd>
        </dl>
        <dl style="background:url(/home/house/images/icon3.jpg) left center no-repeat;">
            <dt>信赖</dt>
            <dd>万名置业顾问 专业服务<br />百万家庭的信赖之选</dd>
        </dl>
        <dl style="background:url(/home/house/images/icon4.jpg) left center no-repeat;">
            <dt>安全</dt>
            <dd>安心承诺 保驾护航<br />多重风险防范机制 无后顾之忧</dd>
        </dl>
        <div class="clears"></div>
    </div><!--width1190/-->
</div><!--xinren/-->
</body>
<script type="text/javascript">

    const system_index = new Vue({
        el:"#system_index",  //element 用来给Vue实例定义一个作用范围
        data:{      //用来给Vue实例定义一些相关数据
            rentingHouseList:[],
            purchaseHouseList:[]
        },
        created(){
            axios.post("/home/house/get_renting_house").then((res)=>{
                if(res.data.code == 0){
                    this.rentingHouseList = res.data.data;
                }else{
                    layer.alert(res.data.msg, {icon: 5});
                }
            }).catch(()=>{
                layer.alert("网络错误，获取租房数据失败！", {icon: 5});
            });
            axios.post("/home/house/get_purchase_house").then((res)=>{
                if(res.data.code == 0){
                    this.purchaseHouseList = res.data.data;
                }else{
                    layer.alert(res.data.msg, {icon: 5});
                }
            }).catch(()=>{
                layer.alert("网络错误，获取购房数据失败！", {icon: 5});
            });
        },
        methods:{

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
