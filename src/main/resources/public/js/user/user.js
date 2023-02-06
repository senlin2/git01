layui.use(['table','layer'],function() {
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    /**
     * 加载数据表格
     */
    var  tableIns = table.render({
        id:'userTable'
        ,elem: '#userList'
        ,height: 312
        ,url: ctx + '/user/list' //数据接口
        ,page: true //开启分页
        ,limit: 10
        ,limits:[10,20,30,40,50]
        ,toolbar: "#toolbarDemo",
        cols : [[
            {type: "checkbox", fixed:"center"},
            {field: "id", title:'编号',sort:true,fixed:"left"},
            {field: 'userName', title: '用户名称',align:"center"},
            {field: 'trueName', title: '真实姓名',  align:'center'},
            {field: 'email', title: '用户邮箱', align:'center'},
            {field: 'phone', title: '用户号码', align:'center'},
            {field: 'createDate', title: '创建时间', align:'center'},
            {field: 'updateDate', title: '修改时间', align:'center'},
            {title: '操作', templet:'#userListBar',fixed:"right",align:"center", minWidth:150}
        ]]
    });

    /**
     * 搜索按钮的点击事件
     */
    $(".search_btn").on("click",function () {
        /**
         * 表格重载
         * 多条件查询
         */
        tableIns.reload({
            page:{
                curr:1  //重新从第一页开始
            },
            //设置需要传递给后端的参数
            where:{
                //后端对应参数名   //前端页面获取到的值
                userName:$("[name='userName']").val(),// 用户名
                email:$("[name='email']").val(),// 邮箱
                phone:$("[name='phone']").val()    //手机号
            }
        })
    });

    /**
     * 监听头部工具栏
     */
    table.on('toolbar(users)',function (data) {
        if (data.event == "add"){//添加用户
            //打开添加/修改用户的对话框
            openAddOrUpdateUserDialog();
        }else if (data.event == "del"){//删除用户
            //删除多条用户记录  参数：选中的记录
            deleteUsers(table.checkStatus(data.config.id).data);
        }
    });

    /**
     * 打开添加/修改用户的对话框
     */
    function openAddOrUpdateUserDialog(id) {
        var title="用户管理-添加用户";
        var url=ctx+"/user/toAddOrUpdateUserPage";
        //判断id是否为空，如果为空，则为添加操作，否则是修改操作
        if(id){
            title="用户管理-更新用户";
            url=url+"?id="+id;//传递主键，查询数据
        }
        layui.layer.open({
            title:title,
            type:2,
            area:["650px","400px"],
            maxmin:true,
            content:url
        })
    }

    /**
     * 删除多条用户记录
     * @param checkStatus
     */
    function deleteUsers(userData) {
        var checkStatus = table.checkStatus("userTable");
        if (userData.length == 0) {
            layer.msg("请选择待删除记录!");
            return;
        }
        layer.confirm("确定删除选中的记录吗？", {
            btn: ['确定', '取消']
        }, function (index) {
            layer.close(index);
            // ids=10&ids=20&ids=30
            var ids = "ids=";
            for (var i = 0; i < userData.length; i++) {
                if (i < userData.length - 1) {
                    ids = ids + userData[i].id + "&ids=";
                } else {
                    ids = ids + userData[i].id;
                }
            }

            $.ajax({
                type: "post",
                url: ctx + "/user/delete",
                data: ids,
                dataType: "json",
                success: function (data) {
                    if (data.code == 200) {
                        tableIns.reload();
                    } else {
                        layer.msg(data.msg);
                    }
                }
            })

        })
    }

    /**
     * 行工具栏
     */
    table.on('tool(users)',function (data) {
        if (data.event == "edit"){//更新用户
            //打开添加/修改用户的对话框
            openAddOrUpdateUserDialog(data.data.id);
        }else if (data.event == "del"){//删除用户
            layer.confirm("确认删除当前记录?",{icon: 3, title: "用户管理"},function (index) {
                layer.close(index);
                $.post(ctx+"/user/delete",{ids:data.data.id},function (data) {
                    if(data.code==200){
                        layer.msg("删除成功");
                        tableIns.reload();
                    }else{
                        layer.msg(data.msg);
                    }
                })
            })
        }
    });
});