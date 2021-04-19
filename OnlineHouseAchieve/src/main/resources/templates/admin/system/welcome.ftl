<!--后台管理系统欢迎页面-->
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>欢迎页面</title>
</head>
<body>
<div style="padding:20px;overflow:hidden; color:red; " >
    <p style="font-size: 50px; line-height: 60px; height: 60px;"> 欢迎您，
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
    <p style="font-size: 25px; line-height: 30px; height: 30px;">欢迎使用后台管理系统</p>
    <p>开发人员：【杨杨吖】</p>
    <p>开发周期：2021/3/20 --- 2020/4/15（共计26天）</p>

    <hr />
    <h2>系统环境</h2>
    <p>系统环境：Windows</p>
    <p>开发工具：IDEA-2019.3.5</p>
    <p>Java版本：JDK 1.8</p>
    <p>服务器：tomcat 8.5</p>
    <p>数据库：MySQL 5.7   Redis5.0.5</p>
    <p>系统采用技术： SpringBoot+mybaits-plus+EasyUI+jQuery+Ajax+面向接口编程</p>
</div>
</body>
</html>