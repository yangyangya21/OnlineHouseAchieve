<!--后台管理系统首页-->
<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>后台管理系统</title>
    <#include "../common/head_url.ftl"/>
</head>
<style type="text/css"> .dialog-button { padding: 5px; text-align: center; } </style>
<body class="easyui-layout">
<!-- 加载动画 -->
<div id="loading" style="position:absolute;z-index:1000;top:0px;left:0px;width:100%;height:100%;background:#FFFFFF;text-align :center;padding-top:6%;">
    <img src="/admin/images/loading.jpg" width="50%">
</div>
<!-- begin of header -->
<div class="wu-header" data-options="region:'north',border:false,split:true">
    <div class="wu-header-left">
        <h1>后台管理系统</h1>
    </div>
    <div class="wu-header-right">
        <p><strong class="easyui-tooltip" title="0条未读消息"></strong>欢迎您，
        ${username!""}[
            <#if roleId??>
                <#if roleId == "1">
                    普通用户
                <#elseif roleId == "2">
                    房产中介
                <#elseif roleId == "3">
                    管理员
                </#if>
            </#if>
            ]
        </p>
        <p><a href="../../home/system/index">网站首页</a>|<a href="/logout?type=admin">安全退出</a></p>
    </div>
</div>
<!-- end of header -->
<!-- begin of sidebar -->
<div class="wu-sidebar" data-options="region:'west',split:true,border:true,title:'导航菜单'">
    <div class="easyui-accordion" data-options="border:false,fit:true">

        <@security.authorize access="hasAuthority('/admin/user/list')">
            <div title="用户管理" data-options="iconCls:'icon-group-gear'" style="padding:5px;">
                <ul class="easyui-tree wu-side-tree">
                    <li iconCls="icon-page"><a href="javascript:void(0)" data-icon="icon-page" data-link="../user/list" iframe="1">用户列表</a></li>
                </ul>
            </div>
        </@security.authorize>

        <@security.authorize access="hasAuthority('/admin/authority/list')">
            <div title="权限管理" data-options="iconCls:'icon-key'" style="padding:5px;">
                <ul class="easyui-tree wu-side-tree">
                    <li iconCls="icon-page"><a href="javascript:void(0)" data-icon="icon-page" data-link="../authority/list" iframe="1">权限列表</a></li>
                </ul>
            </div>
        </@security.authorize>

        <@security.authorize access="hasAuthority('/admin/house/list')">
            <div title="房屋管理" data-options="iconCls:'icon-key'" style="padding:5px;">
                <ul class="easyui-tree wu-side-tree">
                    <li iconCls="icon-page"><a href="javascript:void(0)" data-icon="icon-page" data-link="../house/list" iframe="1">房屋列表</a></li>
                </ul>
            </div>
        </@security.authorize>

        <@security.authorize access="hasAuthority('/admin/order_time/list')">
            <div title="预约管理" data-options="iconCls:'icon-key'" style="padding:5px;">
                <ul class="easyui-tree wu-side-tree">
                    <li iconCls="icon-page"><a href="javascript:void(0)" data-icon="icon-page" data-link="../order_time/list" iframe="1">预约列表</a></li>
                </ul>
            </div>
        </@security.authorize>


    </div>
</div>
<!-- end of sidebar -->
<!-- begin of main -->
<div class="wu-main" data-options="region:'center'">
    <div id="wu-tabs" class="easyui-tabs" data-options="border:false,fit:true">
        <div title="首页" data-options="href:'welcome',closable:false,iconCls:'icon-tip',cls:'pd3'"></div>
    </div>


</div>
<!-- end of main -->
<!-- begin of footer -->
<div class="wu-footer" data-options="region:'south',border:true,split:true">
    &copy; 2020 【后台管理系统】 All Rights Reserved
</div>
</body>
<!-- end of footer -->
<script type="text/javascript">
    $(function(){
        $('.wu-side-tree a').bind("click",function(){
            var title = $(this).text();
            var url = $(this).attr('data-link');
            var iconCls = $(this).attr('data-icon');
            var iframe = $(this).attr('iframe')==1?true:false;
            addTab(title,url,iconCls,iframe);
        });
    });

    //在加载数据的过程中实现等待动画页面
    var pc;
    //不要放在$(function(){});中
    $.parser.onComplete = function () {
        if (pc) clearTimeout(pc);
        pc = setTimeout(closes, 1000);
    };
    //动画褪去
    function closes() {
        $('#loading').fadeOut('normal', function () {
            $(this).remove();
        });
    }

    /**
     * Name 添加菜单选项
     * Param title 名称
     * Param href 链接
     * Param iconCls 图标样式
     * Param iframe 链接跳转方式（true为iframe，false为href）
     */
    function addTab(title, href, iconCls, iframe){
        var tabPanel = $('#wu-tabs');
        if(!tabPanel.tabs('exists',title)){
            var content = '<iframe scrolling="auto" frameborder="0"  src="'+ href +'" style="width:100%;height:100%;"></iframe>';
            if(iframe){
                tabPanel.tabs('add',{
                    title:title,
                    content:content,
                    iconCls:iconCls,
                    fit:true,
                    cls:'pd3',
                    closable:true
                });
            }
            else{
                tabPanel.tabs('add',{
                    title:title,
                    href:href,
                    iconCls:iconCls,
                    fit:true,
                    cls:'pd3',
                    closable:true
                });
            }
        }
        else
        {
            tabPanel.tabs('select',title);
        }
    }
    /**
     * Name 移除菜单选项
     */
    function removeTab(){
        var tabPanel = $('#wu-tabs');
        var tab = tabPanel.tabs('getSelected');
        if (tab){
            var index = tabPanel.tabs('getTabIndex', tab);
            tabPanel.tabs('close', index);
        }
    }
</script>
</body>
</html>