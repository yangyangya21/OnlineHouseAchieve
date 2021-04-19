<!--后台管理系统房屋列表页面-->
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
            <@security.authorize access="hasAuthority('/admin/house/add')">
                <a href="#" class="easyui-linkbutton" iconCls="icon-add" onclick="openAdd();" plain="true">添加</a>
            </@security.authorize>
            <@security.authorize access="hasAuthority('/admin/house/edit')">
                <a href="#" class="easyui-linkbutton" iconCls="icon-edit" onclick="openEdit();" plain="true">编辑</a>
            </@security.authorize>
            <@security.authorize access="hasAuthority('/admin/house/delete')">
                <a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="remove();" plain="true">删除</a>
            </@security.authorize>
            <@security.authorize access="hasAuthority('/admin/house/edit_state_admin')">
                <a href="#" class="easyui-linkbutton" iconCls="icon-user-edit" onclick="openEditStateByAdmin();" plain="true">管理员修改状态</a>
            </@security.authorize>
            <@security.authorize access="hasAuthority('/admin/house/view')">
                <a href="#" class="easyui-linkbutton" iconCls="icon-eye" onclick="openView();" plain="true">查看详情</a>
            </@security.authorize>
            <@security.authorize access="hasAuthority('/admin/house/edit_state_agent')">
                <a href="#" class="easyui-linkbutton" iconCls="icon-user-edit" onclick="openEditStateByAgent();" plain="true">中介修改状态</a>
            </@security.authorize>
        </div>
        <div class="wu-toolbar-search">
            <label>房屋简介：</label><input id="search-info" class="wu-text" style="width:100px" />
            <label>房屋状态：</label>
            <select id="search-state" class="easyui-combobox" panelHeight="auto" editable="false" style="width:120px" >
                <option value="-1">全部</option>
                <option value="1">待审核</option>
                <option value="2">待出租</option>
                <option value="3">已出租</option>
                <option value="4">已下架</option>
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
                <td width="100" align="left">图片预览:</td>
                <td valign="middle">
                    <img id="preview-photo" style="float:left;" src="/common/photo/view?filename=common/no_img.jpg" width="100px" height="90px">
                    <a style="float:left;margin-top:40px;" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-upload" onclick="uploadPhoto()" plain="true">上传图片</a>
                </td>
            </tr>
            <tr>
                <td width="100" align="left">封面图片:</td>
                <td><input type="text" id="photo" name="coverPhoto" value="common/no_img.jpg" readonly="readonly" class="wu-text" /></td>
            </tr>
            <tr>
                <td width="100" align="left">房屋简介:</td>
                <td><input type="text" name="info" class="wu-text easyui-validatebox" data-options="required:true, missingMessage:'请填写房屋简介！'" /></td>
            </tr>
            <tr>
                <td width="100" align="left">出售方式:</td>
                <td>
                    <select name="category" id="category" class="easyui-combobox" editable="false" panelHeight="auto" style="width:268px">
                        <option value="1">租房</option>
                        <option value="2">购房</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td width="100" align="left">金额(元):</td>
                <td><input type="text" name="money" class="wu-text easyui-numberbox easyui-validatebox" data-options="required:true, min:0.00,precision:2,max:99999999.99, missingMessage:'请填写租金！'"/></td>
            </tr>
            <tr>
                <td width="100" align="left">房屋户型:</td>
                <td><input type="text" name="houseType" class="wu-text easyui-validatebox" data-options="required:true, missingMessage:'请填写房屋户型！'" /></td>
            </tr>
            <tr>
                <td width="100" align="left">房屋面积(㎡):</td>
                <td><input type="text" name="area" class="wu-text easyui-numberbox easyui-validatebox" data-options="required:true, min:0.00,precision:2,max:999999.99, missingMessage:'请填写房屋面积！'" /></td>
            </tr>
            <tr>
                <td width="100" align="left">房屋朝向:</td>
                <td><input type="text" name="orientation" class="wu-text easyui-validatebox" data-options="required:true, missingMessage:'请填写房屋朝向！'" /></td>
            </tr>
            <tr>
                <td width="100" align="left">房屋楼层:</td>
                <td><input type="text" name="floor" class="wu-text easyui-validatebox" data-options="required:true, missingMessage:'请填写房屋楼层！'" /></td>
            </tr>
            <tr>
                <td width="100" align="left">房屋装修:</td>
                <td><input type="text" name="renovation" class="wu-text easyui-validatebox" data-options="required:true, missingMessage:'请填写房屋装修！'" /></td>
            </tr>
            <tr>
                <td width="100" align="left">房屋位置:</td>
                <td><input type="text" name="location" class="wu-text easyui-validatebox" data-options="required:true, missingMessage:'请填写房屋位置！'" /></td>
            </tr>
            <tr>
                <td width="100" align="left">房屋详情:</td>
                <td><textarea name="details" style="width:300px;height:100px;" placeholder="请填写房屋详情！" class="wu-text easyui-validatebox" data-options="required:true, missingMessage:'请填写房屋详情！'"></textarea></td>
            </tr>
            <#if roleId == "3">
                <tr>
                    <td width="100" align="left">所属中介:</td>
                    <td>
                        <select name="userId" class="easyui-combobox" editable="false" panelHeight="auto" style="width:268px">
                            <#list userList as user>
                                <option value="${user.id!""}">${user.username!""}</option>
                            </#list>
                        </select>
                    </td>
                </tr>
            </#if>
        </table>
    </form>
