<!--后台管理系统用户列表页面-->
<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>后台管理系统</title>
    <#include "../common/head_url.ftl"/>
</head>
<style type="text/css"> .dialog-button { padding: 5px; text-align: center; } </style>
<div class="easyui-layout" data-options="fit:true">
    <!-- Begin of toolbar -->
    <div id="wu-toolbar">
        <div class="wu-toolbar-button">
            <@security.authorize access="hasAuthority('/admin/user/add')">
                <a href="#" class="easyui-linkbutton" iconCls="icon-add" onclick="openAdd();" plain="true">添加</a>
            </@security.authorize>
            <@security.authorize access="hasAuthority('/admin/user/edit')">
                <a href="#" class="easyui-linkbutton" iconCls="icon-edit" onclick="openEdit();" plain="true">编辑</a>
            </@security.authorize>
            <@security.authorize access="hasAuthority('/admin/user/delete')">
                <a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="remove();" plain="true">删除</a>
            </@security.authorize>
            <@security.authorize access="hasAuthority('/admin/user/edit_state')">
                <a href="#" class="easyui-linkbutton" iconCls="icon-tag-blue-edit" onclick="openEditState();" plain="true">状态改变</a>
            </@security.authorize>
            <@security.authorize access="hasAuthority('/admin/user/edit_role')">
                <a href="#" class="easyui-linkbutton" iconCls="icon-user-edit" onclick="openEditRole();" plain="true">角色改变</a>
            </@security.authorize>
            <@security.authorize access="hasAuthority('/admin/user/get_apply_info')">
                <a href="#" class="easyui-linkbutton" iconCls="icon-eye" onclick="openApplyView();" plain="true">查看申请</a>
            </@security.authorize>
        </div>
        <div class="wu-toolbar-search">
            <label>用户昵称：</label><input id="search-username" class="wu-text" style="width:100px" />
            <label>用户角色：</label>
            <select id="search-roleId" class="easyui-combobox" panelHeight="auto" editable="false" style="width:120px" >
                <option value="-1">全部</option>
                <option value="1">普通用户</option>
                <option value="2">房产中介</option>
                <option value="3">管理员</option>
            </select>
            <label>用户状态：</label>
            <select id="search-state" class="easyui-combobox" panelHeight="auto" editable="false" style="width:120px" >
                <option value="-1">全部</option>
                <option value="1">正常</option>
                <option value="2">冻结</option>
                <option value="3">申请成为中介</option>
            </select>
            <a href="#" id="search-btn" class="easyui-linkbutton" iconCls="icon-search">搜索</a>
        </div>

    </div>
    <!-- End of toolbar -->
    <table id="data-datagrid" class="easyui-datagrid" toolbar="#wu-toolbar"></table>
</div>
<!-- Begin of easyui-dialog -->

<!-- 添加窗口 -->
<div id="add-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:450px; padding:10px;">
    <form id="add-form">
        <table>
            <tr>
                <td width="100" align="left">头像预览:</td>
                <td valign="middle">
                    <img id="preview-photo" style="float:left;" src="/common/photo/view?filename=common/user_img.jpg" width="100px" height="90px">
                    <a style="float:left;margin-top:40px;" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-upload" onclick="uploadPhoto()" plain="true">上传图片</a>
                </td>
            </tr>
            <tr>
                <td width="100" align="left">用户头像:</td>
                <td><input type="text" id="photo" name="headPic" value="common/user_img.jpg" readonly="readonly" class="wu-text" /></td>
            </tr>
            <tr>
                <td width="100" align="left">用户昵称:</td>
                <td><input type="text" name="username" class="wu-text easyui-validatebox" data-options="required:true, missingMessage:'请填写用户昵称！'" /></td>
            </tr>
            <tr>
                <td width="100" align="left">密码:</td>
                <td><input type="password" name="password" class="wu-text easyui-validatebox" data-options="required:true, missingMessage:'请填写密码！'"/></td>
            </tr>
            <tr>
                <td width="100" align="left">电子邮箱:</td>
                <td><input type="text" name="email" id="email" class="wu-text easyui-validatebox" data-options="required:true, missingMessage:'请填写电子邮箱！'"/></td>
            </tr>
            <tr>
                <td width="100" align="left">手机号:</td>
                <td><input type="text" name="phone" class="wu-text easyui-numberbox easyui-validatebox" data-options="required:true, min:1,precision:0, missingMessage:'请填写11位的手机号！'"/></td>
            </tr>
            <tr>
                <td width="100" align="left">性别:</td>
                <td>
                    <select name="sex" class="easyui-combobox" id="sex" editable="false" panelHeight="auto" style="width:268px">
                        <option value="1">男</option>
                        <option value="2">女</option>
                        <option value="3">未知</option>
                    </select>
                </td>
            </tr>

        </table>
    </form>
