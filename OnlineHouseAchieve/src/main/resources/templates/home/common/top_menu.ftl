<!-- 公共顶部菜单 -->
<div id="top_menu">
    <div class="header">
        <div class="width1190">
            <div class="fl">您好，欢迎来到《邻居大妈》，一个在线淘房网站！</div>
            <div class="fr">
                <a href="/home/user/login" v-if="!username">登录|</a>
                <a href="/home/user/register" v-if="!username">注册|</a>
                <a href="javascript:void(0);" v-if="username">欢迎您，<span v-text="username"></span>！|</a>
                <a href="javascript:void(0);" @click="myCustomerService();" v-if="roleId == '2'">我的客服|</a>
                <a href="/home/user/index" v-if="username">我的信息|</a>
                <a href="/logout?type=home" v-if="username">退出登录</a>
            </div>
            <div class="clears"></div>
        </div><!--width1190/-->
    </div><!--header/-->
    <div class="logo-phone">
        <div class="width1190">
            <h1 class="logo"><a href="/home/system/index"><img src="/home/house/images/logo.png" width="163" height="59" /></a></h1>
            <div class="phones"><strong>021-63179891</strong></div>
            <div class="clears"></div>
        </div><!--width1190/-->
    </div><!--logo-phone/-->
    <div class="list-nav">
        <div class="width1190">
            <ul class="nav">
                <li><a href="/home/system/index">首页</a></li>
                <li><a href="/home/house/achieve_list">淘房</a></li>
                <li><a href="/home/user/apply_agent_index">申请成为中介</a></li>
                <li><a href="/home/system/about">关于我们</a></li>

            </ul><!--nav/-->
            <div class="clears"></div>
        </div><!--width1190/-->
    </div><!--list-nav/-->
    <div class="banner" style="background:url(/home/house/images/ban.jpg) "></div>
</div>
<script type="text/javascript">


    const top_menu = new Vue({
        el:"#top_menu",  //element 用来给Vue实例定义一个作用范围
        data:{      //用来给Vue实例定义一些相关数据
            username:"${username!""}",
            roleId:"${roleId!""}"
        },
        methods:{
            myCustomerService(){
                axios.post("/home/user/my_customer_service").then((res)=>{
                    if(res.data.code == 0){
                        window.location.href = "/home/chat/index";
                    }else{
                        layer.alert(res.data.msg, {icon: 5});
                    }
                }).catch(()=>{
                    layer.alert("网络错误，进入我的客服页面失败！", {icon: 5});
                });
            }
        }
    });

</script>
