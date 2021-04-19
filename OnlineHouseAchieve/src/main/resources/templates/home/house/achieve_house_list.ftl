<!-- 前台系统房屋出租页面 -->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <#include "../common/head_url.ftl"/>
</head>

<body>
<#include "../common/top_menu.ftl"/>
<div id="renting_house_list">
    <div class="content">
        <div class="width1190">
            <form action="#" method="get" class="pro-search">
                <table>
                    <tr>
                        <th>搜索：</th>
                        <td>
                            <input type="text" id="searchByInput" placeholder="请输入你要搜索的房源信息..." style="width: 300px;"/>
                            <input type="button" @click='search();' class="proSub"  value="搜索" />
                        </td>
                    </tr>
                    <tr>
                        <th>租金：</th>
                        <td>
                            <a href="javascript:void(0);" @click='searchByMoney("不限");' class="pro-cur">不限</a>
                            <a href="javascript:void(0);" @click='searchByMoney("0元-1000元");'>0元-1000元</a>
                            <a href="javascript:void(0);" @click='searchByMoney("1001元-2000元");'>1001元-2000元</a>
                            <a href="javascript:void(0);" @click='searchByMoney("2001元-3000元");'>2001元-3000元</a>
                            <a href="javascript:void(0);" @click='searchByMoney("3001元-4000元");'>3001元-4000元</a>
                            <a href="javascript:void(0);" @click='searchByMoney("4000以上");'>4000元以上</a>
                        </td>
                    </tr>
                    <tr>
                        <th>面积：</th>
                        <td>
                            <a href="javascript:void(0);" @click='searchByArea("不限");' class="pro-cur">不限</a>
                            <a href="javascript:void(0);" @click='searchByArea("0平方-40平方");'>0平方-40平方</a>
                            <a href="javascript:void(0);" @click='searchByArea("41平方-60平方");'>41平方-60平方</a>
                            <a href="javascript:void(0);" @click='searchByArea("61平方-80平方");'>61平方-80平方</a>
                            <a href="javascript:void(0);" @click='searchByArea("81平方-100平方");'>81平方-100平方</a>
                            <a href="javascript:void(0);" @click='searchByArea("100平方以上");'>100平方以上</a>
                        </td>
                    </tr>
                    <tr>
                        <th>朝向：</th>
                        <td>
                            <a href="javascript:void(0);" @click='searchByOrientation("不限");' class="pro-cur">不限</a>
                            <a href="javascript:void(0);" @click='searchByOrientation("东");'>东</a>
                            <a href="javascript:void(0);" @click='searchByOrientation("南");'>南</a>
                            <a href="javascript:void(0);" @click='searchByOrientation("西");'>西</a>
                            <a href="javascript:void(0);" @click='searchByOrientation("北");'>北</a>
                            <a href="javascript:void(0);" @click='searchByOrientation("东南");'>东南</a>
                            <a href="javascript:void(0);" @click='searchByOrientation("东北");'>东北</a>
                            <a href="javascript:void(0);" @click='searchByOrientation("西南");'>西南</a>
                            <a href="javascript:void(0);" @click='searchByOrientation("西北");'>西北</a>
                        </td>
                    </tr>
                    <tr>
                        <th>方式：</th>
                        <td>
                            <a href="javascript:void(0);" @click='searchByCategory("不限");' class="pro-cur">不限</a>
                            <a href="javascript:void(0);" @click='searchByCategory("1");'>租房</a>
                            <a href="javascript:void(0);" @click='searchByCategory("2");'>购房</a>
                        </td>
                    </tr>
                </table>
                <div class="paixu">
                    <strong>排序：</strong>
                    <a href="javascript:void(0);" class="pai-cur" @click="defaultSort();">默认</a>
                    <a href="javascript:void(0);" @click="moneySort();">价格 &or;</a>
                    <a href="javascript:void(0);" @click="updateTimeSort();">最新 &or;</a>
                </div>
            </form><!--pro-search/-->
        </div><!--width1190/-->
        <div class="width1190">
            <div class="pro-left">

                <dl v-for="house in houseList">
                    <dt><a :href="'/home/house/info?id='+house.id"><img :src="'/common/photo/view?filename='+house.coverPhoto" style="width:286px;height:210px;"/></a></dt>
                    <dd>
                        <h3><a :href="'/home/house/info?id='+house.id"><span v-html="house.info"></span></a></h3>
                        <div class="pro-wei">
                            <img src="/home/house/images/weizhi.png" width="12" height="16" /> <strong v-html="house.location"></strong>
                        </div>
                        <div class="pro-fang">户型:{{house.houseType}}&nbsp;&nbsp;&nbsp;&nbsp;面积:{{house.area}}㎡ <br><br>
                            朝向:{{house.orientation}}&nbsp;&nbsp;&nbsp;&nbsp;楼层:{{house.floor}}
                        </div>
                        <div class="pra-fa" style="margin-top:5px;">发布人：{{house.user.username}}&nbsp;&nbsp;&nbsp;&nbsp;
                            出售方式：
                            <span v-if="house.category == 1">租房</span>
                            <span v-if="house.category == 2">购房</span>
                            <br><br>最后一次更新时间：{{house.updateTime}}
                        </div>
                    </dd>
                    <div class="price">¥ <strong>{{house.money}}</strong>
                        <span class="font12" v-if="house.category == 1">元/月</span>
                        <span class="font12" v-if="house.category == 2">元</span>
                    </div>
                    <div class="clears"></div>
                </dl>

            </div><!--pro-left/-->
            <div class="pro-right">
                <h2 class="right-title">新上房源</h2>
                <div class="right-pro">
                    <dl v-for="newHouse in newHouseList">
                        <dt><a :href="'/home/house/info?id='+newHouse.id"><img :src="'/common/photo/view?filename='+newHouse.coverPhoto" /></a></dt>
                        <dd>
                            <h3><a :href="'/home/house/info?id='+newHouse.id">{{newHouse.info | limitInfoStr}}</a></h3>
                            <div class="pro-fang">
                                <span v-if="newHouse.category == 1">出售方式：租房</span>
                                <span v-if="newHouse.category == 2">出售方式：购房</span>
                            </div>
                            <span v-if="newHouse.category == 1">
                                <div class="right-price">{{newHouse.money}}元/月</div>
                            </span>
                            <span v-if="newHouse.category == 2">
                                <div class="right-price">{{newHouse.money}}元</div>
                            </span>
                        </dd>
                    </dl>
                </div><!--right-pro/-->
            </div><!--pro-right/-->
            <div class="clears"></div>
        </div><!--width1190/-->
    </div><!--content/-->
