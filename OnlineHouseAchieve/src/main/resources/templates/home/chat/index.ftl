<!--前台和中介聊天页面ftl-->
<!DOCTYPE html>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>在线聊天室</title>
    <link rel="stylesheet" type="text/css" href="/home/chat/css/index.css">

</head>

<body>

<div class="qqBox">
    <!-- xx -->
    <div class="BoxHead">
        <!-- <div class="headImg">
        <img src="./index_files/6.jpg">
    </div> -->
        <div class="internetName"></div>
        <div class="tubiao">
            <a href="javascript:void(0);" onclick="logoutChat();"><img style="width: 20px; margin: 15px 5px;" src="/home/chat/iocn/iocnqx.png" alt=""></a>
        </div>
    </div>
    <!-- 好友聊天内容 -->
    <div class="context">

        <div class="conLeft">
            <div class="shouBox">
                <div class="shous">
                    <div style="height: 60px;">
                        <img class="touimg" src="/home/chat/img/touxiang3.png" alt="">
                    </div>
                    <div class="txiangName" id="txiangName"></div>
                    <div style="height: 52px;">

                    </div>
                </div>


                <!-- 搜索 -->

                <form style="height: 130px;" onSubmit="submitFn(this, event);">
                    <div class="search-wrapper">
                        <div class="input-holder">
                        </div>
                        <div class="result-container">

                        </div>
                    </div>
                </form>

                <!-- 菜单分组 -->
                <div class="aui-flexView">

                    <div class="aui-scrollView">
                        <div class="aui-tab-box" data-ydui-tab>
                            <div class="tab-nav">

                                <div class="tab-nav-item tab-active">
                                    <a href="javascript:void(0);" style="width:70px;">消息记录</a>
                                </div>
                            </div>

                            <div class="tab-panel">

                                <!--消息-->
                                <div class="tab-panel-item tab-active">
                                    <div class="tab-item">

                                        <a href="javascript:;" class="aui-list-item">
                                            <ul id="onlineUser">


                                            </ul>

                                        </a>

                                    </div>
                                </div>


                            </div>

                        </div>
                    </div>
                </div>


            </div>

        </div>
        <div class="conRight">
            <div class="Righthead">
            </div>
            <!-- 显示聊天内容 -->
            <div class="RightCont">
                <ul class="newsList" id="newsList" style="display:none">


                </ul>
            </div>
            <div class="RightFoot">
                <div class="emjon" style="display: none;">
                    <ul>
                        <li><img src="/home/chat/index_files/em_02.jpg"></li>
                        <li><img src="/home/chat/index_files/em_05.jpg"></li>
                        <li><img src="/home/chat/index_files/em_07.jpg"></li>
                        <li><img src="/home/chat/index_files/em_12.jpg"></li>
                        <li><img src="/home/chat/index_files/em_14.jpg"></li>
                        <li><img src="/home/chat/index_files/em_16.jpg"></li>
                        <li><img src="/home/chat/index_files/em_20.jpg"></li>
                        <li><img src="/home/chat/index_files/em_23.jpg"></li>
                        <li><img src="/home/chat/index_files/em_25.jpg"></li>
                        <li><img src="/home/chat/index_files/em_30.jpg"></li>
                        <li><img src="/home/chat/index_files/em_31.jpg"></li>
                        <li><img src="/home/chat/index_files/em_33.jpg"></li>
                        <li><img src="/home/chat/index_files/em_37.jpg"></li>
                        <li><img src="/home/chat/index_files/em_38.jpg"></li>
                        <li><img src="/home/chat/index_files/em_40.jpg"></li>
                        <li><img src="/home/chat/index_files/em_45.jpg"></li>
                        <li><img src="/home/chat/index_files/em_47.jpg"></li>
                        <li><img src="/home/chat/index_files/em_48.jpg"></li>
                        <li><img src="/home/chat/index_files/em_52.jpg"></li>
                        <li><img src="/home/chat/index_files/em_54.jpg"></li>
                        <li><img src="/home/chat/index_files/em_55.jpg"></li>
                    </ul>
                </div>
                <div class="footTop" id="footTop" style="display:none">
                    <ul>
                        <li class="ExP"><img src="/home/chat/index_files/20170926103645_33.jpg"></li>
                    </ul>
                </div>
                <div class="inputBox" id="inputBox" style="display:none">
                            <textarea id="dope" style="width: 99%;height: 75px; border: none;outline: none; resize:none;"
                                      name="" rows="" cols="" placeholder="在此输入文字内容..."></textarea>
                    <button class="sendBtn">发送</button>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="/home/chat/js/jquery.min.js"></script>
<script type="text/javascript" src="/common/layui/layui.all.js" charset="utf-8"></script>
<script type="text/javascript" src="/home/chat/js/index.js"></script>
</body>

