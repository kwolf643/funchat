/**
 * 主界面 js 代码
 *
 * 依赖 funchat.js
 */

!$(function () {
    var iNdex = {
        Ws: {
            ws: undefined,
            openWebSocket: function () {
                if (typeof (WebSocket) == "undefined") {
                    console.log("openSocket: 您的浏览器不支持WebSocket");
                } else {
                    console.log("openSocket: 您的浏览器支持WebSocket");
                    var socketUrl = "ws://" + window.location.host + "/websocket?userID=" + $("#userId").val();
                    socketUrl = socketUrl.replace("https", "ws").replace("http", "ws");
                    console.log(socketUrl);
                    socket = new WebSocket(socketUrl);
                    //打开事件
                    socket.onopen = function () {
                        console.log("websocket已打开");
                    };
                    //获得消息事件
                    socket.onmessage = function (msg) {
                        console.log("接收消息");
                        console.log(msg.data);
                        //发现消息进入    开始处理前端触发逻辑
                    };
                    //关闭事件
                    socket.onclose = function () {
                        console.log("websocket已关闭");
                    };
                    //发生了错误事件
                    socket.onerror = function () {
                        console.log("websocket发生了错误");
                    }
                }
            }
        },
        displayGroup: function () {
            funChat.Utils.jsonAjax("/GroupControl", "post",
                {func: "displayGroup"},
                {
                    success_call: function (map) {
                        var list = [];
                        for (var i = 0; i < map.return.length; i++) {
                            var item = {};
                            var o = map.return[i];
                            item.title = o.gropName;
                            item.description = "ID: " + o.gropId + ", 人数: " + o.sum;
                            item.color = "success";
                            item.icon = funChat.Utils.randomLetter(1, 26);
                            item.downlist = [];
                            item.downlist.push({text: "信息", value: "message"});
                            item.downlist.push({text: "删除", value: "delete"});
                            item.datalist = [];
                            item.datalist.push({key: "group-id", value: o.gropId});
                            item.datalist.push({key: "group-name", value: o.gropName});
                            list.push(item);
                        }
                        funChat.List.updateList("chats", list);
                    }
                })
        },
        displayFriend: function () {
            var list = [];
            funChat.Utils.jsonAjax("/FriendControl", "post",
                {func: "getUserFriendsAccept"},
                {
                    success_call: function (map) {
                        for (var i = 0; i < map.return.length; i++) {
                            var item = {};
                            var o = map.return[i];
                            item.title = o.username;
                            item.description = "ID: " + o.id;
                            item.color = funChat.Utils.color.cyan;
                            item.icon = funChat.Utils.randomLetter(1, 26);
                            item.downlist = [];
                            item.downlist.push({text: "删除", value: "delete"});
                            item.datalist = [];
                            item.datalist.push({key: "user-id", value: o.id ? o.id : ""});
                            item.datalist.push({key: "user-name", value: o.username ? o.username : ""});
                            item.datalist.push({key: "user-gender", value: o.geder ? o.geder : ""});
                            item.datalist.push({key: "user-tel", value: o.telephone ? o.telephone : ""});
                            item.datalist.push({key: "user-email", value: o.mail ? o.mail : ""});
                            item.datalist.push({key: "user-addr", value: o.addr ? o.addr : ""});
                            item.datalist.push({key: "user-sign", value: o.perSignature ? o.perSignature : ""});
                            item.datalist.push({key: "user-birthday", value: o.birthday ? o.birthday : ""});
                            item.datalist.push({key: "user-color", value: item.color});
                            item.datalist.push({key: "user-icon", value: item.icon});
                            item.datalist.push({key: "my-toggle", value: "navigation"});
                            item.datalist.push({key: "my-target", value: "contact-information"});
                            item.datalist.push({key: "user-info", value: "contact-information"});
                            list.push(item);
                        }
                        funChat.Utils.jsonAjax("/FriendControl", "post",
                            {func: "getUserWantFriends"}, {
                                success_call: function (map2) {
                                    for (var i = 0; i < map2.return.length; i++) {
                                        var item = {};
                                        var o = map2.return[i];
                                        item.title = "好友申请";
                                        item.description = "名称: " + o.username + ", ID: " + o.id;
                                        item.color = funChat.Utils.color.orange;
                                        item.icon = "Q";
                                        item.downlist = [];
                                        item.downlist.push({text: "接受", value: "accept"});
                                        item.downlist.push({text: "拒绝", value: "reject"});
                                        item.datalist = [];
                                        item.datalist.push({key: "user-id", value: o.id ? o.id : ""});
                                        item.datalist.push({key: "user-name", value: o.username ? o.username : ""});
                                        item.datalist.push({key: "user-gender", value: o.geder ? o.geder : ""});
                                        item.datalist.push({key: "user-tel", value: o.telephone ? o.telephone : ""});
                                        item.datalist.push({key: "user-email", value: o.mail ? o.mail : ""});
                                        item.datalist.push({key: "user-addr", value: o.addr ? o.addr : ""});
                                        item.datalist.push({
                                            key: "user-sign",
                                            value: o.perSignature ? o.perSignature : ""
                                        });
                                        item.datalist.push({key: "user-birthday", value: o.birthday ? o.birthday : ""});
                                        item.datalist.push({key: "user-color", value: item.color});
                                        item.datalist.push({key: "user-icon", value: item.icon});
                                        item.datalist.push({key: "my-toggle", value: "navigation"});
                                        item.datalist.push({key: "my-target", value: "contact-information"});
                                        item.datalist.push({key: "user-info", value: "contact-information"});
                                        list.push(item);
                                    }
                                    funChat.List.updateList("friends", list);
                                }
                            });
                    }
                })
        }
    };

    //初始化
    $(document).ready(function () {
        funChat.Started.pageLoadingClose();
        iNdex.displayGroup();
        iNdex.displayFriend();
    });

    //搜索好友按钮点击
    $(document).on("click", "#addFriends [data-modal-button='search']", function () {
        var search_name = $(this).closest("form").find("input").val();
        funChat.Utils.jsonAjax("/UserControl", "post",
            {func: "searchUser", searchIdOrName: search_name}, {
                success_call: function (map) {
                    var list = [];
                    for (var i = 0; i < map.return.length; i++) {
                        var item = {};
                        var o = map.return[i];
                        item.title = o.username;
                        item.description = "ID: " + o.id;
                        item.color = funChat.Utils.color.green;
                        item.icon = funChat.Utils.randomLetter(1, 26);
                        item.button_text = "添加";
                        item.button_value = "add";
                        item.datalist = [];
                        item.datalist.push({key: "user-id", value: o.id ? o.id : ""});
                        list.push(item);
                    }
                    funChat.Search.updateList("addFriends", list);
                }
            })
    });

    //添加好友按钮点击
    $(document).on("click", "#addFriends [data-list-button='add']", function () {
        var user_id = $(this).closest(".list-group-item").data("user-id");
        funChat.Utils.jsonAjax("/FriendControl", "post",
            {func: "addFriend", friendId: user_id}, {
                success_call: function (map) {
                    alert("发送好友请求成功！");
                },
                failed_call: function (map) {
                    alert("抱歉：发送好友请求失败！");
                }
            })
    });

    //删除好友下拉菜单点击
    $(document).on("click", "#friends [data-list-dropdown='delete']", function (e) {
        var o = $(this).closest(".list-group-item");
        var user_id = o.data("user-id");
        funChat.Utils.jsonAjax("/FriendControl", "post",
            {func: "deleteFriend", friendId: user_id}, {
                success_call: function (map) {
                    o.remove();
                    var oo = $(".sidebar-group .sidebar#contact-information");
                    oo.removeClass("active");
                    oo.closest(".sidebar-group").removeClass("mobile-open");
                },
                failed_call: function (map) {
                    alert("抱歉：删除好友失败！");
                }
            });
        e.stopPropagation();
    });

    //接受好友下拉菜单点击
    $(document).on("click", "#friends [data-list-dropdown='accept']", function (e) {
        var o = $(this).closest(".list-group-item");
        var user_id = o.data("user-id");
        funChat.Utils.jsonAjax("/FriendControl", "post",
            {func: "acceptFriend", friendId: user_id}, {
                success_call: function (map) {
                    iNdex.displayFriend();
                    var oo = $(".sidebar-group .sidebar#contact-information");
                    oo.removeClass("active");
                    oo.closest(".sidebar-group").removeClass("mobile-open");
                },
                failed_call: function (map) {
                    alert("抱歉：接受好友失败！");
                }
            });
        e.stopPropagation();
    });

    //拒绝好友下拉菜单点击
    $(document).on("click", "#friends [data-list-dropdown='reject']", function (e) {
        var o = $(this).closest(".list-group-item");
        var user_id = o.data("user-id");
        funChat.Utils.jsonAjax("/FriendControl", "post",
            {func: "rejectFriend", friendId: user_id}, {
                success_call: function (map) {
                    iNdex.displayFriend();
                    var oo = $(".sidebar-group .sidebar#contact-information");
                    oo.removeClass("active");
                    oo.closest(".sidebar-group").removeClass("mobile-open");
                },
                failed_call: function (map) {
                    alert("抱歉：拒绝好友失败！");
                }
            });
        e.stopPropagation();
    });

    //搜索群聊按钮点击
    $(document).on("click", "#joinGroups [data-modal-button='search']", function () {
        var search_name = $(this).closest("form").find("input").val();
        funChat.Utils.jsonAjax("/GroupControl", "post",
            {func: "searchGroup", searchIdOrName: search_name}, {
                success_call: function (map) {
                    var list = [];
                    for (var i = 0; i < map.return.length; i++) {
                        var item = {};
                        var o = map.return[i];
                        item.title = o.gropName;
                        item.description = "ID: " + o.gropId + ", 人数: " + o.sum;
                        item.color = funChat.Utils.color.green;
                        item.icon = funChat.Utils.randomLetter(1, 26);
                        item.button_text = "加入";
                        item.button_value = "join";
                        item.datalist = [];
                        item.datalist.push({key: "group-id", value: o.gropId ? o.gropId : ""});
                        list.push(item);
                    }
                    funChat.Search.updateList("joinGroups", list);
                }
            })
    });

    //加入群聊按钮点击
    $(document).on("click", "#joinGroups [data-list-button='join']", function () {
        var group_id = $(this).closest(".list-group-item").data("group-id");
        funChat.Utils.jsonAjax("/GroupControl", "post",
            {func: "joinGroup", groupId: group_id},
            {
                success_call: function (map) {
                    alert("加入群聊成功！");
                    $("#joinGroups").modal('hide');
                    iNdex.displayGroup();
                },
                failed_call: function (map) {
                    alert("加入群聊失败！");
                }
            })
    });

    //用户信息显示点击
    $(document).on("click", "[data-user-info]", function () {
        var e = $(this).data("user-info");
        var list = {};
        list.name = $(this).data("user-name");
        list.id = $(this).data("user-id");
        list.color = $(this).data("user-color");
        list.icon = $(this).data("user-icon");
        list.items = [];
        list.items.push({text: "性别", content: $(this).data("user-gender")});
        list.items.push({text: "生日", content: $(this).data("user-birthday")});
        list.items.push({text: "电话", content: $(this).data("user-tel")});
        list.items.push({text: "地址", content: $(this).data("user-addr")});
        list.items.push({text: "邮箱", content: $(this).data("user-email")});
        list.items.push({text: "签名", content: $(this).data("user-sign")});
        funChat.Info.updateInfo(e, list);
    });

    //删除群聊
    $(document).on("click", "#chats [data-list-dropdown='delete']", function () {
        // 这个this指向当前点击对象,this是JS对象的一个特殊指针，它的指向根据环境不同而发生变化
        var o = $(this).closest(".list-group-item");
        var group_id = o.data("group-id");
        //暂时我这么理解：获取当前点击的自定义属性（是jquery对象）并把它转化为JavaScript对象.

        funChat.Utils.jsonAjax("/GroupControl", "post",
            {func: "isOwner", groupId: group_id},
            {
                success_call: function (map) {
                    //删除这个类为 list-group-item 的整个对象
                    //o.remove();
                    alert("您是群主！");
                    funChat.Utils.jsonAjax("/GroupControl", "post",
                        {func: "deleteGroup", groupId: group_id},
                        {
                            success_call: function (map) {
                                o.remove();
                                alert("您已解散本群:"+group_id);
                            }
                        });
                },
                failed_call: function (map) {
                    alert("isOwner: false, 不是群主，没有权限！");
                }
            }
        )
    });


    //弹出群聊信息模态框
    $(document).on("click", ".list-group-item [data-list-dropdown='message']", function () {
        var o = $(this).closest(" .list-group-item");
        var group_id = o.data("group-id");
        //让id为 updateGroupInfoModal 的 模态框 显示出来
        $("#updateGroupInfoModal").modal('show');

        //attr('id',"idname")为id为upgrade_groupName的父元素中最近的的div添加一个id为 group_id
        $("#upgrade_groupName").closest("div").attr('id', group_id);
    });

    //修改群聊名称
    $("#update_group_info_btn").on('click', function () {
        //获取id为 upgrade_groupName 的修改后的group_name
        var group_name = $("#upgrade_groupName").val();
        console.log("从模态框取group_name:" + group_name);
        //attr("id")获取被选元素的id
        var group_id = $("#upgrade_groupName").closest("div").attr("id");

        console.log("获取到的id为：" + group_id);

        funChat.Utils.jsonAjax("/GroupControl", "post",
            {func: "updateGroupInfo", groupId: group_id, groupName: group_name},
            {
                success_call: function (map) {
                    alert("更新群聊信息成功！");
                    //隐藏模态框并把群聊列表重新显示出来
                    $("#updateGroupInfoModal").modal('hide');
                    iNdex.displayGroup();
                },
                failed_call: function (map) {
                    alert("没有权限！");
                }
            }
        )

    });

    //添加群聊
    $(document).on('click', "#newGroup .modal-footer .btn", function () {
        var group_name = $(".form-group #group_name").val();
        //console.log(group_name);
        funChat.Utils.jsonAjax("/GroupControl", "post",
            {func: "createGroup", groupName: group_name},
            {
                success_call: function (map) {
                    alert("创建新的群聊成功！");
                    $("#newGroup").modal('hide');
                    iNdex.displayGroup();
                },
                failed_call: function (map) {
                    alert("参数错误！");
                }
            }
        )
    });
});