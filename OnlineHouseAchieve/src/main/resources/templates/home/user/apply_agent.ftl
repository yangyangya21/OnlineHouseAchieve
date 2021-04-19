<!-- 前台用户申请成为中介页面 -->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <#include "../common/head_url.ftl"/>
</head>

<body>
<#include "../common/top_menu.ftl"/>
<div id="apply_agent">
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
                <h3 class="vipright-title">申请社区自由经纪人</h3>
                <form id="applyAgentForm">
                    <dl class="vip-touxiang">
                        <input type="hidden" id="headPic" name="headPic" value="common/user_img.jpg" />
                        <dt><img src="/common/photo/view?filename=common/tx.jpg" id="preview-headPic" width="100" height="100" /></dt>
                        <dd>
                            <h3><strong>请确保是本人头像，否则审核不通过！</strong></h3>
                            <div class="sctx"><a href="javascript:void(0);" @click="uploadPhoto()">上传</a></div>
                            <p>图片格式：GIF、JPG、JPEG、PNG ，最适合尺寸100*100像素</p>
                        </dd>
                        <div class="clearfix"></div>
                    </dl><!--vip-touxiang/-->
                    <table class="grinfo">
                        <tbody>
                        <tr>
                            <th>手机号：</th>
                            <td> <strong v-text="user.phone"></strong>
                            </td>
                        </tr>
                        <tr>
                            <th>年龄：</th>
                            <td>
                                <input class="inp inw" type="text" id="age" name="age"  onkeyup="value=value.replace(/[^\d]/g,'')"/>
                            </td>
                        </tr>
                        <tr>
                            <th>身份证号：</th>
                            <td>
                                <input class="inp inw" type="text" id="idCard" name="idCard">
                            </td>
                        </tr>
                        <tr>
                            <th>真实姓名：</th>
                            <td>
                                <input class="inp inw" type="text" id="realName" name="realName">
                            </td>
                        </tr>
                        <tr>
                            <th>QQ：</th>
                            <td>
                                <input class="inp inw" type="text" id="QQ" name="QQ">
                            </td>
                        </tr>
                        <tr>
                            <th>&nbsp;</th>
                            <td colspan="2">
                                <label class="butt" id="butt">
                                    <div class="member_mod_buttom" @click="submitApplyAgent();">提交申请</div>
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
</div>
<input type="file" id="photo-file" style="display:none;" onchange="upload()">
<div class="copy">Copyright@ 2015 邻居大妈 版权所有 沪ICP备1234567号-0&nbsp;&nbsp;&nbsp;&nbsp;技术支持：<a target="_blank" href="https://space.bilibili.com/384182241">杨杨吖</a> </div>
<div class="bg100"></div>
</body>
<script type="text/javascript">

    const apply_agent = new Vue({
        el:"#apply_agent",  //element 用来给Vue实例定义一个作用范围
        data:{      //用来给Vue实例定义一些相关数据
            user:{}
        },
        created(){
            $("#user-order-time").removeClass("vipNavCur");
            $("#user-apply-agent").addClass("vipNavCur");
            axios.post("get_user_info").then((res)=>{
                if(res.data.code == 0){
                    this.user = res.data.data;
                }else{
                    layer.alert(res.data.msg, {icon: 5});
                }
            }).catch(()=>{
                layer.alert("网络错误，获取用户数据失败！", {icon: 5});
            });
        },
        methods:{
            //提交申请成为中介
            submitApplyAgent(){
                var data = $("#applyAgentForm").serialize();
                axios.post("submit_apply_agent", data).then((res)=>{
                    if(res.data.code == 0){
                        layer.alert(res.data.msg, {icon: 6});
                    }else{
                        layer.alert(res.data.msg, {icon: 5});
                    }
                }).catch(()=>{
                    layer.alert("网络错误，提交申请失败！", {icon: 5});
                });
            },
            uploadPhoto(){
                $("#photo-file").click();
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
