<!-- 前台用户注册页面 -->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <#include "../common/head_url.ftl"/>
</head>

<body>
<#include "../common/top_menu.ftl"/>
<div id="register">
	<div class="content">
		<div class="width1190">
			<div class="reg-logo">
				<form id="register-form" class="zcform">
					<p class="clearfix">
						<label class="one" for="username">用户昵称：</label>
						<input id="username" name="username" class="required" value type="text" placeholder="请输入用户昵称" />
					</p>

					<p class="clearfix">
						<label class="one"  for="password">登录密码：</label>
						<input id="password" name="password"  type="password" class="required" value placeholder="请输入密码" />
					</p>
					<p class="clearfix">
						<label class="one" for="confirm_password">确认密码：</label>
						<input id="confirm_password" name="confirmPassword" type="password" class="required" value placeholder="请再次输入密码" />
					</p>
					<p class="clearfix">
						<label class="one" for="email">电子邮箱：</label>
						<input id="email" name="email" type="text" class="required" value placeholder="请输入电子邮箱" />
					</p>
					<p class="clearfix">
						<label class="one" for="phone">手机号码：</label>
						<input id="phone" name="phone" type="text" class="required" value placeholder="请输入手机号码" />
					</p>
					<p class="clearfix">
						<label class="one" for="cpacha">验证码：</label>
						<input id="cpacha" name="cpacha" type="text" class="required" value placeholder="请输入验证码" />
					</p>
					<p class="clearfix">
						<label class="one" for="cpacha-img"></label>
						<img id="cpacha-img" title="点击切换验证码" style="cursor:pointer;" src="/common/cpacha/generate_cpacha?vl=4&fs=21&w=98&h=33&method=home_user_register" width="110px" height="36px" @click="changeCpacha()">
					</p>
					
					<p class="clearfix"><input class="submit" @click="submitRegisterForm()" type="button" value="立即注册"/></p>
				</form>
				<div class="reg-logo-right">
					<h3>如果您已有账号，请</h3>
					<a href="/home/user/login" class="logo-a">立即登录</a>
				</div><!--reg-logo-right/-->
				<div class="clears"></div>
			</div><!--reg-logo/-->
		</div><!--width1190/-->
	</div><!--content/-->
</div> <!--register/-->
</body>
<script type="text/javascript">

	const register = new Vue({
		el:"#register",  //element 用来给Vue实例定义一个作用范围
		data:{      //用来给Vue实例定义一些相关数据

		},
		methods:{
			//切换验证码
			changeCpacha(){
				$("#cpacha-img").attr("src",'/common/cpacha/generate_cpacha?vl=4&fs=21&w=98&h=33&method=home_user_register&t=' + new Date().getTime());
			},
			//注册操作处理
			submitRegisterForm(){
				var email = document.getElementById("email");
				//对电子邮箱格式的验证
				var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
				if(!myreg.test(email.value))
				{
					layer.alert("电子邮箱格式不正确!", {icon: 5});
					return false;
				}
				//表单数据序列化
				var data = $('#register-form').serialize();

				axios.post("/home/user/register", data).then((res)=>{
					if(res.data.code == 0){
						layer.alert(res.data.msg, {icon: 6});
					}else{
						layer.alert(res.data.msg, {icon: 5});
					}
					this.$options.methods.changeCpacha();  //在data外面定义的属性和方法通过$options可以获取和调用
				}).catch(()=>{
					layer.alert("网络错误，注册失败！", {icon: 5});
					this.$options.methods.changeCpacha();  //在data外面定义的属性和方法通过$options可以获取和调用
				});

			}

		}
	});

</script>
</html>
