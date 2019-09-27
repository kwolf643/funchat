/**
 * 登录界面 js 代码
 *
 * 依赖 funchat.js
 */

!$(function () {
    //初始化
    $(document).ready(function () {
        funChat.Started.pageLoadingClose();
    });

    //登录注册切换
    $(document).on("click", "[data-my-login]", function () {
        var e = $(this).data("my-login");
        var o = $("#" + e);
        o.closest(".form-wrapper").find("form").css("display", "none");
        o.css("display", "block");
    });

    //my-button-click 按钮点击
    $(document).on("click", "[data-my-button-click]", function () {
        var e = $(this).data("my-button-click");
        var o = $(this).closest("form");
        var username;
        var password;
        var telephone;
        var email;
        if (e === "login") {
            username = o.find("#username").val();
            password = o.find("#password").val();
            funChat.Utils.jsonAjax("/UserControlPublic", "post",
                {func: "login", username: username, password: password},
                {
                    success_call: function (map) {
                        window.location.replace("/");
                    },
                    failed_call: function () {
                        alert("登陆失败！可能的原因：用户名或密码不正确");
                    }
                });
        } else if (e === "regist") {
            username = o.find("#username").val();
            password = o.find("#password").val();
            telephone = o.find("#telephone").val();
            email = o.find("#email").val();
            alert("123");
            funChat.Utils.jsonAjax("/UserControlPublic", "post",
                {func: "regist", username: username, password: password, telephone: telephone, mail: email},
                {
                    success_call: function (map) {
                        alert("注册成功！");
                        //登录
                        funChat.Utils.jsonAjax("/UserControlPublic", "post",
                            {func: "login", username: map.return.username, password: password},
                            {
                                success_call: function (map) {
                                    window.location.replace("/");
                                },
                                failed_call: function () {
                                    alert("登陆失败！可能的原因：用户名或密码不正确");
                                }
                            });
                    },
                    failed_call: function () {
                        alert("注册失败！可能的原因：用户名已存在/用户名或密码为空");
                    }
                });
        }
    });

});