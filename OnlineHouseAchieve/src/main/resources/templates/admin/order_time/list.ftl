<!--后台管理系统预约管理列表页面-->
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
            <@security.authorize access="hasAuthority('/admin/order_time/reply_order_time')">
                <a href="#" class="easyui-linkbutton" iconCls="icon-comment-edit" onclick="openReply();" plain="true">回复预约</a>
            </@security.authorize>
            <@security.authorize access="hasAuthority('/admin/order_time/delete')">
                <a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="remove();" plain="true">删除</a>
            </@security.authorize>
        </div>
        <div class="wu-toolbar-search">
            <label>预约状态：</label>
            <select id="search-state" class="easyui-combobox" panelHeight="auto" editable="false" style="width:120px" >
                <option value="-1">全部</option>
                <option value="1">中介同意</option>
                <option value="2">中介拒绝</option>
                <option value="3">待回复</option>
            </select>
            <a href="#" id="search-btn" class="easyui-linkbutton" iconCls="icon-search">搜索</a>
        </div>
    </div>
    <!-- End of toolbar -->
    <table id="data-datagrid" class="easyui-datagrid" toolbar="#wu-toolbar"></table>
</div>
<!-- Begin of easyui-dialog -->

<!-- 回复窗口 -->
<div id="reply-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:450px; padding:10px;">
    <form id="reply-form">
        <input type="hidden" id="reply-id" name="id" />
        <table border="1" style="width: 420px;">
            <tr>
                <td width="100" align="left">房屋位置:</td>
                <td id="reply-house-location"></td>
            </tr>
            <tr>
                <td width="100" align="left">预约用户:</td>
                <td id="reply-user-username"></td>
            </tr>
            <tr>
                <td width="100" align="left">预约时间:</td>
                <td id="reply-orderTime"></td>
            </tr>
            <tr>
                <td width="100" align="left">中介回复:</td>
                <td><input type="text" class="wu-text easyui-validatebox" id="reply-agent" name="agentReply"></td>
            </tr>
            <tr>
                <td width="100" align="left">状态:</td>
                <td>
                    <select name="state" id="reply-state" class="easyui-combobox" editable="false" panelHeight="auto" style="width:268px">
                        <option value="1">中介同意</option>
                        <option value="2">中介拒绝</option>
                        <option value="3">待回复</option>
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
     * 打开回复预约信息的窗口
     */
    function openReply(){
        var item = $('#data-datagrid').datagrid('getSelections');
        if(item == null || item.length != 1){
            $.messager.alert('信息提示','请选择一条你要回复预约信息的数据！','info');
            return;
        }
        item = item[0];
        $('#reply-dialog').dialog({
            closed: false,
            modal:true,
            title: "回复预约信息",
            buttons: [{
                text: '确定',
                iconCls: 'icon-ok',
                handler: reply
            }, {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#reply-dialog').dialog('close');
                }
            }],
            onBeforeOpen:function(){
                $("#reply-id").val(item.id);
                if(item.house != null){
                    $("#reply-house-location").text(item.house.location);
                }
                if(item.user != null){
                    $("#reply-user-username").text(item.user.username);
                }
                $("#reply-orderTime").text(item.orderTime);
                $("#reply-agent").val(item.agentReply);
                $("#reply-state").combobox('setValue',item.state);
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
                        $.messager.alert('信息提示','网络错误，删除预约信息失败！','error');
                    }
                });
            }
        });
    }

    //搜索按钮监听
    $("#search-btn").click(function(){
        $("#data-datagrid").datagrid('clearSelections');
        var option = {state:$("#search-state").combobox('getValue')};
        $('#data-datagrid').datagrid('reload',option);
    });

    /**
     * 回复预约信息
     */
    function reply(){
        var validate = $("#reply-form").form("validate");
        if(!validate){
            $.messager.alert("消息提醒","请检查你必填项的数据是否输入!","warning");
            return;
        }
        var data = $("#reply-form").serialize();
        $.ajax({
            url:'reply_order_time',
            dataType:'json',
            type:'post',
            data:data,
            success:function(result){
                $("#data-datagrid").datagrid('clearSelections');
                if(result.code == 0){
                    $.messager.alert('信息提示', result.msg, 'info');
                    //会话框关闭、刷新
                    $('#reply-dialog').dialog('close');
                    $('#data-datagrid').datagrid('reload');
                }else{
                    $.messager.alert('信息提示',result.msg,'warning');
                }
            },
            error:function () {
                $("#data-datagrid").datagrid('clearSelections');
                $.messager.alert('信息提示','网络错误，回复预约信息失败！','error');
            }
        });
    }

    //时间格式显示
    function add0(m){return m<10?'0'+m:m }
    function format(shijianchuo){
        //shijianchuo是整数，否则要parseInt转换
        var time = new Date(shijianchuo);
        var y = time.getFullYear();
        var m = time.getMonth()+1;
        var d = time.getDate();
        var h = time.getHours();
        var mm = time.getMinutes();
        var s = time.getSeconds();
        return y+'-'+add0(m)+'-'+add0(d)+' '+add0(h)+':'+add0(mm)+':'+add0(s);
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
            { field:'house.coverPhoto',title:'房屋封面',width:65,align:'center',formatter:function(value,row,index){
                    if(row.house != null){
                        var img = '<img src="../../common/photo/view?filename='+row.house.coverPhoto+'" width="60px" height="60px"/>';
                        return img;
                    }else{
                        return "";
                    }
                }},
            { field:'house.location',title:'房屋位置',width:130, formatter:function(value,row,index) {
                    if(row.house != null){
                        return row.house.location;
                    }else{
                        return "";
                    }
                }},
            { field:'house.category',title:'出售方式',width:50, formatter:function(value,row,index) {
                    if(row.house != null){
                        if(row.house.category == 1){
                            return "租房";
                        }else if(row.house.category == 2){
                            return "购房";
                        }
                        return "";
                    }else{
                        return "";
                    }
                }},
            { field:'orderTime',title:'预约时间',width:100, formatter:function(value,row,index) {
                    return format(value);
                }},
            { field:'agentReply',title:'中介回复',width:150},
            { field:'user.username',title:'预约用户昵称',width:80, formatter:function(value,row,index) {
                    if(row.user != null){
                        return row.user.username;
                    }else{
                        return "";
                    }
                }},
            { field:'state',title:'状态',width:70,formatter:function(value,row,index){
                    switch(value){
                        case 1:{
                            return '<span style="color:green;">中介同意</span>';
                        }
                        case 2:{
                            return '<span style="color:red;">中介拒绝</span>';
                        }
                        case 3:{
                            return '<span style="color:orange;">待回复</span>';
                        }
                    }
                    return "";
                }},
            { field:'createTime',title:'创建时间',width:100, formatter:function(value,row,index) {
                    return format(value);
                }}
        ]]
    });
</script>