</div>

<!-- 修改窗口 -->
<div id="edit-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:450px; padding:10px;">
    <form id="edit-form">
        <input type="hidden" name="id" id="edit-id">
        <table>
            <tr>
                <td width="100" align="left">图片预览:</td>
                <td valign="middle">
                    <img id="edit-preview-photo" style="float:left;" src="/common/photo/view?filename=common/no_img.jpg" width="100px" height="90px">
                    <a style="float:left;margin-top:40px;" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-upload" onclick="uploadPhoto()" plain="true">上传图片</a>
                </td>
            </tr>
            <tr>
                <td width="100" align="left">封面图片:</td>
                <td><input type="text" id="edit-photo" name="coverPhoto" value="common/no_img.jpg" readonly="readonly" class="wu-text" /></td>
            </tr>
            <tr>
                <td width="100" align="left">房屋简介:</td>
                <td><input type="text" name="info" id="edit-info" class="wu-text easyui-validatebox" data-options="required:true, missingMessage:'请填写房屋简介！'" /></td>
            </tr>
            <tr>
                <td width="100" align="left">出售方式:</td>
                <td>
                    <select name="category" id="edit-category" class="easyui-combobox" editable="false" panelHeight="auto" style="width:268px">
                        <option value="1">租房</option>
                        <option value="2">购房</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td width="100" align="left">金额(元):</td>
                <td><input type="text" name="money" id="edit-money" class="wu-text easyui-numberbox easyui-validatebox" data-options="required:true, min:0.00,precision:2,max:99999999.99, missingMessage:'请填写租金！'"/></td>
            </tr>
            <tr>
                <td width="100" align="left">房屋户型:</td>
                <td><input type="text" name="houseType" id="edit-houseType" class="wu-text easyui-validatebox" data-options="required:true, missingMessage:'请填写房屋户型！'" /></td>
            </tr>
            <tr>
                <td width="100" align="left">房屋面积(㎡):</td>
                <td><input type="text" name="area" id="edit-area" class="wu-text easyui-numberbox easyui-validatebox" data-options="required:true, min:0.00,precision:2,max:999999.99, missingMessage:'请填写房屋面积！'" /></td>
            </tr>
            <tr>
                <td width="100" align="left">房屋朝向:</td>
                <td><input type="text" name="orientation" id="edit-orientation" class="wu-text easyui-validatebox" data-options="required:true, missingMessage:'请填写房屋朝向！'" /></td>
            </tr>
            <tr>
                <td width="100" align="left">房屋楼层:</td>
                <td><input type="text" name="floor" id="edit-floor" class="wu-text easyui-validatebox" data-options="required:true, missingMessage:'请填写房屋楼层！'" /></td>
            </tr>
            <tr>
                <td width="100" align="left">房屋装修:</td>
                <td><input type="text" name="renovation" id="edit-renovation" class="wu-text easyui-validatebox" data-options="required:true, missingMessage:'请填写房屋装修！'" /></td>
            </tr>
            <tr>
                <td width="100" align="left">房屋位置:</td>
                <td><input type="text" name="location" id="edit-location" class="wu-text easyui-validatebox" data-options="required:true, missingMessage:'请填写房屋位置！'" /></td>
            </tr>
            <tr>
                <td width="100" align="left">房屋详情:</td>
                <td><textarea name="details" id="edit-details" style="width:300px;height:100px;" placeholder="请填写房屋详情！" class="wu-text easyui-validatebox" data-options="required:true, missingMessage:'请填写房屋详情！'"></textarea></td>
            </tr>
            <#if roleId == "3">
                <tr>
                    <td width="100" align="left">所属中介:</td>
                    <td>
                        <select name="userId" id="edit-userId" class="easyui-combobox" editable="false" panelHeight="auto" style="width:268px">
                            <#list userList as user>
                                <option value="${user.id!""}">${user.username!""}</option>
                            </#list>
                        </select>
                    </td>
                </tr>
            </#if>
        </table>
    </form>
