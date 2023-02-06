layui.use(['form', 'layer','formSelects'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
    var formSelects = layui.formSelects;

    /**
     * 表单submit监听
     */
    form.on('submit(addOrUpdateUser)',function (data) {
        var index= top.layer.msg("数据提交中,请稍后...",{icon:16,time:false,shade:0.8});
        var url = ctx+"/user/add";//请求的地址
        //判断用户ID是否为空，如果不为空则为更新操作
        if ($("[name='id']").val()){//用户id设置在隐藏域中
            //更新操作
            var url = ctx+"/user/update"
        }
        //data.field ：所有的表单元素的值
        $.post(url,data.field,function (data) {
            if(data.code==200){
                top.layer.msg("操作成功");
                top.layer.close(index);
                layer.closeAll("iframe");
                // 刷新父页面
                parent.location.reload();
            }else{
                layer.msg(data.msg);
            }
        });
        return false;
    });


    /**
     * 下拉框中文本内容
     */
    var userId = $("[name='id']").val();
    formSelects.config("selectId",{//xm-select的值
        type:"post",//请求方式
        searchUrl:ctx+"/role/queryAllRoles?userId="+userId,//请求地址
        keyName:'roleName',//下拉框中文本内容，要与返回的数据中对应的key一致
        keyVal: 'id',
    },true)

    /**
     * 关闭弹出框
     */
    $("#closeBtn").click(function (){
        //当你在iframe页面关闭自身时
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭
    });
});