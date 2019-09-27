!$(function () {
    //初始对象
    var funChat = {
        List: {
            updateList: function (id, list) {
                var o = $("#" + id + " .sidebar-body .list-group");
                //清空列表
                o.empty();
                var str = "";
                for (var i = 0; i < list.length; i++) {
                    str += this.listDom(list[i]);
                }
                o.append(str);
            },
            listDom: function (item) {
                var str = '<li class="list-group-item"';
                var datalist = item.datalist ? item.datalist : [];
                for (var i = 0; i < datalist.length; i++) {
                    if (typeof (datalist[i].key) != "undefined")
                        str += ' data-' + datalist[i].key + '="' + datalist[i].value + '" ';
                }
                str +=
                    '>\n' +
                    '    <div class="avatar-group">\n' +
                    '        <figure class="avatar">\n' +
                    '        <span class="avatar-title bg-' + (item.color ? item.color : 'success') + ' rounded-circle">\n' +
                    '            ' + (item.icon ? item.icon : 'C') + '\n' +
                    '        </span>\n' +
                    '        </figure>\n' +
                    '    </div>\n' +
                    '    <div class="users-list-body">\n' +
                    '        <h5>' + (item.title ? item.title : 'Title') + '</h5>\n' +
                    '        <p>' + (item.description ? item.description : 'Description') + '</p>\n' +
                    '        <div class="users-list-action action-toggle">\n' +
                    '            <div class="dropdown">\n' +
                    '                <a data-toggle="dropdown" href="#">\n' +
                    '                    <i class="ti-more"></i>\n' +
                    '                </a>\n' +
                    '                <div class="dropdown-menu dropdown-menu-right">\n';
                var downlist = item.downlist ? item.downlist : [];
                for (i = 0; i < downlist.length; i++) {
                    if (typeof (downlist[i].text) != "undefined")
                        str += '                    <a href="#" class="dropdown-item" data-list-dropdown="' + downlist[i].value + '">' + downlist[i].text + '</a>\n';
                }
                str +=
                    '                </div>\n' +
                    '            </div>\n' +
                    '        </div>\n' +
                    '    </div>\n' +
                    '</li>';
                return str;
            }
        },
        Utils: {
            jsonAjax: function (url, type, data, func, error_call) {
                if (typeof (func) == "object") {
                    if (typeof (func.success_call) != "function")
                        func.success_call = funChat.Utils.jsonAjaxDefaultFunc.success_call;
                    if (typeof (func.failed_call) != "function")
                        func.failed_call = funChat.Utils.jsonAjaxDefaultFunc.failed_call;
                    if (typeof (func.error_call) != "function")
                        func.error_call = funChat.Utils.jsonAjaxDefaultFunc.error_call;
                    if (typeof (func.other_call) != "function")
                        func.other_call = funChat.Utils.jsonAjaxDefaultFunc.other_call;
                } else {
                    func = funChat.Utils.jsonAjaxDefaultFunc;
                }
                $.ajax({
                    url: url,
                    type: type,
                    data: JSON.stringify(data),
                    contentType: "application/json;charset=UTF-8",
                    dataType: "json",
                    success: function (map) {
                        if (typeof (func) == "object") {
                            if (map.result === "success") {
                                if (typeof (func.success_call) == "function")
                                    func.success_call(map);
                            } else if (map.result === "failed") {
                                if (typeof (func.failed_call) == "function")
                                    func.failed_call(map);
                            } else if (map.result === "error") {
                                if (typeof (func.error_call) == "function")
                                    func.error_call(map);
                            } else {
                                if (typeof (func.other_call) == "function")
                                    func.other_call(map);
                            }
                        }
                    },
                    error: function (e) {
                        if (typeof (error_call) == "function")
                            error_call(e);
                    }
                });
            },
            jsonAjaxDefaultFunc: {
                success_call: function (map) {
                    console.log("jsonAjax sucess.");
                },
                failed_call: function (map) {
                    console.log("jsonAjax failed.");
                },
                error_call: function (map) {
                    console.log("jsonAjax error: " + map.error_info);
                },
                other_call: function (map) {
                    console.log("jsonAjax error: Undefined result");
                }
            },
            randomNum: function (minNum, maxNum) {
                switch (arguments.length) {
                    case 1:
                        return parseInt(Math.random() * minNum + 1, 10);
                    case 2:
                        return parseInt(Math.random() * (maxNum - minNum + 1) + minNum, 10);
                    default:
                        return 0;
                }
            },
            randomLetter: function (minLetter, maxLetter) {
                return String.fromCharCode(64 + this.randomNum(minLetter, maxLetter));
            }
        },
        Started: {
            //是否是小屏幕
            mobile: false,
            //关闭加载页面-减淡消失
            pageLoadingClose: function () {
                $(".page-loading").fadeOut(500, function () {
                    $(this).remove();
                })
            },
            //小屏幕判断
            mobileDetect: function () {
                var w = window.innerWidth;
                this.mobile = w <= 1020;
            },
            //初始化
            init: function () {
                //this.pageLoadingClose();
                this.mobileDetect();
            }
        }
    };

    //启用 tooltip
    $('[data-toggle="tooltip"]').tooltip();

    //初始化
    $(document).ready(function () {
        funChat.Started.init();
    });

    //窗口大小改变
    $(window).on("resize", function () {
        funChat.Started.mobileDetect();
        $(".sidebar-group").removeClass("mobile-open");
        $(".chat+.sidebar-group .sidebar").removeClass("active");
    });

    //点击导航切换 sidebar
    $(document).on("click", "[data-navigation-target]", function () {
        //获取源对象和目标对象
        var e = $(this).data("navigation-target"),
            o = $(".sidebar-group .sidebar#" + e);
        //sidebar 切换
        o.closest(".sidebar-group").find(".sidebar").removeClass("active");
        o.addClass("active");
        //设置 sidebar 内的焦点
        o.find("form input:first").focus();
        //导航切换
        $("[data-navigation-target]").removeClass("active");
        $('[data-navigation-target="' + e + '"]').addClass("active");
        funChat.Started.mobile && ($(".sidebar-group").removeClass("mobile-open"),
            o.closest(".sidebar-group").addClass("mobile-open"));
    });

    //关闭 sidebar
    $(document).on("click", ".sidebar-close", function (e) {
        funChat.Started.mobile ?
            $(".sidebar-group").removeClass("mobile-open") :
            $(this).closest(".sidebar.active").removeClass("active");
    });

    window.funChat = funChat;

});