</div>
<!-- 管理员修改状态窗口 -->
<div id="edit-state-dialog-admin" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:450px; padding:10px;">
    <form id="edit-state-form-admin">
        <input type="hidden" name="id" id="edit-state-id-admin">
        <table>
            <tr>
                <td width="100" align="left">所属中介:</td>
                <td>
                    <select name="state" id="edit-state-admin" class="easyui-combobox" editable="false" panelHeight="auto" style="width:268px">
                        <option value="1">待审核</option>
                        <option value="2">待出租</option>
                        <option value="3">已出租</option>
                        <option value="4">已下架</option>
                    </select>
                </td>
            </tr>
        </table>
    </form>
</div>
<!-- 中介修改状态窗口 -->
<div id="edit-state-dialog-agent" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:450px; padding:10px;">
    <form id="edit-state-form-agent">
        <input type="hidden" name="id" id="edit-state-id-agent">
        <table>
            <tr>
                <td width="100" align="left">所属中介:</td>
                <td>
                    <select name="state" id="edit-state-agent" class="easyui-combobox" editable="false" panelHeight="auto" style="width:268px">
                        <option value="2">待出租</option>
                        <option value="3">已出租</option>
                        <option value="4">已下架</option>
                    </select>
                </td>
            </tr>
        </table>
    </form>
</div>
<!-- 查看房屋详情窗口 -->
<div id="view-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:650px;height: 500px; padding:10px;">
    <table border="1">
        <tr>
            <td width="100" align="left">图片预览:</td>
            <td valign="middle">
                <img id="view-photo" style="float:left;" src="/common/photo/view?filename=common/no_img.jpg" width="100px" height="90px">
            </td>
        </tr>
        <tr>
            <td width="100" align="left">房屋简介:</td>
            <td><span id="view-info"></span></td>
        </tr>
        <tr>
            <td width="100" align="left">出售方式:</td>
            <td><span id="view-category"></span></td>
        </tr>
        <tr>
            <td width="100" align="left">租金(元/月):</td>
            <td><span id="view-money"></span></td>
        </tr>
        <tr>
            <td width="100" align="left">房屋户型:</td>
            <td><span id="view-houseType"></span></td>
        </tr>
        <tr>
            <td width="100" align="left">房屋面积(㎡):</td>
            <td><span id="view-area"></span></td>
        </tr>
        <tr>
            <td width="100" align="left">房屋朝向:</td>
            <td><span id="view-orientation"></span></td>
        </tr>
        <tr>
            <td width="100" align="left">房屋楼层:</td>
            <td><span id="view-floor"></span></td>
        </tr>
        <tr>
            <td width="100" align="left">房屋装修:</td>
            <td><span id="view-renovation"></span></td>
        </tr>
        <tr>
            <td width="100" align="left">房屋位置:</td>
            <td><span id="view-location"></span></td>
        </tr>
        <tr>
            <td width="100" align="left">房屋详情:</td>
            <td><span id="view-details"></span></td>
        </tr>
        <tr>
            <td width="100" align="left">所属中介:</td>
            <td><span id="view-username"></span></td>
        </tr>
        <tr>
            <td width="100" align="left">房屋状态:</td>
            <td><span id="view-state"></span></td>
        </tr>
        <tr>
            <td width="100" align="left">创建时间:</td>
            <td><span id="view-createTime"></span></td>
        </tr>
        <tr>
            <td width="100" align="left">更新时间:</td>
            <td><span id="view-updateTime"></span></td>
        </tr>
    </table>
</div>
<input type="file" id="photo-file" style="display:none;" onchange="upload()">
<#include "../common/footer.ftl"/>
<div id="process-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-upload',title:'正在上传图片'" style="width:450px; padding:10px;">
    <div id="p" class="easyui-progressbar" style="width:400px;" data-options="text:'正在上传中...'"></div>
