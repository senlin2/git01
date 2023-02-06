layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    /**
     * 加载数据表格
     */
    var  tableIns = table.render({
        id:'saleChanceTable'
        ,elem: '#saleChanceList'
        ,height: 312
        ,url: ctx + '/sale_chance/list' //数据接口
        ,page: true //开启分页
        ,limit: 10
        ,limits:[10,20,30,40,50]
        ,toolbar: "#toolbarDemo",
        cols : [[
            {type: "checkbox", fixed:"center"},
            {field: "id", title:'编号',fixed:"true"},
            {field: 'chanceSource', title: '机会来源',align:"center"},
            {field: 'customerName', title: '客户名称',  align:'center'},
            {field: 'cgjl', title: '成功几率', align:'center'},
            {field: 'overview', title: '概要', align:'center'},
            {field: 'linkMan', title: '联系人',  align:'center'},
            {field: 'linkPhone', title: '联系电话', align:'center'},
            {field: 'description', title: '描述', align:'center'},
            {field: 'createMan', title: '创建人', align:'center'},
            {field: 'createDate', title: '创建时间', align:'center'},
            {field: 'uname', title: '指派人', align:'center'},
            {field: 'assignTime', title: '分配时间', align:'center'},
            {field: 'state', title: '分配状态', align:'center',templet: function (d){

                    return formatState(d.state);
                }},
            {field: 'devResult', title: '开发状态', align:'center',templet:function (d) {
                    return formatterDevResult(d.devResult);
                }},
            {title: '操作', templet:'#saleChanceListBar',fixed:"right",align:"center", minWidth:150}
        ]]
    });

    /**
     * 格式化分配状态值
     * @param state
     * @returns {undefined}
     */
    function formatState(state) {
        if (state ==0){
            return "<div style='color: yellow'>未分配</div>"
        }else if (state == 1){
            return "<div style='color: green'>已分配</div>"
        }else {
            return "<div style='color: red'>未知</div>"
        }
    }

    /**
     * 格式化开发状态值
     * @param devResult
     * @returns {string}
     */
    function formatterDevResult(devResult) {
        if(devResult==0){
            return "<div style='color: yellow'>未开发</div>";
        }else if(devResult==1){
            return "<div style='color: #00FF00;'>开发中</div>";
        }else if(devResult==2){
            return "<div style='color: #00B83F'>开发成功</div>";
        }else if(devResult==3){
            return "<div style='color: red'>开发失败</div>";
        }else {
            return "<div style='color: #af0000'>未知</div>"
        }
    }

    /**
     * 搜索按钮的点击事件
     */
    $(".search_btn").on("click",function () {
        /**
         * 表格重载
         * 多条件查询
         */
        table.reload("saleChanceListTable",{
            page:{
                curr:1
            },
            where:{
                customerName:$("input[name='customerName']").val(),// 客户名
                createMan:$("input[name='createMan']").val(),// 创建人
                state:$("#state").val()    //分配状态
            }
        })
    });

    /**
     * 头工具栏事件
    */
    table.on('toolbar(saleChances)',function (data) {
        if (data.event == "add"){
            openAddOrUpdateSaleChanceDialog();
        }else if (data.event == "del"){
            delSaleChance(data);
        }
    });

    /**
     * 头工具栏删除营销机会
     * @param data
     */
    function delSaleChance(data) {
        var checkStatus = table.checkStatus("saleChanceTable");
        console.log(data.config.id);
        console.log(checkStatus);
        var saleChanceData = checkStatus.data;
        if (saleChanceData.length == 0) {
            layer.msg("请选择待删除记录!");
            return;
        }

        layer.confirm("确定删除选中的记录", {
            btn: ['确定', '取消']
        }, function (index) {
            layer.close(index);
            // ids=10&ids=20&ids=30
            var ids = "ids=";
            for (var i = 0; i < saleChanceData.length; i++) {
                if (i < saleChanceData.length - 1) {
                    ids = ids + saleChanceData[i].id + "&ids=";
                } else {
                    ids = ids + saleChanceData[i].id;
                }
            }

            $.ajax({
                type: "post",
                url: ctx + "/sale_chance/delete",
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
     * 修改/删除 营销机会数据
     */
    table.on('tool(saleChances)',function (obj) {
        var layEvent =obj.event;
        if(layEvent === "edit"){
            openAddOrUpdateSaleChanceDialog(obj.data.id);
        }else if(layEvent === "del"){
            layer.confirm("确认删除当前记录?",{icon: 3, title: "机会数据管理"},function (index) {
                layer.close(index);
                $.post(ctx+"/sale_chance/delete",{ids:obj.data.id},function (data) {
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

    /**
     * 打开添加或更新对话框
     */
    function openAddOrUpdateSaleChanceDialog(saleChanceId) {
        var title="营销机会管理-机会添加";
        var url=ctx+"/sale_chance/toSaleChancePage";
        if(saleChanceId){
            title="营销机会管理-机会更新";
            url=url+"?saleChanceId="+saleChanceId;
        }
        layui.layer.open({
            title:title,
            type:2,
            area:["500px","620px"],
            maxmin:true,
            content:url
        })
    }
});