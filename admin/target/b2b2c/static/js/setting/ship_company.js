/**
 * 后台快递公司 JS 依赖 shipCompanyGlobal变量
 * Created by shopnc on 2015/11/24.
 */
//定义表格
var dtGridColumns = [
    {
        id: 'shipLetter',
        title: '首字母',
        type: 'string',
        columnClass: 'text-center width-100',
        fastQuery: true,
        fastQueryType: 'eq',
        hideType:'md|sm|xs'
    },
    {
        id: 'shipName',
        title: '公司名称',
        type: 'string',
        headerClass: 'text-left width-200',
        columnClass: 'text-left width-200',
        fastQuery: true,
        fastQueryType: 'lk',
        fastSort:false
    },
    {
        id: 'shipCode',
        title: '公司编号',
        type: 'string',
        headerClass: 'text-left width-200',
        columnClass: 'text-left width-200',
        fastQuery: true,
        fastQueryType: 'lk',
        fastSort:false,
        hideType:'md|sm|xs'
    },
    {
        id: 'shipUrl',
        title: '公司网址',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
        fastQuery: true,
        fastQueryType: 'lk',
        fastSort:false,
        hideType:'sm|xs'
    },
    {
        id: 'shipState',
        title: '状态',
        type: 'string',
        columnClass: 'text-center width-150',
        fastSort: true,
        fastQuery: true,
        fastQueryType: 'eq',
        codeTable:{
            0:$lang.shopCompany.shipStateState0,
            1:$lang.shopCompany.shipStateState1
        },
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var content = '';
            content += '<input type="checkbox" value="1" name="shipState" data-size="small" data-on-color="primary" data-on-text="启用" data-off-text="关闭"';
            content += 'data-dataNo="' + dataNo + '"';
            if (value == 1) {
                content += ' checked ';
            }
            content += '/>';
            return content;
        }
    },
    {
        id: 'shipType',
        title: '常用快递',
        type: 'number',
        columnClass: 'text-center width-150',
        fastSort: true,
        fastQuery: true,
        fastQueryType: 'eq',
        codeTable:{
            0:$lang.shopCompany.shipTypeState0,
            1:$lang.shopCompany.shipTypeState1
        },
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var content = '';
            content += '<input type="checkbox" value="1" name="shipType" data-size="small" data-on-color="primary" data-on-text="是" data-off-text="否"';
            content += 'data-dataNo="' + dataNo + '"';
            if (value == 1) {
                content += ' checked ';
            }
            content += '/>';
            return content;
        }
    },
];
var dtGridOption = {
    lang : 'zh-cn',
    ajaxLoad : true,
                loadURL : ncGlobal.adminRoot + 'ship_company/list.json',
                exportFileName : '快递公司列表',
                columns : dtGridColumns,
                gridContainer : 'dtGridContainer',
                toolbarContainer : 'dtGridToolBarContainer',
                tools : 'refresh|faseQuery',
                pageSize : 10,
                pageSizeLimit : [10, 20, 50],
                ncColumnsType:{int:["shipState","shipType"]},
                onGridComplete:function(grid){
                    $("[name='shipState']").bootstrapSwitch('onSwitchChange',function(event, state){
                        var dataNo = $(this).attr("data-dataNo");
                        $.ajax({
                            type: 'POST',
                url: ncGlobal.adminRoot + 'ship_company/edit.json',
                dataType:'json',
                data:{shipState:state?1:0,shipId:grid.exhibitDatas[dataNo].shipId}
            }).success(function(result){
                if (result.code == 400) {
                    $.ncAlert({
                        autoCloseTime: 3,
                        content: "保存失败"
                    });
                }
            }).error(function(){
                $.ncAlert({
                    autoCloseTime: 3,
                    content: "请求失败"
                });
            });
        });
        $("[name='shipType']").bootstrapSwitch('onSwitchChange',function(event, state){
            var dataNo = $(this).attr("data-dataNo");
            $.ajax({
                type: 'POST',
                url: ncGlobal.adminRoot + 'ship_company/edit.json',
                dataType:'json',
                data:{shipType:state?1:0,shipId:grid.exhibitDatas[dataNo].shipId}
            }).success(function(result){
                if (result.code == 400) {
                    $.ncAlert({
                        autoCloseTime: 3,
                        content: "保存失败"
                    });
                }
            }).error(function(){
                $.ncAlert({
                    autoCloseTime: 3,
                    content: "请求失败"
                });
            });
        });
    }
};
var grid = $.fn.DtGrid.init(dtGridOption);
grid.sortParameter.columnId = 'shipType';
grid.sortParameter.sortType = 1;
var shipCompany = function(){
    /**
     * 绑定事件
     * @private
     */
    function _bindEvent() {
        $('#customSearch').click(function(){
            grid.fastQueryParameters = new Object();
            grid.fastQueryParameters['lk_shipName'] = $('#keyword').val();
            grid.pager.startRecord= 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        });
    }
    return {
        bindEvent: _bindEvent
    }
}()
$(function(){
    shipCompany.bindEvent();
    grid.load();
});