</div>

<!-- 修改窗口 -->
<div id="edit-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:450px; padding:10px;">
    <form id="edit-form">
        <input type="hidden" name="id" id="edit-id">
        <table>
            <tr>
                <td width="100" align="left">头像预览:</td>
                <td valign="middle">
                    <img id="edit-preview-photo" style="float:left;" src="/common/photo/view?filename=common/user_img.jpg" width="100px" height="90px">
                    <a style="float:left;margin-top:40px;" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-upload" onclick="uploadPhoto()" plain="true">上传图片</a>
                </td>
            </tr>
            <tr>
                <td width="100" align="left">用户头像:</td>
                <td><input type="text" id="edit-photo" name="headPic" value="common/user_img.jpg" readonly="readonly" class="wu-text" /></td>
            </tr>
            <tr>
                <td width="100" align="left">用户昵称:</td>
                <td><input type="text" id="edit-username" name="username" class="wu-text easyui-validatebox" data-options="required:true, missingMessage:'请填写用户昵称！'" /></td>
            </tr>
            <tr>
                <td width="100" align="left">密码:</td>
                <td><input type="password" id="edit-password" name="password" class="wu-text easyui-validatebox" data-options="required:true, missingMessage:'请填写密码！'"/></td>
            </tr>
            <tr>
                <td width="100" align="left">电子邮箱:</td>
                <td><input type="text" name="email" id="edit-email" class="wu-text easyui-validatebox" data-options="required:true, missingMessage:'请填写电子邮箱！'"/></td>
            </tr>
            <tr>
                <td width="100" align="left">手机号:</td>
                <td><input type="text" name="phone" id="edit-phone" class="wu-text easyui-numberbox easyui-validatebox" data-options="required:true, min:1,precision:0, missingMessage:'请填写11位的手机号！'"/></td>
            </tr>
            <tr>
                <td width="100" align="left">性别:</td>
                <td>
                    <select name="sex" class="easyui-combobox" id="edit-sex" editable="false" panelHeight="auto" style="width:268px">
                        <option value="1">男</option>
                        <option value="2">女</option>
                        <option value="3">未知</option>
                    </select>
                </td>
            </tr>

        </table>
    </form>
</div>

<!-- 修改状态窗口 -->
<div id="edit-state-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:450px; padding:10px;">
    <form id="edit-state-form">
        <input type="hidden" name="id" id="edit-state-id">
        <table>
            <tr>
                <td width="100" align="left">用户状态:</td>
                <td>
                    <select name="state" class="easyui-combobox" id="edit-state" editable="false" panelHeight="auto" style="width:268px">
                        <option value="1">正常</option>
                        <option value="2">冻结</option>
                        <option value="3">申请成为中介</option>
                    </select>
                </td>
            </tr>
        </table>
    </form>
</div>


<!-- 查看申请窗口 -->
<div id="apply-view-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:450px; padding:10px;">
    <table border="1" style="width: 420px;">
        <tr>
            <td width="100" align="left">本人头像：</td>
            <td valign="middle">
                <img id="apply-headPic" style="float:left;" src="/common/photo/view?filename=common/tx.jpg" width="100px" height="90px">
            </td>
        </tr>
        <tr>
            <td width="100" align="left">真实姓名：</td>
            <td id="apply-realName"></td>
        </tr>
        <tr>
            <td width="100" align="left">年龄：</td>
            <td id="apply-age"></td>
        </tr>
        <tr>
            <td width="100" align="left">身份证号：</td>
            <td id="apply-id-card"></td>
        </tr>
        <tr>
            <td width="100" align="left">QQ：</td>
            <td id="apply-qq"></td>
        </tr>
    </table>
</div>

<!-- 修改用户角色窗口 -->
<div id="edit-role-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:450px; padding:10px;">
    <form id="edit-role-form">
        <input type="hidden" name="id" id="edit-role-id">
        <table>
            <tr>
                <td width="100" align="left">用户角色:</td>
                <td>
                    <select name="roleId" class="easyui-combobox" id="edit-roleId" editable="false" panelHeight="auto" style="width:268px">
                        <option value="1">普通用户</option>
                        <option value="2">房产中介</option>
                        <option value="3">管理员</option>
                    </select>
                </td>
            </tr>
        </table>
    </form>
</div>

