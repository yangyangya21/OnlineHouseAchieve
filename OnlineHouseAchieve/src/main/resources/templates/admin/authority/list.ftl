<!--后台管理系统权限列表页面-->
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
            <@security.authorize access="hasAuthority('/admin/authority/add')">
                <a href="#" class="easyui-linkbutton" iconCls="icon-add" onclick="openAdd();" plain="true">添加</a>
            </@security.authorize>
            <@security.authorize access="hasAuthority('/admin/authority/edit')">
                <a href="#" class="easyui-linkbutton" iconCls="icon-edit" onclick="openEdit();" plain="true">编辑</a>
            </@security.authorize>
            <@security.authorize access="hasAuthority('/admin/authority/delete')">
                <a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="remove();" plain="true">删除</a>
            </@security.authorize>
        </div>
        <div class="wu-toolbar-search">
            <label>权限名称：</label><input id="search-name" class="wu-text" style="width:100px" />
            <label>角色名称：</label>
            <select id="search-roleId" class="easyui-combobox" panelHeight="auto" editable="false" style="width:120px" >
                <option value="-1">全部</option>
                <option value="1">普通用户</option>
                <option value="2">房产中介</option>
                <option value="3">管理员</option>
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
                <td width="100" align="left">权限名称:</td>
                <td><input type="text" name="name" class="wu-text easyui-validatebox" data-options="required:true, missingMessage:'请填写权限名称！'" /></td>
            </tr>
            <tr>
                <td width="100" align="left">权限描述:</td>
                <td><input type="text" name="description" class="wu-text easyui-validatebox" data-options="required:true, missingMessage:'请填写权限描述！'"/></td>
            </tr>
            <tr>
                <td width="100" align="left">所属角色:</td>
                <td>
                    <select name="roleId" class="easyui-combobox" editable="false" panelHeight="auto" style="width:268px">
                        <option value="1">普通用户</option>
                        <option value="2">房产中介</option>
                        <option value="3">管理员</option>
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
                <td width="100" align="left">权限名称:</td>
                <td><input type="text" id="edit-name" name="name" class="wu-text easyui-validatebox" data-options="required:true, missingMessage:'请填写权限名称！'" /></td>
            </tr>
            <tr>
                <td width="100" align="left">权限描述:</td>
                <td><input type="text" id="edit-description" name="description" class="wu-text easyui-validatebox" data-options="required:true, missingMessage:'请填写权限描述！'"/></td>
            </tr>
            <tr>
                <td width="100" align="left">所属角色:</td>
                <td>
                    <select name="roleId" id="edit-roleId" class="easyui-combobox" editable="false" panelHeight="auto" style="width:268px">
                        <option value="1">普通用户</option>
                        <option value="2">房产中介</option>
                        <option value="3">管理员</option>
                    </select>
                </td>
            </tr>
        </table>
    </form>
</div>
<#include "../common/footer.ftl"/>
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
                $.messager.alert('信息提示','网络错误，添加权限信息失败！','error');
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
            title: "添加权限信息",
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
                        $.messager.alert('信息提示','网络错误，删除权限信息失败！','error');
                    }
                });
            }
        });
    }

    //搜索按钮监听
    $("#search-btn").click(function(){
        $("#data-datagrid").datagrid('clearSelections');
        var roleId = $("#search-roleId").combobox('getValue');
        var option = {name:$("#search-name").val()};
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
                $.messager.alert('信息提示','网络错误，修改权限信息失败！','error');
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
            title: "修改权限信息",
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
                $("#edit-name").val(item.name);
                $("#edit-description").val(item.description);
                $("#edit-roleId").combobox('setValue',item.roleId);
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
        pageSize:20,
        pagination:true,
        multiSort:true,
        fitColumns:true,
        idField:'id',
        fit:true,
        columns:[[
            { field:'chk',checkbox:true},
            { field:'name',title:'权限名称',width:100},
            { field:'description',title:'权限描述',width:200},
            { field:'roleId',title:'所属角色',width:100,formatter:function(value,row,index){
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
        ]]
    });
</script>
