<!-- 前台用户个人信息页面 -->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <#include "../common/head_url.ftl"/>
</head>
<body>
<#include "../common/top_menu.ftl"/>
<div id="user_index">
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
                            <a href="/home/user/index" class="vipNavCur">我的资料</a>
                        </dd>
                        <dt class="vipIcon1">我的邻居大妈</dt>
                        <dd>
                            <a href="/home/order_time/user_order_list">预约房源</a>
                            <a href="/home/user/apply_agent_index">申请成为中介</a>
                        </dd>
                    </dl>
                </div><!--vipNav/-->
            </div><!--vip-left/-->
            <div class="vip-right">
                <h3 class="vipright-title">修改头像</h3>
                <form id="updateInfo">
                    <input type="hidden" id="userId" name="id" />
                    <dl class="vip-touxiang">
                        <input type="hidden" id="headPic" name="headPic" value="common/user_img.jpg" />
                        <dt><img src="/common/photo/view?filename=common/user_img.jpg" id="preview-headPic" width="100" height="100" /></dt>
                        <dd>
                            <h3><strong>上传完图像请点击完成修改按钮来保存哦~~</strong></h3>
                            <div class="sctx"><a href="javascript:void(0);" @click="uploadPhoto()">上传</a></div>
                            <p>图片格式：GIF、JPG、JPEG、PNG ，最适合尺寸100*100像素</p>
                        </dd>
                        <div class="clearfix"></div>
                    </dl><!--vip-touxiang/-->
                    <h3 class="vipright-title">修改资料</h3>
                    <table class="grinfo">
                        <tbody>
                        <tr>
                            <th>手机号：</th>
                            <td>
                                <input class="inp inw" type="text" name="phone" id="phone">
                            </td>
                        </tr>
                        <tr>
                            <th>昵称：</th>
                            <td>
                                <input class="inp inw" type="text" name="username" id="username">
                            </td>
                        </tr>
                        <tr>
                            <th>密码：</th>
                            <td>
                                <input class="inp inw" type="password" name="password" id="password">
                            </td>
                        </tr>
                        <tr>
                            <th>性别：</th>
                            <td>
                                <input type="radio" value="1" checked="checked" id="rbSex1" name="sex">
                                <label for="rbSex1">男</label>
                                <input type="radio" value="2" id="rbSex2" name="sex">
                                <label for="rbSex2">女</label>
                                <input type="radio" value="3" id="rbSex3" name="sex">
                                <label for="rbSex3">未知</label>
                                <span id="Sex_Tip"></span>
                            </td>
                        </tr>


                        <tr>
                            <th>电子邮箱：</th>
                            <td>
                                <input class="inp inw" type="text" name="email" id="email">
                            </td>
                        </tr>


                        <tr>
                            <th>&nbsp;</th>
                            <td colspan="2">
                                <label class="butt" id="butt">
                                    <input type="button" class="member_mod_buttom" @click="updateUserInfo()" value="完成修改" />
                                </label>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div><!--vip-right/-->
            <div class="clearfix"></div>
        </div><!--width1190/-->
    </div><!--content/-->
</div><!--user_index/-->
<input type="file" id="photo-file" style="display:none;" onchange="upload()">
<div class="copy">Copyright@ 2015 邻居大妈 版权所有 沪ICP备1234567号-0&nbsp;&nbsp;&nbsp;&nbsp;技术支持：<a target="_blank" href="https://space.bilibili.com/384182241">杨杨吖</a> </div>
<div class="bg100"></div>
<div class="zhidinggoufang">
    <h2>指定购房 <span class="close">X</span></h2>
    <form action="#" method="get">
        <div class="zhiding-list">
            <label>选择区域：</label>
            <select>
                <option>智慧园</option>
                <option>立民村</option>
                <option>塘口村</option>
                <option>勤劳村</option>
                <option>芦胜村</option>
                <option>知新村</option>
            </select>
        </div>
        <div class="zhiding-list">
            <label>方式：</label>
            <select>
                <option>租房</option>
                <option>新房</option>
                <option>二手房</option>
            </select>
        </div>
        <div class="zhiding-list">
            <label>联系方式：</label>
            <input type="text" />
        </div>
        <div class="zhidingsub"><input type="submit" value="提交" /></div>
    </form>
    <div class="zhidingtext">
        <h3>指定购房注意事宜：</h3>
        <p>1、请详细输入您所需要购买的房源信息(精确到小区)</p>
        <p>2、制定购房申请提交后，客服中心会在24小时之内与您取得联系</p>
        <p>3、如有任何疑问，请随时拨打我们的电话：400-000-0000</p>
    </div><!--zhidingtext/-->
</div><!--zhidinggoufang/-->
</body>
<script type="text/javascript">

    const user_index = new Vue({
        el:"#user_index",  //element 用来给Vue实例定义一个作用范围
        data:{      //用来给Vue实例定义一些相关数据
            user:{}
        },
        created(){
            axios.post("get_user_info").then((res)=>{
                if(res.data.code == 0){
                    this.user = res.data.data;
                    if(this.user.sex == 1){
                        $("#rbSex1").attr("checked", true);
                    }else if(this.user.sex == 2){
                        $("#rbSex2").attr("checked", true);
                    }else if(this.user.sex == 3){
                        $("#rbSex3").attr("checked", true);
                    }
                    $("#userId").val(this.user.id);
                    $("#username").val(this.user.username);
                    $("#email").val(this.user.email);
                    $("#phone").val(this.user.phone);
                    $("#password").val(this.user.password);
                    $("#preview-headPic").attr('src','../../common/photo/view?filename='+this.user.headPic);
                    $("#headPic").val(this.user.headPic);
                }else{
                    layer.alert(res.data.msg, {icon: 5});
                }
            }).catch(()=>{
                layer.alert("网络错误，获取用户详情数据失败！", {icon: 5});
            });
        },
        methods:{
            uploadPhoto(){
                $("#photo-file").click();
            },
            updateUserInfo(){
                var data = $("#updateInfo").serialize();
                axios.post("update_user_info", data).then((res)=>{
                    if(res.data.code == 0){
                        layer.alert(res.data.msg, {icon: 6});
                    }else{
                        layer.alert(res.data.msg, {icon: 5});
                    }
                }).catch(()=>{
                    layer.alert("网络错误，更改用户个人信息失败！", {icon: 5});
                });
            }
        }
    });

    //上传图片处理
    function upload(){
        if($("#photo-file").val() == '')return;
        var formData = new FormData();
        formData.append('photo',document.getElementById('photo-file').files[0]);
        $.ajax({
            url:'../../common/upload/upload_photo',
            type:'post',
            data:formData,
            contentType:false,
            processData:false,
            success:function(result){
                if(result.code == 0){
                    $("#preview-headPic").attr('src','../../common/photo/view?filename='+result.data);
                    $("#headPic").val(result.data);
                    layer.alert(result.msg, {icon: 6});
                }else{
                    layer.alert(result.msg, {icon: 5});
                }
            },
            error:function(){
                layer.alert("网络错误，上传失败！", {icon: 5});
            }
        });
    }

</script>
</html>