<script type="text/javascript">

    var username;
    var toName;
    var id;
    /**
     * 获取用户名称
     */
    $(function(){

        $.ajax({
            url:"/home/chat/get_user",
            async:false,
            type:'post',
            dateType:'json',
            success:function(res){
                if(res.code == 0){
                    username = res.data.username;
                    id = res.data.id;
                    document.getElementById("txiangName").innerText = username;
                }else{
                    layer.alert(res.msg, {icon: 5}, function(){
                        window.location.href = "/home/user/login";
                    });
                }
            }
        });



        //创建WebSocket对象
        var ws = new WebSocket("ws://localhost:8080/home/chat?userId="+id);
        //var ws = new WebSocket("ws://8h7vyv.natappfree.cc/home/chat?userId="+id);
        //给ws绑定事件
        ws.onopen = function(){
            //建立连接后要做的事情

        };

        //接收到服务端推送的消息后触发事件
        ws.onmessage = function(evt){
            //获取服务端推送过来的消息 dataStr是服务端传来的字符串
            var dataStr = evt.data;
            //将dataStr转换为json对象
            var res = JSON.parse(dataStr);
            console.log(res);
            //判断是否是系统消息
            if(res.system){
                //系统消息
                var names = res.message;
                console.log(names);
                //上线用户显示
                $("#onlineUser").children().filter('li').remove();
                for(var name of names){
                    if(name != username){
                        var html = '<li class="onlineUser"><div class="liLeft"><img src="/home/chat/index_files/20170926103645_19.jpg"></div>'+
                            '<div class="liRight"><span class="intername">'+name+'</span><span class="infor" id="info-'+name+'"></span></div></li>';
                        $("#onlineUser").append(html);
                    }
                }
            }else{
                //不是系统消息

                //将服务端推送的消息进行展示
                var html = '<li><div class="answerHead"><img src="/home/chat/index_files/20170926103645_19.jpg"/></div>'+
                    '<div class="answers"><img class="jiao" src="/home/chat/img/zuo.jpg">'+res.message+'</div></li>';

                //如果发送对象和消息来自对象一样才进行消息展示，否则只对消息进行存储
                if(toName == res.fromName){
                    $('.newsList').append(html);
                    $('.RightCont').scrollTop($('.RightCont')[0].scrollHeight );
                }

                //接收到的消息存储到sessionStorage中
                var chatData = sessionStorage.getItem(res.fromName);
                if(chatData != null){
                    html = chatData + html;
                }

                //新消息提示
                $("#info-"+res.fromName).html('收到新消息...');

                sessionStorage.setItem(res.fromName, html);
            }
        };

        ws.onclose = function(){

        };

        //聊天页面切换
        $(document).on("click",".onlineUser",function(){
            $(this).addClass('bg').siblings().removeClass('bg');
            var intername=$(this).children('.liRight').children('.intername').text();
            toName = intername;
            $('.internetName').text(intername);
            $('.newsList').html('');
            var ft = document.getElementById("footTop");
            ft.style.display = "block";
            var ib = document.getElementById("inputBox");
            ib.style.display = "block";
            var nl = document.getElementById("newsList");
            nl.style.display = "block";
            //sessionStorage 存储聊天记录
            var chatData = sessionStorage.getItem(toName);
            if(chatData != null){
                //将聊天记录渲染到聊天区
                $('.newsList').html(chatData);
            }
            //新消息消失
            $("#info-"+toName).html('');
        });

        //发送消息
        $('.sendBtn').on('click',function() {
            var news = $('#dope').val();
            if (news == '') {
                alert('消息不能为空！');
            } else {

                $('#dope').val('');

                var json = {"toName": toName, "message": news};

                //发送数据到服务端
                ws.send(JSON.stringify(json));

                //发送者消息展示在聊天区右边
                var html = '<li><div class="nesHead"><img src="/home/chat/img/touxiang3.png"/></div>'+
                    '<div class="news"><img class="jiao" src="/home/chat/img/you.jpg">'+news+'</div></li>';
                $('.newsList').append(html);
                $('.RightCont').scrollTop($('.RightCont')[0].scrollHeight );

                //发送的消息存储到sessionStorage中
                var chatData = sessionStorage.getItem(toName);
                if(chatData != null){
                    html = chatData + html;
                }

                sessionStorage.setItem(toName, html);

            }
        });

        //发送表情
        $('.emjon li').on('click',function(){
            var imgSrc=$(this).children('img').attr('src');
            var news = '<img class="Expr" src="'+imgSrc+'">';

            var json = {"toName": toName, "message": news};

            //发送数据到服务端
            ws.send(JSON.stringify(json));

            var str = '<li>'+
                '<div class="nesHead"><img src="/home/chat/img/touxiang3.png"/></div>'+
                '<div class="news"><img class="jiao" src="/home/chat/img/you.jpg"><img class="Expr" src="'+imgSrc+'"></div>'+
                '</li>';
            $('.newsList').append(str);

            $('.emjon').hide();
            $('.RightCont').scrollTop($('.RightCont')[0].scrollHeight );

            //发送的消息存储到sessionStorage中
            var chatData = sessionStorage.getItem(toName);
            if(chatData != null){
                str = chatData + str;
            }

            sessionStorage.setItem(toName, str);
        });


    });

    /**
     * 用户退出登录
     */
    function logoutChat(){
        $.ajax({
            url:"/home/chat/logout",
            type:'post',
            dateType:'json',
            success:function(res){
                if(res.code == 0){
                    window.location.href = "/home/system/index";
                }else{
                    layer.alert(res.msg, {icon: 5}, function(){
                        window.location.href = "/home/user/login";
                    });
                }
            }
        });
    }






</script>



</html>