</div>
<!-- End of easyui-dialog -->
<script type="text/javascript">


    //上传图片进度条
    function start(){
        var value = $('#p').progressbar('getValue');
        if (value < 100){
            value += Math.floor(Math.random() * 10);
            $('#p').progressbar('setValue', value);
        }else{
            $('#p').progressbar('setValue',0)
        }
    }


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
                $.messager.alert('信息提示','网络错误，添加房屋信息失败！','error');
            }
        });
    }

    /**
     * 打开查看详情窗口
     */
    function openView(){
        var item = $('#data-datagrid').datagrid('getSelections');
        if(item == null || item.length != 1){
            $.messager.alert('信息提示','请选择一条你要查看详情的数据！','info');
            return;
        }
        item = item[0];
        $('#view-dialog').dialog({
            closed: false,
            modal:true,
            title: "查看房屋详情信息",
            buttons: [{
                text: '确定',
                iconCls: 'icon-ok',
                handler: function () {
                    $('#view-dialog').dialog('close');
                }
            }],
            onBeforeOpen:function(){
                $("#view-photo").attr('src','/common/photo/view?filename=' + item.coverPhoto);
                document.getElementById("view-createTime").innerText = format(item.createTime);
                document.getElementById("view-updateTime").innerText = format(item.updateTime);
                if(item.state == "1"){
                    document.getElementById("view-state").innerText = "待审核";
                }else if(item.state == "2"){
                    document.getElementById("view-state").innerText = "待出租";
                }else if(item.state == "3"){
                    document.getElementById("view-state").innerText = "已出租";
                }else if(item.state == "4"){
                    document.getElementById("view-state").innerText = "已下架";
                }
                document.getElementById("view-info").innerText = item.info;
                document.getElementById("view-money").innerText = item.money;
                document.getElementById("view-houseType").innerText = item.houseType;
                document.getElementById("view-area").innerText = item.area;
                document.getElementById("view-orientation").innerText = item.orientation;
                document.getElementById("view-floor").innerText = item.floor;
                document.getElementById("view-renovation").innerText = item.renovation;
                document.getElementById("view-details").innerText = item.details;
                document.getElementById("view-location").innerText = item.location;
                if(item.category == 1){
                    document.getElementById("view-category").innerText = "租房";
                }else if(item.category == 2){
                    document.getElementById("view-category").innerText = "购房";
                }
                if(item.user != null){
                    document.getElementById("view-username").innerText = item.user.username;
                }
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
            title: "添加房屋信息",
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
                        $.messager.alert('信息提示','网络错误，删除房屋信息失败！','error');
                    }
                });
            }
        });
    }

    //搜索按钮监听
    $("#search-btn").click(function(){
        $("#data-datagrid").datagrid('clearSelections');
        var state = $("#search-state").combobox('getValue');
        var option = {info:$("#search-info").val()};
        option.state = state;
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
                $.messager.alert('信息提示','网络错误，修改房屋信息失败！','error');
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
            title: "修改房屋信息",
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
                $("#edit-info").val(item.info);
                $("#edit-floor").val(item.floor);
                $("#edit-money").numberbox('setValue', item.money);
                $("#edit-houseType").val(item.houseType);
                $("#edit-area").numberbox('setValue', item.area);
                $("#edit-orientation").val(item.orientation);
                $("#edit-renovation").val(item.renovation);
                $("#edit-location").val(item.location);
                $("#edit-details").val(item.details);
                $("#edit-photo").val(item.coverPhoto);
                $("#edit-preview-photo").attr("src", "/common/photo/view?filename="+item.coverPhoto);
                $("#edit-userId").combobox('setValue',item.userId);
                $("#edit-category").combobox('setValue',item.category);
            }
        });
    }


    /**
     * 管理员修改状态
     */
    function editStateByAdmin(){
        var validate = $("#edit-state-form-admin").form("validate");
        if(!validate){
            $.messager.alert("消息提醒","请检查你必填项的数据是否输入!","warning");
            return;
        }
        var data = $("#edit-state-form-admin").serialize();
        $.ajax({
            url:'edit_state_admin',
            dataType:'json',
            type:'post',
            data:data,
            success:function(result){
                $("#data-datagrid").datagrid('clearSelections');
                if(result.code == 0){
                    $.messager.alert('信息提示', result.msg, 'info');
                    //会话框关闭、刷新
                    $('#edit-state-dialog-admin').dialog('close');
                    $('#data-datagrid').datagrid('reload');
                }else{
                    $.messager.alert('信息提示',result.msg,'warning');
                }
            },
            error:function () {
                $("#data-datagrid").datagrid('clearSelections');
                $.messager.alert('信息提示','网络错误，修改房屋状态信息失败！','error');
            }
        });
    }


    /**
     * 中介修改状态
     */
    function editStateByAgent(){
        var validate = $("#edit-state-form-agent").form("validate");
        if(!validate){
            $.messager.alert("消息提醒","请检查你必填项的数据是否输入!","warning");
            return;
        }
        var data = $("#edit-state-form-agent").serialize();
        $.ajax({
            url:'edit_state_agent',
            dataType:'json',
            type:'post',
            data:data,
            success:function(result){
                $("#data-datagrid").datagrid('clearSelections');
                if(result.code == 0){
                    $.messager.alert('信息提示', result.msg, 'info');
                    //会话框关闭、刷新
                    $('#edit-state-dialog-agent').dialog('close');
                    $('#data-datagrid').datagrid('reload');
                }else{
                    $.messager.alert('信息提示',result.msg,'warning');
                }
            },
            error:function () {
                $("#data-datagrid").datagrid('clearSelections');
                $.messager.alert('信息提示','网络错误，修改房屋状态信息失败！','error');
            }
        });
    }


    /**
     * 打开管理员修改状态窗口
     */
    function openEditStateByAdmin(){
        var item = $('#data-datagrid').datagrid('getSelections');
        if(item == null || item.length != 1){
            $.messager.alert('信息提示','请选择一条你要修改状态的数据！','info');
            return;
        }
        item = item[0];
        $('#edit-state-dialog-admin').dialog({
            closed: false,
            modal:true,
            title: "管理员修改房屋状态信息",
            buttons: [{
                text: '确定',
                iconCls: 'icon-ok',
                handler: editStateByAdmin
            }, {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#edit-state-dialog-admin').dialog('close');
                }
            }],
            onBeforeOpen:function(){
                $("#edit-state-id-admin").val(item.id);
                $("#edit-state-admin").combobox('setValue',item.state);
            }
        });
    }

    /**
     * 打开中介修改状态窗口
     */
    function openEditStateByAgent(){
        var item = $('#data-datagrid').datagrid('getSelections');
        if(item == null || item.length != 1){
            $.messager.alert('信息提示','请选择一条你要修改状态的数据！','info');
            return;
        }
        item = item[0];
        if(item.state == 1){
            $.messager.alert('信息提示','房屋状态正在审核中，请等管理员审核后再试！','warning');
            return;
        }
        $('#edit-state-dialog-agent').dialog({
            closed: false,
            modal:true,
            title: "中介修改房屋状态信息",
            buttons: [{
                text: '确定',
                iconCls: 'icon-ok',
                handler: editStateByAgent
            }, {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#edit-state-dialog-agent').dialog('close');
                }
            }],
            onBeforeOpen:function(){
                $("#edit-state-id-agent").val(item.id);
                $("#edit-state-agent").combobox('setValue',item.state);
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
            { field:'coverPhoto',title:'封面图片',width:65,align:'center',formatter:function(value,row,index){
                    var img = '<img src="../../common/photo/view?filename='+value+'" width="60px" height="60px"/>';
                    return img;
                }},
            { field:'info',title:'房屋简介',width:100},
            { field:'money',title:'金额(元)',width:50},
            { field:'area',title:'房屋面积(㎡)',width:50},
            { field:'location',title:'房屋位置',width:100},
            { field:'houseType',title:'房屋户型',width:70},
            { field:'user.username',title:'所属中介',width:70,formatter:function(value,row,index) {
                    if(row.user == null){
                        return "";
                    }else{
                        return row.user.username;
                    }
                }},
            { field:'state',title:'房屋状态',width:50,formatter:function(value,row,index){
                    switch(value){
                        case 1:{
                            return '<span style="color:orange;">待审核</span>';
                        }
                        case 2:{
                            return '待出租';
                        }
                        case 3:{
                            return '已出租';
                        }
                        case 4:{
                            return '已下架';
                        }
                    }
                    return "";
                }},
            { field:'createTime',title:'创建时间',width:100,formatter:function(value,row,index) {
                    return format(value);
                }},
            { field:'updateTime',title:'更新时间',width:100,formatter:function(value,row,index) {
                    return format(value);
                }}
        ]]
    });
</script>