</div>

<div class="copy">Copyright@ 2015 邻居大妈 版权所有 沪ICP备1234567号-0&nbsp;&nbsp;&nbsp;&nbsp;技术支持：<a target="_blank" href="https://space.bilibili.com/384182241">杨杨吖</a> </div>
<div class="bg100"></div>
</body>
<script type="text/javascript">

    const renting_house_list = new Vue({
        el:"#renting_house_list",  //element 用来给Vue实例定义一个作用范围
        data:{      //用来给Vue实例定义一些相关数据
            houseList:[],
            newHouseList:[],
            isUpdateTimeOrder:false,
            isMoneyOrder:false,
            money:"",
            area:"",
            orientation:"",
            context:"",
            category:""
        },
        created(){
            //获取所有房源信息
            axios.post("get_data",{}).then((res)=>{
                if(res.data.code == 0){
                    this.houseList = res.data.data;
                }
            }).catch(()=>{
                layer.alert("网络错误，获取房屋数据失败！", {icon: 5});
            });
            //获取新上房源的房屋信息
            axios.post("get_new_house").then((res)=>{
                if(res.data.code == 0){
                    this.newHouseList = res.data.data;
                }
            }).catch(()=>{
                layer.alert("网络错误，获取新上房源的房屋数据失败！", {icon: 5});
            });
            console.log(this.newHouseList);
        },
        methods:{
            //搜索
            search(){
                var context = $("#searchByInput").val();
                this.context = context;
                var data = {
                    context:this.context,
                    isUpdateTimeOrder:this.isUpdateTimeOrder,
                    isMoneyOrder:this.isMoneyOrder,
                    money:this.money,
                    area:this.area,
                    orientation:this.orientation,
                    category:this.category
                };
                axios.post("get_data", data).then((res)=>{
                    if(res.data.code == 0){
                        this.houseList = res.data.data;
                    }
                }).catch(()=>{
                    layer.alert("网络错误，获取房屋数据失败！", {icon: 5});
                });
            },
            //通过租金搜索
            searchByMoney(val){
                this.money = val;
                var data = {
                    isUpdateTimeOrder:this.isUpdateTimeOrder,
                    isMoneyOrder:this.isMoneyOrder,
                    money:this.money,
                    area:this.area,
                    orientation:this.orientation,
                    context:this.context,
                    category:this.category
                };
                axios.post("get_data", data).then((res)=>{
                    if(res.data.code == 0){
                        this.houseList = res.data.data;
                    }
                }).catch(()=>{
                    layer.alert("网络错误，获取房屋数据失败！", {icon: 5});
                });
            },
            //通过面积搜索
            searchByArea(val){
                this.area = val;
                var data = {
                    isUpdateTimeOrder:this.isUpdateTimeOrder,
                    isMoneyOrder:this.isMoneyOrder,
                    money:this.money,
                    area:this.area,
                    orientation:this.orientation,
                    context:this.context,
                    category:this.category
                };
                axios.post("get_data", data).then((res)=>{
                    if(res.data.code == 0){
                        this.houseList = res.data.data;
                    }
                }).catch(()=>{
                    layer.alert("网络错误，获取房屋数据失败！", {icon: 5});
                });
            },
            //通过朝向搜索
            searchByOrientation(val){
                this.orientation = val;
                var data = {
                    isUpdateTimeOrder:this.isUpdateTimeOrder,
                    isMoneyOrder:this.isMoneyOrder,
                    money:this.money,
                    area:this.area,
                    orientation:this.orientation,
                    context:this.context,
                    category:this.category
                };
                axios.post("get_data", data).then((res)=>{
                    if(res.data.code == 0){
                        this.houseList = res.data.data;
                    }
                }).catch(()=>{
                    layer.alert("网络错误，获取房屋数据失败！", {icon: 5});
                });
            },
            //通过出售方式搜索
            searchByCategory(val){
                this.category = val;
                var data = {
                    isUpdateTimeOrder:this.isUpdateTimeOrder,
                    isMoneyOrder:this.isMoneyOrder,
                    money:this.money,
                    area:this.area,
                    orientation:this.orientation,
                    context:this.context,
                    category:this.category
                };
                axios.post("get_data", data).then((res)=>{
                    if(res.data.code == 0){
                        this.houseList = res.data.data;
                    }
                }).catch(()=>{
                    layer.alert("网络错误，获取房屋数据失败！", {icon: 5});
                });
            },

            //默认排序
            defaultSort(){
                this.isMoneyOrder = false;
                this.isUpdateTimeOrder = false;
                var data = {
                    money:this.money,
                    area:this.area,
                    orientation:this.orientation,
                    context:this.context,
                    category:this.category
                };
                axios.post("get_data", data).then((res)=>{
                    if(res.data.code == 0){
                        this.houseList = res.data.data;
                    }
                }).catch(()=>{
                    layer.alert("网络错误，获取房屋数据失败！", {icon: 5});
                });
            },
            //房屋价格排序
            moneySort(){
                this.isMoneyOrder = true;
                this.isUpdateTimeOrder = false;
                var data = {
                    isMoneyOrder:this.isMoneyOrder,
                    money:this.money,
                    area:this.area,
                    orientation:this.orientation,
                    context:this.context,
                    category:this.category
                };
                axios.post("get_data", data).then((res)=>{
                    if(res.data.code == 0){
                        this.houseList = res.data.data;
                    }
                }).catch(()=>{
                    layer.alert("网络错误，获取房屋数据失败！", {icon: 5});
                });
            },
            //房屋信息更新时间排序
            updateTimeSort(){
                this.isUpdateTimeOrder = true;
                this.isMoneyOrder = false;
                var data = {
                    isUpdateTimeOrder:this.isUpdateTimeOrder,
                    money:this.money,
                    area:this.area,
                    orientation:this.orientation,
                    context:this.context,
                    category:this.category
                };
                axios.post("get_data", data).then((res)=>{
                    if(res.data.code == 0){
                        this.houseList = res.data.data;
                    }
                }).catch(()=>{
                    layer.alert("网络错误，获取房屋数据失败！", {icon: 5});
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