<input type="file" id="photo-file" style="display:none;" onchange="upload()">
<#include "../common/footer.ftl"/>
<div id="process-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-upload',title:'正在上传图片'" style="width:450px; padding:10px;">
    <div id="p" class="easyui-progressbar" style="width:400px;" data-options="text:'正在上传中...'"></div>
</div>
<!-- End of easyui-dialog -->
<script type="text/javascript">


    /**
     *  添加记录
     */
    function add(){
        var validate = $("#add-form").form("validate");
        if(!validate){
            $.messager.alert("消息提醒","请检查你必填项的数据是否输入!","warning");
            return;
        }
        //对电子邮箱格式的验证
        var email = document.getElementById("email");
        var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
        if(!myreg.test(email.value))
        {
            $.messager.alert("消息提醒","电子邮箱格式不正确!","warning");
            return;
        }
        var data = $("#add-form").serialize();
        $.ajax({
            url:'add',
            dataType:'json',
            type:'post',
            data:data,
            success:function(result){
                if(result.code == 0){
                    $.messager.alert('信息提示',result.msg,'info');
                    //关闭会话框、刷新
                    $('#add-dialog').dialog('close');
                    $('#data-datagrid').datagrid('reload');
                }else{
                    $.messager.alert('信息提示',result.msg,'warning');
                }
            },
            error:function () {
                $.messager.alert('信息提示','网络错误，添加用户信息失败！','error');
            }
        });
    }


    //上传图片进度条
    function start(){
        var value = $('#p').progressbar('getValue');
        if (value < 100){
            value += Math.floor(Math.random() * 10);
            $('#p').progressbar('setValue', value);
        }else{
            $('#p').progressbar('setValue',0)
        }
    };


    function upload(){
        if($("#photo-file").val() == '')return;
        var formData = new FormData();
        formData.append('photo',document.getElementById('photo-file').files[0]);
        $("#process-dialog").dialog('open');
        var interval = setInterval(start,200);
        $.ajax({
            url:'../../common/upload/upload_photo',
            type:'post',
            data:formData,
            contentType:false,
            processData:false,
            success:function(result){
                clearInterval(interval);
                $("#process-dialog").dialog('close');
                if(result.code == 0){
                    $("#preview-photo").attr('src','../../common/photo/view?filename='+result.data);
                    $("#photo").val(result.data);
                    $("#edit-preview-photo").attr('src','../../common/photo/view?filename='+result.data);
                    $("#edit-photo").val(result.data);
                    $.messager.alert("消息提醒",result.msg,"info");
                }else{
                    $.messager.alert("消息提醒",result.msg,"warning");
                }
            },
            error:function(){
                clearInterval(interval);
                $("#process-dialog").dialog('close');
                $.messager.alert("消息提醒","网络错误，上传失败!","error");
            }
        });
    }

    function uploadPhoto(){
        $("#photo-file").click();
    }

    /**
     * 打开修改状态窗口
     */
    function openEditState() {
        var item = $('#data-datagrid').datagrid('getSelections');
        if(item == null || item.length != 1){
            $.messager.alert('信息提示','请选择一条你要改变用户状态的数据！','info');
            return;
        }
        item = item[0];
        $('#edit-state-dialog').dialog({
            closed: false,
            modal:true,
            title: "修改用户状态信息",
            buttons: [{
                text: '确定',
                iconCls: 'icon-ok',
                handler: editState
            }, {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#edit-state-dialog').dialog('close');
                }
            }],
            onBeforeOpen:function(){
                $("#edit-state-id").val(item.id);
                $("#edit-roleId").combobox('setValue',item.state);
            }
        });
    }


    /**
     * 打开查看中介申请信息的窗口
     */
    function openApplyView() {
        var item = $('#data-datagrid').datagrid('getSelections');
        if(item == null || item.length != 1){
            $.messager.alert('信息提示','请选择一条你要查看申请的数据！','info');
            return;
        }
        item = item[0];
        if(item.state != 3){
            $.messager.alert('信息提示','该用户没有申请信息！','info');
            return;
        }
        $('#apply-view-dialog').dialog({
            closed: false,
            modal:true,
            title: "查看用户申请信息",
            buttons: [{
                text: '确定',
                iconCls: 'icon-ok',
                handler: function () {
                    $('#apply-view-dialog').dialog('close');
                }
            }],
            onBeforeOpen:function(){
                $.ajax({
                    url:'get_apply_info',
                    dataType:'json',
                    type:'post',
                    data:{userId:item.id},
                    success:function(result){
                        if(result.code == 0){
                            var applyAgent = result.data;
                            $("#apply-headPic").attr('src','../../common/photo/view?filename='+applyAgent.headPic);
                            $("#apply-realName").text(applyAgent.realName);
                            $("#apply-age").text(applyAgent.age);
                            $("#apply-id-card").text(applyAgent.idCard);
                            $("#apply-qq").text(applyAgent.qq);
                        }else{
                            $.messager.alert('信息提示',result.msg,'warning');
                        }
                    },
                    error:function () {
                        $.messager.alert('信息提示','网络错误，查看申请信息失败！','error');
                    }
                });
            }
        });
    }

    /**
     * 打开修改用户角色窗口
     */
    function openEditRole() {
        var item = $('#data-datagrid').datagrid('getSelections');
        if(item == null || item.length != 1){
            $.messager.alert('信息提示','请选择一条你要改变用户角色的数据！','info');
            return;
        }
        item = item[0];
        $('#edit-role-dialog').dialog({
            closed: false,
            modal:true,
            title: "修改用户角色信息",
            buttons: [{
                text: '确定',
                iconCls: 'icon-ok',
                handler: editRole
            }, {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#edit-role-dialog').dialog('close');
                }
            }],
            onBeforeOpen:function(){
                $("#edit-role-id").val(item.id);
                $("#edit-roleId").combobox('setValue',item.roleId);
            }
        });
    }

    /**
     * 修改用户状态操作处理
     */
    function editState() {
        var validate = $("#edit-state-form").form("validate");
        if(!validate){
            $.messager.alert("消息提醒","请检查你必填项的数据是否输入!","warning");
            return;
        }
        var data = $("#edit-state-form").serialize();
        $.ajax({
            url:'edit_state',
            dataType:'json',
            type:'post',
            data:data,
            success:function(result){
                $("#data-datagrid").datagrid('clearSelections');
                if(result.code == 0){
                    $.messager.alert('信息提示', result.msg, 'info');
                    //会话框关闭、刷新
                    $('#edit-state-dialog').dialog('close');
                    $('#data-datagrid').datagrid('reload');
                }else{
                    $.messager.alert('信息提示',result.msg,'warning');
                }
            },
            error:function () {
                $("#data-datagrid").datagrid('clearSelections');
                $.messager.alert('信息提示','网络错误，改变用户状态信息失败！','error');
            }
        });
    }

    /**
     * 修改用户角色操作处理
     */
    function editRole() {
        var validate = $("#edit-role-form").form("validate");
        if(!validate){
            $.messager.alert("消息提醒","请检查你必填项的数据是否输入!","warning");
            return;
        }
        var data = $("#edit-role-form").serialize();
        $.ajax({
            url:'edit_role',
            dataType:'json',
            type:'post',
            data:data,
            success:function(result){
                $("#data-datagrid").datagrid('clearSelections');
                if(result.code == 0){
                    $.messager.alert('信息提示', result.msg, 'info');
                    //会话框关闭、刷新
                    $('#edit-role-dialog').dialog('close');
                    $('#data-datagrid').datagrid('reload');
                }else{
                    $.messager.alert('信息提示',result.msg,'warning');
                }
            },
            error:function () {
                $("#data-datagrid").datagrid('clearSelections');
                $.messager.alert('信息提示','网络错误，改变用户角色信息失败！','error');
            }
        });
    }

    /**
     * 打开添加窗口
     */
    function openAdd(){
        $('#add-dialog').dialog({
            closed: false,
            modal:true,
            title: "添加用户信息",
            buttons: [{
                text: '确定',
                iconCls: 'icon-ok',
                handler: add
            }, {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#add-dialog').dialog('close');
                }
            }],
            onBeforeOpen:function(){
                //$("#add-form input").val('');
            }
        });
    }

    /**
     * 删除记录
     */
    function remove(){
        var item = $('#data-datagrid').datagrid('getSelections');
        if(item == null || item.length == 0){
            $.messager.alert('信息提示','请选择要删除的数据！','info');
            return;
        }
        var ids = '';
        for(var i=0;i<item.length;i++){
            ids += item[i].id + ',';
        }
        ids = ids.substring(0,ids.length-1)
        var arr = ids.split(",");
        var length = arr.length;
        $.messager.confirm('信息提示',"确定要删除这<font color='red'>"+length+"</font>条记录？", function(result){
            if(result){
                $.ajax({
                    url:'delete',
                    dataType:'json',
                    type:'post',
                    data:{ids:ids},
                    success:function(result){
                        $("#data-datagrid").datagrid('clearSelections');
                        if(result.code == 0){
                            $.messager.alert('信息提示', result.msg,'info');
                            $('#data-datagrid').datagrid('reload');
                        }else{
                            $.messager.alert('信息提示',result.msg,'warning');
                        }
                    },
                    error:function () {
                        $("#data-datagrid").datagrid('clearSelections');
                        $.messager.alert('信息提示','网络错误，删除用户信息失败！','error');
                    }
                });
            }
        });
    }

    //搜索按钮监听
    $("#search-btn").click(function(){
        $("#data-datagrid").datagrid('clearSelections');
        var roleId = $("#search-roleId").combobox('getValue');
        var state = $("#search-state").combobox('getValue');
        var option = {username:$("#search-username").val()};
        option.state = state;
        option.roleId = roleId;
        $('#data-datagrid').datagrid('reload',option);
    });

    /**
     * 修改记录
     */
    function edit(){
        var validate = $("#edit-form").form("validate");
        if(!validate){
            $.messager.alert("消息提醒","请检查你必填项的数据是否输入!","warning");
            return;
        }
        //对电子邮箱格式的验证
        var email = document.getElementById("edit-email");
        var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
        if(!myreg.test(email.value))
        {
            $.messager.alert("消息提醒","电子邮箱格式不正确!","warning");
            return;
        }
        var data = $("#edit-form").serialize();
        $.ajax({
            url:'edit',
            dataType:'json',
            type:'post',
            data:data,
            success:function(result){
                $("#data-datagrid").datagrid('clearSelections');
                if(result.code == 0){
                    $.messager.alert('信息提示', result.msg, 'info');
                    //会话框关闭、刷新
                    $('#edit-dialog').dialog('close');
                    $('#data-datagrid').datagrid('reload');
                }else{
                    $.messager.alert('信息提示',result.msg,'warning');
                }
            },
            error:function () {
                $("#data-datagrid").datagrid('clearSelections');
                $.messager.alert('信息提示','网络错误，修改用户信息失败！','error');
            }
        });
    }



    /**
     * 打开修改窗口
     */
    function openEdit(){
        var item = $('#data-datagrid').datagrid('getSelections');
        if(item == null || item.length != 1){
            $.messager.alert('信息提示','请选择一条你要修改的数据！','info');
            return;
        }
        item = item[0];
        $('#edit-dialog').dialog({
            closed: false,
            modal:true,
            title: "修改用户信息",
            buttons: [{
                text: '确定',
                iconCls: 'icon-ok',
                handler: edit
            }, {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#edit-dialog').dialog('close');
                }
            }],
            onBeforeOpen:function(){
                $("#edit-id").val(item.id);
                $("#edit-password").val(item.password);
                $("#edit-photo").val(item.headPic);
                $("#edit-preview-photo").attr("src", "/common/photo/view?filename="+item.headPic);
                $("#edit-username").val(item.username);
                $("#edit-email").val(item.email);
                $("#edit-phone").numberbox('setValue', item.phone);
                $("#edit-sex").combobox('setValue',item.sex);
            }
        });
    }


    /**
     * 载入数据
     */
    $('#data-datagrid').datagrid({
        url:'list',
        rownumbers:true,
        singleSelect:false,
        pageSize:10,
        pagination:true,
        multiSort:true,
        fitColumns:true,
        idField:'id',
        fit:true,
        columns:[[
            { field:'chk',checkbox:true},
            { field:'headPic',title:'用户头像',width:65,align:'center',formatter:function(value,row,index){
                    var img = '<img src="../../common/photo/view?filename='+value+'" width="60px" height="60px"/>';
                    return img;
                }},
            { field:'username',title:'用户昵称',width:150},
            { field:'phone',title:'手机号码',width:150},
            { field:'email',title:'电子邮箱',width:150},
            { field:'sex',title:'性别',width:50,formatter:function(value,row,index){
                    switch(value){
                        case 3:{
                            return '未知';
                        }
                        case 1:{
                            return '男';
                        }
                        case 2:{
                            return '女';
                        }
                    }
                    return "";
                }},
            { field:'roleId',title:'用户角色',width:100,formatter:function(value,row,index){
                    switch(value){
                        case 3:{
                            return '管理员';
                        }
                        case 1:{
                            return '普通用户';
                        }
                        case 2:{
                            return '房产中介';
                        }
                    }
                    return "";
                }},
            { field:'state',title:'状态',width:100,formatter:function(value,row,index){
                    switch(value){
                        case 3:{
                            return '<span style="color:orange;">申请成为中介</span>';
                        }
                        case 1:{
                            return '<span style="color:green;">正常</span>';
                        }
                        case 2:{
                            return '<span style="color:red;">冻结</span>';
                        }
                    }
                    return "";
                }}
        ]]
    });
</script>
