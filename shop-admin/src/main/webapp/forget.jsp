<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="/common/script.jsp"></jsp:include>
    <title>Title</title>
    <script>
        function forgetCode(){
            var phone = $("#phone").val();
           $.post(
                "/user/forgetCode.do",
                {"phone":phone},
                function (result){
                    if(result.status==200){
                        var res=result.data;
                        if(res==2){
                            bootbox.alert("发送失败！");
                        }else if(res==3){

                            bootbox.alert("手机号不能为空！");
                        }
                    }else{
                        bootbox.alert("发送失败！");
                    }
                }
            )

        }




        function confirmPw(){

                var phone = $("#phone").val();
                var code = $("#code").val();
                var password = $("#password").val();
                var userPassword = $("#userPassword").val();
                if(password!=null && password==userPassword){
                    $.post(
                        "/user/confirmPw.do",
                        {"phone":phone,"code":code,"password":password},
                        function (result){
                            if(result.status==200){
                                location.href="/login.jsp"

                            }else if(result.status==1001){

                                bootbox.alert("验证码错误！");
                            }else {

                                bootbox.alert("操作失败！");
                            }
                        }
                    )

                }else {

                    bootbox.alert("两次输入密码不一致！");
                }


            }
    </script>
</head>
<body>
<h1 style="margin-left: 670px;margin-top: 70px;">修改密码</h1>
<form class="form-horizontal" style="margin-top: 50px">
    <div class="col-sm-3"></div>
    <div class="form-group">
        <label class="col-sm-2 control-label">注册手机号</label>
        <div class="col-sm-2">
            <input type="text" class="form-control" id="phone" placeholder="注册手机号">
        </div>

    </div>
    <div class="col-sm-3"></div>
    <div class="form-group">
        <label class="col-sm-2 control-label">验证码</label>
        <div class="col-sm-2">
            <input type="text" class="form-control" id="code" placeholder="验证码">
        </div>
        <div class="btn-group" role="group" aria-label="...">
            <button type="button" onclick="forgetCode()" class="btn btn-info">获取验证码</button>
        </div>
    </div>
    <div class="col-sm-3"></div>
    <div class="form-group">
        <label class="col-sm-2 control-label">密码</label>
        <div class="col-sm-2">
            <input type="text" class="form-control" id="password" placeholder="密码">
        </div>
    </div>
    <div class="col-sm-3"></div>
    <div class="form-group">
        <label class="col-sm-2 control-label">确认密码</label>
        <div class="col-sm-2">
            <input type="text" class="form-control" id="userPassword" placeholder="确认密码">
        </div>
    </div>
    <div class="form-group" style="margin-top: 30px">
        <div class="col-sm-offset-5 col-sm-7">
            <div class="btn-group" role="group" aria-label="...">
                <button type="button" onclick="confirmPw()" class="btn btn-success">确认</button>
            </div>&nbsp;&nbsp;&nbsp;&nbsp;
        </div>
    </div>
</form>
</body>
</html>
