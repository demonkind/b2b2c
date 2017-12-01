var dtGridColumns = [{
    id: 'categorySort',
    title: '排序',
    type: 'string',
    headerClass: 'text-center width-50',
    columnClass: 'text-center width-50',
    hideType: 'sm|xs',
    fastQuery: true,
    fastQueryType: 'eq'
}, {
    id: 'categoryName',
    title: '分类名称',
    type: 'string',
    headerClass: 'text-left width-150',
    columnClass: 'text-left width-150',
    fastQuery: true,
    fastQueryType: 'lk'
}, {
    id: 'parentName',
    title: '上级分类',
    type: 'string',
    headerClass: 'text-left width-150',
    columnClass: 'text-left width-150',
    fastQuery: false,
    fastSort: false
},  {
    id: 'commisRate',
    title: '分佣比例',
    type: 'string',
    headerClass: 'text-left',
    columnClass: 'text-left',
    hideType: 'md|sm|xs',
    fastQuery: false,
    fastSort: false,
    resolution: function(value, record, column, grid, dataNo, columnNo) {
        return value + "%";
    }
}, {
    id: 'operation',
    title: '操作管理',
    type: 'string',
    columnClass: 'text-center width-300',
    fastSort: false,
    extra: false,
    resolution: function(value, record, column, grid, dataNo, columnNo) {
        var content = '';
        content += '<div class="btn-group">' +
            '<a data-toggle="modal" href="#editModal" class="btn btn-primary btn-sm m-r-10" data-category-id="' + record.categoryId + '" data-commis-rate ="' +record.commisRate+ '" data-rates-id="'+record.ratesId+'" data-rates-id="'+record.isRel+'" data-deep="'+record.deep+'"><i class="fa fa-gears"></i>&nbsp;编辑&nbsp;</a>' +
            '</div>';
        if (record.deep < 3) {
            content += '<a href="javascript:;" data-parent-id="'+record.parentId+'" data-show-children="' + record.categoryId + '" data-deep="'+record.deep+'" class="btn btn-white btn-sm"><i class="fa fa-level-down"></i>&nbsp;下级分类&nbsp;</a>';
        }
        return content;
    }
}];
var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'rates/list.json',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    pageSize: 50,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {
        int: ["categorySort", "parentId"]
    },
    onGridComplete: function(grid) {
        var returnBtn = $("#returnParent");


        $("a[name='edit']").on('click', function() {
            var dataNo = $(this).attr("data-dataNo");
            $('a[data-toggle="modal"]').click();
        });

        /**
         * 查看下一级按钮事件
         */
        $("a[name='showChildren']").on('click', function() {
            var dataNo = $(this).attr("data-dataNo"),
                categoryInfo = grid.exhibitDatas[dataNo];

            category.current.setList(categoryInfo.deep, categoryInfo);
            category.current.gridGoFormParentId(categoryInfo.categoryId);

        });
    }
};
var grid = $.fn.DtGrid.init(dtGridOption);
grid.fastQueryParameters = new Object();
grid.fastQueryParameters['eq_parentId'] = 0;
grid.sortParameter.columnId = 'categoryId';
grid.sortParameter.sortType = 1;

var rates = function() {
    var curr = {};
    /**
     * 根据父级地区id设置grid
     * @param parentId
     */
    function gridGoFormParentId(parentId){
        grid.fastQueryParameters = new Object();
        grid.fastQueryParameters['eq_parentId'] = parentId;
        grid.pager.startRecord= 0;
        grid.pager.nowPage = 1;
        grid.pager.recordCount = -1;
        grid.pager.pageCount = -1;
        grid.refresh(true);
    }

    function goNext(cateId,deep,parentId) {
        curr[deep] = {
            cateId : cateId,
            deep:deep,
            parentId:parentId
        };
        gridGoFormParentId(cateId);
        $("#returnParent").removeClass("hidden");
    }
    function goPre() {
        var l = _getLast();
        if(l){
            gridGoFormParentId(l.parentId);
            l.parentId == 0 && $("#returnParent").addClass("hidden")
        }
        delete curr[l.deep];
    }
    function _getLast() {
        for(var a = 5 ; a > 0 ;a--){
            if(curr[a]){
                return curr[a];
            }
        }
    }
    return {
        goNext:goNext,
        goPre:goPre
    }
}()

$(function() {
    grid.load();
    //对话框显示
    $('#editModal').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget) // Button that triggered the modal
        var dataNo = button.data('dataNo'),
            isRel = button.data("isRel"),
            deep = button.data("deep")
            ;
        //清楚错误信息
        $(".alert-danger").remove();
        $("#editForm").psly().reset();
        $("#commisRate").val(button.data("commisRate"));
        $("#ratesId").val(button.data("ratesId"));
        $("#categoryId").val(button.data("categoryId"));

        $("#isRel").bootstrapSwitch('state',false);

        if(deep >= 3 ){
            $("#isRelPanel").hide();
        }else{
            $("#isRelPanel").show();
        }
    })

    //$("#isRel").bootstrapSwitch('state', siteGlobal.siteState);
    $("#isRel").bootstrapSwitch('state',false);
    //下一级
    $("#dtGridContainer").on("click","[data-show-children]",function() {
        var $this = $(this),
            categoryId = $this.data("showChildren"),
            deep = $this.data("deep"),
            parentId = $this.data("parentId")
            ;
        rates.goNext(categoryId,deep,parentId);
    })
    /**
     * 上一级点击事件
     */
    $("#returnParent").click(function() {
        rates.goPre();
    })
    /**
     * 模糊搜索
     */
        //模糊搜索
    $('#customSearch').click(function () {
        var k =  $.trim($('#keyword').val());
        grid.fastQueryParameters = new Object();
        k == '' ? grid.fastQueryParameters['eq_parentId'] = 0 :grid.fastQueryParameters['lk_categoryName'] = k;
        grid.pager.startRecord= 0;
        grid.pager.nowPage = 1;
        grid.pager.recordCount = -1;
        grid.pager.pageCount = -1;
        grid.refresh(true);
    });
})