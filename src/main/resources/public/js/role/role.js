layui.use(['table','layer'],function() {
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;


    /**
     * 加载数据表格
     */
    var  tableIns = table.render({
            elem: '#roleList',
            url : ctx+'/role/list',
            cellMinWidth : 95,
            page : true,
            height : "full-125",
            limits : [10,15,20,25],
            limit : 10,
            toolbar: "#toolbarDemo",
            id : "roleListTable",
            cols : [[
                {type: "checkbox", fixed:"left", width:50},
                {field: "id", title:'编号',fixed:"true", width:80},
                {field: 'roleName', title: '角色名', minWidth:50, align:"center"},
                {field: 'roleRemark', title: '角色备注', minWidth:100, align:'center'},
                {field: 'createDate', title: '创建时间', align:'center',minWidth:150},
                {field: 'updateDate', title: '更新时间', align:'center',minWidth:150},
                {title: '操作', minWidth:150, templet:'#roleListBar',fixed:"right",align:"center"}
            ]]
        });


    /**
     * 搜索按钮的点击事件
     */
    $(".search_btn").on("click",function () {
        table.reload("roleListTable",{
            page:{
                curr:1
            },
            where:{
                // 角色名
                roleName:$("input[name='roleName']").val()
            }
        })
    });

    /**
     * 监听头部工具栏
     */
    table.on('toolbar(roles)',function (data) {
        if (data.event == "add"){//添加角色
            //打开添加/修改用户的对话框
            openAddOrUpdateRoleDialog();
        }else if(data.event == "grant"){//授权操作
            //获取数据表格选中的记录数据
            var checkStatus = table.checkStatus(data.config.id);
            //打开授权的对话框
            openAddGrantDialog(checkStatus.data);

        }
    });

    /**
     * 打开添加/修改用户的对话框
     */
    function openAddOrUpdateRoleDialog(id) {
        var title="角色管理-添加角色";
        var url=ctx+"/role/toAddOrUpdateRolePage";
        //判断id是否为空，如果为空，则为添加操作，否则是修改操作
        if(id){
            title="角色管理-更新角色";
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
     * 打开授权页面
     * @param data
     */
    function openAddGrantDialog(data){
        if (data.length == 0){
            layer.msg("请选择要授权的角色！",{icon:5})
            return;
        }
        //只支持单个角色授权
        if (data.length > 1){
            layer.msg("暂不支持批量角色授权！",{icon:5})
            return;
        }
        var url = ctx + "/module/toAddGrandPage?roleId=" + data[0].id;
        var title = "<h3>角色授权-角色授权</h3>"
        layui.layer.open({
            title:title,
            content: url,
            type:2,
            area: ["600px","200px"],
            maxmin:true
        })
        }
    /**
     * 行工具栏
     */
    table.on('tool(roles)',function (data) {
        if (data.event == "edit"){//更新角色
            //打开添加/修改用户的对话框
            openAddOrUpdateRoleDialog(data.data.id);
        }else if (data.event == "del"){//删除角色
            deleteRole(data.data.id)
        }
    });

    /**
     * 删除角色
     * @param id
     */
    function deleteRole(roleId) {
        // 弹出确认框，询问用户是否确认删除
        layer.confirm("确认删除当前记录?",{icon: 3, title: "用户管理"},function (index) {
            // 关闭确认框
            layer.close(index);
            // 发送ajax请求，删除记录
            $.ajax({
                type: "post",
                url: ctx+"/role/delete",
                data:{
                    roleId:roleId
                },
                success:function (data){
                    //判断删除结果
                    if(data.code==200){
                        //提示成功
                        layer.msg("删除成功",{icon: 6});
                        //刷新表格
                        tableIns.reload();
                    }else{
                        //提示失败
                        layer.msg(data.msg,{icon:5});
                    }
                }
        });
        });
    }

})