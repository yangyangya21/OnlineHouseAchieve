<!-- 前台用户登录页面 -->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <#include "../common/head_url.ftl"/>
</head>

<body>
<#include "../common/top_menu.ftl"/>
<div id="login">
    <div class="content">
        <div class="width1190">

            <div class="reg-logo">
                <form id="loginForm"  class="zcform">
                    <input type="hidden" value="home" name="type" />
                    <p class="clearfix">
                        <label class="one" for="username">用户昵称：</label>
                        <input id="username" name="username" type="text" class="required" value placeholder="请输入用户昵称" />
                    </p>
                    <p class="clearfix">
                        <label class="one"  for="password">登录密码：</label>
                        <input id="password" name="password" type="password" class="required" value placeholder="请输入密码" />
                    </p>
                    <p class="clearfix">
                        <label class="one" for="cpacha">验证码：</label>
                        <input id="cpacha" name="cpacha" type="text" class="required" value placeholder="请输入验证码" />
                    </p>
                    <p class="clearfix">
                        <label class="one" for="cpacha-img"></label>
                        <img id="cpacha-img" title="点击切换验证码" style="cursor:pointer;" src="/common/cpacha/generate_cpacha?vl=4&fs=21&w=98&h=33&method=home_user_login" width="110px" height="36px" @click="changeCpacha()">
                    </p>

                    <p class="clearfix"><input class="submit" type="button" @click="submitLoginForm();" value="立即登录"/></p>
                </form>
                <div class="reg-logo-right">
                    <h3>如果您没有账号，请</h3>
                    <a href="/home/user/register" class="logo-a">立即注册</a>
                </div><!--reg-logo-right/-->
                <div class="clears"></div>
            </div><!--reg-logo/-->
        </div><!--width1190/-->
    </div><!--content/-->
</div><!--login/-->
</body>
<script type="text/javascript">


    const login = new Vue({
        el:"#login",  //element 用来给Vue实例定义一个作用范围
        data:{      //用来给Vue实例定义一些相关数据

        },
        methods:{
            //切换验证码
            changeCpacha(){
                $("#cpacha-img").attr("src",'/common/cpacha/generate_cpacha?vl=4&fs=21&w=98&h=33&method=home_user_login&t=' + new Date().getTime());
            },
            //登录操作处理
            submitLoginForm(){
                var data = $('#loginForm').serialize();

                axios.post("/login", data).then((res)=>{
                    if(res.data.code == 0){
                        setTokenToCookie("home_login_token", res.data.data, 7);
                        window.location.href = "/home/system/index";
                    }else{
                        layer.alert(res.data.msg, {icon: 5});
                    }
                    this.$options.methods.changeCpacha();
                }).catch(()=>{
                    layer.alert("网络错误，登录失败！", {icon: 5});
                    this.$options.methods.changeCpacha();
                });
            }

        }
    });

</script>


</html>
